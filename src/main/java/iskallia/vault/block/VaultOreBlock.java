package iskallia.vault.block;

import iskallia.vault.init.ModSounds;
import iskallia.vault.item.ItemVaultGem;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.OreBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class VaultOreBlock extends OreBlock {
    public static final List<VaultOreBlock> ORES = new ArrayList<>();

    public ItemVaultGem associatedGem;

    public VaultOreBlock(@Nonnull ItemVaultGem associatedGem) {
        super(AbstractBlock.Properties.of(Material.STONE, MaterialColor.DIAMOND)
                .requiresCorrectToolForDrops()
                .lightLevel(state -> 9)
                .strength(3.0F, 3.0F)
                .sound((SoundType) ModSounds.VAULT_GEM));


        this.associatedGem = associatedGem;

        ORES.add(this);
    }

    @Nonnull
    public ItemVaultGem getAssociatedGem() {
        return this.associatedGem;
    }


    protected int xpOnDrop(@Nonnull Random random) {
        return MathHelper.nextInt(random, 3, 7);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\block\VaultOreBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */