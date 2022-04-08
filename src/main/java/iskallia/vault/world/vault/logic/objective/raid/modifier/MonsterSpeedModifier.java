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

public class MonsterSpeedModifier
        extends RaidModifier {
    private static final UUID MOB_SPEED_INCREASE = UUID.fromString("83efc139-b73b-4c14-82e6-fb624665fb59");

    public MonsterSpeedModifier(String name) {
        super(true, false, name);
    }


    public void affectRaidMob(MobEntity mob, float value) {
        ModifiableAttributeInstance attr = mob.getAttribute(Attributes.MOVEMENT_SPEED);
        if (attr != null) {
            attr.addPermanentModifier(new AttributeModifier(MOB_SPEED_INCREASE, "Raid Mob Speed", value, AttributeModifier.Operation.MULTIPLY_BASE));
        }
    }


    public void onVaultRaidFinish(VaultRaid vault, ServerWorld world, BlockPos controller, ActiveRaid raid, float value) {
    }


    public ITextComponent getDisplay(float value) {
        int percDisplay = Math.round(value * 100.0F);
        return (ITextComponent) (new StringTextComponent("+" + percDisplay + "% increased Mob Speed"))
                .withStyle(TextFormatting.RED);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\logic\objective\raid\modifier\MonsterSpeedModifier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */