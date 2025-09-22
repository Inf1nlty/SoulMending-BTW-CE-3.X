package com.inf1nlty.soulmending.block.tileentity;

import btw.block.tileentity.TileEntityDataPacketHandler;
import com.inf1nlty.soulmending.block.SoulMendingBlocks;
import com.inf1nlty.soulmending.util.ISoulPossessable;
import net.minecraft.src.*;

public class TileEntityEmptySoulTotem extends TileEntity implements TileEntityDataPacketHandler, ISoulPossessable, ITotemTileEntity {

    private NBTTagList enchantTag = null;
    private transient boolean suppressNextDrop = false;

    public void setEnchantTag(NBTTagList tag) {
        this.enchantTag = tag;
    }

    public NBTTagList getEnchantTag() {
        return this.enchantTag;
    }

    @Override
    public ItemStack getStackInSlot(int i) {
        return null;
    }

    public void suppressNextDrop() {
        this.suppressNextDrop = true;
    }

    @SuppressWarnings("unused")
    public boolean shouldSuppressDrop() {
        return this.suppressNextDrop;
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (!worldObj.isRemote && worldObj.provider.dimensionId == -1 && worldObj.rand.nextInt(1000) == 0) {
            this.transformToSoulTotem(10);
        }
    }

    public void transformToSoulTotem(int soul) {
        if (soul > 0 && worldObj != null && !worldObj.isRemote) {
            this.suppressNextDrop();
            worldObj.setBlock(xCoord, yCoord, zCoord, SoulMendingBlocks.soulTotem.blockID, 0, 3);
            TileEntity te = worldObj.getBlockTileEntity(xCoord, yCoord, zCoord);

            if (te instanceof TileEntitySoulTotem) {
                ((TileEntitySoulTotem) te).setStoredSoul(soul);

                if (this.enchantTag != null)
                    ((TileEntitySoulTotem) te).setEnchantTag((NBTTagList) this.enchantTag.copy());
                worldObj.playAuxSFX(2286, xCoord, yCoord, zCoord, 10010);
            }
        }
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound tag = new NBTTagCompound();

        if (enchantTag != null) {
            tag.setTag("ench", enchantTag);
        }

        return new Packet132TileEntityData(xCoord, yCoord, zCoord, 1, tag);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        if (enchantTag != null) {
            tag.setTag("ench", enchantTag);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        this.enchantTag = tag.hasKey("ench") ? tag.getTagList("ench") : null;
    }

    @Override
    public void readNBTFromPacket(NBTTagCompound tag) {
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
    public void soulMending$onSoulPossession() {
        this.transformToSoulTotem(10);
    }
}