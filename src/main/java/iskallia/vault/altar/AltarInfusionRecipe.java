package iskallia.vault.altar;

import java.util.Iterator;

import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.CompoundNBT;
import iskallia.vault.init.ModConfigs;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import java.util.ArrayList;
import javax.annotation.Nonnull;
import java.util.List;
import java.util.UUID;

public class AltarInfusionRecipe {
    private final UUID player;
    @Nonnull
    private List<RequiredItem> requiredItems;
    @Nonnull
    private List<RequiredItem> cachedItems;
    private boolean pogInfused;

    public AltarInfusionRecipe(final UUID uuid, @Nonnull final List<RequiredItem> items, final List<RequiredItem> cachedItems, final boolean pogInfused) {
        this.player = uuid;
        this.requiredItems = items;
        this.cachedItems = ((cachedItems == null) ? new ArrayList<RequiredItem>() : cachedItems);
        this.pogInfused = pogInfused;
    }

    public AltarInfusionRecipe(final UUID uuid, final List<RequiredItem> items, final boolean pogInfused) {
        this(uuid, items, null, pogInfused);
    }

    public AltarInfusionRecipe(final ServerWorld world, final BlockPos pos, final ServerPlayerEntity player) {
        this(player.getUUID(), ModConfigs.VAULT_ALTAR.getRequiredItemsFromConfig(world, pos, player), false);
    }

    public boolean isPogInfused() {
        return this.pogInfused;
    }

    public void setPogInfused(final boolean pogInfused) {
        this.pogInfused = pogInfused;
    }

    public void cacheRequiredItems(final List<RequiredItem> newRequirements) {
        this.cachedItems = this.requiredItems;
        this.requiredItems = newRequirements;
    }

    public void revertCache() {
        this.requiredItems = this.cachedItems;
        this.cachedItems = new ArrayList<RequiredItem>();
    }

    public static AltarInfusionRecipe deserialize(final CompoundNBT nbt) {
        final UUID player = nbt.getUUID("player");
        final ListNBT requiredItemsNBT = nbt.getList("requiredItems", 10);
        final ListNBT cachedItemsNBT = nbt.getList("cachedItems", 10);
        final List<RequiredItem> requiredItems = new ArrayList<RequiredItem>();
        final List<RequiredItem> cachedItems = new ArrayList<RequiredItem>();
        for (final INBT tag : requiredItemsNBT) {
            final CompoundNBT compound = (CompoundNBT) tag;
            requiredItems.add(RequiredItem.deserializeNBT(compound));
        }
        for (final INBT tag : cachedItemsNBT) {
            final CompoundNBT compound = (CompoundNBT) tag;
            cachedItems.add(RequiredItem.deserializeNBT(compound));
        }
        final boolean pogInfused = nbt.getBoolean("pogInfused");
        return new AltarInfusionRecipe(player, requiredItems, cachedItems, pogInfused);
    }

    public CompoundNBT serialize() {
        final CompoundNBT nbt = new CompoundNBT();
        final ListNBT requiredItems = new ListNBT();
        final ListNBT cachedItems = new ListNBT();
        for (final RequiredItem item : this.getRequiredItems()) {
            requiredItems.add(RequiredItem.serializeNBT(item));
        }
        for (final RequiredItem item : this.getCachedItems()) {
            cachedItems.add(RequiredItem.serializeNBT(item));
        }
        nbt.putUUID("player", this.getPlayer());
        nbt.put("requiredItems", requiredItems);
        nbt.put("cachedItems", cachedItems);
        nbt.putBoolean("pogInfused", this.pogInfused);
        return nbt;
    }

    public UUID getPlayer() {
        return this.player;
    }

    @Nonnull
    public List<RequiredItem> getRequiredItems() {
        return this.requiredItems;
    }

    @Nonnull
    public List<RequiredItem> getCachedItems() {
        return this.cachedItems;
    }

    public boolean isComplete() {
        if (this.requiredItems.isEmpty()) {
            return false;
        }
        for (final RequiredItem item : this.requiredItems) {
            if (!item.reachedAmountRequired()) {
                return false;
            }
        }
        return true;
    }

    public boolean hasEqualQuantities(final AltarInfusionRecipe other) {
        int equals = 0;
        for (int i = 0; i < this.getRequiredItems().size(); ++i) {
            final RequiredItem item = this.getRequiredItems().get(i);
            if (item.getCurrentAmount() == other.getRequiredItems().get(i).getCurrentAmount()) {
                ++equals;
            }
        }
        return equals == 4;
    }
}
