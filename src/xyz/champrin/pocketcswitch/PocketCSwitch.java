package xyz.champrin.pocketcswitch;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.form.window.FormWindowSimple;
import cn.nukkit.item.Item;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import xyz.champrin.pocketcswitch.libs.MetricsLite;
import xyz.champrin.pocketcswitch.untils.GameBox;
import xyz.champrin.pocketcswitch.untils.GamingBoard;

import java.io.File;
import java.util.*;


public class PocketCSwitch extends PluginBase implements Listener {
    //引用：本插件发送ChestUI部分参考了FakeInventories

    /*
    //TODO
    // 1.C2048
    // 2.MemoryMaster 记忆大师
    /*
* 【记忆翻牌】
一次翻两张牌，两张若是一样则被翻过来，尽量把全部牌都翻过来

* 【颜色记忆】
按照四种颜色闪光的顺序重复点击一遍，尽量做到无错误

* */
    public Config config, language;
    public final String PLUGIN_NAME = "PocketCSwitch";
    public final String PLUGIN_No = "9";
    public final String PREFIX = "§a=§l§6PocketCSwitch§r§a=";
    public final String GAME_NAME = "我的口袋游戏机!";
    public LinkedHashMap<String, LinkedHashMap<String, Object>> rooms_message = new LinkedHashMap<>();//房间基本信息
    public LinkedHashMap<String, GamingBoard> rooms = new LinkedHashMap<>();//开启的房间信息 存储Room实例
    public LinkedHashMap<Integer, String> Game = new LinkedHashMap<>();

    private static PocketCSwitch instance;

    public static PocketCSwitch getInstance() {
        return instance;
    }


    @Override
    public void onLoad() {
        Game.put(1, "LightsOut");//关灯
        Game.put(2, "OneToOne");
        Game.put(3, "Jigsaw");//拼图
        Game.put(4, "RemoveAll");//方块消消乐
        Game.put(5, "BlockPlay_4");//15字游戏
        Game.put(6, "BlockPlay_3");//8字游戏
        Game.put(7, "CrazyClick");//疯狂点击
        Game.put(8, "AvoidWhiteBlock");//别踩白块
        //Game.put(9, "Sudoku");//数独
        Game.put(10, "BeFaster");//快速反应
        Game.put(11, "HanoiTower");//汉诺塔游戏
        //Game.put(12, "C2048");//2048
        //Game.put(13, "OnOneLine");//宾果消消乐
        //Game.put(14, "MemoryMaster");//记忆大师
        //Game.put(15, "MemoryMaster");//颜色记忆
        instance = this;
    }

    @Override
    public void onEnable() {
        long start = new Date().getTime();
        this.getLogger().info(PREFIX + "  §d加载中。。。§e|作者：Champrin");
        this.getLogger().info(PREFIX + "  §e ==> Champrin的第§c" + PLUGIN_No + "§e款插件/小游戏 " + GAME_NAME + "！");
        this.getServer().getPluginManager().registerEvents(this, this);
        this.LoadConfig();
        new MetricsLite(this, 6865);
        this.getLogger().info(PREFIX + "  §d已加载完毕。。。");
        this.getLogger().info(PREFIX + "  §e加载耗时" + (new Date().getTime() - start) + "毫秒");
    }

    @Override
    public void onDisable() {
        //给每个房间结算结果
        if (!rooms.isEmpty()) {
            for (Map.Entry<String, GamingBoard> map : rooms.entrySet()) {
                map.getValue().stopGame();
            }
        }
    }

    public void LoadConfig() {
        this.getLogger().info("-配置文件加载中...");

        if (!new File(this.getDataFolder() + "/config.yml").exists()) {
            this.saveResource("config.yml", false);
        }
        this.config = new Config(this.getDataFolder() + "/config.yml", Config.YAML);
        if (config.get("BlockPlay_4") == null) {
            config.set("BlockPlay_4", new ArrayList<>(Arrays.asList("000-player", "000-player", "000-player")));
        }
        if (config.get("BlockPlay_3") == null) {
            config.set("BlockPlay_3", new ArrayList<>(Arrays.asList("000-player", "000-player", "000-player")));
        }
        if (config.get("CrazyClick") == null) {
            config.set("CrazyClick", new ArrayList<>(Arrays.asList("000-player", "000-player", "000-player")));
        }
        if (config.get("Sudoku") == null) {
            config.set("Sudoku", new ArrayList<>(Arrays.asList("000-player", "000-player", "000-player")));
        }
        if (config.get("AvoidWhiteBlock") == null) {
            config.set("AvoidWhiteBlock", new ArrayList<>(Arrays.asList("000-player", "000-player", "000-player")));
        }
        if (config.get("BeFaster") == null) {
            config.set("BeFaster", new ArrayList<>(Arrays.asList("000-player", "000-player", "000-player")));
        }
        if (config.get("HanoiTower") == null) {
            config.set("HanoiTower", new ArrayList<>(Arrays.asList("000-player", "000-player", "000-player")));
        }/*if (config.get("C2048") == null){
            config.set("C2048",new ArrayList<>(Arrays.asList("000-player","000-player","000-player")));
        }if (config.get("OnOneLine") == null){
            config.set("OnOneLine",new ArrayList<>(Arrays.asList("000-player","000-player","000-player")));
        }*/
        config.save();

        if (!new File(this.getDataFolder() + "/language.yml").exists()) {
            this.saveResource("language.yml", false);
        }
        this.language = new Config(this.getDataFolder() + "/language.yml", Config.YAML);

    }

    @EventHandler
    public void onTouch(PlayerInteractEvent event) {
        if (event.getPlayer().getInventory().getItemInHand().getCustomName().equals(">>  §l§6我的口袋游戏机  §r<<")) {
            GameBox.getInstance().addMainBoardWindow(event.getPlayer());
        }
    }


    public void Op_HelpMessage(CommandSender sender) {
        sender.sendMessage(">  §b==========" + PREFIX + "==========§r");
        sender.sendMessage(">  /pcs give  ------ §d给与CSwitch游戏机");
        sender.sendMessage(">  /pcs rank [房间名] ------ §d查看排行榜");
    }


    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if ("pcs".equals(command.getName())) {
            if (args.length < 1) {
                this.Op_HelpMessage(sender);
            } else {
                switch (args[0]) {
                    case "give":
                        if (sender instanceof Player) {
                            ((Player) sender).getInventory().addItem((Item.get(Item.FIREWORKS)).setCustomName(">>  §l§6我的口袋游戏机  §r<<"));
                        } else {
                            sender.sendMessage(">  请在游戏中运行");
                        }
                        break;
                    case "rank":
                        if (sender instanceof Player) {
                            FormWindowSimple window = new FormWindowSimple("PocketCSwitch排行榜", getRank());
                            ((Player) sender).showFormWindow(window);
                        } else {
                            sender.sendMessage(">  请在游戏中运行");
                        }
                        break;
                    case "help":
                    default:
                        this.Op_HelpMessage(sender);
                        break;
                }
            }
        }
        return true;
    }

    public String getRank() {
        StringBuilder rank = new StringBuilder("注:玩家名字前的数字代表耗时或分数\n   记录显示为000-player是暂无记录 \n");
        ;
        Map<String, Object> c = config.getAll();
        for (String m : c.keySet()) {
            String gameName = m;
            ArrayList<String> a = new ArrayList<>((Collection<? extends String>) config.get(gameName));
            gameName = getChineseName(gameName);
            for (int i = 0; i < 3; i++) {
                rank.append("\n").append("§l§6").append(gameName).append(":§r§f").append("第§l§c").append(i + 1).append("§r§f名:§f").append(a.get(i));
            }
            rank.append("\n");
        }
        return rank.toString();
    }

    public String getChineseName(String gameName) {
        switch (gameName) {
            case "LightsOut":
                return "关灯";
            case "OneToOne":
                return "一一对应";
            case "Jigsaw":
                return "拼图";
            case "RemoveAll":
                return "方块消消乐";
            case "OnOneLine":
                return "宾果消消乐";
            case "BlockPlay_4":
                return "4X4方块华容道";
            case "BlockPlay_3":
                return "3X3方块华容道";
            case "CrazyClick":
                return "疯狂点击";
            case "Sudoku":
                return "数独";
            case "C2048":
                return "2048";
            case "AvoidWhiteBlock":
                return "别踩白块";
            case "HanoiTower":
                return "汉诺塔游戏";
            case "BeFaster":
                return "快速反应";
            default:
                return null;
        }
    }

    public void checkRank(String gameName, long spendTime, String gamer) {
        try {
            ArrayList<String> a = new ArrayList<>((Collection<? extends String>) config.get(gameName));
            for (int i = 0; i < 3; i++) {
                String[] in = a.get(i).split("-");
                if (in[0].equals("000") || Integer.parseInt(in[0]) > spendTime) {
                    a.set(i, spendTime + "-" + gamer);
                    break;
                }
            }
            config.set(gameName, a);
            config.save();
        } catch (Exception e) {
        }
    }
}

