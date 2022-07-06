package iskallia.vault.util;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.play.NetworkPlayerInfo;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.network.play.server.SPlayerListItemPacket;
import net.minecraft.tileentity.SkullTileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.loading.FMLEnvironment;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

public class SkinProfile {
    public static final ExecutorService SERVICE;
    private String latestNickname;
    public AtomicReference<GameProfile> gameProfile;
    public AtomicReference<NetworkPlayerInfo> playerInfo;

    public SkinProfile() {
        this.gameProfile = new AtomicReference<GameProfile>();
        this.playerInfo = new AtomicReference<NetworkPlayerInfo>();
    }

    public String getLatestNickname() {
        return this.latestNickname;
    }

    public void updateSkin(final String name) {
        if (name.equals(this.latestNickname)) {
            return;
        }
        this.latestNickname = name;

        if (FMLEnvironment.dist.isClient())
            SERVICE.submit(() -> {
                this.gameProfile.set(new GameProfile(null, name));
                this.gameProfile.set(SkullTileEntity.updateGameprofile(this.gameProfile.get()));
                SPlayerListItemPacket sPlayerListItemPacket = new SPlayerListItemPacket();
                SPlayerListItemPacket.AddPlayerData data = sPlayerListItemPacket.new AddPlayerData(this.gameProfile.get(), 0, null, null);
                this.playerInfo.set(new NetworkPlayerInfo(data));
            });
    }

    @OnlyIn(Dist.CLIENT)
    public ResourceLocation getLocationSkin() {
        if (this.playerInfo == null || this.playerInfo.get() == null) {
            return DefaultPlayerSkin.getDefaultSkin();
        }
        try {
            return this.playerInfo.get().getSkinLocation();
        } catch (final Exception e) {
            e.printStackTrace();
            return DefaultPlayerSkin.getDefaultSkin();
        }
    }

    static {
        SERVICE = Executors.newFixedThreadPool(4);
    }
}
