package iskallia.vault.world.vault.chest;

import com.google.gson.annotations.Expose;
import iskallia.vault.util.DamageUtil;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.player.VaultPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.world.Explosion;
import net.minecraft.world.server.ServerWorld;

public class ExplosionEffect extends VaultChestEffect {
    @Expose
    private final float radius;
    @Expose
    private final double xOffset;
    @Expose
    private final double yOffset;
    @Expose
    private final double zOffset;
    @Expose
    private final boolean causesFire;
    @Expose
    private final float damage;
    @Expose
    private final String mode;

    public ExplosionEffect(final String name, final float radius, final double xOffset, final double yOffset, final double zOffset, final boolean causesFire, final float damage, final Explosion.Mode mode) {
        super(name);
        this.radius = radius;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.zOffset = zOffset;
        this.causesFire = causesFire;
        this.damage = damage;
        this.mode = mode.name();
    }

    public float getRadius() {
        return this.radius;
    }

    public double getXOffset() {
        return this.xOffset;
    }

    public double getYOffset() {
        return this.yOffset;
    }

    public double getZOffset() {
        return this.zOffset;
    }

    public boolean causesFire() {
        return this.causesFire;
    }

    public float getDamage() {
        return this.damage;
    }

    public Explosion.Mode getMode() {
        return Enum.valueOf(Explosion.Mode.class, this.mode);
    }

    @Override
    public void apply(final VaultRaid vault, final VaultPlayer player, final ServerWorld world) {
        player.runIfPresent(world.getServer(), playerEntity -> {
            world.explode(playerEntity, playerEntity.getX() + this.getXOffset(), playerEntity.getY() + this.getYOffset(), playerEntity.getZ() + this.getZOffset(), this.getRadius(), this.causesFire(), this.getMode());
            DamageUtil.shotgunAttack(playerEntity, entity -> entity.hurt(new DamageSource("explosion").setExplosion(), this.getDamage()));
        });
    }
}
