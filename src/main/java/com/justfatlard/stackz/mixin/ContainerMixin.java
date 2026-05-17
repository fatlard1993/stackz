package com.justfatlard.stackz.mixin;

import net.minecraft.world.Container;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Sets the default container stack limit to unlimited.
 * In MC 26.1, getMaxStackSize() is a default method on the Container interface
 * and is no longer overridden by individual block entity classes.
 * Specific containers (hoppers, dispensers, furnaces) override this
 * back down to 64 to preserve redstone and automation behavior.
 */
@Mixin(Container.class)
public interface ContainerMixin {
	@Inject(method = "getMaxStackSize()I", at = @At("HEAD"), cancellable = true)
	private void stackz$unlimitedContainerStackSize(CallbackInfoReturnable<Integer> cir) {
		cir.setReturnValue(Integer.MAX_VALUE);
	}
}
