package iskallia.vault.network.message;

import iskallia.vault.block.entity.CryoChamberTileEntity;
import iskallia.vault.config.EternalAuraConfig;
import iskallia.vault.container.inventory.CryochamberContainer;
import iskallia.vault.entity.eternal.EternalData;
import iskallia.vault.entity.eternal.EternalDataAccess;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.init.ModItems;
import iskallia.vault.world.data.EternalsData;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class EternalInteractionMessage {
    private final Action action;
    private CompoundNBT extraData = new CompoundNBT();

    private EternalInteractionMessage(Action action) {
        this.action = action;
    }

    public static EternalInteractionMessage feedItem(ItemStack stack) {
        EternalInteractionMessage pkt = new EternalInteractionMessage(Action.FEED_SELECTED);
        pkt.extraData.put("stack", (INBT) stack.serializeNBT());
        return pkt;
    }

    public static EternalInteractionMessage levelUp(String attribute) {
        EternalInteractionMessage pkt = new EternalInteractionMessage(Action.LEVEL_UP);
        pkt.extraData.putString("attribute", attribute);
        return pkt;
    }

    public static EternalInteractionMessage selectEffect(String effectName) {
        EternalInteractionMessage pkt = new EternalInteractionMessage(Action.SELECT_EFFECT);
        pkt.extraData.putString("effectName", effectName);
        return pkt;
    }

    public static void encode(EternalInteractionMessage pkt, PacketBuffer buffer) {
        buffer.writeEnum(pkt.action);
        buffer.writeNbt(pkt.extraData);
    }

    public static EternalInteractionMessage decode(PacketBuffer buffer) {
        EternalInteractionMessage pkt = new EternalInteractionMessage((Action) buffer.readEnum(Action.class));
        pkt.extraData = buffer.readNbt();
        return pkt;
    }

    public static void handle(EternalInteractionMessage pkt, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ItemStack activeStack;
            String attribute;
            float added;
            List<String> options;
            String selectedEffect;
            ServerPlayerEntity player = ((NetworkEvent.Context) contextSupplier.get()).getSender();
            if (!(player.containerMenu instanceof CryochamberContainer)) {
                return;
            }
            CryoChamberTileEntity tile = ((CryochamberContainer) player.containerMenu).getCryoChamber((World) player.getLevel());
            if (tile == null) {
                return;
            }
            UUID eternalId = tile.getEternalId();
            EternalsData data = EternalsData.get(player.getLevel());
            EternalsData.EternalGroup eternals = data.getEternals((PlayerEntity) player);
            EternalData eternal = eternals.get(eternalId);
            if (eternal == null) {
                return;
            }
            switch (pkt.action) {
                case FEED_SELECTED:
                    activeStack = player.inventory.getCarried();
                    if (activeStack.isEmpty() || !canBeFed((EternalDataAccess) eternal, activeStack)) {
                        return;
                    }
                    if (eternal.getLevel() < eternal.getMaxLevel()) {
                        ModConfigs.ETERNAL.getFoodExp(activeStack.getItem()).ifPresent(());
                    }
                    if (!eternal.isAlive() && activeStack.getItem().equals(ModItems.LIFE_SCROLL)) {
                        eternal.setAlive(true);
                        if (!player.isCreative()) {
                            activeStack.shrink(1);
                            player.containerMenu.broadcastChanges();
                        }
                    }
                    if (activeStack.getItem().equals(ModItems.AURA_SCROLL)) {
                        eternal.shuffleSeed();
                        if (eternal.getAura() != null) {
                            eternal.setAura(null);
                        }
                        if (!player.isCreative()) {
                            activeStack.shrink(1);
                            player.containerMenu.broadcastChanges();
                        }
                    }
                    break;


                case LEVEL_UP:
                    if (eternal.getUsedLevels() >= eternal.getMaxLevel()) {
                        return;
                    }
                    attribute = pkt.extraData.getString("attribute");
                    switch (attribute) {
                        case "health":
                            added = ModConfigs.ETERNAL_ATTRIBUTES.getHealthRollRange().getRandom();
                            eternal.addAttributeValue(Attributes.MAX_HEALTH, added);
                            break;


                        case "damage":
                            added = ModConfigs.ETERNAL_ATTRIBUTES.getDamageRollRange().getRandom();
                            eternal.addAttributeValue(Attributes.ATTACK_DAMAGE, added);
                            break;

                        case "movespeed":
                            added = ModConfigs.ETERNAL_ATTRIBUTES.getMoveSpeedRollRange().getRandom();
                            eternal.addAttributeValue(Attributes.MOVEMENT_SPEED, added);
                            break;
                    }

                    break;

                case SELECT_EFFECT:
                    if (eternal.getAura() != null) {
                        return;
                    }
                    options = (List<String>) ModConfigs.ETERNAL_AURAS.getRandom(eternal.getSeededRand(), 3).stream().map(EternalAuraConfig.AuraConfig::getName).collect(Collectors.toList());
                    selectedEffect = pkt.extraData.getString("effectName");
                    if (!options.contains(selectedEffect)) {
                        return;
                    }
                    eternal.setAura(selectedEffect);
                    break;
            }

        });
        context.setPacketHandled(true);
    }

    public static boolean canBeFed(EternalDataAccess eternal, ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        if (!eternal.isAlive() && stack.getItem().equals(ModItems.LIFE_SCROLL)) {
            return true;
        }
        if (stack.getItem().equals(ModItems.AURA_SCROLL)) {
            return true;
        }
        if (eternal.getLevel() < eternal.getMaxLevel() && ModConfigs.ETERNAL.getFoodExp(stack.getItem()).isPresent()) {
            return true;
        }
        return false;
    }

    public enum Action {
        FEED_SELECTED,
        LEVEL_UP,
        SELECT_EFFECT;
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\network\message\EternalInteractionMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */