package iskallia.vault.init;

import iskallia.vault.fluid.VoidFluid;
import net.minecraft.fluid.Fluid;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModFluids {
    public static final DeferredRegister<Fluid> REGISTRY = DeferredRegister.create(ForgeRegistries.FLUIDS, "the_vault");


    public static final RegistryObject<VoidFluid.Source> VOID_LIQUID = REGISTRY.register("void_liquid", iskallia.vault.fluid.VoidFluid.Source::new);

    public static final RegistryObject<VoidFluid.Flowing> FLOWING_VOID_LIQUID = REGISTRY.register("flowing_void_liquid", iskallia.vault.fluid.VoidFluid.Flowing::new);
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\init\ModFluids.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */