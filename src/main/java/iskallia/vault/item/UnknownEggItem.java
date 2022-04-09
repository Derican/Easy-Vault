// 
// Decompiled by Procyon v0.6.0
// 

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
import net.minecraft.item.SpawnEggItem;
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
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class UnknownEggItem extends Item
{
    public static VAttribute<String, StringAttribute> STORED_EGG;
    
    public UnknownEggItem(final ResourceLocation id, final Item.Properties properties) {
        super(properties);
        this.setRegistryName(id);
    }
    
    public ActionResultType useOn(final ItemUseContext context) {
        final World world = context.getLevel();
        if (!world.isClientSide) {
            final VaultRaid vault = VaultRaidData.get((ServerWorld)world).getAt((ServerWorld)world, context.getClickedPos());
            if (vault != null) {
                final Optional<Item> egg = this.getStoredEgg(vault, context.getItemInHand(), world.getRandom());
                egg.ifPresent(item -> item.useOn(context));
            }
        }
        return super.useOn(context);
    }
    
    public ActionResult<ItemStack> use(final World world, final PlayerEntity player, final Hand hand) {
        if (!world.isClientSide) {
            final VaultRaid vault = VaultRaidData.get((ServerWorld)world).getActiveFor(player.getUUID());
            final Optional<Item> egg = this.getStoredEgg(vault, player.getItemInHand(hand), world.getRandom());
            egg.ifPresent(item -> item.use(world, player, hand));
        }
        return (ActionResult<ItemStack>)super.use(world, player, hand);
    }
    
    public Optional<Item> getStoredEgg(final VaultRaid vault, final ItemStack stack, final Random random) {
        String itemName;
        if (vault == null) {
            final List<Item> spawnEggs = Registry.ITEM.stream().filter(item -> item instanceof SpawnEggItem).collect(Collectors.toList());
            itemName = UnknownEggItem.STORED_EGG.getOrCreate(stack, spawnEggs.get(random.nextInt(spawnEggs.size())).getRegistryName().toString()).getValue(stack);
        }
        else {
            final int level = vault.getProperties().getBase(VaultRaid.LEVEL).orElse(0);
            itemName = UnknownEggItem.STORED_EGG.getOrCreate(stack, ModConfigs.UNKNOWN_EGG.getForLevel(level).EGG_POOL.getRandom(random)).getValue(stack);
        }
        return (itemName == null) ? Optional.empty() : Registry.ITEM.getOptional(new ResourceLocation(itemName));
    }
    
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(final ItemStack stack, final World world, final List<ITextComponent> tooltip, final ITooltipFlag flag) {
        super.appendHoverText(stack, world, (List)tooltip, flag);
        tooltip.add((ITextComponent)new StringTextComponent("Target: ").append((ITextComponent)new StringTextComponent((String)UnknownEggItem.STORED_EGG.getOrDefault(stack, "NONE").getValue(stack)).withStyle(TextFormatting.GREEN)));
    }
    
    static {
        UnknownEggItem.STORED_EGG = new VAttribute<String, StringAttribute>(Vault.id("stored_egg"), StringAttribute::new);
    }
}
