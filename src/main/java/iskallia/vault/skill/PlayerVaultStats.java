// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.skill;

import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModNetwork;
import iskallia.vault.init.ModSounds;
import iskallia.vault.network.message.VaultLevelMessage;
import iskallia.vault.util.NetcodeUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.network.NetworkDirection;

import java.util.UUID;

public class PlayerVaultStats implements INBTSerializable<CompoundNBT>
{
    private final UUID uuid;
    private int vaultLevel;
    private int exp;
    private int unspentSkillPts;
    private int unspentKnowledgePts;
    private int totalSpentSkillPoints;
    private int totalSpentKnowledgePoints;
    
    public PlayerVaultStats(final UUID uuid) {
        this.unspentSkillPts = 5;
        this.uuid = uuid;
    }
    
    public int getVaultLevel() {
        return this.vaultLevel;
    }
    
    public int getExp() {
        return this.exp;
    }
    
    public int getUnspentSkillPts() {
        return this.unspentSkillPts;
    }
    
    public int getUnspentKnowledgePts() {
        return this.unspentKnowledgePts;
    }
    
    public int getTotalSpentSkillPoints() {
        return this.totalSpentSkillPoints;
    }
    
    public int getTotalSpentKnowledgePoints() {
        return this.totalSpentKnowledgePoints;
    }
    
    public int getTnl() {
        return ModConfigs.LEVELS_META.getLevelMeta(this.vaultLevel).tnl;
    }
    
    public PlayerVaultStats setVaultLevel(final MinecraftServer server, final int level) {
        this.vaultLevel = level;
        this.exp = 0;
        this.sync(server);
        return this;
    }
    
    public PlayerVaultStats addVaultExp(final MinecraftServer server, final int exp) {
        this.exp = Math.max(this.exp, 0);
        this.exp += exp;
        final int initialLevel = this.vaultLevel;
        int tnl;
        while (this.exp >= (tnl = this.getTnl())) {
            ++this.vaultLevel;
            if (this.vaultLevel <= 200) {
                ++this.unspentSkillPts;
            }
            this.exp -= tnl;
        }
        if (this.vaultLevel > initialLevel) {
            NetcodeUtils.runIfPresent(server, this.uuid, this::fancyLevelUpEffects);
        }
        this.sync(server);
        return this;
    }
    
    protected void fancyLevelUpEffects(final ServerPlayerEntity player) {
        final World world = player.level;
        final Vector3d pos = player.position();
        for (int i = 0; i < 20; ++i) {
            final double d0 = world.random.nextGaussian() * 1.0;
            final double d2 = world.random.nextGaussian() * 1.0;
            final double d3 = world.random.nextGaussian() * 1.0;
            ((ServerWorld)world).sendParticles((IParticleData)ParticleTypes.TOTEM_OF_UNDYING, pos.x() + world.random.nextDouble() - 0.5, pos.y() + world.random.nextDouble() - 0.5 + 3.0, pos.z() + world.random.nextDouble() - 0.5, 10, d0, d2, d3, 0.25);
        }
        world.playSound((PlayerEntity)null, player.blockPosition(), ModSounds.VAULT_LEVEL_UP_SFX, SoundCategory.PLAYERS, 1.0f, 2.0f);
    }
    
    public PlayerVaultStats spendSkillPoints(final MinecraftServer server, final int amount) {
        this.unspentSkillPts -= amount;
        this.totalSpentSkillPoints += amount;
        this.sync(server);
        return this;
    }
    
    public PlayerVaultStats spendKnowledgePoints(final MinecraftServer server, final int amount) {
        this.unspentKnowledgePts -= amount;
        this.totalSpentKnowledgePoints += amount;
        this.sync(server);
        return this;
    }
    
    public PlayerVaultStats reset(final MinecraftServer server) {
        this.vaultLevel = 0;
        this.exp = 0;
        this.unspentSkillPts = 0;
        this.unspentKnowledgePts = 0;
        this.sync(server);
        return this;
    }
    
    public PlayerVaultStats addSkillPoints(final int amount) {
        this.unspentSkillPts += amount;
        return this;
    }
    
    public PlayerVaultStats addKnowledgePoints(final int amount) {
        this.unspentKnowledgePts += amount;
        return this;
    }
    
    public void sync(final MinecraftServer server) {
        NetcodeUtils.runIfPresent(server, this.uuid, (ServerPlayerEntity player) -> ModNetwork.CHANNEL.sendTo(new VaultLevelMessage(this.vaultLevel, this.exp, this.getTnl(), this.unspentSkillPts, this.unspentKnowledgePts), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT));
    }
    
    public CompoundNBT serializeNBT() {
        final CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("vaultLevel", this.vaultLevel);
        nbt.putInt("exp", this.exp);
        nbt.putInt("unspentSkillPts", this.unspentSkillPts);
        nbt.putInt("unspentKnowledgePts", this.unspentKnowledgePts);
        nbt.putInt("totalSpentSkillPoints", this.totalSpentSkillPoints);
        nbt.putInt("totalSpentKnowledgePoints", this.totalSpentKnowledgePoints);
        return nbt;
    }
    
    public void deserializeNBT(final CompoundNBT nbt) {
        this.vaultLevel = nbt.getInt("vaultLevel");
        this.exp = nbt.getInt("exp");
        this.unspentSkillPts = nbt.getInt("unspentSkillPts");
        this.unspentKnowledgePts = nbt.getInt("unspentKnowledgePts");
        this.totalSpentSkillPoints = nbt.getInt("totalSpentSkillPoints");
        this.totalSpentKnowledgePoints = nbt.getInt("totalSpentKnowledgePoints");
        this.vaultLevel = nbt.getInt("vaultLevel");
    }
}
