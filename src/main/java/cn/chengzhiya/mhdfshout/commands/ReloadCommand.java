package cn.chengzhiya.mhdfshout.commands;

import cn.chengzhiya.mhdfshout.main;
import cn.chengzhiya.mhdfshout.utils.PluginUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public final class ReloadCommand
implements CommandExecutor {
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender.hasPermission("MHDFShout.Commands.Reload")) {
            main.main.reloadConfig();
            File LangFile = new File(main.main.getDataFolder(), "lang.yml");
            PluginUtil.Lang = YamlConfiguration.loadConfiguration(LangFile);
            sender.sendMessage(PluginUtil.i18n("ReloadDone"));
        } else {
            sender.sendMessage(PluginUtil.i18n("NotPermission"));
        }
        return false;
    }
}

