package xyz.champrin.pocketcswitch.Game;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.inventory.InventoryClickEvent;
import cn.nukkit.item.Item;
import xyz.champrin.pocketcswitch.untils.GamingBoard;

import java.util.Random;

public class LightsOut extends Game {

    public LightsOut(GamingBoard room) {
        this.room = room;
        this.game_type = room.game_type;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (this.room.finish) return;
        if (this.room.game != 1) return;
        if (this.game_type.equals("LightsOut")) {
            //TODO 判断目标为Chest Inventory
            if (room.gamePlayer == event.getPlayer()) {
                updateItem(event.getSlot());
                checkFinish();
            }
        }
    }

    /**
     * mate:5绿色羊毛 15黑色羊毛
     **/
    public void updateItem(int slot) {
        turn(slot);
        turn(slot + 9);
        turn(slot - 9);
        int targetX = slot % 9;
        int targetY = slot / 9;
        //TODO 不能超过边框
        if (targetX - 1 != 0) {
            turn(targetX - 1 + targetY * 9);
        }
        if (targetX + 1 != 8) {
            turn(targetX + 1 + targetY * 9);
        }

    }

    private void turn(int slot) {
        Item checkItem = room.gameBox.getItem(slot);
        if (checkItem.getId() == 35) {
            if (checkItem.getDamage() == 15) {
                this.room.rank = this.room.rank + 1;
                room.gameBox.setItem(slot - 9, Item.get(35, 5));
            } else if (checkItem.getDamage() == 5) {
                if (this.room.rank - 1 > 0) {
                    this.room.rank = room.rank - 1;
                }
                room.gameBox.setItem(slot - 9, Item.get(35, 15));
            }
        }
    }

    public void checkFinish() {
        if (this.room.rank >= 54) {
            this.room.finish = true;
            room.gameComplete();
            onDefault();
        }
    }

    public void boardBuild() {
        for (int y = 1; y <= 6; y++) {
            for (int x = 0; x <= 8; x++) {
                int num = new Random().nextInt(2);
                int mate = (num == 1 ? 5 : 15);
                if (mate == 5) {
                    this.room.rank = room.rank + 1;
                }
                room.gameBox.setItem(x + (y - 1) * 9, Item.get(35, 0));
            }
        }
    }
}