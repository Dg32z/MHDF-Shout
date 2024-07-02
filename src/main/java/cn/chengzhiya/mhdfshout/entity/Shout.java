package cn.chengzhiya.mhdfshout.entity;

import lombok.Data;
import lombok.Getter;

@Getter
@Data
public final class Shout {
    String BossBarColor;
    String BossBarBackground;
    String Message;
    String Sound;
    int ShowTime;

    public Shout(String BossBarColor, String BossBarBackground, String Message, String Sound, int ShowTime) {
        this.BossBarColor = BossBarColor;
        this.BossBarBackground = BossBarBackground;
        this.Message = Message;
        this.Sound = Sound;
        this.ShowTime = ShowTime;
    }
}

