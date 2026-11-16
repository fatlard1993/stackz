package com.justfatlard.stackz.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Removes per-item stack size limits for non-durability items.
 * Items with durability (tools, weapons, armor) keep vanilla stacking (1)
 * because each is a distinct object with its own wear.
 *
 * In MC 26.1, getMaxStackSize() moved from ItemStack to the ItemInstance
 * interface as a default method. We target ItemInstance directly.
 */
@Mixin(ItemInstance.class)
public interface ItemStackMixin {
	@ModifyReturnValue(method = "getMaxStackSize", at = @At("RETURN"))
	private int stackz$unlimitedItemStackSize(int original) {
		Object self = this;
		if (self instanceof ItemStack stack) {
			if (stack.has(DataComponents.MAX_DAMAGE)) return original;

			// Anything that carries its own contents keeps vanilla's cap of one.
			// Vanilla does not limit bundles and shulker boxes to a single item out
			// of caution: the contents live in a component on the stack, so two of
			// them sharing one stack share one set of contents. Filling a stack of
			// three bundles gives three bundles holding the same items, which is a
			// duplication bug wearing a convenience feature's clothes. It also makes
			// them impossible to separate, since splitting a stack copies the
			// component rather than dividing it.
			if (stack.has(DataComponents.BUNDLE_CONTENTS)) return original;
			if (stack.has(DataComponents.CONTAINER)) return original;
		}
		// Integer.MAX_VALUE overflows when vanilla's /give command multiplies by 100
		// (Integer.MAX_VALUE * 100 wraps to -100, giving "cannot give more than -100").
		// Integer.MAX_VALUE / 100 = 21,474,836; still effectively unlimited.
		return Integer.MAX_VALUE / 100;
	}
}
