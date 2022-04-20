package iskallia.vault.item;

import iskallia.vault.Vault;
import iskallia.vault.init.ModAttributes;
import iskallia.vault.init.ModItems;
import iskallia.vault.init.ModModels;
import iskallia.vault.item.gear.VaultGear;
import iskallia.vault.util.AdvancementHelper;
import iskallia.vault.util.EntityHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Supplier;

public class ItemModArmorCrate extends BasicItem {
    private final Supplier<List<ModModels.SpecialGearModel.SpecialGearModelSet>> modelSetSupplier;

    public ItemModArmorCrate(final ResourceLocation id, final Item.Properties properties, final Supplier<List<ModModels.SpecialGearModel.SpecialGearModelSet>> modelSetSupplier) {
        super(id, properties);
        this.modelSetSupplier = modelSetSupplier;
    }

    @Nonnull
    public ActionResult<ItemStack> use(final World world, @Nonnull final PlayerEntity player, @Nonnull final Hand hand) {
        if (!world.isClientSide) {
            final List<ModModels.SpecialGearModel.SpecialGearModelSet> modelSets = this.modelSetSupplier.get();
            final ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
            final ItemStack heldStack = player.getMainHandItem();
            final int modelSetIndex = world.getRandom().nextInt(modelSets.size());
            final ModModels.SpecialGearModel.SpecialGearModelSet modelSet = modelSets.get(modelSetIndex);
            final int slot = world.getRandom().nextInt(4);
            ItemStack itemStack;
            if (slot == 0) {
                itemStack = new ItemStack((IItemProvider) ModItems.HELMET);
                this.configureItemStack(itemStack, modelSet.head.getId());
                AdvancementHelper.grantCriterion(serverPlayer, Vault.id("armors/" + this.getRegistryName().getPath() + "/set"), "looted_helmet");
            } else if (slot == 1) {
                itemStack = new ItemStack((IItemProvider) ModItems.CHESTPLATE);
                this.configureItemStack(itemStack, modelSet.chestplate.getId());
                AdvancementHelper.grantCriterion(serverPlayer, Vault.id("armors/" + this.getRegistryName().getPath() + "/set"), "looted_chestplate");
            } else if (slot == 2) {
                itemStack = new ItemStack((IItemProvider) ModItems.LEGGINGS);
                this.configureItemStack(itemStack, modelSet.leggings.getId());
                AdvancementHelper.grantCriterion(serverPlayer, Vault.id("armors/" + this.getRegistryName().getPath() + "/set"), "looted_leggings");
            } else {
                itemStack = new ItemStack((IItemProvider) ModItems.BOOTS);
                this.configureItemStack(itemStack, modelSet.boots.getId());
                AdvancementHelper.grantCriterion(serverPlayer, Vault.id("armors/" + this.getRegistryName().getPath() + "/set"), "looted_boots");
            }
            EntityHelper.giveItem(player, itemStack);
            ItemRelicBoosterPack.successEffects(world, player.position());
            heldStack.shrink(1);
        }
        return (ActionResult<ItemStack>) super.use(world, player, hand);
    }

    private void configureItemStack(final ItemStack gearStack, final int model) {
        ModAttributes.GEAR_STATE.create(gearStack, VaultGear.State.IDENTIFIED);
        gearStack.getOrCreateTag().remove("RollTicks");
        gearStack.getOrCreateTag().remove("LastModelHit");
        ModAttributes.GEAR_RARITY.create(gearStack, VaultGear.Rarity.UNIQUE);
        ModAttributes.GEAR_SET.create(gearStack, VaultGear.Set.NONE);
        ModAttributes.GEAR_SPECIAL_MODEL.create(gearStack, model);
        ModAttributes.GEAR_COLOR.create(gearStack, -1);
    }
}
