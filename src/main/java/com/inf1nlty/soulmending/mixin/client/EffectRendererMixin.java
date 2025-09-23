package com.inf1nlty.soulmending.mixin.client;

import com.inf1nlty.soulmending.client.EntitySoulFX;
import com.inf1nlty.soulmending.client.EntityTotemFX;
import com.prupe.mcpatcher.sky.FireworksHelper;
import net.minecraft.src.*;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import java.util.List;

@Mixin(EffectRenderer.class)
public abstract class EffectRendererMixin {

    @Shadow public TextureManager renderer;
    @Shadow public List<EntityFX>[] fxLayers;

    /**
     * Overwrite vanilla renderParticles to ensure layer==4 custom particles
     * always uses your custom texture and the correct GL blend/alpha state.
     * Other layers are treated as vanilla.
     * @author Inf1nlty
     * @reason Fix vanilla not initializing GL state for custom soul particle layer 4,
     * preventing "transparent/gray effect" and ensuring correct render for mod particles.
     */
    @Overwrite
    public void renderParticles(Entity par1Entity, float par2) {
        float var3 = ActiveRenderInfo.rotationX;
        float var4 = ActiveRenderInfo.rotationZ;
        float var5 = ActiveRenderInfo.rotationYZ;
        float var6 = ActiveRenderInfo.rotationXY;
        float var7 = ActiveRenderInfo.rotationXZ;
        EntityFX.interpPosX = par1Entity.lastTickPosX + (par1Entity.posX - par1Entity.lastTickPosX) * (double)par2;
        EntityFX.interpPosY = par1Entity.lastTickPosY + (par1Entity.posY - par1Entity.lastTickPosY) * (double)par2;
        EntityFX.interpPosZ = par1Entity.lastTickPosZ + (par1Entity.posZ - par1Entity.lastTickPosZ) * (double)par2;

        for (int var8 = 0; var8 < 5; ++var8) {

            if (!FireworksHelper.skipThisLayer(fxLayers[var8].isEmpty(), var8)) {

                switch (var8) {

                    case 1:
                        renderer.bindTexture(TextureMap.locationBlocksTexture);
                        break;
                    case 2:
                        renderer.bindTexture(TextureMap.locationItemsTexture);
                        break;
                    case 4:
                        break;
                    case 0:
                        renderer.bindTexture(new ResourceLocation("textures/particle/particles.png"));
                        break;
                }

                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                GL11.glEnable(GL11.GL_BLEND);

                if (var8 == 4) {
                    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                    GL11.glAlphaFunc(GL11.GL_GREATER, 0.003921569F);
                    GL11.glDepthMask(true);

                    Tessellator tessellator = Tessellator.instance;
                    tessellator.startDrawingQuads();

                    ResourceLocation lastTex = null;
                    for (EntityFX fx : fxLayers[var8]) {

                        ResourceLocation currentTex;

                        if (fx instanceof EntityTotemFX) {

                            currentTex = new ResourceLocation("soulmending:textures/particle/totem_atlas.png");

                        } else if (fx instanceof EntitySoulFX && ((EntitySoulFX)fx).useSoulAtlas) {
                            currentTex = new ResourceLocation("soulmending:textures/particle/totem_atlas.png");

                        } else {
                            currentTex = new ResourceLocation("soulmending:textures/blocks/soul.png");
                        }

                        if (lastTex == null || !lastTex.equals(currentTex)) {
                            tessellator.draw();
                            renderer.bindTexture(currentTex);
                            tessellator.startDrawingQuads();
                            lastTex = currentTex;
                        }
                        tessellator.setBrightness(fx.getBrightnessForRender(par2));
                        fx.renderParticle(tessellator, par2, var3, var7, var4, var5, var6);
                    }
                    tessellator.draw();
                    GL11.glDisable(GL11.GL_BLEND);
                    GL11.glDepthMask(true);
                    GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
                    continue;

                } else {
                    FireworksHelper.setParticleBlendMethod(var8, 0, true);
                    GL11.glAlphaFunc(GL11.GL_GREATER, 0.003921569F);
                }

                Tessellator var9 = Tessellator.instance;
                var9.startDrawingQuads();

                for (EntityFX var11 : fxLayers[var8]) {
                    var9.setBrightness(var11.getBrightnessForRender(par2));
                    var11.renderParticle(var9, par2, var3, var7, var4, var5, var6);
                }

                var9.draw();
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glDepthMask(true);
                GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
            }
        }
    }
}