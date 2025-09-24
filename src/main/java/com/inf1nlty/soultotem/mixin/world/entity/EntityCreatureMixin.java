package com.inf1nlty.soultotem.mixin.world.entity;

import btw.entity.mob.possession.PossessionSource;
import com.inf1nlty.soultotem.util.ISoulPossessable;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(EntityCreature.class)
public abstract class EntityCreatureMixin {

    @Inject(method = "attemptToPossessNearbyCreature", at = @At("TAIL"))
    public void onAttemptToPossessNearbyCreature(double dRange, boolean bPersistentSpirit, PossessionSource<?> possessionSource, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue()) return;

        EntityCreature self = (EntityCreature) (Object) this;
        World world = self.worldObj;

        if (!world.isRemote) {
            AxisAlignedBB aabb = AxisAlignedBB.getAABBPool().getAABB(
                    self.posX - dRange, self.posY - dRange, self.posZ - dRange,
                    self.posX + dRange, self.posY + dRange, self.posZ + dRange
            );

            // Entity
            @SuppressWarnings("unchecked")
            List<Entity> entityList = world.getEntitiesWithinAABB(Entity.class, aabb);
            for (Entity entity : entityList) {
                if (entity != self && entity.isEntityAlive()
                        && !(entity instanceof EntityCreature)
                        && entity instanceof ISoulPossessable) {
                    ((ISoulPossessable) entity).soulMending$onSoulPossession();
                    return;
                }
            }

            // TileEntity
            @SuppressWarnings("unchecked")
            List<TileEntity> tileEntityList = (List<TileEntity>) world.loadedTileEntityList;
            for (TileEntity tileEntity : tileEntityList) {
                if (tileEntity.xCoord >= aabb.minX && tileEntity.xCoord <= aabb.maxX &&
                        tileEntity.yCoord >= aabb.minY && tileEntity.yCoord <= aabb.maxY &&
                        tileEntity.zCoord >= aabb.minZ && tileEntity.zCoord <= aabb.maxZ &&
                        tileEntity instanceof ISoulPossessable) {
                    ((ISoulPossessable) tileEntity).soulMending$onSoulPossession();
                    return;
                }
            }
        }
    }
}