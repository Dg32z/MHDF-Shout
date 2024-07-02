package cn.chengzhiya.mhdfshout.utils;

import cn.chengzhiya.mhdfshout.api.PluginAPI;
import cn.chengzhiya.mhdfshout.entity.Shout;
import cn.chengzhiya.mhdfshout.main;
import com.alibaba.fastjson.JSONObject;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import lombok.Getter;
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
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.*;

public final class PluginUtil {
    public static YamlConfiguration Lang = null;
    @Getter
    public static HashMap<String, List<Shout>> shoutWaitHashMap = new HashMap<>();
    @Getter
    public static HashMap<String, Integer> shoutDelayHashMap = new HashMap<>();

    public static void registerCommand(Plugin plugin, CommandExecutor commandExecutor, String description, String ... aliases) {
        PluginCommand command = cn.chengzhiya.mhdfshout.utils.PluginUtil.getCommand(aliases[0], plugin);
        command.setAliases(Arrays.asList(aliases));
        command.setDescription(description);
        cn.chengzhiya.mhdfshout.utils.PluginUtil.getCommandMap().register(plugin.getDescription().getName(), command);
        command.setExecutor(commandExecutor);
    }

    private static PluginCommand getCommand(String name, Plugin plugin) {
        PluginCommand command = null;
        try {
            Constructor<PluginCommand> c = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
            c.setAccessible(true);
            command = c.newInstance(name, plugin);
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
        return PluginAPI.ChatColor(Lang.getString(Value));
    }

    public static void sendShout(String shoutType, boolean wait, Shout shout) {
        if (!main.main.getConfig().getBoolean("BungeeCordMode")) {
            List<Object> shoutWaitList = new ArrayList<>(Collections.singletonList(cn.chengzhiya.mhdfshout.utils.PluginUtil.shoutWaitHashMap.computeIfAbsent(shoutType, k -> new ArrayList<>())));
            shoutWaitList.add(shout);

            if (wait && shoutWaitList.size() == 1) {
                cn.chengzhiya.mhdfshout.utils.PluginUtil.startShout(shoutType);
            } else if (!wait) {
                cn.chengzhiya.mhdfshout.utils.PluginUtil.sendShout(shout);
            }
        } else {
            final JSONObject data = getJsonObject(shoutType, wait, shout);

            cn.chengzhiya.mhdfshout.utils.PluginUtil.sendPluginMessage(data.toJSONString());
        }
    }

    @NotNull
    private static JSONObject getJsonObject(String shoutType, boolean wait, Shout shout) {
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
        return data;
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
        List<Shout> shoutWaitList = cn.chengzhiya.mhdfshout.utils.PluginUtil.getShoutWaitHashMap().get(shoutType);
        Shout shout = cn.chengzhiya.mhdfshout.utils.PluginUtil.getShoutWaitHashMap().get(shoutType).get(0);
        cn.chengzhiya.mhdfshout.utils.PluginUtil.sendShout(shout);
        Bukkit.getScheduler().runTaskLaterAsynchronously(main.main, () -> {
            shoutWaitList.remove(shout);
            cn.chengzhiya.mhdfshout.utils.PluginUtil.getShoutWaitHashMap().put(shoutType, shoutWaitList);
            if (!shoutWaitList.isEmpty()) {
                cn.chengzhiya.mhdfshout.utils.PluginUtil.startShout(shoutType);
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
            cn.chengzhiya.mhdfshout.utils.PluginUtil.sendPluginMessage(data.toJSONString());
        }
    }

    public static void setDelay(String player, int delay) {
        cn.chengzhiya.mhdfshout.utils.PluginUtil.getShoutDelayHashMap().put(player, delay);
        if (main.main.getConfig().getBoolean("BungeeCordMode")) {
            JSONObject data = new JSONObject();
            data.put("action", "setDelay");
            JSONObject params = new JSONObject();
            params.put("player", player);
            params.put("delay", delay);
            data.put("params", params);
            cn.chengzhiya.mhdfshout.utils.PluginUtil.sendPluginMessage(data.toJSONString());
        }
    }

    public static void sendPluginMessage(String data) {
        block0: {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("MHDFShout");
            out.writeUTF(data);
            Iterator<? extends Player> iterator = Bukkit.getOnlinePlayers().iterator();
            if (!iterator.hasNext()) break block0;
            Player player = iterator.next();
            player.sendPluginMessage(main.main, "BungeeCord", out.toByteArray());
        }
    }

}

