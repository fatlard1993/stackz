package com.justfatlard.stackz;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Stackz implements ModInitializer {
	public static final String MOD_ID = "stackz";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Stackz loaded — stack sizes rebalanced");
		LOGGER.info("  Unlimited: player inventory, chests, barrels, ender chests, shulker boxes, minecart chests");
		LOGGER.info("  Vanilla (64): hoppers, dispensers, droppers, furnaces, brewing stands, crafters");
		LOGGER.info("  Durability items: vanilla stacking (tools, weapons, armor don't stack)");
		LOGGER.info("  Bundles: 9 item types, unlimited quantity per type");
		LOGGER.info("  Codecs: save/load uncapped for large stacks");
	}
}
