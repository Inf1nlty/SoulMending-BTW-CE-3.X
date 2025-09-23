package com.inf1nlty.soultotem.mixin.entity;

import btw.item.BTWItems;
import com.inf1nlty.soultotem.STEnchantments;
import net.minecraft.src.EntityGhast;
import net.minecraft.src.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(EntityGhast.class)
public abstract class EntityGhastMixin {

    @Inject(method = "checkForScrollDrop", at = @At("HEAD"))
    private void soulmending$extraSoulMendingScroll(CallbackInfo ci) {
        EntityGhast self = (EntityGhast)(Object)this;
        Random rand = self.rand;

        if (rand.nextInt(250) == 0) {
            ItemStack itemstack = new ItemStack(BTWItems.arcaneScroll, 1, STEnchantments.SOUL_MENDING_ID);
            self.entityDropItem(itemstack, 0.0F);
        }
    }
}