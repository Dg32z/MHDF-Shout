/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.scheduler.BukkitRunnable
 */
package cn.chengzhiya.mhdfshout.task;

import cn.chengzhiya.mhdfshout.main;
import cn.chengzhiya.mhdfshout.util.Util;
import org.bukkit.scheduler.BukkitRunnable;

public final class TakeShoutDelay
extends BukkitRunnable {
    public void run() {
        for (String player : Util.getShoutDelayHashMap().keySet()) {
            if (!main.main.getConfig().getBoolean("BungeeCordMode")) {
                if (Util.getShoutDelayHashMap().get(player) > 0) {
                    Util.getShoutDelayHashMap().put(player, Util.getShoutDelayHashMap().get(player) - 1);
                    continue;
                }
                Util.getShoutDelayHashMap().remove(player);
                continue;
            }
            Util.updateDelay(player);
        }
    }
}

