package xyz.champrin.pocketcswitch.untils.schedule;

import cn.nukkit.scheduler.Task;

public class TimeBlockElement extends Task {

    private int tick;

    public TimeBlockElement(int tick) {
        this.tick = tick;
    }

    @Override
    public void onRun(int i) {
        this.tick = tick- 1;
        if (this.tick <= 0){
            this.cancel();
        }
    }
}
