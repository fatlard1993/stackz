package com.justfatlard.stackz.mixin;

import net.minecraft.world.level.block.entity.DispenserBlockEntity;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Dispensers and droppers keep vanilla stack size (64) for redstone signal
 * strength; droppers extend DispenserBlockEntity, so this covers both.
 * Soft implementation; see ContainerMixin for the mechanism.
 */
@Mixin(DispenserBlockEntity.class)
public abstract class DispenserBlockEntityMixin {
	public int getMaxStackSize() {
		return 64;
	}
}
