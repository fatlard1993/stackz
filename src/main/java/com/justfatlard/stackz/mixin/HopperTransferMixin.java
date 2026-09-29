package com.justfatlard.stackz.mixin;

import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * A hopper fills a container only as far as that container will hold.
 *
 * <p>Vanilla asks the moving stack how big a stack of it can be, never the container it is going
 * into, because in vanilla those are the same number. Here they are not: a chest holds millions
 * and a furnace holds sixty-four, so the arithmetic that decides how much room is left reads the
 * item's ceiling and finds room that the container does not have.
 *
 * <p>Left alone, that fills an automation container one item per transfer, past its own cap and
 * without limit - a furnace fed by a hopper reached three hundred of an ore whose slot the player
 * could only put sixty-four into by hand. The cap the other mixins here set is real; nothing was
 * consulting it on this path.
 *
 * <p>Both sites are the same mistake. {@code tryMoveInItem} computes the space left in a slot, and
 * {@code isFullContainer} decides whether to bother trying - so with only the first fixed a hopper
 * would keep offering items to a container it had already filled.
 */
@Mixin(HopperBlockEntity.class)
public abstract class HopperTransferMixin {

	/** How much of this item the destination will actually take, not how big the item stacks. */
	@Redirect(
		method = "tryMoveInItem",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getMaxStackSize()I"))
	private static int stackz$roomTheDestinationHas(ItemStack moving, Container from, Container into,
			ItemStack itemStack, int slot, Direction direction) {
		return Math.min(moving.getMaxStackSize(), into.getMaxStackSize(moving));
	}

	/** Full means full for that container, which for an automation container is sixty-four. */
	@Redirect(
		method = "isFullContainer",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getMaxStackSize()I"))
	private static int stackz$fullForThisContainer(ItemStack inSlot, Container container,
			Direction direction) {
		return Math.min(inSlot.getMaxStackSize(), container.getMaxStackSize(inSlot));
	}
}
