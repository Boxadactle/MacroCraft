package dev.boxadactle.macrocraft.macro;

import dev.boxadactle.boxlib.scheduling.ScheduleAction;
import dev.boxadactle.boxlib.scheduling.Scheduling;
import dev.boxadactle.boxlib.util.ClientUtils;
import dev.boxadactle.boxlib.util.MouseUtils;
import dev.boxadactle.boxlib.util.WorldUtils;
import dev.boxadactle.macrocraft.MacroCraft;
import net.minecraft.network.chat.Component;

import java.util.LinkedList;
import java.util.List;

public class Macro {

    public int duration;
    public List<MacroAction> actions;

    private final LinkedList<MacroAction.Scheduled> SCHEDULED_TASKS = new LinkedList<>();
    private MacroFinishScheduler finishScheduler;

    public int ticksElapsed = 0;
    public boolean isPlaying = false;
    public boolean isPaused = false;

    public Macro() {
        this(0, new LinkedList<>());
    }

    public Macro(int duration, List<MacroAction> actions) {
        this.duration = duration;
        this.actions = actions;
    }

    public boolean shouldRenderHud() {
        return (isPlaying || isPaused) && MacroCraft.CONFIG.get().shouldRenderHud;
    }

    public void tick() {
        if (isPlaying && !isPaused) {
            ticksElapsed++;

            if (ticksElapsed >= duration) {
                endMacro();
            }
        }
    }

    public boolean playMacro() {
        if (actions.isEmpty()) {
            ClientUtils.showToast(
                    Component.translatable("toast.macrocraft.emptyMacro"),
                    Component.translatable("toast.macrocraft.emptyMacro.description")
            );

            return false;
        }

        if (ticksElapsed != 0) {
            throw new IllegalStateException("Attempting to start macro playback when macro is already playing or has already started! If you are trying to unpause the macro, use the resumeMacro method!");
        }

        if (WorldUtils.getWorld() == null) {
            ClientUtils.showToast(
                    Component.translatable("toast.macrocraft.notInWorld.title"),
                    Component.translatable("toast.macrocraft.notInWorld.description.play")
            );

            return false;
        }

        isPlaying = true;
        isPaused = false;

        for (MacroAction action : actions) {
            MacroAction.Scheduled scheduled = new MacroAction.Scheduled(action);
            SCHEDULED_TASKS.add(scheduled);
            Scheduling.schedule(scheduled);
        }

        finishScheduler = new MacroFinishScheduler(this, duration);

        Scheduling.schedule(finishScheduler);

        return true;
    }

    private void showMacroFinishedToast() {
        ClientUtils.showToast(
                Component.translatable("toast.macrocraft.finished.title"),
                Component.translatable("toast.macrocraft.finished.description", actions.size(), duration / 20, MacroState.MACRO_NAME)
        );
    }

    public void pauseMacro() {
        isPlaying = false;
        isPaused = true;

        ClientUtils.showToast(
                Component.translatable("toast.macrocraft.paused.title"),
                Component.translatable("toast.macrocraft.paused.description.play")
        );

        for (MacroAction.Scheduled scheduled : SCHEDULED_TASKS) {
            Scheduling.cancel(scheduled);
        }

        SCHEDULED_TASKS.clear();

        Scheduling.cancel(finishScheduler);
        finishScheduler = null;
    }

    public void resumeMacro() {
        isPlaying = true;
        isPaused = false;

        if (ticksElapsed == 0) {
            throw new IllegalStateException("Attempting to resume macro playback when macro has not started! If you are trying to start the macro, use the playMacro method!");
        }

        ClientUtils.showToast(
                Component.translatable("toast.macrocraft.resumed.title"),
                Component.translatable("toast.macrocraft.resumed.description.play")
        );

        for (MacroAction action : actions) {
            int ticks = action.runsAfter(ticksElapsed);

            if (ticks >= 0) {
                MacroAction.Scheduled scheduled = new MacroAction.Scheduled(action, ticks);
                Scheduling.schedule(scheduled);
                SCHEDULED_TASKS.add(scheduled);
            }
        }

        finishScheduler = new MacroFinishScheduler(this, duration, ticksElapsed);
        Scheduling.schedule(finishScheduler);
    }

    public void endMacro() {
        if (isPlaying) {
            showMacroFinishedToast();
        }

        isPlaying = false;
        isPaused = false;
        ticksElapsed = 0;

        for (MacroAction.Scheduled scheduled : SCHEDULED_TASKS) {
            Scheduling.cancel(scheduled);
        }

        if (ClientUtils.getCurrentScreen() == null) {
            MouseUtils.getMouse().releaseMouse();
            MouseUtils.getMouse().grabMouse();
        }

        SCHEDULED_TASKS.clear();
        Scheduling.cancel(finishScheduler);
        finishScheduler = null;
    }

    public static class MacroFinishScheduler extends ScheduleAction {

        int timeUntilEnd;
        Macro macro;

        public MacroFinishScheduler(Macro macro, int duration) {
            this.macro = macro;
            this.timeUntilEnd = duration;
        }

        public MacroFinishScheduler(Macro macro, int duration, int ticksElapsed) {
            this(macro, duration - ticksElapsed);
        }


        @Override
        public int getWaitTime() {
            return timeUntilEnd;
        }

        @Override
        public void run() {
            macro.endMacro();
        }
    }

}
