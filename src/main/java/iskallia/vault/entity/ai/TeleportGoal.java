// 
// Decompiled by Procyon v0.6.0
// 

package iskallia.vault.entity.ai;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.vector.Vector3d;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class TeleportGoal<T extends LivingEntity> extends GoalTask<T> {
    private final Predicate<T> startCondition;
    private final Function<T, Vector3d> targetSupplier;
    private final Consumer<T> postTeleport;

    protected TeleportGoal(final T entity, final Predicate<T> startCondition, final Function<T, Vector3d> targetSupplier, final Consumer<T> postTeleport) {
        super(entity);
        this.startCondition = startCondition;
        this.targetSupplier = targetSupplier;
        this.postTeleport = postTeleport;
    }

    public static <T extends LivingEntity> Builder<T> builder(final T entity) {
        return new Builder<T>(entity);
    }

    public boolean canUse() {
        return this.startCondition.test(this.getEntity());
    }

    public void start() {
        final Vector3d target = this.targetSupplier.apply(this.getEntity());
        if (target != null) {
            final boolean teleported = this.getEntity().randomTeleport(target.x(), target.y(), target.z(), true);
            if (teleported) {
                this.postTeleport.accept(this.getEntity());
            }
        }
    }

    public static class Builder<T extends LivingEntity> {
        private final T entity;
        private Predicate<T> startCondition;
        private Function<T, Vector3d> targetSupplier;
        private Consumer<T> postTeleport;

        private Builder(final T entity) {
            this.entity = entity;
        }

        public Builder<T> start(final Predicate<T> startCondition) {
            this.startCondition = startCondition;
            return this;
        }

        public Builder<T> to(final Function<T, Vector3d> targetSupplier) {
            this.targetSupplier = targetSupplier;
            return this;
        }

        public Builder<T> then(final Consumer<T> postTeleport) {
            this.postTeleport = postTeleport;
            return this;
        }

        public TeleportGoal<T> build() {
            return new TeleportGoal<T>(this.entity, this.startCondition, this.targetSupplier, this.postTeleport);
        }
    }
}
