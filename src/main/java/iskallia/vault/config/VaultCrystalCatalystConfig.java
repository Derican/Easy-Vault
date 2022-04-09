// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.config;

import com.google.gson.annotations.Expose;
import iskallia.vault.item.catalyst.CompoundModifierOutcome;
import iskallia.vault.item.catalyst.ModifierRollType;
import iskallia.vault.item.catalyst.SingleModifierOutcome;
import iskallia.vault.util.data.WeightedList;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Predicate;

public class VaultCrystalCatalystConfig extends Config
{
    private static final Random rand;
    @Expose
    private final Map<String, TaggedPool> TAGGED_MODIFIER_POOLS;
    @Expose
    private final WeightedList<CompoundModifierOutcome> OUTCOMES;
    
    public VaultCrystalCatalystConfig() {
        this.TAGGED_MODIFIER_POOLS = new HashMap<String, TaggedPool>();
        this.OUTCOMES = new WeightedList<CompoundModifierOutcome>();
    }
    
    @Override
    public String getName() {
        return "vault_crystal_catalyst_modifiers";
    }
    
    @Nullable
    public CompoundModifierOutcome getModifiers() {
        return this.OUTCOMES.getRandom(VaultCrystalCatalystConfig.rand);
    }
    
    @Nullable
    public TaggedPool getPool(final String poolName) {
        return this.TAGGED_MODIFIER_POOLS.get(poolName);
    }
    
    @Override
    protected void reset() {
        this.TAGGED_MODIFIER_POOLS.clear();
        this.OUTCOMES.clear();
        this.TAGGED_MODIFIER_POOLS.put("BAD", new TaggedPool("negative", 15597568, new WeightedList<String>().add("Crowded", 1).add("Fast", 1).add("Rush", 1).add("Hard", 1).add("Unlucky", 1).add("Locked", 1)));
        this.TAGGED_MODIFIER_POOLS.put("GOOD", new TaggedPool("positive", 43520, new WeightedList<String>().add("Lonely", 1).add("Easy", 1).add("Treasure", 1).add("Gilded", 1).add("Hoard", 1)));
        this.TAGGED_MODIFIER_POOLS.put("VERY_BAD", new TaggedPool("very negative", 7798784, new WeightedList<String>().add("Chaotic", 1).add("Hard", 1).add("Unlucky", 1)));
        this.OUTCOMES.add(new CompoundModifierOutcome().addOutcome(new SingleModifierOutcome(ModifierRollType.ADD_RANDOM_MODIFIER, "GOOD")), 1);
        this.OUTCOMES.add(new CompoundModifierOutcome().addOutcome(new SingleModifierOutcome(ModifierRollType.ADD_RANDOM_MODIFIER, "GOOD")).addOutcome(new SingleModifierOutcome(ModifierRollType.ADD_RANDOM_MODIFIER, "BAD")), 1);
        this.OUTCOMES.add(new CompoundModifierOutcome().addOutcome(new SingleModifierOutcome(ModifierRollType.ADD_RANDOM_MODIFIER, "GOOD")).addOutcome(new SingleModifierOutcome(ModifierRollType.ADD_SPECIFIC_MODIFIER, "BAD")), 1);
        this.OUTCOMES.add(new CompoundModifierOutcome().addOutcome(new SingleModifierOutcome(ModifierRollType.ADD_SPECIFIC_MODIFIER, "GOOD")).addOutcome(new SingleModifierOutcome(ModifierRollType.ADD_RANDOM_MODIFIER, "BAD")), 1);
        this.OUTCOMES.add(new CompoundModifierOutcome().addOutcome(new SingleModifierOutcome(ModifierRollType.ADD_SPECIFIC_MODIFIER, "GOOD")).addOutcome(new SingleModifierOutcome(ModifierRollType.ADD_SPECIFIC_MODIFIER, "BAD")), 1);
    }
    
    static {
        rand = new Random();
    }
    
    public static class TaggedPool
    {
        @Expose
        private final String displayName;
        @Expose
        private final int color;
        @Expose
        private final WeightedList<String> modifiers;
        
        public TaggedPool(final String displayName, final int color, final WeightedList<String> modifiers) {
            this.displayName = displayName;
            this.color = color;
            this.modifiers = modifiers;
        }
        
        public IFormattableTextComponent getDisplayName() {
            final StringTextComponent cmp = new StringTextComponent(this.displayName);
            cmp.setStyle(Style.EMPTY.withColor(Color.fromRgb(this.color)));
            return (IFormattableTextComponent)cmp;
        }
        
        @Nullable
        public String getModifier(final Random random) {
            return this.getModifier(random, mod -> false);
        }
        
        @Nullable
        public String getModifier(final Random random, final Predicate<String> modifierFilter) {
            final WeightedList<String> filteredModifiers = this.modifiers.copy();
            filteredModifiers.removeIf(entry -> modifierFilter.test(entry.value));
            return filteredModifiers.getRandom(random);
        }
    }
}
