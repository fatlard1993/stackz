package com.justfatlard.stackz.mixin;

import net.minecraft.world.level.block.entity.CrafterBlockEntity;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Crafter keeps vanilla stack size (64).
 * Redstone automation block — comparator signal and crafting behavior
 * depend on predictable limits.
 *
 * In MC 26.1, getMaxStackSize() is only a default method on Container
 * and is not overridden in CrafterBlockEntity, so we add it as
 * a soft implementation to override the unlimited default from ContainerMixin.
 */
@Mixin(CrafterBlockEntity.class)
public abstract class CrafterBlockEntityMixin {
	public int getMaxStackSize() {
		return 64;
	}
}
