// 
// Decompiled by Procyon v0.6.0
// 

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

public class MonsterSpeedModifier extends RaidModifier
{
    private static final UUID MOB_SPEED_INCREASE;
    
    public MonsterSpeedModifier(final String name) {
        super(true, false, name);
    }
    
    @Override
    public void affectRaidMob(final MobEntity mob, final float value) {
        final ModifiableAttributeInstance attr = mob.getAttribute(Attributes.MOVEMENT_SPEED);
        if (attr != null) {
            attr.addPermanentModifier(new AttributeModifier(MonsterSpeedModifier.MOB_SPEED_INCREASE, "Raid Mob Speed", (double)value, AttributeModifier.Operation.MULTIPLY_BASE));
        }
    }
    
    @Override
    public void onVaultRaidFinish(final VaultRaid vault, final ServerWorld world, final BlockPos controller, final ActiveRaid raid, final float value) {
    }
    
    @Override
    public ITextComponent getDisplay(final float value) {
        final int percDisplay = Math.round(value * 100.0f);
        return (ITextComponent)new StringTextComponent("+" + percDisplay + "% increased Mob Speed").withStyle(TextFormatting.RED);
    }
    
    static {
        MOB_SPEED_INCREASE = UUID.fromString("83efc139-b73b-4c14-82e6-fb624665fb59");
    }
}
