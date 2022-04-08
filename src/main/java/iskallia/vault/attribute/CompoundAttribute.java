package iskallia.vault.attribute;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.function.Function;
import java.util.function.Supplier;

public class CompoundAttribute<T extends INBTSerializable<CompoundNBT>>
        extends VAttribute.Instance<T> {
    protected Function<CompoundNBT, T> read;

    public CompoundAttribute(Function<CompoundNBT, T> read) {
        this.read = read;
    }

    public static <T extends INBTSerializable<CompoundNBT>> CompoundAttribute<T> of(Supplier<T> supplier) {
        return new CompoundAttribute<T>(nbt -> {
            T iNBTSerializable = supplier.get();
            iNBTSerializable.deserializeNBT(nbt);
            return iNBTSerializable;
        });
    }


    public void write(CompoundNBT nbt) {
        if (getBaseValue() != null) {
            nbt.put("BaseValue", ((INBTSerializable) getBaseValue()).serializeNBT());
        }
    }


    public void read(CompoundNBT nbt) {
        if (nbt.contains("BaseValue", 10))
            setBaseValue(this.read.apply(nbt.getCompound("BaseValue")));
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\attribute\CompoundAttribute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */