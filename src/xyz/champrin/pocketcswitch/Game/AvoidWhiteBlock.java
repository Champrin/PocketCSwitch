package xyz.champrin.pocketcswitch.Game;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.inventory.InventoryClickEvent;
import cn.nukkit.item.Item;
import xyz.champrin.pocketcswitch.untils.GamingBoard;

import java.util.Random;

public class AvoidWhiteBlock extends Game {

    private int useTimes;

    public AvoidWhiteBlock(GamingBoard room) {
        this.room = room;
        this.game_type = room.game_type;
        this.count = (int) this.room.data.get("times");
        this.useTimes = count;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (this.room.finish) return;
        if (this.room.game != 1) return;
        if (this.game_type.equals("AvoidWhiteItem")) {
            if (room.gamePlayer == event.getPlayer()) {
                Item targetItem = event.getSourceItem();
                if (targetItem.getDamage() == 15) {
                    if (event.getSlot() < 45) return;
                    this.room.rank = room.rank + 1;
                    checkFinish();
                    updateItem();
                } else {
                    room.gameFail();
                }
            }
        }
    }

    public void updateItem() {
        this.useTimes = useTimes - 1;
        for (int x = 0; x <= 8; x++) {
            room.gameBox.setItem(x + 54, Item.get(0, 0));
        }
        for (int y = 4; y >= 0; y--) {
            for (int x = 0; x <= 8; x++) {
                Item item = room.gameBox.getItem(x + y * 9);
                room.gameBox.setItem(x + (y + 1) * 9, item);
            }
        }
        if (useTimes < 6) {
            for (int x = 0; x <= 8; x++) {
                room.gameBox.setItem(x, Item.get(35, 5));
            }
        } else {
            for (int x = 0; x <= 8; x++) {
                room.gameBox.setItem(x, Item.get(35, 0));
            }
            int num = new Random().nextInt(9);
            room.gameBox.setItem(num, Item.get(35, 15));
        }
    }

    @Override
    public void checkFinish() {
        if (this.room.rank >= this.count) {
            this.room.finish = true;
            room.gameComplete();
            onDefault();
        }
    }

    public void boardBuild() {
        for (int y = 0; y <= 5; y++) {
            for (int x = 0; x <= 8; x++) {
                room.gameBox.setItem(x + y * 9, Item.get(35, 0));
            }
        }
        for (int y = 0; y <= 5; y++) {
            room.gameBox.setItem(new Random().nextInt(9) + y * 9, Item.get(35, 15));
        }
    }
}
