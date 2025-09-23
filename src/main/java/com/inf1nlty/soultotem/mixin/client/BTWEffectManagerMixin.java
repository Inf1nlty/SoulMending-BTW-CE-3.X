package com.inf1nlty.soultotem.mixin.client;

import com.inf1nlty.soultotem.client.EntitySoulFX;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import btw.client.fx.BTWEffectManager;
import btw.client.fx.EffectHandler;

@Mixin(value = BTWEffectManager.class, remap = false)
public abstract class BTWEffectManagerMixin {

    @Inject(method = "initEffects", at = @At("TAIL"))
    private static void soulmending$injectCustomSoulParticle(CallbackInfo ci) {
        int id = 2286;
        EffectHandler.effectMap.compute(id, (k, original) -> (mc, world, player, x, y, z, data) -> {
            if (data == 10000) {
                EntitySoulFX.spawnRing(world, x + 0.5, y, z + 0.5);

            } else if (data == 10010) {
                EntitySoulFX.spawnGatherToBlock(world, x, y, z);

            } else if (data == 10011) {
                EntitySoulFX.findAndSpawnGatherToItem(world, x, y, z);

            } else if (data == 10086) {
                    EntitySoulFX.spawnRingToPlayer(world, x, y, z);
            }

            if (original != null) {
                original.playEffect(mc, world, player, x, y, z, data);
            }
        });
    }
}