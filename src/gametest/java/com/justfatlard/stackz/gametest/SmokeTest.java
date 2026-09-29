package com.justfatlard.stackz.gametest;

import net.fabricmc.fabric.api.client.gametest.v1.FabricClientGameTest;
import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext;
import net.fabricmc.fabric.api.client.gametest.v1.context.TestSingleplayerContext;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.HopperBlockEntity;

/**
 * This mod loads, its mixins bind, and a world comes up with it installed.
 *
 * <p>Written out rather than sharing Pandorical's harness, because this mod does not depend on
 * Pandorical and a test that dragged it in would be testing the mod beside a mod it never ships
 * with. The same few lines, run as this mod actually runs.
 */
public final class SmokeTest implements FabricClientGameTest {

	@Override
	public void runTest(ClientGameTestContext context) {
		if (!FabricLoader.getInstance().isModLoaded("stackz")) {
			throw new AssertionError("stackz is not loaded: the test is testing nothing");
		}

		try (TestSingleplayerContext world = context.worldBuilder().create()) {
			world.getConnection().waitForChunksRender();
			// Long enough for anything that registers on join to have thrown if it was going to.
			context.waitTicks(60);
			context.runOnClient(client -> hopperRespectsTheDestinationsCap());
		}
		// Closing a world still coming out of a pause deadlocks the harness's own tick lock.
		context.waitTicks(10);
	}

	/**
	 * A hopper fills a capped container only as far as its cap.
	 *
	 * <p>Vanilla's transfer asks the moving stack how big a stack of it can be and never asks the
	 * container, which is the same number in vanilla and is not here. Unfixed, this walks a capped
	 * container past its cap one item per call and never stops, which is how a furnace fed by a
	 * hopper reached three hundred of something whose slot holds sixty-four by hand.
	 *
	 * <p>HopperBlockEntity.addItem is public static, so the transfer can be asked directly rather
	 * than by standing a hopper over a furnace and waiting.
	 */
	private static void hopperRespectsTheDestinationsCap() {
		int cap = 64;
		SimpleContainer capped = new SimpleContainer(1) {
			@Override
			public int getMaxStackSize() {
				return cap;
			}
		};
		capped.setItem(0, new ItemStack(Items.COBBLESTONE, cap));

		ItemStack more = new ItemStack(Items.COBBLESTONE, 1);
		ItemStack left = HopperBlockEntity.addItem(null, capped, more, null);

		int now = capped.getItem(0).getCount();
		if (now != cap) {
			throw new AssertionError("a full container took another item: " + cap + " -> " + now);
		}
		if (left.isEmpty()) {
			throw new AssertionError("the item went somewhere, but the container did not grow");
		}
	}
}
