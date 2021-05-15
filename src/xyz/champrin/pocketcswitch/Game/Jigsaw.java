package xyz.champrin.pocketcswitch.Game;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.inventory.InventoryClickEvent;
import cn.nukkit.item.Item;
import xyz.champrin.pocketcswitch.untils.GamingBoard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Jigsaw extends Game {

    /**
     * 布局 ALL
     * x=1 x=3 x=5 x=7
     * * * * * * * * *
     * √ √ √ * √ √ √ *y=1
     * √ √ √ * √ √ √ *
     * √ √ √ * √ √ √ *y=3
     * * * * * * * * *
     * * * * * * * * *
     * x = slot%9 y = slot/9
     **/
    public ArrayList<String> layout = new ArrayList<>(Arrays.asList("35-0", "42-0", "80-0", "155-0", "159-0", "24-0", "35-4", "159-4", "179-0"));
    public ArrayList<Integer> Pos = new ArrayList<>(Arrays.asList(10, 11, 12, 19, 20, 21, 28, 29, 30));

    public Jigsaw(GamingBoard room) {
        this.room = room;
        this.game_type = room.game_type;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (this.room.finish) return;
        if (this.room.game != 1) return;
        if (this.game_type.equals("Jigsaw")) {
            if (room.gamePlayer == event.getPlayer()) {
                int slot = event.getSlot();
                if (this.room.isInArena(1, 3, 1, 3, slot)) {
                    Item handItem = event.getHeldItem();
                    if (handItem.getId() == 0) return;
                    String Hand = handItem.getId() + "-" + handItem.getDamage();
                    if (layout.contains(Hand)) {
                        int a = layout.indexOf(Hand);
                        if (Pos.get(a).equals(slot)) {
                            this.room.rank = room.rank + 1;
                            updateItem(handItem, slot);
                        }
                    }
                }
            }
        }
    }

    public void updateItem(Item handItem, int slot) {
        room.gameBox.setItem(slot, handItem);
        checkFinish();
    }

    public void checkFinish() {
        if (this.room.rank >= 9) {
            this.room.finish = true;
            room.gameComplete();
            onDefault();
        }
    }

    public void boardBuild() {
        Collections.shuffle(layout);
        for (int slot : Pos) {
            String[] item = layout.get(Pos.indexOf(slot)).split("-");
            room.gameBox.setItem(slot, Item.get(Integer.parseInt(item[0]), Integer.parseInt(item[1])));
        }
    }
}
