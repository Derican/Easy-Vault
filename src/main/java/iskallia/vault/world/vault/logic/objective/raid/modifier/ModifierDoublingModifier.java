package iskallia.vault.world.vault.logic.objective.raid.modifier;

import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.logic.objective.raid.ActiveRaid;
import net.minecraft.entity.MobEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.server.ServerWorld;

public class ModifierDoublingModifier extends RaidModifier {
    public ModifierDoublingModifier(final String name) {
        super(false, true, name);
    }

    @Override
    public void affectRaidMob(final MobEntity mob, final float value) {
    }

    @Override
    public void onVaultRaidFinish(final VaultRaid vault, final ServerWorld world, final BlockPos controller, final ActiveRaid raid, final float value) {
    }

    @Override
    public ITextComponent getDisplay(final float value) {
        return (ITextComponent) new StringTextComponent("Doubles values of all existing modifiers").withStyle(TextFormatting.GREEN);
    }
}
