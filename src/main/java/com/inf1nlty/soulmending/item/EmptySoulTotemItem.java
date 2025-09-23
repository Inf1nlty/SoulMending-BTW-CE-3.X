package com.inf1nlty.soulmending.item;

import btw.util.MiscUtils;
import com.inf1nlty.soulmending.EnchantmentSoulMending;
import com.inf1nlty.soulmending.block.SoulMendingBlocks;
import com.inf1nlty.soulmending.block.tileentity.TileEntityEmptySoulTotem;
import net.minecraft.src.*;

import java.util.List;

public class EmptySoulTotemItem extends SMItem {

    public EmptySoulTotemItem(int id) {
        super(id);
        this.maxStackSize = 1;
        this.setCreativeTab(CreativeTabs.tabMisc);
        this.setUnlocalizedName("empty_soul_totem");
        this.setTextureName("soulmending:empty_soul_totem");
    }

    @Override
    public boolean isEnchantmentApplicable(Enchantment enchantment) {
        return enchantment instanceof EnchantmentSoulMending;
    }

    @Override
    public int getInfernalMaxEnchantmentCost() {
        return 30;
    }

    @Override
    public int getInfernalMaxNumEnchants() {
        return 1;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void addInformation(ItemStack stack, EntityPlayer player, List info, boolean advanced) {
        info.add(StatCollector.translateToLocal("empty_soul_totem.tooltip"));
    }

    public static ItemStack convertToSoulTotem(int soul) {
        ItemStack totem = new ItemStack(SoulMendingBlocks.soulTotemItem);
        SoulTotemItem.setStoredSoul(totem, soul);
        return totem;
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        int placeX = x, placeY = y, placeZ = z;
        switch (side) {
            case 0: --placeY; break;
            case 1: ++placeY; break;
            case 2: --placeZ; break;
            case 3: ++placeZ; break;
            case 4: --placeX; break;
            case 5: ++placeX; break;
        }

        if (!world.isAirBlock(placeX, placeY, placeZ))
            return false;

        int facing = MiscUtils.convertPlacingEntityOrientationToBlockFacingReversed(player);

        if (facing == 0 || facing == 1) {
            facing = MiscUtils.convertOrientationToFlatBlockFacingReversed(player);
        }

        world.setBlock(placeX, placeY, placeZ, SoulMendingBlocks.emptySoulTotem.blockID, facing, 3);
        TileEntity te = world.getBlockTileEntity(placeX, placeY, placeZ);

        if (te instanceof TileEntityEmptySoulTotem) {
            if (stack.hasTagCompound() && stack.stackTagCompound.hasKey("ench")) {
                ((TileEntityEmptySoulTotem) te).setEnchantTag(stack.stackTagCompound.getTagList("ench"));
            }
        }
        world.playSoundEffect(placeX + 0.5, placeY + 0.5, placeZ + 0.5, "step.cloth", 0.7F, 1.0F);

        if (!player.capabilities.isCreativeMode) {
            --stack.stackSize;
        }
        return true;
    }

}