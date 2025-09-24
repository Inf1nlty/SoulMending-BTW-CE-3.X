package com.inf1nlty.soultotem.init;

import btw.crafting.recipe.RecipeManager;
import btw.entity.mob.villager.trade.TradeItem;
import btw.entity.mob.villager.trade.TradeProvider;
import btw.item.BTWItems;
import btw.block.BTWBlocks;
import com.inf1nlty.soultotem.STEnchantments;
import com.inf1nlty.soultotem.block.STBlocks;
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
                        'U', new ItemStack(BTWItems.soulUrn, 1, 4), 'E', new ItemStack(Item.emerald, 1, 4), 'O', new ItemStack(BTWItems.ocularOfEnder)});

        ItemStack fullTotem = new ItemStack(STBlocks.soulTotemItem);
        SoulTotemItem.setStoredSoul(fullTotem, SoulTotemItem.MAX_SOUL);

        RecipeManager.addRecipe(
                fullTotem,
                new Object[]{
                        "SGS",
                        "GNG",
                        "CGC",
                        'S', STItems.soul_eye, 'G', Item.ingotGold, 'N', Item.netherStar, 'C', new ItemStack(BTWBlocks.companionCube, 1, 8)});
    }

    private static void addPriestTrades(){

        TradeProvider.getBuilder()
                .name("stPriestSoulMendingScroll")
                .profession(2)
                .level(5)
                .convert()
                .input(TradeItem.fromID(Item.paper.itemID))
                .secondInput(TradeItem.fromID(Item.emerald.itemID, 32, 64))
                .output(TradeItem.fromIDAndMetadata(BTWItems.arcaneScroll.itemID, STEnchantments.SOUL_MENDING_ID))
                // 100% chance
//                .mandatory()
                .addToTradeList();
    }
}