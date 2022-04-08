package iskallia.vault.world.vault.chest;

import com.google.gson.annotations.Expose;
import iskallia.vault.util.DamageUtil;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.player.VaultPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.world.Explosion;
import net.minecraft.world.server.ServerWorld;

public class ExplosionEffect extends VaultChestEffect {
    @Expose
    private final float radius;
    @Expose
    private final double xOffset;
    @Expose
    private final double yOffset;

    public ExplosionEffect(String name, float radius, double xOffset, double yOffset, double zOffset, boolean causesFire, float damage, Explosion.Mode mode) {
        super(name);
        this.radius = radius;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.zOffset = zOffset;
        this.causesFire = causesFire;
        this.damage = damage;
        this.mode = mode.name();
    }

    @Expose
    private final double zOffset;
    @Expose
    private final boolean causesFire;
    @Expose
    private final float damage;
    @Expose
    private final String mode;

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
        return Enum.<Explosion.Mode>valueOf(Explosion.Mode.class, this.mode);
    }


    public void apply(VaultRaid vault, VaultPlayer player, ServerWorld world) {
        player.runIfPresent(world.getServer(), playerEntity -> {
            world.explode((Entity) playerEntity, playerEntity.getX() + getXOffset(), playerEntity.getY() + getYOffset(), playerEntity.getZ() + getZOffset(), getRadius(), causesFire(), getMode());
            DamageUtil.shotgunAttack((Entity) playerEntity, ());
        });
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\world\vault\chest\ExplosionEffect.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */