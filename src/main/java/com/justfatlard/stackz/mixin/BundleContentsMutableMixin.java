package com.justfatlard.stackz.mixin;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
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

	@Shadow
	@Final
	private List<ItemStack> items;

	/**
	 * Guard tryInsert: reject new item types when we already have 9.
	 * Existing types can always accept more.
	 */
	@Inject(method = "tryInsert", at = @At("HEAD"), cancellable = true)
	private void stackz$limitBundleTypes(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
		if (stack.isEmpty()) return;
		if (hasMatchingType(stack)) return;
		if (items.size() >= MAX_ITEM_TYPES) {
			cir.setReturnValue(0);
		}
	}

	/**
	 * Remove the weight-based quantity cap. Once a type is allowed in,
	 * accept the entire stack.
	 */
	@Redirect(
		method = "tryInsert",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/item/component/BundleContents$Mutable;getMaxAmountToAdd(Lorg/apache/commons/lang3/math/Fraction;)I"
		)
	)
	private int stackz$unlimitedPerType(Object instance, Object itemWeight) {
		return Integer.MAX_VALUE;
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
		if (items.size() >= MAX_ITEM_TYPES) {
			cir.setReturnValue(0);
		}
	}

	/**
	 * Remove weight cap on the transfer path too.
	 */
	@Redirect(
		method = "tryTransfer",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/item/component/BundleContents$Mutable;getMaxAmountToAdd(Lorg/apache/commons/lang3/math/Fraction;)I"
		)
	)
	private int stackz$unlimitedPerTypeTransfer(Object instance, Object itemWeight) {
		return Integer.MAX_VALUE;
	}

	private boolean hasMatchingType(ItemStack stack) {
		for (ItemStack existing : items) {
			if (ItemStack.isSameItemSameComponents(existing, stack)) {
				return true;
			}
		}
		return false;
	}
}
