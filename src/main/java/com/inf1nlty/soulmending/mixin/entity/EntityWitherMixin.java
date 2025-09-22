package com.inf1nlty.soulmending.mixin.entity;

import btw.item.BTWItems;
import com.inf1nlty.soulmending.EnchantmentSoulMending;
import net.minecraft.src.EntityWither;
import net.minecraft.src.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityWither.class)
public abstract class EntityWitherMixin {

    @Inject(method = "dropFewItems", at = @At("TAIL"))
    private void soulmending$dropSoulMendingScroll(boolean recentlyHit, int lootingLevel, CallbackInfo ci) {
        EntityWither self = (EntityWither)(Object)this;
        ItemStack itemstack = new ItemStack(BTWItems.arcaneScroll, 1, EnchantmentSoulMending.SOUL_MENDING_ID);
        self.entityDropItem(itemstack, 0.0F);
    }
}