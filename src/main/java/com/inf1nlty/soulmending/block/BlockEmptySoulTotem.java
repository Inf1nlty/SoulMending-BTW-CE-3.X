package com.inf1nlty.soulmending.block;

import btw.block.model.BlockModel;
import com.inf1nlty.soulmending.block.tileentity.TileEntityEmptySoulTotem;
import com.inf1nlty.soulmending.client.SoulTotemModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.*;

public class BlockEmptySoulTotem extends BlockContainer implements IBlockModelProvider {

    private final BlockModel modelSoulTotem = new SoulTotemModel();

    public BlockEmptySoulTotem(int id) {
        super(id, Material.iron);
        this.setHardness(0.0F);
        this.setResistance(1200.0F);
        this.setStepSound(Block.soundClothFootstep);
        this.setUnlocalizedName("empty_soul_totem");
        this.setTextureName("soulmending:empty_soul_totem");
    }

    @Override
    public int idPicked(World world, int x, int y, int z) {
        return SoulMendingBlocks.emptySoulTotemItem.itemID;
    }

    @Override
    public TileEntity createNewTileEntity(World world) {
        return new TileEntityEmptySoulTotem();
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, int blockId, int meta) {
        TileEntityEmptySoulTotem te = (TileEntityEmptySoulTotem) world.getBlockTileEntity(x, y, z);
        boolean drop = te == null || !te.shouldSuppressDrop();

        if (drop) {
            boolean dropTotem = true;
            EntityPlayer breaker = world.getClosestPlayer(x + 0.5, y + 0.5, z + 0.5, 8.0);

            if (breaker != null && breaker.capabilities.isCreativeMode) {
                dropTotem = false;
            }

            if (dropTotem && !world.isRemote) {
                ItemStack empty = new ItemStack(SoulMendingBlocks.emptySoulTotemItem);
                if (te != null && te.getEnchantTag() != null && te.getEnchantTag().tagCount() > 0) {
                    if (empty.stackTagCompound == null) empty.stackTagCompound = new NBTTagCompound();
                    empty.stackTagCompound.setTag("ench", te.getEnchantTag().copy());
                }
                EntityItem entityItem = new EntityItem(world, x + 0.5, y + 0.5, z + 0.5, empty);
                world.spawnEntityInWorld(entityItem);
            }
        }
        super.breakBlock(world, x, y, z, blockId, meta);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
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
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        return AxisAlignedBB.getAABBPool().getAABB(
                x + 0.1875D, y + 0.0D,   z + 0.25D,
                x + 0.8125D, y + 1.0D,   z + 0.75D
        );
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
        return this.getCollisionBoundingBoxFromPool(world, x, y, z);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
        this.setBlockBounds(0.1875F, 0.0F, 0.25F, 0.8125F, 1.0F, 0.75F);
    }

    @Environment(EnvType.CLIENT)
    public BlockModel getBlockModel() {
        return modelSoulTotem;
    }
}