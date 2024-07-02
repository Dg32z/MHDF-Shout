/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.iridium.iridiumcolorapi.IridiumColorAPI
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.command.ConsoleCommandSender
 */
package cn.chengzhiya.mhdfshout.api;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;

public final class Util {
    public static String ChatColor(String Message) {
        Message = translateHexCodes(Message);
        return ChatColor.translateAlternateColorCodes('&', Message);
    }
    private static String translateHexCodes(String message) {
        final String hexPattern = "#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{8})";
        Matcher matcher = Pattern.compile(hexPattern).matcher(message);
        StringBuffer sb = new StringBuffer(message.length());
        while (matcher.find()) {
            String hex = matcher.group(1);
            net.md_5.bungee.api.ChatColor color = net.md_5.bungee.api.ChatColor.of("#" + hex);
            matcher.appendReplacement(sb, color.toString());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    public static void ColorLog(String Message) {
        ConsoleCommandSender consoleCommandSender = Bukkit.getConsoleSender();
        consoleCommandSender.sendMessage(Util.ChatColor(Message));
    }

    public static void CreateFolder(File file) {
        file.mkdirs();
    }

    public static void CreateFile(File file) throws IOException {
        file.createNewFile();
    }

    public static void CopyFile(File DataFolder, String FileName) {
        YamlFileUtil.SaveResource(DataFolder.getPath(), FileName, FileName, true);
    }
}

