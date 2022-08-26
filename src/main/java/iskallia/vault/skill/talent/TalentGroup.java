package iskallia.vault.skill.talent;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.gson.annotations.Expose;
import iskallia.vault.skill.talent.type.AttributeTalent;
import iskallia.vault.skill.talent.type.EffectTalent;
import iskallia.vault.skill.talent.type.PlayerTalent;
import iskallia.vault.util.RomanNumber;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.potion.Effect;

import java.util.function.IntFunction;
import java.util.function.IntToDoubleFunction;
import java.util.function.IntUnaryOperator;
import java.util.stream.IntStream;

public class TalentGroup<T extends PlayerTalent> {
    @Expose
    private final String name;
    @Expose
    private final T[] levels;
    private BiMap<String, T> registry;

    public TalentGroup(final String name, final T... levels) {
        this.name = name;
        this.levels = levels;
    }

    public int getMaxLevel() {
        return this.levels.length;
    }

    public String getParentName() {
        return this.name;
    }

    public String getName(final int level) {
        if (level == 0) {
            return this.name + " " + RomanNumber.toRoman(0);
        }
        return this.getRegistry().inverse().get(this.getTalent(level));
    }

    public T getTalent(final int level) {
        if (level < 0) {
            return this.levels[0];
        }
        if (level >= this.getMaxLevel()) {
            return this.levels[this.getMaxLevel() - 1];
        }
        return this.levels[level - 1];
    }

    public int learningCost() {
        return this.levels[0].getCost();
    }

    public int cost(final int level) {
        if (level > this.getMaxLevel()) {
            return -1;
        }
        return this.levels[level - 1].getCost();
    }

    public BiMap<String, T> getRegistry() {
        if (this.registry == null) {
            this.registry = HashBiMap.create(this.getMaxLevel());
            if (this.getMaxLevel() == 1) {
                this.registry.put(this.getParentName(), this.levels[0]);
            } else if (this.getMaxLevel() > 1) {
                for (int i = 0; i < this.getMaxLevel(); ++i) {
                    this.registry.put((this.getParentName() + " " + RomanNumber.toRoman(i + 1)), this.getTalent(i + 1));
                }
            }
        }
        return this.registry;
    }

    public static TalentGroup<EffectTalent> ofEffect(final String name, final Effect effect, final EffectTalent.Type type, final int maxLevel, final IntUnaryOperator cost, final EffectTalent.Operator operator) {
        final EffectTalent[] talents = IntStream.range(0, maxLevel).mapToObj(i -> new EffectTalent(cost.applyAsInt(i + 1), effect, i, type, operator)).toArray(EffectTalent[]::new);
        return new TalentGroup<EffectTalent>(name, talents);
    }

    public static TalentGroup<AttributeTalent> ofAttribute(String name, Attribute attribute, String modifierName, int maxLevel, IntUnaryOperator cost, IntToDoubleFunction amount, IntFunction<AttributeModifier.Operation> operation) {
        AttributeTalent[] talents = IntStream.range(0, maxLevel).mapToObj(i -> new AttributeTalent(cost.applyAsInt(i + 1), attribute, new AttributeTalent.Modifier(modifierName + " " + RomanNumber.toRoman(i + 1), amount.applyAsDouble(i + 1), operation.apply(i + 1)))).toArray(x$0 -> new AttributeTalent[x$0]);
        return new TalentGroup<>(name, talents);
    }

    public static <T extends PlayerTalent> TalentGroup<T> of(final String name, final int maxLevel, final IntFunction<T> supplier) {
        final PlayerTalent[] talents = IntStream.range(0, maxLevel).mapToObj((IntFunction<?>) supplier).toArray(PlayerTalent[]::new);
        return new TalentGroup<T>(name, (T[]) talents);
    }
}
