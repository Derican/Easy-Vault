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
                return new StringTextComponent("Crystal Cave").withStyle(TextFormatting.DARK_PURPLE);
            }
            case "contest_alien": {
                return new StringTextComponent("Contest: Alien").withStyle(TextFormatting.DARK_AQUA);
            }
            case "contest_birdcage": {
                return new StringTextComponent("Contest: Ancient Temple").withStyle(TextFormatting.DARK_AQUA);
            }
            case "contest_city": {
                return new StringTextComponent("Contest: City Streets").withStyle(TextFormatting.DARK_AQUA);
            }
            case "contest_dragon": {
                return new StringTextComponent("Contest: Dragon").withStyle(TextFormatting.DARK_AQUA);
            }
            case "contest_fishtank": {
                return new StringTextComponent("Contest: Aquarium").withStyle(TextFormatting.DARK_AQUA);
            }
            case "contest_mine": {
                return new StringTextComponent("Contest: Mine").withStyle(TextFormatting.DARK_AQUA);
            }
            case "contest_mustard": {
                return new StringTextComponent("Contest: Yellow Brick Road").withStyle(TextFormatting.DARK_AQUA);
            }
            case "contest_oriental": {
                return new StringTextComponent("Contest: Oriental").withStyle(TextFormatting.DARK_AQUA);
            }
            case "contest_pixel": {
                return new StringTextComponent("Contest: Pixelart").withStyle(TextFormatting.DARK_AQUA);
            }
            case "contest_prismarine": {
                return new StringTextComponent("Contest: Atlantis").withStyle(TextFormatting.DARK_AQUA);
            }
            case "contest_tree": {
                return new StringTextComponent("Contest: Tree").withStyle(TextFormatting.DARK_AQUA);
            }
            case "contest_web": {
                return new StringTextComponent("Contest: Spiderweb").withStyle(TextFormatting.DARK_AQUA);
            }
            case "digsite": {
                return new StringTextComponent("Digsite").withStyle(TextFormatting.YELLOW);
            }
            case "dungeons": {
                return new StringTextComponent("Dungeon").withStyle(TextFormatting.WHITE);
            }
            case "forest": {
                return new StringTextComponent("Forest").withStyle(TextFormatting.DARK_GREEN);
            }
            case "graves": {
                return new StringTextComponent("Grave").withStyle(TextFormatting.DARK_GRAY);
            }
            case "lakes": {
                return new StringTextComponent("Lake").withStyle(TextFormatting.BLUE);
            }
            case "lava": {
                return new StringTextComponent("Lava").withStyle(TextFormatting.RED);
            }
            case "mineshaft": {
                return new StringTextComponent("Mine").withStyle(TextFormatting.GOLD);
            }
            case "mushroom_forest": {
                return new StringTextComponent("Mushroom Forest").withStyle(TextFormatting.LIGHT_PURPLE);
            }
            case "nether_flowers": {
                return new StringTextComponent("Nether Flowers").withStyle(TextFormatting.RED);
            }
            case "pirate_cove": {
                return new StringTextComponent("Pirate Cove").withStyle(TextFormatting.DARK_AQUA);
            }
            case "puzzle_cube": {
                return new StringTextComponent("Puzzle").withStyle(TextFormatting.YELLOW);
            }
            case "rainbow_forest": {
                return new StringTextComponent("Rainbow Forest").withStyle(TextFormatting.GREEN);
            }
            case "vendor": {
                return new StringTextComponent("Vendor").withStyle(TextFormatting.GOLD);
            }
            case "viewer": {
                return new StringTextComponent("Viewer").withStyle(TextFormatting.GOLD);
            }
            case "village": {
                return new StringTextComponent("Village").withStyle(TextFormatting.AQUA);
            }
            case "wildwest": {
                return new StringTextComponent("Wild West").withStyle(TextFormatting.YELLOW);
            }
            case "x_spot": {
                return new StringTextComponent("X-Mark").withStyle(TextFormatting.YELLOW);
            }
            default: {
                return null;
            }
        }
    }
}
