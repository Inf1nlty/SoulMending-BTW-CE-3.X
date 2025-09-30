package com.inf1nlty.soultotem.init;

import btw.crafting.recipe.RecipeManager;
import btw.entity.mob.villager.trade.TradeItem;
import btw.entity.mob.villager.trade.TradeProvider;
import btw.item.BTWItems;
import btw.block.BTWBlocks;
import com.inf1nlty.soultotem.STEnchantments;
import com.inf1nlty.soultotem.block.STBlocks;
import net.minecraft.src.Enchantment;
import net.minecraft.src.EntityVillager;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import com.inf1nlty.soultotem.item.STItems;
import com.inf1nlty.soultotem.item.SoulTotemItem;

public class STInit {

    public static void init() {
        addCraftingRecipes();
        addPriestTrades();
    }

    private static void addCraftingRecipes() {

        RecipeManager.addRecipe(
                new ItemStack(STItems.soul_eye, 1),
                new Object[]{
                        "UEU",
                        "EOE",
                        "UEU",
                        'U', new ItemStack(BTWItems.soulUrn, 1), 'E', new ItemStack(Item.emerald, 1), 'O', new ItemStack(BTWItems.ocularOfEnder)});


        ItemStack emptyTotem = new ItemStack(STBlocks.soulTotemItem);

        ItemStack fullTotem = new ItemStack(STBlocks.soulTotemItem);
        SoulTotemItem.setStoredSoul(fullTotem, SoulTotemItem.MAX_SOUL);

        RecipeManager.addRecipe(
                emptyTotem,
                new Object[]{
                        "SGS",
                        "GNG",
                        "CGC",
                        'S', STItems.soul_eye, 'G', Item.ingotGold, 'N', Item.netherStar, 'C', new ItemStack(BTWBlocks.companionCube, 1, 8)});
    }

    private static void addPriestTrades() {

        boolean nightmareLoaded = false;
        Class<?> bloodOrbClass = null;
        try {
            Class<?> nmItemsClass = Class.forName("com.itlesports.nightmaremode.item.NMItems");
            nightmareLoaded = true;
            bloodOrbClass = nmItemsClass;
        } catch (ClassNotFoundException ignored) {
        }
        Item bloodOrb = null;
        if (nightmareLoaded) {
            try {
                bloodOrb = (Item) bloodOrbClass.getField("bloodOrb").get(null);
            } catch (Exception e) {
                System.err.println("[SoulTotem] Failed to get NMItems.bloodOrb for soul_mending villager trade!");
                System.err.println("[SoulTotem] Exception: " + e);
            }
        }

        if (bloodOrb != null) {
            TradeProvider.getBuilder()
                    .name("stPriestSoulMendingScroll")
                    .profession(2)
                    .level(5)
                    .convert()
                    .input(TradeItem.fromID(Item.paper.itemID))
                    .secondInput(TradeItem.fromID(bloodOrb.itemID, 24, 32))
                    .output(TradeItem.fromIDAndMetadata(BTWItems.arcaneScroll.itemID, STEnchantments.SOUL_MENDING_ID))
                    .weight(1.0f)
                    .addToTradeList();
        }
        else {
            TradeProvider.getBuilder()
                    .name("stPriestSoulMendingScroll")
                    .profession(2)
                    .level(5)
                    .arcaneScroll()
                    .scrollEnchant(STEnchantments.soulMending)
                    .secondaryEmeraldCost(32, 64)
                    .mandatory()
                    .addToTradeList();
        }
    }
}