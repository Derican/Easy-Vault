// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.skill.ability.effect.sub;

import iskallia.vault.entity.EternalEntity;
import iskallia.vault.skill.ability.config.sub.SummonEternalDamageConfig;
import iskallia.vault.skill.ability.effect.SummonEternalAbility;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;

import java.util.UUID;

public class SummonEternalDamageAbility extends SummonEternalAbility<SummonEternalDamageConfig>
{
    private static final UUID INCREASED_DAMAGE_MOD_UUID;
    
    @Override
    protected void postProcessEternal(final EternalEntity eternalEntity, final SummonEternalDamageConfig config) {
        super.postProcessEternal(eternalEntity, config);
        final ModifiableAttributeInstance instance = eternalEntity.getAttribute(Attributes.ATTACK_DAMAGE);
        instance.addTransientModifier(new AttributeModifier(SummonEternalDamageAbility.INCREASED_DAMAGE_MOD_UUID, "Eternal increased damage", (double)config.getIncreasedDamagePercent(), AttributeModifier.Operation.MULTIPLY_BASE));
        eternalEntity.sizeMultiplier *= 1.2f;
    }
    
    static {
        INCREASED_DAMAGE_MOD_UUID = UUID.fromString("68ab19f2-a345-49ed-b5c4-0746d8508685");
    }
}
