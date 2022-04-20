package iskallia.vault.config;

import com.google.gson.annotations.Expose;
import iskallia.vault.config.entry.RangeEntry;
import iskallia.vault.util.data.WeightedList;

import java.util.Random;

public class FlawedRubyConfig extends Config {
    @Expose
    private RubyEntry ARTISAN;
    @Expose
    private RubyEntry TREASURE_HUNTER;

    @Override
    public String getName() {
        return "flawed_ruby";
    }

    @Override
    protected void reset() {
        final WeightedList<Outcome> artisanOutcomes = new WeightedList<Outcome>();
        artisanOutcomes.add(new WeightedList.Entry<Outcome>(Outcome.FAIL, 5));
        artisanOutcomes.add(new WeightedList.Entry<Outcome>(Outcome.IMBUE, 25));
        artisanOutcomes.add(new WeightedList.Entry<Outcome>(Outcome.BREAK, 70));
        this.ARTISAN = new RubyEntry(artisanOutcomes, new RangeEntry(1, 1));
        final WeightedList<Outcome> treasureHunterOutcomes = new WeightedList<Outcome>();
        treasureHunterOutcomes.add(new WeightedList.Entry<Outcome>(Outcome.FAIL, 5));
        treasureHunterOutcomes.add(new WeightedList.Entry<Outcome>(Outcome.IMBUE, 5));
        treasureHunterOutcomes.add(new WeightedList.Entry<Outcome>(Outcome.BREAK, 90));
        this.TREASURE_HUNTER = new RubyEntry(treasureHunterOutcomes, new RangeEntry(1, 1));
    }

    public Outcome getForArtisan() {
        return this.ARTISAN.outcomes.getRandom(new Random());
    }

    public Outcome getForTreasureHunter() {
        return this.TREASURE_HUNTER.outcomes.getRandom(new Random());
    }

    public int getArtisanAdditionalModifierCount() {
        return this.ARTISAN.additionModifierCount.getRandom();
    }

    public int getTreasureHunterAdditionalModifierCount() {
        return this.TREASURE_HUNTER.additionModifierCount.getRandom();
    }

    public static class RubyEntry {
        @Expose
        private WeightedList<Outcome> outcomes;
        @Expose
        private RangeEntry additionModifierCount;

        public RubyEntry(final WeightedList<Outcome> outcomes, final RangeEntry additionModifierCount) {
            this.outcomes = outcomes;
            this.additionModifierCount = additionModifierCount;
        }
    }

    public enum Outcome {
        FAIL,
        IMBUE,
        BREAK;
    }
}
