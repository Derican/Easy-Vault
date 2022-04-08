package iskallia.vault.entity;

import com.google.common.base.Strings;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModEntities;
import iskallia.vault.util.NameProviderPublic;
import iskallia.vault.world.data.VaultRaidData;
import iskallia.vault.world.vault.VaultRaid;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootTable;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class VaultFighterEntity extends FighterEntity {
    public VaultFighterEntity(EntityType<? extends ZombieEntity> type, World world) {
        super(type, world);
    }

    protected void dropFromLootTable(DamageSource source, boolean attackedRecently) {
        ServerWorld world = (ServerWorld) this.level;
        VaultRaid vault = VaultRaidData.get(world).getAt(world, blockPosition());

        if (vault != null) {
            vault.getProperties().getBase(VaultRaid.HOST).flatMap(vault::getPlayer).ifPresent(player -> {
                int level = ((Integer) player.getProperties().getBase(VaultRaid.LEVEL).orElse(Integer.valueOf(0))).intValue();

                ResourceLocation id = ModConfigs.LOOT_TABLES.getForLevel(level).getVaultFighter();
                LootTable loot = this.level.getServer().getLootTables().get(id);
                LootContext.Builder builder = createLootContext(attackedRecently, source);
                LootContext ctx = builder.create(LootParameterSets.ENTITY);
                loot.getRandomItems(ctx).forEach(this::spawnAtLocation);
            });
        }
        super.dropFromLootTable(source, attackedRecently);
    }


    public ILivingEntityData finalizeSpawn(IServerWorld world, DifficultyInstance difficulty, SpawnReason reason, ILivingEntityData spawnData, CompoundNBT dataTag) {
        ILivingEntityData livingData = super.finalizeSpawn(world, difficulty, reason, spawnData, dataTag);

        ServerWorld sWorld = (ServerWorld) this.level;

        if (!this.level.isClientSide()) {
            VaultRaid vault = VaultRaidData.get(sWorld).getAt(sWorld, blockPosition());

            if (vault != null) {
                String name = NameProviderPublic.getRandomName();

                String star = String.valueOf('✦');
                int count = Math.max(ModEntities.VAULT_FIGHTER_TYPES.indexOf(getType()), 0);


                IFormattableTextComponent customName = (new StringTextComponent("")).append((ITextComponent) (new StringTextComponent(Strings.repeat(star, count))).withStyle(TextFormatting.GOLD)).append(" ").append((ITextComponent) new StringTextComponent(name));
                setCustomName((ITextComponent) customName);
                getPersistentData().putString("VaultPlayerName", name);
            }
        }

        return livingData;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\entity\VaultFighterEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */