// 
// Decompiled by Procyon v0.6.0
// 

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

public class FloatingItemPlacementModifier extends VoteModifier
{
    @Expose
    private final int blocksPerSpawn;
    @Expose
    private final WeightedList<SingleItemEntry> itemList;
    
    public FloatingItemPlacementModifier(final String name, final String description, final int voteLockDurationChangeSeconds, final int blocksPerSpawn, final List<ItemStack> itemList) {
        super(name, description, voteLockDurationChangeSeconds);
        this.blocksPerSpawn = blocksPerSpawn;
        this.itemList = new WeightedList<SingleItemEntry>();
        itemList.forEach(stack -> this.itemList.add(new SingleItemEntry(stack), 1));
    }
    
    public WeightedList<ItemStack> getItemList() {
        final WeightedList<ItemStack> itemWeights = new WeightedList<ItemStack>();
        this.itemList.forEach((itemKey, weight) -> itemWeights.add(itemKey.createItemStack(), weight.intValue()));
        return itemWeights;
    }
    
    @Nullable
    @Override
    public VaultPieceProcessor getPostProcessor(final ArchitectObjective objective, final VaultRaid vault) {
        return new FloatingItemPostProcessor(this.blocksPerSpawn, this.getItemList());
    }
    
    public static List<ItemStack> defaultGemList() {
        final List<ItemStack> list = new ArrayList<ItemStack>();
        list.add(new ItemStack((IItemProvider)ModItems.ALEXANDRITE_GEM));
        list.add(new ItemStack((IItemProvider)ModItems.BENITOITE_GEM));
        list.add(new ItemStack((IItemProvider)ModItems.LARIMAR_GEM));
        list.add(new ItemStack((IItemProvider)ModItems.WUTODIE_GEM));
        list.add(new ItemStack((IItemProvider)ModItems.PAINITE_GEM));
        list.add(new ItemStack((IItemProvider)ModItems.BLACK_OPAL_GEM));
        return list;
    }
    
    public static List<ItemStack> defaultPrismaticList() {
        final List<ItemStack> list = new ArrayList<ItemStack>();
        list.add(new ItemStack((IItemProvider)ModItems.VAULT_CATALYST_FRAGMENT));
        return list;
    }
    
    public static List<ItemStack> defaultVaultGearList() {
        final List<ItemStack> list = new ArrayList<ItemStack>();
        list.add(new ItemStack((IItemProvider)ModItems.SWORD));
        list.add(new ItemStack((IItemProvider)ModItems.AXE));
        list.add(new ItemStack((IItemProvider)ModItems.HELMET));
        list.add(new ItemStack((IItemProvider)ModItems.CHESTPLATE));
        list.add(new ItemStack((IItemProvider)ModItems.LEGGINGS));
        list.add(new ItemStack((IItemProvider)ModItems.BOOTS));
        return list;
    }
}
