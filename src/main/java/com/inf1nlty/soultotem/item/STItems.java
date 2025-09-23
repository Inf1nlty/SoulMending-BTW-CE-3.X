package com.inf1nlty.soultotem.item;

import net.minecraft.src.Item;

public class STItems {

    public static Item soul_emerald;

    public static void initSMItems() {

        soul_emerald = new SoulEmeraldItem(3700).setUnlocalizedName("soul_emerald").setCreativeTab(net.minecraft.src.CreativeTabs.tabMaterials).setTextureName("soultotem:soul_emerald");

    }
}