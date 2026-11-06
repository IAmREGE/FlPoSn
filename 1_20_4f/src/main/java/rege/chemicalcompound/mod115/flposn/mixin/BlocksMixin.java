package rege.chemicalcompound.mod115.flposn.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.sound.BlockSoundGroup;
import rege.chemicalcompound.mod115.flposn.block.PowderSnowBlock;
import rege.chemicalcompound.mod115.flposn.fluid.PowderSnowFluid.Still;

@Mixin(net.minecraft.block.Blocks.class)
public abstract class BlocksMixin {
//    @Shadow
//    @Final
//    public static Block POWDER_SNOW;
//
//    @Unique
//    private static ArrayDeque<BlockState> flposn$deferred;

    @WrapOperation(method = "<clinit>", at = @At(value = "NEW", target = "(Lnet/minecraft/block/AbstractBlock$Settings;)Lnet/minecraft/block/PowderSnowBlock;"))
    private static net.minecraft.block.PowderSnowBlock injected(Block.Settings properties, Operation<net.minecraft.block.PowderSnowBlock> original) {
        return null;
    }

    @ModifyArgs(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Blocks;register(Ljava/lang/String;Lnet/minecraft/block/Block;)Lnet/minecraft/block/Block;"))
    private static void injected(Args args) {
        if ("powder_snow".equals(args.get(0))) {
            args.set(1, new PowderSnowBlock(Still.POWDER_SNOW, Block.Settings.create().mapColor(MapColor.WHITE).strength(0.25F).sounds(BlockSoundGroup.POWDER_SNOW).dynamicBounds().solidBlock(Blocks::never)));
        }
    }

//    @Inject(method = "method_1_6513", at = @At("HEAD"))
//    private static void initDeferDeque(CallbackInfo ci) {
//        flposn$deferred = new ArrayDeque<>();
//    }
//
//    @WrapOperation(method = "method_1_6513", at = @At(value = "INVOKE", target = "Ljava/util/Iterator;next()Ljava/lang/Object;"))
//    private static <E> E deferIdAdd(Iterator<E> instance, Operation<E> original) {
//        E value = original.call(instance);
//        while (value instanceof BlockState) {
//            BlockState state = (BlockState)value;
//            if (state.getBlock() == POWDER_SNOW && state.get(PowderSnowBlock.LEVEL) != 0) {
//                flposn$deferred.offer(state);
//                if (!instance.hasNext()) {
//                    break;
//                }
//                value = instance.next();
//            } else {
//                break;
//            }
//        }
//        return value;
//    }
//
//    @Inject(method = "method_1_6513", at = @At("RETURN"))
//    private static void deferIdProcess(CallbackInfo ci) {
//        while (!flposn$deferred.isEmpty()) {
//            BlockState state = flposn$deferred.poll();
//            Block.STATE_IDS.add(state);
//            state.initShapeCache();
//        }
//    }
}
