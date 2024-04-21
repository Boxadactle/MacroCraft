package dev.boxadactle.macrocraft.macro;

import com.google.gson.JsonObject;
import dev.boxadactle.boxlib.scheduling.ScheduleAction;

public abstract class MacroAction {
    public int startTicks;

    public abstract void execute();

    public abstract JsonObject getDataObject();

    public abstract void loadObject(JsonObject object);

    public MacroAction(int startTicks) {
        this.startTicks = startTicks;
    }

    /**
     * Returns the number of ticks to wait before running this action.
     */
    public int runsAfter(int ticksElapsed) {
        int i = startTicks - ticksElapsed;

        if (i < 0) {
            return -1;
        }

        return i;
    }

    public JsonObject serialize() {
        JsonObject object = new JsonObject();
        object.addProperty("startTicks", startTicks);
        object.add("data", getDataObject());

        return object;
    }

    public MacroAction deserialize(JsonObject object) {
        startTicks = object.get("startTicks").getAsInt();
        loadObject(object.get("data").getAsJsonObject());

        return this;
    }

    public static class Scheduled extends ScheduleAction {
        private final MacroAction action;
        private final int startTicks;

        public Scheduled(MacroAction action) {
            this.action = action;
            this.startTicks = action.startTicks;
        }

        public Scheduled(MacroAction action, int ticksElapsed) {
            this.action = action;
            this.startTicks = action.runsAfter(ticksElapsed);

            if (this.startTicks < 0) {
                throw new IllegalStateException("Attempting to schedule a macro action that should have already run!");
            }
        }

        @Override
        public int getWaitTime() {
            return startTicks;
        }

        @Override
        public void run() {
            action.execute();
        }

        public MacroAction getAction() {
            return action;
        }

    }

}
