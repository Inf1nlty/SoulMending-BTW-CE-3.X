package com.inf1nlty.soulmending.util;

import com.inf1nlty.soulmending.EnchantmentSoulMending;
import net.minecraft.src.*;

public class SoulMendingHelper {

    public static boolean hasSoulMending(ItemStack stack) {
        return EnchantmentHelper.getEnchantmentLevel(EnchantmentSoulMending.SOUL_MENDING_ID, stack) > 0;
    }

    public static int trySoulMending(ItemStack equip, ItemStack totem, int storedSoul) {
        if (equip != null && totem != null
                && hasSoulMending(totem)
                && equip.isItemDamaged()
                && storedSoul > 0) {
            int level = EnchantmentHelper.getEnchantmentLevel(EnchantmentSoulMending.SOUL_MENDING_ID, totem);
            int soulNeeded = getSoulNeeded(level);
            if (storedSoul >= soulNeeded) {
                equip.setItemDamage(equip.getItemDamage() - 1);
                return storedSoul - soulNeeded;
            }
        }
        return storedSoul;
    }

    private static int getSoulNeeded(int level) {
        return switch (level) {
            case 2 -> 8;
            case 3 -> 6;
            case 4 -> 4;
            case 5 -> 2;
            default -> 10;
        };
    }
}