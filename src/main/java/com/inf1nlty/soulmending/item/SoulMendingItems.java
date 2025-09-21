package com.inf1nlty.soulmending.item;

import net.minecraft.src.Item;

public class SoulMendingItems {

    public static Item soul_emerald;

    public static void initSMItems() {

        soul_emerald = new SoulEmeraldItem(3700).setUnlocalizedName("soul_emerald").setCreativeTab(net.minecraft.src.CreativeTabs.tabMaterials).setTextureName("soulmending:soul_emerald");

    }
}