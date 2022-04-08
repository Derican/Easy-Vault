package iskallia.vault.block;

import iskallia.vault.init.ModSounds;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.OreBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.util.math.MathHelper;

import java.util.Random;

public class VaultRockBlock extends OreBlock {
    public VaultRockBlock() {
        super(AbstractBlock.Properties.of(Material.STONE, MaterialColor.DIAMOND)
                .requiresCorrectToolForDrops()
                .lightLevel(state -> 9)
                .strength(3.0F, 3.0F)
                .sound((SoundType) ModSounds.VAULT_GEM));
    }


    protected int xpOnDrop(Random rand) {
        return MathHelper.nextInt(rand, 3, 7);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\block\VaultRockBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */