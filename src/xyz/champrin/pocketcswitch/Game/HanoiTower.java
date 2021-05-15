package xyz.champrin.pocketcswitch.Game;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.inventory.InventoryClickEvent;
import cn.nukkit.form.window.FormWindowSimple;
import cn.nukkit.item.Item;
import xyz.champrin.pocketcswitch.untils.GamingBoard;


public class HanoiTower extends Game {

    /**
     * 布局 y1 2 3 x3 4 5
     * * * * * * * * *
     * * * √ √ √ * * *
     * * * √ √ √ * * *
     * * * √ √ √ * * *
     * * * * * * * * *
     * * * * * * * * *
     **/
    public HanoiTower(GamingBoard plugin) {
        this.room = plugin;
        this.game_type = plugin.game_type;
    }

    private int clickDamage = -1, sourceSlot;

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (this.room.finish) return;
        if (this.room.game != 1) return;
        if (this.game_type.equals("HanoiTower")) {
            if (room.gamePlayer == event.getPlayer()) {
                int slot = event.getSlot();
                if (this.room.isInArena(3, 5, 1, 3, slot)) {
                    Item targetItem = event.getSourceItem();
                    if (targetItem.getDamage() == 15) return;
                    if (clickDamage == -1) {
                        this.clickDamage = targetItem.getDamage();
                        this.sourceSlot = slot;
                    } else {
                        updateItem(slot);
                        this.clickDamage = -1;
                    }
                }
            }
        }
    }

    public void updateItem(int slot) {
        int targetX = slot % 9;
        if (room.gameBox.getItem(slot).getDamage() == 0) {
            if (clickDamage < room.gameBox.getItem(targetX + 9).getDamage()) {
                room.gameBox.setItem(sourceSlot, Item.get(35, 0));
                room.gameBox.setItem(slot, Item.get(35, clickDamage));
            } else {
                FormWindowSimple window = new FormWindowSimple("§c操作错误", "");
                room.gamePlayer.showFormWindow(window);
            }
        } else {
            if (room.gameBox.getItem(targetX - 9).getDamage() == 0) {
                if (clickDamage < room.gameBox.getItem(slot).getDamage()) {
                    room.gameBox.setItem(sourceSlot, Item.get(35, 0));
                    room.gameBox.setItem(targetX - 9, Item.get(35, clickDamage));
                } else {
                    FormWindowSimple window = new FormWindowSimple("§c操作错误", "");
                    room.gamePlayer.showFormWindow(window);
                }
            } else {
                if (room.gameBox.getItem(targetX - 18).getDamage() == 0) {
                    if (clickDamage < room.gameBox.getItem(slot - 9).getDamage()) {
                        room.gameBox.setItem(sourceSlot, Item.get(35, 0));
                        room.gameBox.setItem(targetX - 18, Item.get(35, clickDamage));
                    } else {
                        FormWindowSimple window = new FormWindowSimple("§c操作错误", "");
                        room.gamePlayer.showFormWindow(window);
                    }
                } else {
                    FormWindowSimple window = new FormWindowSimple("§c操作错误", "");
                    room.gamePlayer.showFormWindow(window);
                }
            }
        }
        if (room.gameBox.getItem(32).getDamage() == 3 && room.gameBox.getItem(23).getDamage() == 2 && room.gameBox.getItem(14).getDamage() == 1) {
            room.gameComplete();
            onDefault();
        }
    }

    @Override
    public void checkFinish() {
    }

    @Override
    public void boardBuild() {
        room.gameBox.setItem(12, Item.get(35, 3));
        room.gameBox.setItem(21, Item.get(35, 2));
        room.gameBox.setItem(30, Item.get(35, 1));
    }

}
