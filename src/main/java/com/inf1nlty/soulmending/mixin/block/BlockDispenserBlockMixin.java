package com.inf1nlty.soulmending.mixin.block;

import com.inf1nlty.soulmending.block.SoulMendingBlocks;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import btw.block.blocks.BlockDispenserBlock;
import btw.world.util.BlockPos;

@Mixin(BlockDispenserBlock.class)
public abstract class BlockDispenserBlockMixin {

    @Inject(method = "consumeFacingBlock", at = @At("HEAD"), cancellable = true, remap = false)
    private void injectSoulTotemSkip(World world, int i, int j, int k, CallbackInfo ci) {
        int facing = ((BlockDispenserBlock)(Object)this).getFacing(world, i, j, k);
        BlockPos targetPos = new BlockPos(i, j, k);
        targetPos.addFacingAsOffset(facing);
        int targetId = world.getBlockId(targetPos.x, targetPos.y, targetPos.z);

        if (targetId == SoulMendingBlocks.soulTotem.blockID || targetId == SoulMendingBlocks.emptySoulTotem.blockID) {
            ci.cancel();
        }
    }
}