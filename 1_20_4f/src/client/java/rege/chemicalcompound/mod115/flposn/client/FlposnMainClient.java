package rege.chemicalcompound.mod115.flposn.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.minecraft.util.Identifier;

import static rege.chemicalcompound.mod115.flposn.fluid.PowderSnowFluid.Flowing.FLOWING_POWDER_SNOW;
import static rege.chemicalcompound.mod115.flposn.fluid.PowderSnowFluid.Still.POWDER_SNOW;

public class FlposnMainClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        Identifier a = new Identifier("flposn", "block/transparent");
        FluidRenderHandlerRegistry.INSTANCE.register(POWDER_SNOW, FLOWING_POWDER_SNOW, new SimpleFluidRenderHandler(a, a));
    }
}
