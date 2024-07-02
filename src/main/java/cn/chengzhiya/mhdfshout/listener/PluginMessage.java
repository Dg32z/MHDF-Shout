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
import cn.chengzhiya.mhdfshout.util.Util;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

public final class PluginMessage
implements PluginMessageListener {
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, byte @NotNull [] message) {
        if (!channel.equals("BungeeCord")) {
            return;
        }
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(message));
        try {
            String subchannel = in.readUTF();
            if (subchannel.equals("MHDFShout")) {
                JSONObject data = JSON.parseObject((String)in.readUTF());
                switch (data.getString("action")) {
                    case "sendShout": {
                        String shoutType = data.getJSONObject("params").getString("shoutType");
                        Shout shout = new Shout(data.getJSONObject("params").getString("shoutBossBarColor"), data.getJSONObject("params").getString("shoutBossBarBackground"), data.getJSONObject("params").getString("shoutMessage"), data.getJSONObject("params").getString("shoutSound"), data.getJSONObject("params").getInteger("shoutShowTime"));
                        List<Object> shoutWaitList = new ArrayList<>(Util.getShoutWaitHashMap().get(shoutType) != null ? Collections.singletonList(Util.getShoutWaitHashMap().get(shoutType)) : new ArrayList<>());
                        shoutWaitList.add(shout);
                        if (data.getJSONObject("params").getBoolean("shoutWait")) {
                            if (Util.getShoutWaitHashMap().get(shoutType).size() != 1) break;
                            Util.startShout(shoutType);
                            break;
                        }
                        Util.sendShout(shout);
                        break;
                    }
                    case "getDelay": {
                        if (data.getJSONObject("params").getIntValue("delay") != -1) {
                            Util.getShoutDelayHashMap().put(data.getJSONObject("params").getString("player"), data.getJSONObject("params").getIntValue("delay"));
                            break;
                        }
                        Util.getShoutDelayHashMap().remove(data.getJSONObject("params").getString("player"));
                    }
                }
            }
        }
        catch (IOException iOException) {
            // empty catch block
        }
    }
}

