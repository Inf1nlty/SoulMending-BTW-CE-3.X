package com.inf1nlty.soultotem.mixin.block;

import com.inf1nlty.soultotem.util.ISoulPossessable;
import btw.block.tileentity.HopperTileEntity;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(HopperTileEntity.class)
public abstract class HopperTileEntitySoulPossessMixin {

    @Inject(method = "updateEntity", at = @At("TAIL"))
    public void onUpdateEntity(CallbackInfo ci) {
        HopperTileEntity self = (HopperTileEntity)(Object)this;
        World world = self.worldObj;

        if (!world.isRemote
                && self.containedSoulCount > 0
                && self.filterItemID == net.minecraft.src.Block.slowSand.blockID) {

            int range = 16;
            AxisAlignedBB aabb = AxisAlignedBB.getAABBPool().getAABB(
                    self.xCoord - range, self.yCoord - range, self.zCoord - range,
                    self.xCoord + range, self.yCoord + range, self.zCoord + range
            );

            boolean possessed = false;
            @SuppressWarnings("unchecked")
            List<Entity> creatures = world.getEntitiesWithinAABB(net.minecraft.src.EntityCreature.class, aabb);
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