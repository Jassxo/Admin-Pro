package admintools.render;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;

public class RenderUtils {

    // Helper method to fill a rounded rectangle. This is a simplified version using multiple standard rectangles.
    // For a highly performant rounded rect, a shader is preferred, but this works well enough for UI.
    public static void fillRoundedRect(DrawContext context, int x, int y, int width, int height, int radius, int color) {
        // Draw the main cross
        context.fill(x + radius, y, x + width - radius, y + height, color); // Center vertical
        context.fill(x, y + radius, x + radius, y + height - radius, color); // Left rect
        context.fill(x + width - radius, y + radius, x + width, y + height - radius, color); // Right rect

        // Draw the 4 corners using a simple approximation (a few smaller rectangles)
        // For a more perfect circle, we'd use Tessellator. This approximation is cheap and often "good enough" for small radii.
        if (radius > 0) {
            drawCorner(context, x + radius, y + radius, radius, 0, color); // Top-Left
            drawCorner(context, x + width - radius, y + radius, radius, 1, color); // Top-Right
            drawCorner(context, x + radius, y + height - radius, radius, 2, color); // Bottom-Left
            drawCorner(context, x + width - radius, y + height - radius, radius, 3, color); // Bottom-Right
        }
    }

    private static void drawCorner(DrawContext context, int cx, int cy, int radius, int cornerType, int color) {
        // A very basic approximated corner
        for (int i = 0; i < radius; i++) {
            for (int j = 0; j < radius; j++) {
                if (i * i + j * j <= radius * radius) {
                    int px = cx;
                    int py = cy;
                    if (cornerType == 0) { px -= i; py -= j; } // TL
                    else if (cornerType == 1) { px += i; py -= j; } // TR
                    else if (cornerType == 2) { px -= i; py += j; } // BL
                    else if (cornerType == 3) { px += i; py += j; } // BR
                    
                    context.fill(px, py, px + 1, py + 1, color);
                }
            }
        }
    }

    public static int applyAlpha(int color, float alpha) {
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = color & 0xFF;
        int a = (int) (alpha * 255.0f);
        return (a << 24) | (r << 16) | (g << 8) | b;
    }
}
