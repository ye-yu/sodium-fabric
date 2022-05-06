package me.jellysquid.mods.sodium.mixin.features.options;

import net.minecraft.client.gui.widget.ButtonWidget;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ButtonWidget.class)
public interface ButtonWidgetAccessor {
    @Final
    @Accessor
    void setOnPress(ButtonWidget.PressAction onPress);
}
