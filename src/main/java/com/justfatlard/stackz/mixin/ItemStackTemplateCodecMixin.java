package com.justfatlard.stackz.mixin;

import net.minecraft.world.item.ItemStackTemplate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

/**
 * Raises the ItemStackTemplate.MAP_CODEC count range from [1, 99] to [1, MAX_VALUE].
 * Templates are used for recipes and loot tables — need the same cap raise.
 */
@Mixin(ItemStackTemplate.class)
public abstract class ItemStackTemplateCodecMixin {
	@ModifyArg(
		method = "lambda$static$0",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/util/ExtraCodecs;intRange(II)Lcom/mojang/serialization/Codec;"
		),
		index = 1
	)
	private static int stackz$raiseTemplateCountCodecMax(int original) {
		return Integer.MAX_VALUE;
	}
}
