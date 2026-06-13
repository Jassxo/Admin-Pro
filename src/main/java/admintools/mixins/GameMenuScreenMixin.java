package admintools.mixins;

import admintools.gui.AdminScreen;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameMenuScreen.class)
public class GameMenuScreenMixin extends Screen {

    protected GameMenuScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "initWidgets", at = @At("RETURN"))
    private void addAdminButton(CallbackInfo ci) {
        // Find a place to add the button, let's put it top left
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Admin"), button -> {
            if (this.client != null) {
                this.client.setScreen(new AdminScreen());
            }
        }).dimensions(10, 10, 80, 20).build());
    }
}
