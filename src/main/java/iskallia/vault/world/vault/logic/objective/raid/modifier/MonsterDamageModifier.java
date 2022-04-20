package iskallia.vault.world.vault.logic.objective.raid.modifier;

import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.logic.objective.raid.ActiveRaid;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.server.ServerWorld;

import java.util.UUID;

public class MonsterDamageModifier extends RaidModifier {
    private static final UUID MOB_DAMAGE_INCREASE;

    public MonsterDamageModifier(final String name) {
        super(true, false, name);
    }

    @Override
    public void affectRaidMob(final MobEntity mob, final float value) {
        final ModifiableAttributeInstance attr = mob.getAttribute(Attributes.ATTACK_DAMAGE);
        if (attr != null) {
            attr.addPermanentModifier(new AttributeModifier(MonsterDamageModifier.MOB_DAMAGE_INCREASE, "Raid Mob Damage Increase", (double) value, AttributeModifier.Operation.MULTIPLY_BASE));
        }
    }

    @Override
    public void onVaultRaidFinish(final VaultRaid vault, final ServerWorld world, final BlockPos controller, final ActiveRaid raid, final float value) {
    }

    @Override
    public ITextComponent getDisplay(final float value) {
        final int percDisplay = Math.round(value * 100.0f);
        return (ITextComponent) new StringTextComponent("+" + percDisplay + "% increased Mob Damage").withStyle(TextFormatting.RED);
    }

    static {
        MOB_DAMAGE_INCREASE = UUID.fromString("827b6afb-f01b-498c-9206-970f85da732a");
    }
}
