package rege.chemicalcompound.mod115.flposn.mixin;

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.function.Function;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.Block;
import net.minecraft.block.BlockItemKeys;
import net.minecraft.block.BlockState;
import rege.chemicalcompound.mod115.flposn.block.PowderSnowBlock;
import rege.chemicalcompound.mod115.flposn.fluid.PowderSnowFluid.Still;

@Mixin(net.minecraft.block.Blocks.class)
public abstract class BlocksMixin {
    @Shadow
    @Final
    public static Block POWDER_SNOW;

    @Unique
    private static ArrayDeque<BlockState> flposn$deferred;

    @ModifyArgs(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Blocks;register(Lnet/minecraft/block/BlockItemKey;Ljava/util/function/Function;Lnet/minecraft/block/AbstractBlock$Settings;)Lnet/minecraft/block/Block;"))
    private static void injected(Args args) {
        if (args.get(0) == BlockItemKeys.POWDER_SNOW) {
            args.set(1, (Function<Block.Settings, Block>)(settings -> new PowderSnowBlock(Still.POWDER_SNOW, settings)));
        }
    }

    @Inject(method = "method_1_6513", at = @At("HEAD"))
    private static void initDeferDeque(CallbackInfo ci) {
        flposn$deferred = new ArrayDeque<>();
    }

    @WrapOperation(method = "method_1_6513", at = @At(value = "INVOKE", target = "Ljava/util/Iterator;next()Ljava/lang/Object;"))
    private static <E> E deferIdAdd(Iterator<E> instance, Operation<E> original) {
        E value = original.call(instance);
        while (value instanceof BlockState) {
            BlockState state = (BlockState)value;
            if (state.getBlock() == POWDER_SNOW && state.get(PowderSnowBlock.LEVEL) != 0) {
                flposn$deferred.offer(state);
                if (!instance.hasNext()) {
                    break;
                }
                value = instance.next();
            } else {
                break;
            }
        }
        return value;
    }

    @Inject(method = "method_1_6513", at = @At("RETURN"))
    private static void deferIdProcess(CallbackInfo ci) {
        while (!flposn$deferred.isEmpty()) {
            BlockState state = flposn$deferred.poll();
            Block.STATE_IDS.add(state);
            state.initShapeCache();
        }
    }
}
