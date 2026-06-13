package admintools.mixins;

import admintools.modules.impl.CommandDisplayModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

    public PlayerEntityRendererMixin(EntityRendererFactory.Context ctx, PlayerEntityModel<AbstractClientPlayerEntity> model, float shadowRadius) {
        super(ctx, model, shadowRadius);
    }

    @Inject(method = "renderLabelIfPresent(Lnet/minecraft/client/network/AbstractClientPlayerEntity;Lnet/minecraft/text/Text;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IF)V", at = @At("HEAD"))
    private void renderCommandDisplay(AbstractClientPlayerEntity player, Text text, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, float tickDelta, CallbackInfo ci) {
        CommandDisplayModule.CommandEntry entry = CommandDisplayModule.getCommandForPlayer(player.getUuid());
        if (entry != null) {
            double d = this.dispatcher.getSquaredDistanceToCamera(player);
            if (d <= 4096.0D) { // Only render if within 64 blocks
                boolean bl = !player.isSneaking();
                float f = player.getNameLabelHeight() + 0.3F; // Render above the normal nametag
                matrices.push();
                matrices.translate(0.0F, f, 0.0F);
                matrices.multiply(this.dispatcher.getRotation());
                matrices.scale(-0.025F, -0.025F, 0.025F);
                Matrix4f matrix4f = matrices.peek().getPositionMatrix();
                TextRenderer textRenderer = this.getTextRenderer();
                
                String commandText = entry.command;
                float g = (float)(-textRenderer.getWidth(commandText) / 2);
                
                int color = 0xFFFFFF; // White text
                // Background opacity calculation can go here
                int bgColor = 0x40000000;
                
                textRenderer.draw(commandText, g, 0f, color, false, matrix4f, vertexConsumers, bl ? TextRenderer.TextLayerType.SEE_THROUGH : TextRenderer.TextLayerType.NORMAL, bgColor, light);
                
                matrices.pop();
            }
        }
    }
}
