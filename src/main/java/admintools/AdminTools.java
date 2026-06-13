package admintools;

import admintools.config.ConfigManager;
import admintools.gui.AdminScreen;
import admintools.modules.ModuleManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdminTools implements ClientModInitializer {
    public static final String MOD_ID = "admintools";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static KeyBinding openGuiKey;
    private static ModuleManager moduleManager;

    @Override
    public void onInitializeClient() {
        LOGGER.info("Initializing AdminTools Client...");

        ConfigManager.loadConfig();

        moduleManager = new ModuleManager();
        moduleManager.init();

        openGuiKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.admintools.open_gui",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                "category.admintools.main"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openGuiKey.wasPressed()) {
                if (client.currentScreen == null) {
                    client.setScreen(new AdminScreen());
                }
            }
            moduleManager.tick();
        });

        LOGGER.info("AdminTools Client initialized!");
    }

    public static ModuleManager getModuleManager() {
        return moduleManager;
    }
}
