package iskallia.vault.util;

import iskallia.vault.init.ModAttributes;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.item.gear.VaultGear;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IItemProvider;

public class GearItemStackBuilder {
    int modelId;
    int specialModelId;
    int color;
    Item item;
    VaultGear.Rarity gearRarity;

    public GearItemStackBuilder(final Item item) {
        this.modelId = -1;
        this.specialModelId = -1;
        this.color = -1;
        this.item = null;
        this.gearRarity = VaultGear.Rarity.UNIQUE;
        if (!(item instanceof VaultGear)) {
            throw new IllegalArgumentException("Expected a vault gear item");
        }
        this.item = item;
    }

    public GearItemStackBuilder setColor(final int color) {
        this.color = color;
        return this;
    }

    public GearItemStackBuilder setModelId(final int modelId) {
        this.modelId = modelId;
        return this;
    }

    public GearItemStackBuilder setSpecialModelId(final int specialModelId) {
        this.specialModelId = specialModelId;
        return this;
    }

    public GearItemStackBuilder setGearRarity(final VaultGear.Rarity gearRarity) {
        this.gearRarity = gearRarity;
        return this;
    }

    public ItemStack build() {
        final ItemStack itemStack = new ItemStack(this.item);
        ModAttributes.GEAR_STATE.create(itemStack, VaultGear.State.IDENTIFIED);
        ModAttributes.GEAR_RARITY.create(itemStack, this.gearRarity);
        itemStack.getOrCreateTag().remove("RollTicks");
        itemStack.getOrCreateTag().remove("LastModelHit");
        ModAttributes.GEAR_ROLL_TYPE.create(itemStack, ModConfigs.VAULT_GEAR.DEFAULT_ROLL);
        ModAttributes.GEAR_COLOR.create(itemStack, this.color);
        ModAttributes.GEAR_MODEL.create(itemStack, this.modelId);
        ModAttributes.GEAR_SPECIAL_MODEL.create(itemStack, this.specialModelId);
        return itemStack;
    }
}
