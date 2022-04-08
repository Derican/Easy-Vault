package iskallia.vault.world.vault.time.extension;

import iskallia.vault.Vault;
import iskallia.vault.item.ItemVaultFruit;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

public class FruitExtension extends TimeExtension {
    public static final ResourceLocation ID = Vault.id("fruit");

    protected ItemVaultFruit fruit;

    public FruitExtension() {
    }

    public FruitExtension(ItemVaultFruit fruit) {
        this(ID, fruit);
    }

    public FruitExtension(ResourceLocation id, ItemVaultFruit fruit) {
        super(id, fruit.getExtraVaultTicks());
        this.fruit = fruit;
    }

    public ItemVaultFruit getFruit() {
        return this.fruit;
    }


    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = super.serializeNBT();
        nbt.putString("Fruit", getFruit().getRegistryName().toString());
        return nbt;
    }


    public void deserializeNBT(CompoundNBT nbt) {
        super.deserializeNBT(nbt);

        this


                .fruit = (ItemVaultFruit) Registry.ITEM.getOptional(new ResourceLocation(nbt.getString("Fruit"))).filter(item -> item instanceof ItemVaultFruit).map(item -> (ItemVaultFruit) item).orElseThrow(() -> {
            Vault.LOGGER.error("Fruit item <" + nbt.getString("Fruit") + "> is not defined.");
            return new IllegalStateException();
        });
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\time\extension\FruitExtension.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */