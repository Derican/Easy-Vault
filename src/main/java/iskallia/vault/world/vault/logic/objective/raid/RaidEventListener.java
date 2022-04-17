// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.world.vault.logic.objective.raid;

import iskallia.vault.block.VaultRaidControllerBlock;
import iskallia.vault.world.data.VaultRaidData;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.gen.piece.VaultRaidRoom;
import iskallia.vault.world.vault.logic.objective.raid.modifier.DamageTakenModifier;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.ZombieEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class RaidEventListener
{
    @SubscribeEvent
    public static void onPlayerDamage(final LivingHurtEvent event) {
        final LivingEntity entity = event.getEntityLiving();
        final World world = entity.getCommandSenderWorld();
        if (world.isClientSide()) {
            return;
        }
        if (entity instanceof ServerPlayerEntity) {
            final ServerPlayerEntity sPlayer = (ServerPlayerEntity)entity;
            final VaultRaid vault = VaultRaidData.get(sPlayer.getLevel()).getAt(sPlayer.getLevel(), sPlayer.blockPosition());
            if (vault != null) {
                final ActiveRaid raid = vault.getActiveRaid();
                if (raid != null && raid.isPlayerInRaid((PlayerEntity)sPlayer)) {
                    float dmg = event.getAmount();
                    dmg *= (float)(1.0 + vault.getActiveObjective(RaidChallengeObjective.class).map(raidObjective -> raidObjective.getModifiersOfType(DamageTakenModifier.class).values().stream().mapToDouble(Float::doubleValue).sum()).orElse(0.0));
                    event.setAmount(dmg);
                }
            }
        }
    }
    
    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onDeath(final LivingDeathEvent event) {
        final LivingEntity died = event.getEntityLiving();
        final World world = died.getCommandSenderWorld();
        final BlockPos at = died.blockPosition();
        final ActiveRaid raid = getRaidAt((IWorld)world, at);
        if (raid == null) {
            return;
        }
        raid.getActiveEntities().remove(died.getUUID());
        if (raid.getActiveEntities().isEmpty() && raid.hasNextWave()) {
            raid.setStartDelay(100);
        }
    }
    
    @SubscribeEvent
    public static void onSpawn(final ZombieEvent.SummonAidEvent event) {
        if (isInLockedRaidRoom((IWorld)event.getWorld(), event.getSummoner().blockPosition())) {
            event.setResult(Event.Result.DENY);
        }
    }
    
    @SubscribeEvent
    public static void onBreak(final BlockEvent.BreakEvent event) {
        if (isInLockedRaidRoom(event.getWorld(), event.getPos())) {
            event.setCanceled(true);
        }
    }
    
    @SubscribeEvent
    public static void onPlace(final BlockEvent.EntityPlaceEvent event) {
        if (isInLockedRaidRoom(event.getWorld(), event.getPos())) {
            event.setCanceled(true);
        }
    }
    
    @SubscribeEvent
    public static void onFluidPlace(final BlockEvent.FluidPlaceBlockEvent event) {
        if (isInLockedRaidRoom(event.getWorld(), event.getPos())) {
            event.setCanceled(true);
        }
    }
    
    @SubscribeEvent
    public static void onInteract(final PlayerInteractEvent event) {
        if (event instanceof PlayerInteractEvent.RightClickBlock) {
            final BlockState interacted = event.getWorld().getBlockState(event.getPos());
            if (interacted.getBlock() instanceof VaultRaidControllerBlock) {
                return;
            }
        }
        final ItemStack interacted2 = event.getItemStack();
        if (event instanceof PlayerInteractEvent.RightClickItem) {
            final UseAction action = interacted2.getUseAnimation();
            if (action == UseAction.EAT || action == UseAction.DRINK) {
                return;
            }
            if (isWhitelistedItem(interacted2)) {
                return;
            }
        }
        if (isInLockedRaidRoom((IWorld)event.getWorld(), event.getPos())) {
            event.setCanceled(true);
            event.setCancellationResult(ActionResultType.FAIL);
        }
    }
    
    private static boolean isWhitelistedItem(final ItemStack interacted) {
        if (interacted.isEmpty()) {
            return false;
        }
        final ResourceLocation key = interacted.getItem().getRegistryName();
        return (key.getNamespace().equals("dankstorage") && key.getPath().startsWith("dank_")) || (key.toString().equals("minecraft:netherite_pickaxe") || key.toString().equals("quark:flamerang")) || key.getNamespace().equals("simplybackpacks");
    }
    
    private static boolean isInLockedRaidRoom(final IWorld world, final BlockPos pos) {
        if (world.isClientSide() || !(world instanceof ServerWorld)) {
            return false;
        }
        final ServerWorld sWorld = (ServerWorld)world;
        final VaultRaid vault = VaultRaidData.get(sWorld).getAt(sWorld, pos);
        if (vault == null) {
            return false;
        }
        final VaultRaidRoom room = (VaultRaidRoom) vault.getGenerator().getPiecesAt(pos, VaultRaidRoom.class).stream().findFirst().orElse(null);
        return room != null && !room.isRaidFinished();
    }
    
    private static ActiveRaid getRaidAt(final IWorld world, final BlockPos pos) {
        if (world.isClientSide() || !(world instanceof ServerWorld)) {
            return null;
        }
        final ServerWorld sWorld = (ServerWorld)world;
        final VaultRaid vault = VaultRaidData.get(sWorld).getAt(sWorld, pos);
        if (vault == null) {
            return null;
        }
        return vault.getActiveRaid();
    }
}
