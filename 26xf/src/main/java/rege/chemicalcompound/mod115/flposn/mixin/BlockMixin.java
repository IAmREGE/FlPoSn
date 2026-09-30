package rege.chemicalcompound.mod115.flposn.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.Direction;
import rege.chemicalcompound.mod115.flposn.block.PowderSnowBlock;

@Mixin(net.minecraft.block.Block.class)
public abstract class BlockMixin {
//    @ModifyReturnValue(method = "shouldDrawSide", at = @At("RETURN"))
//    private static boolean injected(boolean original, final BlockState state, final BlockState otherState, final Direction side) {
//        return original || (state.getBlock() instanceof PowderSnowBlock && !state.isSideInvisible(world.getBlockState(otherPos), side));
//    }
}
