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
    public static final ExecutorService SERVICE = Executors.newFixedThreadPool(4);

    private String latestNickname;
    public AtomicReference<GameProfile> gameProfile = new AtomicReference<>();
    public AtomicReference<NetworkPlayerInfo> playerInfo = new AtomicReference<>();

    public String getLatestNickname() {
        return this.latestNickname;
    }

    public void updateSkin(String name) {
        if (name.equals(this.latestNickname)) {
            return;
        }
        this.latestNickname = name;

        if (FMLEnvironment.dist.isClient())
            SERVICE.submit(() -> {
                this.gameProfile.set(new GameProfile(null, name));
                this.gameProfile.set(SkullTileEntity.updateGameprofile(this.gameProfile.get()));
                (new SPlayerListItemPacket()).getClass();
                SPlayerListItemPacket.AddPlayerData data = new SPlayerListItemPacket.AddPlayerData(new SPlayerListItemPacket(), this.gameProfile.get(), 0, null, null);
                this.playerInfo.set(new NetworkPlayerInfo(data));
            });
    }

    @OnlyIn(Dist.CLIENT)
    public ResourceLocation getLocationSkin() {
        if (this.playerInfo == null || this.playerInfo.get() == null) {
            return DefaultPlayerSkin.getDefaultSkin();
        }

        try {
            return ((NetworkPlayerInfo) this.playerInfo.get()).getSkinLocation();
        } catch (Exception e) {
            e.printStackTrace();


            return DefaultPlayerSkin.getDefaultSkin();
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vaul\\util\SkinProfile.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */