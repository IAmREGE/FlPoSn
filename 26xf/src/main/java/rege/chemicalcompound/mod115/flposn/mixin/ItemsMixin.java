package rege.chemicalcompound.mod115.flposn.mixin;

import java.util.function.Function;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import net.minecraft.block.BlockItemKeys;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import rege.chemicalcompound.mod115.flposn.fluid.PowderSnowFluid;

@Mixin(net.minecraft.item.Items.class)
public abstract class ItemsMixin {
    @ModifyArgs(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Items;register(Lnet/minecraft/block/BlockItemKey;Ljava/util/function/Function;Lnet/minecraft/item/Item$Settings;)Lnet/minecraft/item/Item;"))
    private static void injected(Args args) {
        if (args.get(0) == BlockItemKeys.POWDER_SNOW) {
            args.set(1, (Function<Item.Settings, Item>)(settings -> new BucketItem(PowderSnowFluid.Still.POWDER_SNOW, settings)));
        }
    }
}
