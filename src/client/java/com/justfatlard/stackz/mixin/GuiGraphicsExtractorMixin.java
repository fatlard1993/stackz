package com.justfatlard.stackz.mixin;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix3x2fStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Replaces the entire item decoration pass with a compact, scaled abbreviation.
 *
 * We inject at HEAD of BOTH overloads:
 *   • 4-arg — called by the hotbar render path
 *   • 5-arg — called directly by AbstractContainerScreen (inventory); it is
 *             the real implementation that the 4-arg delegates to with null.
 *
 * Anchor strategy: translate to (x+17, y+9), scale, then draw the label at
 * (-textWidth, 0).  The right edge of the text is always at x+17 regardless
 * of whether pose.scale is honoured by the text renderer, preventing overflow
 * into adjacent slots.  At SCALE=0.75 the text is 6 px tall and sits from
 * y+9 to y+15 — visually in the bottom-right corner of the slot.
 *
 * Trade-off: item bar (durability) and cooldown overlay are also suppressed.
 * That is intentional — stackz only unlocks non-durability items, so bars
 * never appear on affected stacks anyway.
 */
@Mixin(GuiGraphicsExtractor.class)
public abstract class GuiGraphicsExtractorMixin {

    @Inject(
        method = "itemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;II)V",
        at = @At("HEAD"),
        cancellable = true
    )
    private void stackz$renderCompactDecorations(
            Font font, ItemStack stack, int x, int y, CallbackInfo ci) {
        stackz$drawLabel(font, stack, x, y, ci);
    }

    @Inject(
        method = "itemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V",
        at = @At("HEAD"),
        cancellable = true
    )
    private void stackz$renderCompactDecorationsLabeled(
            Font font, ItemStack stack, int x, int y, String customLabel, CallbackInfo ci) {
        stackz$drawLabel(font, stack, x, y, ci);
    }

    private void stackz$drawLabel(Font font, ItemStack stack, int x, int y, CallbackInfo ci) {
        if (stack.isEmpty() || stack.getCount() == 1) {
            return;
        }

        String label = stackz$abbreviate(stack.getCount());
        GuiGraphicsExtractor self = (GuiGraphicsExtractor) (Object) this;
        int textWidth = font.width(label);

        final float SCALE = 0.75f;
        float anchorX = x + 17f;
        float anchorY = y + 11f;

        Matrix3x2fStack pose = self.pose();
        pose.pushMatrix();
        pose.translate(anchorX, anchorY);
        pose.scale(SCALE, SCALE);
        self.text(font, label, -textWidth, 0, -1, true);
        pose.popMatrix();

        ci.cancel();
    }

    private static String stackz$abbreviate(int count) {
        if (count >= 1_000_000_000) {
            return (count / 1_000_000_000) + "b";
        } else if (count >= 10_000_000) {
            return (count / 1_000_000) + "m";
        } else if (count >= 1_000_000) {
            int major = count / 1_000_000;
            int minor = (count % 1_000_000) / 100_000;
            return major + "." + minor + "m";
        } else if (count >= 1_000) {
            return (count / 1_000) + "k";
        } else {
            return String.valueOf(count);
        }
    }
}
