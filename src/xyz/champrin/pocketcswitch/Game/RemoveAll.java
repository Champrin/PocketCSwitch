package xyz.champrin.pocketcswitch.Game;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.inventory.InventoryClickEvent;
import cn.nukkit.item.Item;
import xyz.champrin.pocketcswitch.untils.GamingBoard;

import java.util.Random;

public class RemoveAll extends Game {
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
    public RemoveAll(GamingBoard room) {
        this.room = room;
        this.count = 1;
        this.game_type = room.game_type;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (this.room.finish) return;
        if (this.room.game != 1) return;
        if (this.game_type.equals("RemoveAll")) {
            if (room.gamePlayer == event.getPlayer()) {
                Item targetItem = event.getSourceItem();
                if (targetItem.getId() != 159) return;
                updateItem(event.getSlot());
            }
        }
    }

    public void updateItem(int slot) {
        cleanItem(slot);
        this.count = 1;
        MoveItem();
        checkFinish();
    }

    public void cleanItem(int slot) {
        Item targetItem = room.gameBox.getItem(slot);
        if (targetItem == null) return;
        if (targetItem.getId() != 159) return;
        int damage = targetItem.getDamage();
        int targetX = slot % 9;
        int targetY = slot / 9;
        checkItem(slot, damage);
        checkItem(targetX + (targetY + 1) * 9, damage);
        checkItem(targetX + (targetY - 1) * 9, damage);
        if (targetX - 1 != 0) {
            checkItem(targetX + 1 + targetY * 9, damage);
        }
        if (targetX + 1 != 8) {
            checkItem(targetX - 1 + targetY * 9, damage);
        }
    }

    //从最下开始

    public void checkItem(int slot, int damage) {
        Item checkItem = room.gameBox.getItem(slot);
        if (checkItem.getDamage() == damage) {
            this.count = count * 2;
            room.gameBox.setItem(slot, Item.get(0, 0));
            this.check = check + 1;
            this.room.rank = this.room.rank + count;
            this.cleanItem(damage);
        }
    }

    public void FallingItem(int slot) {
        Item checkItem = room.gameBox.getItem(slot);
        Item underItem = room.gameBox.getItem(slot + 9);
        if (underItem.getId() == 0) {
            room.gameBox.setItem(slot, Item.get(0, 0));
            room.gameBox.setItem(slot + 9, checkItem);
            FallingItem(slot + 9);
        }
    }

    public void MoveItem() {
        for (int y = 0; y <= 5; y++) {
            for (int x = 0; x <= 8; x++) {
                if (room.gameBox.getItem(x + y * 9).getId() != 0) {
                    FallingItem(x + y * 9);
                }
            }
        }
    }

    public void checkFinish() {
        if (check >= 54) {
            this.room.finish = true;
            room.gameComplete();
            onDefault();
        }
    }

    /*TODO 自动靠拢
      public void clearAir(String direction, int xi, int xa, int yi, int ya, int zi, int za,  ) {

                 for (int x = xi; x <= xa; x++) {
                     int a = 0;
                     for (int y = yi; y <= ya; y++) {
                         if (.getItem(new Vector3(x, y, zi)).getId() == 0) {
                             a = a + 1;
                             if (y == ya && a == (int) this.room.data.get("width")) {
                                 for (int i = yi; i <= ya; i++) {
                                     Item b = .getItem(new Vector3(x + 1, i, zi));
                                     .setItem(new Vector3(x, i, zi), b);
                                     .setItem(b, Item.get(0, 0));

                                 }
                             }
                         }
                     }
                 }

     }

     public void checkFinish(String direction, int xi, int xa, int yi, int ya, int zi, int za,  ) {
         int all = 0;
         switch (direction) {
             case "x+":
             case "x-":
                 for (int x = xi; x <= xa; x++) {
                     for (int y = yi; y <= ya; y++) {
                         if (.getItem(new Vector3(x, zi, y)).getId() == 0) {
                             all = all + 1;
                         } else {
                             break;
                         }
                         if (y == ya) {
                             if (all >= area) {
                                 this.room.finish = true;
                                 this.count = 0;
                             }
                         }
                     }
                 }
                 break;
             case "z+":
             case "z-":
                 for (int z = zi; z <= za; z++) {
                     for (int y = yi; y <= ya; y++) {
                         if (.getItem(new Vector3(xi, z, y)).getId() != 0) {
                             all = all + 1;
                             System.out.println("all"+all);
                         } else {
                             break;
                         }
                         if (y == ya) {
                             if (all >= area) {
                                 this.room.finish = true;
                                 this.count = 0;
                             }
                         }
                     }
                 }
                 break;
         }
     }*/
    public void boardBuild() {
        for (int y = 0; y <= 5; y++) {
            for (int x = 0; x <= 8; x++) {
                int num = new Random().nextInt(5) + 4;
                room.gameBox.setItem(x + y * 9, Item.get(159, num));
            }
        }
    }
}