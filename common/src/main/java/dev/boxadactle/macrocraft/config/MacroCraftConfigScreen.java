package dev.boxadactle.macrocraft.config;

import dev.boxadactle.boxlib.gui.config.BOptionScreen;
import dev.boxadactle.boxlib.gui.config.widget.BSpacingEntry;
import dev.boxadactle.boxlib.gui.config.widget.button.BBooleanButton;
import dev.boxadactle.boxlib.gui.config.widget.button.BConfigScreenButton;
import dev.boxadactle.boxlib.util.ClientUtils;
import dev.boxadactle.macrocraft.MacroCraft;
import dev.boxadactle.macrocraft.gui.MacroListScreen;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class MacroCraftConfigScreen extends BOptionScreen {

    public MacroCraftConfigScreen(Screen parent) {
        super(parent);

        MacroCraft.CONFIG.cacheConfig();
    }

    @Override
    protected Component getName() {
        return Component.translatable("screen.macrocraft.config.title", MacroCraft.VERSION_STRING);
    }

    @Override
    protected void initFooter(int startX, int startY) {
        this.addRenderableWidget(createHalfCancelButton(startX, startY, b -> {
            ClientUtils.setScreen(parent);
            MacroCraft.CONFIG.restoreCache();
        }));

        this.setSaveButton(createHalfSaveButton(startX, startY, b -> {
            ClientUtils.setScreen(parent);
            MacroCraft.CONFIG.save();
        }));
    }

    @Override
    protected void initConfigButtons() {
        BBooleanButton b = new BBooleanButton(
                "screen.macrocraft.config.blockInputsWhenPlaying",
                MacroCraft.CONFIG.get().blockInputsWhenPlaying,
                (value) -> MacroCraft.CONFIG.get().blockInputsWhenPlaying = value
        );
        b.setTooltip(Tooltip.create(Component.translatable("screen.macrocraft.config.blockInputsWhenPlaying.description")));
        addConfigLine(b);

        BBooleanButton c = new BBooleanButton(
                "screen.macrocraft.config.ignoreMenuNavigation",
                MacroCraft.CONFIG.get().ignoreMenuNavigation,
                (value) -> MacroCraft.CONFIG.get().ignoreMenuNavigation = value
        );
        c.setTooltip(Tooltip.create(Component.translatable("screen.macrocraft.config.ignoreMenuNavigation.description")));
        addConfigLine(c);

        BBooleanButton d = new BBooleanButton(
                "screen.macrocraft.config.ignoreChatTyping",
                MacroCraft.CONFIG.get().ignoreChatTyping,
                (value) -> MacroCraft.CONFIG.get().ignoreChatTyping = value
        );
        d.setTooltip(Tooltip.create(Component.translatable("screen.macrocraft.config.ignoreChatTyping.description")));
        addConfigLine(d);

        BBooleanButton e = new BBooleanButton(
                "screen.macrocraft.config.moveMouseWhenPlaying",
                MacroCraft.CONFIG.get().moveMouseWhenPlaying,
                (value) -> MacroCraft.CONFIG.get().moveMouseWhenPlaying = value
        );
        e.setTooltip(Tooltip.create(Component.translatable("screen.macrocraft.config.moveMouseWhenPlaying.description")));
        addConfigLine(e);

        BBooleanButton f = new BBooleanButton(
                "screen.macrocraft.config.shouldRenderHud",
                MacroCraft.CONFIG.get().shouldRenderHud,
                (value) -> MacroCraft.CONFIG.get().shouldRenderHud = value
        );
        e.setTooltip(Tooltip.create(Component.translatable("screen.macrocraft.config.shouldRenderHud.description")));
        addConfigLine(f);

        addConfigLine(new BSpacingEntry());

        addConfigLine(new BConfigScreenButton(
                Component.translatable("screen.macrocraft.config.macroManager"),
                this,
                MacroListScreen::new
        ));
    }
}
