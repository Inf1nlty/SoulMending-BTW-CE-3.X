package com.inf1nlty.soultotem.mixin.world;

import com.inf1nlty.soultotem.util.PotionEffectQueue;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {

    @Inject(method = "tick", at = @At("TAIL"))
    private void soulmending$flushPotionEffectQueue(CallbackInfo ci) {
        PotionEffectQueue.flush();
    }
}