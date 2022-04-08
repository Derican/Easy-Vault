package iskallia.vault.world.vault.logic.objective;

import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.gen.VaultGenerator;
import iskallia.vault.world.vault.logic.task.VaultTask;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.loot.LootTable;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Function;
import java.util.function.Supplier;


public class TroveObjective
        extends VaultObjective {
    public TroveObjective(ResourceLocation id) {
        super(id, VaultTask.EMPTY, VaultTask.EMPTY);
    }


    @Nonnull
    public BlockState getObjectiveRelevantBlock() {
        return Blocks.AIR.defaultBlockState();
    }


    @Nullable
    public LootTable getRewardLootTable(VaultRaid vault, Function<ResourceLocation, LootTable> tblResolver) {
        return null;
    }


    public ITextComponent getObjectiveDisplayName() {
        return getVaultName();
    }


    public ITextComponent getVaultName() {
        return (ITextComponent) (new StringTextComponent("Vault Trove")).withStyle(TextFormatting.GOLD);
    }


    public int getVaultTimerStart(int vaultTime) {
        return 12000;
    }


    public boolean preventsEatingExtensionFruit(MinecraftServer srv, VaultRaid vault) {
        return true;
    }


    public boolean preventsMobSpawning() {
        return true;
    }


    public boolean preventsTrappedChests() {
        return true;
    }


    public boolean preventsInfluences() {
        return true;
    }


    @Nonnull
    public Supplier<? extends VaultGenerator> getVaultGenerator() {
        return VaultRaid.TROVE_GENERATOR;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\logic\objective\TroveObjective.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */