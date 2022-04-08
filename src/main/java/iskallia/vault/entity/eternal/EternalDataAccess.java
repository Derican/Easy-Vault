package iskallia.vault.entity.eternal;

import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Random;
import java.util.UUID;


public interface EternalDataAccess {
    UUID getId();

    long getSeed();

    int getLevel();

    int getMaxLevel();

    String getName();

    boolean isAlive();

    boolean isAncient();

    Map<EquipmentSlotType, ItemStack> getEquipment();

    Map<Attribute, Float> getEntityAttributes();

    @Nullable
    String getAbilityName();

    default Random getSeededRand() {
        long seed = getSeed();
        seed ^= getId().getMostSignificantBits();
        seed ^= getId().getLeastSignificantBits();
        return new Random(seed);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\entity\eternal\EternalDataAccess.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */