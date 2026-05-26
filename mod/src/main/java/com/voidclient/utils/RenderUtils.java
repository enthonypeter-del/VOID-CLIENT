package com.voidclient.utils;

import net.minecraft.client.gui.FontRenderer;

public class RenderUtils {

    public static void drawOutlineString(FontRenderer font, String text, int x, int y, int color, int outline) {
        font.drawString(text, x - 1, y, outline);
        font.drawString(text, x + 1, y, outline);
        font.drawString(text, x, y - 1, outline);
        font.drawString(text, x, y + 1, outline);
        font.drawString(text, x, y, color);
    }
}
