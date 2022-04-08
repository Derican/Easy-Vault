package iskallia.vault.event;

import iskallia.vault.Vault;
import iskallia.vault.attribute.EffectCloudAttribute;
import iskallia.vault.attribute.VAttribute;
import iskallia.vault.config.ScavengerHuntConfig;
import iskallia.vault.entity.EffectCloudEntity;
import iskallia.vault.entity.EternalEntity;
import iskallia.vault.init.ModAttributes;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModItems;
import iskallia.vault.init.ModSounds;
import iskallia.vault.item.ItemVaultRaffleSeal;
import iskallia.vault.item.gear.VaultGear;
import iskallia.vault.item.gear.VaultGearHelper;
import iskallia.vault.skill.talent.TalentNode;
import iskallia.vault.skill.talent.type.EffectTalent;
import iskallia.vault.skill.talent.type.SoulShardTalent;
import iskallia.vault.util.EntityHelper;
import iskallia.vault.util.MiscUtils;
import iskallia.vault.util.PlayerFilter;
import iskallia.vault.world.data.PlayerTalentsData;
import iskallia.vault.world.data.VaultRaidData;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.influence.VaultAttributeInfluence;
import iskallia.vault.world.vault.logic.objective.ScavengerHuntObjective;
import iskallia.vault.world.vault.logic.objective.VaultObjective;
import iskallia.vault.world.vault.modifier.CurseOnHitModifier;
import iskallia.vault.world.vault.modifier.DurabilityDamageModifier;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import net.minecraft.block.BlockState;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MerchantOffer;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.*;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EntityEvents {
    private static final Random rand = new Random();


    @SubscribeEvent
    public static void onTradesLoad(VillagerTradesEvent event) {
        for (ObjectIterator<List<VillagerTrades.ITrade>> objectIterator = event.getTrades().values().iterator(); objectIterator.hasNext(); ) {
            List<VillagerTrades.ITrade> trades = objectIterator.next();
            trades.removeIf(trade -> {
                try {
                    MerchantOffer offer = trade.getOffer(null, rand);
                    ItemStack output = offer.assemble();
                    Item outItem = output.getItem();
                    if (outItem instanceof net.minecraft.item.ShieldItem) {
                        return true;
                    }
                    if (outItem instanceof net.minecraft.item.TippedArrowItem && PotionUtils.getPotion(output).equals(Potions.REGENERATION)) {
                        return true;
                    }
                } catch (Exception exception) {
                }
                return false;
            });
        }

    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        PlayerEntity player = event.getPlayer();
        ModifiableAttributeInstance reachAttr = player.getAttribute((Attribute) ForgeMod.REACH_DISTANCE.get());
        if (reachAttr == null) {
            return;
        }
        BlockPos pos = event.getPos();
        BlockState state = player.getCommandSenderWorld().getBlockState(pos);
        if (!(state.getBlock() instanceof iskallia.vault.block.VaultChestBlock)) {
            return;
        }
        double reach = reachAttr.getValue();
        if (player.distanceToSqr(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D) >= reach * reach) {
            event.setCanceled(true);
        }
    }


    @SubscribeEvent
    public static void onEffectImmune(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntity().getCommandSenderWorld().isClientSide())
            return;
        if (!(event.getEntity() instanceof LivingEntity))
            return;
        LivingEntity livingEntity = (LivingEntity) event.getEntity();
        EffectTalent.getImmunities(livingEntity).forEach(livingEntity::removeEffect);
    }

    @SubscribeEvent
    public static void onPlayerFallDamage(LivingDamageEvent event) {
        if ((event.getEntity()).level.isClientSide)
            return;
        if (!(event.getEntity() instanceof PlayerEntity))
            return;
        if (event.getSource() != DamageSource.FALL)
            return;
        ServerPlayerEntity player = (ServerPlayerEntity) event.getEntity();
        float totalReduction = 0.0F;

        for (EquipmentSlotType slot : EquipmentSlotType.values()) {
            ItemStack stack = player.getItemBySlot(slot);

            totalReduction += ((Float) ModAttributes.FEATHER_FEET.get(stack)
                    .map(attribute -> (Float) attribute.getValue(stack)).orElse(Float.valueOf(0.0F))).floatValue();
        }

        event.setAmount(event.getAmount() * (1.0F - Math.min(totalReduction, 1.0F)));
        if (event.getAmount() <= 1.0E-4D) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onPlayerMobHit(LivingHurtEvent event) {
        World world = event.getEntity().getCommandSenderWorld();
        if (world.isClientSide())
            return;
        if (!(event.getSource().getEntity() instanceof LivingEntity))
            return;
        LivingEntity attacked = event.getEntityLiving();
        LivingEntity attacker = (LivingEntity) event.getSource().getEntity();


        boolean doEffectClouds = (!ActiveFlags.IS_AOE_ATTACKING.isSet() && !ActiveFlags.IS_DOT_ATTACKING.isSet() && !ActiveFlags.IS_REFLECT_ATTACKING.isSet());
        if (doEffectClouds) {
            for (EquipmentSlotType slot : EquipmentSlotType.values()) {
                ItemStack stack = attacker.getItemBySlot(slot);
                if (!(stack.getItem() instanceof VaultGear) || ((VaultGear) stack.getItem()).isIntendedForSlot(slot)) {


                    List<EffectCloudEntity.Config> configs = (List<EffectCloudEntity.Config>) ((EffectCloudAttribute) ModAttributes.EFFECT_CLOUD.getOrDefault(stack, new ArrayList())).getValue(stack);

                    for (EffectCloudEntity.Config config : configs) {
                        if (world.random.nextFloat() >= config.getChance())
                            continue;
                        EffectCloudEntity cloud = EffectCloudEntity.fromConfig(attacked.level, attacker, attacked.getX(), attacked.getY(), attacked.getZ(), config);
                        world.addFreshEntity((Entity) cloud);
                    }
                }
            }
        }
        float incDamage = VaultGearHelper.getAttributeValueOnGearSumFloat(attacker, new VAttribute[]{ModAttributes.DAMAGE_INCREASE, ModAttributes.DAMAGE_INCREASE_2});
        CreatureAttribute creatureType = attacked.getMobType();
        if (creatureType == CreatureAttribute.UNDEAD) {
            incDamage += VaultGearHelper.getAttributeValueOnGearSumFloat(attacker, new VAttribute[]{ModAttributes.DAMAGE_UNDEAD});
        } else if (creatureType == CreatureAttribute.ARTHROPOD) {
            incDamage += VaultGearHelper.getAttributeValueOnGearSumFloat(attacker, new VAttribute[]{ModAttributes.DAMAGE_SPIDERS});
        } else if (creatureType == CreatureAttribute.ILLAGER) {
            incDamage += VaultGearHelper.getAttributeValueOnGearSumFloat(attacker, new VAttribute[]{ModAttributes.DAMAGE_ILLAGERS});
        }
        event.setAmount(event.getAmount() * (1.0F + incDamage));
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onPlayerMobHitAfter(LivingHurtEvent event) {
        World world = event.getEntity().getCommandSenderWorld();
        if (world.isClientSide())
            return;
        if (!(event.getSource().getEntity() instanceof LivingEntity))
            return;
        LivingEntity attacked = event.getEntityLiving();
        LivingEntity attacker = (LivingEntity) event.getSource().getEntity();

        if (ActiveFlags.IS_DOT_ATTACKING.isSet() || ActiveFlags.IS_REFLECT_ATTACKING.isSet()) {
            return;
        }

        boolean mayChainAttack = true;
        if (attacker instanceof PlayerEntity) {
            mayChainAttack = !PlayerActiveFlags.isSet((PlayerEntity) attacker, PlayerActiveFlags.Flag.CHAINING_AOE);
        }
        if (mayChainAttack) {
            int additionalChains = VaultGearHelper.getAttributeValueOnGearSumInt(attacker, new VAttribute[]{ModAttributes.ON_HIT_CHAIN});
            if (additionalChains > 0) {
                ActiveFlags.IS_AOE_ATTACKING.runIfNotSet(() -> {
                    List<MobEntity> nearby = EntityHelper.getNearby((IWorld) world, (Vector3i) attacked.blockPosition(), 5.0F, MobEntity.class);
                    nearby.remove(attacked);
                    nearby.remove(attacker);
//                    nearby.removeIf(());
                    if (!nearby.isEmpty()) {
//                        nearby.sort(Comparator.comparing(()->{}));
                        nearby = nearby.subList(0, Math.min(additionalChains, nearby.size()));
                        float multiplier = 0.5F;
                        for (MobEntity me : nearby) {
                            me.hurt(event.getSource(), event.getAmount() * multiplier);
                            multiplier *= 0.5F;
                        }
                    }
                });
                if (attacker instanceof PlayerEntity) {
                    PlayerActiveFlags.set((PlayerEntity) attacker, PlayerActiveFlags.Flag.CHAINING_AOE, 2);
                }
            }
        }

        boolean mayAoeAttack = true;
        if (attacker instanceof PlayerEntity) {
            mayAoeAttack = !PlayerActiveFlags.isSet((PlayerEntity) attacker, PlayerActiveFlags.Flag.ATTACK_AOE);
        }
        if (mayAoeAttack) {
            int blockAoE = VaultGearHelper.getAttributeValueOnGearSumInt(attacker, new VAttribute[]{ModAttributes.ON_HIT_AOE});
            if (blockAoE > 0) {
                ActiveFlags.IS_AOE_ATTACKING.runIfNotSet(() -> {
                    List<MobEntity> nearby = EntityHelper.getNearby((IWorld) world, (Vector3i) attacked.blockPosition(), blockAoE, MobEntity.class);
                    nearby.remove(attacked);
                    nearby.remove(attacker);
//                    nearby.removeIf(());
                    if (!nearby.isEmpty()) {
                        for (MobEntity me : nearby) {
                            me.hurt(event.getSource(), event.getAmount() * 0.6F);
                        }
                    }
                });
            }
            if (attacker instanceof PlayerEntity) {
                PlayerActiveFlags.set((PlayerEntity) attacker, PlayerActiveFlags.Flag.ATTACK_AOE, 2);
            }
        }

        float stunChance = VaultGearHelper.getAttributeValueOnGearSumFloat(attacker, new VAttribute[]{ModAttributes.ON_HIT_STUN});
        if (rand.nextFloat() < stunChance) {
            attacked.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 40, 9));
            attacked.addEffect(new EffectInstance(Effects.DIG_SLOWDOWN, 40, 9));
        }
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onDamageTotem(LivingHurtEvent event) {
        World world = event.getEntity().getCommandSenderWorld();
        if (world.isClientSide() || !(world instanceof ServerWorld))
            return;
        if (!(event.getEntityLiving() instanceof PlayerEntity))
            return;
        if (event.getSource().isBypassArmor()) {
            return;
        }
        ServerWorld sWorld = (ServerWorld) world;

        ItemStack offHand = event.getEntityLiving().getOffhandItem();
        if (!(offHand.getItem() instanceof iskallia.vault.item.gear.IdolItem)) {
            return;
        }
        PlayerEntity player = (PlayerEntity) event.getEntityLiving();
        float damage = Math.max(1.0F, event.getAmount() / 5.0F);
        VaultRaid vault = VaultRaidData.get(sWorld).getAt(sWorld, player.blockPosition());
        if (vault != null) {
            for (VaultAttributeInfluence influence : vault.getInfluences().getInfluences(VaultAttributeInfluence.class)) {
                if (influence.getType() == VaultAttributeInfluence.Type.DURABILITY_DAMAGE && !influence.isMultiplicative()) {
                    damage += influence.getValue();
                }
            }
            for (DurabilityDamageModifier modifier : vault.getActiveModifiersFor(PlayerFilter.of(new PlayerEntity[]{player} ), DurabilityDamageModifier.class)) {
                damage *= modifier.getDurabilityDamageTakenMultiplier();
            }
            for (VaultAttributeInfluence influence : vault.getInfluences().getInfluences(VaultAttributeInfluence.class)) {
                if (influence.getType() == VaultAttributeInfluence.Type.DURABILITY_DAMAGE && influence.isMultiplicative()) {
                    damage += influence.getValue();
                }
            }
        }
        offHand.hurtAndBreak((int) damage, event.getEntityLiving(), entity -> entity.broadcastBreakEvent(EquipmentSlotType.OFFHAND));
    }


    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onEntityDrops(LivingDropsEvent event) {
        World world = (event.getEntity()).level;
        if (world.isClientSide() || !(world instanceof ServerWorld))
            return;
        if (world.dimension() != Vault.VAULT_KEY)
            return;
        Entity entity = event.getEntity();

        if (shouldDropDefaultInVault(entity)) {
            return;
        }

        BlockPos pos = entity.blockPosition();
        ServerWorld sWorld = (ServerWorld) world;
        VaultRaid vault = VaultRaidData.get(sWorld).getAt(sWorld, pos);
        if (vault == null) {
            event.setCanceled(true);
            return;
        }
        DamageSource killingSrc = event.getSource();
        if (!(entity instanceof iskallia.vault.entity.VaultFighterEntity) && !(entity instanceof iskallia.vault.entity.AggressiveCowEntity)) {
            event.getDrops().clear();
        }

        if (vault.getActiveObjectives().stream().anyMatch(VaultObjective::preventsNormalMonsterDrops)) {
            event.setCanceled(true);

            return;
        }
        boolean addedDrops = entity instanceof iskallia.vault.entity.AggressiveCowEntity;
        addedDrops |= addScavengerDrops(world, entity, vault, event.getDrops());
        addedDrops |= addSubFighterDrops(world, entity, vault, event.getDrops());

        Entity killerEntity = killingSrc.getEntity();
        if (killerEntity instanceof EternalEntity) {
            killerEntity = ((EternalEntity) killerEntity).getOwner().right().orElse(null);
        }
        if (killerEntity instanceof ServerPlayerEntity) {
            ServerPlayerEntity killer = (ServerPlayerEntity) killerEntity;

            if (MiscUtils.inventoryContains((IInventory) killer.inventory, stack -> stack.getItem() instanceof iskallia.vault.item.ItemShardPouch) &&
                    vault.getActiveObjectives().stream().noneMatch(objective -> objective.shouldPauseTimer(sWorld.getServer(), vault))) {
                addedDrops |= addShardDrops(world, entity, killer, vault, event.getDrops());
            }
        }


        if (!addedDrops) {
            event.setCanceled(true);
        }
    }

    private static boolean shouldDropDefaultInVault(Entity entity) {
        return (entity instanceof iskallia.vault.entity.VaultGuardianEntity || entity instanceof iskallia.vault.entity.TreasureGoblinEntity);
    }


    private static boolean addScavengerDrops(World world, Entity killed, VaultRaid vault, Collection<ItemEntity> drops) {
        Optional<ScavengerHuntObjective> objectiveOpt = vault.getActiveObjective(ScavengerHuntObjective.class);
        if (!objectiveOpt.isPresent()) {
            return false;
        }

        ScavengerHuntObjective objective = objectiveOpt.get();
        List<ScavengerHuntConfig.ItemEntry> specialDrops = ModConfigs.SCAVENGER_HUNT.generateMobDropLoot(objective.getGenerationDropFilter(), killed.getType());
        if (specialDrops.isEmpty()) {
            return false;
        }
        return ((Boolean) vault.getProperties().getBase(VaultRaid.IDENTIFIER).map(identifier -> {
//            specialDrops.forEach(());


            return Boolean.valueOf(true);
        }).orElse(Boolean.valueOf(false))).booleanValue();
    }

    private static boolean addSubFighterDrops(World world, Entity killed, VaultRaid vault, Collection<ItemEntity> drops) {
        if (!(killed instanceof iskallia.vault.entity.VaultFighterEntity)) {
            return false;
        }
        int level = ((Integer) vault.getProperties().getBase(VaultRaid.LEVEL).orElse(Integer.valueOf(0))).intValue();
        float shardChance = ModConfigs.LOOT_TABLES.getForLevel(level).getSubFighterRaffleChance();
        if (rand.nextFloat() >= shardChance) {
            return false;
        }
        String name = killed.getPersistentData().getString("VaultPlayerName");
        if (name.isEmpty()) {
            return false;
        }

        ItemStack raffleSeal = new ItemStack((IItemProvider) ModItems.CRYSTAL_SEAL_RAFFLE);
        ItemVaultRaffleSeal.setPlayerName(raffleSeal, name);
        ItemEntity itemEntity = new ItemEntity(world, killed.getX(), killed.getY(), killed.getZ(), raffleSeal);
        itemEntity.setDefaultPickUpDelay();
        drops.add(itemEntity);
        return true;
    }

    private static boolean addShardDrops(World world, Entity killed, ServerPlayerEntity killer, VaultRaid vault, Collection<ItemEntity> drops) {
        List<TalentNode<SoulShardTalent>> shardNodes = PlayerTalentsData.get(killer.getLevel()).getTalents((PlayerEntity) killer).getLearnedNodes(SoulShardTalent.class);
        if (shardNodes.isEmpty()) {
            return false;
        }
        for (TalentNode<SoulShardTalent> node : shardNodes) {
            if (!node.isLearned()) {
                return false;
            }
        }

        int shardCount = ModConfigs.SOUL_SHARD.getRandomShards(killed.getType());
        for (VaultAttributeInfluence influence : vault.getInfluences().getInfluences(VaultAttributeInfluence.class)) {
            if (influence.getType() == VaultAttributeInfluence.Type.SOUL_SHARD_DROPS && !influence.isMultiplicative()) {
                shardCount = (int) (shardCount + influence.getValue());
            }
        }
        if (shardCount <= 0) {
            return false;
        }

        float additionalSoulShardChance = 0.0F;
        for (TalentNode<SoulShardTalent> node : shardNodes) {
            additionalSoulShardChance += ((SoulShardTalent) node.getTalent()).getAdditionalSoulShardChance();
        }
        float shShardCount = shardCount * (1.0F + additionalSoulShardChance);
        shardCount = MathHelper.floor(shShardCount);
        float decimal = shShardCount - shardCount;
        if (rand.nextFloat() < decimal) {
            shardCount++;
        }
        for (VaultAttributeInfluence influence : vault.getInfluences().getInfluences(VaultAttributeInfluence.class)) {
            if (influence.getType() == VaultAttributeInfluence.Type.SOUL_SHARD_DROPS && influence.isMultiplicative()) {
                shardCount = (int) (shardCount * influence.getValue());
            }
        }

        ItemStack shards = new ItemStack((IItemProvider) ModItems.SOUL_SHARD, shardCount);
        ItemEntity itemEntity = new ItemEntity(world, killed.getX(), killed.getY(), killed.getZ(), shards);
        itemEntity.setDefaultPickUpDelay();
        drops.add(itemEntity);
        return true;
    }

    @SubscribeEvent
    public static void onEntitySpawn(LivingSpawnEvent.CheckSpawn event) {
        if (event.getEntity().getCommandSenderWorld().dimension() == Vault.VAULT_KEY && !event.isSpawner()) {
            event.setResult(Event.Result.DENY);
        }
    }


    @SubscribeEvent
    public static void onDamageArmorHit(LivingDamageEvent event) {
        LivingEntity damaged = event.getEntityLiving();
        if (!(damaged instanceof PlayerEntity) || damaged.getCommandSenderWorld().isClientSide()) {
            return;
        }
        PlayerEntity player = (PlayerEntity) damaged;

        Entity trueSrc = event.getSource().getEntity();
        if (trueSrc instanceof LivingEntity) {
            double chance = ((LivingEntity) trueSrc).getAttributeValue(ModAttributes.BREAK_ARMOR_CHANCE);
            while (chance > 0.0D &&
                    rand.nextFloat() <= chance) {


                chance--;

                player.inventory.hurtArmor(event.getSource(), 4.0F);
            }
        }
    }

    @SubscribeEvent
    public static void onCurseOnHit(LivingDamageEvent event) {
        if (!(event.getSource().getEntity() instanceof LivingEntity)) {
            return;
        }
        LivingEntity damaged = event.getEntityLiving();
        if (!(damaged instanceof ServerPlayerEntity)) {
            return;
        }
        ServerPlayerEntity sPlayer = (ServerPlayerEntity) damaged;
        ServerWorld sWorld = sPlayer.getLevel();

        VaultRaid vault = VaultRaidData.get(sWorld).getAt(sWorld, sPlayer.blockPosition());
        if (vault != null) {
            vault.getActiveModifiersFor(PlayerFilter.any(), CurseOnHitModifier.class).forEach(modifier -> modifier.applyCurse(sPlayer));
        }
    }


    @SubscribeEvent
    public static void onVaultGuardianDamage(LivingDamageEvent event) {
        LivingEntity entityLiving = event.getEntityLiving();

        if (entityLiving.level.isClientSide)
            return;
        if (entityLiving instanceof iskallia.vault.entity.VaultGuardianEntity) {
            Entity trueSource = event.getSource().getEntity();
            if (trueSource instanceof LivingEntity) {
                LivingEntity attacker = (LivingEntity) trueSource;
                attacker.hurt(DamageSource.thorns((Entity) entityLiving), event.getAmount() * 0.2F);
            }
        }
    }

    @SubscribeEvent
    public static void onLivingHurtCrit(LivingHurtEvent event) {
        if (!(event.getSource().getEntity() instanceof LivingEntity))
            return;
        LivingEntity source = (LivingEntity) event.getSource().getEntity();
        if (source.level.isClientSide)
            return;
        if (source.getAttributes().hasAttribute(ModAttributes.CRIT_CHANCE)) {
            double chance = source.getAttributeValue(ModAttributes.CRIT_CHANCE);

            if (source.getAttributes().hasAttribute(ModAttributes.CRIT_MULTIPLIER)) {
                double multiplier = source.getAttributeValue(ModAttributes.CRIT_MULTIPLIER);

                if (source.level.random.nextDouble() < chance) {
                    source.level.playSound(null, source.getX(), source.getY(), source.getZ(), SoundEvents.PLAYER_ATTACK_CRIT, source.getSoundSource(), 1.0F, 1.0F);
                    event.setAmount((float) (event.getAmount() * multiplier));
                }
            }
        }
    }

    @SubscribeEvent
    public static void onLivingHurtTp(LivingHurtEvent event) {
        if ((event.getEntityLiving()).level.isClientSide)
            return;
        boolean direct = (event.getSource().getDirectEntity() == event.getSource().getEntity());

        if (direct && event.getEntityLiving().getAttributes().hasAttribute(ModAttributes.TP_CHANCE)) {
            double chance = event.getEntityLiving().getAttributeValue(ModAttributes.TP_CHANCE);

            if (event.getEntityLiving().getAttributes().hasAttribute(ModAttributes.TP_RANGE)) {
                double range = event.getEntityLiving().getAttributeValue(ModAttributes.TP_RANGE);

                if ((event.getEntityLiving()).level.random.nextDouble() < chance) {
                    for (int i = 0; i < 64; i++) {
                        if (teleportRandomly(event.getEntityLiving(), range)) {
                            (event.getEntityLiving()).level.playSound(null,
                                    (event.getEntityLiving()).xo,
                                    (event.getEntityLiving()).yo,
                                    (event.getEntityLiving()).zo, ModSounds.BOSS_TP_SFX, event
                                            .getEntityLiving().getSoundSource(), 1.0F, 1.0F);
                            event.setCanceled(true);
                            return;
                        }
                    }
                }
            }
        } else if (!direct && event.getEntityLiving().getAttributes().hasAttribute(ModAttributes.TP_INDIRECT_CHANCE)) {
            double chance = event.getEntityLiving().getAttributeValue(ModAttributes.TP_INDIRECT_CHANCE);

            if (event.getEntityLiving().getAttributes().hasAttribute(ModAttributes.TP_RANGE)) {
                double range = event.getEntityLiving().getAttributeValue(ModAttributes.TP_RANGE);

                if ((event.getEntityLiving()).level.random.nextDouble() < chance) {
                    for (int i = 0; i < 64; i++) {
                        if (teleportRandomly(event.getEntityLiving(), range)) {
                            (event.getEntityLiving()).level.playSound(null,
                                    (event.getEntityLiving()).xo,
                                    (event.getEntityLiving()).yo,
                                    (event.getEntityLiving()).zo, ModSounds.BOSS_TP_SFX, event
                                            .getEntityLiving().getSoundSource(), 1.0F, 1.0F);
                            event.setCanceled(true);
                            return;
                        }
                    }
                }
            }
        }
    }

    private static boolean teleportRandomly(LivingEntity entity, double range) {
        if (!entity.level.isClientSide() && entity.isAlive()) {
            double d0 = entity.getX() + (entity.level.random.nextDouble() - 0.5D) * range * 2.0D;
            double d1 = entity.getY() + entity.level.random.nextInt((int) (range * 2.0D)) - range;
            double d2 = entity.getZ() + (entity.level.random.nextDouble() - 0.5D) * range * 2.0D;
            return entity.randomTeleport(d0, d1, d2, true);
        }

        return false;
    }

    @SubscribeEvent
    public static void onEntityDestroy(LivingDestroyBlockEvent event) {
        if (event.getState().getBlock() instanceof iskallia.vault.block.VaultDoorBlock)
            event.setCanceled(true);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\event\EntityEvents.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */