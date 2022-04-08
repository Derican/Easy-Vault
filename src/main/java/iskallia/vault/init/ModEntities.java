package iskallia.vault.init;

import iskallia.vault.Vault;
import iskallia.vault.entity.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ModEntities {
    public static List<EntityType<VaultFighterEntity>> VAULT_FIGHTER_TYPES = new ArrayList<>();

    public static EntityType<FighterEntity> FIGHTER;

    public static EntityType<ArenaBossEntity> ARENA_BOSS;

    public static EntityType<VaultGuardianEntity> VAULT_GUARDIAN;

    public static EntityType<EternalEntity> ETERNAL;

    public static EntityType<TreasureGoblinEntity> TREASURE_GOBLIN;
    public static EntityType<AggressiveCowEntity> AGGRESSIVE_COW;
    public static EntityType<EtchingVendorEntity> ETCHING_VENDOR;
    public static EntityType<MonsterEyeEntity> MONSTER_EYE;
    public static EntityType<RobotEntity> ROBOT;
    public static EntityType<BlueBlazeEntity> BLUE_BLAZE;
    public static EntityType<BoogiemanEntity> BOOGIEMAN;
    public static EntityType<AggressiveCowBossEntity> AGGRESSIVE_COW_BOSS;
    public static EntityType<DrillArrowEntity> DRILL_ARROW;
    public static EntityType<EffectCloudEntity> EFFECT_CLOUD;
    public static EntityType<FloatingItemEntity> FLOATING_ITEM;

    public static void register(RegistryEvent.Register<EntityType<?>> event) {
        for (int i = 0; i < 10; i++) {
            VAULT_FIGHTER_TYPES.add(registerVaultFighter(i, event));
        }

        FIGHTER = registerLiving("fighter", EntityType.Builder.of(FighterEntity::new, EntityClassification.MONSTER)
                .sized(0.6F, 1.95F), ZombieEntity::createAttributes, event);
        ARENA_BOSS = registerLiving("arena_boss", EntityType.Builder.of(ArenaBossEntity::new, EntityClassification.MONSTER)
                .sized(0.6F, 1.95F), ArenaBossEntity::createAttributes, event);
        VAULT_GUARDIAN = registerLiving("vault_guardian", EntityType.Builder.of(VaultGuardianEntity::new, EntityClassification.MONSTER)
                .sized(1.3F, 2.95F), ZombieEntity::createAttributes, event);
        ETERNAL = registerLiving("eternal", EntityType.Builder.of(EternalEntity::new, EntityClassification.CREATURE)
                .sized(0.6F, 1.95F), ZombieEntity::createAttributes, event);
        TREASURE_GOBLIN = registerLiving("treasure_goblin", EntityType.Builder.of(TreasureGoblinEntity::new, EntityClassification.CREATURE)
                .sized(0.5F, 1.5F), ZombieEntity::createAttributes, event);
        AGGRESSIVE_COW = registerLiving("aggressive_cow", EntityType.Builder.of(AggressiveCowEntity::new, EntityClassification.MONSTER)
                .sized(0.9F, 1.4F).clientTrackingRange(8), AggressiveCowEntity::createAttributes, event);
        ETCHING_VENDOR = registerLiving("etching_vendor", EntityType.Builder.of(EtchingVendorEntity::new, EntityClassification.MISC), ZombieEntity::createAttributes, event);


        MONSTER_EYE = registerLiving("monster_eye", EntityType.Builder.of(MonsterEyeEntity::new, EntityClassification.MONSTER)
                .sized(4.08F, 4.08F), ZombieEntity::createAttributes, event);
        ROBOT = registerLiving("robot", EntityType.Builder.of(RobotEntity::new, EntityClassification.MONSTER)
                .sized(2.8F, 5.4F), ZombieEntity::createAttributes, event);
        BLUE_BLAZE = registerLiving("blue_blaze", EntityType.Builder.of(BlueBlazeEntity::new, EntityClassification.MONSTER)
                .sized(1.2F, 3.6F), ZombieEntity::createAttributes, event);
        BOOGIEMAN = registerLiving("boogieman", EntityType.Builder.of(BoogiemanEntity::new, EntityClassification.MONSTER)
                .sized(1.2F, 3.9F), ZombieEntity::createAttributes, event);
        AGGRESSIVE_COW_BOSS = registerLiving("aggressive_cow_boss", EntityType.Builder.of(AggressiveCowBossEntity::new, EntityClassification.MONSTER)
                .sized(2.6999998F, 4.2F), AggressiveCowEntity::createAttributes, event);

        DRILL_ARROW = register("drill_arrow", EntityType.Builder.of(DrillArrowEntity::new, EntityClassification.MISC), event);
        EFFECT_CLOUD = register("effect_cloud", EntityType.Builder.of(EffectCloudEntity::new, EntityClassification.MISC), event);
        FLOATING_ITEM = register("floating_item", EntityType.Builder.of(FloatingItemEntity::new, EntityClassification.MISC), event);
    }

    private static EntityType<VaultFighterEntity> registerVaultFighter(int count, RegistryEvent.Register<EntityType<?>> event) {
        return registerLiving((count > 0) ? ("vault_fighter_" + count) : "vault_fighter",
                EntityType.Builder.of(VaultFighterEntity::new, EntityClassification.MONSTER).sized(0.6F, 1.95F), ZombieEntity::createAttributes, event);
    }


    private static <T extends net.minecraft.entity.Entity> EntityType<T> register(String name, EntityType.Builder<T> builder, RegistryEvent.Register<EntityType<?>> event) {
        EntityType<T> entityType = builder.build(Vault.sId(name));
        event.getRegistry().register(entityType.setRegistryName(Vault.id(name)));
        return entityType;
    }

    private static <T extends net.minecraft.entity.LivingEntity> EntityType<T> registerLiving(String name, EntityType.Builder<T> builder, Supplier<AttributeModifierMap.MutableAttribute> attributes, RegistryEvent.Register<EntityType<?>> event) {
        EntityType<T> entityType = (EntityType) register(name, (EntityType.Builder) builder, event);
        if (attributes != null) {
            GlobalEntityTypeAttributes.put(entityType, ((AttributeModifierMap.MutableAttribute) attributes.get()).build());
        }
        return entityType;
    }

    public static class Renderers {
        public static void register(FMLClientSetupEvent event) {
            ModEntities.VAULT_FIGHTER_TYPES.forEach(type -> RenderingRegistry.registerEntityRenderingHandler(type, iskallia.vault.entity.renderer.FighterRenderer::new));


            RenderingRegistry.registerEntityRenderingHandler(ModEntities.FIGHTER, iskallia.vault.entity.renderer.FighterRenderer::new);
            RenderingRegistry.registerEntityRenderingHandler(ModEntities.ARENA_BOSS, iskallia.vault.entity.renderer.FighterRenderer::new);
            RenderingRegistry.registerEntityRenderingHandler(ModEntities.VAULT_GUARDIAN, iskallia.vault.entity.renderer.VaultGuardianRenderer::new);
            RenderingRegistry.registerEntityRenderingHandler(ModEntities.ETERNAL, iskallia.vault.entity.renderer.EternalRenderer::new);
            RenderingRegistry.registerEntityRenderingHandler(ModEntities.TREASURE_GOBLIN, iskallia.vault.entity.renderer.TreasureGoblinRenderer::new);
            RenderingRegistry.registerEntityRenderingHandler(ModEntities.AGGRESSIVE_COW, net.minecraft.client.renderer.entity.CowRenderer::new);
            RenderingRegistry.registerEntityRenderingHandler(ModEntities.ETCHING_VENDOR, iskallia.vault.entity.renderer.EtchingVendorRenderer::new);

            RenderingRegistry.registerEntityRenderingHandler(ModEntities.MONSTER_EYE, iskallia.vault.entity.renderer.MonsterEyeRenderer::new);
            RenderingRegistry.registerEntityRenderingHandler(ModEntities.ROBOT, iskallia.vault.entity.renderer.RobotRenderer::new);
            RenderingRegistry.registerEntityRenderingHandler(ModEntities.BLUE_BLAZE, iskallia.vault.entity.renderer.BlueBlazeRenderer::new);
            RenderingRegistry.registerEntityRenderingHandler(ModEntities.BOOGIEMAN, iskallia.vault.entity.renderer.BoogiemanRenderer::new);
            RenderingRegistry.registerEntityRenderingHandler(ModEntities.AGGRESSIVE_COW_BOSS, iskallia.vault.entity.renderer.AggressiveCowBossRenderer::new);

            RenderingRegistry.registerEntityRenderingHandler(ModEntities.DRILL_ARROW, net.minecraft.client.renderer.entity.TippedArrowRenderer::new);
            RenderingRegistry.registerEntityRenderingHandler(ModEntities.EFFECT_CLOUD, iskallia.vault.entity.renderer.EffectCloudRenderer::new);
            RenderingRegistry.registerEntityRenderingHandler(ModEntities.FLOATING_ITEM, rm -> new ItemRenderer(rm, Minecraft.getInstance().getItemRenderer()));
        }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\init\ModEntities.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */