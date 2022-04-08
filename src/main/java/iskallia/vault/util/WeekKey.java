package iskallia.vault.util;

import net.minecraft.nbt.CompoundNBT;

import java.time.LocalDateTime;
import java.time.temporal.IsoFields;
import java.util.Objects;


public class WeekKey {
    private final int year;
    private final int week;

    private WeekKey(int year, int week) {
        this.year = year;
        this.week = week;
    }

    public int getYear() {
        return this.year;
    }

    public int getWeek() {
        return this.week;
    }

    public static WeekKey of(int year, int week) {
        return new WeekKey(year, week);
    }

    public static WeekKey current() {
        LocalDateTime ldt = LocalDateTime.now();
        int year = ldt.get(IsoFields.WEEK_BASED_YEAR);
        int week = ldt.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
        return new WeekKey(year, week);
    }

    public static WeekKey previous() {
        LocalDateTime ldt = LocalDateTime.now().minusWeeks(1L);
        int year = ldt.get(IsoFields.WEEK_BASED_YEAR);
        int week = ldt.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
        return new WeekKey(year, week);
    }

    public CompoundNBT serialize() {
        CompoundNBT tag = new CompoundNBT();
        tag.putInt("year", this.year);
        tag.putInt("week", this.week);
        return tag;
    }

    public static WeekKey deserialize(CompoundNBT tag) {
        return new WeekKey(tag.getInt("year"), tag.getInt("week"));
    }


    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WeekKey weekKey = (WeekKey) o;
        return (this.year == weekKey.year && this.week == weekKey.week);
    }


    public int hashCode() {
        return Objects.hash(new Object[]{Integer.valueOf(this.year), Integer.valueOf(this.week)});
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vaul\\util\WeekKey.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */