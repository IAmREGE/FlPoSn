package rege.chemicalcompound.mod115.flposn.client.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.block.enums.CameraSubmersionType;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import rege.chemicalcompound.mod115.flposn.fluid.PowderSnowFluid;

@Mixin(net.minecraft.client.render.Camera.class)
public abstract class CameraMixin {
    @Shadow
    private @Nullable World area;
    @Shadow
    @Final
    private Mutable blockPos;
    @Shadow
    private Vec3d pos;

    @ModifyReturnValue(method = "getSubmersionType", at = @At("RETURN"))
    private CameraSubmersionType injected(CameraSubmersionType original) {
        if (original == CameraSubmersionType.POWDER_SNOW && this.area != null) {
            final FluidState FS = this.area.getFluidState(this.blockPos);
            if ((!(FS.getFluid() instanceof PowderSnowFluid)) || this.pos.y >= this.blockPos.getY() + FS.getHeight()) {
                return CameraSubmersionType.NONE;
            }
        }
        return original;
    }
}
