package admintools.modules.impl;

import admintools.config.ConfigManager;
import admintools.modules.Module;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.client.gui.DrawContext;

public class ChatFilterModule implements Module {
    private boolean enabled = true;
    private boolean initialized = false;

    public ChatFilterModule() {
        this.enabled = !"OFF".equals(ConfigManager.getConfig().chatFilterMode);
    }

    @Override
    public String getName() {
        return "Chat Filter";
    }

    @Override
    public String getDescription() {
        return "Filters incoming chat messages based on configured rules.";
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (enabled) {
            // Keep default config mode if turning on
            if ("OFF".equals(ConfigManager.getConfig().chatFilterMode)) {
                ConfigManager.getConfig().chatFilterMode = "NORMAL_CHAT";
            }
        } else {
            ConfigManager.getConfig().chatFilterMode = "OFF";
        }
        saveConfig();
    }

    @Override
    public void enable() {
        if (!initialized) {
            ClientReceiveMessageEvents.ALLOW_CHAT.register((message, signedMessage, sender, params, receptionTimestamp) -> {
                return !shouldFilter(message.getString());
            });
            ClientReceiveMessageEvents.ALLOW_GAME.register((message, overlay) -> {
                return !shouldFilter(message.getString());
            });
            initialized = true;
        }
    }

    @Override
    public void disable() {
        // We handle filtering state inside the event callback based on isEnabled()
    }

    @Override
    public void tick() {
    }

    @Override
    public void render(DrawContext context, float tickDelta) {
    }

    @Override
    public void loadConfig() {
        this.enabled = !"OFF".equals(ConfigManager.getConfig().chatFilterMode);
    }

    @Override
    public void saveConfig() {
        ConfigManager.saveConfig();
    }

    private boolean shouldFilter(String text) {
        if (!enabled) return false;

        String mode = ConfigManager.getConfig().chatFilterMode;
        switch (mode) {
            case "SERVER_ONLY":
                // Basic heuristic: messages without <Player> format.
                if (text.matches("^<[^>]+> .*")) return true;
                break;
            case "STAFF_CHAT_ONLY":
                // Assuming staff chat starts with a specific prefix like "[Staff]"
                if (!text.startsWith("[Staff]")) return true;
                break;
            case "CUSTOM":
                String regex = ConfigManager.getConfig().chatFilterCustomRegex;
                if (regex != null && !regex.isEmpty()) {
                    try {
                        return !text.matches(regex);
                    } catch (Exception e) {
                        return false; // Invalid regex
                    }
                }
                break;
        }
        return false; // Don't filter by default
    }
}
