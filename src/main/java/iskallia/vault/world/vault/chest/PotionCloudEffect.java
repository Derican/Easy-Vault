package iskallia.vault.world.vault.chest;

import com.google.gson.annotations.Expose;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.player.VaultPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PotionEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PotionCloudEffect extends VaultChestEffect {
    public PotionCloudEffect(String name, Potion... potions) {
        super(name);

        this


                .potions = (List<String>) Arrays.<Potion>stream(potions).map(ForgeRegistryEntry::getRegistryName).filter(Objects::nonNull).map(ResourceLocation::toString).collect(Collectors.toList());
    }

    @Expose
    public List<String> potions;

    public List<Potion> getPotions() {
        return (List<Potion>) this.potions.stream()
                .map(s -> (Potion) Registry.POTION.getOptional(new ResourceLocation(s)).orElse(null))
                .filter(Objects::nonNull).collect(Collectors.toList());
    }


    public void apply(VaultRaid vault, VaultPlayer player, ServerWorld world) {
        player.runIfPresent(world.getServer(), playerEntity -> {
            PotionEntity entity = new PotionEntity((World) world, (LivingEntity) playerEntity);
            ItemStack stack = new ItemStack((IItemProvider) Items.LINGERING_POTION);
            getPotions().forEach(());
            entity.setItem(stack);
            entity.shootFromRotation((Entity) playerEntity, playerEntity.xRot, playerEntity.yRot, -20.0F, 0.5F, 1.0F);
            world.addFreshEntity((Entity) entity);
        });
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\chest\PotionCloudEffect.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */