package iskallia.vault.network.message;

import iskallia.vault.container.SkillTreeContainer;
import iskallia.vault.research.ResearchTree;
import iskallia.vault.skill.ability.AbilityTree;
import iskallia.vault.skill.talent.TalentTree;
import iskallia.vault.world.data.PlayerAbilitiesData;
import iskallia.vault.world.data.PlayerResearchesData;
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
    public static void encode(OpenSkillTreeMessage message, PacketBuffer buffer) {
    }

    public static OpenSkillTreeMessage decode(PacketBuffer buffer) {
        return new OpenSkillTreeMessage();
    }

    public static void handle(OpenSkillTreeMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayerEntity sender = context.getSender();

            if (sender == null) {
                return;
            }

            PlayerAbilitiesData playerAbilitiesData = PlayerAbilitiesData.get((ServerWorld) sender.level);

            final AbilityTree abilityTree = playerAbilitiesData.getAbilities((PlayerEntity) sender);

            PlayerTalentsData playerTalentsData = PlayerTalentsData.get((ServerWorld) sender.level);
            final TalentTree talentTree = playerTalentsData.getTalents((PlayerEntity) sender);
            PlayerResearchesData playerResearchesData = PlayerResearchesData.get((ServerWorld) sender.level);
            final ResearchTree researchTree = playerResearchesData.getResearches((PlayerEntity) sender);
            NetworkHooks.openGui(sender, new INamedContainerProvider() {
                public ITextComponent getDisplayName() {
                    return (ITextComponent) new TranslationTextComponent("container.vault.ability_tree");
                }


                @Nullable
                public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
                    return (Container) new SkillTreeContainer(i, abilityTree, talentTree, researchTree);
                }
            } ());
        });


        context.setPacketHandled(true);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\network\message\OpenSkillTreeMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */