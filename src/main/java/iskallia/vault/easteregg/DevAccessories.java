package iskallia.vault.easteregg;

import iskallia.vault.entity.FighterEntity;
import iskallia.vault.init.ModItems;
import iskallia.vault.init.ModModels;
import iskallia.vault.item.gear.VaultGear;
import iskallia.vault.util.EntityHelper;
import iskallia.vault.util.GearItemStackBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.server.ServerBossInfo;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class DevAccessories {
    @SubscribeEvent
    public static void onVaultFighterBossKilled(final LivingDeathEvent event) {
        final LivingEntity entityLiving = event.getEntityLiving();
        if (!entityLiving.isEffectiveAi()) {
            return;
        }
        if (entityLiving instanceof FighterEntity) {
            final FighterEntity fighter = (FighterEntity) entityLiving;
            final Entity trueSource = event.getSource().getEntity();
            if (fighter.getTags().contains("vault_boss") && trueSource instanceof PlayerEntity) {
                onDevBossKill(fighter, (ServerPlayerEntity) trueSource);
            }
        }
    }

    public static void onDevBossKill(final FighterEntity boss, final ServerPlayerEntity player) {
        final ServerBossInfo bossInfo = boss.bossInfo;
        if (bossInfo == null) {
            return;
        }
        final ServerWorld world = (ServerWorld) boss.getCommandSenderWorld();
        if (world.getRandom().nextDouble() > 0.4) {
            return;
        }
        final String bossName = bossInfo.getName().getString();
        if (bossName.equalsIgnoreCase("iskall85")) {
            final ItemStack itemStack = new GearItemStackBuilder(ModItems.HELMET).setGearRarity(VaultGear.Rarity.UNIQUE).setColor(-5384139).setSpecialModelId(ModModels.SpecialGearModel.ISKALL_HOLOLENS.getId()).build();
            EntityHelper.giveItem(player, itemStack);
        } else if (!bossName.equalsIgnoreCase("iGoodie")) {
            if (bossName.equalsIgnoreCase("Douwsky")) {
                final ItemStack itemStack = new GearItemStackBuilder(ModItems.SWORD).setGearRarity(VaultGear.Rarity.UNIQUE).setSpecialModelId(ModModels.SpecialSwordModel.JANITORS_BROOM.getId()).build();
                EntityHelper.giveItem(player, itemStack);
            }
        }
    }
}
