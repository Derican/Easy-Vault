package iskallia.vault.util;

public class Counter {
    private int value;

    public Counter() {
        this(0);
    }

    public Counter(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public void decrement() {
        this.value--;
    }

    public void increment() {
        this.value++;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vaul\\util\Counter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */