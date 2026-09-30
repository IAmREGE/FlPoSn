package rege.chemicalcompound.mod115.flposn.client.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import rege.chemicalcompound.mod115.flposn.fluid.PowderSnowFluid;

@Mixin(net.minecraft.client.world.ClientWorld.class)
public abstract class ClientWorldMixin {
    @WrapOperation(method = {"addBlockBreakParticles", "addBlockHitParticle"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;getOutlineShape(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/util/shape/VoxelShape;"))
    private VoxelShape modifyOutlineShape(BlockState instance, BlockView blockView, BlockPos pos, Operation<VoxelShape> original) {
        return instance.getFluidState().getFluid() instanceof PowderSnowFluid ? VoxelShapes.fullCube() : original.call(instance, blockView, pos);
    }
}
