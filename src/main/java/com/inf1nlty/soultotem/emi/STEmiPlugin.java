package com.inf1nlty.soultotem.emi;

import emi.dev.emi.emi.api.EmiPlugin;
import emi.dev.emi.emi.api.EmiRegistry;

public class STEmiPlugin implements EmiPlugin {

    @Override
    public void register(EmiRegistry registry) {

        registry.addRecipe(STEmiRecipe.makeSoulTotemChargeRecipe());
        registry.addRecipe(STEmiInfoRecipe.makeSoulTotemInfo());
    }
}