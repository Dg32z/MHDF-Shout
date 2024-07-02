package cn.chengzhiya.mhdfshout.command;

import cn.chengzhiya.mhdfshout.main;
import cn.chengzhiya.mhdfshout.util.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public final class ShoutReload
implements CommandExecutor {
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender.hasPermission("MHDFShout.Commands.Reload")) {
            main.main.reloadConfig();
            File LangFile = new File(main.main.getDataFolder(), "lang.yml");
            Util.Lang = YamlConfiguration.loadConfiguration(LangFile);
            sender.sendMessage(Util.i18n("ReloadDone"));
        } else {
            sender.sendMessage(Util.i18n("NotPermission"));
        }
        return false;
    }
}

