package iskallia.vault.skill.talent;

import iskallia.vault.skill.talent.type.PlayerTalent;

import java.util.function.IntFunction;
import java.util.stream.IntStream;

public class ArchetypeTalentGroup<T extends PlayerTalent> extends TalentGroup<T> {
    public ArchetypeTalentGroup(final String name, final T... levels) {
        super(name, levels);
    }

    public static <T extends PlayerTalent> ArchetypeTalentGroup<T> of(final String name, final int maxLevel, final IntFunction<T> supplier) {
        final PlayerTalent[] talents = IntStream.range(0, maxLevel).mapToObj((IntFunction<?>) supplier).toArray(PlayerTalent[]::new);
        return new ArchetypeTalentGroup<T>(name, (T[]) talents);
    }
}
