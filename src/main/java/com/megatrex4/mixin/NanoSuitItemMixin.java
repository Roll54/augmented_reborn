package com.megatrex4.mixin;

import com.megatrex4.items.ActivatableItem;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import techreborn.config.TechRebornConfig;
import techreborn.items.armor.NanoSuitItem;

@Mixin(NanoSuitItem.class)
public abstract class NanoSuitItemMixin {

    private static final long ENERGY_COST = TechRebornConfig.nanoSuitNightVisionCost;

    @Inject(method = "tickArmor", at = @At("HEAD"), cancellable = true)
    private void tickArmor(ItemStack stack, PlayerEntity playerEntity, CallbackInfo info) {
        if (!(stack.getItem() instanceof ActivatableItem activatable) || !activatable.isActivated(stack)) {
            removeNightVision(playerEntity);
            return;
        }

        if (playerEntity.getInventory().getArmorStack(3) == stack && tryUseEnergy(stack, playerEntity, ENERGY_COST)) {
            playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 220, 1, false, false));
        } else {
            removeNightVision(playerEntity);
        }
    }

    private boolean tryUseEnergy(ItemStack stack, PlayerEntity player, long energyCost) {
        NanoSuitItem item = (NanoSuitItem) (Object) this;
        return item.tryUseEnergy(stack, energyCost);
    }

    private void removeNightVision(PlayerEntity playerEntity) {
        if (playerEntity.hasStatusEffect(StatusEffects.NIGHT_VISION)) {
            playerEntity.removeStatusEffect(StatusEffects.NIGHT_VISION);
        }
    }
}
