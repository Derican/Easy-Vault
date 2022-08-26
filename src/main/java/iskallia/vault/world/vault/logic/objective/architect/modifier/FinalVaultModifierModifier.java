// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.world.vault.logic.objective.architect.modifier;

import net.minecraft.entity.player.ServerPlayerEntity;
import iskallia.vault.world.vault.player.VaultPlayer;
import net.minecraft.util.text.ITextComponent;
import iskallia.vault.world.vault.modifier.VaultModifier;
import net.minecraft.util.Util;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.StringTextComponent;
import iskallia.vault.init.ModConfigs;
import net.minecraft.world.server.ServerWorld;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.logic.objective.architect.ArchitectObjective;
import com.google.gson.annotations.Expose;

public class FinalVaultModifierModifier extends VoteModifier
{
    @Expose
    private final String addedModifier;
    
    public FinalVaultModifierModifier(final String name, final String description, final String addedModifier) {
        super(name, description, 0);
        this.addedModifier = addedModifier;
    }
    public FinalVaultModifierModifier(final String name, final String description, final String color, final int voteLockDurationChangeSeconds, final String addedModifier) {
        super(name, description, color, voteLockDurationChangeSeconds);
        this.addedModifier = addedModifier;
    }
    
    @Override
    public void onApply(final ArchitectObjective objective, final VaultRaid vault, final ServerWorld world) {
        super.onApply(objective, vault, world);
        final VaultModifier modifier = ModConfigs.VAULT_MODIFIERS.getByName(this.addedModifier);
        if (modifier == null) {
            return;
        }
        final ITextComponent ct = new StringTextComponent("Added ").withStyle(TextFormatting.GRAY).append(modifier.getNameComponent());
        vault.getModifiers().addPermanentModifier(modifier);
        vault.getPlayers().forEach(vPlayer -> {
            modifier.apply(vault, vPlayer, world, world.getRandom());
            vPlayer.runIfPresent(world.getServer(), sPlayer -> sPlayer.sendMessage(ct, Util.NIL_UUID));
        });
    }
}
