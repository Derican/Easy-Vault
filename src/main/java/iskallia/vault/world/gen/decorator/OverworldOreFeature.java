package iskallia.vault.world.gen.decorator;

import com.mojang.serialization.Codec;
import iskallia.vault.Vault;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.Random;

public class OverworldOreFeature extends OreFeature {
    public static Feature<OreFeatureConfig> INSTANCE;

    public OverworldOreFeature(Codec<OreFeatureConfig> codec) {
        super(codec);
    }


    public boolean place(ISeedReader world, ChunkGenerator gen, Random random, BlockPos pos, OreFeatureConfig config) {
        if (world.getLevel().dimension() != World.OVERWORLD) {
            return false;
        }

        if (config.size == 1) {
            if (config.target.test(world.getBlockState(pos), random)) {
                world.setBlock(pos, config.state, 2);
                return true;
            }

            return false;
        }
        return super.place(world, gen, random, pos, config);
    }


    public static void register(RegistryEvent.Register<Feature<?>> event) {
        INSTANCE = (Feature<OreFeatureConfig>) new OverworldOreFeature(OreFeatureConfig.CODEC);
        INSTANCE.setRegistryName(Vault.id("overworld_ore"));
        event.getRegistry().register((IForgeRegistryEntry) INSTANCE);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\gen\decorator\OverworldOreFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */