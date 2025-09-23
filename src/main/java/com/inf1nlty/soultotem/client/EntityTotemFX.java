package com.inf1nlty.soultotem.client;

import net.minecraft.src.*;

public class EntityTotemFX extends EntityFX {
    private final double targetX, targetY, targetZ;
    private final double motionDirX, motionDirY, motionDirZ;
    private final double travelDist;
    private final int travelDuration;
    private int travelTick;
    private boolean falling = false;
    private double fallVelY = 0.0;

    public EntityTotemFX(World world, double x, double y, double z, double dirX, double dirY, double dirZ) {
        super(world, x, y, z, 0, 0, 0);

        double norm = Math.sqrt(dirX * dirX + dirY * dirY + dirZ * dirZ);
        this.motionDirX = norm > 0.001 ? dirX / norm : 1.0;
        this.motionDirY = norm > 0.001 ? dirY / norm : 0.1;
        this.motionDirZ = norm > 0.001 ? dirZ / norm : 0.1;
        this.travelDist = 1.4 + rand.nextDouble() * 1.5;
        this.targetX = x + this.motionDirX * travelDist;
        this.targetY = y + this.motionDirY * travelDist;
        this.targetZ = z + this.motionDirZ * travelDist;
        this.travelDuration = 100;
        this.travelTick = 0;
        this.particleScale = 0.1F + rand.nextFloat() * 0.28F;
        this.particleMaxAge = travelDuration + 16 + rand.nextInt(8);

        if (rand.nextInt(4) == 0) {
            this.particleRed = 0.6F + rand.nextFloat() * 0.2F;
            this.particleGreen = 0.6F + rand.nextFloat() * 0.3F;
            this.particleBlue = rand.nextFloat() * 0.2F;

        } else {
            this.particleRed = 0.1F + rand.nextFloat() * 0.2F;
            this.particleGreen = 0.4F + rand.nextFloat() * 0.3F;
            this.particleBlue = rand.nextFloat() * 0.2F;
        }

        this.particleAlpha = 1.0F;
        this.noClip = true;
        this.motionX = 0;
        this.motionY = 0;
        this.motionZ = 0;
    }

    @Override
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        if (this.particleAge >= this.particleMaxAge) {
            this.setDead();
            return;
        }

        if (!falling) {
            travelTick++;
            double t = travelTick;
            double n = travelDuration;
            double dx = targetX - this.posX;
            double dy = targetY - this.posY;
            double dz = targetZ - this.posZ;
            double distSq = dx * dx + dy * dy + dz * dz;

            if (distSq < 0.1 * 0.1) {
                falling = true;
                fallVelY = -0.08 - rand.nextDouble() * 0.05;
                this.motionX = 0.0;
                this.motionY = fallVelY;
                this.motionZ = 0.0;

            } else if (travelTick <= travelDuration) {
                double nx = this.posX * (1.0 - t / n) + targetX * (t / n);
                double ny = this.posY * (1.0 - t / n) + targetY * (t / n);
                double nz = this.posZ * (1.0 - t / n) + targetZ * (t / n);
                this.motionX = nx - this.posX;
                this.motionY = ny - this.posY;
                this.motionZ = nz - this.posZ;

            } else {
                falling = true;
                fallVelY = -0.08 - rand.nextDouble() * 0.05;
                this.motionX = 0.0;
                this.motionY = fallVelY;
                this.motionZ = 0.0;
            }
        } else {
            this.motionY += -0.026;
            this.motionX *= 0.92;
            this.motionY *= 0.92;
            this.motionZ *= 0.92;
        }

        if (this.particleAge > this.particleMaxAge * 0.7) {
            this.particleAlpha = Math.max(0F, 1.0F - ((float)this.particleAge - this.particleMaxAge * 0.7F) / (this.particleMaxAge * 0.3F));
        }

        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        this.particleAge++;
    }

    @Override
    public int getFXLayer() {
        return 4;
    }

    @Override
    public void renderParticle(Tessellator tess, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
        float scale = 0.18F * this.particleScale;
        double interpPosX = Minecraft.getMinecraft().renderViewEntity.prevPosX + (Minecraft.getMinecraft().renderViewEntity.posX - Minecraft.getMinecraft().renderViewEntity.prevPosX) * partialTicks;
        double interpPosY = Minecraft.getMinecraft().renderViewEntity.prevPosY + (Minecraft.getMinecraft().renderViewEntity.posY - Minecraft.getMinecraft().renderViewEntity.prevPosY) * partialTicks;
        double interpPosZ = Minecraft.getMinecraft().renderViewEntity.prevPosZ + (Minecraft.getMinecraft().renderViewEntity.posZ - Minecraft.getMinecraft().renderViewEntity.prevPosZ) * partialTicks;
        float px = (float)(this.prevPosX + (this.posX - this.prevPosX) * partialTicks - interpPosX);
        float py = (float)(this.prevPosY + (this.posY - this.prevPosY) * partialTicks - interpPosY);
        float pz = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * partialTicks - interpPosZ);

        int animFrameCount = 8;
        int frameIdx = 176 + (animFrameCount - 1) - (this.particleAge * animFrameCount / this.particleMaxAge);
        if (frameIdx < 176) frameIdx = 176;
        int uIndex = frameIdx % 16;
        int vIndex = frameIdx / 16;
        float minU = uIndex * 8f / 128f;
        float maxU = minU + 8f / 128f;
        float minV = vIndex * 8f / 128f;
        float maxV = minV + 8f / 128f;

        tess.setColorRGBA_F(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha);

        tess.addVertexWithUV(px - rotationX * scale - rotationXY * scale, py - rotationZ * scale, pz - rotationYZ * scale - rotationXZ * scale, minU, maxV);
        tess.addVertexWithUV(px - rotationX * scale + rotationXY * scale, py + rotationZ * scale, pz - rotationYZ * scale + rotationXZ * scale, minU, minV);
        tess.addVertexWithUV(px + rotationX * scale + rotationXY * scale, py + rotationZ * scale, pz + rotationYZ * scale + rotationXZ * scale, maxU, minV);
        tess.addVertexWithUV(px + rotationX * scale - rotationXY * scale, py - rotationZ * scale, pz + rotationYZ * scale - rotationXZ * scale, maxU, maxV);
    }

    public static class Provider {
        public static void spawn(World world, double x, double y, double z) {
            com.inf1nlty.soultotem.client.ParticleSpawnQueue.enqueue(() -> {
                Minecraft mc = Minecraft.getMinecraft();
                int count = 96;
                for (int i = 0; i < count; i++) {
                    double theta = 2 * Math.PI * i / count + mc.theWorld.rand.nextDouble() * 0.23;
                    double phi = Math.acos(2.0 * mc.theWorld.rand.nextDouble() - 1.0);
                    double dirX = Math.sin(phi) * Math.cos(theta);
                    double dirY = Math.cos(phi) * 0.45 + 0.22;
                    double dirZ = Math.sin(phi) * Math.sin(theta);
                    mc.effectRenderer.addEffect(new EntityTotemFX(world, x, y + 1.0, z, dirX, dirY, dirZ));
                }
            });
        }
    }
}