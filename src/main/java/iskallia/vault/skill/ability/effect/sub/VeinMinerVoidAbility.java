package iskallia.vault.skill.ability.effect.sub;

import iskallia.vault.Vault;
import iskallia.vault.skill.ability.config.sub.VeinMinerVoidConfig;
import iskallia.vault.skill.ability.effect.VeinMinerAbility;
import net.minecraft.block.Block;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.server.ServerWorld;

public class VeinMinerVoidAbility
        extends VeinMinerAbility<VeinMinerVoidConfig> {
    public boolean shouldVoid(ServerWorld world, Block targetBlock) {
        return (world.dimension() == Vault.VAULT_KEY && !targetBlock.is(BlockTags.getAllTags().getTagOrEmpty(Vault.id("voidmine_exclusions"))));
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\ability\effect\sub\VeinMinerVoidAbility.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */