package com.inf1nlty.soulmending.mixin.client;

import com.inf1nlty.soulmending.block.BlockSoulTotem;
import com.inf1nlty.soulmending.block.tileentity.TileEntityISoulTotem;
import com.inf1nlty.soulmending.item.SoulTotemItem;
import net.minecraft.src.*;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiIngame.class)
public class GuiIngameMixin {

    @Inject(method = "renderGameOverlay", at = @At("RETURN"))
    private void injectSoulTotemHUD(float partialTicks, boolean b, int mx, int my, CallbackInfo ci) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc == null || mc.objectMouseOver == null) return;

        if (mc.objectMouseOver.typeOfHit == EnumMovingObjectType.TILE) {

            int x = mc.objectMouseOver.blockX;
            int y = mc.objectMouseOver.blockY;
            int z = mc.objectMouseOver.blockZ;
            World world = mc.theWorld;
            int blockId = world.getBlockId(x, y, z);
            Block block = Block.blocksList[blockId];

            if (block instanceof BlockSoulTotem) {
                TileEntity te = world.getBlockTileEntity(x, y, z);

                if (te instanceof TileEntityISoulTotem) {

                    int soul = ((TileEntityISoulTotem) te).getStoredSoul();
                    int maxSoul = SoulTotemItem.MAX_SOUL;
                    float percent = (float) soul / (float) maxSoul;
                    String color;

                    if (percent >= 0.75f) {
                        color = "§a";

                    } else if (percent >= 0.5f) {
                        color = "§e";

                    } else if (percent >= 0.25f) {
                        color = "§6";

                    } else {
                        color = "§c";
                    }

                    String colorMax = "§7" + maxSoul + "§f";
                    String soulStr = color + soul + "§f";
                    String text = String.format(StatCollector.translateToLocal("soul_totem.tooltip"), soulStr, colorMax);

                    FontRenderer fr = mc.fontRenderer;
                    ScaledResolution sr = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);

                    int screenWidth = sr.getScaledWidth();
                    int screenHeight = sr.getScaledHeight();
                    int centerX = screenWidth / 2;
                    int centerY = screenHeight / 2;
                    int textX = centerX - (fr.getStringWidth(text) / 2);
                    int textY = centerY - 30;

                    GL11.glPushMatrix();
                    fr.drawStringWithShadow(text, textX, textY, 0x66FFFF);
                    GL11.glPopMatrix();
                }
            }
        }
    }
}