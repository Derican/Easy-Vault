// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.altar;

import iskallia.vault.init.ModConfigs;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AltarInfusionRecipe
{
    private final UUID player;
    private List<RequiredItem> requiredItems;
    
    public AltarInfusionRecipe(final UUID uuid, final List<RequiredItem> items) {
        this.requiredItems = new ArrayList<RequiredItem>();
        this.player = uuid;
        this.requiredItems = items;
    }
    
    public AltarInfusionRecipe(final ServerWorld world, final BlockPos pos, final ServerPlayerEntity player) {
        this(player.getUUID(), ModConfigs.VAULT_ALTAR.getRequiredItemsFromConfig(world, pos, player));
    }
    
    public AltarInfusionRecipe(final UUID player) {
        this.requiredItems = new ArrayList<RequiredItem>();
        this.player = player;
    }
    
    public static AltarInfusionRecipe deserialize(final CompoundNBT nbt) {
        final UUID player = nbt.getUUID("player");
        final ListNBT list = nbt.getList("requiredItems", 10);
        final List<RequiredItem> requiredItems = new ArrayList<RequiredItem>();
        for (final INBT tag : list) {
            final CompoundNBT compound = (CompoundNBT)tag;
            requiredItems.add(RequiredItem.deserializeNBT(compound));
        }
        return new AltarInfusionRecipe(player, requiredItems);
    }
    
    public static CompoundNBT serialize(final AltarInfusionRecipe recipe) {
        final CompoundNBT nbt = new CompoundNBT();
        final ListNBT list = new ListNBT();
        for (final RequiredItem item : recipe.getRequiredItems()) {
            list.add(RequiredItem.serializeNBT(item));
        }
        nbt.putUUID("player", recipe.getPlayer());
        nbt.put("requiredItems", (INBT)list);
        return nbt;
    }
    
    public CompoundNBT serialize() {
        final CompoundNBT nbt = new CompoundNBT();
        final ListNBT list = new ListNBT();
        for (final RequiredItem item : this.getRequiredItems()) {
            list.add(RequiredItem.serializeNBT(item));
        }
        nbt.putUUID("player", this.getPlayer());
        nbt.put("requiredItems", (INBT)list);
        return nbt;
    }
    
    public UUID getPlayer() {
        return this.player;
    }
    
    public List<RequiredItem> getRequiredItems() {
        return this.requiredItems;
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
