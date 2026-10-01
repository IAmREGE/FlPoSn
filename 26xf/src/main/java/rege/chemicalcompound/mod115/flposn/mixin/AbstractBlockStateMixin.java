package rege.chemicalcompound.mod115.flposn.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.block.BlockState;
import net.minecraft.block.EntityShapeContext;
import net.minecraft.block.ShapeContext;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import rege.chemicalcompound.mod115.flposn.fluid.PowderSnowFluid;

@Mixin(net.minecraft.block.AbstractBlock.AbstractBlockState.class)
public abstract class AbstractBlockStateMixin {
    @Shadow
    public abstract FluidState getFluidState();

    @ModifyReturnValue(method = "getOutlineShape(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/ShapeContext;)Lnet/minecraft/util/shape/VoxelShape;", at = @At("RETURN"))
    private VoxelShape unionShape(VoxelShape original, final BlockView world, final BlockPos pos, final ShapeContext context) {
        FluidState fluidState = this.getFluidState();
        BlockState blockState = fluidState.getBlockState();
        return blockState != (Object)this && fluidState.getFluid() instanceof PowderSnowFluid && context instanceof EntityShapeContext ? VoxelShapes.union(original, blockState.getOutlineShape(world, pos, context)) : original;
    }

    @ModifyReturnValue(method = "getCollisionShape(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/ShapeContext;)Lnet/minecraft/util/shape/VoxelShape;", at = @At("RETURN"))
    private VoxelShape unionShape2(VoxelShape original, final BlockView world, final BlockPos pos, final ShapeContext context) {
        FluidState fluidState = this.getFluidState();
        BlockState blockState = fluidState.getBlockState();
        return blockState != (Object)this && fluidState.getFluid() instanceof PowderSnowFluid && context instanceof EntityShapeContext ? VoxelShapes.union(original, blockState.getCollisionShape(world, pos, context)) : original;
    }
}
