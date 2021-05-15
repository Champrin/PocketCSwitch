package xyz.champrin.pocketcswitch.Game;


import cn.nukkit.event.EventHandler;
import cn.nukkit.event.inventory.InventoryClickEvent;
import cn.nukkit.item.Item;
import xyz.champrin.pocketcswitch.untils.GamingBoard;

import java.util.Random;

public class OnOneLine extends Game {

    public OnOneLine(GamingBoard room) {
        this.room = room;
        this.game_type = room.game_type;
    }

    private int firstItem = 0, firstSlot = 0;

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (this.room.finish) return;
        if (this.room.game != 1) return;
        if (this.game_type.equals("OnOneLine")) {
            if (room.gamePlayer == event.getPlayer()) {
                Item targetItem = event.getSourceItem();
                Item handItem = event.getHeldItem();
                int slot = event.getSlot();
                if (firstSlot == 0) {
                    this.firstItem = targetItem.getDamage();
                    this.firstSlot = slot;
                } else {
                    room.gameBox.setItem(slot, Item.get(159, firstItem));
                    room.gameBox.setItem(firstSlot, targetItem);
                    updateItem(firstItem);
                    this.firstItem = 0;
                    this.firstSlot = 0;
                }
                if (handItem.getId() == Item.DOOR_BLOCK) {
                    this.room.finish = true;
                    room.gameComplete();
                    onDefault();
                }
            }
        }
    }

    public void updateItem(int slot) {
        cleanItem(slot);
        this.check = 0;
        this.count = 1;
        MoveItem();
    }

    public void cleanItem(int slot) {
        Item targetItem = room.gameBox.getItem(slot);
        if (targetItem == null) return;
        if (targetItem.getId() != 159) return;
        int damage = targetItem.getDamage();
        int targetX = slot % 9;
        int targetY = slot / 9;
        check(slot, damage);
        check(targetX + (targetY + 1) * 9, damage);
        check(targetX + (targetY - 1) * 9, damage);
        if (targetX - 1 != 0) {
            check(targetX + 1 + targetY * 9, damage);
        }
        if (targetX + 1 != 8) {
            check(targetX - 1 + targetY * 9, damage);
        }
        if (check >= 3) {
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
    }

    public void check(int slot, int damage) {
        if (room.gameBox.getItem(slot).getDamage() == damage) {
            this.check = check + 1;
        }
    }

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
    }

    public void boardBuild() {
        for (int y = 0; y <= 5; y++) {
            for (int x = 0; x <= 8; x++) {
                int num = new Random().nextInt(5) + 4;
                room.gameBox.setItem(x + y * 9, Item.get(159, num));
            }
        }
    }
   /*TODO 自动靠拢
     public void clearAir(String direction, int xi, int xa, int yi, int ya, int zi, int za, Level level) {
        switch (direction) {
            case "x+":
            case "x-":
                for (int x = xi; x <= xa; x++) {
                    int a = 0;
                    for (int y = yi; y <= ya; y++) {
                        if (level.getItem(new Vector3(x, y, zi)).getId() == 0) {
                            a = a + 1;
                            if (y == ya && a == (int) this.room.data.get("width")) {
                                for (int i = yi; i <= ya; i++) {
                                    Item b = level.getItem(new Vector3(x + 1, i, zi));
                                    level.setItem(new Vector3(x, i, zi), b);
                                    level.setItem(b, Item.get(0, 0));

                                }
                            }
                        }
                    }
                }
                break;
            case "z+":
            case "z-":
                for (int z = zi; z <= za; z++) {
                    int a = 0;
                    for (int y = yi; y <= ya; y++) {
                        if (level.getItem(new Vector3(xi, y, z)).getId() == 0) {
                            a = a + 1;
                            if (y == ya && a == (int) this.room.data.get("width")) {
                                for (int i = yi; i <= ya; i++) {
                                    System.out.println(xi+""+i+""+z);
                                    Item b = level.getItem(new Vector3(xi, i, z + 1));
                                    level.setItem(new Vector3(xi, i, z), Item.get(0, 0));
                                    level.setItem(b, Item.get(0, 0));
                                }
                            }
                        }
                    }
                }
                break;
        }
    }

    public void checkFinish(String direction, int xi, int xa, int yi, int ya, int zi, int za, Level level) {
        int all = 0;
        switch (direction) {
            case "x+":
            case "x-":
                for (int x = xi; x <= xa; x++) {
                    for (int y = yi; y <= ya; y++) {
                        if (level.getItem(new Vector3(x, zi, y)).getId() == 0) {
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
                        if (level.getItem(new Vector3(xi, z, y)).getId() != 0) {
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
}