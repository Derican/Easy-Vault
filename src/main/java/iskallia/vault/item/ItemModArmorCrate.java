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

public class ItemModArmorCrate
        extends BasicItem {
    private final Supplier<List<ModModels.SpecialGearModel.SpecialGearModelSet>> modelSetSupplier;

    public ItemModArmorCrate(ResourceLocation id, Item.Properties properties, Supplier<List<ModModels.SpecialGearModel.SpecialGearModelSet>> modelSetSupplier) {
        super(id, properties);
        this.modelSetSupplier = modelSetSupplier;
    }


    @Nonnull
    public ActionResult<ItemStack> use(World world, @Nonnull PlayerEntity player, @Nonnull Hand hand) {
        if (!world.isClientSide) {
            ItemStack itemStack;
            List<ModModels.SpecialGearModel.SpecialGearModelSet> modelSets = this.modelSetSupplier.get();
            ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
            ItemStack heldStack = player.getMainHandItem();

            int modelSetIndex = world.getRandom().nextInt(modelSets.size());
            ModModels.SpecialGearModel.SpecialGearModelSet modelSet = modelSets.get(modelSetIndex);

            int slot = world.getRandom().nextInt(4);


            if (slot == 0) {
                itemStack = new ItemStack((IItemProvider) ModItems.HELMET);
                configureItemStack(itemStack, modelSet.head.getId());
                AdvancementHelper.grantCriterion(serverPlayer, Vault.id("armors/" + getRegistryName().getPath() + "/set"), "looted_helmet");
            } else if (slot == 1) {
                itemStack = new ItemStack((IItemProvider) ModItems.CHESTPLATE);
                configureItemStack(itemStack, modelSet.chestplate.getId());
                AdvancementHelper.grantCriterion(serverPlayer, Vault.id("armors/" + getRegistryName().getPath() + "/set"), "looted_chestplate");
            } else if (slot == 2) {
                itemStack = new ItemStack((IItemProvider) ModItems.LEGGINGS);
                configureItemStack(itemStack, modelSet.leggings.getId());
                AdvancementHelper.grantCriterion(serverPlayer, Vault.id("armors/" + getRegistryName().getPath() + "/set"), "looted_leggings");
            } else {

                itemStack = new ItemStack((IItemProvider) ModItems.BOOTS);
                configureItemStack(itemStack, modelSet.boots.getId());
                AdvancementHelper.grantCriterion(serverPlayer, Vault.id("armors/" + getRegistryName().getPath() + "/set"), "looted_boots");
            }

            EntityHelper.giveItem(player, itemStack);
            ItemRelicBoosterPack.successEffects(world, player.position());
            heldStack.shrink(1);
        }

        return super.use(world, player, hand);
    }

    private void configureItemStack(ItemStack gearStack, int model) {
        ModAttributes.GEAR_STATE.create(gearStack, VaultGear.State.IDENTIFIED);
        gearStack.getOrCreateTag().remove("RollTicks");
        gearStack.getOrCreateTag().remove("LastModelHit");
        ModAttributes.GEAR_RARITY.create(gearStack, VaultGear.Rarity.UNIQUE);
        ModAttributes.GEAR_SET.create(gearStack, VaultGear.Set.NONE);
        ModAttributes.GEAR_SPECIAL_MODEL.create(gearStack, Integer.valueOf(model));
        ModAttributes.GEAR_COLOR.create(gearStack, Integer.valueOf(-1));
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\item\ItemModArmorCrate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */