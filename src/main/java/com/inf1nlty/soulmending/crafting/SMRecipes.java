package com.inf1nlty.soulmending.crafting;

import btw.crafting.recipe.RecipeManager;
import btw.item.BTWItems;
import btw.block.BTWBlocks;
import com.inf1nlty.soulmending.block.SoulMendingBlocks;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import com.inf1nlty.soulmending.item.SoulMendingItems;
import com.inf1nlty.soulmending.item.SoulTotemItem;

public class SMRecipes {

    public static void initRecipes() {
        addCraftingRecipes();
    }

    private static void addCraftingRecipes() {

        RecipeManager.addRecipe(
                new ItemStack(SoulMendingItems.soul_emerald, 4),
                new Object[]{
                        "LEL",
                        "EUE",
                        "LEL",
                        'L', new ItemStack(Item.dyePowder, 1, 4), 'E', new ItemStack(Item.emerald, 1, 4), 'U', new ItemStack(BTWItems.soulUrn)});

        ItemStack fullTotem = new ItemStack(SoulMendingBlocks.soulTotemItem);
        SoulTotemItem.setStoredSoul(fullTotem, SoulTotemItem.MAX_SOUL);

        RecipeManager.addRecipe(
                fullTotem,
                new Object[]{
                        "SGS",
                        "GNG",
                        "CGC",
                        'S', SoulMendingItems.soul_emerald, 'G', Item.ingotGold, 'N', Item.netherStar, 'C', new ItemStack(BTWBlocks.companionCube, 1, 8)});
    }
}