package iskallia.vault.config;

import com.google.gson.annotations.Expose;
import iskallia.vault.config.entry.RangeEntry;
import iskallia.vault.util.data.WeightedList;

import java.util.Random;

public class FlawedRubyConfig
        extends Config {
    @Expose
    private RubyEntry ARTISAN;
    @Expose
    private RubyEntry TREASURE_HUNTER;

    public String getName() {
        return "flawed_ruby";
    }


    protected void reset() {
        WeightedList<Outcome> artisanOutcomes = new WeightedList();
        artisanOutcomes.add(new WeightedList.Entry(Outcome.FAIL, 5));
        artisanOutcomes.add(new WeightedList.Entry(Outcome.IMBUE, 25));
        artisanOutcomes.add(new WeightedList.Entry(Outcome.BREAK, 70));
        this.ARTISAN = new RubyEntry(artisanOutcomes, new RangeEntry(1, 1));

        WeightedList<Outcome> treasureHunterOutcomes = new WeightedList();
        treasureHunterOutcomes.add(new WeightedList.Entry(Outcome.FAIL, 5));
        treasureHunterOutcomes.add(new WeightedList.Entry(Outcome.IMBUE, 5));
        treasureHunterOutcomes.add(new WeightedList.Entry(Outcome.BREAK, 90));
        this.TREASURE_HUNTER = new RubyEntry(treasureHunterOutcomes, new RangeEntry(1, 1));
    }

    public Outcome getForArtisan() {
        return (Outcome) this.ARTISAN.outcomes.getRandom(new Random());
    }

    public Outcome getForTreasureHunter() {
        return (Outcome) this.TREASURE_HUNTER.outcomes.getRandom(new Random());
    }

    public int getArtisanAdditionalModifierCount() {
        return this.ARTISAN.additionModifierCount.getRandom();
    }

    public int getTreasureHunterAdditionalModifierCount() {
        return this.TREASURE_HUNTER.additionModifierCount.getRandom();
    }

    public static class RubyEntry {
        @Expose
        private WeightedList<FlawedRubyConfig.Outcome> outcomes;

        public RubyEntry(WeightedList<FlawedRubyConfig.Outcome> outcomes, RangeEntry additionModifierCount) {
            this.outcomes = outcomes;
            this.additionModifierCount = additionModifierCount;
        }

        @Expose
        private RangeEntry additionModifierCount;
    }

    public enum Outcome {
        FAIL,
        IMBUE,
        BREAK;
    }

}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\config\FlawedRubyConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */