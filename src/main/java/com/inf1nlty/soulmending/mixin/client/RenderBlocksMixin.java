package com.inf1nlty.soulmending.mixin.client;

import com.inf1nlty.soulmending.block.BlockEmptySoulTotem;
import com.inf1nlty.soulmending.block.BlockSoulTotem;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RenderBlocks.class)
public abstract class RenderBlocksMixin {

    @Inject(method = "renderBlockByRenderType", at = @At("HEAD"), cancellable = true)
    private void injectSoulTotemModel(Block block, int x, int y, int z, CallbackInfoReturnable<Boolean> cir) {
        if (block instanceof BlockSoulTotem || block instanceof BlockEmptySoulTotem) {
            cir.setReturnValue(false);
        }
    }
}