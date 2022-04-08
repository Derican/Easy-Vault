package iskallia.vault.skill.talent;

import iskallia.vault.skill.talent.type.PlayerTalent;

import java.util.function.IntFunction;
import java.util.stream.IntStream;

public class ArchetypeTalentGroup<T extends PlayerTalent>
        extends TalentGroup<T> {
    public ArchetypeTalentGroup(String name, T... levels) {
        super(name, levels);
    }

    public static <T extends PlayerTalent> ArchetypeTalentGroup<T> of(String name, int maxLevel, IntFunction<T> supplier) {
        PlayerTalent[] talents = (PlayerTalent[]) IntStream.range(0, maxLevel).<T>mapToObj(supplier).toArray(x$0 -> new PlayerTalent[x$0]);
        return new ArchetypeTalentGroup<>(name, (T[]) talents);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\talent\ArchetypeTalentGroup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */