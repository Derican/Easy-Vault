package iskallia.vault.world.vault.logic.objective.raid;

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
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;


@EventBusSubscriber
public class RaidEventListener {
    @SubscribeEvent
    public static void onPlayerDamage(LivingHurtEvent event) {
        LivingEntity entity = event.getEntityLiving();
        World world = entity.getCommandSenderWorld();
        if (world.isClientSide()) {
            return;
        }
        if (entity instanceof ServerPlayerEntity) {
            ServerPlayerEntity sPlayer = (ServerPlayerEntity) entity;
            VaultRaid vault = VaultRaidData.get(sPlayer.getLevel()).getAt(sPlayer.getLevel(), sPlayer.blockPosition());
            if (vault != null) {
                ActiveRaid raid = vault.getActiveRaid();
                if (raid != null && raid.isPlayerInRaid((PlayerEntity) sPlayer)) {
                    float dmg = event.getAmount();


                    dmg = (float) (dmg * (1.0D + ((Double) vault.getActiveObjective(RaidChallengeObjective.class).map(raidObjective -> Double.valueOf(raidObjective.<DamageTakenModifier>getModifiersOfType(DamageTakenModifier.class).values().stream().mapToDouble(Float::doubleValue).sum())).orElse(Double.valueOf(0.0D))).doubleValue()));

                    event.setAmount(dmg);
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onDeath(LivingDeathEvent event) {
        LivingEntity died = event.getEntityLiving();

        World world = died.getCommandSenderWorld();
        BlockPos at = died.blockPosition();
        ActiveRaid raid = getRaidAt((IWorld) world, at);
        if (raid == null) {
            return;
        }
        raid.getActiveEntities().remove(died.getUUID());
        if (raid.getActiveEntities().isEmpty() && raid.hasNextWave()) {
            raid.setStartDelay(100);
        }
    }

    @SubscribeEvent
    public static void onSpawn(ZombieEvent.SummonAidEvent event) {
        if (isInLockedRaidRoom((IWorld) event.getWorld(), event.getSummoner().blockPosition())) {
            event.setResult(Event.Result.DENY);
        }
    }

    @SubscribeEvent
    public static void onBreak(BlockEvent.BreakEvent event) {
        if (isInLockedRaidRoom(event.getWorld(), event.getPos())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onPlace(BlockEvent.EntityPlaceEvent event) {
        if (isInLockedRaidRoom(event.getWorld(), event.getPos())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onFluidPlace(BlockEvent.FluidPlaceBlockEvent event) {
        if (isInLockedRaidRoom(event.getWorld(), event.getPos())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onInteract(PlayerInteractEvent event) {
        if (event instanceof PlayerInteractEvent.RightClickBlock) {
            BlockState blockState = event.getWorld().getBlockState(event.getPos());
            if (blockState.getBlock() instanceof iskallia.vault.block.VaultRaidControllerBlock) {
                return;
            }
        }
        ItemStack interacted = event.getItemStack();
        if (event instanceof PlayerInteractEvent.RightClickItem) {
            UseAction action = interacted.getUseAnimation();
            if (action == UseAction.EAT || action == UseAction.DRINK) {
                return;
            }
            if (isWhitelistedItem(interacted)) {
                return;
            }
        }
        if (isInLockedRaidRoom((IWorld) event.getWorld(), event.getPos())) {
            event.setCanceled(true);
            event.setCancellationResult(ActionResultType.FAIL);
        }
    }

    private static boolean isWhitelistedItem(ItemStack interacted) {
        if (interacted.isEmpty()) {
            return false;
        }
        ResourceLocation key = interacted.getItem().getRegistryName();
        if (key.getNamespace().equals("dankstorage") && key.getPath().startsWith("dank_")) {
            return true;
        }
        if (key.toString().equals("quark:pickarang") || key.toString().equals("quark:flamerang")) {
            return true;
        }
        if (key.getNamespace().equals("simplybackpacks")) {
            return true;
        }
        return false;
    }

    private static boolean isInLockedRaidRoom(IWorld world, BlockPos pos) {
        if (world.isClientSide() || !(world instanceof ServerWorld)) {
            return false;
        }
        ServerWorld sWorld = (ServerWorld) world;
        VaultRaid vault = VaultRaidData.get(sWorld).getAt(sWorld, pos);
        if (vault == null) {
            return false;
        }


        VaultRaidRoom room = vault.getGenerator().getPiecesAt(pos, VaultRaidRoom.class).stream().findFirst().orElse(null);
        if (room == null) {
            return false;
        }
        return !room.isRaidFinished();
    }

    private static ActiveRaid getRaidAt(IWorld world, BlockPos pos) {
        if (world.isClientSide() || !(world instanceof ServerWorld)) {
            return null;
        }
        ServerWorld sWorld = (ServerWorld) world;
        VaultRaid vault = VaultRaidData.get(sWorld).getAt(sWorld, pos);
        if (vault == null) {
            return null;
        }
        return vault.getActiveRaid();
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\logic\objective\raid\RaidEventListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */