package com.justfatlard.stackz.mixin;

import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Furnaces (including blast furnaces and smokers) keep vanilla stack size (64).
 * Comparator signal integrity and automation timing depend on predictable limits.
 *
 * In MC 26.1, getMaxStackSize() is only a default method on Container
 * and is not overridden in AbstractFurnaceBlockEntity, so we add it as
 * a soft implementation to override the unlimited default from ContainerMixin.
 */
@Mixin(AbstractFurnaceBlockEntity.class)
public abstract class AbstractFurnaceBlockEntityMixin {
	public int getMaxStackSize() {
		return 64;
	}
}
