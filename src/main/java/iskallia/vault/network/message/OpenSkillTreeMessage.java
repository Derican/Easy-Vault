package iskallia.vault.network.message;

import iskallia.vault.container.SkillTreeContainer;
import iskallia.vault.skill.ability.AbilityTree;
import iskallia.vault.skill.talent.TalentTree;
import iskallia.vault.world.data.PlayerAbilitiesData;
import iskallia.vault.world.data.PlayerTalentsData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class OpenSkillTreeMessage {
    public static void encode(final OpenSkillTreeMessage message, final PacketBuffer buffer) {
    }

    public static OpenSkillTreeMessage decode(final PacketBuffer buffer) {
        return new OpenSkillTreeMessage();
    }

    public static void handle(final OpenSkillTreeMessage message, final Supplier<NetworkEvent.Context> contextSupplier) {
        final NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            final ServerPlayerEntity sender = context.getSender();
            if (sender == null) {
                return;
            } else {
                final PlayerAbilitiesData playerAbilitiesData = PlayerAbilitiesData.get((ServerWorld) sender.level);
                final AbilityTree abilityTree = playerAbilitiesData.getAbilities(sender);
                final PlayerTalentsData playerTalentsData = PlayerTalentsData.get((ServerWorld) sender.level);
                final TalentTree talentTree = playerTalentsData.getTalents(sender);
                NetworkHooks.openGui(sender, new INamedContainerProvider() {

                    public ITextComponent getDisplayName() {
                        return new TranslationTextComponent("container.vault.ability_tree");
                    }

                    @Nullable
                    public Container createMenu(final int i, final PlayerInventory playerInventory, final PlayerEntity playerEntity) {
                        return new SkillTreeContainer(i, abilityTree, talentTree);
                    }
                }, buffer -> {
                    buffer.writeNbt(abilityTree.serializeNBT());
                    buffer.writeNbt(talentTree.serializeNBT());
                });
                return;
            }
        });
        context.setPacketHandled(true);
    }
}
