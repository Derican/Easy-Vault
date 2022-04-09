// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.util;

public class Counter
{
    private int value;
    
    public Counter() {
        this(0);
    }
    
    public Counter(final int value) {
        this.value = value;
    }
    
    public int getValue() {
        return this.value;
    }
    
    public void decrement() {
        --this.value;
    }
    
    public void increment() {
        ++this.value;
    }
}
