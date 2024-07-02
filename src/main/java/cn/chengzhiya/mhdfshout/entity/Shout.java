/*
 * Decompiled with CFR 0.152.
 */
package cn.chengzhiya.mhdfshout.entity;

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

    public String getBossBarColor() {
        return this.BossBarColor;
    }

    public String getBossBarBackground() {
        return this.BossBarBackground;
    }

    public String getMessage() {
        return this.Message;
    }

    public String getSound() {
        return this.Sound;
    }

    public int getShowTime() {
        return this.ShowTime;
    }

    public void setBossBarColor(String BossBarColor) {
        this.BossBarColor = BossBarColor;
    }

    public void setBossBarBackground(String BossBarBackground) {
        this.BossBarBackground = BossBarBackground;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public void setSound(String Sound) {
        this.Sound = Sound;
    }

    public void setShowTime(int ShowTime) {
        this.ShowTime = ShowTime;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Shout)) {
            return false;
        }
        Shout other = (Shout)o;
        if (this.getShowTime() != other.getShowTime()) {
            return false;
        }
        String this$BossBarColor = this.getBossBarColor();
        String other$BossBarColor = other.getBossBarColor();
        if (this$BossBarColor == null ? other$BossBarColor != null : !this$BossBarColor.equals(other$BossBarColor)) {
            return false;
        }
        String this$BossBarBackground = this.getBossBarBackground();
        String other$BossBarBackground = other.getBossBarBackground();
        if (this$BossBarBackground == null ? other$BossBarBackground != null : !this$BossBarBackground.equals(other$BossBarBackground)) {
            return false;
        }
        String this$Message = this.getMessage();
        String other$Message = other.getMessage();
        if (this$Message == null ? other$Message != null : !this$Message.equals(other$Message)) {
            return false;
        }
        String this$Sound = this.getSound();
        String other$Sound = other.getSound();
        return !(this$Sound == null ? other$Sound != null : !this$Sound.equals(other$Sound));
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getShowTime();
        String $BossBarColor = this.getBossBarColor();
        result = result * 59 + ($BossBarColor == null ? 43 : $BossBarColor.hashCode());
        String $BossBarBackground = this.getBossBarBackground();
        result = result * 59 + ($BossBarBackground == null ? 43 : $BossBarBackground.hashCode());
        String $Message = this.getMessage();
        result = result * 59 + ($Message == null ? 43 : $Message.hashCode());
        String $Sound = this.getSound();
        result = result * 59 + ($Sound == null ? 43 : $Sound.hashCode());
        return result;
    }

    public String toString() {
        return "Shout(BossBarColor=" + this.getBossBarColor() + ", BossBarBackground=" + this.getBossBarBackground() + ", Message=" + this.getMessage() + ", Sound=" + this.getSound() + ", ShowTime=" + this.getShowTime() + ")";
    }
}

