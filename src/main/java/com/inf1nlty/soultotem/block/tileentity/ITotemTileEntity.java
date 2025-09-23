package com.inf1nlty.soultotem.block.tileentity;

import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagList;

public interface ITotemTileEntity {

    NBTTagList getEnchantTag();
    ItemStack getStackInSlot(int i);
}