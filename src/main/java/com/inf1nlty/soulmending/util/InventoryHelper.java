package com.inf1nlty.soulmending.util;

import com.inf1nlty.soulmending.block.SoulMendingBlocks;
import com.inf1nlty.soulmending.item.EmptySoulTotemItem;
import com.inf1nlty.soulmending.item.SoulTotemItem;
import net.minecraft.src.IInventory;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.World;

public class InventoryHelper {

    public static ItemStack convertToSoulTotem(ItemStack stack, int soul) {

        if (stack == null || stack.getItem() != SoulMendingBlocks.emptySoulTotemItem)
            return stack;
        ItemStack totem = EmptySoulTotemItem.convertToSoulTotem(soul);

        if (stack.hasTagCompound() && stack.stackTagCompound.hasKey("ench")) {
            totem.setTagCompound(new NBTTagCompound());
            totem.stackTagCompound.setTag("ench", stack.stackTagCompound.getTagList("ench").copy());
        }
        return totem;
    }

    public static ItemStack convertToEmptySoulTotem(ItemStack stack) {

        if (stack == null || stack.getItem() != SoulMendingBlocks.soulTotemItem)
            return stack;
        ItemStack empty = new ItemStack(SoulMendingBlocks.emptySoulTotemItem);

        if (stack.hasTagCompound() && stack.stackTagCompound.hasKey("ench")) {
            empty.setTagCompound(new NBTTagCompound());
            empty.stackTagCompound.setTag("ench", stack.stackTagCompound.getTagList("ench").copy());
        }
        return empty;
    }

    public static void addSoulToAllTotems(IInventory inventory, World world, int x, int y, int z, int addSoul) {
        boolean addedAny = false;

        int size = inventory.getSizeInventory();
        for (int i = 0; i < size; i++) {
            ItemStack stack = inventory.getStackInSlot(i);

            if (stack != null) {
                ItemStack result = InventoryHelper.addSoulToStack(stack, addSoul);

                if (result != stack) {
                    inventory.setInventorySlotContents(i, result);
                    addedAny = true;

                } else if (stack.getItem() == SoulMendingBlocks.soulTotemItem || stack.getItem() == SoulMendingBlocks.emptySoulTotemItem) {
                    addedAny = true;
                }
            }
        }

        if (addedAny) {
            world.playAuxSFX(2286, x, y, z, 0);
        }
    }

    public static void addSoulToBestTotem(IInventory inventory, World world, int x, int y, int z, int addSoul) {
        int size = inventory.getSizeInventory();
        int maxSoul = -1;
        int bestIndex = -1;

        for (int i = 0; i < size; i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            if (stack != null && stack.getItem() == SoulMendingBlocks.soulTotemItem) {
                int current = SoulTotemItem.getStoredSoul(stack);
                if (current < SoulTotemItem.MAX_SOUL && current > maxSoul) {
                    maxSoul = current;
                    bestIndex = i;
                }
            }
        }

        if (bestIndex == -1) {
            for (int i = 0; i < size; i++) {
                ItemStack stack = inventory.getStackInSlot(i);
                if (stack != null && stack.getItem() == SoulMendingBlocks.emptySoulTotemItem) {
                    bestIndex = i;
                    break;
                }
            }
        }

        if (bestIndex != -1) {
            ItemStack stack = inventory.getStackInSlot(bestIndex);
            ItemStack result = addSoulToStack(stack, addSoul);
            if (result != stack) {
                inventory.setInventorySlotContents(bestIndex, result);
            }
            world.playAuxSFX(2286, x, y, z, 10086);
        }
    }

    public static ItemStack addSoulToStack(ItemStack stack, int addSoul) {
        if (stack == null) return null;

        if (stack.getItem() == SoulMendingBlocks.soulTotemItem) {
            int current = SoulTotemItem.getStoredSoul(stack);
            int newSoul = Math.min(current + addSoul, SoulTotemItem.MAX_SOUL);

            if (newSoul <= 0) {
                return convertToEmptySoulTotem(stack);

            } else {
                SoulTotemItem.setStoredSoul(stack, newSoul);
                return stack;
            }
        }
        else if (stack.getItem() == SoulMendingBlocks.emptySoulTotemItem) {
            if (addSoul > 0) {
                return convertToSoulTotem(stack, addSoul);
            }
            return stack;
        }

        return stack;
    }
}