package iskallia.vault.network.message;

import iskallia.vault.skill.ability.AbilityGroup;
import iskallia.vault.skill.ability.AbilityTree;
import iskallia.vault.world.data.PlayerAbilitiesData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;


public class AbilityKeyMessage {
    public boolean keyUp;
    public boolean keyDown;
    public boolean scrollUp;
    public boolean scrollDown;
    public boolean shouldCancelDown;
    public String selectedAbility = "";

    public AbilityKeyMessage() {
    }

    public AbilityKeyMessage(boolean keyUp, boolean keyDown, boolean scrollUp, boolean scrollDown) {
        this.keyUp = keyUp;
        this.keyDown = keyDown;
        this.scrollUp = scrollUp;
        this.scrollDown = scrollDown;
    }

    public AbilityKeyMessage(boolean shouldCancelDown) {
        this.shouldCancelDown = shouldCancelDown;
    }

    public AbilityKeyMessage(AbilityGroup<?, ?> selectAbility) {
        this.selectedAbility = selectAbility.getParentName();
    }

    public static void encode(AbilityKeyMessage message, PacketBuffer buffer) {
        buffer.writeBoolean(message.keyUp);
        buffer.writeBoolean(message.keyDown);
        buffer.writeBoolean(message.scrollUp);
        buffer.writeBoolean(message.scrollDown);
        buffer.writeBoolean(message.shouldCancelDown);
        buffer.writeUtf(message.selectedAbility);
    }

    public static AbilityKeyMessage decode(PacketBuffer buffer) {
        AbilityKeyMessage message = new AbilityKeyMessage();
        message.keyUp = buffer.readBoolean();
        message.keyDown = buffer.readBoolean();
        message.scrollUp = buffer.readBoolean();
        message.scrollDown = buffer.readBoolean();
        message.shouldCancelDown = buffer.readBoolean();
        message.selectedAbility = buffer.readUtf(32767);
        return message;
    }

    public static void handle(AbilityKeyMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayerEntity sender = context.getSender();

            if (sender == null) {
                return;
            }

            PlayerAbilitiesData abilitiesData = PlayerAbilitiesData.get((ServerWorld) sender.level);

            AbilityTree abilityTree = abilitiesData.getAbilities((PlayerEntity) sender);
            if (message.scrollUp) {
                abilityTree.scrollUp(sender.server);
            } else if (message.scrollDown) {
                abilityTree.scrollDown(sender.server);
            } else if (message.keyUp) {
                abilityTree.keyUp(sender.server);
            } else if (message.keyDown) {
                abilityTree.keyDown(sender.server);
            } else if (message.shouldCancelDown) {
                abilityTree.cancelKeyDown(sender.server);
            } else if (!message.selectedAbility.isEmpty()) {
                abilityTree.quickSelectAbility(sender.server, message.selectedAbility);
            }
        });
        context.setPacketHandled(true);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\network\message\AbilityKeyMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */