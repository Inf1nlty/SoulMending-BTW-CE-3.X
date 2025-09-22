package com.inf1nlty.soulmending.mixin.client;

import com.inf1nlty.soulmending.SoulMendingConfig;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.GuiVideoSettings;
import net.minecraft.src.I18n;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(GuiVideoSettings.class)
public abstract class GuiVideoSettingsMixin extends GuiScreen {

    @Unique
    private static final int SOUL_PARTICLES_BTN_ID = 5001;

    @Unique
    private static final int SOUL_HUD_BTN_ID = 5002;

    @SuppressWarnings("unchecked")
    @Inject(method = "initGui", at = @At("TAIL"))
    private void soulmending$addSoulMendingButtons(CallbackInfo ci) {
        int yBase = this.height / 6 + 210;
        int x = this.width / 2 - 100;

        GuiButton soulParticlesButton = new GuiButton(SOUL_PARTICLES_BTN_ID, x, yBase, 200, 20, getSoulParticlesLabel());
        GuiButton soulHudButton = new GuiButton(SOUL_HUD_BTN_ID, x, yBase + 24, 200, 20, getSoulHudLabel());

        ((List<GuiButton>) this.buttonList).add(soulParticlesButton);
        ((List<GuiButton>) this.buttonList).add(soulHudButton);
    }

    @Inject(method = "actionPerformed", at = @At("HEAD"))
    private void soulmending$onSoulButton(GuiButton button, CallbackInfo ci) {
        if (button.id == SOUL_PARTICLES_BTN_ID) {
            SoulMendingConfig.renderSoulParticles = !SoulMendingConfig.renderSoulParticles;
            button.displayString = getSoulParticlesLabel();
        } else if (button.id == SOUL_HUD_BTN_ID) {
            SoulMendingConfig.showSoulHUD = !SoulMendingConfig.showSoulHUD;
            button.displayString = getSoulHudLabel();
        }
    }

    @Unique
    private String getSoulParticlesLabel() {
        return I18n.getString("options.soulmending.soulParticles") + ": "
                + I18n.getString(SoulMendingConfig.renderSoulParticles ? "options.soulmending.on" : "options.soulmending.off");
    }

    @Unique
    private String getSoulHudLabel() {
        return I18n.getString("options.soulmending.soulHUD") + ": "
                + I18n.getString(SoulMendingConfig.showSoulHUD ? "options.soulmending.on" : "options.soulmending.off");
    }
}