package com.inf1nlty.soultotem.item;

import net.minecraft.src.ItemStack;

public class SoulEmeraldItem extends STItem {

    public SoulEmeraldItem(int id) {
        super(id);
        this.setUnlocalizedName("soul_emerald");
        this.setCreativeTab(net.minecraft.src.CreativeTabs.tabMaterials);
        this.setTextureName("soultotem:soul_emerald");
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return true;
    }
}