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


public class AltarInfusionRecipe {
    private final UUID player;
    private List<RequiredItem> requiredItems = new ArrayList<>();

    public AltarInfusionRecipe(UUID uuid, List<RequiredItem> items) {
        this.player = uuid;
        this.requiredItems = items;
    }

    public AltarInfusionRecipe(ServerWorld world, BlockPos pos, ServerPlayerEntity player) {
        this(player.getUUID(), ModConfigs.VAULT_ALTAR.getRequiredItemsFromConfig(world, pos, player));
    }

    public AltarInfusionRecipe(UUID player) {
        this.player = player;
    }

    public static AltarInfusionRecipe deserialize(CompoundNBT nbt) {
        UUID player = nbt.getUUID("player");
        ListNBT list = nbt.getList("requiredItems", 10);
        List<RequiredItem> requiredItems = new ArrayList<>();
        for (INBT tag : list) {
            CompoundNBT compound = (CompoundNBT) tag;
            requiredItems.add(RequiredItem.deserializeNBT(compound));
        }
        return new AltarInfusionRecipe(player, requiredItems);
    }

    public static CompoundNBT serialize(AltarInfusionRecipe recipe) {
        CompoundNBT nbt = new CompoundNBT();
        ListNBT list = new ListNBT();
        for (RequiredItem item : recipe.getRequiredItems()) {
            list.add(RequiredItem.serializeNBT(item));
        }
        nbt.putUUID("player", recipe.getPlayer());
        nbt.put("requiredItems", (INBT) list);
        return nbt;
    }


    public CompoundNBT serialize() {
        CompoundNBT nbt = new CompoundNBT();
        ListNBT list = new ListNBT();
        for (RequiredItem item : getRequiredItems()) {
            list.add(RequiredItem.serializeNBT(item));
        }
        nbt.putUUID("player", getPlayer());
        nbt.put("requiredItems", (INBT) list);
        return nbt;
    }

    public UUID getPlayer() {
        return this.player;
    }

    public List<RequiredItem> getRequiredItems() {
        return this.requiredItems;
    }

    public boolean isComplete() {
        if (this.requiredItems.isEmpty()) return false;
        for (RequiredItem item : this.requiredItems) {
            if (!item.reachedAmountRequired()) {
                return false;
            }
        }
        return true;
    }

    public boolean hasEqualQuantities(AltarInfusionRecipe other) {
        int equals = 0;
        for (int i = 0; i < getRequiredItems().size(); i++) {
            RequiredItem item = getRequiredItems().get(i);
            if (item.getCurrentAmount() == ((RequiredItem) other.getRequiredItems().get(i)).getCurrentAmount())
                equals++;
        }
        return (equals == 4);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\altar\AltarInfusionRecipe.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */