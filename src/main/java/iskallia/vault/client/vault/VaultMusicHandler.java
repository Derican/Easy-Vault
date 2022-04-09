// 
// Decompiled by Procyon v0.6.0
// 

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
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.function.Supplier;

@Mod.EventBusSubscriber({ Dist.CLIENT })
public class VaultMusicHandler
{
    public static SimpleSound panicSound;
    public static SimpleSound ambientLoop;
    public static SimpleSound ambientSound;
    public static SimpleSound bossLoop;
    public static boolean playBossMusic;
    private static int ticksBeforeAmbientSound;
    
    public static void startBossLoop() {
        if (VaultMusicHandler.bossLoop != null) {
            stopBossLoop();
        }
        VaultMusicHandler.bossLoop = SimpleSound.forMusic(ModSounds.VAULT_BOSS_LOOP);
        Minecraft.getInstance().getSoundManager().play((ISound)VaultMusicHandler.bossLoop);
    }
    
    public static void stopBossLoop() {
        if (VaultMusicHandler.bossLoop != null) {
            Minecraft.getInstance().getSoundManager().stop((ISound)VaultMusicHandler.bossLoop);
            VaultMusicHandler.bossLoop = null;
        }
        VaultMusicHandler.playBossMusic = false;
    }
    
    @SubscribeEvent
    public static void onTick(final TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            return;
        }
        final Minecraft mc = Minecraft.getInstance();
        final SoundHandler sh = mc.getSoundManager();
        if (mc.level == null) {
            stopBossLoop();
            return;
        }
        final boolean inVault = mc.level.dimension() == Vault.VAULT_KEY;
        if (!inVault) {
            stopBossLoop();
            return;
        }
        final VaultOverlayMessage.OverlayType type = ClientVaultRaidData.getOverlayType();
        if (type != VaultOverlayMessage.OverlayType.VAULT) {
            return;
        }
        final int remainingTicks = ClientVaultRaidData.getRemainingTicks();
        final int panicTicks = 600;
        if (remainingTicks < 600) {
            VaultMusicHandler.panicSound = playNotActive(VaultMusicHandler.panicSound, () -> SimpleSound.forUI(ModSounds.TIMER_PANIC_TICK_SFX, 2.0f - remainingTicks / (float)panicTicks));
        }
        if (!ClientVaultRaidData.isInBossFight()) {
            stopBossLoop();
        }
        else if (!sh.isActive((ISound)VaultMusicHandler.bossLoop)) {
            startBossLoop();
        }
        if (ClientVaultRaidData.isInBossFight()) {
            stopSound(VaultMusicHandler.ambientLoop);
        }
        else {
            VaultMusicHandler.ambientLoop = playNotActive(VaultMusicHandler.ambientLoop, () -> SimpleSound.forMusic(ModSounds.VAULT_AMBIENT_LOOP));
        }
        if (VaultMusicHandler.ticksBeforeAmbientSound < 0) {
            VaultMusicHandler.ambientSound = playNotActive(VaultMusicHandler.ambientSound, () -> {
                VaultMusicHandler.ticksBeforeAmbientSound = 3600;
                return SimpleSound.forAmbientAddition(ModSounds.VAULT_AMBIENT);
            });
        }
        --VaultMusicHandler.ticksBeforeAmbientSound;
    }
    
    private static void stopSound(final SimpleSound sound) {
        final SoundHandler sh = Minecraft.getInstance().getSoundManager();
        if (sound != null && sh.isActive((ISound)sound)) {
            sh.stop((ISound)sound);
        }
    }
    
    private static SimpleSound playNotActive(@Nullable SimpleSound existing, final Supplier<SimpleSound> playSound) {
        final Minecraft mc = Minecraft.getInstance();
        if (existing == null || !mc.getSoundManager().isActive((ISound)existing)) {
            existing = playSound.get();
            mc.getSoundManager().play((ISound)existing);
        }
        return existing;
    }
}
