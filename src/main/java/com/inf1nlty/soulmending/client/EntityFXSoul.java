package com.inf1nlty.soulmending.client;

import net.minecraft.src.*;

public class EntityFXSoul extends EntityFX {
    private final double targetX, targetY, targetZ;
    private final float initScale;
    private final Entity targetEntity;

    public EntityFXSoul(World world, double x, double y, double z, double targetX, double targetY, double targetZ) {
        super(world, x, y, z, 0, 0, 0);
        this.targetX = targetX;
        this.targetY = targetY;
        this.targetZ = targetZ;
        this.targetEntity = null;

        double dx = targetX - x;
        double dy = targetY - y;
        double dz = targetZ - z;
        double len = Math.sqrt(dx*dx + dy*dy + dz*dz);

        if (len > 0) {
            double speed = 0.01 + rand.nextFloat() * 0.01;
            motionX = dx / len * speed;
            motionY = dy / len * speed;
            motionZ = dz / len * speed;
        }

        this.particleMaxAge = 30 + rand.nextInt(8);
        this.particleScale = this.initScale = 0.20F + rand.nextFloat() * 0.1F;
        this.noClip = true;

        this.particleRed = 1.0F;
        this.particleGreen = 1.0F;
        this.particleBlue = 1.0F;
        this.particleAlpha = 0.95F;
    }

    public EntityFXSoul(World world, double x, double y, double z, Entity targetEntity) {
        super(world, x, y, z, 0, 0, 0);
        this.targetX = 0;
        this.targetY = 0;
        this.targetZ = 0;
        this.targetEntity = targetEntity;
        this.initScale = this.particleScale = 0.20F + rand.nextFloat() * 0.1F;
        this.particleMaxAge = 30 + rand.nextInt(8);
        this.noClip = true;
        this.particleRed = 1.0F;
        this.particleGreen = 1.0F;
        this.particleBlue = 1.0F;
        this.particleAlpha = 0.95F;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        double dx, dy, dz;

        if (targetEntity != null && !targetEntity.isDead) {
            dx = targetEntity.posX - posX;
            dy = (targetEntity.posY + targetEntity.getEyeHeight()) - posY;
            dz = targetEntity.posZ - posZ;

        } else {
            dx = targetX - posX;
            dy = targetY - posY;
            dz = targetZ - posZ;
        }

        double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
        if (dist < 0.15) {
            setDead();
            return;
        }

        this.particleScale = (float) (this.initScale * (0.6 + dist * 0.4));
        motionX += dx * 0.005;
        motionY += dy * 0.005;
        motionZ += dz * 0.005;
        motionX *= 0.95;
        motionY *= 0.95;
        motionZ *= 0.95;
    }

    @Override
    public int getFXLayer() {
        return 0;
    }

    @Override
    public void renderParticle(Tessellator tessellator, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
        Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation("soulmending:textures/blocks/soul.png"));

        float scale = this.particleScale * (1F - ((float)this.particleAge + partialTicks) / (float)this.particleMaxAge);

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

        tessellator.setColorRGBA_F(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha);

        tessellator.addVertexWithUV(px - rotationX * scale - rotationXY * scale, py - rotationZ * scale, pz - rotationYZ * scale - rotationXZ * scale, minU, maxV);
        tessellator.addVertexWithUV(px - rotationX * scale + rotationXY * scale, py + rotationZ * scale, pz - rotationYZ * scale + rotationXZ * scale, minU, minV);
        tessellator.addVertexWithUV(px + rotationX * scale + rotationXY * scale, py + rotationZ * scale, pz + rotationYZ * scale + rotationXZ * scale, maxU, minV);
        tessellator.addVertexWithUV(px + rotationX * scale - rotationXY * scale, py - rotationZ * scale, pz + rotationYZ * scale - rotationXZ * scale, maxU, maxV);
    }

    public static void spawnRing(World world, double blockX, double blockY, double blockZ) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc == null || mc.effectRenderer == null) return;

        double targetY = blockY + 1.5;

        for (int i = 0; i < 3; i++) {
            double angle = world.rand.nextDouble() * Math.PI * 2.0;
            double radius = 0.35 + world.rand.nextDouble() * 0.3;
            double x = blockX + Math.cos(angle) * radius;
            double y = blockY + 0.2 + world.rand.nextDouble() * 0.5;
            double z = blockZ + Math.sin(angle) * radius;
            mc.effectRenderer.addEffect(new EntityFXSoul(world, x, y, z, blockX, targetY, blockZ));
        }
    }

    public static void spawnRingToPlayer(World world, double eventX, double eventY, double eventZ) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc == null || mc.effectRenderer == null) return;

        EntityPlayer player = mc.theWorld.getClosestPlayer(eventX + 0.5, eventY, eventZ + 0.5, 2.5);
        if (player == null) return;

        int particleCount = 16;
        for (int i = 0; i < particleCount; i++) {
            double angle = world.rand.nextDouble() * Math.PI * 2.0;
            double radius = 1.1 + world.rand.nextDouble() * 0.5;
            double height = -0.5 + world.rand.nextDouble() * 2.0;
            double x = player.posX + Math.cos(angle) * radius;
            double y = player.posY + player.getEyeHeight() + height;
            double z = player.posZ + Math.sin(angle) * radius;
            mc.effectRenderer.addEffect(new EntityFXSoul(world, x, y, z, player));
        }
    }

    public static void findAndSpawnGatherToItem(World world, double x, double y, double z) {
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
        Minecraft mc = Minecraft.getMinecraft();
        if (mc == null || mc.effectRenderer == null || targetEntity == null) return;

        double targetX = targetEntity.posX;
        double targetY = targetEntity.posY + targetEntity.height * 0.5;
        double targetZ = targetEntity.posZ;

        for (int i = 0; i < 8; i++) {
            double angle = world.rand.nextDouble() * Math.PI * 2.0;
            double radius = 1.0 + world.rand.nextDouble() * 0.8;
            double height = world.rand.nextDouble();
            double x = targetX + Math.cos(angle) * radius;
            double y = targetY + 0.2 + height * 0.6;
            double z = targetZ + Math.sin(angle) * radius;
            mc.effectRenderer.addEffect(new EntityFXSoul(world, x, y, z, targetEntity));
        }
    }

    public static void spawnGatherToBlock(World world, double blockX, double blockY, double blockZ) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc == null || mc.effectRenderer == null) return;

        double targetX = blockX + 0.5;
        double targetY = blockY + 0.5;
        double targetZ = blockZ + 0.5;

        for (int i = 0; i < 8; i++) {
            double angle = world.rand.nextDouble() * Math.PI * 2.0;
            double radius = 0.5 + world.rand.nextDouble() * 0.6;
            double height = world.rand.nextDouble();
            double x = targetX + Math.cos(angle) * radius;
            double y = blockY + 0.2 + height * 0.6;
            double z = targetZ + Math.sin(angle) * radius;
            mc.effectRenderer.addEffect(new EntityFXSoul(world, x, y, z, targetX, targetY, targetZ));
        }
    }
}