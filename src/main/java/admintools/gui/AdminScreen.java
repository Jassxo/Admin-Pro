package admintools.gui;

import admintools.AdminTools;
import admintools.modules.Module;
import admintools.render.RenderUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

import java.util.List;

public class AdminScreen extends Screen {

    private float openProgress = 0f;
    private TextFieldWidget searchField;
    private int scrollY = 0;
    
    // Theme colors
    private static final int BG_COLOR = 0xFF1A1A1A; // Dark grey
    private static final int PANEL_COLOR = 0xFF242424; // Lighter dark grey
    private static final int ACCENT_COLOR = 0xFF3B82F6; // Blue accent
    private static final int TEXT_COLOR = 0xFFFFFFFF; // White
    private static final int TEXT_MUTED = 0xFFAAAAAA; // Gray text
    
    public AdminScreen() {
        super(Text.literal("AdminTools"));
    }

    @Override
    protected void init() {
        super.init();
        int panelWidth = 400;
        int panelHeight = 250;
        int startX = (this.width - panelWidth) / 2;
        int startY = (this.height - panelHeight) / 2;
        
        searchField = new TextFieldWidget(this.textRenderer, startX + 130, startY + 15, 250, 20, Text.literal("Search..."));
        searchField.setMaxLength(50);
        this.addDrawableChild(searchField);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        
        // Handle animation
        if (openProgress < 1.0f) {
            openProgress += delta * 0.15f;
            if (openProgress > 1.0f) openProgress = 1.0f;
        }

        // Apply easing (easeOutExpo)
        float easedProgress = (float) (openProgress == 1.0 ? 1.0 : 1.0 - Math.pow(2, -10 * openProgress));

        // Draw blurred background
        // In 1.21, we can call this.renderBackground to draw the dark tint or blur
        this.renderBackground(context, mouseX, mouseY, delta);

        int panelWidth = 400;
        int panelHeight = 250;
        int startX = (this.width - panelWidth) / 2;
        int startY = (int) ((this.height - panelHeight) / 2 + (1.0f - easedProgress) * 20); // Slide up animation
        
        // Draw main panel
        RenderUtils.fillRoundedRect(context, startX, startY, panelWidth, panelHeight, 10, RenderUtils.applyAlpha(BG_COLOR, easedProgress));
        
        // Draw sidebar
        RenderUtils.fillRoundedRect(context, startX, startY, 120, panelHeight, 10, RenderUtils.applyAlpha(PANEL_COLOR, easedProgress));
        
        // Sidebar Title
        context.drawText(this.textRenderer, "AdminTools", startX + 25, startY + 20, TEXT_COLOR, false);

        // Sidebar Categories (Mockup)
        String[] categories = {"General", "Commands", "Chat", "Rendering", "Performance"};
        for (int i = 0; i < categories.length; i++) {
            int yPos = startY + 60 + (i * 25);
            boolean isHovered = mouseX >= startX && mouseX <= startX + 120 && mouseY >= yPos && mouseY < yPos + 25;
            int catColor = isHovered ? ACCENT_COLOR : TEXT_MUTED;
            context.drawText(this.textRenderer, categories[i], startX + 25, yPos + 5, catColor, false);
        }

        // Search Bar rendering
        searchField.render(context, mouseX, mouseY, delta);

        // Draw Modules
        List<Module> modules = AdminTools.getModuleManager().getModules();
        int modY = startY + 50 + scrollY;
        
        // Simple scissor for scrolling area (requires GL calls, omitted for simplicity, instead we'll just restrict drawing bounds)
        for (Module mod : modules) {
            if (modY >= startY + 40 && modY + 40 <= startY + panelHeight - 10) {
                // Module panel
                RenderUtils.fillRoundedRect(context, startX + 130, modY, 250, 40, 5, PANEL_COLOR);
                
                // Module Name
                context.drawText(this.textRenderer, mod.getName(), startX + 140, modY + 10, TEXT_COLOR, false);
                // Module Description
                context.drawText(this.textRenderer, mod.getDescription(), startX + 140, modY + 25, TEXT_MUTED, false);
                
                // Toggle Button (Mockup)
                int toggleColor = mod.isEnabled() ? ACCENT_COLOR : 0xFF555555;
                RenderUtils.fillRoundedRect(context, startX + 340, modY + 12, 30, 16, 8, toggleColor);
                
                // Circle inside toggle switch
                int circleOffset = mod.isEnabled() ? 14 : 2;
                RenderUtils.fillRoundedRect(context, startX + 340 + circleOffset, modY + 14, 12, 12, 6, TEXT_COLOR);
            }
            modY += 50;
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (super.mouseClicked(mouseX, mouseY, button)) return true;

        int panelWidth = 400;
        int panelHeight = 250;
        int startX = (this.width - panelWidth) / 2;
        int startY = (this.height - panelHeight) / 2;

        List<Module> modules = AdminTools.getModuleManager().getModules();
        int modY = startY + 50 + scrollY;

        for (Module mod : modules) {
            if (modY >= startY + 40 && modY + 40 <= startY + panelHeight - 10) {
                if (mouseX >= startX + 340 && mouseX <= startX + 370 && mouseY >= modY + 12 && mouseY <= modY + 28) {
                    mod.setEnabled(!mod.isEnabled());
                    return true;
                }
            }
            modY += 50;
        }

        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        scrollY += (int) (verticalAmount * 20);
        // Simple clamp
        if (scrollY > 0) scrollY = 0;
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }
}
