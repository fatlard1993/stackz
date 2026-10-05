# Stackz

A Fabric mod that rebalances stack sizes throughout the game. Player inventories, chests, barrels, ender chests, shulker boxes, and minecart chests hold effectively unlimited quantities of a single item, while automation-sensitive containers like hoppers, hopper minecarts, dispensers, droppers, furnaces, brewing stands, and crafters keep vanilla's stack size of 64 so redstone contraptions, sorters, and comparator-based systems keep working exactly as they did before.

## Features

- **Unlimited storage stacking**: player inventory, chests, barrels, ender chests, shulker boxes, and minecart chests hold up to roughly 21.4 million of a single non-durability item per slot
- **Vanilla automation containers unchanged**: hoppers, hopper minecarts, dispensers, droppers, furnaces, brewing stands, and crafters are explicitly kept at the vanilla 64-stack cap so item transfer rates, sorting, and comparator logic aren't affected
- **Durability and container items stack normally**: tools, weapons and armor (anything with a max-damage component) keep vanilla stacking, and so do bundles and shulker boxes, whose contents live on the stack itself. Two in one stack would share one set of contents
- **Bundles rebalanced by type, not weight**: bundles hold up to 9 distinct item types with up to 99 of each type, instead of vanilla's weight-based capacity; the bundle's fullness bar reflects how many of the 9 type slots are used. 99 is the most of one item a vanilla client can read in a bundle entry: more than that crashes it
- **Oversized bundles repaired on join**: a bundle in a player's inventory holding more than 99 of one item (from before the cap) is split into entries of 99 or fewer when they join, so it holds exactly what it did in portions a client can read
- **Comparator signal fix**: containers with unlimited stack sizes still report a non-zero redstone comparator signal when they contain any items, instead of the 0 vanilla's formula would otherwise produce; capped containers keep the full 0-15 signal range
- **Compact stack count display**: on clients running [Pandorical](https://github.com/fatlard1993/pandorical), large stack counts render as abbreviated labels (e.g. `12k`, `3.4m`, `1b`) with an exact-count tooltip; the presentation is Pandorical's, so stackz itself ships no client code. Vanilla clients handle oversized counts correctly, they just draw the raw number
- **Uncapped save/load**: item stack codecs are patched so large stacks serialize and deserialize correctly instead of being clamped
- **Eating from a large stack**: the crumb particles from a stack over 64 are sent as a single item, so a vanilla client draws the food rather than the missing-texture square

## Development

Installing is in [DEVELOPMENT.md](DEVELOPMENT.md).

## License

MIT, see [LICENSE](LICENSE).
