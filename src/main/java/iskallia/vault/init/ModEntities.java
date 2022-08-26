package iskallia.vault.init;

import iskallia.vault.Vault;
import iskallia.vault.entity.*;
import iskallia.vault.entity.renderer.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.CowRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraft.client.renderer.entity.TippedArrowRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
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
    public static List<EntityType<VaultFighterEntity>> VAULT_FIGHTER_TYPES;
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
    public static EntityType<EyesoreEntity> EYESORE;
    public static EntityType<EyestalkEntity> EYESTALK;
    public static EntityType<DrillArrowEntity> DRILL_ARROW;
    public static EntityType<EffectCloudEntity> EFFECT_CLOUD;
    public static EntityType<VaultSandEntity> VAULT_SAND;
    public static EntityType<FloatingItemEntity> FLOATING_ITEM;
    public static EntityType<EyesoreFireballEntity> EYESORE_FIREBALL;

    public static void register(final RegistryEvent.Register<EntityType<?>> event) {
        for (int i = 0; i < 10; ++i) {
            ModEntities.VAULT_FIGHTER_TYPES.add(registerVaultFighter(i, event));
        }
        ModEntities.FIGHTER = registerLiving("fighter", EntityType.Builder.of(FighterEntity::new, EntityClassification.MONSTER).sized(0.6f, 1.95f), ZombieEntity::createAttributes, event);
        ModEntities.ARENA_BOSS = registerLiving("arena_boss", EntityType.Builder.of(ArenaBossEntity::new, EntityClassification.MONSTER).sized(0.6f, 1.95f), ArenaBossEntity::createAttributes, event);
        ModEntities.VAULT_GUARDIAN = registerLiving("vault_guardian", EntityType.Builder.of(VaultGuardianEntity::new, EntityClassification.MONSTER).sized(1.3f, 2.95f), ZombieEntity::createAttributes, event);
        ModEntities.ETERNAL = registerLiving("eternal", EntityType.Builder.of(EternalEntity::new, EntityClassification.CREATURE).sized(0.6f, 1.95f), ZombieEntity::createAttributes, event);
        ModEntities.TREASURE_GOBLIN = registerLiving("treasure_goblin", EntityType.Builder.of(TreasureGoblinEntity::new, EntityClassification.CREATURE).sized(0.5f, 1.5f), ZombieEntity::createAttributes, event);
        ModEntities.AGGRESSIVE_COW = registerLiving("aggressive_cow", EntityType.Builder.of(AggressiveCowEntity::new, EntityClassification.MONSTER).sized(0.9f, 1.4f).clientTrackingRange(8), AggressiveCowEntity::createAttributes, event);
        ModEntities.ETCHING_VENDOR = registerLiving("etching_vendor", EntityType.Builder.of(EtchingVendorEntity::new, EntityClassification.MISC), ZombieEntity::createAttributes, event);
        ModEntities.MONSTER_EYE = registerLiving("monster_eye", EntityType.Builder.of(MonsterEyeEntity::new, EntityClassification.MONSTER).sized(4.08f, 4.08f), ZombieEntity::createAttributes, event);
        ModEntities.ROBOT = registerLiving("robot", EntityType.Builder.of(RobotEntity::new, EntityClassification.MONSTER).sized(2.8f, 5.4f), ZombieEntity::createAttributes, event);
        ModEntities.BLUE_BLAZE = registerLiving("blue_blaze", EntityType.Builder.of(BlueBlazeEntity::new, EntityClassification.MONSTER).sized(1.2f, 3.6f), ZombieEntity::createAttributes, event);
        ModEntities.BOOGIEMAN = registerLiving("boogieman", EntityType.Builder.of(BoogiemanEntity::new, EntityClassification.MONSTER).sized(1.2f, 3.9f), ZombieEntity::createAttributes, event);
        ModEntities.AGGRESSIVE_COW_BOSS = registerLiving("aggressive_cow_boss", EntityType.Builder.of(AggressiveCowBossEntity::new, EntityClassification.MONSTER).sized(2.6999998f, 4.2f), AggressiveCowEntity::createAttributes, event);
        ModEntities.EYESORE = registerLiving("eyesore", EntityType.Builder.of(EyesoreEntity::new, EntityClassification.MONSTER).sized(9.78f, 9.78f), EyesoreEntity::createAttributes, event);
        ModEntities.EYESTALK = registerLiving("eyestalk",EntityType.Builder.of(EyestalkEntity::new, EntityClassification.MONSTER).sized(2.2f, 2.6f), EyestalkEntity::createAttributes, event);
        ModEntities.DRILL_ARROW = register("drill_arrow", EntityType.Builder.of(DrillArrowEntity::new, EntityClassification.MISC), event);
        ModEntities.EFFECT_CLOUD = register("effect_cloud", EntityType.Builder.of(EffectCloudEntity::new, EntityClassification.MISC), event);
        ModEntities.VAULT_SAND = register("vault_sand", EntityType.Builder.of(VaultSandEntity::new, EntityClassification.MISC), event);
        ModEntities.FLOATING_ITEM = register("floating_item", EntityType.Builder.of(FloatingItemEntity::new, EntityClassification.MISC), event);
        ModEntities.EYESORE_FIREBALL = register("eyesore_fireball", EntityType.Builder.of(EyesoreFireballEntity::new, EntityClassification.MISC), event);
    }

    private static EntityType<VaultFighterEntity> registerVaultFighter(final int count, final RegistryEvent.Register<EntityType<?>> event) {
        return registerLiving((count > 0) ? ("vault_fighter_" + count) : "vault_fighter", EntityType.Builder.of(VaultFighterEntity::new, EntityClassification.MONSTER).sized(0.6f, 1.95f), ZombieEntity::createAttributes, event);
    }

    private static <T extends Entity> EntityType<T> register(final String name, final EntityType.Builder<T> builder, final RegistryEvent.Register<EntityType<?>> event) {
        final EntityType<T> entityType = builder.build(Vault.sId(name));
        event.getRegistry().register(entityType.setRegistryName(Vault.id(name)));
        return entityType;
    }

    private static <T extends LivingEntity> EntityType<T> registerLiving(final String name, final EntityType.Builder<T> builder, final Supplier<AttributeModifierMap.MutableAttribute> attributes, final RegistryEvent.Register<EntityType<?>> event) {
        final EntityType<T> entityType = register(name, builder, event);
        if (attributes != null) {
            GlobalEntityTypeAttributes.put( entityType, attributes.get().build());
        }
        return entityType;
    }

    static {
        ModEntities.VAULT_FIGHTER_TYPES = new ArrayList<EntityType<VaultFighterEntity>>();
    }

    public static class Renderers {
        public static void register(final FMLClientSetupEvent event) {
            ModEntities.VAULT_FIGHTER_TYPES.forEach(type -> RenderingRegistry.registerEntityRenderingHandler(type, FighterRenderer::new));
            RenderingRegistry.registerEntityRenderingHandler( ModEntities.FIGHTER, FighterRenderer::new);
            RenderingRegistry.registerEntityRenderingHandler( ModEntities.ARENA_BOSS, FighterRenderer::new);
            RenderingRegistry.registerEntityRenderingHandler( ModEntities.VAULT_GUARDIAN, VaultGuardianRenderer::new);
            RenderingRegistry.registerEntityRenderingHandler( ModEntities.ETERNAL, EternalRenderer::new);
            RenderingRegistry.registerEntityRenderingHandler( ModEntities.TREASURE_GOBLIN, TreasureGoblinRenderer::new);
            RenderingRegistry.registerEntityRenderingHandler( ModEntities.AGGRESSIVE_COW, CowRenderer::new);
            RenderingRegistry.registerEntityRenderingHandler( ModEntities.ETCHING_VENDOR, EtchingVendorRenderer::new);
            RenderingRegistry.registerEntityRenderingHandler( ModEntities.MONSTER_EYE, MonsterEyeRenderer::new);
            RenderingRegistry.registerEntityRenderingHandler( ModEntities.ROBOT, RobotRenderer::new);
            RenderingRegistry.registerEntityRenderingHandler( ModEntities.BLUE_BLAZE, BlueBlazeRenderer::new);
            RenderingRegistry.registerEntityRenderingHandler( ModEntities.BOOGIEMAN, BoogiemanRenderer::new);
            RenderingRegistry.registerEntityRenderingHandler( ModEntities.AGGRESSIVE_COW_BOSS, AggressiveCowBossRenderer::new);
            RenderingRegistry.registerEntityRenderingHandler(ModEntities.EYESORE, EyesoreRenderer::new);
            RenderingRegistry.registerEntityRenderingHandler(ModEntities.EYESTALK, EyestalkRenderer::new);
            RenderingRegistry.registerEntityRenderingHandler(ModEntities.DRILL_ARROW, TippedArrowRenderer::new);
            RenderingRegistry.registerEntityRenderingHandler(ModEntities.EFFECT_CLOUD, EffectCloudRenderer::new);
            RenderingRegistry.registerEntityRenderingHandler(ModEntities.VAULT_SAND, rm -> new ItemRenderer(rm, Minecraft.getInstance().getItemRenderer()));
            RenderingRegistry.registerEntityRenderingHandler(ModEntities.FLOATING_ITEM, rm -> new ItemRenderer(rm, Minecraft.getInstance().getItemRenderer()));
            RenderingRegistry.registerEntityRenderingHandler(ModEntities.EYESORE_FIREBALL, rm -> new SpriteRenderer(rm, Minecraft.getInstance().getItemRenderer(), 5.0f, true));
        }
    }
}
