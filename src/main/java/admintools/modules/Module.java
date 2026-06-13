package admintools.modules;

import net.minecraft.client.gui.DrawContext;

public interface Module {
    String getName();
    String getDescription();
    boolean isEnabled();
    void setEnabled(boolean enabled);
    void enable();
    void disable();
    void tick();
    void render(DrawContext context, float tickDelta);
    void loadConfig();
    void saveConfig();
}
