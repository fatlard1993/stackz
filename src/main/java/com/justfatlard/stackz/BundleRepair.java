package com.justfatlard.stackz;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.component.BundleContents;

/**
 * Splits bundle entries that a vanilla client cannot hash, so nobody is stuck crashing.
 *
 * <p>Bundles were allowed unlimited quantity per type, and that turned out to be one thing further
 * than a vanilla client will go: every container click makes the client hash the stack it clicked,
 * components included, and the codec for a bundle's contents refuses any entry outside [1;99]. A
 * bundle holding 132 of something did not render oddly or lose the extra - the client threw while
 * hashing and closed.
 *
 * <p>Capping insertion stops new ones being made. It does nothing for the bundles already out there,
 * and their owners cannot fix them by hand: opening a container is the very thing that crashes, so
 * the item is unreachable by the only means of reaching it. They are repaired on join instead.
 *
 * <p>Nothing is destroyed. An oversized entry is split into several of the same item - the contents
 * are a list and may name the same thing more than once - so the bundle holds exactly what it held
 * before, in portions the client can read.
 */
public final class BundleRepair {
	private BundleRepair() {}

	/** The vanilla client's hashing limit, and therefore the largest entry that can be sent. */
	private static final int MAX_PER_ENTRY = 99;

	/** Main inventory and hotbar; nothing else can be clicked into a container. */
	private static final int INVENTORY_SLOTS = 41;

	public static void onJoin(ServerPlayer player) {
		int repaired = 0;

		for (int slot = 0; slot < INVENTORY_SLOTS; slot++) {
			ItemStack stack = player.getInventory().getItem(slot);
			if (stack.isEmpty()) continue;

			BundleContents contents = stack.get(DataComponents.BUNDLE_CONTENTS);
			if (contents == null) continue;

			List<ItemStackTemplate> split = split(contents);
			if (split == null) continue;

			stack.set(DataComponents.BUNDLE_CONTENTS, new BundleContents(split));
			repaired++;
		}

		if (repaired > 0) {
			Stackz.LOGGER.info("Repaired {} unhashable bundle(s) for {} - entries split to {} or under",
				repaired, player.getName().getString(), MAX_PER_ENTRY);
		}
	}

	/** The contents re-portioned, or null when every entry was already small enough. */
	private static List<ItemStackTemplate> split(BundleContents contents) {
		boolean oversized = false;
		for (ItemStackTemplate entry : contents.items()) {
			if (entry.count() > MAX_PER_ENTRY) {
				oversized = true;
				break;
			}
		}
		if (!oversized) return null;

		List<ItemStackTemplate> out = new ArrayList<>();
		for (ItemStackTemplate entry : contents.items()) {
			int left = entry.count();
			while (left > MAX_PER_ENTRY) {
				out.add(entry.withCount(MAX_PER_ENTRY));
				left -= MAX_PER_ENTRY;
			}
			if (left > 0) out.add(entry.withCount(left));
		}
		return out;
	}
}
