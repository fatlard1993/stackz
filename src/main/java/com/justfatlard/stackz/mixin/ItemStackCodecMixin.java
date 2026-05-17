package com.justfatlard.stackz.mixin;

import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

/**
 * Raises the ItemStack.MAP_CODEC count range from [1, 99] to [1, MAX_VALUE].
 * Without this, stacks above 99 fail to save to disk — silent item loss.
 */
@Mixin(ItemStack.class)
public abstract class ItemStackCodecMixin {
	@ModifyArg(
		method = "lambda$static$1",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/util/ExtraCodecs;intRange(II)Lcom/mojang/serialization/Codec;"
		),
		index = 1
	)
	private static int stackz$raiseCountCodecMax(int original) {
		return Integer.MAX_VALUE;
	}
}
