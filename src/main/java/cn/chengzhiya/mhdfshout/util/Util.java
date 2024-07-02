/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSONObject
 *  com.google.common.io.ByteArrayDataOutput
 *  com.google.common.io.ByteStreams
 *  net.kyori.adventure.bossbar.BossBar
 *  net.kyori.adventure.bossbar.BossBar$Color
 *  net.kyori.adventure.bossbar.BossBar$Overlay
 *  net.kyori.adventure.text.Component
 *  org.bukkit.Bukkit
 *  org.bukkit.Sound
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandMap
 *  org.bukkit.command.PluginCommand
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.SimplePluginManager
 */
package cn.chengzhiya.mhdfshout.util;

import cn.chengzhiya.mhdfshout.entity.Shout;
import cn.chengzhiya.mhdfshout.main;
import com.alibaba.fastjson.JSONObject;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.SimplePluginManager;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.*;

public final class Util {
    public static YamlConfiguration Lang = null;
    public static HashMap<String, List<Shout>> shoutWaitHashMap = new HashMap();
    public static HashMap<String, Integer> shoutDelayHashMap = new HashMap();

    public static void registerCommand(Plugin plugin, CommandExecutor commandExecutor, String description, String ... aliases) {
        PluginCommand command = Util.getCommand(aliases[0], plugin);
        command.setAliases(Arrays.asList(aliases));
        command.setDescription(description);
        Util.getCommandMap().register(plugin.getDescription().getName(), command);
        command.setExecutor(commandExecutor);
    }

    private static PluginCommand getCommand(String name, Plugin plugin) {
        PluginCommand command = null;
        try {
            Constructor c = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
            c.setAccessible(true);
            command = (PluginCommand)c.newInstance(name, plugin);
        }
        catch (Exception exception) {
            // empty catch block
        }
        return command;
    }

    private static CommandMap getCommandMap() {
        CommandMap commandMap = null;
        try {
            if (Bukkit.getPluginManager() instanceof SimplePluginManager) {
                Field f = SimplePluginManager.class.getDeclaredField("commandMap");
                f.setAccessible(true);
                commandMap = (CommandMap)f.get(Bukkit.getPluginManager());
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        return commandMap;
    }

    public static String i18n(String Value) {
        if (Lang == null) {
            File LangFile = new File(main.main.getDataFolder(), "lang.yml");
            Lang = YamlConfiguration.loadConfiguration(LangFile);
        }
        return cn.chengzhiya.mhdfshout.api.Util.ChatColor(Lang.getString(Value));
    }

    public static void sendShout(String shoutType, boolean wait, Shout shout) {
        if (!main.main.getConfig().getBoolean("BungeeCordMode")) {
            List<Object> shoutWaitList = new ArrayList<>(Collections.singletonList(Util.shoutWaitHashMap.computeIfAbsent(shoutType, k -> new ArrayList<>())));
            shoutWaitList.add(shout);

            if (wait && shoutWaitList.size() == 1) {
                Util.startShout(shoutType);
            } else if (!wait) {
                Util.sendShout(shout);
            }
        } else {
            JSONObject data = new JSONObject();
            data.put("action", "sendShout");

            JSONObject params = new JSONObject();
            params.put("shoutType", shoutType);
            params.put("shoutWait", wait);
            params.put("shoutBossBarColor", shout.getBossBarColor());
            params.put("shoutBossBarBackground", shout.getBossBarBackground());
            params.put("shoutMessage", shout.getMessage());
            params.put("shoutSound", shout.getSound());
            params.put("shoutShowTime", shout.getShowTime());

            data.put("params", params);

            Util.sendPluginMessage(data.toJSONString());
        }
    }

    public static void sendShout(Shout shout) {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            BossBar NullBossBar = BossBar.bossBar(Component.text(shout.getBossBarBackground()), 1.0f, BossBar.Color.valueOf(shout.getBossBarColor()), BossBar.Overlay.PROGRESS);
            BossBar ShoutBossBar = BossBar.bossBar(Component.text(shout.getMessage()), 1.0f, BossBar.Color.valueOf(shout.getBossBarColor()), BossBar.Overlay.PROGRESS);
            main.adventure().player(onlinePlayer).showBossBar(NullBossBar);
            main.adventure().player(onlinePlayer).showBossBar(ShoutBossBar);
            String[] sound = shout.getSound().split("\\|");
            try {
                onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.valueOf(sound[0]), Float.parseFloat(sound[1]), Float.parseFloat(sound[2]));
            }
            catch (Exception e) {
                onlinePlayer.playSound(onlinePlayer.getLocation(), sound[0], Float.parseFloat(sound[1]), Float.parseFloat(sound[2]));
            }
            Bukkit.getScheduler().runTaskLaterAsynchronously(main.main, () -> {
                main.adventure().player(onlinePlayer).hideBossBar(NullBossBar);
                main.adventure().player(onlinePlayer).hideBossBar(ShoutBossBar);
            }, 20L * (long)shout.getShowTime());
        }
    }

    public static void startShout(String shoutType) {
        List<Shout> shoutWaitList = Util.getShoutWaitHashMap().get(shoutType);
        Shout shout = Util.getShoutWaitHashMap().get(shoutType).get(0);
        Util.sendShout(shout);
        Bukkit.getScheduler().runTaskLaterAsynchronously(main.main, () -> {
            shoutWaitList.remove(shout);
            Util.getShoutWaitHashMap().put(shoutType, shoutWaitList);
            if (!shoutWaitList.isEmpty()) {
                Util.startShout(shoutType);
            }
        }, 20L * (long)shout.getShowTime());
    }

    public static void updateDelay(String player) {
        if (main.main.getConfig().getBoolean("BungeeCordMode")) {
            JSONObject data = new JSONObject();
            data.put("action", "getDelay");
            JSONObject params = new JSONObject();
            params.put("player", player);
            data.put("params", params);
            Util.sendPluginMessage(data.toJSONString());
        }
    }

    public static void setDelay(String player, int delay) {
        Util.getShoutDelayHashMap().put(player, delay);
        if (main.main.getConfig().getBoolean("BungeeCordMode")) {
            JSONObject data = new JSONObject();
            data.put("action", "setDelay");
            JSONObject params = new JSONObject();
            params.put("player", player);
            params.put("delay", delay);
            data.put("params", params);
            Util.sendPluginMessage(data.toJSONString());
        }
    }

    public static void sendPluginMessage(String data) {
        block0: {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("MHDFShout");
            out.writeUTF(data);
            Iterator iterator = Bukkit.getOnlinePlayers().iterator();
            if (!iterator.hasNext()) break block0;
            Player player = (Player)iterator.next();
            player.sendPluginMessage(main.main, "BungeeCord", out.toByteArray());
        }
    }

    public static HashMap<String, List<Shout>> getShoutWaitHashMap() {
        return shoutWaitHashMap;
    }

    public static HashMap<String, Integer> getShoutDelayHashMap() {
        return shoutDelayHashMap;
    }
}

