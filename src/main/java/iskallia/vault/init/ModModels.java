package iskallia.vault.init;

import com.google.common.base.Predicates;
import iskallia.vault.Vault;
import iskallia.vault.block.CryoChamberBlock;
import iskallia.vault.config.VaultGearConfig;
import iskallia.vault.item.ItemDrillArrow;
import iskallia.vault.item.crystal.VaultCrystalItem;
import iskallia.vault.item.gear.GearModelProperties;
import iskallia.vault.item.gear.VaultGear;
import iskallia.vault.item.gear.model.*;
import iskallia.vault.item.gear.specials.*;
import iskallia.vault.util.MiscUtils;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IDyeableArmorItem;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.loading.FMLEnvironment;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class ModModels {
    public static void setupModelLoaders() {
    }

    public static void setupRenderLayers() {
        RenderTypeLookup.setRenderLayer((Block) ModBlocks.VAULT_PORTAL, RenderType.translucent());
        RenderTypeLookup.setRenderLayer((Block) ModBlocks.ISKALLIUM_DOOR, RenderType.cutout());
        RenderTypeLookup.setRenderLayer((Block) ModBlocks.GORGINITE_DOOR, RenderType.cutout());
        RenderTypeLookup.setRenderLayer((Block) ModBlocks.SPARKLETINE_DOOR, RenderType.cutout());
        RenderTypeLookup.setRenderLayer((Block) ModBlocks.ASHIUM_DOOR, RenderType.cutout());
        RenderTypeLookup.setRenderLayer((Block) ModBlocks.BOMIGNITE_DOOR, RenderType.cutout());
        RenderTypeLookup.setRenderLayer((Block) ModBlocks.FUNSOIDE_DOOR, RenderType.cutout());
        RenderTypeLookup.setRenderLayer((Block) ModBlocks.TUBIUM_DOOR, RenderType.cutout());
        RenderTypeLookup.setRenderLayer((Block) ModBlocks.UPALINE_DOOR, RenderType.cutout());
        RenderTypeLookup.setRenderLayer((Block) ModBlocks.PUFFIUM_DOOR, RenderType.cutout());
        RenderTypeLookup.setRenderLayer((Block) ModBlocks.VAULT_ALTAR, RenderType.cutout());
        RenderTypeLookup.setRenderLayer((Block) ModBlocks.VAULT_ARTIFACT, RenderType.cutout());
        RenderTypeLookup.setRenderLayer((Block) ModBlocks.KEY_PRESS, RenderType.cutout());
        RenderTypeLookup.setRenderLayer((Block) ModBlocks.OMEGA_STATUE, RenderType.cutout());
        RenderTypeLookup.setRenderLayer((Block) ModBlocks.XP_ALTAR, RenderType.translucent());
        RenderTypeLookup.setRenderLayer((Block) ModBlocks.BLOOD_ALTAR, RenderType.translucent());
        RenderTypeLookup.setRenderLayer((Block) ModBlocks.TIME_ALTAR, RenderType.translucent());
        RenderTypeLookup.setRenderLayer((Block) ModBlocks.SOUL_ALTAR, RenderType.translucent());
        RenderTypeLookup.setRenderLayer((Block) ModBlocks.VAULT_GLASS, RenderType.translucent());
        setRenderLayers(ModBlocks.VENDING_MACHINE, RenderType.cutout(), RenderType.translucent());
        setRenderLayers(ModBlocks.ADVANCED_VENDING_MACHINE, RenderType.cutout(), RenderType.translucent());
        setRenderLayers(ModBlocks.CRYO_CHAMBER, RenderType.solid(), RenderType.translucent());
        setRenderLayers(ModBlocks.VAULT_CRATE_SCAVENGER, RenderType.solid(), RenderType.translucent());
        setRenderLayers(ModBlocks.VAULT_CRATE_CAKE, RenderType.cutout());
        setRenderLayers(ModBlocks.STABILIZER, RenderType.solid(), RenderType.translucent());
        setRenderLayers(ModBlocks.RAID_CONTROLLER_BLOCK, RenderType.solid(), RenderType.translucent());
        setRenderLayers(ModBlocks.VAULT_CHARM_CONTROLLER_BLOCK, RenderType.solid(), RenderType.translucent());
    }

    private static void setRenderLayers(final Block block, final RenderType... renderTypes) {
        RenderTypeLookup.setRenderLayer(block, (Predicate) Predicates.in((Collection) Arrays.asList(renderTypes)));
    }

    public static void registerItemColors(final ItemColors colors) {
        colors.register((stack, color) -> {
            if (color > 0) {
                if (ModAttributes.GEAR_STATE.getBase(stack).orElse(VaultGear.State.UNIDENTIFIED) == VaultGear.State.UNIDENTIFIED) {
                    final String gearType = ModAttributes.GEAR_ROLL_TYPE.getBase(stack).orElse(null);
                    final VaultGearConfig.General.Roll gearRoll = ModConfigs.VAULT_GEAR.getRoll(gearType).orElse(null);
                    if (gearRoll != null) {
                        return MiscUtils.blendColors(gearRoll.getColor(), 16777215, 0.8f);
                    }
                }
                return -1;
            }
            return ((IDyeableArmorItem) stack.getItem()).getColor(stack);
        }, new IItemProvider[]{(IItemProvider) ModItems.HELMET, (IItemProvider) ModItems.CHESTPLATE, (IItemProvider) ModItems.LEGGINGS, (IItemProvider) ModItems.BOOTS});
        colors.register((stack, color) -> {
            if (color > 0) {
                if (ModAttributes.GEAR_STATE.getBase(stack).orElse(VaultGear.State.UNIDENTIFIED) == VaultGear.State.UNIDENTIFIED) {
                    final String gearType = ModAttributes.GEAR_ROLL_TYPE.getBase(stack).orElse(null);
                    final VaultGearConfig.General.Roll gearRoll = ModConfigs.VAULT_GEAR.getRoll(gearType).orElse(null);
                    if (gearRoll != null) {
                        return MiscUtils.blendColors(gearRoll.getColor(), 16777215, 0.8f);
                    }
                }
                return -1;
            }
            return ((VaultGear) stack.getItem()).getColor(stack.getItem(), stack);
        }, new IItemProvider[]{(IItemProvider) ModItems.AXE, (IItemProvider) ModItems.SWORD});
        colors.register((stack, color) -> {
            if (color > 0) {
                if (ModAttributes.GEAR_STATE.getBase(stack).orElse(VaultGear.State.UNIDENTIFIED) == VaultGear.State.UNIDENTIFIED) {
                    final String gearType = ModAttributes.GEAR_ROLL_TYPE.getBase(stack).orElse(null);
                    final VaultGearConfig.General.Roll gearRoll = ModConfigs.VAULT_GEAR.getRoll(gearType).orElse(null);
                    if (gearRoll != null) {
                        return MiscUtils.blendColors(gearRoll.getColor(), 16777215, 0.8f);
                    }
                }
                return -1;
            }
            return -1;
        }, new IItemProvider[]{(IItemProvider) ModItems.IDOL_BENEVOLENT, (IItemProvider) ModItems.IDOL_OMNISCIENT, (IItemProvider) ModItems.IDOL_TIMEKEEPER, (IItemProvider) ModItems.IDOL_MALEVOLENCE});
    }

    public static class ItemProperty {
        public static IItemPropertyGetter SPECIAL_GEAR_TEXTURE;
        public static IItemPropertyGetter GEAR_TEXTURE;
        public static IItemPropertyGetter GEAR_RARITY;
        public static IItemPropertyGetter ETCHING;
        public static IItemPropertyGetter PUZZLE_COLOR;

        public static void register() {
            registerItemProperty((Item) ModItems.SWORD, "special_texture", ItemProperty.SPECIAL_GEAR_TEXTURE);
            registerItemProperty((Item) ModItems.HELMET, "special_texture", ItemProperty.SPECIAL_GEAR_TEXTURE);
            registerItemProperty((Item) ModItems.CHESTPLATE, "special_texture", ItemProperty.SPECIAL_GEAR_TEXTURE);
            registerItemProperty((Item) ModItems.LEGGINGS, "special_texture", ItemProperty.SPECIAL_GEAR_TEXTURE);
            registerItemProperty((Item) ModItems.BOOTS, "special_texture", ItemProperty.SPECIAL_GEAR_TEXTURE);
            registerItemProperty((Item) ModItems.SWORD, "texture", ItemProperty.GEAR_TEXTURE);
            registerItemProperty((Item) ModItems.AXE, "texture", ItemProperty.GEAR_TEXTURE);
            registerItemProperty((Item) ModItems.HELMET, "texture", ItemProperty.GEAR_TEXTURE);
            registerItemProperty((Item) ModItems.CHESTPLATE, "texture", ItemProperty.GEAR_TEXTURE);
            registerItemProperty((Item) ModItems.LEGGINGS, "texture", ItemProperty.GEAR_TEXTURE);
            registerItemProperty((Item) ModItems.BOOTS, "texture", ItemProperty.GEAR_TEXTURE);
            registerItemProperty(ModItems.ETCHING, "texture", ItemProperty.GEAR_TEXTURE);
            registerItemProperty(ModItems.IDOL_BENEVOLENT, "texture", ItemProperty.GEAR_TEXTURE);
            registerItemProperty(ModItems.IDOL_OMNISCIENT, "texture", ItemProperty.GEAR_TEXTURE);
            registerItemProperty(ModItems.IDOL_TIMEKEEPER, "texture", ItemProperty.GEAR_TEXTURE);
            registerItemProperty(ModItems.IDOL_MALEVOLENCE, "texture", ItemProperty.GEAR_TEXTURE);
            registerItemProperty((Item) ModItems.SWORD, "vault_rarity", ItemProperty.GEAR_RARITY);
            registerItemProperty((Item) ModItems.AXE, "vault_rarity", ItemProperty.GEAR_RARITY);
            registerItemProperty((Item) ModItems.HELMET, "vault_rarity", ItemProperty.GEAR_RARITY);
            registerItemProperty((Item) ModItems.CHESTPLATE, "vault_rarity", ItemProperty.GEAR_RARITY);
            registerItemProperty((Item) ModItems.LEGGINGS, "vault_rarity", ItemProperty.GEAR_RARITY);
            registerItemProperty((Item) ModItems.BOOTS, "vault_rarity", ItemProperty.GEAR_RARITY);
            registerItemProperty(ModItems.ETCHING, "vault_rarity", ItemProperty.GEAR_RARITY);
            registerItemProperty(ModItems.IDOL_BENEVOLENT, "vault_rarity", ItemProperty.GEAR_TEXTURE);
            registerItemProperty(ModItems.IDOL_OMNISCIENT, "vault_rarity", ItemProperty.GEAR_TEXTURE);
            registerItemProperty(ModItems.IDOL_TIMEKEEPER, "vault_rarity", ItemProperty.GEAR_TEXTURE);
            registerItemProperty(ModItems.IDOL_MALEVOLENCE, "vault_rarity", ItemProperty.GEAR_TEXTURE);
            registerItemProperty(ModItems.ETCHING, "vault_set", ItemProperty.ETCHING);
            registerItemProperty(ModItems.PUZZLE_RUNE, "puzzle_color", ItemProperty.PUZZLE_COLOR);
            registerItemProperty((Item) ModBlocks.PUZZLE_RUNE_BLOCK_ITEM, "puzzle_color", ItemProperty.PUZZLE_COLOR);
            ItemModelsProperties.register((Item) ModItems.DRILL_ARROW, new ResourceLocation("tier"), (stack, world, entity) -> ItemDrillArrow.getArrowTier(stack).ordinal() / (float) ItemDrillArrow.ArrowTier.values().length);
            ItemModelsProperties.register(Item.byBlock((Block) ModBlocks.CRYO_CHAMBER), new ResourceLocation("type"), (stack, world, entity) -> stack.getDamageValue() / (float) CryoChamberBlock.ChamberState.values().length);
            ItemModelsProperties.register((Item) ModItems.VAULT_CRYSTAL, new ResourceLocation("type"), (stack, world, entity) -> (float) VaultCrystalItem.getData(stack).getType().ordinal());
        }

        public static void registerItemProperty(final Item item, final String name, final IItemPropertyGetter property) {
            ItemModelsProperties.register(item, Vault.id(name), property);
        }

        static {
            ItemProperty.SPECIAL_GEAR_TEXTURE = ((stack, world, entity) -> ModAttributes.GEAR_SPECIAL_MODEL.getOrDefault(stack, -1).getValue(stack));
            ItemProperty.GEAR_TEXTURE = ((stack, world, entity) -> ModAttributes.GEAR_MODEL.getOrDefault(stack, -1).getValue(stack));
            ItemProperty.GEAR_RARITY = ((stack, world, entity) -> ModAttributes.GEAR_RARITY.get(stack).map(attribute -> attribute.getValue(stack)).map(Enum::ordinal).orElse(-1));
            ItemProperty.ETCHING = ((stack, world, entity) -> ModAttributes.GEAR_SET.get(stack).map(attribute -> attribute.getValue(stack)).map(Enum::ordinal).orElse(-1));
            ItemProperty.PUZZLE_COLOR = ((stack, world, entity) -> ModAttributes.PUZZLE_COLOR.get(stack).map(attribute -> attribute.getValue(stack)).map(Enum::ordinal).orElse(-1));
        }
    }

    public static class SpecialSwordModel {
        public static Map<Integer, SpecialSwordModel> REGISTRY;
        public static SpecialSwordModel JANITORS_BROOM;
        int id;
        String displayName;
        GearModelProperties modelProperties;

        public SpecialSwordModel() {
            this.modelProperties = new GearModelProperties();
        }

        public static SpecialSwordModel getModel(final int id) {
            return SpecialSwordModel.REGISTRY.get(id);
        }

        public static void register() {
            SpecialSwordModel.REGISTRY = new HashMap<Integer, SpecialSwordModel>();
            SpecialSwordModel.JANITORS_BROOM = register("Janitor's Broom", new GearModelProperties().allowTransmogrification());
        }

        public int getId() {
            return this.id;
        }

        public String getDisplayName() {
            return this.displayName;
        }

        public GearModelProperties getModelProperties() {
            return this.modelProperties;
        }

        private static SpecialSwordModel register(final String displayName) {
            final SpecialSwordModel swordModel = new SpecialSwordModel();
            swordModel.displayName = displayName;
            swordModel.id = SpecialSwordModel.REGISTRY.size();
            SpecialSwordModel.REGISTRY.put(swordModel.id, swordModel);
            return swordModel;
        }

        private static SpecialSwordModel register(final String displayName, final GearModelProperties modelProperties) {
            final SpecialSwordModel swordModel = register(displayName);
            swordModel.modelProperties = modelProperties;
            return swordModel;
        }
    }

    public static class GearModel {
        public static Map<Integer, GearModel> SCRAPPY_REGISTRY;
        public static Map<Integer, GearModel> REGISTRY;
        public static GearModel SCRAPPY_1;
        public static GearModel SCRAPPY_2;
        public static GearModel SCRAPPY_3;
        public static GearModel SCALE_1;
        public static GearModel SCALE_2;
        public static GearModel SCALE_3;
        public static GearModel SCALE_4;
        public static GearModel PLATED_1;
        public static GearModel PLATED_1_DARK;
        public static GearModel PLATED_2;
        public static GearModel PLATED_2_DARK;
        public static GearModel PLATED_3;
        public static GearModel PLATED_3_DARK;
        public static GearModel PLATED_4;
        public static GearModel PLATED_4_DARK;
        public static GearModel FUR_1;
        public static GearModel FUR_2;
        public static GearModel FUR_3;
        public static GearModel FUR_4;
        public static GearModel CLOAK_1;
        public static GearModel CLOAK_2;
        public static GearModel CLOAK_3;
        public static GearModel CLOAK_4;
        public static GearModel ROYAL_1;
        public static GearModel ROYAL_2;
        public static GearModel SCRAPPY_1_NORMAL;
        public static GearModel SCRAPPY_2_NORMAL;
        public static GearModel SCRAPPY_3_NORMAL;
        public static GearModel BARBARIAN_1;
        public static GearModel BARBARIAN_2;
        public static GearModel BARBARIAN_3;
        public static GearModel ROYAL_1_DARK;
        public static GearModel BARBARIAN_1_DARK;
        public static GearModel BARBARIAN_2_DARK;
        public static GearModel BARBARIAN_3_DARK;
        public static GearModel OMARLATIF;
        public static GearModel SCUBA_1;
        public static GearModel LEPRECHAUN_1;
        public static GearModel BONE_1;
        public static GearModel JAWBONE_1;
        public static GearModel REVENANT_1;
        public static GearModel REVENANT_2;
        public static GearModel KNIGHT_1;
        public static GearModel KNIGHT_2;
        public static GearModel KNIGHT_3;
        public static GearModel DEVIL_DUCK_1;
        public static GearModel ANGEL_1;
        public static GearModel DEVIL_1;
        int id;
        String displayName;
        VaultGearModel<? extends LivingEntity> helmetModel;
        VaultGearModel<? extends LivingEntity> chestplateModel;
        VaultGearModel<? extends LivingEntity> leggingsModel;
        VaultGearModel<? extends LivingEntity> bootsModel;

        public static void register() {
            GearModel.SCRAPPY_REGISTRY = new HashMap<Integer, GearModel>();
            GearModel.REGISTRY = new HashMap<Integer, GearModel>();
            GearModel.SCRAPPY_1 = registerScrappy("Scrappy 1", () -> ScrappyArmorModel.Variant1.class);
            GearModel.SCRAPPY_2 = registerScrappy("Scrappy 2", () -> ScrappyArmorModel.Variant2.class);
            GearModel.SCRAPPY_3 = registerScrappy("Scrappy 3", () -> ScrappyArmorModel.Variant3.class);
            GearModel.SCALE_1 = register("Scale 1", () -> ScaleArmorModel.Variant1.class);
            GearModel.SCALE_2 = register("Scale 2", () -> ScaleArmorModel.Variant2.class);
            GearModel.SCALE_3 = register("Scale 3", () -> ScaleArmorModel.Variant3.class);
            GearModel.SCALE_4 = register("Scale 4", () -> ScaleArmorModel.Variant4.class);
            GearModel.PLATED_1 = register("Plated 1", () -> PlatedArmorModel.Variant1.class);
            GearModel.PLATED_1_DARK = register("Plated 1 Dark", () -> PlatedArmorModel.Variant1.class);
            GearModel.PLATED_2 = register("Plated 2", () -> PlatedArmorModel.Variant2.class);
            GearModel.PLATED_2_DARK = register("Plated 2 Dark", () -> PlatedArmorModel.Variant2.class);
            GearModel.PLATED_3 = register("Plated 3", () -> PlatedArmorModel.Variant3.class);
            GearModel.PLATED_3_DARK = register("Plated 3 Dark", () -> PlatedArmorModel.Variant3.class);
            GearModel.PLATED_4 = register("Plated 4", () -> PlatedArmorModel.Variant4.class);
            GearModel.PLATED_4_DARK = register("Plated 4 Dark", () -> PlatedArmorModel.Variant4.class);
            GearModel.FUR_1 = register("Fur 1", () -> FurArmorModel.Variant1.class);
            GearModel.FUR_2 = register("Fur 2", () -> FurArmorModel.Variant2.class);
            GearModel.FUR_3 = register("Fur 3", () -> FurArmorModel.Variant3.class);
            GearModel.FUR_4 = register("Fur 4", () -> FurArmorModel.Variant4.class);
            GearModel.CLOAK_1 = register("Cloak 1", () -> CloakArmorModel.Variant1.class);
            GearModel.CLOAK_2 = register("Cloak 2", () -> CloakArmorModel.Variant2.class);
            GearModel.CLOAK_3 = register("Cloak 3", () -> CloakArmorModel.Variant3.class);
            GearModel.CLOAK_4 = register("Cloak 4", () -> CloakArmorModel.Variant4.class);
            GearModel.ROYAL_1 = register("Royal 1", () -> RoyalArmorModel.Variant1.class);
            GearModel.ROYAL_2 = register("Royal 2", () -> RoyalArmorModel.Variant2.class);
            GearModel.SCRAPPY_1_NORMAL = register("Scrappy 1", () -> ScrappyArmorModel.Variant1.class);
            GearModel.SCRAPPY_2_NORMAL = register("Scrappy 2", () -> ScrappyArmorModel.Variant2.class);
            GearModel.SCRAPPY_3_NORMAL = register("Scrappy 3", () -> ScrappyArmorModel.Variant3.class);
            GearModel.BARBARIAN_1 = register("Barbarian 1", () -> BarbarianArmorModel.Variant1.class);
            GearModel.BARBARIAN_2 = register("Barbarian 2", () -> BarbarianArmorModel.Variant2.class);
            GearModel.BARBARIAN_3 = register("Barbarian 3", () -> BarbarianArmorModel.Variant3.class);
            GearModel.ROYAL_1_DARK = register("Royal 1 Dark", () -> RoyalArmorModel.Variant1.class);
            GearModel.BARBARIAN_1_DARK = register("Barbarian 1 Dark", () -> BarbarianArmorModel.Variant1.class);
            GearModel.BARBARIAN_2_DARK = register("Barbarian 2 Dark", () -> BarbarianArmorModel.Variant2.class);
            GearModel.BARBARIAN_3_DARK = register("Barbarian 3 Dark", () -> BarbarianArmorModel.Variant3.class);
            GearModel.OMARLATIF = register("Omarlatif", () -> OmarlatifArmorModel.class);
            GearModel.SCUBA_1 = register("Scuba 1", () -> ScubaArmorModel.Variant1.class);
            GearModel.LEPRECHAUN_1 = register("Leprechaun 1", () -> LeprechaunArmorModel.Variant1.class);
            GearModel.BONE_1 = register("Bone 1", () -> BoneArmorModel.Variant1.class);
            GearModel.JAWBONE_1 = register("Jawbone 1", () -> JawboneArmorModel.Variant1.class);
            GearModel.REVENANT_1 = register("Revenant 1", () -> RevenantArmorModel.Variant1.class);
            GearModel.REVENANT_2 = register("Revenant 2", () -> RevenantArmorModel.Variant2.class);
            GearModel.KNIGHT_1 = register("Knight 1", () -> KnightArmorModel.Variant1.class);
            GearModel.KNIGHT_2 = register("Knight 2", () -> KnightArmorModel.Variant2.class);
            GearModel.KNIGHT_3 = register("Knight 3", () -> KnightArmorModel.Variant3.class);
            GearModel.DEVIL_DUCK_1 = register("DevilDuck 1", () -> DevilDuckArmorModel.Variant1.class);
            GearModel.ANGEL_1 = register("Angel 1", () -> AngelArmorModel.Variant1.class);
            GearModel.DEVIL_1 = register("Devil 1", () -> DevilArmorModel.Variant1.class);
        }

        public VaultGearModel<? extends LivingEntity> forSlotType(final EquipmentSlotType slotType) {
            switch (slotType) {
                case HEAD: {
                    return this.helmetModel;
                }
                case CHEST: {
                    return this.chestplateModel;
                }
                case LEGS: {
                    return this.leggingsModel;
                }
                default: {
                    return this.bootsModel;
                }
            }
        }

        public int getId() {
            return this.id;
        }

        public String getDisplayName() {
            return this.displayName;
        }

        public String getTextureName(final EquipmentSlotType slotType, final String type) {
            final String base = Vault.sId("textures/models/armor/" + this.displayName.toLowerCase().replace(" ", "_") + "_armor") + ((slotType == EquipmentSlotType.LEGS) ? "_layer2" : "_layer1");
            return ((type == null) ? base : (base + "_" + type)) + ".png";
        }

        private static <T extends VaultGearModel<?>> GearModel registerScrappy(final String textureName, final Supplier<Class<T>> modelClassSupplier) {
            return register(textureName, modelClassSupplier, GearModel.SCRAPPY_REGISTRY);
        }

        private static <T extends VaultGearModel<?>> GearModel register(final String textureName, final Supplier<Class<T>> modelClassSupplier) {
            return register(textureName, modelClassSupplier, GearModel.REGISTRY);
        }

        private static <T extends VaultGearModel<?>> GearModel register(final String textureName, final Supplier<Class<T>> modelClassSupplier, final Map<Integer, GearModel> registry) {
            try {
                final GearModel gearModel = new GearModel();
                gearModel.displayName = textureName;
                gearModel.id = registry.size();
                if (FMLEnvironment.dist.isClient()) {
                    final Class<T> modelClass = modelClassSupplier.get();
                    assert modelClass != null;
                    final Constructor<T> constructor = modelClass.getConstructor(Float.TYPE, EquipmentSlotType.class);
                    final T helmetModel = constructor.newInstance(1.0f, EquipmentSlotType.HEAD);
                    final T chestplateModel = constructor.newInstance(1.0f, EquipmentSlotType.CHEST);
                    final T leggingsModel = constructor.newInstance(1.0f, EquipmentSlotType.LEGS);
                    final T bootsModel = constructor.newInstance(1.0f, EquipmentSlotType.FEET);
                    gearModel.helmetModel = (VaultGearModel<? extends LivingEntity>) helmetModel;
                    gearModel.chestplateModel = (VaultGearModel<? extends LivingEntity>) chestplateModel;
                    gearModel.leggingsModel = (VaultGearModel<? extends LivingEntity>) leggingsModel;
                    gearModel.bootsModel = (VaultGearModel<? extends LivingEntity>) bootsModel;
                }
                registry.put(gearModel.id, gearModel);
                return gearModel;
            } catch (final Exception e) {
                throw new InternalError("Error while registering Gear Model: " + textureName);
            }
        }
    }

    public static class SpecialGearModel {
        public static Map<Integer, SpecialGearModel> HEAD_REGISTRY;
        public static Map<Integer, SpecialGearModel> CHESTPLATE_REGISTRY;
        public static Map<Integer, SpecialGearModel> LEGGINGS_REGISTRY;
        public static Map<Integer, SpecialGearModel> BOOTS_REGISTRY;
        public static SpecialGearModel CHEESE_HAT;
        public static SpecialGearModel ISKALL_HOLOLENS;
        public static SpecialGearModelSet HELLCOW_SET;
        public static SpecialGearModelSet BOTANIA_SET;
        public static SpecialGearModelSet CREATE_SET;
        public static SpecialGearModelSet DANK_SET;
        public static SpecialGearModelSet FLUX_SET;
        public static SpecialGearModelSet IMMERSIVE_ENGINEERING_SET;
        public static SpecialGearModelSet MEKA_SET_LIGHT;
        public static SpecialGearModelSet MEKA_SET_DARK;
        public static SpecialGearModelSet POWAH_SET;
        public static SpecialGearModelSet THERMAL_SET;
        public static SpecialGearModelSet TRASH_SET;
        public static SpecialGearModelSet SKALLIBOMBA_SET;
        public static SpecialGearModelSet VILLAGER_SET;
        public static SpecialGearModelSet AUTOMATIC_SET;
        public static SpecialGearModelSet FAIRY_SET;
        public static SpecialGearModelSet BUILDING_SET;
        public static SpecialGearModelSet ZOMBIE_SET;
        public static SpecialGearModelSet XNET_SET;
        public static SpecialGearModelSet TEST_DUMMY_SET;
        public static SpecialGearModelSet INDUSTRIAL_FOREGOING_SET;
        public static SpecialGearModelSet CAKE_SET;
        int id;
        String displayName;
        VaultGearModel<? extends LivingEntity> model;
        GearModelProperties modelProperties;

        public SpecialGearModel() {
            this.modelProperties = new GearModelProperties();
        }

        public static Map<Integer, SpecialGearModel> getRegistryForSlot(final EquipmentSlotType slotType) {
            switch (slotType) {
                case HEAD: {
                    return SpecialGearModel.HEAD_REGISTRY;
                }
                case CHEST: {
                    return SpecialGearModel.CHESTPLATE_REGISTRY;
                }
                case LEGS: {
                    return SpecialGearModel.LEGGINGS_REGISTRY;
                }
                default: {
                    return SpecialGearModel.BOOTS_REGISTRY;
                }
            }
        }

        public static SpecialGearModel getModel(final EquipmentSlotType slotType, final int id) {
            final Map<Integer, SpecialGearModel> registry = getRegistryForSlot(slotType);
            if (registry == null) {
                return null;
            }
            return registry.get(id);
        }

        public static void register() {
            SpecialGearModel.HEAD_REGISTRY = new HashMap<Integer, SpecialGearModel>();
            SpecialGearModel.CHESTPLATE_REGISTRY = new HashMap<Integer, SpecialGearModel>();
            SpecialGearModel.LEGGINGS_REGISTRY = new HashMap<Integer, SpecialGearModel>();
            SpecialGearModel.BOOTS_REGISTRY = new HashMap<Integer, SpecialGearModel>();
            SpecialGearModel.CHEESE_HAT = registerHead("Cheese Hat", () -> CheeseHatModel.class);
            SpecialGearModel.ISKALL_HOLOLENS = registerHead("Iskall Hololens", () -> IskallHololensModel.class);
            SpecialGearModel.HELLCOW_SET = registerSet("Hellcow", () -> HellcowArmorModel.class, new GearModelProperties().allowTransmogrification());
            SpecialGearModel.BOTANIA_SET = registerSet("Botania", () -> BotaniaArmorModel.class, new GearModelProperties().allowTransmogrification());
            SpecialGearModel.CREATE_SET = registerSet("Create", () -> CreateArmorModel.class, new GearModelProperties().allowTransmogrification());
            SpecialGearModel.DANK_SET = registerSet("Dank", () -> DankArmorModel.class, new GearModelProperties().allowTransmogrification());
            SpecialGearModel.FLUX_SET = registerSet("Flux", () -> FluxArmorModel.class, new GearModelProperties().allowTransmogrification());
            SpecialGearModel.IMMERSIVE_ENGINEERING_SET = registerSet("Immersive Engineering", () -> ImmersiveEngineeringArmorModel.class, new GearModelProperties().allowTransmogrification());
            SpecialGearModel.MEKA_SET_LIGHT = registerSet("Meka Light", () -> MekaArmorModel.class, new GearModelProperties().allowTransmogrification());
            SpecialGearModel.MEKA_SET_DARK = registerSet("Meka Dark", () -> MekaArmorModel.class, new GearModelProperties().allowTransmogrification());
            SpecialGearModel.POWAH_SET = registerSet("Powah", () -> PowahArmorModel.class, new GearModelProperties().allowTransmogrification());
            SpecialGearModel.THERMAL_SET = registerSet("Thermal", () -> ThermalArmorModel.class, new GearModelProperties().allowTransmogrification());
            SpecialGearModel.TRASH_SET = registerSet("Trash", () -> TrashArmorModel.class, new GearModelProperties().allowTransmogrification());
            SpecialGearModel.SKALLIBOMBA_SET = registerSet("Skallibomba", () -> SkallibombaArmorModel.class, new GearModelProperties().allowTransmogrification());
            SpecialGearModel.VILLAGER_SET = registerSet("Villager", () -> VillagerArmorModel.class, new GearModelProperties().allowTransmogrification());
            SpecialGearModel.AUTOMATIC_SET = registerSet("Automatic", () -> AutomaticArmorModel.class, new GearModelProperties().allowTransmogrification());
            SpecialGearModel.FAIRY_SET = registerSet("Fairy", () -> FairyArmorModel.class, new GearModelProperties().allowTransmogrification());
            SpecialGearModel.BUILDING_SET = registerSet("Building", () -> BuildingArmorModel.class, new GearModelProperties().allowTransmogrification());
            SpecialGearModel.ZOMBIE_SET = registerSet("Zombie", () -> ZombieArmorModel.class, new GearModelProperties().allowTransmogrification());
            SpecialGearModel.XNET_SET = registerSet("Xnet", () -> XnetArmorModel.class, new GearModelProperties().allowTransmogrification());
            SpecialGearModel.TEST_DUMMY_SET = registerSet("Test Dummy", () -> TestDummyArmorModel.class, new GearModelProperties().allowTransmogrification());
            SpecialGearModel.INDUSTRIAL_FOREGOING_SET = registerSet("Industrial Foregoing", () -> IndustrialForegoingArmorModel.class, new GearModelProperties().allowTransmogrification());
            SpecialGearModel.CAKE_SET = registerSet("Cake", () -> CakeArmorModel.class, new GearModelProperties().allowTransmogrification());
        }

        public int getId() {
            return this.id;
        }

        public String getDisplayName() {
            return this.displayName;
        }

        public VaultGearModel<? extends LivingEntity> getModel() {
            return this.model;
        }

        public String getTextureName(final EquipmentSlotType slotType, final String type) {
            if (this.modelProperties.isPieceOfSet()) {
                final String base = Vault.sId("textures/models/armor/special/" + this.displayName.toLowerCase().replace(" ", "_") + "_armor") + ((slotType == EquipmentSlotType.LEGS) ? "_layer2" : "_layer1");
                return ((type == null) ? base : (base + "_" + type)) + ".png";
            }
            final String base = Vault.sId("textures/models/armor/special/" + this.displayName.toLowerCase().replace(" ", "_"));
            return ((type == null) ? base : (base + "_" + type)) + ".png";
        }

        public GearModelProperties getModelProperties() {
            return this.modelProperties;
        }

        private static <T extends VaultGearModel<?>> SpecialGearModelSet registerSet(final String textureName, final Supplier<Class<T>> modelClassSupplier) {
            return registerSet(textureName, modelClassSupplier, new GearModelProperties());
        }

        private static <T extends VaultGearModel<?>> SpecialGearModelSet registerSet(final String textureName, final Supplier<Class<T>> modelClassSupplier, final GearModelProperties modelProperties) {
            final SpecialGearModelSet set = new SpecialGearModelSet();
            modelProperties.makePieceOfSet();
            set.head = registerHead(textureName, modelClassSupplier, modelProperties);
            set.chestplate = registerChestplate(textureName, modelClassSupplier, modelProperties);
            set.leggings = registerLeggings(textureName, modelClassSupplier, modelProperties);
            set.boots = registerBoots(textureName, modelClassSupplier, modelProperties);
            return set;
        }

        private static <T extends VaultGearModel<?>> SpecialGearModel registerHead(final String textureName, final Supplier<Class<T>> modelClassSupplier) {
            return registerHead(textureName, modelClassSupplier, new GearModelProperties());
        }

        private static <T extends VaultGearModel<?>> SpecialGearModel registerHead(final String textureName, final Supplier<Class<T>> modelClassSupplier, final GearModelProperties modelProperties) {
            return register(textureName, modelClassSupplier, modelProperties, EquipmentSlotType.HEAD, SpecialGearModel.HEAD_REGISTRY);
        }

        private static <T extends VaultGearModel<?>> SpecialGearModel registerChestplate(final String textureName, final Supplier<Class<T>> modelClassSupplier) {
            return registerChestplate(textureName, modelClassSupplier, new GearModelProperties());
        }

        private static <T extends VaultGearModel<?>> SpecialGearModel registerChestplate(final String textureName, final Supplier<Class<T>> modelClassSupplier, final GearModelProperties modelProperties) {
            return register(textureName, modelClassSupplier, modelProperties, EquipmentSlotType.CHEST, SpecialGearModel.CHESTPLATE_REGISTRY);
        }

        private static <T extends VaultGearModel<?>> SpecialGearModel registerLeggings(final String textureName, final Supplier<Class<T>> modelClassSupplier) {
            return registerLeggings(textureName, modelClassSupplier, new GearModelProperties());
        }

        private static <T extends VaultGearModel<?>> SpecialGearModel registerLeggings(final String textureName, final Supplier<Class<T>> modelClassSupplier, final GearModelProperties modelProperties) {
            return register(textureName, modelClassSupplier, modelProperties, EquipmentSlotType.LEGS, SpecialGearModel.LEGGINGS_REGISTRY);
        }

        private static <T extends VaultGearModel<?>> SpecialGearModel registerBoots(final String textureName, final Supplier<Class<T>> modelClassSupplier) {
            return registerBoots(textureName, modelClassSupplier, new GearModelProperties());
        }

        private static <T extends VaultGearModel<?>> SpecialGearModel registerBoots(final String textureName, final Supplier<Class<T>> modelClassSupplier, final GearModelProperties modelProperties) {
            return register(textureName, modelClassSupplier, modelProperties, EquipmentSlotType.FEET, SpecialGearModel.BOOTS_REGISTRY);
        }

        private static <T extends VaultGearModel<?>> SpecialGearModel register(final String textureName, final Supplier<Class<T>> modelClassSupplier, final GearModelProperties modelProperties, final EquipmentSlotType slotType, final Map<Integer, SpecialGearModel> registry) {
            try {
                final SpecialGearModel specialGearModel = new SpecialGearModel();
                specialGearModel.displayName = textureName;
                specialGearModel.id = registry.size();
                specialGearModel.modelProperties = modelProperties;
                if (FMLEnvironment.dist.isClient()) {
                    final Class<T> modelClass = modelClassSupplier.get();
                    final Constructor<T> constructor = modelClass.getConstructor(Float.TYPE, EquipmentSlotType.class);
                    specialGearModel.model = (VaultGearModel<? extends LivingEntity>) constructor.newInstance(1.0f, slotType);
                }
                registry.put(specialGearModel.id, specialGearModel);
                return specialGearModel;
            } catch (final Exception e) {
                throw new InternalError("Error while registering Special Gear Model: " + textureName);
            }
        }

        public static class SpecialGearModelSet {
            public SpecialGearModel head;
            public SpecialGearModel chestplate;
            public SpecialGearModel leggings;
            public SpecialGearModel boots;

            public SpecialGearModel modelForSlot(final EquipmentSlotType slot) {
                if (slot == EquipmentSlotType.HEAD) {
                    return this.head;
                }
                if (slot == EquipmentSlotType.CHEST) {
                    return this.chestplate;
                }
                if (slot == EquipmentSlotType.LEGS) {
                    return this.leggings;
                }
                return this.boots;
            }
        }
    }
}
