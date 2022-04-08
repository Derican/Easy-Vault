package iskallia.vault.config;

import com.google.gson.annotations.Expose;
import iskallia.vault.item.gear.VaultGear;
import iskallia.vault.util.data.WeightedList;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

public class EtchingConfig
        extends Config {
    @Expose
    public Map<VaultGear.Set, Etching> ETCHINGS;

    public String getName() {
        return "etching";
    }

    public VaultGear.Set getRandomSet() {
        return getRandomSet(new Random());
    }

    public VaultGear.Set getRandomSet(Random random) {
        WeightedList<VaultGear.Set> list = new WeightedList();
        this.ETCHINGS.forEach((set, etching) -> {
            if (set != VaultGear.Set.NONE) {
                list.add(set, etching.weight);
            }
        });
        return (VaultGear.Set) list.getRandom(random);
    }

    public Etching getFor(VaultGear.Set set) {
        return this.ETCHINGS.get(set);
    }


    protected void reset() {
        this.ETCHINGS = new LinkedHashMap<>();

        for (VaultGear.Set set : VaultGear.Set.values()) {
            this.ETCHINGS.put(set, new Etching(1, 1, 2, "yes", 5636095));
        }
    }


    public static class Etching {
        @Expose
        public int weight;
        @Expose
        public int minValue;

        public Etching(int weight, int minValue, int maxValue, String effectText, int color) {
            this.weight = weight;
            this.minValue = minValue;
            this.maxValue = maxValue;
            this.effectText = effectText;
            this.color = color;
        }

        @Expose
        public int maxValue;
        @Expose
        public String effectText;
        @Expose
        public int color;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\config\EtchingConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */