// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.util.nbt;

public class UnserializableClassException extends Exception
{
    private final Class<?> clazz;
    
    public UnserializableClassException(final Class<?> clazz) {
        this.clazz = clazz;
    }
    
    public Class<?> getOffendingClass() {
        return this.clazz;
    }
}
