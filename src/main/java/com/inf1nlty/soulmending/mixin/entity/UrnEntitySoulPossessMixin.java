package com.inf1nlty.soulmending.mixin.entity;

import btw.entity.UrnEntity;
import com.inf1nlty.soulmending.util.ISoulPossessable;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.List;

@Mixin(UrnEntity.class)
public abstract class UrnEntitySoulPossessMixin {

    @Inject(method = "looseSoulEffects", at = @At("TAIL"))
    public void onLooseSoulEffects(World worldObj, double xCoord, double yCoord, double zCoord, CallbackInfo ci) {
        AxisAlignedBB possessionBox = AxisAlignedBB.getAABBPool().getAABB(
                xCoord - 4.0F, yCoord - 4.0F, zCoord - 4.0F,
                xCoord + 4.0F, yCoord + 4.0F, zCoord + 4.0F
        );

        // Entity
        @SuppressWarnings("unchecked")
        List<Entity> nearbyEntities = worldObj.getEntitiesWithinAABB(Entity.class, possessionBox);
        for (Entity entity : nearbyEntities) {
            if (!(entity instanceof EntityCreature) && entity instanceof ISoulPossessable possessable) {
                possessable.soulMending$onSoulPossession();
                return;
            }
        }

        // TileEntity
        @SuppressWarnings("unchecked")
        List<TileEntity> tileEntities = worldObj.loadedTileEntityList;
        for (TileEntity te : tileEntities) {
            if (te instanceof ISoulPossessable possessableTile &&
                    te.xCoord >= possessionBox.minX && te.xCoord <= possessionBox.maxX &&
                    te.yCoord >= possessionBox.minY && te.yCoord <= possessionBox.maxY &&
                    te.zCoord >= possessionBox.minZ && te.zCoord <= possessionBox.maxZ) {
                possessableTile.soulMending$onSoulPossession();
                return;
            }
        }
    }
}