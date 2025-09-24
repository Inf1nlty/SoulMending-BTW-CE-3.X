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

    private static void addPriestTrades(){

        EntityVillager.removeCustomTrade(2,
                TradeProvider.getBuilder()
                        .name("btw:sell_fortune_scroll")
                        .profession(2)
                        .level(5)
                        .arcaneScroll()
                        .scrollEnchant(Enchantment.fortune)
                        .secondaryEmeraldCost(48, 64)
                        .mandatory()
                        .build()
        );

        TradeProvider.getBuilder()
                .name("btw:sell_fortune_scroll")
                .profession(2)
                .level(5)
                .arcaneScroll()
                .scrollEnchant(Enchantment.fortune)
                .secondaryEmeraldCost(48, 64)
                .mandatory()
                .condition(villager -> villager.getRNG().nextFloat() < 0.5f)
                .addToTradeList();

        TradeProvider.getBuilder()
                .name("stPriestSoulMendingScroll")
                .profession(2)
                .level(5)
                .arcaneScroll()
                .scrollEnchant(STEnchantments.soulMending)
                .secondaryEmeraldCost(32, 64)
                .mandatory()
                .condition(villager -> villager.getRNG().nextFloat() >= 0.5f)
                .addToTradeList();
    }
}