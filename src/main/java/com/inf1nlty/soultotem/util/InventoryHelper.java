package com.inf1nlty.soultotem.util;

import btw.community.soultotem.SoulTotemAddon;
import com.inf1nlty.soultotem.block.STBlocks;
import com.inf1nlty.soultotem.item.EmptySoulTotemItem;
import com.inf1nlty.soultotem.item.SoulTotemItem;
import com.inf1nlty.soultotem.network.TotemParticleNet;
import net.minecraft.src.*;

public class InventoryHelper {

    public static ItemStack convertToSoulTotem(ItemStack stack, int soul) {

        if (stack == null || stack.getItem() != STBlocks.emptySoulTotemItem)
            return stack;
        ItemStack totem = EmptySoulTotemItem.convertToSoulTotem(soul);

        if (stack.hasTagCompound() && stack.stackTagCompound.hasKey("ench")) {
            totem.setTagCompound(new NBTTagCompound());
            totem.stackTagCompound.setTag("ench", stack.stackTagCompound.getTagList("ench").copy());
        }
        return totem;
    }

    public static ItemStack convertToEmptySoulTotem(ItemStack stack) {

        if (stack == null || stack.getItem() != STBlocks.soulTotemItem)
            return stack;
        ItemStack empty = new ItemStack(STBlocks.emptySoulTotemItem);

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

                } else if (stack.getItem() == STBlocks.soulTotemItem || stack.getItem() == STBlocks.emptySoulTotemItem) {
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
            if (stack != null && stack.getItem() == STBlocks.soulTotemItem) {
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
                if (stack != null && stack.getItem() == STBlocks.emptySoulTotemItem) {
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

        if (stack.getItem() == STBlocks.soulTotemItem) {

            int current = SoulTotemItem.getStoredSoul(stack);
            int newSoul = Math.min(current + addSoul, SoulTotemItem.MAX_SOUL);

            if (newSoul <= 0) {
                return convertToEmptySoulTotem(stack);

            } else {
                SoulTotemItem.setStoredSoul(stack, newSoul);
                return stack;
            }
        }

        else if (stack.getItem() == STBlocks.emptySoulTotemItem) {
            if (addSoul > 0) {
                return convertToSoulTotem(stack, addSoul);
            }
            return stack;
        }

        return stack;
    }

    public static boolean trySoulTotemRevive(EntityPlayer player) {

        IInventory inv = player.inventory;
        int bestIndex = -1;
        int maxSoul = -1;

        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack != null && stack.getItem() == STBlocks.soulTotemItem) {
                int soul = SoulTotemItem.getStoredSoul(stack);
                if (soul >= 1 && soul > maxSoul) {
                    maxSoul = soul;
                    bestIndex = i;
                }
            }
        }

        if (bestIndex != -1) {
            ItemStack stack = inv.getStackInSlot(bestIndex);

            int newSoul = maxSoul - 1;

            if (newSoul <= 0) {
                ItemStack empty = InventoryHelper.convertToEmptySoulTotem(stack);
                inv.setInventorySlotContents(bestIndex, empty);

            } else {
                SoulTotemItem.setStoredSoul(stack, newSoul);
            }

            player.addPotionEffect(new PotionEffect(Potion.field_76444_x.id, 20*10, 4)); // Absorption V, 10s
            player.addPotionEffect(new PotionEffect(Potion.regeneration.id, 20*10, 1)); // Regeneration II, 10s
            player.addPotionEffect(new PotionEffect(Potion.resistance.id, 20*10, 2));   // Resistance III, 10s
            player.worldObj.playSoundAtEntity(player, SoulTotemAddon.SOUL_TOTEM_USE.sound(), 1.0F, 1.0F);

            if (!player.worldObj.isRemote && player instanceof EntityPlayerMP) {
                TotemParticleNet.sendTotemRevive((EntityPlayerMP)player, player.posX, player.posY + 1.0, player.posZ);
            }
            return true;
        }

        return false;
    }
}