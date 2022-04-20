package iskallia.vault.init;

import iskallia.vault.fluid.VoidFluid;
import net.minecraft.fluid.Fluid;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class ModFluids {
    public static final DeferredRegister<Fluid> REGISTRY;
    public static final RegistryObject<VoidFluid.Source> VOID_LIQUID;
    public static final RegistryObject<VoidFluid.Flowing> FLOWING_VOID_LIQUID;

    static {
        REGISTRY = DeferredRegister.create(ForgeRegistries.FLUIDS, "the_vault");
        VOID_LIQUID = ModFluids.REGISTRY.register("void_liquid", (Supplier) VoidFluid.Source::new);
        FLOWING_VOID_LIQUID = ModFluids.REGISTRY.register("flowing_void_liquid", (Supplier) VoidFluid.Flowing::new);
    }
}
