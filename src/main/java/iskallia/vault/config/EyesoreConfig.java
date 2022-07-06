//
// Decompiled by Procyon v0.6.0
//

package iskallia.vault.config;

import iskallia.vault.entity.EyestalkEntity;
import iskallia.vault.entity.EyesoreFireballEntity;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.data.VaultRaidData;
import net.minecraft.world.server.ServerWorld;
import iskallia.vault.entity.EyesoreEntity;
import com.google.gson.annotations.Expose;

public class EyesoreConfig extends Config {
    @Expose
    public float health;
    @Expose
    public float extraHealthPerPlayer;
    @Expose
    public MeleeAttack meleeAttack;
    @Expose
    public BasicAttack basicAttack;
    @Expose
    public LaserAttack laserAttack;
    @Expose
    public EyeStalk eyeStalk;

    public float getHealth(final EyesoreEntity entity) {
        if (!(entity.getCommandSenderWorld() instanceof ServerWorld)) {
            return 10.0f;
        }
        final ServerWorld world = (ServerWorld) entity.getCommandSenderWorld();
        final VaultRaid vault = VaultRaidData.get(world).getAt(world, entity.blockPosition());
        float health = this.health;
        if (vault != null) {
            health += this.extraHealthPerPlayer * (vault.getPlayers().size() - 1);
        }
        return health;
    }

    @Override
    public String getName() {
        return "eyesore";
    }

    @Override
    protected void reset() {
        this.health = 512.0f;
        this.meleeAttack = new MeleeAttack(10.0f, 2.0f, 5.0f);
        this.basicAttack = new BasicAttack(5.0f, 4.0f);
        this.laserAttack = new LaserAttack(4.0f, 2.0f, 20);
        this.eyeStalk = new EyeStalk(1.0f, 1.0f, 3.0f);
    }

    public static class MeleeAttack {
        @Expose
        public float baseDamage;
        @Expose
        public float extraDamagePerPlayer;
        @Expose
        public float knockback;

        public MeleeAttack(final float baseDamage, final float extraDamagePerPlayer, final float knockback) {
            this.baseDamage = baseDamage;
            this.extraDamagePerPlayer = extraDamagePerPlayer;
            this.knockback = knockback;
        }

        public float getDamage(final EyesoreEntity entity) {
            final ServerWorld world = (ServerWorld) entity.getCommandSenderWorld();
            final VaultRaid vault = VaultRaidData.get(world).getAt(world, entity.blockPosition());
            float damage = this.baseDamage;
            if (vault != null) {
                damage += this.extraDamagePerPlayer * (vault.getPlayers().size() - 1);
            }
            return damage;
        }
    }

    public static class BasicAttack {
        @Expose
        public float baseDamage;
        @Expose
        public float extraDamagePerPlayer;

        public BasicAttack(final float baseDamage, final float extraDamagePerPlayer) {
            this.baseDamage = baseDamage;
            this.extraDamagePerPlayer = extraDamagePerPlayer;
        }

        public float getDamage(final EyesoreFireballEntity entity) {
            final ServerWorld world = (ServerWorld) entity.getCommandSenderWorld();
            final VaultRaid vault = VaultRaidData.get(world).getAt(world, entity.blockPosition());
            float damage = this.baseDamage;
            if (vault != null) {
                damage += this.extraDamagePerPlayer * (vault.getPlayers().size() - 1);
            }
            return damage;
        }
    }

    public static class LaserAttack {
        @Expose
        public float baseDamage;
        @Expose
        public float extraDamagePerPlayer;
        @Expose
        public int tickDelay;

        public LaserAttack(final float baseDamage, final float extraDamagePerPlayer, final int tickDelay) {
            this.baseDamage = baseDamage;
            this.extraDamagePerPlayer = extraDamagePerPlayer;
            this.tickDelay = tickDelay;
        }

        public float getDamage(final EyesoreEntity entity, final int tick) {
            if (tick % this.tickDelay != 0) {
                return 0.0f;
            }
            final ServerWorld world = (ServerWorld) entity.getCommandSenderWorld();
            final VaultRaid vault = VaultRaidData.get(world).getAt(world, entity.blockPosition());
            float damage = this.baseDamage;
            if (vault != null) {
                damage += this.extraDamagePerPlayer * (vault.getPlayers().size() - 1);
            }
            return damage;
        }
    }

    public static class EyeStalk {
        @Expose
        public float baseDamage;
        @Expose
        public float extraDamagePerPlayer;
        @Expose
        public float knockback;

        public EyeStalk(final float baseDamage, final float extraDamagePerPlayer, final float knockback) {
            this.baseDamage = baseDamage;
            this.extraDamagePerPlayer = extraDamagePerPlayer;
            this.knockback = knockback;
        }

        public float getDamage(final EyestalkEntity entity) {
            final ServerWorld world = (ServerWorld) entity.getCommandSenderWorld();
            final VaultRaid vault = VaultRaidData.get(world).getAt(world, entity.blockPosition());
            float damage = this.baseDamage;
            if (vault != null) {
                damage += this.extraDamagePerPlayer * (vault.getPlayers().size() - 1);
            }
            return damage;
        }
    }
}
