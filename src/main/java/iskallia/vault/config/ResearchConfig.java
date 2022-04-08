package iskallia.vault.config;

import com.google.gson.annotations.Expose;
import iskallia.vault.research.Restrictions;
import iskallia.vault.research.type.CustomResearch;
import iskallia.vault.research.type.ModResearch;
import iskallia.vault.research.type.Research;

import java.util.LinkedList;
import java.util.List;

public class ResearchConfig
        extends Config {
    @Expose
    public List<ModResearch> MOD_RESEARCHES;
    @Expose
    public List<CustomResearch> CUSTOM_RESEARCHES;

    public String getName() {
        return "researches";
    }

    public List<Research> getAll() {
        List<Research> all = new LinkedList<>();
        all.addAll(this.MOD_RESEARCHES);
        all.addAll(this.CUSTOM_RESEARCHES);
        return all;
    }

    public Research getByName(String name) {
        for (Research research : getAll()) {
            if (research.getName().equals(name))
                return research;
        }
        return null;
    }


    protected void reset() {
        this.MOD_RESEARCHES = new LinkedList<>();
        this.MOD_RESEARCHES.add((new ModResearch("Backpacks!", 2, new String[]{"simplybackpacks"})).withRestrictions(false, false, false, false, true));
        this.MOD_RESEARCHES.add((new ModResearch("Waystones", 3, new String[]{"waystones"})).withRestrictions(false, false, true, true, true));
        this.MOD_RESEARCHES.add((new ModResearch("Safety First", 3, new String[]{"torchmaster"})).withRestrictions(false, false, false, false, true));
        this.MOD_RESEARCHES.add((new ModResearch("Organisation", 3, new String[]{"trashcans", "dankstorage", "pickletweaks"})).withRestrictions(false, false, false, false, true));
        this.MOD_RESEARCHES.add((new ModResearch("Super Builder", 3, new String[]{"buildinggadgets"})).withRestrictions(false, false, false, false, true));
        this.MOD_RESEARCHES.add((new ModResearch("Super Miner", 8, new String[]{"mininggadgets"})).withRestrictions(false, false, false, false, true));
        this.MOD_RESEARCHES.add((new ModResearch("Storage Noob", 1, new String[]{"ironchest", "metalbarrels"})).withRestrictions(false, false, false, false, true));
        this.MOD_RESEARCHES.add((new ModResearch("Storage Master", 2, new String[]{"storage_overhaul", "storagedrawers", "modularrouters"})).withRestrictions(false, false, false, false, true));
        this.MOD_RESEARCHES.add((new ModResearch("Storage Refined", 6, new String[]{"refinedstorage"})).withRestrictions(false, false, false, false, true));
        this.MOD_RESEARCHES.add((new ModResearch("Storage Energistic", 6, new String[]{"appliedenergistics"})).withRestrictions(false, false, false, false, true));
        this.MOD_RESEARCHES.add((new ModResearch("Storage Enthusiast", 4, new String[]{"rftoolsstorage"})).withRestrictions(false, false, false, false, true));
        this.MOD_RESEARCHES.add((new ModResearch("Decorator", 1, new String[]{"decorative_blocks", "camera", "masonry"})).withRestrictions(false, false, false, false, true));
        this.MOD_RESEARCHES.add((new ModResearch("Decorator Pro", 2, new String[]{"mcwbridges", "mcwdoors", "mcwroofs", "mcwwindows", "enviromats", "blockcarpentry", "platforms"})).withRestrictions(false, false, false, false, true));
        this.MOD_RESEARCHES.add((new ModResearch("Engineer", 1, new String[]{"ironfurnaces", "engineersdecor"})).withRestrictions(false, false, false, false, true));
        this.MOD_RESEARCHES.add((new ModResearch("Super Engineer", 3, new String[]{"movingelevators", "immersiveengineering"})).withRestrictions(false, false, false, false, true));
        this.MOD_RESEARCHES.add((new ModResearch("One with Ender", 1, new String[]{"endermail", "elevatorid"})).withRestrictions(false, false, false, false, true));
        this.MOD_RESEARCHES.add((new ModResearch("The Chef", 1, new String[]{"cookingforblockheads"})).withRestrictions(false, false, false, false, true));
        this.MOD_RESEARCHES.add((new ModResearch("World Eater", 10, new String[]{"comforts"})).withRestrictions(false, false, false, false, true));
        this.MOD_RESEARCHES.add((new ModResearch("Adventurer", 3, new String[]{"dimstorage"})).withRestrictions(false, false, false, false, true));
        this.MOD_RESEARCHES.add((new ModResearch("Hacker", 6, new String[]{"xnet"})).withRestrictions(false, false, false, false, true));
        this.MOD_RESEARCHES.add((new ModResearch("Redstoner", 1, new String[]{"rsgauges", "rftoolsutility"})).withRestrictions(false, false, false, false, true));
        this.MOD_RESEARCHES.add((new ModResearch("Natural Magical", 8, new String[]{"botania"})).withRestrictions(false, false, false, false, true));
        this.MOD_RESEARCHES.add((new ModResearch("Tech Freak", 10, new String[]{"mekanism"})).withRestrictions(false, false, false, false, true));
        this.MOD_RESEARCHES.add((new ModResearch("The Emerald King", 3, new String[]{"easy_villagers"})).withRestrictions(false, false, false, false, true));
        this.MOD_RESEARCHES.add((new ModResearch("Quarry", 6, new String[]{"rftoolsbuilder"})).withRestrictions(false, false, false, false, true));
        this.MOD_RESEARCHES.add((new ModResearch("Spaceman", 4, new String[]{"ironjetpacks"})).withRestrictions(false, false, false, false, true));
        this.MOD_RESEARCHES.add((new ModResearch("Total Control", 3, new String[]{"darkutils"})).withRestrictions(false, false, false, false, true));

        this.MOD_RESEARCHES.add((new ModResearch("Creator", 2, new String[]{"create"})).withRestrictions(false, false, false, false, true));
        this.MOD_RESEARCHES.add((new ModResearch("Piper", 1, new String[]{"prettypipes"})).withRestrictions(false, false, false, false, true));
        this.MOD_RESEARCHES.add((new ModResearch("Uber Sand", 1, new String[]{"snad"})).withRestrictions(false, false, false, false, true));
        this.MOD_RESEARCHES.add((new ModResearch("Power Manager", 8, new String[]{"fluxnetworks"})).withRestrictions(true, true, true, true, true));

        this.MOD_RESEARCHES.add(new ModResearch("Let there be light!", 2, new String[]{""}));
        this.MOD_RESEARCHES.add(new ModResearch("Energetic", 2, new String[]{""}));
        this.MOD_RESEARCHES.add(new ModResearch("Thermal Technician", 10, new String[]{""}));
        this.MOD_RESEARCHES.add(new ModResearch("Plastic Technician", 8, new String[]{""}));
        this.MOD_RESEARCHES.add(new ModResearch("Extended Possibilities", 8, new String[]{""}));
        this.MOD_RESEARCHES.add(new ModResearch("Power Overwhelming", 6, new String[]{""}));
        this.MOD_RESEARCHES.add(new ModResearch("Nuclear Power", 6, new String[]{""}));

        this.CUSTOM_RESEARCHES = new LinkedList<>();
        CustomResearch customResearch = new CustomResearch("Automatic Genius", 100);
        customResearch.getItemRestrictions().put("refinedstorage:crafter", Restrictions.forItems(true));
        customResearch.getItemRestrictions().put("rftoolsutility:crafter1", Restrictions.forItems(true));
        customResearch.getItemRestrictions().put("appliedenergistics2:molecular_assembler", Restrictions.forItems(true));
        customResearch.getItemRestrictions().put("mekanism:formulaic_assemblicator", Restrictions.forItems(true));
        this.CUSTOM_RESEARCHES.add(customResearch);
    }
}


/* Location:              C:\Users\Grady\Desktop\the_vault-1.7.2p1.12.4.jar!\iskallia\vault\config\ResearchConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */