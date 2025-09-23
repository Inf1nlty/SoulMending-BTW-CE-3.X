package com.inf1nlty.soultotem;

import btw.item.BTWItems;
import com.inf1nlty.soultotem.item.EmptySoulTotemItem;
import com.inf1nlty.soultotem.item.SoulTotemItem;
import net.minecraft.src.*;

public class STEnchantments extends Enchantment {

    public static STEnchantments soulMending;
    public static final int SOUL_MENDING_ID = 100;
    public static ItemStack arcaneSoulMendingScroll;

    public STEnchantments(int id, int weight) {
        super(id, weight, EnumEnchantmentType.all);
        this.setName("soul_mending");
        this.canBeAppliedByVanillaEnchanter = true;
    }

    public static void registerEnchantment() {
        soulMending = new STEnchantments(SOUL_MENDING_ID, 5);
        arcaneSoulMendingScroll = new ItemStack(BTWItems.arcaneScroll, 1, SOUL_MENDING_ID);
    }

    @Override
    public boolean canBeAppliedByVanillaEnchanter() {
        return true;
    }

    @Override
    public boolean canApply(ItemStack stack) {
        if (stack == null) return false;
        Item item = stack.getItem();
        return item instanceof SoulTotemItem || item instanceof EmptySoulTotemItem;
    }

    public int getMaxLevel() {
        return 5;
    }
}