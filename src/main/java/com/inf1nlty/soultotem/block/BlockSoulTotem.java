package com.inf1nlty.soultotem.block;

import btw.block.util.RayTraceUtils;
import btw.community.soultotem.SoulTotemAddon;
import com.inf1nlty.soultotem.block.tileentity.TileEntitySoulTotem;
import com.inf1nlty.soultotem.item.SoulTotemItem;
import com.inf1nlty.soultotem.util.InventoryHelper;
import net.minecraft.src.*;

import java.util.ArrayList;
import java.util.Random;

public class BlockSoulTotem extends BlockContainer {

    public BlockSoulTotem(int id) {
        super(id, Material.iron);
        this.setHardness(0.0F);
        this.setResistance(1200.0F);
        this.setStepSound(Block.soundClothFootstep);
        this.setUnlocalizedName("soul_totem");
        this.setTextureName("soultotem:soul_totem_particle");
    }

    @Override
    public int idPicked(World world, int x, int y, int z) {
        return STBlocks.soulTotemItem.itemID;
    }

    @Override
    public TileEntity createNewTileEntity(World world) {
        return new TileEntitySoulTotem();
    }

    @Override
    public void onBlockHarvested(World world, int x, int y, int z, int meta, EntityPlayer player) {
        if (world.isRemote) return;

        TileEntitySoulTotem te = (TileEntitySoulTotem) world.getBlockTileEntity(x, y, z);
        if (te != null) {
            te.dropAllItems(world, x, y, z);

            if (player == null || !player.capabilities.isCreativeMode) {
                ItemStack totem;
                if (te.getStoredSoul() <= 0) {
                    totem = InventoryHelper.convertToEmptySoulTotem(te.getTotemAsItemStack());
                } else {
                    totem = new ItemStack(STBlocks.soulTotemItem);
                    SoulTotemItem.setStoredSoul(totem, te.getStoredSoul());
                    NBTTagList ench = te.getEnchantTag();
                    if (ench != null && ench.tagCount() > 0) {
                        if (totem.stackTagCompound == null)
                            totem.stackTagCompound = new NBTTagCompound();
                        totem.stackTagCompound.setTag("ench", ench.copy());
                    }
                }
                EntityItem entityItem = new EntityItem(world, x + 0.5, y + 0.5, z + 0.5, totem);
                world.spawnEntityInWorld(entityItem);
            }
        }
        super.onBlockHarvested(world, x, y, z, meta, player);
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, int blockId, int meta) {
        if (!world.isRemote) {world.playSoundEffect(x + 0.5, y + 0.5, z + 0.5, SoulTotemAddon.TOTEM_BREAK.sound(), 1.0F, 1.0F);
        }
        super.breakBlock(world, x, y, z, blockId, meta);
    }

    @Override
    public int idDropped(int meta, Random random, int fortune) {
        return 0;
    }

    @Override
    public int quantityDropped(Random random) {
        return 0;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        if (world.isRemote) return true;
        TileEntitySoulTotem te = (TileEntitySoulTotem) world.getBlockTileEntity(x, y, z);
        if (te == null) return false;

        ItemStack stackInTotem = te.getStackInSlot(0);
        ItemStack held = player.getCurrentEquippedItem();

        if (stackInTotem != null && (held == null || held.stackSize == 0) && te.interactCooldown == 0) {
            player.inventory.addItemStackToInventory(stackInTotem);
            te.setInventorySlotContents(0, null);
            te.interactCooldown = 10;

            if (player instanceof EntityPlayerMP) {
                ((EntityPlayerMP)player).sendContainerToPlayer(player.inventoryContainer);
            }
            return true;
        }

        else if (stackInTotem == null && held != null && held.stackSize > 0 && te.interactCooldown == 0) {
            ItemStack toStore = held.copy();
            toStore.stackSize = 1;
            te.setInventorySlotContents(0, toStore);
            player.inventory.decrStackSize(player.inventory.currentItem, 1);
            te.interactCooldown = 1;
            return true;
        }

        return false;
    }

    @Override
    public int getRenderType() {
        return -1;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public int getFacing(int meta) {
        return meta;
    }

    @Override
    public int setFacing(int meta, int facing) {
        return facing;
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
        return this.getCollisionBoundingBoxFromPool(world, x, y, z);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        return AxisAlignedBB.getAABBPool().getAABB(
                x + 0.1875D, y + 0.0D, z + 0.25D,
                x + 0.8125D, y + 1.0D, z + 0.75D
        );
    }

    @Override
    @SuppressWarnings("deprecation")
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
        this.setBlockBounds(0.1875F, 0.0F, 0.25F, 0.8125F, 1.0F, 0.75F);
    }

    @Override
    public MovingObjectPosition collisionRayTrace(World world, int x, int y, int z, Vec3 start, Vec3 end) {
        RayTraceUtils rayTrace = new RayTraceUtils(world, x, y, z, start, end);
        rayTrace.addBoxWithLocalCoordsToIntersectionList(
                0.1875, 0.0, 0.25,
                0.8125, 1.0, 0.75);
        return rayTrace.getFirstIntersection();
    }
}