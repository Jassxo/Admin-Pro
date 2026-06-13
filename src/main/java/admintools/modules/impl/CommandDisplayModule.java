package admintools.modules.impl;

import admintools.config.ConfigManager;
import admintools.modules.Module;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CommandDisplayModule implements Module {
    private boolean enabled;
    
    // Maps a player's UUID to the last command they executed, and the timestamp it was executed
    private static final Map<UUID, CommandEntry> activeCommands = new ConcurrentHashMap<>();

    public CommandDisplayModule() {
        this.enabled = ConfigManager.getConfig().commandDisplayEnabled;
    }

    @Override
    public String getName() {
        return "Command Display";
    }

    @Override
    public String getDescription() {
        return "Displays executed moderation commands above player name tags.";
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        saveConfig();
        if (enabled) enable();
        else disable();
    }

    @Override
    public void enable() {
        ClientSendMessageEvents.COMMAND.register(command -> {
            onCommandSent(command);
        });
    }

    @Override
    public void disable() {
        activeCommands.clear();
    }

    @Override
    public void tick() {
        if (!enabled) return;
        long currentTime = System.currentTimeMillis();
        long displayTimeMs = ConfigManager.getConfig().commandDisplayTimeSeconds * 1000L;
        
        activeCommands.entrySet().removeIf(entry -> 
            currentTime - entry.getValue().timestamp > displayTimeMs
        );
    }

    @Override
    public void render(DrawContext context, float tickDelta) {
        // 2D rendering if needed, but we handle 3D rendering via Mixin
    }

    @Override
    public void loadConfig() {
        this.enabled = ConfigManager.getConfig().commandDisplayEnabled;
    }

    @Override
    public void saveConfig() {
        ConfigManager.getConfig().commandDisplayEnabled = this.enabled;
        ConfigManager.saveConfig();
    }
    
    // Called from mixin or event when the client sends a command
    public static void onCommandSent(String command) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null && ConfigManager.getConfig().commandDisplayEnabled) {
            if (ConfigManager.getConfig().commandDisplayOwnCommands) {
                activeCommands.put(client.player.getUuid(), new CommandEntry("/" + command, System.currentTimeMillis()));
            }
        }
    }
    
    // For when server broadcasts commands (placeholder for future regex parsing)
    public static void onCommandReceived(UUID playerUuid, String command) {
        if (ConfigManager.getConfig().commandDisplayEnabled) {
            activeCommands.put(playerUuid, new CommandEntry(command, System.currentTimeMillis()));
        }
    }

    public static CommandEntry getCommandForPlayer(UUID uuid) {
        return activeCommands.get(uuid);
    }

    public static class CommandEntry {
        public final String command;
        public final long timestamp;

        public CommandEntry(String command, long timestamp) {
            this.command = command;
            this.timestamp = timestamp;
        }
    }
}
