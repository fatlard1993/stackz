package com.justfatlard.stackz;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import java.text.NumberFormat;
import java.util.Locale;

public class StackzClient implements ClientModInitializer {

    private static final NumberFormat COMMA_FORMAT = NumberFormat.getNumberInstance(Locale.US);

    @Override
    public void onInitializeClient() {
        ItemTooltipCallback.EVENT.register((stack, context, flag, lines) -> {
            int count = stack.getCount();
            if (count > 1) {
                lines.add(Component.literal("Count: " + COMMA_FORMAT.format(count))
                        .withStyle(ChatFormatting.GRAY));
            }
        });
    }
}
