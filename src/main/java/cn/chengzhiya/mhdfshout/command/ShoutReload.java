/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.jetbrains.annotations.NotNull
 */
package cn.chengzhiya.mhdfshout.command;

import cn.chengzhiya.mhdfshout.main;
import cn.chengzhiya.mhdfshout.util.Util;
import java.io.File;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

public final class ShoutReload
implements CommandExecutor {
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender.hasPermission("MHDFShout.Commands.Reload")) {
            main.main.reloadConfig();
            File LangFile = new File(main.main.getDataFolder(), "lang.yml");
            Util.Lang = YamlConfiguration.loadConfiguration((File)LangFile);
            sender.sendMessage(Util.i18n("ReloadDone"));
        } else {
            sender.sendMessage(Util.i18n("NotPermission"));
        }
        return false;
    }
}

