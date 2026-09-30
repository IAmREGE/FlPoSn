package rege.chemicalcompound.mod115.flposn.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderingRegistry;
import net.minecraft.client.color.block.BlockColorProviders;
import net.minecraft.client.render.block.model.FluidModel;
import net.minecraft.client.render.model.ModelTexture;
import net.minecraft.util.Identifier;

import static rege.chemicalcompound.mod115.flposn.fluid.PowderSnowFluid.Flowing.FLOWING_POWDER_SNOW;
import static rege.chemicalcompound.mod115.flposn.fluid.PowderSnowFluid.Still.POWDER_SNOW;

public class FlposnMainClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        Identifier a = Identifier.of("flposn", "block/transparent");
        FluidRenderingRegistry.register(
            POWDER_SNOW, FLOWING_POWDER_SNOW, new FluidModel.Unbaked(
                new ModelTexture(a), new ModelTexture(a), new ModelTexture(a),
                BlockColorProviders.constant(-1)
            )
        );
    }
}
