package rege.chemicalcompound.mod115.flposn.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;
import rege.chemicalcompound.mod115.flposn.fluid.PowderSnowFluid;

@Mixin(net.minecraft.entity.Entity.class)
public abstract class EntityMixin {
    @Shadow
    public abstract boolean collidesWithStateAtPos(BlockPos pos, BlockState state);

    @Shadow
    public abstract World getWorld();

    @Shadow
    public abstract Box getBoundingBox();

    @Inject(method = "checkBlockCollision", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;onEntityCollision(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/Entity;)V", shift = At.Shift.AFTER))
    private void injected(CallbackInfo ci, @Local BlockPos.Mutable mutable, @Local BlockState blockState) {
        FluidState fluidState = blockState.getFluidState();
        if (fluidState.getFluid() instanceof PowderSnowFluid) {
            Entity THIS = (Entity)(Object)this;
            if (VoxelShapes.matchesAnywhere(VoxelShapes.cuboid(0, 0, 0, 1, fluidState.getHeight(this.getWorld(), mutable), 1).offset(mutable.getX(), mutable.getY(), mutable.getZ()), VoxelShapes.cuboid(this.getBoundingBox()), BooleanBiFunction.AND)) {
                PowderSnowFluid.onEntityCollision(this.getWorld(), mutable, THIS);
            }
        }
    }
}
