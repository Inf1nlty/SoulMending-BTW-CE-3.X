package com.inf1nlty.soultotem.client;

import com.inf1nlty.soultotem.STConfig;
import net.minecraft.src.*;

public class EntitySoulFX extends EntityFX {
    private final double centerX, centerY, centerZ;
    private final float portalParticleScale;
    private final Entity targetEntity;

    private final double theta0, r0, yOffset0, omega;
    private final int spiralTurns;
    private final double verticalCurve;
    public final boolean useSoulAtlas;
    public final int atlasIndex;
    private final float colorBase;

    public EntitySoulFX(World world, double x, double y, double z, double targetX, double targetY, double targetZ, boolean useSoulAtlas) {
        super(world, x, y, z, 0, 0, 0);
        this.useSoulAtlas = useSoulAtlas;

        if(useSoulAtlas) {
            this.atlasIndex = world.rand.nextInt(16);
            this.colorBase = 0.5F - world.rand.nextFloat();
        } else {
            this.atlasIndex = 0;
            this.colorBase = 0.0F;
        }

        this.centerX = targetX;
        this.centerY = targetY;
        this.centerZ = targetZ;
        this.targetEntity = null;

        this.r0 = Math.sqrt((x - targetX)*(x - targetX) + (z - targetZ)*(z - targetZ));
        this.theta0 = Math.atan2(z - targetZ, x - targetX);
        this.yOffset0 = y - targetY;
        this.spiralTurns = 2 + rand.nextInt(2);
        this.omega = 0.33 * spiralTurns;
        this.verticalCurve = 0.5 + rand.nextDouble() * 0.2;

        this.particleMaxAge = 56 + rand.nextInt(16);
        this.portalParticleScale = this.particleScale = 0.18F + rand.nextFloat() * 0.08F;
        this.noClip = true;
        this.particleRed = 0.70F + rand.nextFloat() * 0.3F;
        this.particleGreen = 0.8F + rand.nextFloat() * 0.15F;
        this.particleBlue = 1.0F;
        this.particleAlpha = 0.92F + rand.nextFloat() * 0.07F;
    }

    public EntitySoulFX(World world, double x, double y, double z, Entity targetEntity, boolean useSoulAtlas) {
        super(world, x, y, z, 0, 0, 0);
        this.useSoulAtlas = useSoulAtlas;

        if(useSoulAtlas) {
            this.atlasIndex = world.rand.nextInt(16);
            this.colorBase = 0.5F - world.rand.nextFloat();
        } else {
            this.atlasIndex = 0;
            this.colorBase = 0.0F;
        }

        this.centerX = targetEntity.posX;
        this.centerY = targetEntity.posY + targetEntity.getEyeHeight() * 0.2;
        this.centerZ = targetEntity.posZ;
        this.targetEntity = targetEntity;

        this.r0 = Math.sqrt((x - centerX)*(x - centerX) + (z - centerZ)*(z - centerZ));
        this.theta0 = Math.atan2(z - centerZ, x - centerX);
        this.yOffset0 = y - centerY;

        this.spiralTurns = 2 + rand.nextInt(2);
        this.omega = spiralTurns;
        this.verticalCurve = 0.5 + rand.nextDouble() * 0.2;
        this.particleMaxAge = 36 + rand.nextInt(8);
        this.portalParticleScale = this.particleScale = 0.18F + rand.nextFloat() * 0.08F;
        this.noClip = true;
        this.particleRed = 0.70F + rand.nextFloat() * 0.3F;
        this.particleGreen = 0.8F + rand.nextFloat() * 0.15F;
        this.particleBlue = 1.0F;
        this.particleAlpha = 0.92F + rand.nextFloat() * 0.07F;
    }

    @Override
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        if (useSoulAtlas) {
            float t = colorBase + (float)this.particleAge / (float)this.particleMaxAge;
            this.particleRed   = (float)Math.pow(Math.sin(Math.PI * (t + 0.0f / 3.0f)), 2);
            this.particleGreen = (float)Math.pow(Math.sin(Math.PI * (t + 1.0f / 3.0f)), 2);
            this.particleBlue  = (float)Math.pow(Math.sin(Math.PI * (t + 2.0f / 3.0f)), 2);
        }

        double cx = centerX, cy = centerY, cz = centerZ;
        double r = r0, theta = theta0, yOffset = yOffset0;
        if (targetEntity != null && !targetEntity.isDead) {
            cx = targetEntity.posX;
            cy = targetEntity.posY + targetEntity.getEyeHeight()*0.2;
            cz = targetEntity.posZ;

            if (this.particleAge == 0) {
                r = Math.sqrt((this.posX - cx)*(this.posX - cx) + (this.posZ - cz)*(this.posZ - cz));
                theta = Math.atan2(this.posZ - cz, this.posX - cx);
                yOffset = this.posY - cy;
            }
        }

        float t = (float)this.particleAge / (float)this.particleMaxAge;

        double spiralStrength = 1.0;
        double rNow = r * Math.pow(1 - t, spiralStrength);
        double thetaNow = theta + omega * t * 2 * Math.PI;
        double yNow = cy + yOffset * (1 - t) + verticalCurve * Math.sin(t * Math.PI) - 0.15D;

        this.posX = cx + rNow * Math.cos(thetaNow);
        this.posY = yNow;
        this.posZ = cz + rNow * Math.sin(thetaNow);

        double destX, destY, destZ;
        destX = cx;
        destY = cy;
        destZ = cz;
        double dist = Math.sqrt(
                (this.posX - destX) * (this.posX - destX) +
                        (this.posY - destY) * (this.posY - destY) +
                        (this.posZ - destZ) * (this.posZ - destZ));

        if (dist < 0.25) {
            setDead();
            return;
        }

        float ageScale = ((float)this.particleAge) / (float)this.particleMaxAge;
        ageScale = 1.0F - ageScale;
        ageScale *= ageScale;
        ageScale = 1.0F - ageScale;
        this.particleScale = this.portalParticleScale * ageScale;

        if (++this.particleAge >= this.particleMaxAge) {
            setDead();
        }
    }

    @Override
    public int getFXLayer() {
        return 4; // Use 4 to indicate custom rendering, vanilla particles usually return 0
    }

    @Override
    public void renderParticle(Tessellator tessellator, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
//        Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation("soulmending:textures/blocks/soul.png"));
        float scale = this.particleScale;

        if (useSoulAtlas) {
            scale *= 0.25F;
        }

        double interpPosX = Minecraft.getMinecraft().renderViewEntity.prevPosX +
                (Minecraft.getMinecraft().renderViewEntity.posX - Minecraft.getMinecraft().renderViewEntity.prevPosX) * partialTicks;
        double interpPosY = Minecraft.getMinecraft().renderViewEntity.prevPosY +
                (Minecraft.getMinecraft().renderViewEntity.posY - Minecraft.getMinecraft().renderViewEntity.prevPosY) * partialTicks;
        double interpPosZ = Minecraft.getMinecraft().renderViewEntity.prevPosZ +
                (Minecraft.getMinecraft().renderViewEntity.posZ - Minecraft.getMinecraft().renderViewEntity.prevPosZ) * partialTicks;

        float px = (float)(this.prevPosX + (this.posX - this.prevPosX) * partialTicks - interpPosX);
        float py = (float)(this.prevPosY + (this.posY - this.prevPosY) * partialTicks - interpPosY);
        float pz = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * partialTicks - interpPosZ);

        float minU = 0.0F, maxU = 1.0F, minV = 0.0F, maxV = 1.0F;

        if(useSoulAtlas) {
            int texIndex = 224 + atlasIndex; // 224~239
            int uIndex = texIndex % 16;
            int vIndex = texIndex / 16;
            minU = uIndex / 16.0F;
            maxU = minU + 1.0F / 16.0F;
            minV = vIndex / 16.0F;
            maxV = minV + 1.0F / 16.0F;
        }

        tessellator.setColorRGBA_F(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha);

        tessellator.addVertexWithUV(px - rotationX * scale - rotationXY * scale, py - rotationZ * scale, pz - rotationYZ * scale - rotationXZ * scale, minU, maxV);
        tessellator.addVertexWithUV(px - rotationX * scale + rotationXY * scale, py + rotationZ * scale, pz - rotationYZ * scale + rotationXZ * scale, minU, minV);
        tessellator.addVertexWithUV(px + rotationX * scale + rotationXY * scale, py + rotationZ * scale, pz + rotationYZ * scale + rotationXZ * scale, maxU, minV);
        tessellator.addVertexWithUV(px + rotationX * scale - rotationXY * scale, py - rotationZ * scale, pz + rotationYZ * scale - rotationXZ * scale, maxU, maxV);
    }

    public static void spawnRing(World world, double blockX, double blockY, double blockZ) {
        if (!STConfig.renderSoulParticles) return;

        Minecraft mc = Minecraft.getMinecraft();
        if (mc == null || mc.effectRenderer == null) return;

        double targetY = blockY + 1.5;

        int count = 6 + world.rand.nextInt(3);

        ParticleSpawnQueue.enqueue(() -> {
            for (int i = 0; i < count; i++) {
                double angle = world.rand.nextDouble() * Math.PI * 2.0;
                double radius = 0.7 + world.rand.nextDouble() * 1.7;
                double y = blockY + 0.2 + world.rand.nextDouble() * 0.5;
                double x = blockX + Math.cos(angle) * radius;
                double z = blockZ + Math.sin(angle) * radius;

                x += (world.rand.nextDouble() - 0.5) * 0.3;
                z += (world.rand.nextDouble() - 0.5) * 0.3;
                mc.effectRenderer.addEffect(new EntitySoulFX(world, x, y, z, blockX, targetY, blockZ, true));
            }
        });
    }

    public static void spawnRingToPlayer(World world, double eventX, double eventY, double eventZ) {
        if (!STConfig.renderSoulParticles) return;

        Minecraft mc = Minecraft.getMinecraft();
        if (mc == null || mc.effectRenderer == null) return;

        EntityPlayer player = mc.theWorld.getClosestPlayer(eventX + 0.5, eventY, eventZ + 0.5, 2.5);
        if (player == null) return;

        int particleCount = 10;
        ParticleSpawnQueue.enqueue(() -> {
            for (int i = 0; i < particleCount; i++) {
                double angle = world.rand.nextDouble() * Math.PI * 2.0;
                double radius = 1.6 + world.rand.nextDouble() * 0.8;
                double height = -1.0 + world.rand.nextDouble() * 3.0;
                double x = player.posX + Math.cos(angle) * radius;
                double y = player.posY + player.getEyeHeight() + height;
                double z = player.posZ + Math.sin(angle) * radius;
                mc.effectRenderer.addEffect(new EntitySoulFX(world, x, y, z, player, false));
            }
        });
    }

    public static void findAndSpawnGatherToItem(World world, double x, double y, double z) {
        if (!STConfig.renderSoulParticles) return;

        Minecraft mc = Minecraft.getMinecraft();
        if (mc == null || mc.effectRenderer == null) return;

        EntityItem targetEntity = null;
        double minDistSq = 0.04;
        for (Object obj : world.loadedEntityList) {
            if (obj instanceof EntityItem item) {
                double dx = item.posX - x;
                double dy = item.posY - y;
                double dz = item.posZ - z;
                double distSq = dx * dx + dy * dy + dz * dz;
                if (distSq < minDistSq) {
                    targetEntity = item;
                    break;
                }
            }
        }
        if (targetEntity != null) {
            spawnGatherToItem(world, targetEntity);
        }
    }

    public static void spawnGatherToItem(World world, Entity targetEntity) {
        if (!STConfig.renderSoulParticles) return;

        Minecraft mc = Minecraft.getMinecraft();
        if (mc == null || mc.effectRenderer == null || targetEntity == null) return;

        double targetX = targetEntity.posX;
        double targetY = targetEntity.posY + targetEntity.height * 0.5;
        double targetZ = targetEntity.posZ;

        ParticleSpawnQueue.enqueue(() -> {
            for (int i = 0; i < 8; i++) {
                double angle = world.rand.nextDouble() * Math.PI * 2.0;
                double radius = 1.0 + world.rand.nextDouble() * 0.8;
                double height = world.rand.nextDouble();
                double x = targetX + Math.cos(angle) * radius;
                double y = targetY + 0.2 + height * 0.6;
                double z = targetZ + Math.sin(angle) * radius;
                mc.effectRenderer.addEffect(new EntitySoulFX(world, x, y, z, targetEntity, false));
            }
        });
    }

    public static void spawnGatherToBlock(World world, double blockX, double blockY, double blockZ) {
        if (!STConfig.renderSoulParticles) return;

        Minecraft mc = Minecraft.getMinecraft();
        if (mc == null || mc.effectRenderer == null) return;

        double targetX = blockX + 0.5;
        double targetY = blockY + 0.5;
        double targetZ = blockZ + 0.5;

        ParticleSpawnQueue.enqueue(() -> {
        for (int i = 0; i < 8; i++) {
            double angle = world.rand.nextDouble() * Math.PI * 2.0;
            double radius = 0.5 + world.rand.nextDouble() * 0.6;
            double height = world.rand.nextDouble();
            double x = targetX + Math.cos(angle) * radius;
            double y = blockY + 0.2 + height * 0.6;
            double z = targetZ + Math.sin(angle) * radius;
            mc.effectRenderer.addEffect(new EntitySoulFX(world, x, y, z, targetX, targetY, targetZ, false));
        }
        });
    }
}