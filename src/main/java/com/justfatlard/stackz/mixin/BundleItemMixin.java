package com.justfatlard.stackz.mixin;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BundleContents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Fixes bundle display to show type-count fullness instead of weight.
 * Bar shows how many of the 9 type slots are used.
 */
@Mixin(BundleItem.class)
public abstract class BundleItemMixin {
	private static final int MAX_ITEM_TYPES = 9;

	/**
	 * Fullness is now types/9 instead of weight fraction.
	 * 0 types = 0.0, 9 types = 1.0
	 */
	@Inject(method = "getFullnessDisplay", at = @At("HEAD"), cancellable = true)
	private static void stackz$typeBasedFullness(ItemStack stack, CallbackInfoReturnable<Float> cir) {
		BundleContents contents = stack.get(DataComponents.BUNDLE_CONTENTS);
		if (contents == null) {
			cir.setReturnValue(0f);
			return;
		}
		cir.setReturnValue((float) contents.size() / MAX_ITEM_TYPES);
	}
}
