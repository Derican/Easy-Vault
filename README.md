# Easy-Vault

This project is taken from [iskallia's Vault Hunters](https://github.com/Iskallia/Vault-public-S1) and is up-to-date with the version on Curseforge. The project aims to remove the dependencies of variant mods that are included in researches, bringing simpler gaming and more flexibility.

## Environment

Minecraft 1.16.5 \
Forge 36.2.34 \
JEI 7.7.1.145 \
Curios 4.0.5.3

PS: If some of your mods depend on higher versions of JEI or Curios, it's usually OK to upgrade to newer versions.

## Feature

### No researches
No more researches lock! You can freely add any mods you like! But still be careful with conflicts.

### Average loots and decorations

Special loots and decoration blocks have been replaced by vanilla blocks, which leads to more common vault scenes. If you have ideas about replacement, pull requests are welcomed!

### No Witch Skull

Witch skull is removed due to runtime error. Pull requests are appreciated!

## Changelog

- 23 Aug,2022 Fix Vault gear config initialization crash
- 20 Aug,2022 Fix Twerker talent
- 14 Aug,2022 Fix chests textures, add recipe for advanced vending machine
- 30 Jul,2022 Delete FinalVaultAllowParty
- 5 Jul,2022 Update to 1.13.4
- 26 Jun,2022 Fix greyscale vert
- 25 Jun,2022 Fix InventoryAccessor
- 23 Jun,2022 Fix BREAK_ARMOR_CHANCE not found
- 21 Jun,2022 Add skill descriptions
- 25 Apr,2022 Remove mod blocks in nbt
- 22 Apr,2022 Enable KnowledgeStar to fit skill points
- 20 Apr,2022 Fix Jigsaw compile error
- 15 Apr,2022 Deleted researches

## Known Issues

- Unhandled base gravity setter
- Unhandled last slots setter
- Lack in global trader configs
- Missing Witch skull

## Q&A

1. The timer of vault disappeared!
   1. Check whether you've installed Inventory HUD+, if so, please disable **Potion** tab.
   2. Report in the issues.