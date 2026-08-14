package com.justfatlard.stackz.mixin;

import net.minecraft.world.Container;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Sets the default container stack limit to unlimited.
 *
 * In MC 26.1, getMaxStackSize() is a default method on the Container interface,
 * not overridden by individual block entity classes, so this single injection
 * covers every container. Automation containers (hopper, dispenser, furnace,
 * brewing stand, crafter, minecart hopper) restore vanilla 64 by soft
 * implementation: their mixins add a plain public getMaxStackSize() to the
 * concrete class, which wins over this interface default.
 */
@Mixin(Container.class)
public interface ContainerMixin {
	@Inject(method = "getMaxStackSize()I", at = @At("HEAD"), cancellable = true)
	private void stackz$unlimitedContainerStackSize(CallbackInfoReturnable<Integer> cir) {
		cir.setReturnValue(Integer.MAX_VALUE);
	}
}
