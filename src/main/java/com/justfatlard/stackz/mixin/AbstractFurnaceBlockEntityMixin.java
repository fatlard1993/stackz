package com.justfatlard.stackz.mixin;

import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Furnaces (including blast furnaces and smokers) keep vanilla stack size (64):
 * comparator signal integrity and automation timing depend on predictable limits.
 * Soft implementation; see ContainerMixin for the mechanism.
 */
@Mixin(AbstractFurnaceBlockEntity.class)
public abstract class AbstractFurnaceBlockEntityMixin {
	public int getMaxStackSize() {
		return 64;
	}
}
