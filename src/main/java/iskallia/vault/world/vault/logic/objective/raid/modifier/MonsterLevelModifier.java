package iskallia.vault.world.vault.logic.objective.raid.modifier;

import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.logic.objective.raid.ActiveRaid;
import net.minecraft.entity.MobEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.server.ServerWorld;

public class MonsterLevelModifier extends RaidModifier {
    public MonsterLevelModifier(final String name) {
        super(false, false, name);
    }

    @Override
    public void affectRaidMob(final MobEntity mob, final float value) {
    }

    @Override
    public void onVaultRaidFinish(final VaultRaid vault, final ServerWorld world, final BlockPos controller, final ActiveRaid raid, final float value) {
    }

    @Override
    public ITextComponent getDisplay(final float value) {
        return (ITextComponent) new StringTextComponent("+" + this.getLevelAdded(value) + " to Monster Level").withStyle(TextFormatting.RED);
    }

    public int getLevelAdded(final float value) {
        return Math.round(value);
    }
}
