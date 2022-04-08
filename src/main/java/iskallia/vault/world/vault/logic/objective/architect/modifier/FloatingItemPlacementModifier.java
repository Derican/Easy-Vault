package iskallia.vault.world.vault.logic.objective.architect.modifier;

import com.google.gson.annotations.Expose;
import iskallia.vault.config.entry.SingleItemEntry;
import iskallia.vault.init.ModItems;
import iskallia.vault.util.data.WeightedList;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.logic.objective.architect.ArchitectObjective;
import iskallia.vault.world.vault.logic.objective.architect.processor.FloatingItemPostProcessor;
import iskallia.vault.world.vault.logic.objective.architect.processor.VaultPieceProcessor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IItemProvider;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class FloatingItemPlacementModifier
        extends VoteModifier {
    @Expose
    private final int blocksPerSpawn;

    public FloatingItemPlacementModifier(String name, String description, int voteLockDurationChangeSeconds, int blocksPerSpawn, List<ItemStack> itemList) {
        super(name, description, voteLockDurationChangeSeconds);
        this.blocksPerSpawn = blocksPerSpawn;
        this.itemList = new WeightedList();
        itemList.forEach(stack -> this.itemList.add(new SingleItemEntry(stack), 1));
    }

    @Expose
    private final WeightedList<SingleItemEntry> itemList;

    public WeightedList<ItemStack> getItemList() {
        WeightedList<ItemStack> itemWeights = new WeightedList();
        this.itemList.forEach((itemKey, weight) -> itemWeights.add(itemKey.createItemStack(), weight.intValue()));


        return itemWeights;
    }


    @Nullable
    public VaultPieceProcessor getPostProcessor(ArchitectObjective objective, VaultRaid vault) {
        return (VaultPieceProcessor) new FloatingItemPostProcessor(this.blocksPerSpawn, getItemList());
    }

    public static List<ItemStack> defaultGemList() {
        List<ItemStack> list = new ArrayList<>();
        list.add(new ItemStack((IItemProvider) ModItems.ALEXANDRITE_GEM));
        list.add(new ItemStack((IItemProvider) ModItems.BENITOITE_GEM));
        list.add(new ItemStack((IItemProvider) ModItems.LARIMAR_GEM));
        list.add(new ItemStack((IItemProvider) ModItems.WUTODIE_GEM));
        list.add(new ItemStack((IItemProvider) ModItems.PAINITE_GEM));
        list.add(new ItemStack((IItemProvider) ModItems.BLACK_OPAL_GEM));
        return list;
    }

    public static List<ItemStack> defaultPrismaticList() {
        List<ItemStack> list = new ArrayList<>();
        list.add(new ItemStack((IItemProvider) ModItems.VAULT_CATALYST_FRAGMENT));
        return list;
    }

    public static List<ItemStack> defaultVaultGearList() {
        List<ItemStack> list = new ArrayList<>();
        list.add(new ItemStack((IItemProvider) ModItems.SWORD));
        list.add(new ItemStack((IItemProvider) ModItems.AXE));
        list.add(new ItemStack((IItemProvider) ModItems.HELMET));
        list.add(new ItemStack((IItemProvider) ModItems.CHESTPLATE));
        list.add(new ItemStack((IItemProvider) ModItems.LEGGINGS));
        list.add(new ItemStack((IItemProvider) ModItems.BOOTS));
        return list;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\logic\objective\architect\modifier\FloatingItemPlacementModifier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */