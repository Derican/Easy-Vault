// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.util;

import java.util.TreeMap;

public class RomanNumber
{
    private static final TreeMap<Integer, String> LITERALS;
    
    public static String toRoman(final int number) {
        if (number == 0) {
            return "";
        }
        final int literal = RomanNumber.LITERALS.floorKey(number);
        if (number == literal) {
            return RomanNumber.LITERALS.get(number);
        }
        return RomanNumber.LITERALS.get(literal) + toRoman(number - literal);
    }
    
    static {
        (LITERALS = new TreeMap<Integer, String>()).put(1000, "M");
        RomanNumber.LITERALS.put(900, "CM");
        RomanNumber.LITERALS.put(500, "D");
        RomanNumber.LITERALS.put(400, "CD");
        RomanNumber.LITERALS.put(100, "C");
        RomanNumber.LITERALS.put(90, "XC");
        RomanNumber.LITERALS.put(50, "L");
        RomanNumber.LITERALS.put(40, "XL");
        RomanNumber.LITERALS.put(10, "X");
        RomanNumber.LITERALS.put(9, "IX");
        RomanNumber.LITERALS.put(5, "V");
        RomanNumber.LITERALS.put(4, "IV");
        RomanNumber.LITERALS.put(1, "I");
    }
}
