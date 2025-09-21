package com.inf1nlty.soulmending.item;

import net.minecraft.src.ItemStack;

public class SoulEmeraldItem extends SMItem {

    public SoulEmeraldItem(int id) {
        super(id);
        this.setUnlocalizedName("soul_emerald");
        this.setCreativeTab(net.minecraft.src.CreativeTabs.tabMaterials);
        this.setTextureName("soulmending:soul_emerald");
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return true;
    }
}