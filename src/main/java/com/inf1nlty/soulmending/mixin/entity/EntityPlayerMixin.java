package com.inf1nlty.soulmending.mixin.entity;

import com.inf1nlty.soulmending.util.InventoryHelper;
import com.inf1nlty.soulmending.util.ISoulPossessable;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(EntityPlayer.class)
public abstract class EntityPlayerMixin implements ISoulPossessable {

    public void soulMending$onSoulPossession() {
        EntityPlayer self = (EntityPlayer)(Object)this;
        InventoryHelper.addSoulToBestTotem(self.inventory, self.worldObj, (int)self.posX, (int)self.posY, (int)self.posZ, 10);
    }

    @Inject(method = "onLivingUpdate", at = @At("HEAD"))
    private void soulmending$netherAirPossession(CallbackInfo ci) {
        EntityPlayer self = (EntityPlayer) (Object) this;
        World world = self.worldObj;
        if (!world.isRemote && world.provider.dimensionId == -1) {
            Random rand = world.rand;
            if (rand.nextInt(1000) == 0) {
                this.soulMending$onSoulPossession();
            }
        }
    }
}