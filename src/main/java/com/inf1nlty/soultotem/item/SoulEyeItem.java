package com.inf1nlty.soultotem.item;

import net.minecraft.src.ItemStack;

public class SoulEyeItem extends STItem {

    public SoulEyeItem(int id) {
        super(id);
        this.setUnlocalizedName("soul_eye");
        this.setCreativeTab(net.minecraft.src.CreativeTabs.tabMaterials);
        this.setTextureName("soultotem:soul_eye");
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return true;
    }
}