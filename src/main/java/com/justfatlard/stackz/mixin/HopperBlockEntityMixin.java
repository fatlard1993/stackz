package com.justfatlard.stackz.mixin;

import net.minecraft.world.level.block.entity.HopperBlockEntity;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Hoppers keep vanilla stack size (64).
 * Comparator signal strength, sorting systems, and transfer rates
 * all depend on this being predictable.
 *
 * In MC 26.1, getMaxStackSize() is only a default method on Container
 * and is not overridden in HopperBlockEntity, so we add it as
 * a soft implementation to override the unlimited default from ContainerMixin.
 */
@Mixin(HopperBlockEntity.class)
public abstract class HopperBlockEntityMixin {
	public int getMaxStackSize() {
		return 64;
	}
}
