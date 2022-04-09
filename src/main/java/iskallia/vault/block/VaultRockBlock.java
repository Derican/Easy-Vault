// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.block;

import iskallia.vault.init.ModSounds;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.OreBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.util.math.MathHelper;

import java.util.Random;

public class VaultRockBlock extends OreBlock
{
    public VaultRockBlock() {
        super(AbstractBlock.Properties.of(Material.STONE, MaterialColor.DIAMOND).requiresCorrectToolForDrops().lightLevel(state -> 9).strength(3.0f, 3.0f).sound((SoundType)ModSounds.VAULT_GEM));
    }
    
    protected int xpOnDrop(final Random rand) {
        return MathHelper.nextInt(rand, 3, 7);
    }
}
