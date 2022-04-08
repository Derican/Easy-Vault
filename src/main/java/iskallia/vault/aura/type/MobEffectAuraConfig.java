package iskallia.vault.aura.type;

import com.google.gson.annotations.Expose;
import iskallia.vault.aura.ActiveAura;
import iskallia.vault.config.EternalAuraConfig;
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

    public MobEffectAuraConfig(Effect effect, int amplifier, String name, String icon) {
        super("Mob_" + name, name, "Applies " + name + " to enemies in its radius", icon, 8.0F);
        this.effect = effect.getRegistryName().toString();
        this.amplifier = amplifier;
    }

    @Expose
    private final int amplifier;

    public void onTick(World world, ActiveAura aura) {
        super.onTick(world, aura);

        if (world.getGameTime() % 20L != 0L) {
            return;
        }
        Effect effect = (Effect) ForgeRegistries.POTIONS.getValue(new ResourceLocation(this.effect));
        if (effect == null) {
            return;
        }
        EntityHelper.getNearby((IWorld) world, (Vector3i) new BlockPos(aura.getOffset()), aura.getRadius(), MobEntity.class).forEach(entity -> {
            if (!(entity instanceof iskallia.vault.entity.EternalEntity))
                entity.addEffect(new EffectInstance(effect, 259, this.amplifier, true, true));
        });
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\aura\type\MobEffectAuraConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */