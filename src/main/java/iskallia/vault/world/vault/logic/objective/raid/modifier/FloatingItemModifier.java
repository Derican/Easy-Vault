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


public class FloatingItemModifier
        extends RaidModifier {
    @Expose
    private final int itemsToSpawn;

    public FloatingItemModifier(String name, int itemsToSpawn, WeightedList<SingleItemEntry> itemList, String itemDescription) {
        super(false, true, name);
        this.itemsToSpawn = itemsToSpawn;
        this.itemList = itemList;
        this.itemDescription = itemDescription;
    }

    @Expose
    private final WeightedList<SingleItemEntry> itemList;
    @Expose
    private final String itemDescription;

    public void affectRaidMob(MobEntity mob, float value) {
    }

    public void onVaultRaidFinish(VaultRaid vault, ServerWorld world, BlockPos controller, ActiveRaid raid, float value) {
        WeightedList<ItemStack> items = getItemList();
        int toPlace = this.itemsToSpawn * Math.round(value);
        AxisAlignedBB placementBox = raid.getRaidBoundingBox();

        for (int i = 0; i < toPlace; i++) {
            BlockPos at;
            do {
                at = MiscUtils.getRandomPos(placementBox, rand);
            } while (!world.isEmptyBlock(at));

            ItemStack stack = (ItemStack) items.getRandom(rand);
            if (stack != null && !stack.isEmpty()) {
                world.addFreshEntity((Entity) FloatingItemEntity.create((World) world, at, stack.copy()));
            }
        }
    }

    public WeightedList<ItemStack> getItemList() {
        WeightedList<ItemStack> itemWeights = new WeightedList();
        this.itemList.forEach((itemKey, weight) -> itemWeights.add(itemKey.createItemStack(), weight.intValue()));


        return itemWeights;
    }


    public ITextComponent getDisplay(float value) {
        int sets = Math.round(value);
        String set = (sets > 1) ? "sets" : "set";
        return (ITextComponent) (new StringTextComponent("+" + sets + " " + set + " of " + this.itemDescription))
                .withStyle(TextFormatting.GREEN);
    }

    public static WeightedList<SingleItemEntry> defaultGemList() {
        WeightedList<SingleItemEntry> list = new WeightedList();
        list.add(new SingleItemEntry(new ItemStack((IItemProvider) ModItems.ALEXANDRITE_GEM)), 1);
        list.add(new SingleItemEntry(new ItemStack((IItemProvider) ModItems.BENITOITE_GEM)), 1);
        list.add(new SingleItemEntry(new ItemStack((IItemProvider) ModItems.LARIMAR_GEM)), 1);
        list.add(new SingleItemEntry(new ItemStack((IItemProvider) ModItems.WUTODIE_GEM)), 1);
        list.add(new SingleItemEntry(new ItemStack((IItemProvider) ModItems.PAINITE_GEM)), 1);
        list.add(new SingleItemEntry(new ItemStack((IItemProvider) ModItems.BLACK_OPAL_GEM)), 1);
        return list;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\logic\objective\raid\modifier\FloatingItemModifier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */