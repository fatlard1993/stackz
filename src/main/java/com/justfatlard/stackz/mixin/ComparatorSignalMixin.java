package com.justfatlard.stackz.mixin;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Fixes comparator output for unlimited containers.
 * Vanilla formula produces 0 when maxStackSize is MAX_VALUE because
 * count/MAX_VALUE rounds to 0. This ensures at least signal 1 when
 * the container has any items, so comparators can detect empty vs not.
 * Capped containers (hoppers, furnaces, etc.) still get full 0-15 range.
 */
@Mixin(AbstractContainerMenu.class)
public abstract class ComparatorSignalMixin {
	@Inject(method = "getRedstoneSignalFromContainer", at = @At("RETURN"), cancellable = true)
	private static void stackz$fixUnlimitedComparatorSignal(Container container, CallbackInfoReturnable<Integer> cir) {
		if (cir.getReturnValueI() != 0) return;
		if (container == null) return;

		for (int i = 0; i < container.getContainerSize(); i++) {
			if (!container.getItem(i).isEmpty()) {
				cir.setReturnValue(1);
				return;
			}
		}
	}
}
