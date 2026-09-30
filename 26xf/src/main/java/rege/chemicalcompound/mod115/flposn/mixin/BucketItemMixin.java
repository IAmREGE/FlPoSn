package rege.chemicalcompound.mod115.flposn.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import net.minecraft.fluid.Fluid;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import rege.chemicalcompound.mod115.flposn.fluid.PowderSnowFluid;

@Mixin(net.minecraft.item.BucketItem.class)
public abstract class BucketItemMixin {
    @Shadow
    @Final
    protected Fluid fluid;

    @ModifyVariable(method = "playEmptyingSound", at = @At("STORE"), ordinal = 0)
    private SoundEvent injected(SoundEvent soundEvent) {
        return this.fluid instanceof PowderSnowFluid ? SoundEvents.ITEM_BUCKET_EMPTY_POWDER_SNOW : soundEvent;
    }
}
