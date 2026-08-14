package com.justfatlard.stackz.mixin;

import net.minecraft.world.level.block.entity.HopperBlockEntity;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Hoppers keep vanilla stack size (64): comparator signal strength, sorting
 * systems, and transfer rates all depend on this being predictable.
 * Soft implementation; see ContainerMixin for the mechanism.
 */
@Mixin(HopperBlockEntity.class)
public abstract class HopperBlockEntityMixin {
	public int getMaxStackSize() {
		return 64;
	}
}
