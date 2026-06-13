package admintools.modules;

import admintools.modules.impl.ChatFilterModule;
import admintools.modules.impl.CommandDisplayModule;
import net.minecraft.client.gui.DrawContext;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
    private final List<Module> modules = new ArrayList<>();

    public void init() {
        modules.add(new CommandDisplayModule());
        modules.add(new ChatFilterModule());

        for (Module module : modules) {
            module.loadConfig();
            if (module.isEnabled()) {
                module.enable();
            }
        }
    }

    public void tick() {
        for (Module module : modules) {
            if (module.isEnabled()) {
                module.tick();
            }
        }
    }

    public void render(DrawContext context, float tickDelta) {
        for (Module module : modules) {
            if (module.isEnabled()) {
                module.render(context, tickDelta);
            }
        }
    }

    public List<Module> getModules() {
        return modules;
    }

    @SuppressWarnings("unchecked")
    public <T extends Module> T getModule(Class<T> clazz) {
        for (Module module : modules) {
            if (module.getClass() == clazz) {
                return (T) module;
            }
        }
        return null;
    }
}
