package iskallia.vault.world.vault.time.extension;

import iskallia.vault.Vault;
import iskallia.vault.item.ItemVaultFruit;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

public class FruitExtension extends TimeExtension {
    public static final ResourceLocation ID;
    protected ItemVaultFruit fruit;

    public FruitExtension() {
    }

    public FruitExtension(final ItemVaultFruit fruit) {
        this(FruitExtension.ID, fruit);
    }

    public FruitExtension(final ResourceLocation id, final ItemVaultFruit fruit) {
        super(id, fruit.getExtraVaultTicks());
        this.fruit = fruit;
    }

    public ItemVaultFruit getFruit() {
        return this.fruit;
    }

    @Override
    public CompoundNBT serializeNBT() {
        final CompoundNBT nbt = super.serializeNBT();
        nbt.putString("Fruit", this.getFruit().getRegistryName().toString());
        return nbt;
    }

    @Override
    public void deserializeNBT(final CompoundNBT nbt) {
        super.deserializeNBT(nbt);
        this.fruit = (ItemVaultFruit) Registry.ITEM.getOptional(new ResourceLocation(nbt.getString("Fruit"))).filter(item -> item instanceof ItemVaultFruit).map(item -> item).orElseThrow(() -> {
            Vault.LOGGER.error("Fruit item <" + nbt.getString("Fruit") + "> is not defined.");
            return new IllegalStateException();
        });
    }

    static {
        ID = Vault.id("fruit");
    }
}
