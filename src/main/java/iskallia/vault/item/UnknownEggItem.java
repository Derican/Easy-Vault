package iskallia.vault.item;

import iskallia.vault.Vault;
import iskallia.vault.attribute.StringAttribute;
import iskallia.vault.attribute.VAttribute;
import iskallia.vault.init.ModConfigs;
import iskallia.vault.world.data.VaultRaidData;
import iskallia.vault.world.vault.VaultRaid;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

public class UnknownEggItem
        extends Item {
    public static VAttribute<String, StringAttribute> STORED_EGG = new VAttribute(Vault.id("stored_egg"), StringAttribute::new);

    public UnknownEggItem(ResourceLocation id, Item.Properties properties) {
        super(properties);
        setRegistryName(id);
    }


    public ActionResultType useOn(ItemUseContext context) {
        World world = context.getLevel();

        if (!world.isClientSide) {
            VaultRaid vault = VaultRaidData.get((ServerWorld) world).getAt((ServerWorld) world, context.getClickedPos());

            if (vault != null) {
                Optional<Item> egg = getStoredEgg(vault, context.getItemInHand(), world.getRandom());
                egg.ifPresent(item -> item.useOn(context));
            }
        }

        return super.useOn(context);
    }


    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if (!world.isClientSide) {
            VaultRaid vault = VaultRaidData.get((ServerWorld) world).getActiveFor(player.getUUID());
            Optional<Item> egg = getStoredEgg(vault, player.getItemInHand(hand), world.getRandom());
            egg.ifPresent(item -> item.use(world, player, hand));
        }

        return super.use(world, player, hand);
    }


    public Optional<Item> getStoredEgg(VaultRaid vault, ItemStack stack, Random random) {
        String itemName;
        if (vault == null) {
            List<Item> spawnEggs = (List<Item>) Registry.ITEM.stream().filter(item -> item instanceof net.minecraft.item.SpawnEggItem).collect(Collectors.toList());
            itemName = (String) ((StringAttribute) STORED_EGG.getOrCreate(stack, ((Item) spawnEggs.get(random.nextInt(spawnEggs.size()))).getRegistryName().toString())).getValue(stack);
        } else {
            int level = ((Integer) vault.getProperties().getBase(VaultRaid.LEVEL).orElse(Integer.valueOf(0))).intValue();
            itemName = (String) ((StringAttribute) STORED_EGG.getOrCreate(stack, (ModConfigs.UNKNOWN_EGG.getForLevel(level)).EGG_POOL.getRandom(random))).getValue(stack);
        }

        return (itemName == null) ? Optional.<Item>empty() : Registry.ITEM.getOptional(new ResourceLocation(itemName));
    }


    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
        super.appendHoverText(stack, world, tooltip, flag);
        tooltip.add((new StringTextComponent("Target: "))
                .append((ITextComponent) (new StringTextComponent((String) ((StringAttribute) STORED_EGG.getOrDefault(stack, "NONE")).getValue(stack)))
                        .withStyle(TextFormatting.GREEN)));
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\item\UnknownEggItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */