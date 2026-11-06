package rege.chemicalcompound.mod115.flposn.mixin;

import java.util.function.Function;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.Block;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.PowderSnowBucketItem;
import net.minecraft.sound.SoundEvent;
import rege.chemicalcompound.mod115.flposn.fluid.PowderSnowFluid;

@Mixin(net.minecraft.item.Items.class)
public abstract class ItemsMixin {
    @WrapOperation(method = "<clinit>", at = @At(value = "NEW", target = "(Lnet/minecraft/block/Block;Lnet/minecraft/sound/SoundEvent;Lnet/minecraft/item/Item$Settings;)Lnet/minecraft/item/PowderSnowBucketItem;"))
    private static PowderSnowBucketItem injected(Block block, SoundEvent placeSound, Item.Settings settings, Operation<PowderSnowBucketItem> original) {
        return null;
    }

    @ModifyArgs(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Items;register(Ljava/lang/String;Lnet/minecraft/item/Item;)Lnet/minecraft/item/Item;"))
    private static void injected(Args args) {
        if ("powder_snow_bucket".equals(args.get(0))) {
            args.set(1, new BucketItem(PowderSnowFluid.Still.POWDER_SNOW, new Item.Settings().maxCount(1)));
        }
    }
}
