package iskallia.vault.easteregg;

import iskallia.vault.Vault;
import iskallia.vault.util.AdvancementHelper;
import iskallia.vault.world.data.VaultRaidData;
import iskallia.vault.world.vault.VaultRaid;
import iskallia.vault.world.vault.gen.piece.VaultRoom;
import iskallia.vault.world.vault.logic.objective.CakeHuntObjective;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import java.util.Random;


@EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SuperSecretCakes {
    public static final String[] CAKE_QUOTES = new String[]{"The cake is a lie", "You can have cake and eat it too?", "Would like some tea with that?", "The cake equals Ï€ (Pi) ?", "This cake is made with love", "DONT GET GREEDY", "The cake is a pine?", "That'll go right to your thighs", "Have you got the coffee?", "When life gives you cake you eat it", "The cake says 'goodbye'", "The pie want to cry", "It's a piece of cake to bake a pretty cake", "The cherries are a lie", "1000 calories", "Icing on the cake!", "Happy Birthday! Is it your birthday?", "This is caketastic!", "An actual pie chart", "Arrr! I'm a Pie-rate", "Not every pies in the world is round, sometimes... pi * r ^ 2", "HALLO!", "#NeverLeaving cause cake sticks to you", "Tell me lies, tell me sweet little pies", "Diet...what diet!!!!", "I'll take the three story pie and a diet coke... don't want to get fat", "This is the end of all cake"};


    @SubscribeEvent
    public static void onCakePlaced(BlockEvent.EntityPlaceEvent event) {
        if (event.getWorld().isClientSide())
            return;
        if (((ServerWorld) event.getWorld()).dimension() != Vault.VAULT_KEY) {
            return;
        }
        if (event.getPlacedBlock().getBlock() == Blocks.CAKE) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onCakeEat(PlayerInteractEvent.RightClickBlock event) {
        if (event.getWorld().dimension() != Vault.VAULT_KEY)
            return;
        World world = event.getWorld();
        PlayerEntity player = event.getPlayer();

        if (world.getBlockState(event.getPos()).getBlock() instanceof net.minecraft.block.CakeBlock)
            if (world.isClientSide()) {
                Random random = new Random();
                String cakeQuote = CAKE_QUOTES[random.nextInt(CAKE_QUOTES.length)];
                StringTextComponent text = new StringTextComponent("\"" + cakeQuote + "\"");
                text.setStyle(Style.EMPTY.withItalic(Boolean.valueOf(true)).withColor(Color.fromRgb(-15343)));
                player.displayClientMessage((ITextComponent) text, true);
            } else if (world instanceof ServerWorld && player instanceof ServerPlayerEntity) {
                ServerPlayerEntity sPlayer = (ServerPlayerEntity) player;
                ServerWorld sWorld = (ServerWorld) world;

                world.destroyBlock(event.getPos(), false);
                player.addEffect(new EffectInstance(Effects.ABSORPTION, 1200, 0));
                AdvancementHelper.grantCriterion(sPlayer, Vault.id("main/super_secret_cakes"), "cake_consumed");

                VaultRaid raid = VaultRaidData.get(sWorld).getAt(sWorld, event.getPos());
                if (raid != null) {
                    raid.getGenerator().getPiecesAt(event.getPos(), VaultRoom.class).forEach(room -> room.setCakeEaten(true));


                    raid.getActiveObjective(CakeHuntObjective.class).ifPresent(cakeObjective -> cakeObjective.expandVault(sWorld, event.getPos(), raid));
                }


                event.setCanceled(true);
            }
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\easteregg\SuperSecretCakes.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */