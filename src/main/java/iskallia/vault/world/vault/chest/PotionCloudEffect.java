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
import net.minecraft.potion.PotionUtils;
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
    @Expose
    public List<String> potions;

    public PotionCloudEffect(final String name, final Potion... potions) {
        super(name);
        this.potions = Arrays.stream(potions).map(ForgeRegistryEntry::getRegistryName).filter(Objects::nonNull).map(ResourceLocation::toString).collect(Collectors.toList());
    }

    public List<Potion> getPotions() {
        return this.potions.stream().map(s -> Registry.POTION.getOptional(new ResourceLocation(s)).orElse(null)).filter(Objects::nonNull).collect(Collectors.toList());
    }

    @Override
    public void apply(final VaultRaid vault, final VaultPlayer player, final ServerWorld world) {
        player.runIfPresent(world.getServer(), playerEntity -> {
            final PotionEntity entity = new PotionEntity((World) world, (LivingEntity) playerEntity);
            final ItemStack stack = new ItemStack((IItemProvider) Items.LINGERING_POTION);
            this.getPotions().forEach(potion -> PotionUtils.setPotion(stack, potion));
            entity.setItem(stack);
            entity.shootFromRotation((Entity) playerEntity, playerEntity.xRot, playerEntity.yRot, -20.0f, 0.5f, 1.0f);
            world.addFreshEntity((Entity) entity);
        });
    }
}
