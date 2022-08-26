package iskallia.vault.aura.type;

import com.google.gson.annotations.Expose;
import iskallia.vault.aura.ActiveAura;
import iskallia.vault.config.EternalAuraConfig;
import iskallia.vault.entity.EternalEntity;
import iskallia.vault.util.EntityHelper;
import net.minecraft.entity.MobEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

public class MobEffectAuraConfig extends EternalAuraConfig.AuraConfig {
    @Expose
    private final String effect;
    @Expose
    private final int amplifier;

    public MobEffectAuraConfig(final Effect effect, final int amplifier, final String name, final String icon) {
        super("Mob_" + name, name, "Applies " + name + " to enemies in its radius", icon, 8.0f);
        this.effect = effect.getRegistryName().toString();
        this.amplifier = amplifier;
    }

    public MobEffectAuraConfig(final String name, final String displayName, final String description, final String iconPath, final float radius, final Effect effect, final int amplifier) {
        super(name, displayName, description, iconPath, radius);
        this.effect = effect.getRegistryName().toString();
        this.amplifier = amplifier;
    }

    @Override
    public void onTick(final World world, final ActiveAura aura) {
        super.onTick(world, aura);
        if (world.getGameTime() % 20L != 0L) {
            return;
        }
        final Effect effect = ForgeRegistries.POTIONS.getValue(new ResourceLocation(this.effect));
        if (effect == null) {
            return;
        }
        EntityHelper.getNearby(world, new BlockPos(aura.getOffset()), aura.getRadius(), MobEntity.class).forEach(entity -> {
            if (!(entity instanceof EternalEntity)) {
                entity.addEffect(new EffectInstance(effect, 259, this.amplifier, true, true));
            }
        });
    }
}
