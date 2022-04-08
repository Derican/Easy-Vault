package iskallia.vault.config;

import com.google.gson.annotations.Expose;


public class VaultGearUtilitiesConfig
        extends Config {
    @Expose
    private float voidOrbRepairCostChance;

    public String getName() {
        return "vault_gear_utilities";
    }

    @Expose
    private float voidOrbPredefinedRepairCostChance;
    @Expose
    private float fabricationJewelKeepModifierChance;

    public float getVoidOrbRepairCostChance() {
        return this.voidOrbRepairCostChance;
    }

    public float getVoidOrbPredefinedRepairCostChance() {
        return this.voidOrbPredefinedRepairCostChance;
    }

    public float getFabricationJewelKeepModifierChance() {
        return this.fabricationJewelKeepModifierChance;
    }


    protected void reset() {
        this.voidOrbRepairCostChance = 0.2F;
        this.voidOrbPredefinedRepairCostChance = 0.4F;
        this.fabricationJewelKeepModifierChance = 0.8F;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\config\VaultGearUtilitiesConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */