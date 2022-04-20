package iskallia.vault.world.vault.logic.objective.raid.modifier;

import com.google.gson.annotations.Expose;
import iskallia.vault.config.entry.SingleItemEntry;
import iskallia.vault.entity.FloatingItemEntity;
import iskallia.vault.init.ModItems;
import iskallia.vault.util.MiscUtils;
import iskallia.vault.util.data.WeightedList;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.logic.objective.raid.ActiveRaid;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class FloatingItemModifier extends RaidModifier {
    @Expose
    private final int itemsToSpawn;
    @Expose
    private final WeightedList<SingleItemEntry> itemList;
    @Expose
    private final String itemDescription;

    public FloatingItemModifier(final String name, final int itemsToSpawn, final WeightedList<SingleItemEntry> itemList, final String itemDescription) {
        super(false, true, name);
        this.itemsToSpawn = itemsToSpawn;
        this.itemList = itemList;
        this.itemDescription = itemDescription;
    }

    @Override
    public void affectRaidMob(final MobEntity mob, final float value) {
    }

    @Override
    public void onVaultRaidFinish(final VaultRaid vault, final ServerWorld world, final BlockPos controller, final ActiveRaid raid, final float value) {
        final WeightedList<ItemStack> items = this.getItemList();
        final int toPlace = this.itemsToSpawn * Math.round(value);
        final AxisAlignedBB placementBox = raid.getRaidBoundingBox();
        for (int i = 0; i < toPlace; ++i) {
            BlockPos at;
            do {
                at = MiscUtils.getRandomPos(placementBox, FloatingItemModifier.rand);
            } while (!world.isEmptyBlock(at));
            final ItemStack stack = items.getRandom(FloatingItemModifier.rand);
            if (stack != null && !stack.isEmpty()) {
                world.addFreshEntity((Entity) FloatingItemEntity.create((World) world, at, stack.copy()));
            }
        }
    }

    public WeightedList<ItemStack> getItemList() {
        final WeightedList<ItemStack> itemWeights = new WeightedList<ItemStack>();
        this.itemList.forEach((itemKey, weight) -> itemWeights.add(itemKey.createItemStack(), weight.intValue()));
        return itemWeights;
    }

    @Override
    public ITextComponent getDisplay(final float value) {
        final int sets = Math.round(value);
        final String set = (sets > 1) ? "sets" : "set";
        return (ITextComponent) new StringTextComponent("+" + sets + " " + set + " of " + this.itemDescription).withStyle(TextFormatting.GREEN);
    }

    public static WeightedList<SingleItemEntry> defaultGemList() {
        final WeightedList<SingleItemEntry> list = new WeightedList<SingleItemEntry>();
        list.add(new SingleItemEntry(new ItemStack((IItemProvider) ModItems.ALEXANDRITE_GEM)), 1);
        list.add(new SingleItemEntry(new ItemStack((IItemProvider) ModItems.BENITOITE_GEM)), 1);
        list.add(new SingleItemEntry(new ItemStack((IItemProvider) ModItems.LARIMAR_GEM)), 1);
        list.add(new SingleItemEntry(new ItemStack((IItemProvider) ModItems.WUTODIE_GEM)), 1);
        list.add(new SingleItemEntry(new ItemStack((IItemProvider) ModItems.PAINITE_GEM)), 1);
        list.add(new SingleItemEntry(new ItemStack((IItemProvider) ModItems.BLACK_OPAL_GEM)), 1);
        return list;
    }
}
