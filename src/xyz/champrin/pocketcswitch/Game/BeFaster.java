package xyz.champrin.pocketcswitch.Game;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.inventory.InventoryClickEvent;
import cn.nukkit.item.Item;
import xyz.champrin.pocketcswitch.untils.GamingBoard;

public class BeFaster extends Game {

    public BeFaster(GamingBoard room) {
        this.room = room;
        this.game_type = room.game_type;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (this.room.finish) return;
        if (this.room.game != 1) return;
        if (this.game_type.equals("BeFaster")) {
            if (room.gamePlayer == event.getPlayer()) {
                Item targetItem = event.getSourceItem();
                if (targetItem.getId() != 35) return;
                if (targetItem.getDamage() == 0) return;
                this.room.rank = room.rank + 1;
                room.gameBox.setItem(event.getSlot(), Item.get(20, 0));
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
