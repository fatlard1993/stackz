package com.justfatlard.stackz.mixin;

import net.minecraft.world.level.block.entity.CrafterBlockEntity;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Crafters keep vanilla stack size (64): comparator signal and crafting
 * behavior depend on predictable limits.
 * Soft implementation; see ContainerMixin for the mechanism.
 */
@Mixin(CrafterBlockEntity.class)
public abstract class CrafterBlockEntityMixin {
	public int getMaxStackSize() {
		return 64;
	}
}
