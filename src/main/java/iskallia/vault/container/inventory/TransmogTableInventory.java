package iskallia.vault.container.inventory;

import iskallia.vault.attribute.IntegerAttribute;
import iskallia.vault.container.base.RecipeInventory;
import iskallia.vault.init.ModAttributes;
import iskallia.vault.init.ModItems;
import iskallia.vault.init.ModModels;
import iskallia.vault.item.gear.VaultGear;
import net.minecraft.entity.MobEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;


public class TransmogTableInventory
        extends RecipeInventory {
    public static final int GEAR_SLOT = 0;
    public static final int APPEARANCE_SLOT = 1;
    public static final int BRONZE_SLOT = 2;

    public TransmogTableInventory() {
        super(3);
    }

    public int requiredVaultBronze() {
        ItemStack gearStack = getItem(0);

        if (gearStack.isEmpty()) return -1;

        IntegerAttribute levelAttr = (IntegerAttribute) ModAttributes.MIN_VAULT_LEVEL.getOrDefault(gearStack, Integer.valueOf(1));
        int gearLevel = ((Integer) levelAttr.getValue(gearStack)).intValue();

        return MathHelper.clamp(gearLevel, 1, 64);
    }


    public boolean recipeFulfilled() {
        ItemStack gearStack = getItem(0);
        ItemStack appearanceStack = getItem(1);
        ItemStack bronzeStack = getItem(2);

        if (gearStack.getItem() instanceof iskallia.vault.item.gear.VaultArmorItem && appearanceStack
                .getItem() instanceof iskallia.vault.item.gear.VaultArmorItem) {
            return armorRecipeFulfilled(gearStack, appearanceStack, bronzeStack);
        }

        if (gearStack.getItem() instanceof iskallia.vault.item.gear.VaultSwordItem && appearanceStack
                .getItem() instanceof iskallia.vault.item.gear.VaultSwordItem) {
            return swordRecipeFulfilled(gearStack, appearanceStack, bronzeStack);
        }

        return false;
    }

    private boolean armorRecipeFulfilled(ItemStack armorStack, ItemStack appearanceStack, ItemStack bronzeStack) {
        VaultGear.Rarity armorRarity = ModAttributes.GEAR_RARITY.getBase(armorStack).orElse(VaultGear.Rarity.SCRAPPY);
        VaultGear.Rarity appearanceRarity = ModAttributes.GEAR_RARITY.getBase(appearanceStack).orElse(VaultGear.Rarity.SCRAPPY);


        if (armorRarity == VaultGear.Rarity.SCRAPPY)
            return false;
        if (appearanceRarity == VaultGear.Rarity.SCRAPPY) {
            return false;
        }
        EquipmentSlotType armorSlot = MobEntity.getEquipmentSlotForItem(appearanceStack);
        EquipmentSlotType appearanceSlot = MobEntity.getEquipmentSlotForItem(armorStack);


        if (armorSlot != appearanceSlot) {
            return false;
        }
        int armorSpecialModel = ((Integer) ModAttributes.GEAR_SPECIAL_MODEL.getBase(armorStack).orElse(Integer.valueOf(-1))).intValue();
        int appearanceSpecialModel = ((Integer) ModAttributes.GEAR_SPECIAL_MODEL.getBase(appearanceStack).orElse(Integer.valueOf(-1))).intValue();


        if (armorSpecialModel != -1) {
            return false;
        }

        if (appearanceSpecialModel != -1) {
            ModModels.SpecialGearModel specialGearModel = ModModels.SpecialGearModel.getModel(appearanceSlot, appearanceSpecialModel);
            if (specialGearModel != null && !specialGearModel.getModelProperties().doesAllowTransmogrification()) {
                return false;
            }
        }


        return (bronzeStack.getItem() == ModItems.VAULT_BRONZE && bronzeStack
                .getCount() >= requiredVaultBronze());
    }

    private boolean swordRecipeFulfilled(ItemStack swordStack, ItemStack appearanceStack, ItemStack bronzeStack) {
        VaultGear.Rarity swordRarity = ModAttributes.GEAR_RARITY.getBase(swordStack).orElse(VaultGear.Rarity.SCRAPPY);
        VaultGear.Rarity appearanceRarity = ModAttributes.GEAR_RARITY.getBase(appearanceStack).orElse(VaultGear.Rarity.SCRAPPY);


        if (swordRarity == VaultGear.Rarity.SCRAPPY)
            return false;
        if (appearanceRarity == VaultGear.Rarity.SCRAPPY) {
            return false;
        }
        int armorSpecialModel = ((Integer) ModAttributes.GEAR_SPECIAL_MODEL.getBase(swordStack).orElse(Integer.valueOf(-1))).intValue();
        int appearanceSpecialModel = ((Integer) ModAttributes.GEAR_SPECIAL_MODEL.getBase(appearanceStack).orElse(Integer.valueOf(-1))).intValue();


        if (armorSpecialModel != -1) {
            return false;
        }

        if (appearanceSpecialModel != -1) {
            ModModels.SpecialSwordModel specialSwordModel = ModModels.SpecialSwordModel.getModel(appearanceSpecialModel);
            if (specialSwordModel != null && !specialSwordModel.getModelProperties().doesAllowTransmogrification()) {
                return false;
            }
        }


        return (bronzeStack.getItem() == ModItems.VAULT_BRONZE && bronzeStack
                .getCount() >= requiredVaultBronze());
    }


    public ItemStack resultingItemStack() {
        ItemStack gearStack = getItem(0);
        ItemStack appearanceStack = getItem(1);

        IntegerAttribute modelAttr = (IntegerAttribute) ModAttributes.GEAR_MODEL.getOrDefault(appearanceStack, Integer.valueOf(0));
        int modelId = ((Integer) modelAttr.getValue(appearanceStack)).intValue();

        IntegerAttribute specialModelAttr = (IntegerAttribute) ModAttributes.GEAR_SPECIAL_MODEL.getOrDefault(appearanceStack, Integer.valueOf(-1));
        int specialModelId = ((Integer) specialModelAttr.getValue(appearanceStack)).intValue();

        ItemStack resultingStack = gearStack.copy();
        ModAttributes.GEAR_MODEL.create(resultingStack, Integer.valueOf(modelId));
        if (specialModelId != -1)
            ModAttributes.GEAR_SPECIAL_MODEL.create(resultingStack, Integer.valueOf(specialModelId));
        return resultingStack;
    }


    public void consumeIngredients() {
        removeItem(2, requiredVaultBronze());
        removeItem(0, 1);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\container\inventory\TransmogTableInventory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */