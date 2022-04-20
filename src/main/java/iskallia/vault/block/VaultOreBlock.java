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
    public static final List<VaultOreBlock> ORES;
    public ItemVaultGem associatedGem;

    public VaultOreBlock(@Nonnull final ItemVaultGem associatedGem) {
        super(AbstractBlock.Properties.of(Material.STONE, MaterialColor.DIAMOND).requiresCorrectToolForDrops().lightLevel(state -> 9).strength(3.0f, 3.0f).sound((SoundType) ModSounds.VAULT_GEM));
        this.associatedGem = associatedGem;
        VaultOreBlock.ORES.add(this);
    }

    @Nonnull
    public ItemVaultGem getAssociatedGem() {
        return this.associatedGem;
    }

    protected int xpOnDrop(@Nonnull final Random random) {
        return MathHelper.nextInt(random, 3, 7);
    }

    static {
        ORES = new ArrayList<VaultOreBlock>();
    }
}
