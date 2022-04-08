package iskallia.vault.init;

import com.google.common.base.Predicates;
import iskallia.vault.Vault;
import iskallia.vault.attribute.IntegerAttribute;
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
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class ModModels {
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

        setRenderLayers((Block) ModBlocks.VENDING_MACHINE, new RenderType[]{RenderType.cutout(), RenderType.translucent()});
        setRenderLayers((Block) ModBlocks.ADVANCED_VENDING_MACHINE, new RenderType[]{RenderType.cutout(), RenderType.translucent()});
        setRenderLayers((Block) ModBlocks.CRYO_CHAMBER, new RenderType[]{RenderType.solid(), RenderType.translucent()});
        setRenderLayers((Block) ModBlocks.VAULT_CRATE_SCAVENGER, new RenderType[]{RenderType.solid(), RenderType.translucent()});
        setRenderLayers((Block) ModBlocks.VAULT_CRATE_CAKE, new RenderType[]{RenderType.cutout()});
        setRenderLayers((Block) ModBlocks.STABILIZER, new RenderType[]{RenderType.solid(), RenderType.translucent()});
        setRenderLayers((Block) ModBlocks.RAID_CONTROLLER_BLOCK, new RenderType[]{RenderType.solid(), RenderType.translucent()});
        setRenderLayers((Block) ModBlocks.VAULT_CHARM_CONTROLLER_BLOCK, new RenderType[]{RenderType.solid(), RenderType.translucent()});
    }

    public static void setupModelLoaders() {
    }

    private static void setRenderLayers(Block block, RenderType... renderTypes) {
        RenderTypeLookup.setRenderLayer(block, (Predicate) Predicates.in(Arrays.asList(renderTypes)));
    }

    public static void registerItemColors(ItemColors colors) {
        colors.register((stack, color) -> {
                    if (color > 0) {
                        if (ModAttributes.GEAR_STATE.getBase(stack).orElse(VaultGear.State.UNIDENTIFIED) == VaultGear.State.UNIDENTIFIED) {
                            String gearType = ModAttributes.GEAR_ROLL_TYPE.getBase(stack).orElse(null);
                            VaultGearConfig.General.Roll gearRoll = ModConfigs.VAULT_GEAR.getRoll(gearType).orElse(null);
                            if (gearRoll != null) return MiscUtils.blendColors(gearRoll.getColor(), 16777215, 0.8F);
                        }
                        return -1;
                    }
                    return ((IDyeableArmorItem) stack.getItem()).getColor(stack);
                },
                new IItemProvider[]{(IItemProvider) ModItems.HELMET, (IItemProvider) ModItems.CHESTPLATE, (IItemProvider) ModItems.LEGGINGS, (IItemProvider) ModItems.BOOTS})
        ;


        colors.register((stack, color) -> {
            if (color > 0) {
                if (ModAttributes.GEAR_STATE.getBase(stack).orElse(VaultGear.State.UNIDENTIFIED) == VaultGear.State.UNIDENTIFIED) {
                    String gearType = ModAttributes.GEAR_ROLL_TYPE.getBase(stack).orElse(null);
                    VaultGearConfig.General.Roll gearRoll = ModConfigs.VAULT_GEAR.getRoll(gearType).orElse(null);
                    if (gearRoll != null) return MiscUtils.blendColors(gearRoll.getColor(), 16777215, 0.8F);
                }
                return -1;
            }
            return ((VaultGear) stack.getItem()).getColor(stack.getItem(), stack);
        }, new IItemProvider[]{(IItemProvider) ModItems.AXE, (IItemProvider) ModItems.SWORD});


        colors.register((stack, color) -> {
                    if (color > 0) {
                        if (ModAttributes.GEAR_STATE.getBase(stack).orElse(VaultGear.State.UNIDENTIFIED) == VaultGear.State.UNIDENTIFIED) {
                            String gearType = ModAttributes.GEAR_ROLL_TYPE.getBase(stack).orElse(null);
                            VaultGearConfig.General.Roll gearRoll = ModConfigs.VAULT_GEAR.getRoll(gearType).orElse(null);
                            if (gearRoll != null) return MiscUtils.blendColors(gearRoll.getColor(), 16777215, 0.8F);
                        }
                        return -1;
                    }
                    return -1;
                },
                new IItemProvider[]{(IItemProvider) ModItems.IDOL_BENEVOLENT, (IItemProvider) ModItems.IDOL_OMNISCIENT, (IItemProvider) ModItems.IDOL_TIMEKEEPER, (IItemProvider) ModItems.IDOL_MALEVOLENCE})
        ;
    }


    public static class ItemProperty {
        public static IItemPropertyGetter SPECIAL_GEAR_TEXTURE;

        public static IItemPropertyGetter GEAR_TEXTURE;

        public static IItemPropertyGetter GEAR_RARITY;

        public static IItemPropertyGetter ETCHING;

        public static IItemPropertyGetter PUZZLE_COLOR;

        static {
            SPECIAL_GEAR_TEXTURE = ((stack, world, entity) -> ((Integer) ((IntegerAttribute) ModAttributes.GEAR_SPECIAL_MODEL.getOrDefault(stack, Integer.valueOf(-1))).getValue(stack)).intValue());


            GEAR_TEXTURE = ((stack, world, entity) -> ((Integer) ((IntegerAttribute) ModAttributes.GEAR_MODEL.getOrDefault(stack, Integer.valueOf(-1))).getValue(stack)).intValue());


            GEAR_RARITY = ((stack, world, entity) -> (Integer) ModAttributes.GEAR_RARITY.get(stack).map(()).map(Enum::ordinal).orElse(Integer.valueOf(-1)));


            ETCHING = ((stack, world, entity) -> (Integer) ModAttributes.GEAR_SET.get(stack).map(()).map(Enum::ordinal).orElse(Integer.valueOf(-1)));


            PUZZLE_COLOR = ((stack, world, entity) -> (Integer) ModAttributes.PUZZLE_COLOR.get(stack).map(()).map(Enum::ordinal).orElse(Integer.valueOf(-1)));
        }


        public static void register() {
            registerItemProperty((Item) ModItems.SWORD, "special_texture", SPECIAL_GEAR_TEXTURE);
            registerItemProperty((Item) ModItems.HELMET, "special_texture", SPECIAL_GEAR_TEXTURE);
            registerItemProperty((Item) ModItems.CHESTPLATE, "special_texture", SPECIAL_GEAR_TEXTURE);
            registerItemProperty((Item) ModItems.LEGGINGS, "special_texture", SPECIAL_GEAR_TEXTURE);
            registerItemProperty((Item) ModItems.BOOTS, "special_texture", SPECIAL_GEAR_TEXTURE);

            registerItemProperty((Item) ModItems.SWORD, "texture", GEAR_TEXTURE);
            registerItemProperty((Item) ModItems.AXE, "texture", GEAR_TEXTURE);
            registerItemProperty((Item) ModItems.HELMET, "texture", GEAR_TEXTURE);
            registerItemProperty((Item) ModItems.CHESTPLATE, "texture", GEAR_TEXTURE);
            registerItemProperty((Item) ModItems.LEGGINGS, "texture", GEAR_TEXTURE);
            registerItemProperty((Item) ModItems.BOOTS, "texture", GEAR_TEXTURE);
            registerItemProperty((Item) ModItems.ETCHING, "texture", GEAR_TEXTURE);
            registerItemProperty((Item) ModItems.IDOL_BENEVOLENT, "texture", GEAR_TEXTURE);
            registerItemProperty((Item) ModItems.IDOL_OMNISCIENT, "texture", GEAR_TEXTURE);
            registerItemProperty((Item) ModItems.IDOL_TIMEKEEPER, "texture", GEAR_TEXTURE);
            registerItemProperty((Item) ModItems.IDOL_MALEVOLENCE, "texture", GEAR_TEXTURE);

            registerItemProperty((Item) ModItems.SWORD, "vault_rarity", GEAR_RARITY);
            registerItemProperty((Item) ModItems.AXE, "vault_rarity", GEAR_RARITY);
            registerItemProperty((Item) ModItems.HELMET, "vault_rarity", GEAR_RARITY);
            registerItemProperty((Item) ModItems.CHESTPLATE, "vault_rarity", GEAR_RARITY);
            registerItemProperty((Item) ModItems.LEGGINGS, "vault_rarity", GEAR_RARITY);
            registerItemProperty((Item) ModItems.BOOTS, "vault_rarity", GEAR_RARITY);
            registerItemProperty((Item) ModItems.ETCHING, "vault_rarity", GEAR_RARITY);
            registerItemProperty((Item) ModItems.IDOL_BENEVOLENT, "vault_rarity", GEAR_TEXTURE);
            registerItemProperty((Item) ModItems.IDOL_OMNISCIENT, "vault_rarity", GEAR_TEXTURE);
            registerItemProperty((Item) ModItems.IDOL_TIMEKEEPER, "vault_rarity", GEAR_TEXTURE);
            registerItemProperty((Item) ModItems.IDOL_MALEVOLENCE, "vault_rarity", GEAR_TEXTURE);

            registerItemProperty((Item) ModItems.ETCHING, "vault_set", ETCHING);

            registerItemProperty((Item) ModItems.PUZZLE_RUNE, "puzzle_color", PUZZLE_COLOR);
            registerItemProperty((Item) ModBlocks.PUZZLE_RUNE_BLOCK_ITEM, "puzzle_color", PUZZLE_COLOR);

            ItemModelsProperties.register((Item) ModItems.DRILL_ARROW, new ResourceLocation("tier"), (stack, world, entity) -> ItemDrillArrow.getArrowTier(stack).ordinal() / (ItemDrillArrow.ArrowTier.values()).length);


            ItemModelsProperties.register(Item.byBlock((Block) ModBlocks.CRYO_CHAMBER), new ResourceLocation("type"), (stack, world, entity) -> stack.getDamageValue() / (CryoChamberBlock.ChamberState.values()).length);


            ItemModelsProperties.register((Item) ModItems.VAULT_CRYSTAL, new ResourceLocation("type"), (stack, world, entity) -> VaultCrystalItem.getData(stack).getType().ordinal());
        }


        public static void registerItemProperty(Item item, String name, IItemPropertyGetter property) {
            ItemModelsProperties.register(item, Vault.id(name), property);
        }
    }

    public static class SpecialSwordModel {
        public static Map<Integer, SpecialSwordModel> REGISTRY;
        public static SpecialSwordModel JANITORS_BROOM;
        int id;
        String displayName;

        public static SpecialSwordModel getModel(int id) {
            return REGISTRY.get(Integer.valueOf(id));
        }


        public static void register() {
            REGISTRY = new HashMap<>();

            JANITORS_BROOM = register("Janitor's Broom", (new GearModelProperties()).allowTransmogrification());
        }


        GearModelProperties modelProperties = new GearModelProperties();

        public int getId() {
            return this.id;
        }

        public String getDisplayName() {
            return this.displayName;
        }

        public GearModelProperties getModelProperties() {
            return this.modelProperties;
        }

        private static SpecialSwordModel register(String displayName) {
            SpecialSwordModel swordModel = new SpecialSwordModel();
            swordModel.displayName = displayName;
            swordModel.id = REGISTRY.size();

            REGISTRY.put(Integer.valueOf(swordModel.id), swordModel);
            return swordModel;
        }

        private static SpecialSwordModel register(String displayName, GearModelProperties modelProperties) {
            SpecialSwordModel swordModel = register(displayName);
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
            SCRAPPY_REGISTRY = new HashMap<>();
            REGISTRY = new HashMap<>();

            SCRAPPY_1 = registerScrappy("Scrappy 1", () -> ScrappyArmorModel.Variant1.class);
            SCRAPPY_2 = registerScrappy("Scrappy 2", () -> ScrappyArmorModel.Variant2.class);
            SCRAPPY_3 = registerScrappy("Scrappy 3", () -> ScrappyArmorModel.Variant3.class);


            SCALE_1 = register("Scale 1", () -> ScaleArmorModel.Variant1.class);
            SCALE_2 = register("Scale 2", () -> ScaleArmorModel.Variant2.class);
            SCALE_3 = register("Scale 3", () -> ScaleArmorModel.Variant3.class);
            SCALE_4 = register("Scale 4", () -> ScaleArmorModel.Variant4.class);
            PLATED_1 = register("Plated 1", () -> PlatedArmorModel.Variant1.class);
            PLATED_1_DARK = register("Plated 1 Dark", () -> PlatedArmorModel.Variant1.class);
            PLATED_2 = register("Plated 2", () -> PlatedArmorModel.Variant2.class);
            PLATED_2_DARK = register("Plated 2 Dark", () -> PlatedArmorModel.Variant2.class);
            PLATED_3 = register("Plated 3", () -> PlatedArmorModel.Variant3.class);
            PLATED_3_DARK = register("Plated 3 Dark", () -> PlatedArmorModel.Variant3.class);
            PLATED_4 = register("Plated 4", () -> PlatedArmorModel.Variant4.class);
            PLATED_4_DARK = register("Plated 4 Dark", () -> PlatedArmorModel.Variant4.class);
            FUR_1 = register("Fur 1", () -> FurArmorModel.Variant1.class);
            FUR_2 = register("Fur 2", () -> FurArmorModel.Variant2.class);
            FUR_3 = register("Fur 3", () -> FurArmorModel.Variant3.class);
            FUR_4 = register("Fur 4", () -> FurArmorModel.Variant4.class);
            CLOAK_1 = register("Cloak 1", () -> CloakArmorModel.Variant1.class);
            CLOAK_2 = register("Cloak 2", () -> CloakArmorModel.Variant2.class);
            CLOAK_3 = register("Cloak 3", () -> CloakArmorModel.Variant3.class);
            CLOAK_4 = register("Cloak 4", () -> CloakArmorModel.Variant4.class);
            ROYAL_1 = register("Royal 1", () -> RoyalArmorModel.Variant1.class);
            ROYAL_2 = register("Royal 2", () -> RoyalArmorModel.Variant2.class);

            SCRAPPY_1_NORMAL = register("Scrappy 1", () -> ScrappyArmorModel.Variant1.class);
            SCRAPPY_2_NORMAL = register("Scrappy 2", () -> ScrappyArmorModel.Variant2.class);
            SCRAPPY_3_NORMAL = register("Scrappy 3", () -> ScrappyArmorModel.Variant3.class);

            BARBARIAN_1 = register("Barbarian 1", () -> BarbarianArmorModel.Variant1.class);
            BARBARIAN_2 = register("Barbarian 2", () -> BarbarianArmorModel.Variant2.class);
            BARBARIAN_3 = register("Barbarian 3", () -> BarbarianArmorModel.Variant3.class);
            ROYAL_1_DARK = register("Royal 1 Dark", () -> RoyalArmorModel.Variant1.class);
            BARBARIAN_1_DARK = register("Barbarian 1 Dark", () -> BarbarianArmorModel.Variant1.class);
            BARBARIAN_2_DARK = register("Barbarian 2 Dark", () -> BarbarianArmorModel.Variant2.class);
            BARBARIAN_3_DARK = register("Barbarian 3 Dark", () -> BarbarianArmorModel.Variant3.class);
            OMARLATIF = register("Omarlatif", () -> OmarlatifArmorModel.class);
            SCUBA_1 = register("Scuba 1", () -> ScubaArmorModel.Variant1.class);
            LEPRECHAUN_1 = register("Leprechaun 1", () -> LeprechaunArmorModel.Variant1.class);
            BONE_1 = register("Bone 1", () -> BoneArmorModel.Variant1.class);
            JAWBONE_1 = register("Jawbone 1", () -> JawboneArmorModel.Variant1.class);
            REVENANT_1 = register("Revenant 1", () -> RevenantArmorModel.Variant1.class);
            REVENANT_2 = register("Revenant 2", () -> RevenantArmorModel.Variant2.class);
            KNIGHT_1 = register("Knight 1", () -> KnightArmorModel.Variant1.class);
            KNIGHT_2 = register("Knight 2", () -> KnightArmorModel.Variant2.class);
            KNIGHT_3 = register("Knight 3", () -> KnightArmorModel.Variant3.class);
            DEVIL_DUCK_1 = register("DevilDuck 1", () -> DevilDuckArmorModel.Variant1.class);
            ANGEL_1 = register("Angel 1", () -> AngelArmorModel.Variant1.class);
            DEVIL_1 = register("Devil 1", () -> DevilArmorModel.Variant1.class);
        }


        public VaultGearModel<? extends LivingEntity> forSlotType(EquipmentSlotType slotType) {
            switch (slotType) {
                case HEAD:
                    return this.helmetModel;
                case CHEST:
                    return this.chestplateModel;
                case LEGS:
                    return this.leggingsModel;
            }

            return this.bootsModel;
        }


        public int getId() {
            return this.id;
        }

        public String getDisplayName() {
            return this.displayName;
        }

        public String getTextureName(EquipmentSlotType slotType, String type) {
            String base = Vault.sId("textures/models/armor/" + this.displayName.toLowerCase().replace(" ", "_") + "_armor") + ((slotType == EquipmentSlotType.LEGS) ? "_layer2" : "_layer1");

            return ((type == null) ? base : (base + "_" + type)) + ".png";
        }

        private static <T extends VaultGearModel<?>> GearModel registerScrappy(String textureName, Supplier<Class<T>> modelClassSupplier) {
            return register(textureName, modelClassSupplier, SCRAPPY_REGISTRY);
        }

        private static <T extends VaultGearModel<?>> GearModel register(String textureName, Supplier<Class<T>> modelClassSupplier) {
            return register(textureName, modelClassSupplier, REGISTRY);
        }

        private static <T extends VaultGearModel<?>> GearModel register(String textureName, Supplier<Class<T>> modelClassSupplier, Map<Integer, GearModel> registry) {
            try {
                GearModel gearModel = new GearModel();
                gearModel.displayName = textureName;
                gearModel.id = registry.size();

                if (FMLEnvironment.dist.isClient()) {
                    Class<T> modelClass = modelClassSupplier.get();
                    assert modelClass != null;
                    Constructor<T> constructor = modelClass.getConstructor(new Class[]{float.class, EquipmentSlotType.class});
                    VaultGearModel<? extends LivingEntity> vaultGearModel1 = (VaultGearModel) constructor.newInstance(new Object[]{Float.valueOf(1.0F), EquipmentSlotType.HEAD});
                    VaultGearModel<? extends LivingEntity> vaultGearModel2 = (VaultGearModel) constructor.newInstance(new Object[]{Float.valueOf(1.0F), EquipmentSlotType.CHEST});
                    VaultGearModel<? extends LivingEntity> vaultGearModel3 = (VaultGearModel) constructor.newInstance(new Object[]{Float.valueOf(1.0F), EquipmentSlotType.LEGS});
                    VaultGearModel<? extends LivingEntity> vaultGearModel4 = (VaultGearModel) constructor.newInstance(new Object[]{Float.valueOf(1.0F), EquipmentSlotType.FEET});
                    gearModel.helmetModel = vaultGearModel1;
                    gearModel.chestplateModel = vaultGearModel2;
                    gearModel.leggingsModel = vaultGearModel3;
                    gearModel.bootsModel = vaultGearModel4;
                }

                registry.put(Integer.valueOf(gearModel.id), gearModel);
                return gearModel;
            } catch (Exception e) {
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

        public static Map<Integer, SpecialGearModel> getRegistryForSlot(EquipmentSlotType slotType) {
            switch (slotType) {
                case HEAD:
                    return HEAD_REGISTRY;
                case CHEST:
                    return CHESTPLATE_REGISTRY;
                case LEGS:
                    return LEGGINGS_REGISTRY;
            }

            return BOOTS_REGISTRY;
        }


        public static SpecialGearModel getModel(EquipmentSlotType slotType, int id) {
            Map<Integer, SpecialGearModel> registry = getRegistryForSlot(slotType);
            if (registry == null) return null;
            return registry.get(Integer.valueOf(id));
        }


        public static void register() {
            HEAD_REGISTRY = new HashMap<>();
            CHESTPLATE_REGISTRY = new HashMap<>();
            LEGGINGS_REGISTRY = new HashMap<>();
            BOOTS_REGISTRY = new HashMap<>();


            CHEESE_HAT = registerHead("Cheese Hat", () -> CheeseHatModel.class);
            ISKALL_HOLOLENS = registerHead("Iskall Hololens", () -> IskallHololensModel.class);
            HELLCOW_SET = registerSet("Hellcow", () -> HellcowArmorModel.class, (new GearModelProperties()).allowTransmogrification());
            BOTANIA_SET = registerSet("Botania", () -> BotaniaArmorModel.class, (new GearModelProperties()).allowTransmogrification());
            CREATE_SET = registerSet("Create", () -> CreateArmorModel.class, (new GearModelProperties()).allowTransmogrification());
            DANK_SET = registerSet("Dank", () -> DankArmorModel.class, (new GearModelProperties()).allowTransmogrification());
            FLUX_SET = registerSet("Flux", () -> FluxArmorModel.class, (new GearModelProperties()).allowTransmogrification());
            IMMERSIVE_ENGINEERING_SET = registerSet("Immersive Engineering", () -> ImmersiveEngineeringArmorModel.class, (new GearModelProperties()).allowTransmogrification());
            MEKA_SET_LIGHT = registerSet("Meka Light", () -> MekaArmorModel.class, (new GearModelProperties()).allowTransmogrification());
            MEKA_SET_DARK = registerSet("Meka Dark", () -> MekaArmorModel.class, (new GearModelProperties()).allowTransmogrification());
            POWAH_SET = registerSet("Powah", () -> PowahArmorModel.class, (new GearModelProperties()).allowTransmogrification());
            THERMAL_SET = registerSet("Thermal", () -> ThermalArmorModel.class, (new GearModelProperties()).allowTransmogrification());
            TRASH_SET = registerSet("Trash", () -> TrashArmorModel.class, (new GearModelProperties()).allowTransmogrification());
            SKALLIBOMBA_SET = registerSet("Skallibomba", () -> SkallibombaArmorModel.class, (new GearModelProperties()).allowTransmogrification());
            VILLAGER_SET = registerSet("Villager", () -> VillagerArmorModel.class, (new GearModelProperties()).allowTransmogrification());
            AUTOMATIC_SET = registerSet("Automatic", () -> AutomaticArmorModel.class, (new GearModelProperties()).allowTransmogrification());
            FAIRY_SET = registerSet("Fairy", () -> FairyArmorModel.class, (new GearModelProperties()).allowTransmogrification());
            BUILDING_SET = registerSet("Building", () -> BuildingArmorModel.class, (new GearModelProperties()).allowTransmogrification());
            ZOMBIE_SET = registerSet("Zombie", () -> ZombieArmorModel.class, (new GearModelProperties()).allowTransmogrification());
            XNET_SET = registerSet("Xnet", () -> XnetArmorModel.class, (new GearModelProperties()).allowTransmogrification());
            TEST_DUMMY_SET = registerSet("Test Dummy", () -> TestDummyArmorModel.class, (new GearModelProperties()).allowTransmogrification());
            INDUSTRIAL_FOREGOING_SET = registerSet("Industrial Foregoing", () -> IndustrialForegoingArmorModel.class, (new GearModelProperties()).allowTransmogrification());
            CAKE_SET = registerSet("Cake", () -> CakeArmorModel.class, (new GearModelProperties()).allowTransmogrification());
        }


        GearModelProperties modelProperties = new GearModelProperties();

        public int getId() {
            return this.id;
        }

        public String getDisplayName() {
            return this.displayName;
        }

        public VaultGearModel<? extends LivingEntity> getModel() {
            return this.model;
        }

        public String getTextureName(EquipmentSlotType slotType, String type) {
            if (this.modelProperties.isPieceOfSet()) {
                String str = Vault.sId("textures/models/armor/special/" + this.displayName.toLowerCase().replace(" ", "_") + "_armor") + ((slotType == EquipmentSlotType.LEGS) ? "_layer2" : "_layer1");

                return ((type == null) ? str : (str + "_" + type)) + ".png";
            }

            String base = Vault.sId("textures/models/armor/special/" + this.displayName.toLowerCase().replace(" ", "_"));
            return ((type == null) ? base : (base + "_" + type)) + ".png";
        }

        public GearModelProperties getModelProperties() {
            return this.modelProperties;
        }

        private static <T extends VaultGearModel<?>> SpecialGearModelSet registerSet(String textureName, Supplier<Class<T>> modelClassSupplier) {
            return registerSet(textureName, modelClassSupplier, new GearModelProperties());
        }

        private static <T extends VaultGearModel<?>> SpecialGearModelSet registerSet(String textureName, Supplier<Class<T>> modelClassSupplier, GearModelProperties modelProperties) {
            SpecialGearModelSet set = new SpecialGearModelSet();
            modelProperties.makePieceOfSet();
            set.head = registerHead(textureName, modelClassSupplier, modelProperties);
            set.chestplate = registerChestplate(textureName, modelClassSupplier, modelProperties);
            set.leggings = registerLeggings(textureName, modelClassSupplier, modelProperties);
            set.boots = registerBoots(textureName, modelClassSupplier, modelProperties);
            return set;
        }

        private static <T extends VaultGearModel<?>> SpecialGearModel registerHead(String textureName, Supplier<Class<T>> modelClassSupplier) {
            return registerHead(textureName, modelClassSupplier, new GearModelProperties());
        }

        private static <T extends VaultGearModel<?>> SpecialGearModel registerHead(String textureName, Supplier<Class<T>> modelClassSupplier, GearModelProperties modelProperties) {
            return register(textureName, modelClassSupplier, modelProperties, EquipmentSlotType.HEAD, HEAD_REGISTRY);
        }

        private static <T extends VaultGearModel<?>> SpecialGearModel registerChestplate(String textureName, Supplier<Class<T>> modelClassSupplier) {
            return registerChestplate(textureName, modelClassSupplier, new GearModelProperties());
        }

        private static <T extends VaultGearModel<?>> SpecialGearModel registerChestplate(String textureName, Supplier<Class<T>> modelClassSupplier, GearModelProperties modelProperties) {
            return register(textureName, modelClassSupplier, modelProperties, EquipmentSlotType.CHEST, CHESTPLATE_REGISTRY);
        }

        private static <T extends VaultGearModel<?>> SpecialGearModel registerLeggings(String textureName, Supplier<Class<T>> modelClassSupplier) {
            return registerLeggings(textureName, modelClassSupplier, new GearModelProperties());
        }

        private static <T extends VaultGearModel<?>> SpecialGearModel registerLeggings(String textureName, Supplier<Class<T>> modelClassSupplier, GearModelProperties modelProperties) {
            return register(textureName, modelClassSupplier, modelProperties, EquipmentSlotType.LEGS, LEGGINGS_REGISTRY);
        }

        private static <T extends VaultGearModel<?>> SpecialGearModel registerBoots(String textureName, Supplier<Class<T>> modelClassSupplier) {
            return registerBoots(textureName, modelClassSupplier, new GearModelProperties());
        }

        private static <T extends VaultGearModel<?>> SpecialGearModel registerBoots(String textureName, Supplier<Class<T>> modelClassSupplier, GearModelProperties modelProperties) {
            return register(textureName, modelClassSupplier, modelProperties, EquipmentSlotType.FEET, BOOTS_REGISTRY);
        }

        private static <T extends VaultGearModel<?>> SpecialGearModel register(String textureName, Supplier<Class<T>> modelClassSupplier, GearModelProperties modelProperties, EquipmentSlotType slotType, Map<Integer, SpecialGearModel> registry) {
            try {
                SpecialGearModel specialGearModel = new SpecialGearModel();
                specialGearModel.displayName = textureName;
                specialGearModel.id = registry.size();
                specialGearModel.modelProperties = modelProperties;

                if (FMLEnvironment.dist.isClient()) {
                    Class<T> modelClass = modelClassSupplier.get();
                    Constructor<T> constructor = modelClass.getConstructor(new Class[]{float.class, EquipmentSlotType.class});
                    specialGearModel.model = (VaultGearModel<? extends LivingEntity>) constructor.newInstance(new Object[]{Float.valueOf(1.0F), slotType});
                }

                registry.put(Integer.valueOf(specialGearModel.id), specialGearModel);
                return specialGearModel;
            } catch (Exception e) {
                throw new InternalError("Error while registering Special Gear Model: " + textureName);
            }
        }

        public static class SpecialGearModelSet {
            public ModModels.SpecialGearModel head;
            public ModModels.SpecialGearModel chestplate;
            public ModModels.SpecialGearModel leggings;
            public ModModels.SpecialGearModel boots;

            public ModModels.SpecialGearModel modelForSlot(EquipmentSlotType slot) {
                if (slot == EquipmentSlotType.HEAD)
                    return this.head;
                if (slot == EquipmentSlotType.CHEST)
                    return this.chestplate;
                if (slot == EquipmentSlotType.LEGS) {
                    return this.leggings;
                }
                return this.boots;
            }
        }
    }

}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\init\ModModels.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */