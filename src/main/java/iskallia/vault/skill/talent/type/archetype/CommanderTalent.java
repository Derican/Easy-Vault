// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.skill.talent.type.archetype;

import com.google.gson.annotations.Expose;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.skill.talent.TalentNode;
import iskallia.vault.skill.talent.TalentTree;
import iskallia.vault.util.PlayerDamageHelper;
import iskallia.vault.world.data.PlayerTalentsData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber
public class CommanderTalent extends ArchetypeTalent
{
    public static final UUID ETERNAL_DAMAGE_INCREASE_MODIFIER;
    private static final Map<UUID, PlayerDamageHelper.DamageMultiplier> multiplierMap;
    @Expose
    protected float damageTakenMultiplier;
    @Expose
    protected float damageDealtMultiplier;
    @Expose
    protected float summonEternalAdditionalCooldownReduction;
    @Expose
    protected float summonEternalDamageDealtMultiplier;
    @Expose
    protected boolean doEternalsUnaliveWhenDead;
    
    public CommanderTalent(final int cost, final float damageTakenMultiplier, final float damageDealtMultiplier, final float summonEternalAdditionalCooldownReduction, final float summonEternalDamageDealtMultiplier, final boolean doEternalsUnaliveWhenDead) {
        super(cost);
        this.damageTakenMultiplier = damageTakenMultiplier;
        this.damageDealtMultiplier = damageDealtMultiplier;
        this.summonEternalAdditionalCooldownReduction = summonEternalAdditionalCooldownReduction;
        this.summonEternalDamageDealtMultiplier = summonEternalDamageDealtMultiplier;
        this.doEternalsUnaliveWhenDead = doEternalsUnaliveWhenDead;
    }
    
    public float getDamageTakenMultiplier() {
        return this.damageTakenMultiplier;
    }
    
    public float getDamageDealtMultiplier() {
        return this.damageDealtMultiplier;
    }
    
    public float getSummonEternalAdditionalCooldownReduction() {
        return this.summonEternalAdditionalCooldownReduction;
    }
    
    public float getSummonEternalDamageDealtMultiplier() {
        return this.summonEternalDamageDealtMultiplier;
    }
    
    public boolean doEternalsUnaliveWhenDead() {
        return this.doEternalsUnaliveWhenDead;
    }
    
    @SubscribeEvent
    public static void onPlayerDamage(final LivingHurtEvent event) {
        final LivingEntity entity = event.getEntityLiving();
        final World world = entity.getCommandSenderWorld();
        if (world.isClientSide()) {
            return;
        }
        if (!ArchetypeTalent.isEnabled(world)) {
            return;
        }
        if (entity instanceof ServerPlayerEntity) {
            final ServerPlayerEntity sPlayer = (ServerPlayerEntity)entity;
            final TalentTree talents = PlayerTalentsData.get(sPlayer.getLevel()).getTalents((PlayerEntity)sPlayer);
            for (final TalentNode<?> node : talents.getLearnedNodes(CommanderTalent.class)) {
                final CommanderTalent talent = (CommanderTalent)node.getTalent();
                event.setAmount(event.getAmount() * talent.getDamageTakenMultiplier());
            }
        }
    }
    
    @SubscribeEvent
    public static void onPlayerTick(final TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END || !(event.player instanceof ServerPlayerEntity)) {
            return;
        }
        final ServerPlayerEntity sPlayer = (ServerPlayerEntity)event.player;
        final TalentTree talents = PlayerTalentsData.get(sPlayer.getLevel()).getTalents((PlayerEntity)sPlayer);
        if (talents.hasLearnedNode(ModConfigs.TALENTS.COMMANDER) && ArchetypeTalent.isEnabled((World)sPlayer.getLevel())) {
            final float damageMultiplier = talents.getNodeOf(ModConfigs.TALENTS.COMMANDER).getTalent().getDamageDealtMultiplier();
            PlayerDamageHelper.DamageMultiplier existing = CommanderTalent.multiplierMap.get(sPlayer.getUUID());
            if (existing != null) {
                if (existing.getMultiplier() == damageMultiplier) {
                    existing.refreshDuration(sPlayer.getServer());
                }
                else {
                    PlayerDamageHelper.removeMultiplier(sPlayer, existing);
                    existing = null;
                }
            }
            if (existing == null) {
                existing = PlayerDamageHelper.applyMultiplier(sPlayer, damageMultiplier, PlayerDamageHelper.Operation.STACKING_MULTIPLY, false);
                CommanderTalent.multiplierMap.put(sPlayer.getUUID(), existing);
            }
        }
        else {
            removeExistingDamageBuff(sPlayer);
        }
    }
    
    private static void removeExistingDamageBuff(final ServerPlayerEntity player) {
        final PlayerDamageHelper.DamageMultiplier existing = CommanderTalent.multiplierMap.get(player.getUUID());
        if (existing != null) {
            PlayerDamageHelper.removeMultiplier(player, existing);
        }
    }
    
    static {
        ETERNAL_DAMAGE_INCREASE_MODIFIER = UUID.fromString("f231b599-0de3-42d6-aeb1-775b0be4fae8");
        multiplierMap = new HashMap<UUID, PlayerDamageHelper.DamageMultiplier>();
    }
}
