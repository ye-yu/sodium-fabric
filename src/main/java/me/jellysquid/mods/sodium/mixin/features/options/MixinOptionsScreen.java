package me.jellysquid.mods.sodium.mixin.features.options;

import me.jellysquid.mods.sodium.client.gui.SodiumOptionsGUI;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.screen.option.VideoOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(OptionsScreen.class)
public class MixinOptionsScreen extends Screen {
    protected MixinOptionsScreen(Text title) {
        super(title);
    }

    private static final Text VIDEO_OPTIONS = Text.translatable("options.video");
    @Override
    protected <T extends Element & Drawable & Selectable> T addDrawableChild(T drawableElement) {
        if (drawableElement instanceof ButtonWidget buttonWidget && buttonWidget.getMessage().equals(VIDEO_OPTIONS)) {
            //noinspection unchecked
            drawableElement = (T) new ButtonWidget(this.width / 2 - 155, this.height / 6 + 72 - 6, 150, 20, Text.translatable("options.video"), (button) -> {
                Objects.requireNonNull(this.client).setScreen(new SodiumOptionsGUI(this));
            });
        }
        return super.addDrawableChild(drawableElement);
    }
}
