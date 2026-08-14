package com.justfatlard.stackz.mixin;

import net.minecraft.world.entity.vehicle.minecart.MinecartHopper;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Hopper minecarts keep vanilla stack size (64), same reasoning as regular
 * hoppers. Soft implementation; see ContainerMixin for the mechanism.
 */
@Mixin(MinecartHopper.class)
public abstract class MinecartHopperMixin {
	public int getMaxStackSize() {
		return 64;
	}
}
