package com.inf1nlty.soulmending.item;

import btw.util.MiscUtils;
import com.inf1nlty.soulmending.EnchantmentSoulMending;
import com.inf1nlty.soulmending.block.SoulMendingBlocks;
import com.inf1nlty.soulmending.block.tileentity.TileEntitySoulTotem;
import net.minecraft.src.*;
import java.util.List;

public class SoulTotemItem extends SMItem {
    public static final int MAX_SOUL = 10000;

    public SoulTotemItem(int id) {
        super(id);
        this.maxStackSize = 1;
        this.setCreativeTab(CreativeTabs.tabMisc);
        this.setUnlocalizedName("soul_totem");
        this.setTextureName("soulmending:soul_totem");
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
    public void getSubItems(int id, CreativeTabs tab, List list) {
        ItemStack full = new ItemStack(this);
        setStoredSoul(full, MAX_SOUL);
        list.add(full);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void addInformation(ItemStack stack, EntityPlayer player, List info, boolean advanced) {

        int storedSoul = getStoredSoul(stack);
        float percent = (float) storedSoul / (float) MAX_SOUL;
        String color;

        if (percent >= 0.75f) {
            color = "§a";

        } else if (percent >= 0.5f) {
            color = "§e";

        } else if (percent >= 0.25f) {
            color = "§6";

        } else {
            color = "§c";
        }

        String colorMax = "§7" + MAX_SOUL + "§f";
        info.add(String.format(StatCollector.translateToLocal("soul_totem.tooltip"), color + storedSoul + "§f", colorMax));
    }

    public static int getStoredSoul(ItemStack stack) {
        NBTTagCompound tag = stack.stackTagCompound;
        return tag != null ? tag.getInteger("StoredSoul") : 0;
    }

    public static void setStoredSoul(ItemStack stack, int soul) {
        NBTTagCompound tag = stack.stackTagCompound;
        if (tag == null) {
            tag = new NBTTagCompound();
            stack.setTagCompound(tag);
        }
        tag.setInteger("StoredSoul", soul);
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

        world.setBlock(placeX, placeY, placeZ, SoulMendingBlocks.soulTotem.blockID, facing, 3);
        TileEntity te = world.getBlockTileEntity(placeX, placeY, placeZ);

        if (te instanceof TileEntitySoulTotem) {
            int soul = getStoredSoul(stack);
            ((TileEntitySoulTotem) te).setStoredSoul(soul);

            if (stack.hasTagCompound() && stack.stackTagCompound.hasKey("ench")) {
                ((TileEntitySoulTotem) te).setEnchantTag(stack.stackTagCompound.getTagList("ench"));
            }
        }
        world.playSoundEffect(placeX + 0.5, placeY + 0.5, placeZ + 0.5, "step.cloth", 0.7F, 1.0F);

        if (!player.capabilities.isCreativeMode) {
            --stack.stackSize;
        }

        return true;
    }
}