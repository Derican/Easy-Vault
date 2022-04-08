package iskallia.vault.config;

import com.google.gson.annotations.Expose;
import iskallia.vault.config.entry.SingleItemEntry;
import iskallia.vault.config.entry.StatueDecay;
import iskallia.vault.util.StatueType;
import iskallia.vault.util.data.WeightedList;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class StatueLootConfig extends Config {
    @Expose
    private int MAX_ACCELERATION_CHIPS;
    @Expose
    private HashMap<Integer, Integer> INTERVAL_DECREASE_PER_CHIP = new HashMap<>();

    @Expose
    private WeightedList<SingleItemEntry> GIFT_NORMAL_STATUE_LOOT;

    @Expose
    private int GIFT_NORMAL_STATUE_INTERVAL;

    @Expose
    private StatueDecay GIFT_NORMAL_DECAY;

    @Expose
    private WeightedList<SingleItemEntry> GIFT_MEGA_STATUE_LOOT;

    @Expose
    private int GIFT_MEGA_STATUE_INTERVAL;


    public String getName() {
        return "statue_loot";
    }

    @Expose
    private StatueDecay GIFT_MEGA_DECAY;
    @Expose
    private WeightedList<SingleItemEntry> VAULT_BOSS_STATUE_LOOT;
    @Expose
    private int VAULT_BOSS_STATUE_INTERVAL;
    @Expose
    private StatueDecay VAULT_BOSS_DECAY;
    @Expose
    private WeightedList<SingleItemEntry> OMEGA_STATUE_LOOT;
    @Expose
    private int OMEGA_STATUE_INTERVAL;

    public int getDecay(StatueType type) {
        switch (type) {
            case GIFT_NORMAL:
                return this.GIFT_NORMAL_DECAY.getDecay();
            case GIFT_MEGA:
                return this.GIFT_MEGA_DECAY.getDecay();
            case VAULT_BOSS:
                return this.VAULT_BOSS_DECAY.getDecay();
        }
        return -1;
    }


    protected void reset() {
        this.MAX_ACCELERATION_CHIPS = 4;
        this.INTERVAL_DECREASE_PER_CHIP.put(Integer.valueOf(1), Integer.valueOf(50));
        this.INTERVAL_DECREASE_PER_CHIP.put(Integer.valueOf(2), Integer.valueOf(100));
        this.INTERVAL_DECREASE_PER_CHIP.put(Integer.valueOf(3), Integer.valueOf(200));
        this.INTERVAL_DECREASE_PER_CHIP.put(Integer.valueOf(4), Integer.valueOf(500));

        this.GIFT_NORMAL_STATUE_LOOT = new WeightedList();
        ItemStack fancyApple = new ItemStack((IItemProvider) Items.APPLE);
        fancyApple.setHoverName((ITextComponent) new StringTextComponent("Fancy Apple"));
        this.GIFT_NORMAL_STATUE_LOOT.add(new WeightedList.Entry(new SingleItemEntry(fancyApple), 1));
        ItemStack sword = new ItemStack((IItemProvider) Items.WOODEN_SWORD);
        sword.enchant(Enchantments.SHARPNESS, 10);
        this.GIFT_NORMAL_STATUE_LOOT.add(new WeightedList.Entry(new SingleItemEntry(sword), 1));
        this.GIFT_NORMAL_STATUE_INTERVAL = 500;
        this.GIFT_NORMAL_DECAY = new StatueDecay(100, 1000);

        this.GIFT_MEGA_STATUE_LOOT = new WeightedList();
        ItemStack fancierApple = new ItemStack((IItemProvider) Items.GOLDEN_APPLE);
        fancierApple.setHoverName((ITextComponent) new StringTextComponent("Fancier Apple"));
        this.GIFT_MEGA_STATUE_LOOT.add(new WeightedList.Entry(new SingleItemEntry(fancierApple), 1));
        sword = new ItemStack((IItemProvider) Items.DIAMOND_SWORD);
        sword.enchant(Enchantments.SHARPNESS, 10);
        this.GIFT_MEGA_STATUE_LOOT.add(new WeightedList.Entry(new SingleItemEntry(sword), 1));
        this.GIFT_MEGA_STATUE_INTERVAL = 1000;
        this.GIFT_MEGA_DECAY = new StatueDecay(100, 1000);

        this.VAULT_BOSS_STATUE_LOOT = new WeightedList();
        ItemStack fanciestApple = new ItemStack((IItemProvider) Items.ENCHANTED_GOLDEN_APPLE);
        fanciestApple.setHoverName((ITextComponent) new StringTextComponent("Fanciest Apple"));
        this.VAULT_BOSS_STATUE_LOOT.add(new WeightedList.Entry(new SingleItemEntry(fanciestApple), 1));
        sword = new ItemStack((IItemProvider) Items.NETHERITE_SWORD);
        sword.enchant(Enchantments.SHARPNESS, 10);
        this.VAULT_BOSS_STATUE_LOOT.add(new WeightedList.Entry(new SingleItemEntry(sword), 1));
        this.VAULT_BOSS_STATUE_INTERVAL = 500;
        this.VAULT_BOSS_DECAY = new StatueDecay(100, 1000);

        this.OMEGA_STATUE_LOOT = new WeightedList();
        this.OMEGA_STATUE_LOOT.add(new WeightedList.Entry(new SingleItemEntry((IItemProvider) Blocks.STONE), 1));
        this.OMEGA_STATUE_LOOT.add(new WeightedList.Entry(new SingleItemEntry((IItemProvider) Blocks.COBBLESTONE), 1));
        this.OMEGA_STATUE_LOOT.add(new WeightedList.Entry(new SingleItemEntry((IItemProvider) Blocks.DIORITE), 1));
        this.OMEGA_STATUE_LOOT.add(new WeightedList.Entry(new SingleItemEntry((IItemProvider) Blocks.ANDESITE), 1));
        this.OMEGA_STATUE_LOOT.add(new WeightedList.Entry(new SingleItemEntry((IItemProvider) Blocks.OAK_LOG), 1));
        this.OMEGA_STATUE_INTERVAL = 1000;
    }

    public ItemStack randomLoot(StatueType type) {
        switch (type) {
            case GIFT_NORMAL:
                return getItem((SingleItemEntry) this.GIFT_NORMAL_STATUE_LOOT.getRandom(new Random()));
            case GIFT_MEGA:
                return getItem((SingleItemEntry) this.GIFT_MEGA_STATUE_LOOT.getRandom(new Random()));
            case VAULT_BOSS:
                return getItem((SingleItemEntry) this.VAULT_BOSS_STATUE_LOOT.getRandom(new Random()));
            case OMEGA:
            case OMEGA_VARIANT:
                return getItem((SingleItemEntry) this.OMEGA_STATUE_LOOT.getRandom(new Random()));
        }

        throw new InternalError("Unknown Statue variant: " + type);
    }

    public List<ItemStack> getOmegaOptions() {
        List<ItemStack> options = new ArrayList<>();
        WeightedList<SingleItemEntry> entries = this.OMEGA_STATUE_LOOT;

        for (int i = 0; i < 5; i++) {
            SingleItemEntry entry = (SingleItemEntry) entries.getRandom(new Random());
            entries.remove(entry);
            options.add(getItem(entry));
        }

        return options;
    }

    public int getInterval(StatueType type) {
        switch (type) {
            case GIFT_NORMAL:
                return this.GIFT_NORMAL_STATUE_INTERVAL;
            case GIFT_MEGA:
                return this.GIFT_MEGA_STATUE_INTERVAL;
            case VAULT_BOSS:
                return this.VAULT_BOSS_STATUE_INTERVAL;
            case OMEGA:
            case OMEGA_VARIANT:
                return this.OMEGA_STATUE_INTERVAL;
        }

        throw new IllegalArgumentException("Unknown Statue variant: " + type);
    }

    public int getMaxAccelerationChips() {
        return this.MAX_ACCELERATION_CHIPS;
    }

    private ItemStack getItem(SingleItemEntry entry) {
        ItemStack stack = ItemStack.EMPTY;
        try {
            Item item = (Item) ForgeRegistries.ITEMS.getValue(new ResourceLocation(entry.ITEM));
            stack = new ItemStack((IItemProvider) item);
            if (entry.NBT != null) {
                CompoundNBT nbt = JsonToNBT.parseTag(entry.NBT);
                stack.setTag(nbt);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return stack;
    }

    public int getIntervalDecrease(int chipCount) {
        return ((Integer) this.INTERVAL_DECREASE_PER_CHIP.get(Integer.valueOf(chipCount))).intValue();
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\config\StatueLootConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */