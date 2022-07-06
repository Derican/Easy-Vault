package iskallia.vault.config;

import com.google.gson.annotations.Expose;
import iskallia.vault.item.gear.VaultGear;
import iskallia.vault.util.data.WeightedList;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

public class EtchingConfig extends Config {
    @Expose
    public Map<VaultGear.Set, Etching> ETCHINGS;

    @Override
    public String getName() {
        return "etching";
    }

    public VaultGear.Set getRandomSet() {
        return this.getRandomSet(new Random());
    }

    public VaultGear.Set getRandomSet(final Random random) {
        final WeightedList<VaultGear.Set> list = new WeightedList<VaultGear.Set>();
        this.ETCHINGS.forEach((set, etching) -> {
            if (set != VaultGear.Set.NONE) {
                list.add(set, etching.weight);
            }
            return;
        });
        return list.getRandom(random);
    }

    public Etching getFor(final VaultGear.Set set) {
        return this.ETCHINGS.get(set);
    }

    @Override
    protected void reset() {
        this.ETCHINGS = new LinkedHashMap<VaultGear.Set, Etching>();
        this.ETCHINGS.put(VaultGear.Set.NONE, new Etching(1, 1, 2, "yes", 5636095));
        this.ETCHINGS.put(VaultGear.Set.DRAGON, new Etching(6, 12, 24, "+50% Damage and grants Elytra Wings", 5636095));
        this.ETCHINGS.put(VaultGear.Set.ZOD, new Etching(2, 26, 64, "Makes all armour pieces indestructible", 13158600));
        this.ETCHINGS.put(VaultGear.Set.NINJA, new Etching(10, 7, 16, "+30% Parry and +10% Parry Cap", 5046445));
        this.ETCHINGS.put(VaultGear.Set.GOLEM, new Etching(10, 7, 16, "+20% Resistance Cap", 4029206));
        this.ETCHINGS.put(VaultGear.Set.RIFT, new Etching(5, 14, 28, "+50% Cooldown Reduction, applied on top of regular Cooldown Reduction and surpasses caps", 4029393));
        this.ETCHINGS.put(VaultGear.Set.CARAPACE, new Etching(6, 10, 20, "+50% Absorption Heart Cap", 4039156));
        this.ETCHINGS.put(VaultGear.Set.DIVINITY, new Etching(5, 14, 36, "Grants immunity to all curses", 4046406));
        this.ETCHINGS.put(VaultGear.Set.DRYAD, new Etching(2, 18, 36, "+10 Hearts", 4046461));
        this.ETCHINGS.put(VaultGear.Set.BLOOD, new Etching(4, 12, 28, "+200% Damage", 12386304));
        this.ETCHINGS.put(VaultGear.Set.VAMPIRE, new Etching(10, 7, 16, "+5% Life Leech", 12386427));
        this.ETCHINGS.put(VaultGear.Set.TREASURE, new Etching(5, 18, 64, "+200% Chest Rarity", 12451707));
        this.ETCHINGS.put(VaultGear.Set.ASSASSIN, new Etching(4, 7, 16, "+40% Fatal Strike Chance", 13930844));
        this.ETCHINGS.put(VaultGear.Set.PHOENIX, new Etching(2, 14, 28, "Respawn and reset inventory on death in a vault", 16739386));
        this.ETCHINGS.put(VaultGear.Set.DREAM, new Etching(5, 16, 48, "+50% Damage, +2 Haste, +10% Resistance, +10% Parry, +25% Chest Rarity", 16773120));
        this.ETCHINGS.put(VaultGear.Set.PORCUPINE, new Etching(10, 12, 32, "+40% Thorns Chance, +100% Thorns Damage", 3093151));
    }

    public static class Etching {
        @Expose
        public int weight;
        @Expose
        public int minValue;
        @Expose
        public int maxValue;
        @Expose
        public String effectText;
        @Expose
        public int color;

        public Etching(final int weight, final int minValue, final int maxValue, final String effectText, final int color) {
            this.weight = weight;
            this.minValue = minValue;
            this.maxValue = maxValue;
            this.effectText = effectText;
            this.color = color;
        }
    }
}
