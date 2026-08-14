# Stackz

A Fabric mod that rebalances stack sizes throughout the game. Player inventories, chests, barrels, ender chests, shulker boxes, and minecart chests hold effectively unlimited quantities of a single item, while automation-sensitive containers like hoppers, dispensers, droppers, furnaces, brewing stands, and crafters keep vanilla's stack size of 64 so redstone contraptions, sorters, and comparator-based systems keep working exactly as they did before.

## Features

- **Unlimited storage stacking**: player inventory, chests, barrels, ender chests, shulker boxes, and minecart chests hold up to roughly 21.4 million of a single non-durability item per slot
- **Vanilla automation containers unchanged**: hoppers, dispensers, droppers, furnaces, brewing stands, and crafters are explicitly kept at the vanilla 64-stack cap so item transfer rates, sorting, and comparator logic aren't affected
- **Durability items stack normally**: tools, weapons, and armor (anything with a max-damage component) are left at vanilla stacking (1) since each individual item has its own wear
- **Bundles rebalanced by type, not weight**: bundles hold up to 9 distinct item types with unlimited quantity per type, instead of vanilla's weight-based capacity; the bundle's fullness bar reflects how many of the 9 type slots are used
- **Comparator signal fix**: containers with unlimited stack sizes still report a non-zero redstone comparator signal when they contain any items, instead of the 0 vanilla's formula would otherwise produce; capped containers keep the full 0-15 signal range
- **Compact stack count display**: the client renders large stack counts as abbreviated labels (e.g. `12k`, `3.4m`, `1b`) instead of overflowing or truncating oddly in the corner of an item slot
- **Uncapped save/load**: item stack codecs are patched so large stacks serialize and deserialize correctly instead of being clamped

## Requirements

Targets the Minecraft, Fabric Loader, and Fabric API versions declared in this mod's `gradle.properties`; check there for the exact currently-supported version.

## Installation

Install alongside its declared dependencies (see `fabric.mod.json`). This mod uses mixins on both the server and client, so it should be installed on both sides in multiplayer.

## License

MIT
