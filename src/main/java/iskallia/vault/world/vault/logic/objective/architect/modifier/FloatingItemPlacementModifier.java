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

public class FloatingItemPlacementModifier extends VoteModifier {
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

    public FloatingItemPlacementModifier(final String name, final String description, final int voteLockDurationChangeSeconds, final int blocksPerSpawn, final WeightedList<ItemStack> itemList) {
        super(name, description, voteLockDurationChangeSeconds);
        this.blocksPerSpawn = blocksPerSpawn;
        this.itemList = new WeightedList<SingleItemEntry>();
        itemList.forEach((stack,weight) -> this.itemList.add(new SingleItemEntry(stack), weight.intValue()));
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

    public static WeightedList<ItemStack> defaultGemList() {
        final WeightedList<ItemStack> list = new WeightedList<>();
        list.add(new ItemStack(ModItems.ALEXANDRITE_GEM),64);
        list.add(new ItemStack(ModItems.BENITOITE_GEM),128);
        list.add(new ItemStack(ModItems.LARIMAR_GEM),128);
        list.add(new ItemStack(ModItems.WUTODIE_GEM),64);
        list.add(new ItemStack(ModItems.PAINITE_GEM),16);
        list.add(new ItemStack(ModItems.BLACK_OPAL_GEM),8);
        list.add(new ItemStack(ModItems.ECHO_GEM),1);
        return list;
    }

    public static WeightedList<ItemStack> perfectGemList() {
        final WeightedList<ItemStack> list = new WeightedList<>();
        list.add(new ItemStack(ModItems.PERFECT_ALEXANDRITE),64);
        list.add(new ItemStack(ModItems.PERFECT_PAINITE),128);
        list.add(new ItemStack(ModItems.PERFECT_LARIMAR),128);
        list.add(new ItemStack(ModItems.PERFECT_BENITOITE),64);
        list.add(new ItemStack(ModItems.PERFECT_BLACK_OPAL),16);
        list.add(new ItemStack(ModItems.PERFECT_WUTODIE),8);
        list.add(new ItemStack(ModItems.PERFECT_ECHO_GEM),1);
        return list;
    }

    public static WeightedList<ItemStack> defaultPogList() {
        final WeightedList<ItemStack> list = new WeightedList<>();
        list.add(new ItemStack(ModItems.POG),8);
        list.add(new ItemStack(ModItems.ECHO_POG),1);
        return list;
    }

    public static WeightedList<ItemStack> defaultWutaxList() {
        final WeightedList<ItemStack> list = new WeightedList<>();
        list.add(new ItemStack(ModItems.WUTAX_SHARD),16);
        list.add(new ItemStack(ModItems.WUTAX_CRYSTAL),1);
        return list;
    }

    public static WeightedList<ItemStack> defaultEssenceList() {
        final WeightedList<ItemStack> list = new WeightedList<>();
        list.add(new ItemStack(ModItems.VAULT_ESSENCE),16);
        return list;
    }

    public static WeightedList<ItemStack> defaultXpList() {
        final WeightedList<ItemStack> list = new WeightedList<>();
        list.add(new ItemStack(ModItems.VAULT_BURGER),8);
        list.add(new ItemStack(ModItems.OOZING_PIZZA),32);
        list.add(new ItemStack(ModItems.VAULT_STEW_MYSTERY),1);
        return list;
    }

    public static WeightedList<ItemStack> defaultBronzeList() {
        final WeightedList<ItemStack> list = new WeightedList<>();
        list.add(new ItemStack(ModItems.VAULT_BRONZE),10);
        list.add(new ItemStack(ModItems.VAULT_SILVER),4);
        list.add(new ItemStack(ModItems.VAULT_GOLD),1);
        return list;
    }

    public static WeightedList<ItemStack> defaultKeysList() {
        final WeightedList<ItemStack> list = new WeightedList<>();
        list.add(new ItemStack(ModItems.ISKALLIUM_KEY),1);
        list.add(new ItemStack(ModItems.GORGINITE_KEY),1);
        list.add(new ItemStack(ModItems.SPARKLETINE_KEY),1);
        list.add(new ItemStack(ModItems.ASHIUM_KEY),1);
        list.add(new ItemStack(ModItems.BOMIGNITE_KEY),1);
        list.add(new ItemStack(ModItems.FUNSOIDE_KEY),1);
        list.add(new ItemStack(ModItems.TUBIUM_KEY),1);
        list.add(new ItemStack(ModItems.UPALINE_KEY),1);
        list.add(new ItemStack(ModItems.PUFFIUM_KEY),1);
        return list;
    }

    public static WeightedList<ItemStack> defaultGambaList() {
        final WeightedList<ItemStack> list = new WeightedList<>();
        list.add(new ItemStack(ModItems.UNIDENTIFIED_TREASURE_KEY),1);
        list.add(new ItemStack(ModItems.GEAR_CHARM),4);
        list.add(new ItemStack(ModItems.VAULT_ESSENCE),16);
        list.add(new ItemStack(ModItems.STAR_SHARD),8);
        list.add(new ItemStack(ModItems.SKILL_SHARD),4);
        list.add(new ItemStack(ModItems.SKILL_ESSENCE),8);
        list.add(new ItemStack(ModItems.SKILL_ORB),1);
        list.add(new ItemStack(ModItems.SKILL_ORB_FRAME),2);
        list.add(new ItemStack(ModItems.STAR_CORE),4);
        list.add(new ItemStack(ModItems.VAULT_STEW_MYSTERY),6);
        list.add(new ItemStack(ModItems.ACCELERATION_CHIP),6);
        list.add(new ItemStack(ModItems.PANDORAS_BOX),16);
        list.add(new ItemStack(ModItems.VAULT_DIAMOND),16);
        list.add(new ItemStack(ModItems.BANISHED_SOUL),4);
        list.add(new ItemStack(ModItems.VAULT_PLATINUM),8);
        return list;
    }

    public static WeightedList<ItemStack> defaultRepairCoreList() {
        final WeightedList<ItemStack> list = new WeightedList<>();
        list.add(new ItemStack(ModItems.REPAIR_CORE),1);
        return list;
    }

    public static WeightedList<ItemStack> defaultVoidOrbList() {
        final WeightedList<ItemStack> list = new WeightedList<>();
        list.add(new ItemStack(ModItems.VOID_ORB),1);
        return list;
    }

    public static WeightedList<ItemStack> defaultKnowledgeList() {
        final WeightedList<ItemStack> list = new WeightedList<>();
        list.add(new ItemStack(ModItems.STAR_ESSENCE),32);
        list.add(new ItemStack(ModItems.STAR_SHARD),4);
        list.add(new ItemStack(ModItems.STAR_CORE),1);
        return list;
    }
    public static WeightedList<ItemStack> defaultSkillList() {
        final WeightedList<ItemStack> list = new WeightedList<>();
        list.add(new ItemStack(ModItems.SKILL_ESSENCE),48);
        list.add(new ItemStack(ModItems.SKILL_SHARD),4);
        list.add(new ItemStack(ModItems.SKILL_ORB_FRAME),1);
        return list;
    }
    public static WeightedList<ItemStack> defaultPrismaticList() {
        final WeightedList<ItemStack> list = new WeightedList<>();
        list.add(new ItemStack(ModItems.VAULT_CATALYST),1);
        list.add(new ItemStack(ModItems.VAULT_CATALYST_FRAGMENT),4);
        return list;
    }

    public static WeightedList<ItemStack> defaultVaultGearList() {
        final WeightedList<ItemStack> list = new WeightedList<>();
        list.add(new ItemStack(ModItems.SWORD),1);
        list.add(new ItemStack(ModItems.AXE),1);
        list.add(new ItemStack(ModItems.HELMET),1);
        list.add(new ItemStack(ModItems.CHESTPLATE),1);
        list.add(new ItemStack(ModItems.LEGGINGS),1);
        list.add(new ItemStack(ModItems.BOOTS),1);
        list.add(new ItemStack(ModItems.IDOL_BENEVOLENT),1);
        list.add(new ItemStack(ModItems.IDOL_MALEVOLENCE),1);
        list.add(new ItemStack(ModItems.IDOL_OMNISCIENT),1);
        list.add(new ItemStack(ModItems.IDOL_TIMEKEEPER),1);
        return list;
    }
}
