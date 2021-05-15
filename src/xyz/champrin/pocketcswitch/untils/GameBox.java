package xyz.champrin.pocketcswitch.untils;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.inventory.InventoryClickEvent;
import cn.nukkit.item.Item;
import xyz.champrin.pocketcswitch.PocketCSwitch;
import xyz.champrin.pocketcswitch.libs.DoubleChestFakeInventory;


import java.util.ArrayList;
import java.util.LinkedHashMap;

public class GameBox implements Listener {
    /*TODO seek ChestInventory.java*/
    private static GameBox instance;
    public static GameBox getInstance() {
        return instance;
    }
    public PocketCSwitch mainPlugin = PocketCSwitch.getInstance();

    private LinkedHashMap<String, DoubleChestFakeInventory> gameBox = new LinkedHashMap<>();

    public GameBox(){
        mainPlugin.getServer().getPluginManager().registerEvents(this, mainPlugin);
    }

    public DoubleChestFakeInventory getGameBoxID(String playerName) {
        return gameBox.get(playerName);
    }

    public void setGameBox(String playerName, DoubleChestFakeInventory box) {
        gameBox.put(playerName, box);
    }

    public void addMainBoardWindow(Player player) {
        DoubleChestFakeInventory box = new DoubleChestFakeInventory(player, "PocketSwitch");
        LinkedHashMap<Integer, Item> map =new LinkedHashMap<>();
        //TODO
        map.put(4, Item.get(1, 0, 2));
        map.put(6, Item.get(2, 0, 2));
        map.put(8, Item.get(3, 0, 2));
        box.setContents(map);
        this.setGameBox(player.getName(), box);
        player.addWindow(box);
    }

    public String[] getGameRule(String gameName) {
        ArrayList<String> rule = new ArrayList<>();
        switch (gameName) {
            case "LightsOut":
                rule.add("关灯");
                rule.add("§a胜利条件: §f使所有方块都是绿色羊毛");
                rule.add("§b玩法: §f点击黑色羊毛，其上下左右的黑色");
                rule.add("      §f羊毛都会变成绿色而周围的绿色羊");
                rule.add("      §f毛会重新变为黑色");
                break;
            case "OneToOne":
                rule.add("一一对应");
                rule.add("§b玩法: §f将手中的颜料，与游戏区域内的原料一一对应");
                rule.add("      §f对应正确得一分，错误扣1分，直至所有方块都");
                rule.add("      §f变为黑色时游戏结束");
                break;
            case "Jigsaw":
                rule.add("拼图");
                rule.add("§a胜利条件: §f使游戏区域内的方块变为与模板一模一样的图案");
                rule.add("§b玩法: §f将手中的方块正确点击游戏区域,要做到");
                rule.add("      §f手中的方块与模板的同一位置的方块一样");
                break;
            case "RemoveAll":
                rule.add("方块消消乐");
                rule.add("§a胜利条件: §f消除所有方块");
                rule.add("§b玩法: §f点击一个方块可以消除");
                rule.add("      §f当这个方块周围连着同样的");
                rule.add("      §f方块时,会被一起消除");
                break;
            case "OnOneLine":
                rule.add("宾果消消乐");
                rule.add("§a胜利条件: §f尽可能的消完方块");
                rule.add("§b玩法: §f点击一个方块与另外一个方块交换");
                rule.add("      §f当两个方块的连线有相同方块时，会消除所有同颜色的方块");
                rule.add("§c注意: §a当你认为你已经不能再进行下一步时,请切换“门”物品以结束");
                rule.add("      §a游戏！此游戏排行榜以分统计！");
                break;
            case "CrazyClick":
                rule.add("疯狂点击");
                rule.add("§b玩法: §f测试手的手速！");
                rule.add("      §f游戏开始后，尽你的可能快速点击游戏区");
                rule.add("      §f域内的 |钻石块|");
                break;
            case "Sudoku":
                rule.add("数独");
                rule.add("§a胜利条件: §f各种颜色的羊毛在每一行、每一列和每一宫中都只出现一次");
                rule.add("§b玩法: §f给出一定的已知羊毛颜色和解题条件");
                rule.add("      §f利用逻辑和推理,在其他的空格上填入羊毛");
                rule.add("      §f点击方块可以删除答案");
                break;
            case "C2048":
                rule.add("2048");
                rule.add("§a胜利条件: §f最终拼出绿色羊毛方块！");
                rule.add("§b玩法: §f用物品栏的物品，实现上下左右操作");
                rule.add("      §f你需要控制所有方块向同一个方向运动，两个相同的方块撞在一起之后会生成");
                rule.add("      §f下一级的方块，每次操作之后会随机生成一个方块");
                break;
            case "AvoidWhiteBlock":
                rule.add("别踩白块");
                rule.add("§a胜利条件: §f将所有黑块“踩齐”");
                rule.add("§b玩法: §f踩黑块,不能踩白块");
                break;
            case "HanoiTower":
                rule.add("汉诺塔游戏");
                rule.add("§a胜利条件: §f将左边第一列所有方块移动到右边第一列");
                rule.add("§b玩法: §f每次只能移动一个盘子。");
                rule.add("      §f以左边第一列为主要，下面的方块不能放在这个方块上面的方块的上面！");
                break;
            case "BeFaster":
                rule.add("快速反应");
                rule.add("§b玩法: §f在一定的时间内，尽可能的快速点击带颜色的方块");
                break;
            case "BlockPlay_4":
                rule.add("4X4方块华容道");
            case "BlockPlay_3":
                rule.add("3X3方块华容道");
                rule.add("§a胜利条件: §f将方块的顺序移为 数字从小到大1,2,3,...");
                rule.add("§b玩法: §f点击你想移动的方块，会与玻璃方块互相交换");
                break;
        }
        return rule.toArray(new String[0]);
    }
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (gameBox.get(event.getPlayer().getName()) != null){
            Item targetItem = event.getSourceItem();
            if (targetItem.getNamedTag().getString("gameName") != null){
                //TODO
            }
        }
    }
}