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

import java.util.Random;

public class OverworldOreFeature extends OreFeature {
    public static Feature<OreFeatureConfig> INSTANCE;

    public OverworldOreFeature(final Codec<OreFeatureConfig> codec) {
        super(codec);
    }

    public boolean place(final ISeedReader world, final ChunkGenerator gen, final Random random, final BlockPos pos, final OreFeatureConfig config) {
        if (world.getLevel().dimension() != World.OVERWORLD) {
            return false;
        }
        if (config.size != 1) {
            return super.place(world, gen, random, pos, config);
        }
        if (config.target.test(world.getBlockState(pos), random)) {
            world.setBlock(pos, config.state, 2);
            return true;
        }
        return false;
    }

    public static void register(final RegistryEvent.Register<Feature<?>> event) {
        (OverworldOreFeature.INSTANCE = new OverworldOreFeature(OreFeatureConfig.CODEC)).setRegistryName(Vault.id("overworld_ore"));
        event.getRegistry().register(OverworldOreFeature.INSTANCE);
    }
}
