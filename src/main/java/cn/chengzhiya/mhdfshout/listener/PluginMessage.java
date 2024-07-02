/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.alibaba.fastjson.JSON
 *  com.alibaba.fastjson.JSONObject
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.messaging.PluginMessageListener
 *  org.jetbrains.annotations.NotNull
 */
package cn.chengzhiya.mhdfshout.listener;

import cn.chengzhiya.mhdfshout.entity.Shout;
import cn.chengzhiya.mhdfshout.utils.PluginUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class PluginMessage implements PluginMessageListener {

    private static final String CHANNEL_BUNGEECORD = "BungeeCord";
    private static final String SUBCHANNEL_MHDFSHOUT = "MHDFShout";

    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, byte @NotNull [] message) {
        if (!channel.equals(CHANNEL_BUNGEECORD)) {
            return;
        }

        try (DataInputStream in = new DataInputStream(new ByteArrayInputStream(message))) {
            String subchannel = in.readUTF();

            if (!subchannel.equals(SUBCHANNEL_MHDFSHOUT)) {
                return;
            }

            String jsonMessage = in.readUTF();
            JSONObject data = JSON.parseObject(jsonMessage);

            String action = data.getString("action");
            JSONObject params = data.getJSONObject("params");

            switch (action) {
                case "sendShout": {
                    String shoutType = params.getString("shoutType");
                    Shout shout = new Shout(
                            params.getString("shoutBossBarColor"),
                            params.getString("shoutBossBarBackground"),
                            params.getString("shoutMessage"),
                            params.getString("shoutSound"),
                            params.getInteger("shoutShowTime")
                    );

                    List<Object> shoutWaitList = new ArrayList<>(Collections.singletonList(PluginUtil.getShoutWaitHashMap().computeIfAbsent(shoutType, k -> new ArrayList<>())));
                    shoutWaitList.add(shout);

                    if (params.getBoolean("shoutWait")) {
                        if (shoutWaitList.size() == 1) {
                            PluginUtil.startShout(shoutType);
                        }
                    } else {
                        PluginUtil.sendShout(shout);
                    }
                    break;
                }
                case "getDelay": {
                    String playerKey = params.getString("player");
                    int delay = params.getIntValue("delay");

                    if (delay != -1) {
                        PluginUtil.getShoutDelayHashMap().put(playerKey, delay);
                    } else {
                        PluginUtil.getShoutDelayHashMap().remove(playerKey);
                    }
                    break;
                }
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }
}