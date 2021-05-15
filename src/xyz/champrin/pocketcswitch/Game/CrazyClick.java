package xyz.champrin.pocketcswitch.Game;


import cn.nukkit.event.EventHandler;
import cn.nukkit.event.inventory.InventoryClickEvent;
import xyz.champrin.pocketcswitch.untils.GamingBoard;

public class CrazyClick extends Game {

    public CrazyClick(GamingBoard room) {
        this.room = room;
        this.game_type = room.game_type;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (this.room.finish) return;
        if (this.room.game != 1) return;
        if (this.game_type.equals("CrazyClick")) {
            if (room.gamePlayer == event.getPlayer()) {
                if (event.getSourceItem().getId() == 57) {
                    this.room.rank = room.rank + 1;
                }
            }
        }
    }

    @Override
    public void checkFinish() {

    }

    @Override
    public void boardBuild() {

    }
}
