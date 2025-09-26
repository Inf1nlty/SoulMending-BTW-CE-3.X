package com.inf1nlty.soultotem.block.tileentity;

import btw.block.tileentity.TileEntityDataPacketHandler;
import btw.community.soultotem.SoulTotemAddon;
import com.inf1nlty.soultotem.STEnchantments;
import com.inf1nlty.soultotem.block.STBlocks;
import com.inf1nlty.soultotem.client.EntitySoulFX;
import com.inf1nlty.soultotem.item.SoulTotemItem;
import com.inf1nlty.soultotem.util.SoulMendingHelper;
import com.inf1nlty.soultotem.util.ISoulPossessable;
import net.minecraft.src.*;

public class TileEntitySoulTotem extends TileEntity implements IInventory, TileEntityDataPacketHandler, ISoulPossessable, ITotemTileEntity {

    private ItemStack[] inventory = new ItemStack[1];
    private int storedSoul = 0;
    public int interactCooldown = 0;
    public int particleCooldown = 0;
    private NBTTagList enchantTag = null;

    public ItemStack getTotemAsItemStack() {
        ItemStack fake = new ItemStack(STBlocks.soulTotemItem);
        if (this.enchantTag != null && this.enchantTag.tagCount() > 0) {
            if (fake.stackTagCompound == null)
                fake.stackTagCompound = new NBTTagCompound();
            fake.stackTagCompound.setTag("ench", this.enchantTag.copy());
        }
        return fake;
    }

    @Override
    public void updateEntity() {

        if (interactCooldown > 0) interactCooldown--;
        if (particleCooldown > 0) particleCooldown--;

        if (!worldObj.isRemote && worldObj.provider.dimensionId == -1 && worldObj.rand.nextInt(1000) == 0) {
            absorbSoul(10);
        }

        if (!worldObj.isRemote) {
            ItemStack equip = inventory[0];
            ItemStack totem = getTotemAsItemStack();

            int newStoredSoul = SoulMendingHelper.trySoulMending(equip, totem, storedSoul);
            if (newStoredSoul != storedSoul) {
                int level = EnchantmentHelper.getEnchantmentLevel(STEnchantments.SOUL_MENDING_ID, totem);
                int cooldownTicks = switch (level) {
                    case 2 -> 8;
                    case 3 -> 6;
                    case 4 -> 4;
                    case 5 -> 2;
                    default -> 10;
                };

                if (particleCooldown <= 0) {
                    if (worldObj.isRemote) {
                        EntitySoulFX.spawnRing(worldObj, xCoord + 0.5, yCoord, zCoord + 0.5);
                    }
                    worldObj.playAuxSFX(10086, xCoord, yCoord, zCoord, 10000);
                    worldObj.playSoundEffect(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, SoulTotemAddon.SOUL_TOTEM_FIX.sound(), 1.0F, 1.0F);
                    particleCooldown = cooldownTicks;
                }
                setStoredSoul(newStoredSoul);
            }

            if (equip != null && equip.isItemDamaged() && totem != null && SoulMendingHelper.hasSoulMending(totem)) {
                onInventoryChanged();
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        tag.setInteger("StoredSoul", storedSoul);
        NBTTagList list = new NBTTagList();
        for (int i = 0; i < inventory.length; i++) {
            if (inventory[i] != null) {
                NBTTagCompound slotTag = new NBTTagCompound();
                slotTag.setByte("Slot", (byte) i);
                inventory[i].writeToNBT(slotTag);
                list.appendTag(slotTag);
            }
        }
        if (enchantTag != null) {
            tag.setTag("ench", enchantTag);
        }
        tag.setTag("Items", list);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        this.storedSoul = tag.getInteger("StoredSoul");
        NBTTagList list = tag.getTagList("Items");
        inventory = new ItemStack[1];
        for (int i = 0; i < list.tagCount(); i++) {
            NBTTagCompound slotTag = (NBTTagCompound) list.tagAt(i);
            int slot = slotTag.getByte("Slot");
            if (slot >= 0 && slot < inventory.length) {
                inventory[slot] = ItemStack.loadItemStackFromNBT(slotTag);
            }
        }
        if (tag.hasKey("ench")) {
            this.enchantTag = tag.getTagList("ench");
        } else {
            this.enchantTag = null;
        }
    }

    public void setEnchantTag(NBTTagList tag) {
        this.enchantTag = tag;
    }

    public NBTTagList getEnchantTag() {
        return this.enchantTag;
    }

    public void dropAllItems(World world, int x, int y, int z) {
        for (ItemStack stack : inventory) {
            if (stack != null) {
                float rx = world.rand.nextFloat() * 0.8F + 0.1F;
                float ry = world.rand.nextFloat() * 0.8F + 0.1F;
                float rz = world.rand.nextFloat() * 0.8F + 0.1F;
                ItemStack copy = stack.copy();

                EntityItem entityItem = new EntityItem(world, x + rx, y + ry, z + rz, copy);
                world.spawnEntityInWorld(entityItem);
            }
        }
    }

    @Override
    public int getSizeInventory() {
        return 1;
    }

    @Override
    public ItemStack getStackInSlot(int i) {
        return inventory[i];
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("StoredSoul", this.storedSoul);

        if (inventory[0] != null) {
            NBTTagCompound itemTag = new NBTTagCompound();
            inventory[0].writeToNBT(itemTag);
            tag.setCompoundTag("x", itemTag);
        }

        if (enchantTag != null) {
            tag.setTag("ench", enchantTag);
        }

        return new Packet132TileEntityData(xCoord, yCoord, zCoord, 1, tag);
    }

    @Override
    public void readNBTFromPacket(NBTTagCompound tag) {

        if (tag.hasKey("StoredSoul")) {
            this.storedSoul = tag.getInteger("StoredSoul");
        }

        NBTTagCompound itemTag = tag.getCompoundTag("x");

        if (itemTag != null) {
            inventory[0] = ItemStack.loadItemStackFromNBT(itemTag);

        } else {
            inventory[0] = null;
        }

        if (tag.hasKey("ench")) {
            this.enchantTag = tag.getTagList("ench");

        } else {
            this.enchantTag = null;
        }

        if (worldObj != null) {
            worldObj.markBlockRangeForRenderUpdate(xCoord, yCoord, zCoord, xCoord, yCoord, zCoord);
        }
    }

    @Override
    public ItemStack decrStackSize(int i, int count) {
        if (inventory[i] != null) {
            ItemStack itemstack;

            if (inventory[i].stackSize <= count) {
                itemstack = inventory[i];
                inventory[i] = null;
                onInventoryChanged();

            } else {
                itemstack = inventory[i].splitStack(count);
                if (inventory[i].stackSize == 0) inventory[i] = null;
                onInventoryChanged();
            }
            return itemstack;
        }

        return null;
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack stack) {
        inventory[i] = stack;
        onInventoryChanged();
    }


    public void setStoredSoul(int soul) {
        this.storedSoul = soul;
        onInventoryChanged();
    }

    public int getStoredSoul() {
        return storedSoul;
    }

    @Override
    public String getInvName() {
        return "tile.soul_totem.name";
    }

    @Override
    public boolean isInvNameLocalized() {
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public void onInventoryChanged() {
        super.onInventoryChanged();
        if (worldObj != null && !worldObj.isRemote)
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) == this
                && player.getDistanceSq((double)xCoord + 0.5D, (double)yCoord + 0.5D, (double)zCoord + 0.5D) <= 64.0D;
    }

    @Override
    public void openChest() {
    }

    @Override
    public void closeChest() {
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemStack) {
        return true;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int i) {
        return null;
    }

    public void attemptToAddSingleItemFromStack(ItemStack stack) {
        if (inventory[0] == null && stack != null) {
            ItemStack copy = stack.copy();
            copy.stackSize = 1;
            inventory[0] = copy;
            onInventoryChanged();
        }
    }

    public void absorbSoul(int soul) {
        if (soul > 0 && worldObj != null && !worldObj.isRemote) {
            int maxSoul = SoulTotemItem.MAX_SOUL;
            int current = this.getStoredSoul();
            int after = Math.min(current + soul, maxSoul);
            if (after != current) {
                this.setStoredSoul(after);
                worldObj.playAuxSFX(2286, xCoord, yCoord, zCoord, 10010);
            }
        }
    }

    @Override
    public void soulMending$onSoulPossession() {
        if (worldObj != null && !worldObj.isRemote) {
            absorbSoul(10);
        }
    }
}