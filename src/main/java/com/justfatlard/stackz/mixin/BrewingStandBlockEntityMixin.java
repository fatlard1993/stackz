package com.justfatlard.stackz.mixin;

import net.minecraft.world.level.block.entity.BrewingStandBlockEntity;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Brewing stands keep vanilla stack size (64):
 * comparator signal integrity and automation timing depend on predictable limits.
 * Soft implementation; see ContainerMixin for the mechanism.
 */
@Mixin(BrewingStandBlockEntity.class)
public abstract class BrewingStandBlockEntityMixin {
	public int getMaxStackSize() {
		return 64;
	}
}
