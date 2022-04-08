package iskallia.vault.client.vault;

import iskallia.vault.Vault;
import iskallia.vault.client.ClientVaultRaidData;
import iskallia.vault.init.ModSounds;
import iskallia.vault.network.message.VaultOverlayMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.audio.SoundHandler;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import javax.annotation.Nullable;
import java.util.function.Supplier;


@EventBusSubscriber({Dist.CLIENT})
public class VaultMusicHandler {
    public static SimpleSound panicSound;
    public static SimpleSound ambientLoop;
    public static SimpleSound ambientSound;
    public static SimpleSound bossLoop;
    public static boolean playBossMusic;
    private static int ticksBeforeAmbientSound;

    public static void startBossLoop() {
        if (bossLoop != null) {
            stopBossLoop();
        }
        bossLoop = SimpleSound.forMusic(ModSounds.VAULT_BOSS_LOOP);
        Minecraft.getInstance().getSoundManager().play((ISound) bossLoop);
    }

    public static void stopBossLoop() {
        if (bossLoop != null) {
            Minecraft.getInstance().getSoundManager().stop((ISound) bossLoop);
            bossLoop = null;
        }
        playBossMusic = false;
    }

    @SubscribeEvent
    public static void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        SoundHandler sh = mc.getSoundManager();
        if (mc.level == null) {
            stopBossLoop();

            return;
        }
        boolean inVault = (mc.level.dimension() == Vault.VAULT_KEY);
        if (!inVault) {
            stopBossLoop();

            return;
        }
        VaultOverlayMessage.OverlayType type = ClientVaultRaidData.getOverlayType();
        if (type != VaultOverlayMessage.OverlayType.VAULT) {
            return;
        }

        int remainingTicks = ClientVaultRaidData.getRemainingTicks();
        int panicTicks = 600;
        if (remainingTicks < 600) {
            panicSound = playNotActive(panicSound, () -> SimpleSound.forUI(ModSounds.TIMER_PANIC_TICK_SFX, 2.0F - remainingTicks / panicTicks));
        }

        if (!ClientVaultRaidData.isInBossFight()) {
            stopBossLoop();
        } else if (!sh.isActive((ISound) bossLoop)) {
            startBossLoop();
        }

        if (ClientVaultRaidData.isInBossFight()) {
            stopSound(ambientLoop);
        } else {
            ambientLoop = playNotActive(ambientLoop, () -> SimpleSound.forMusic(ModSounds.VAULT_AMBIENT_LOOP));
        }

        if (ticksBeforeAmbientSound < 0) {
            ambientSound = playNotActive(ambientSound, () -> {
                ticksBeforeAmbientSound = 3600;
                return SimpleSound.forAmbientAddition(ModSounds.VAULT_AMBIENT);
            });
        }
        ticksBeforeAmbientSound--;
    }

    private static void stopSound(SimpleSound sound) {
        SoundHandler sh = Minecraft.getInstance().getSoundManager();
        if (sound != null && sh.isActive((ISound) sound)) {
            sh.stop((ISound) sound);
        }
    }

    private static SimpleSound playNotActive(@Nullable SimpleSound existing, Supplier<SimpleSound> playSound) {
        Minecraft mc = Minecraft.getInstance();
        if (existing == null || !mc.getSoundManager().isActive((ISound) existing)) {
            existing = playSound.get();
            mc.getSoundManager().play((ISound) existing);
        }
        return existing;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\client\vault\VaultMusicHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */