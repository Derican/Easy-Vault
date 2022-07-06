// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.util;

import java.util.Collection;
import net.minecraft.client.network.play.ClientPlayNetHandler;
import java.util.function.Function;
import net.minecraft.client.network.play.NetworkPlayerInfo;
import net.minecraft.client.Minecraft;
import com.mojang.authlib.GameProfile;
import java.util.Optional;
import java.util.UUID;

public class McClientHelper
{
    public static Optional<GameProfile> getOnlineProfile(final UUID uuid) {
        if (uuid == null) {
            return Optional.empty();
        }
        final ClientPlayNetHandler connection = Minecraft.getInstance().getConnection();
        if (connection == null) {
            return Optional.empty();
        }
        final Collection<NetworkPlayerInfo> playerInfoMap = connection.getOnlinePlayers();
        final GameProfile gameProfile = playerInfoMap.stream().map((Function<? super NetworkPlayerInfo, ? extends GameProfile>)NetworkPlayerInfo::getProfile).filter(profile -> profile.getId().equals(uuid)).findFirst().orElse(null);
        return Optional.ofNullable(gameProfile);
    }
}
