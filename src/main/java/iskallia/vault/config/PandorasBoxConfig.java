package iskallia.vault.config;

import com.google.gson.annotations.Expose;
import iskallia.vault.config.entry.vending.ProductEntry;
import iskallia.vault.init.ModItems;
import iskallia.vault.util.data.WeightedList;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;

public class PandorasBoxConfig extends Config {
    @Expose
    public WeightedList<ProductEntry> POOL;

    public PandorasBoxConfig() {
        this.POOL = new WeightedList<ProductEntry>();
    }

    @Override
    public String getName() {
        return "pandoras_box";
    }

    @Override
    protected void reset() {
        this.POOL.add(new ProductEntry(ModItems.HELMET, 1, 1, "{Vault:{Attributes:[{Id:\\\"the_vault:gear_roll_pool\\\",BaseValue:\\\"special\\\"}]}}"), 3);
        this.POOL.add(new ProductEntry(ModItems.CHESTPLATE, 1, 1, "{Vault:{Attributes:[{Id:\\\"the_vault:gear_roll_pool\\\",BaseValue:\\\"special\\\"}]}}"), 3);
        this.POOL.add(new ProductEntry(ModItems.LEGGINGS, 1, 1, "{Vault:{Attributes:[{Id:\\\"the_vault:gear_roll_pool\\\",BaseValue:\\\"special\\\"}]}}"), 3);
        this.POOL.add(new ProductEntry(ModItems.BOOTS, 1, 1, "{Vault:{Attributes:[{Id:\\\"the_vault:gear_roll_pool\\\",BaseValue:\\\"special\\\"}]}}"), 3);
        this.POOL.add(new ProductEntry(ModItems.SWORD, 1, 1, "{Vault:{Attributes:[{Id:\\\"the_vault:gear_roll_pool\\\",BaseValue:\\\"special\\\"}]}}"), 3);
        this.POOL.add(new ProductEntry(ModItems.AXE, 1, 1, "{Vault:{Attributes:[{Id:\\\"the_vault:gear_roll_pool\\\",BaseValue:\\\"special\\\"}]}}"), 3);
        this.POOL.add(new ProductEntry(ModItems.IDOL_BENEVOLENT, 1, 1, "{Vault:{Attributes:[{Id:\\\"the_vault:gear_roll_pool\\\",BaseValue:\\\"special\\\"}]}}"), 3);
        this.POOL.add(new ProductEntry(ModItems.IDOL_OMNISCIENT, 1, 1, "{Vault:{Attributes:[{Id:\\\"the_vault:gear_roll_pool\\\",BaseValue:\\\"special\\\"}]}}"), 3);
        this.POOL.add(new ProductEntry(ModItems.IDOL_TIMEKEEPER, 1, 1, "{Vault:{Attributes:[{Id:\\\"the_vault:gear_roll_pool\\\",BaseValue:\\\"special\\\"}]}}"), 3);
        this.POOL.add(new ProductEntry(ModItems.IDOL_MALEVOLENCE, 1, 1, "{Vault:{Attributes:[{Id:\\\"the_vault:gear_roll_pool\\\",BaseValue:\\\"special\\\"}]}}"), 3);
        this.POOL.add(new ProductEntry(ModItems.POG, 1, 1, (CompoundNBT) null), 1);
        this.POOL.add(new ProductEntry(ModItems.VAULT_CRYSTAL, 1, 1, (CompoundNBT) null), 1);
        this.POOL.add(new ProductEntry(ModItems.VAULT_CATALYST_FRAGMENT, 1, 8, (CompoundNBT) null), 3);
        this.POOL.add(new ProductEntry(ModItems.VAULT_CATALYST, 1, 1, (CompoundNBT) null), 1);
        this.POOL.add(new ProductEntry(ModItems.VAULT_BURGER, 1, 12, (CompoundNBT) null), 10);
        this.POOL.add(new ProductEntry(ModItems.CRYSTAL_BURGER, 1, 6, (CompoundNBT) null), 6);
        this.POOL.add(new ProductEntry(ModItems.FULL_PIZZA, 1, 6, (CompoundNBT) null), 6);
        this.POOL.add(new ProductEntry(ModItems.OOZING_PIZZA, 6, 12, (CompoundNBT) null), 10);
        this.POOL.add(new ProductEntry(ModItems.LIFE_SCROLL, 1, 1, (CompoundNBT) null), 4);
        this.POOL.add(new ProductEntry(ModItems.HUNTER_EYE, 1, 2, (CompoundNBT) null), 1);
        this.POOL.add(new ProductEntry(ModItems.POISONOUS_MUSHROOM, 2, 6, (CompoundNBT) null), 3);
        this.POOL.add(new ProductEntry(ModItems.VAULT_DIAMOND, 1, 3, (CompoundNBT) null), 4);
        this.POOL.add(new ProductEntry(ModItems.DRILL_ARROW, 4, 16, (CompoundNBT) null), 8);
        this.POOL.add(new ProductEntry(ModItems.RESPEC_FLASK, 1, 1, (CompoundNBT) null), 5);
        this.POOL.add(new ProductEntry(ModItems.WUTAX_SHARD, 2, 6, (CompoundNBT) null), 8);
        this.POOL.add(new ProductEntry(ModItems.VAULT_BRONZE, 32, 64, (CompoundNBT) null), 12);
        this.POOL.add(new ProductEntry(ModItems.VAULT_PLATING, 4, 8, (CompoundNBT) null), 12);
        this.POOL.add(new ProductEntry(ModItems.VAULT_DUST, 2, 6, (CompoundNBT) null), 8);
        this.POOL.add(new ProductEntry(Items.NETHERITE_INGOT, 2, 8, (CompoundNBT) null), 4);
        this.POOL.add(new ProductEntry(Items.NETHERITE_BLOCK, 1, 1, (CompoundNBT) null), 3);
        this.POOL.add(new ProductEntry(ModItems.CANDY_BAR, 2, 4, (CompoundNBT) null), 8);
        this.POOL.add(new ProductEntry(ModItems.POWER_BAR, 1, 1, (CompoundNBT) null), 8);
        this.POOL.add(new ProductEntry(ModItems.LUCKY_APPLE, 1, 1, (CompoundNBT) null), 1);
        this.POOL.add(new ProductEntry(ModItems.GOLEM_APPLE, 1, 1, (CompoundNBT) null), 2);
        this.POOL.add(new ProductEntry(ModItems.GHOST_APPLE, 1, 1, (CompoundNBT) null), 2);
        this.POOL.add(new ProductEntry(ModItems.VAULT_APPLE, 8, 16, (CompoundNBT) null), 8);
        this.POOL.add(new ProductEntry(ModItems.HEARTY_APPLE, 8, 16, (CompoundNBT) null), 8);
        this.POOL.add(new ProductEntry(Items.GOLDEN_APPLE, 8, 16, (CompoundNBT) null), 10);
        this.POOL.add(new ProductEntry(ModItems.VAULT_ESSENCE, 1, 5, (CompoundNBT) null), 2);
        this.POOL.add(new ProductEntry(ModItems.VAULT_SCRAP, 2, 4, (CompoundNBT) null), 7);
        this.POOL.add(new ProductEntry(Items.KELP, 1, 1, (CompoundNBT) null), 57);
        this.POOL.add(new ProductEntry(Items.HORN_CORAL_BLOCK, 1, 1, (CompoundNBT) null), 12);
    }
}
