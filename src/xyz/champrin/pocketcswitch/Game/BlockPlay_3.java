package xyz.champrin.pocketcswitch.Game;


import cn.nukkit.event.EventHandler;
import cn.nukkit.event.inventory.InventoryClickEvent;
import cn.nukkit.item.Item;
import xyz.champrin.pocketcswitch.untils.GamingBoard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;


public class BlockPlay_3 extends Game {
    /**
     * 布局
     * x=3 x=5
     * * * * * * * * *
     * * * √ √ √ * * *y=1
     * * * √ √ √ * * *
     * * * √ √ √ * * *y=3
     * * * * * * * * *
     * * * * * * * * *
     * x = slot%9 y = slot/9
     **/
    public ArrayList<Integer> checkLayout = new ArrayList<>(Arrays.asList(14, 1, 4, 5, 13, 9, 3, 11, 0));

    public BlockPlay_3(GamingBoard room) {
        this.room = room;
        this.game_type = room.game_type;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (this.room.finish) return;
        if (this.room.game != 1) return;
        if (this.game_type.equals("BlockPlay_3")) {
            if (room.gamePlayer == event.getPlayer()) {
                if (event.getSourceItem().getId() == 20) return;
                int slot = event.getSlot();
                if (this.room.isInArena(3, 5, 1, 3, slot)) {
                    updateItem(slot);
                }
            }
        }
    }

    public void updateItem(int slot) {
        Item sourceItem = room.gameBox.getItem(slot);
        int sourceX = slot % 9;
        int sourceY = slot / 9;
        if (checkItem(slot, sourceX + (sourceY + 1) * 9, sourceItem)) {
            if (checkItem(slot, sourceX + (sourceY - 1) * 9, sourceItem)) {
                if (checkItem(slot, sourceX + 1 + sourceY * 9, sourceItem)) {
                    checkItem(slot, sourceX - 1 + sourceY * 9, sourceItem);
                }
            }
        }
        checkArea();
        checkFinish();
    }

    public boolean checkItem(int sourceSlot, int targetSlot, Item sourceItem) {
        if (room.gameBox.getItem(targetSlot).getId() == 20) {
            room.gameBox.setItem(sourceSlot, Item.get(20, 0));
            room.gameBox.setItem(targetSlot, sourceItem);
            return false;
        }
        return true;
    }

    public ArrayList<Integer> check = new ArrayList<>();

    public void checkArea() {
        for (int y = 0; y <= 5; y++) {
            for (int x = 0; x <= 8; x++) {
                check.add(room.gameBox.getItem(x + y * 9).getDamage());
            }
        }
    }

    @Override
    public void checkFinish() {
        if (this.checkLayout.equals(this.check)) {
            this.room.finish = true;
            room.gameComplete();
        }
    }

    public void boardBuild() {
        ArrayList<Integer> layout = new ArrayList<>(Arrays.asList(14, 1, 4, 5, 13, 9, 3, 11));
        Collections.shuffle(layout);
        int a = 0;
        for (int y = 0; y <= 5; y++) {
            for (int x = 0; x <= 8; x++) {
                if (a == 8) break;
                room.gameBox.setItem(x + y * 9, Item.get(35, layout.get(a)));
                a = a + 1;
            }
        }
    }
}