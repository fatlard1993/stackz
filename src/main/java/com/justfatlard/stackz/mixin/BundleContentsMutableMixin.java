package com.justfatlard.stackz.mixin;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BundleContents;
import org.apache.commons.lang3.math.Fraction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

/**
 * Replaces the weight-based bundle capacity with a type-count model.
 * Bundles hold up to 9 distinct item types, unlimited quantity per type.
 * Slots are for variety, stacks are for count.
 */
@Mixin(targets = "net.minecraft.world.item.component.BundleContents$Mutable")
public abstract class BundleContentsMutableMixin {
	private static final int MAX_ITEM_TYPES = 9;

	/**
	 * Most of one item a bundle may hold in a single entry.
	 *
	 * <p>Not a taste decision - it is the vanilla client's hard limit, and this mod is server-side.
	 * Every container click makes the client hash the stack it clicked, components and all, and the
	 * codec for a bundle's contents refuses any entry outside [1;99]. Hand a vanilla client a
	 * bundle holding more than that and it does not mis-render it or drop the extra: it throws
	 * while hashing and the game closes. One player put 132 clay balls in a bundle and crashed on
	 * the next slot they clicked.
	 *
	 * <p>Nine types of ninety-nine is still far more than the weight model this replaced ever
	 * allowed, so the mod keeps its point - a bundle is for variety, and the stack is for count.
	 */
	private static final int MAX_PER_TYPE = 99;

	/**
	 * The item list lives on the superclass, {@code SimpleMutableContainer}, so a
	 * shadow declared here never resolves. Reached through an accessor on the
	 * class that actually owns it instead.
	 */
	private List<ItemStack> stackz$items() {
		return ((SimpleMutableContainerAccessor) (Object) this).stackz$items();
	}

	/**
	 * Guard tryInsert: reject new item types when we already have 9.
	 * Existing types can always accept more.
	 */
	@Inject(method = "tryInsert", at = @At("HEAD"), cancellable = true)
	private void stackz$limitBundleTypes(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
		if (stack.isEmpty()) return;
		if (hasMatchingType(stack)) return;
		if (stackz$items().size() >= MAX_ITEM_TYPES) {
			cir.setReturnValue(0);
		}
	}

	/**
	 * Replace the weight-based quantity cap with the client's hashing limit.
	 *
	 * <p>The stack being inserted comes in as a trailing parameter: a redirect may take the
	 * enclosing method's own arguments after its own, which is the only way to know which type is
	 * being topped up and therefore how much room that type has left.
	 */
	@Redirect(
		method = "tryInsert",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/item/component/BundleContents$Mutable;getMaxAmountToAdd(Lorg/apache/commons/lang3/math/Fraction;)I"
		)
	)
	private int stackz$roomForType(BundleContents.Mutable instance, Fraction itemWeight, ItemStack stack) {
		return roomFor(stack);
	}

	/**
	 * Guard tryTransfer BEFORE safeTake extracts items from the slot.
	 * Without this, items get taken from the slot then rejected by tryInsert,
	 * causing item loss.
	 */
	@Inject(method = "tryTransfer", at = @At("HEAD"), cancellable = true)
	private void stackz$limitBundleTypesTransfer(Slot slot, Player player, CallbackInfoReturnable<Integer> cir) {
		ItemStack slotItem = slot.getItem();
		if (slotItem.isEmpty()) return;
		if (hasMatchingType(slotItem)) return;
		if (stackz$items().size() >= MAX_ITEM_TYPES) {
			cir.setReturnValue(0);
		}
	}

	/** The same limit on the transfer path, read off the slot being moved from. */
	@Redirect(
		method = "tryTransfer",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/item/component/BundleContents$Mutable;getMaxAmountToAdd(Lorg/apache/commons/lang3/math/Fraction;)I"
		)
	)
	private int stackz$roomForTypeTransfer(BundleContents.Mutable instance, Fraction itemWeight,
			Slot slot, Player player) {
		return roomFor(slot.getItem());
	}

	/** How much more of this item the bundle may take before an entry becomes unhashable. */
	private int roomFor(ItemStack stack) {
		if (stack.isEmpty()) return 0;

		int existing = 0;
		for (ItemStack held : stackz$items()) {
			if (ItemStack.isSameItemSameComponents(held, stack)) existing += held.getCount();
		}
		return Math.max(0, MAX_PER_TYPE - existing);
	}

	private boolean hasMatchingType(ItemStack stack) {
		for (ItemStack existing : stackz$items()) {
			if (ItemStack.isSameItemSameComponents(existing, stack)) {
				return true;
			}
		}
		return false;
	}
}
