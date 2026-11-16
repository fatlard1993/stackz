package com.justfatlard.stackz.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.Container;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

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
	@ModifyReturnValue(method = "getMaxStackSize()I", at = @At("RETURN"))
	private int stackz$unlimitedContainerStackSize(int original) {
		return Integer.MAX_VALUE;
	}
}
