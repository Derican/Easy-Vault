package iskallia.vault.container.inventory;

import iskallia.vault.attribute.IntegerAttribute;
import iskallia.vault.container.base.RecipeInventory;
import iskallia.vault.init.ModAttributes;
import iskallia.vault.init.ModItems;
import iskallia.vault.init.ModModels;
import iskallia.vault.item.gear.VaultArmorItem;
import iskallia.vault.item.gear.VaultGear;
import iskallia.vault.item.gear.VaultSwordItem;
import net.minecraft.entity.MobEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

public class TransmogTableInventory extends RecipeInventory {
    public static final int GEAR_SLOT = 0;
    public static final int APPEARANCE_SLOT = 1;
    public static final int BRONZE_SLOT = 2;

    public TransmogTableInventory() {
        super(3);
    }

    public int requiredVaultBronze() {
        final ItemStack gearStack = this.getItem(0);
        if (gearStack.isEmpty()) {
            return -1;
        }
        final IntegerAttribute levelAttr = ModAttributes.MIN_VAULT_LEVEL.getOrDefault(gearStack, 1);
        final int gearLevel = levelAttr.getValue(gearStack);
        return MathHelper.clamp(gearLevel, 1, 64);
    }

    @Override
    public boolean recipeFulfilled() {
        final ItemStack gearStack = this.getItem(0);
        final ItemStack appearanceStack = this.getItem(1);
        final ItemStack bronzeStack = this.getItem(2);
        if (gearStack.getItem() instanceof VaultArmorItem && appearanceStack.getItem() instanceof VaultArmorItem) {
            return this.armorRecipeFulfilled(gearStack, appearanceStack, bronzeStack);
        }
        return gearStack.getItem() instanceof VaultSwordItem && appearanceStack.getItem() instanceof VaultSwordItem && this.swordRecipeFulfilled(gearStack, appearanceStack, bronzeStack);
    }

    private boolean armorRecipeFulfilled(final ItemStack armorStack, final ItemStack appearanceStack, final ItemStack bronzeStack) {
        final VaultGear.Rarity armorRarity = ModAttributes.GEAR_RARITY.getBase(armorStack).orElse(VaultGear.Rarity.SCRAPPY);
        final VaultGear.Rarity appearanceRarity = ModAttributes.GEAR_RARITY.getBase(appearanceStack).orElse(VaultGear.Rarity.SCRAPPY);
        if (armorRarity == VaultGear.Rarity.SCRAPPY) {
            return false;
        }
        if (appearanceRarity == VaultGear.Rarity.SCRAPPY) {
            return false;
        }
        final EquipmentSlotType armorSlot = MobEntity.getEquipmentSlotForItem(appearanceStack);
        final EquipmentSlotType appearanceSlot = MobEntity.getEquipmentSlotForItem(armorStack);
        if (armorSlot != appearanceSlot) {
            return false;
        }
        final int armorSpecialModel = ModAttributes.GEAR_SPECIAL_MODEL.getBase(armorStack).orElse(-1);
        final int appearanceSpecialModel = ModAttributes.GEAR_SPECIAL_MODEL.getBase(appearanceStack).orElse(-1);
        if (armorSpecialModel != -1) {
            return false;
        }
        if (appearanceSpecialModel != -1) {
            final ModModels.SpecialGearModel specialGearModel = ModModels.SpecialGearModel.getModel(appearanceSlot, appearanceSpecialModel);
            if (specialGearModel != null && !specialGearModel.getModelProperties().doesAllowTransmogrification()) {
                return false;
            }
        }
        return bronzeStack.getItem() == ModItems.VAULT_BRONZE && bronzeStack.getCount() >= this.requiredVaultBronze();
    }

    private boolean swordRecipeFulfilled(final ItemStack swordStack, final ItemStack appearanceStack, final ItemStack bronzeStack) {
        final VaultGear.Rarity swordRarity = ModAttributes.GEAR_RARITY.getBase(swordStack).orElse(VaultGear.Rarity.SCRAPPY);
        final VaultGear.Rarity appearanceRarity = ModAttributes.GEAR_RARITY.getBase(appearanceStack).orElse(VaultGear.Rarity.SCRAPPY);
        if (swordRarity == VaultGear.Rarity.SCRAPPY) {
            return false;
        }
        if (appearanceRarity == VaultGear.Rarity.SCRAPPY) {
            return false;
        }
        final int armorSpecialModel = ModAttributes.GEAR_SPECIAL_MODEL.getBase(swordStack).orElse(-1);
        final int appearanceSpecialModel = ModAttributes.GEAR_SPECIAL_MODEL.getBase(appearanceStack).orElse(-1);
        if (armorSpecialModel != -1) {
            return false;
        }
        if (appearanceSpecialModel != -1) {
            final ModModels.SpecialSwordModel specialSwordModel = ModModels.SpecialSwordModel.getModel(appearanceSpecialModel);
            if (specialSwordModel != null && !specialSwordModel.getModelProperties().doesAllowTransmogrification()) {
                return false;
            }
        }
        return bronzeStack.getItem() == ModItems.VAULT_BRONZE && bronzeStack.getCount() >= this.requiredVaultBronze();
    }

    @Override
    public ItemStack resultingItemStack() {
        final ItemStack gearStack = this.getItem(0);
        final ItemStack appearanceStack = this.getItem(1);
        final IntegerAttribute modelAttr = ModAttributes.GEAR_MODEL.getOrDefault(appearanceStack, 0);
        final int modelId = modelAttr.getValue(appearanceStack);
        final IntegerAttribute specialModelAttr = ModAttributes.GEAR_SPECIAL_MODEL.getOrDefault(appearanceStack, -1);
        final int specialModelId = specialModelAttr.getValue(appearanceStack);
        final ItemStack resultingStack = gearStack.copy();
        ModAttributes.GEAR_MODEL.create(resultingStack, modelId);
        if (specialModelId != -1) {
            ModAttributes.GEAR_SPECIAL_MODEL.create(resultingStack, specialModelId);
        }
        return resultingStack;
    }

    @Override
    public void consumeIngredients() {
        this.removeItem(2, this.requiredVaultBronze());
        this.removeItem(0, 1);
    }
}
