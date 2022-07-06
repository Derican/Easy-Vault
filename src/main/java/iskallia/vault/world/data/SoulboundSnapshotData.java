package iskallia.vault.world.data;

import iskallia.vault.init.ModAttributes;
import iskallia.vault.world.vault.VaultRaid;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SoulboundSnapshotData extends InventorySnapshotData {
    protected static final String DATA_NAME = "the_vault_Soulbound";

    public SoulboundSnapshotData() {
        super("the_vault_Soulbound");
    }

    @Override
    protected boolean shouldSnapshotItem(final PlayerEntity player, final ItemStack stack) {
        return ModAttributes.SOULBOUND.getOrDefault(stack, false).getValue(stack);
    }

    @SubscribeEvent
    public static void onTick(final TickEvent.PlayerTickEvent event) {
        final PlayerEntity player = event.player;
        if (!player.isAlive() || !(player.level instanceof ServerWorld)) {
            return;
        }
        final ServerWorld world = (ServerWorld) event.player.level;
        final SoulboundSnapshotData data = get(world);
        if (data.hasSnapshot(player)) {
            data.restoreSnapshot(player);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onDeath(final LivingDeathEvent event) {
        if (!(event.getEntity() instanceof PlayerEntity) || !(event.getEntity().level instanceof ServerWorld)) {
            return;
        }
        final PlayerEntity player = (PlayerEntity) event.getEntity();
        final ServerWorld world = (ServerWorld) player.level;
        final VaultRaid vault = VaultRaidData.get(world).getAt(world, player.blockPosition());
        if (vault != null && vault.getProperties().exists(VaultRaid.PARENT)) {
            return;
        }
        final SoulboundSnapshotData data = get(world);
        if (!data.hasSnapshot(player)) {
            data.createSnapshot(player);
        }
    }

    public static SoulboundSnapshotData get(final ServerWorld world) {
        return (SoulboundSnapshotData) world.getServer().overworld().getDataStorage().computeIfAbsent((Supplier) SoulboundSnapshotData::new, "the_vault_Soulbound");
    }
}
