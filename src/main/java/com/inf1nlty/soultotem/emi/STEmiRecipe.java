package com.inf1nlty.soultotem.emi;

import com.inf1nlty.soultotem.block.STBlocks;
import com.inf1nlty.soultotem.item.SoulTotemItem;
import btw.item.BTWItems;
import emi.dev.emi.emi.api.recipe.EmiWorldInteractionRecipe;
import emi.dev.emi.emi.api.stack.EmiStack;
import emi.dev.emi.emi.api.widget.SlotWidget;
import emi.shims.java.net.minecraft.text.Text;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ResourceLocation;

import java.util.function.Function;

public class STEmiRecipe {

    public static EmiWorldInteractionRecipe makeSoulTotemChargeRecipe() {

        EmiStack emptyTotem = EmiStack.of(new ItemStack(STBlocks.emptySoulTotemItem));

        EmiStack soulUrn = EmiStack.of(new ItemStack(BTWItems.soulUrn));

        ItemStack fullTotemStack = new ItemStack(STBlocks.soulTotemItem);
        SoulTotemItem.setStoredSoul(fullTotemStack, SoulTotemItem.MAX_SOUL);
        EmiStack fullTotem = EmiStack.of(fullTotemStack);

        Function<SlotWidget, SlotWidget> soulUrnTooltip = slot -> slot.appendTooltip(Text.translatable("emi.soultotem.charge.tooltip"));

        return EmiWorldInteractionRecipe.builder()
                .id(new ResourceLocation("soultotem", "charge_soul_totem"))
                .leftInput(emptyTotem)
                .rightInput(soulUrn, false, soulUrnTooltip)
                .output(fullTotem)
                .build();
    }
}