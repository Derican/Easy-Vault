package iskallia.vault.world.vault.logic.objective.raid.modifier;

import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.logic.objective.raid.ActiveRaid;
import net.minecraft.entity.MobEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.server.ServerWorld;

public class ModifierDoublingModifier
        extends RaidModifier {
    public ModifierDoublingModifier(String name) {
        super(false, true, name);
    }


    public void affectRaidMob(MobEntity mob, float value) {
    }


    public void onVaultRaidFinish(VaultRaid vault, ServerWorld world, BlockPos controller, ActiveRaid raid, float value) {
    }


    public ITextComponent getDisplay(float value) {
        return (ITextComponent) (new StringTextComponent("Doubles values of all existing modifiers"))
                .withStyle(TextFormatting.GREEN);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\logic\objective\raid\modifier\ModifierDoublingModifier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */