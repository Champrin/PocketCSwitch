package xyz.champrin.pocketcswitch.Game;


import cn.nukkit.event.EventHandler;
import cn.nukkit.event.inventory.InventoryClickEvent;
import cn.nukkit.form.window.FormWindowSimple;
import cn.nukkit.item.Item;
import xyz.champrin.pocketcswitch.untils.GamingBoard;

import java.util.Random;

public class OneToOne extends Game {
    /**
     * 布局 ALL
     * x=0             x=8
     * * * * * * * * *y=0
     * * * * * * * * *
     * * * * * * * * *
     * * * * * * * * *
     * * * * * * * * *
     * * * * * * * * *y=5
     * x = slot%9 y = slot/9
     **/
    public OneToOne(GamingBoard room) {
        this.room = room;
        this.game_type = room.game_type;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (this.room.finish) return;
        if (this.room.game != 1) return;
        if (this.game_type.equals("OneToOne")) {
            if (room.gamePlayer == event.getPlayer()) {
                Item targetItem = event.getSourceItem();
                Item handItem = event.getHeldItem();
                if (handItem.getId() == 0 || targetItem.getId() == 0) return;
                if (targetItem.getId() == 35) {
                    this.count = count + 1;
                    room.gameBox.setItem(event.getSlot(), Item.get(20, 0));
                    if (targetItem.getDamage() + handItem.getDamage() == 15) {
                        this.room.rank = this.room.rank + 1;
                    } else {
                        this.room.rank = this.room.rank - 1;
                        FormWindowSimple window = new FormWindowSimple("§l§c哎呀！配对错误", "§a接下来要小心哦！");
                        room.gamePlayer.showFormWindow(window);
                    }
                }
                checkFinish();
            }
        }
    }

    @Override
    public void checkFinish() {
        if (count >= 54) {
            this.room.finish = true;
            room.gameComplete();
            onDefault();
        }
    }

    @Override
    public void boardBuild() {
        for (int x = 0; x <= 8; x++) {
            for (int y = 0; y <= 5; y++) {
                room.gameBox.setItem(x + y * 9, Item.get(35, new Random().nextInt(16)));
            }
        }
    }
}
