package xyz.champrin.pocketcswitch.Game;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.inventory.InventoryClickEvent;
import xyz.champrin.pocketcswitch.untils.GamingBoard;

import java.util.LinkedHashMap;

public class MemoryMaster extends Game {


    public LinkedHashMap<Integer, Integer> layout = new LinkedHashMap<>();
    public int use = 0;

    public MemoryMaster(GamingBoard room) {
        this.room = room;
        this.game_type = room.game_type;
        int puzzle = (int) room.data.get("area") / 2;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (this.room.finish) return;
        if (this.room.game != 1) return;
        if (this.game_type.equals("MemoryMaster")) {
            if (room.gamePlayer == event.getPlayer()) {
                if (layout.get(use) == room.gameBox.getItem(event.getSlot()).getDamage()) {
                    this.room.rank = room.rank + 1;
                }
                use = use + 1;
                checkFinish();

            }
        }
    }


    @Override
    public void checkFinish() {
        if (this.layout.size() >= use) {
            this.room.finish = true;
        }
    }

    @Override
    public void boardBuild() {

    }
}
