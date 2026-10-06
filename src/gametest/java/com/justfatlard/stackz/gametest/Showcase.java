package com.justfatlard.stackz.gametest;

import justfatlard.pandorical.gametest.Pictures;
import net.fabricmc.fabric.api.client.gametest.v1.FabricClientGameTest;
import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext;
import net.fabricmc.fabric.api.client.gametest.v1.context.TestServerConnection;
import net.fabricmc.fabric.api.client.gametest.v1.context.TestServerContext;
import net.fabricmc.fabric.api.client.gametest.v1.context.TestSingleplayerContext;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

/**
 * The pictures for the readme and the mod page: a chest holding millions of one item to a slot, each count shortened to fit.
 */
public final class Showcase implements FabricClientGameTest {
	private static final long SEED = 20261005L;

	@Override
	public void runTest(ClientGameTestContext context) {
		try (TestSingleplayerContext world = Pictures.world(context, SEED)) {
			TestServerContext server = world.getServer();
			TestServerConnection connection = world.getConnection();
			Pictures.stage(context, server, Pictures.MORNING);

			BlockPos at = server.computeOnServer(s -> {
				BlockPos spawn = connection.getServerPlayer().blockPosition();
				return Pictures.dryGround(s.overworld(), spawn.getX(), spawn.getZ(), 16);
			});
			if (at == null) throw new AssertionError("no dry ground near spawn");
			server.runOnServer(s -> {
				ServerLevel level = s.overworld();
				level.setBlockAndUpdate(at, Blocks.CHEST.defaultBlockState());
				var box = (net.minecraft.world.level.block.entity.ChestBlockEntity) level.getBlockEntity(at);
				box.setItem(0, new ItemStack(Items.COBBLESTONE, 21_000_000));
				box.setItem(1, new ItemStack(Items.DEEPSLATE, 3_400_000));
				box.setItem(2, new ItemStack(Items.REDSTONE, 1_250_000));
				box.setItem(3, new ItemStack(Items.IRON_INGOT, 865_000));
				box.setItem(4, new ItemStack(Items.OAK_LOG, 12_000));
				box.setItem(9, new ItemStack(Items.DIRT, 640_000));
				box.setItem(10, new ItemStack(Items.SAND, 98_000));
				box.setItem(11, new ItemStack(Items.WHEAT, 4_500));
				box.setChanged();
			});
			Pictures.carry(server, connection,
				new ItemStack(Items.IRON_PICKAXE), new ItemStack(Items.IRON_AXE), new ItemStack(Items.TORCH, 48),
				new ItemStack(Items.BREAD, 12), new ItemStack(Items.WATER_BUCKET), new ItemStack(Items.COBBLESTONE, 64),
				new ItemStack(Items.OAK_PLANKS, 30), new ItemStack(Items.CRAFTING_TABLE), new ItemStack(Items.STONE_SWORD));
			Pictures.open(context, server, connection, at);

			Pictures.shootScreen(context, connection, "million-stack-chest");
		}
	}
}
