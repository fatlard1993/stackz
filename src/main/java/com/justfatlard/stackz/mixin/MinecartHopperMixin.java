package com.justfatlard.stackz.mixin;

import net.minecraft.world.entity.vehicle.minecart.MinecartHopper;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Hopper minecarts keep vanilla stack size (64).
 * Same reasoning as regular hoppers — redstone signal integrity.
 *
 * In MC 26.1, getMaxStackSize() is only a default method on Container
 * and is not overridden in MinecartHopper, so we add it as
 * a soft implementation to override the unlimited default from ContainerMixin.
 */
@Mixin(MinecartHopper.class)
public abstract class MinecartHopperMixin {
	public int getMaxStackSize() {
		return 64;
	}
}
