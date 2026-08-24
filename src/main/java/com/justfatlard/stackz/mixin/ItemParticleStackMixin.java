package com.justfatlard.stackz.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/**
 * Sends one item's worth of stack when the particle only needs the picture.
 *
 * <p>Eating spawns crumbs from whatever is in hand, and the stack goes out with the particle.
 * A client without this mod rebuilds it against vanilla's limit, refuses anything over 64, and
 * draws the missing-texture square instead: a mouthful of pink and black while eating from a
 * stack of 109 melon slices.
 *
 * <p>Server-side is the right end to fix it. The count does nothing for a crumb - it is drawn
 * from the item and its components, both of which a copy keeps - and clamping here means a
 * vanilla client is handed something it already knows how to read, rather than needing this mod
 * installed to watch somebody eat.
 *
 * <p>Only oversized stacks are touched, so ordinary eating goes out exactly as vanilla sends it.
 */
@Mixin(LivingEntity.class)
public abstract class ItemParticleStackMixin {

	@ModifyVariable(method = "spawnItemParticles", at = @At("HEAD"), argsOnly = true, require = 1)
	private ItemStack stackz$crumbsNeedNoCount(ItemStack stack) {
		if (stack.isEmpty() || stack.getCount() <= 64) return stack;
		return stack.copyWithCount(1);
	}
}
