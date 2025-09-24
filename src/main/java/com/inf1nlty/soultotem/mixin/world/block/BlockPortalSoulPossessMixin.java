package com.inf1nlty.soultotem.mixin.world.block;

import com.inf1nlty.soultotem.util.ISoulPossessable;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Random;

@Mixin(BlockPortal.class)
public abstract class BlockPortalSoulPossessMixin {

    @Inject(method = "updateTick", at = @At("TAIL"))
    public void onUpdateTick(World world, int x, int y, int z, Random rand, CallbackInfo ci) {

        if (rand.nextInt(2) == 0 && world.provider.isSurfaceWorld()) {
            int range = 16;
            AxisAlignedBB aabb = AxisAlignedBB.getAABBPool().getAABB(
                    x - range, y - range, z - range,
                    x + range, y + range, z + range
            );

            // EntityCreature
            boolean possessed = false;
            @SuppressWarnings("unchecked")
            List<Entity> creatures = world.getEntitiesWithinAABB(EntityCreature.class, aabb);
            for (Entity entity : creatures) {
                if (entity instanceof EntityCreature creature && creature.isPossessed()) {
                    return;
                }
            }

            if (!possessed) {
                // Entity
                @SuppressWarnings("unchecked")
                List<Entity> entityList = world.getEntitiesWithinAABB(Entity.class, aabb);
                for (Entity entity : entityList) {
                    if (!(entity instanceof EntityCreature) && entity instanceof ISoulPossessable possessable) {
                        possessable.soulMending$onSoulPossession();
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
                            tileEntity instanceof ISoulPossessable possessableTile) {
                        possessableTile.soulMending$onSoulPossession();
                        return;
                    }
                }
            }
        }
    }
}