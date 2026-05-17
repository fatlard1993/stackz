package com.justfatlard.stackz.mixin;

import net.minecraft.world.level.block.entity.BrewingStandBlockEntity;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Brewing stands keep vanilla stack size (64).
 * Comparator signal integrity and automation timing depend on predictable limits.
 *
 * In MC 26.1, getMaxStackSize() is only a default method on Container
 * and is not overridden in BrewingStandBlockEntity, so we add it as
 * a soft implementation to override the unlimited default from ContainerMixin.
 */
@Mixin(BrewingStandBlockEntity.class)
public abstract class BrewingStandBlockEntityMixin {
	public int getMaxStackSize() {
		return 64;
	}
}
