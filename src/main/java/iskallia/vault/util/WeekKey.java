// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.util;

import net.minecraft.nbt.CompoundNBT;

import java.time.LocalDateTime;
import java.time.temporal.IsoFields;
import java.util.Objects;

public class WeekKey
{
    private final int year;
    private final int week;
    
    private WeekKey(final int year, final int week) {
        this.year = year;
        this.week = week;
    }
    
    public int getYear() {
        return this.year;
    }
    
    public int getWeek() {
        return this.week;
    }
    
    public static WeekKey of(final int year, final int week) {
        return new WeekKey(year, week);
    }
    
    public static WeekKey current() {
        final LocalDateTime ldt = LocalDateTime.now();
        final int year = ldt.get(IsoFields.WEEK_BASED_YEAR);
        final int week = ldt.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
        return new WeekKey(year, week);
    }
    
    public static WeekKey previous() {
        final LocalDateTime ldt = LocalDateTime.now().minusWeeks(1L);
        final int year = ldt.get(IsoFields.WEEK_BASED_YEAR);
        final int week = ldt.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
        return new WeekKey(year, week);
    }
    
    public CompoundNBT serialize() {
        final CompoundNBT tag = new CompoundNBT();
        tag.putInt("year", this.year);
        tag.putInt("week", this.week);
        return tag;
    }
    
    public static WeekKey deserialize(final CompoundNBT tag) {
        return new WeekKey(tag.getInt("year"), tag.getInt("week"));
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final WeekKey weekKey = (WeekKey)o;
        return this.year == weekKey.year && this.week == weekKey.week;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(this.year, this.week);
    }
}
