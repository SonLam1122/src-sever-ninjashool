package com.nsoz.model.phancung;

public class UserModel {
    private String stt;
    private String ingame;
    private String status;
    private String type;

    public UserModel() {
    }

    public UserModel(String stt, String ingame, String status, String type) {
        this.stt = stt;
        this.ingame = ingame;
        this.status = status;
        this.type = type;
    }

    public String getStt() {
        return stt;
    }

    public void setStt(String stt) {
        this.stt = stt;
    }

    public String getIngame() {
        return ingame;
    }

    public void setIngame(String ingame) {
        this.ingame = ingame;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "stt='" + stt + '\'' +
                ", ingame='" + ingame + '\'' +
                ", status='" + status + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
