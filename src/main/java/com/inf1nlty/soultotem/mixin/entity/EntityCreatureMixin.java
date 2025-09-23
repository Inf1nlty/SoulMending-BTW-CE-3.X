package com.inf1nlty.soultotem.mixin.entity;

import btw.entity.mob.possession.PossessionSource;
import com.inf1nlty.soultotem.util.ISoulPossessable;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.List;

@Mixin(EntityCreature.class)
public abstract class EntityCreatureMixin implements ISoulPossessable {

    @Override
    public void soulMending$onSoulPossession() {
    }

    @Inject(method = "initiatePossession", at = @At("TAIL"))
    public void onInitiatePossession(PossessionSource<?> possessionSource, CallbackInfo ci) {
        this.soulMending$onSoulPossession();
    }

    @Inject(method = "onDeath", at = @At("RETURN"))
    public void onDeathInject(DamageSource source, CallbackInfo ci) {
        EntityCreature self = (EntityCreature)(Object)this;

        if (!self.worldObj.isRemote && self.isPossessed()) {
            double range = 16D;
            double x = self.posX, y = self.posY, z = self.posZ;
            World world = self.worldObj;
            AxisAlignedBB box = AxisAlignedBB.getAABBPool().getAABB(
                    x - range, y - range, z - range,
                    x + range, y + range, z + range
            );

            // Entity
            @SuppressWarnings("unchecked")
            List<Entity> entityList = world.getEntitiesWithinAABB(Entity.class, box);
            for (Entity entity : entityList) {
                if (entity != self && entity.isEntityAlive()
                        && !(entity instanceof EntityCreature)
                        && entity instanceof ISoulPossessable possessable) {
                    possessable.soulMending$onSoulPossession();
                }
            }

            // TileEntity
            @SuppressWarnings("unchecked")
            List<TileEntity> tileList = (List<TileEntity>)world.loadedTileEntityList;
            for (TileEntity tile : tileList) {
                if (tile.xCoord >= box.minX && tile.xCoord <= box.maxX &&
                        tile.yCoord >= box.minY && tile.yCoord <= box.maxY &&
                        tile.zCoord >= box.minZ && tile.zCoord <= box.maxZ &&
                        tile instanceof ISoulPossessable possessable) {
                    possessable.soulMending$onSoulPossession();
                }
            }
        }
    }
}