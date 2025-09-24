package com.inf1nlty.soultotem.emi;

import com.inf1nlty.soultotem.block.STBlocks;
import com.inf1nlty.soultotem.item.SoulTotemItem;
import emi.dev.emi.emi.api.recipe.EmiInfoRecipe;
import emi.dev.emi.emi.api.stack.EmiStack;
import emi.shims.java.net.minecraft.text.Text;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class STEmiInfoRecipe {

    public static EmiInfoRecipe makeSoulTotemInfo() {

        ItemStack fullTotemStack = new ItemStack(STBlocks.soulTotemItem);
        SoulTotemItem.setStoredSoul(fullTotemStack, SoulTotemItem.MAX_SOUL);
        EmiStack soulTotem = EmiStack.of(fullTotemStack);

        EmiStack emptySoulTotem = EmiStack.of(new ItemStack(STBlocks.emptySoulTotemItem));

        List<EmiStack> stacks = new ArrayList<>();
        stacks.add(soulTotem);
        stacks.add(emptySoulTotem);

        String infoText = Text.translatable("emi.soultotem.info").asString();
        List<Text> info = new ArrayList<>();
        for (String line : infoText.split("\\\\n")) {
            info.add(Text.literal(line));
        }

        return new EmiInfoRecipe(
                new ArrayList<>(stacks),
                info,
                new ResourceLocation("soultotem", "soul_totem_info")
        );
    }
}