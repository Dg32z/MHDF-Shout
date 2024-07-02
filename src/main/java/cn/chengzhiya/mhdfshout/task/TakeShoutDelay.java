package cn.chengzhiya.mhdfshout.task;

import cn.chengzhiya.mhdfshout.main;
import cn.chengzhiya.mhdfshout.utils.PluginUtil;
import org.bukkit.scheduler.BukkitRunnable;

public final class TakeShoutDelay
extends BukkitRunnable {
    public void run() {
        for (String player : PluginUtil.getShoutDelayHashMap().keySet()) {
            if (!main.main.getConfig().getBoolean("BungeeCordMode")) {
                if (PluginUtil.getShoutDelayHashMap().get(player) > 0) {
                    PluginUtil.getShoutDelayHashMap().put(player, PluginUtil.getShoutDelayHashMap().get(player) - 1);
                    continue;
                }
                PluginUtil.getShoutDelayHashMap().remove(player);
                continue;
            }
            PluginUtil.updateDelay(player);
        }
    }
}

