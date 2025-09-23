package com.inf1nlty.soulmending.mixin.entity;

import com.inf1nlty.soulmending.util.InventoryHelper;
import net.minecraft.src.DamageSource;
import net.minecraft.src.EntityLivingBase;
import net.minecraft.src.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityLivingBase.class)
public abstract class EntityLivingBaseMixin {

    @Inject(method = "attackEntityFrom", at = @At("HEAD"), cancellable = true)
    private void soulmending$injectAttackEntityFrom(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {

        if ((Object) this instanceof EntityPlayer player) {

            if (!player.worldObj.isRemote && player.getHealth() - amount <= 0.0F) {
                if (InventoryHelper.trySoulTotemRevive(player)) {
                    player.setHealth(2.0F);
                    cir.setReturnValue(false);
                }
            }
        }
    }
}