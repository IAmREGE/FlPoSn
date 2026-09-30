package rege.chemicalcompound.mod115.flposn.client.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.Vec3d;
import rege.chemicalcompound.mod115.flposn.fluid.PowderSnowFluid;

@Mixin(net.minecraft.client.render.Camera.class)
public abstract class CameraMixin {
    @Shadow
    @Final
    private Mutable blockPos;
    @Shadow
    private Vec3d pos;

    @WrapOperation(method = "getSubmersionType", at = @At(
        value = "INVOKE", ordinal = 0,
        target = "Lnet/minecraft/block/BlockState;is(Ljava/lang/Object;)Z"
    ))
    private boolean injected(BlockState instance, Object value, Operation<Boolean> original) {
        if (value != Blocks.POWDER_SNOW) {
            return original.call(instance, value);
        }
        FluidState fluidState = instance.getFluidState();
        return fluidState.getFluid() instanceof PowderSnowFluid && this.pos.y < this.blockPos.getY() + fluidState.getHeight();
    }
}
