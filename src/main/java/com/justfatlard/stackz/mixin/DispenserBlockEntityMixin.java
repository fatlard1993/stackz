package com.justfatlard.stackz.mixin;

import net.minecraft.world.level.block.entity.DispenserBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

/**
 * Dispensers and droppers keep vanilla stack size (64).
 * Droppers extend DispenserBlockEntity, so this covers both.
 * Preserves redstone signal strength behavior.
 *
 * In MC 26.1, getMaxStackSize() is only a default method on Container
 * and is not overridden in DispenserBlockEntity, so we add it as
 * a soft implementation to override the unlimited default from ContainerMixin.
 */
@Mixin(DispenserBlockEntity.class)
public abstract class DispenserBlockEntityMixin {
	public int getMaxStackSize() {
		return 64;
	}
}
