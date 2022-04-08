package iskallia.vault.skill;

import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModNetwork;
import iskallia.vault.init.ModSounds;
import iskallia.vault.network.message.VaultLevelMessage;
import iskallia.vault.util.NetcodeUtils;
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

public class PlayerVaultStats
        implements INBTSerializable<CompoundNBT> {
    private final UUID uuid;
    private int vaultLevel;
    private int exp;
    private int unspentSkillPts = 5;
    private int unspentKnowledgePts;
    private int totalSpentSkillPoints;
    private int totalSpentKnowledgePoints;

    public PlayerVaultStats(UUID uuid) {
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
        return (ModConfigs.LEVELS_META.getLevelMeta(this.vaultLevel)).tnl;
    }


    public PlayerVaultStats setVaultLevel(MinecraftServer server, int level) {
        this.vaultLevel = level;
        this.exp = 0;
        sync(server);

        return this;
    }


    public PlayerVaultStats addVaultExp(MinecraftServer server, int exp) {
        this.exp = Math.max(this.exp, 0);
        this.exp += exp;

        int initialLevel = this.vaultLevel;
        int tnl;
        while (this.exp >= (tnl = getTnl())) {
            this.vaultLevel++;
            if (this.vaultLevel <= 200) {
                this.unspentSkillPts++;
            }
            this.exp -= tnl;
        }

        if (this.vaultLevel > initialLevel) {
            NetcodeUtils.runIfPresent(server, this.uuid, this::fancyLevelUpEffects);
        }

        sync(server);

        return this;
    }

    protected void fancyLevelUpEffects(ServerPlayerEntity player) {
        World world = player.level;

        Vector3d pos = player.position();

        for (int i = 0; i < 20; i++) {
            double d0 = world.random.nextGaussian() * 1.0D;
            double d1 = world.random.nextGaussian() * 1.0D;
            double d2 = world.random.nextGaussian() * 1.0D;

            ((ServerWorld) world).sendParticles((IParticleData) ParticleTypes.TOTEM_OF_UNDYING, pos
                    .x() + world.random.nextDouble() - 0.5D, pos
                    .y() + world.random.nextDouble() - 0.5D + 3.0D, pos
                    .z() + world.random.nextDouble() - 0.5D, 10, d0, d1, d2, 0.25D);
        }

        world.playSound(null, player.blockPosition(), ModSounds.VAULT_LEVEL_UP_SFX, SoundCategory.PLAYERS, 1.0F, 2.0F);
    }


    public PlayerVaultStats spendSkillPoints(MinecraftServer server, int amount) {
        this.unspentSkillPts -= amount;
        this.totalSpentSkillPoints += amount;

        sync(server);

        return this;
    }

    public PlayerVaultStats spendKnowledgePoints(MinecraftServer server, int amount) {
        this.unspentKnowledgePts -= amount;
        this.totalSpentKnowledgePoints += amount;

        sync(server);

        return this;
    }

    public PlayerVaultStats reset(MinecraftServer server) {
        this.vaultLevel = 0;
        this.exp = 0;
        this.unspentSkillPts = 0;
        this.unspentKnowledgePts = 0;

        sync(server);

        return this;
    }

    public PlayerVaultStats addSkillPoints(int amount) {
        this.unspentSkillPts += amount;
        return this;
    }

    public PlayerVaultStats addKnowledgePoints(int amount) {
        this.unspentKnowledgePts += amount;
        return this;
    }


    public void sync(MinecraftServer server) {
        NetcodeUtils.runIfPresent(server, this.uuid, player -> ModNetwork.CHANNEL.sendTo(new VaultLevelMessage(this.vaultLevel, this.exp, getTnl(), this.unspentSkillPts, this.unspentKnowledgePts), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT));
    }


    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("vaultLevel", this.vaultLevel);
        nbt.putInt("exp", this.exp);
        nbt.putInt("unspentSkillPts", this.unspentSkillPts);
        nbt.putInt("unspentKnowledgePts", this.unspentKnowledgePts);
        nbt.putInt("totalSpentSkillPoints", this.totalSpentSkillPoints);
        nbt.putInt("totalSpentKnowledgePoints", this.totalSpentKnowledgePoints);
        return nbt;
    }


    public void deserializeNBT(CompoundNBT nbt) {
        this.vaultLevel = nbt.getInt("vaultLevel");
        this.exp = nbt.getInt("exp");
        this.unspentSkillPts = nbt.getInt("unspentSkillPts");
        this.unspentKnowledgePts = nbt.getInt("unspentKnowledgePts");
        this.totalSpentSkillPoints = nbt.getInt("totalSpentSkillPoints");
        this.totalSpentKnowledgePoints = nbt.getInt("totalSpentKnowledgePoints");
        this.vaultLevel = nbt.getInt("vaultLevel");
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\skill\PlayerVaultStats.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */