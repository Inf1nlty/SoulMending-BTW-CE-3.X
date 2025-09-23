package com.inf1nlty.soultotem.mixin.client;

import com.inf1nlty.soultotem.item.SoulTotemItem;
import net.minecraft.src.ItemStack;
import net.minecraft.src.RenderItem;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.TextureManager;
import net.minecraft.src.Tessellator;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderItem.class)
public abstract class RenderItemMixin {

    @Inject(method = "renderItemOverlayIntoGUI(Lnet/minecraft/src/FontRenderer;Lnet/minecraft/src/TextureManager;Lnet/minecraft/src/ItemStack;II)V", at = @At("TAIL"))
    private void soulmending$renderSoulBar(FontRenderer fontRenderer, TextureManager textureManager, ItemStack stack, int x, int y, CallbackInfo ci) {
        if (stack == null || !(stack.getItem() instanceof SoulTotemItem)) return;

        int soul = SoulTotemItem.getStoredSoul(stack);
        int maxSoul = SoulTotemItem.MAX_SOUL;

        if (soul > 0 && soul < maxSoul) {
            float percent = (float) soul / (float) maxSoul;
            int barLength = (int)Math.floor(13.0D * percent);
            int barColor = getBarColor(percent);

            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            Tessellator t = Tessellator.instance;
            renderQuad(t, x + 2, y + 13, 13, 2, 0x000000);
            renderQuad(t, x + 2, y + 13, 12, 1, 0x404040);
            renderQuad(t, x + 2, y + 13, barLength, 1, barColor);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glColor4f(1,1,1,1);
        }
    }

    @Unique
    private int getBarColor(float percent) {
        int red = (int)(255.0F * (1.0F - percent));
        int green = 255 - red;
        return (red << 16) | (green << 8);
    }

    @Unique
    private void renderQuad(Tessellator tess, int x, int y, int width, int height, int color) {
        tess.startDrawingQuads();
        tess.setColorOpaque_I(color);
        tess.addVertex(x,         y,          0.0D);
        tess.addVertex(x,         y + height, 0.0D);
        tess.addVertex(x + width, y + height, 0.0D);
        tess.addVertex(x + width, y,          0.0D);
        tess.draw();
    }
}