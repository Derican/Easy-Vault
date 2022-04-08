package iskallia.vault.util;

import java.util.TreeMap;

public class RomanNumber {
    private static final TreeMap<Integer, String> LITERALS = new TreeMap<>();

    public static String toRoman(int number) {
        if (number == 0) {
            return "";
        }
        int literal = ((Integer) LITERALS.floorKey(Integer.valueOf(number))).intValue();
        if (number == literal) {
            return LITERALS.get(Integer.valueOf(number));
        }
        return (String) LITERALS.get(Integer.valueOf(literal)) + toRoman(number - literal);
    }

    static {
        LITERALS.put(Integer.valueOf(1000), "M");
        LITERALS.put(Integer.valueOf(900), "CM");
        LITERALS.put(Integer.valueOf(500), "D");
        LITERALS.put(Integer.valueOf(400), "CD");
        LITERALS.put(Integer.valueOf(100), "C");
        LITERALS.put(Integer.valueOf(90), "XC");
        LITERALS.put(Integer.valueOf(50), "L");
        LITERALS.put(Integer.valueOf(40), "XL");
        LITERALS.put(Integer.valueOf(10), "X");
        LITERALS.put(Integer.valueOf(9), "IX");
        LITERALS.put(Integer.valueOf(5), "V");
        LITERALS.put(Integer.valueOf(4), "IV");
        LITERALS.put(Integer.valueOf(1), "I");
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vaul\\util\RomanNumber.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */