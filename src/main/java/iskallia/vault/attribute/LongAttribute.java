// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.attribute;

import net.minecraft.nbt.CompoundNBT;

import java.util.Random;

public class LongAttribute extends NumberAttribute<Long>
{
    public LongAttribute() {
    }
    
    public LongAttribute(final VAttribute.Modifier<Long> modifier) {
        super(modifier);
    }
    
    @Override
    public void write(final CompoundNBT nbt) {
        nbt.putLong("BaseValue", (long)this.getBaseValue());
    }
    
    @Override
    public void read(final CompoundNBT nbt) {
        this.setBaseValue(nbt.getLong("BaseValue"));
    }
    
    public static Generator generator() {
        return new Generator();
    }
    
    public static Generator.Operator of(final Type type) {
        return new Generator.Operator(type);
    }
    
    public static class Generator extends NumberAttribute.Generator<Long, Generator.Operator>
    {
        @Override
        public Long getDefaultValue(final Random random) {
            return 0L;
        }
        
        public static Operator of(final Type type) {
            return new Operator(type);
        }
        
        public static class Operator extends NumberAttribute.Generator.Operator<Long>
        {
            public Operator(final Type type) {
                super(type);
            }
            
            @Override
            public Long apply(final Long value, final Long modifier) {
                try{
                if (this.getType() == Type.SET) {
                    return modifier;
                }
                if (this.getType() == Type.ADD) {
                    return value + modifier;
                }
                if (this.getType() == Type.MULTIPLY) {
                    return value * modifier;
                }}catch (Throwable e){

                }
                return value;
            }
        }
    }
}
