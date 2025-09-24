package com.inf1nlty.soultotem.mixin.world.block;

import com.inf1nlty.soultotem.block.STBlocks;
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

        if (targetId == STBlocks.soulTotem.blockID || targetId == STBlocks.emptySoulTotem.blockID) {
            ci.cancel();
        }
    }
}