package com.inf1nlty.soultotem.mixin.client;

import com.inf1nlty.soultotem.STConfig;
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

    @Inject(method = "initGui", at = @At("TAIL"))
    @SuppressWarnings("unchecked")
    private void soulmending$addSoulMendingButtons(CallbackInfo ci) {

        int optionCount = 0;
        for (Object o : this.buttonList) {
            if (o instanceof GuiButton btn && btn.id != 200) {
                optionCount++;
            }
        }

        int yOffset = getYOffset();
        int x1 = this.width / 2 - 155 + (optionCount % 2) * 160;
        int y1 = this.height / 7 + yOffset + 24 * (optionCount / 2);
        GuiButton soulParticlesButton = new GuiButton(SOUL_PARTICLES_BTN_ID, x1, y1, 150, 20, getSoulParticlesLabel());
        ((List<GuiButton>) this.buttonList).add(optionCount, soulParticlesButton);

        optionCount++;
        int x2 = this.width / 2 - 155 + (optionCount % 2) * 160;
        int y2 = this.height / 7 + yOffset + 24 * (optionCount / 2);
        GuiButton soulHudButton = new GuiButton(SOUL_HUD_BTN_ID, x2, y2, 150, 20, getSoulHudLabel());
        ((List<GuiButton>) this.buttonList).add(optionCount, soulHudButton);
        for (Object o : this.buttonList) {
            if (o instanceof GuiButton btn && btn.id == 200) {
                btn.yPosition += 24;
                break;
            }
        }
    }

    @Unique
    private int getYOffset() {
        boolean is64bit = false;
        String[] props = new String[] {"sun.arch.data.model", "com.ibm.vm.bitmode", "os.arch"};
        for (String key : props) {
            String v = System.getProperty(key);
            if (v != null && v.contains("64")) {
                is64bit = true;
                break;
            }
        }
        return is64bit ? -8 : -21;
    }

    @Inject(method = "actionPerformed", at = @At("HEAD"))
    private void soulmending$onSoulButton(GuiButton button, CallbackInfo ci) {

        if (button.id == SOUL_PARTICLES_BTN_ID) {
            STConfig.renderSoulParticles = !STConfig.renderSoulParticles;
            button.displayString = getSoulParticlesLabel();
            STConfig.save();

        } else if (button.id == SOUL_HUD_BTN_ID) {
            STConfig.showSoulHUD = !STConfig.showSoulHUD;
            button.displayString = getSoulHudLabel();
            STConfig.save();
        }
    }

    @Unique
    private String getSoulParticlesLabel() {
        return I18n.getString("options.soulmending.soulParticles") + ": "
                + I18n.getString(STConfig.renderSoulParticles ? "options.soulmending.on" : "options.soulmending.off");
    }

    @Unique
    private String getSoulHudLabel() {
        return I18n.getString("options.soulmending.soulHUD") + ": "
                + I18n.getString(STConfig.showSoulHUD ? "options.soulmending.on" : "options.soulmending.off");
    }
}