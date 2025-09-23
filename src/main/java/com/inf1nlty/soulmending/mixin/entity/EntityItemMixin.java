package com.inf1nlty.soulmending.mixin.entity;

import com.inf1nlty.soulmending.block.SoulMendingBlocks;
import com.inf1nlty.soulmending.item.SoulTotemItem;
import com.inf1nlty.soulmending.util.InventoryHelper;
import com.inf1nlty.soulmending.util.ISoulPossessable;
import net.minecraft.src.DamageSource;
import net.minecraft.src.EntityItem;
import net.minecraft.src.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityItem.class)
public abstract class EntityItemMixin implements ISoulPossessable {

    @Inject(method = "attackEntityFrom", at = @At("HEAD"), cancellable = true)
    private void soulmending$protectFromExplosionAndFire(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        EntityItem self = (EntityItem)(Object)this;
        ItemStack original = self.getEntityItem();

        if (original == null) return;

        if (original.getItem() == SoulMendingBlocks.soulTotemItem || original.getItem() == SoulMendingBlocks.emptySoulTotemItem) {
            if (source.isExplosion() || source.isFireDamage()|| source == DamageSource.cactus) {
                cir.setReturnValue(false);
            }
        }
    }

    @Override
    public void soulMending$onSoulPossession() {
        EntityItem self = (EntityItem)(Object)this;
        ItemStack original = self.getEntityItem();

        if (original == null) return;

        if (original.getItem() != SoulMendingBlocks.soulTotemItem &&
                original.getItem() != SoulMendingBlocks.emptySoulTotemItem) {
            return;
        }

        int before = SoulTotemItem.getStoredSoul(original);
        ItemStack result = InventoryHelper.addSoulToStack(original, 10);
        int after = SoulTotemItem.getStoredSoul(result);

        if (after > before || result != original) {
            self.setEntityItemStack(result);
            self.worldObj.playAuxSFX(2286, (int)self.posX, (int)self.posY, (int)self.posZ, 10010);
        }
    }
}