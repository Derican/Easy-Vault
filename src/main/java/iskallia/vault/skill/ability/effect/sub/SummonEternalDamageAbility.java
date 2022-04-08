package iskallia.vault.skill.ability.effect.sub;

import iskallia.vault.entity.EternalEntity;
import iskallia.vault.skill.ability.config.SummonEternalConfig;
import iskallia.vault.skill.ability.config.sub.SummonEternalDamageConfig;
import iskallia.vault.skill.ability.effect.SummonEternalAbility;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;

import java.util.UUID;

public class SummonEternalDamageAbility
        extends SummonEternalAbility<SummonEternalDamageConfig> {
    private static final UUID INCREASED_DAMAGE_MOD_UUID = UUID.fromString("68ab19f2-a345-49ed-b5c4-0746d8508685");


    protected void postProcessEternal(EternalEntity eternalEntity, SummonEternalDamageConfig config) {
        super.postProcessEternal(eternalEntity, (SummonEternalConfig) config);

        ModifiableAttributeInstance instance = eternalEntity.getAttribute(Attributes.ATTACK_DAMAGE);
        instance.addTransientModifier(new AttributeModifier(INCREASED_DAMAGE_MOD_UUID, "Eternal increased damage", config
                .getIncreasedDamagePercent(), AttributeModifier.Operation.MULTIPLY_BASE));

        eternalEntity.sizeMultiplier *= 1.2F;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\ability\effect\sub\SummonEternalDamageAbility.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */