package iskallia.vault.world.vault.gen;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;

public class VaultRoomNames {
    @Nullable
    public static ITextComponent getName(final String filterKey) {
        switch (filterKey) {
            case "crystal_caves": {
                return (ITextComponent) new StringTextComponent("Crystal Cave").withStyle(TextFormatting.DARK_PURPLE);
            }
            case "contest_alien": {
                return (ITextComponent) new StringTextComponent("Contest: Alien").withStyle(TextFormatting.DARK_AQUA);
            }
            case "contest_birdcage": {
                return (ITextComponent) new StringTextComponent("Contest: Ancient Temple").withStyle(TextFormatting.DARK_AQUA);
            }
            case "contest_city": {
                return (ITextComponent) new StringTextComponent("Contest: City Streets").withStyle(TextFormatting.DARK_AQUA);
            }
            case "contest_dragon": {
                return (ITextComponent) new StringTextComponent("Contest: Dragon").withStyle(TextFormatting.DARK_AQUA);
            }
            case "contest_fishtank": {
                return (ITextComponent) new StringTextComponent("Contest: Aquarium").withStyle(TextFormatting.DARK_AQUA);
            }
            case "contest_mine": {
                return (ITextComponent) new StringTextComponent("Contest: Mine").withStyle(TextFormatting.DARK_AQUA);
            }
            case "contest_mustard": {
                return (ITextComponent) new StringTextComponent("Contest: Yellow Brick Road").withStyle(TextFormatting.DARK_AQUA);
            }
            case "contest_oriental": {
                return (ITextComponent) new StringTextComponent("Contest: Oriental").withStyle(TextFormatting.DARK_AQUA);
            }
            case "contest_pixel": {
                return (ITextComponent) new StringTextComponent("Contest: Pixelart").withStyle(TextFormatting.DARK_AQUA);
            }
            case "contest_prismarine": {
                return (ITextComponent) new StringTextComponent("Contest: Atlantis").withStyle(TextFormatting.DARK_AQUA);
            }
            case "contest_tree": {
                return (ITextComponent) new StringTextComponent("Contest: Tree").withStyle(TextFormatting.DARK_AQUA);
            }
            case "contest_web": {
                return (ITextComponent) new StringTextComponent("Contest: Spiderweb").withStyle(TextFormatting.DARK_AQUA);
            }
            case "digsite": {
                return (ITextComponent) new StringTextComponent("Digsite").withStyle(TextFormatting.YELLOW);
            }
            case "dungeons": {
                return (ITextComponent) new StringTextComponent("Dungeon").withStyle(TextFormatting.WHITE);
            }
            case "forest": {
                return (ITextComponent) new StringTextComponent("Forest").withStyle(TextFormatting.DARK_GREEN);
            }
            case "graves": {
                return (ITextComponent) new StringTextComponent("Grave").withStyle(TextFormatting.DARK_GRAY);
            }
            case "lakes": {
                return (ITextComponent) new StringTextComponent("Lake").withStyle(TextFormatting.BLUE);
            }
            case "lava": {
                return (ITextComponent) new StringTextComponent("Lava").withStyle(TextFormatting.RED);
            }
            case "mineshaft": {
                return (ITextComponent) new StringTextComponent("Mine").withStyle(TextFormatting.GOLD);
            }
            case "mushroom_forest": {
                return (ITextComponent) new StringTextComponent("Mushroom Forest").withStyle(TextFormatting.LIGHT_PURPLE);
            }
            case "nether_flowers": {
                return (ITextComponent) new StringTextComponent("Nether Flowers").withStyle(TextFormatting.RED);
            }
            case "pirate_cove": {
                return (ITextComponent) new StringTextComponent("Pirate Cove").withStyle(TextFormatting.DARK_AQUA);
            }
            case "puzzle_cube": {
                return (ITextComponent) new StringTextComponent("Puzzle").withStyle(TextFormatting.YELLOW);
            }
            case "rainbow_forest": {
                return (ITextComponent) new StringTextComponent("Rainbow Forest").withStyle(TextFormatting.GREEN);
            }
            case "vendor": {
                return (ITextComponent) new StringTextComponent("Vendor").withStyle(TextFormatting.GOLD);
            }
            case "viewer": {
                return (ITextComponent) new StringTextComponent("Viewer").withStyle(TextFormatting.GOLD);
            }
            case "village": {
                return (ITextComponent) new StringTextComponent("Village").withStyle(TextFormatting.AQUA);
            }
            case "wildwest": {
                return (ITextComponent) new StringTextComponent("Wild West").withStyle(TextFormatting.YELLOW);
            }
            case "x_spot": {
                return (ITextComponent) new StringTextComponent("X-Mark").withStyle(TextFormatting.YELLOW);
            }
            default: {
                return null;
            }
        }
    }
}
