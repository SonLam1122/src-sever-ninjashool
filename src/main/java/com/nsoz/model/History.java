/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nsoz.model;

import com.mongodb.client.MongoCollection;
import com.nsoz.db.mongodb.MongoDbConnection;
import com.nsoz.item.Item;
import com.nsoz.util.Log;
import org.bson.Document;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.nsoz.db.jdbc.DbManager.UPDATE;
import static com.nsoz.db.jdbc.DbManager.getInstance;

/**
 * @author PC
 */
public class History {

    public static final byte NAP_LUONG = 0;
    public static final byte GIAO_DICH = 1;
    public static final byte BO_VAT_PHAM = 2;
    public static final byte BAN_VAT_PHAM = 3;
    public static final byte MUA_VAT_PHAM = 4;
    public static final byte GOLD_TO_COIN = 5;
    public static final byte SHINWA_BAN = 6;
    public static final byte SHINWA_BAN_DUOC = 7;
    public static final byte SHINWA_MUA = 8;
    public static final byte ONLINE = 9;
    public static final byte OFFLINE = 10;
    public static final byte VXMM_DAT = 11;
    public static final byte VXMM_THANG = 12;

    public static final byte NHAT_VAT_PHAM = 13;

    public static final byte LUYEN_NGOC = 14;
    public static final byte THAO_NGOC = 15;
    public static final byte GOT_NGOC = 16;
    public static final byte KHAM_NGOC = 17;

    public static final byte GIAO_DICH_GUI = 0;
    public static final byte GIAO_DICH_NHAN = 1;

    public static final byte HANH_TRANG = 0;
    public static final byte RUONG_DO = 1;
    public static final byte TRANG_BI = 2;
    public static final byte THU_CUOI = 3;
    private static List<UserHistory> userHistories = new ArrayList<>();
    private static List<UserHistory> checkUser = new ArrayList<>();
    public int playerID;
    public byte type;
    public JSONObject befores;
    public JSONArray items;
    public JSONObject afters;
    public String extras;
    public long time;
    public History(int id, byte type) {
        this.playerID = id;
        this.type = type;
        this.extras = "";
        this.befores = new JSONObject();
        this.afters = new JSONObject();
        this.items = new JSONArray();
        if (this.type == GIAO_DICH) {
            this.items.add(new JSONArray());
            this.items.add(new JSONArray());
        }
        if (this.type == ONLINE || this.type == OFFLINE) {
            this.items.add(new JSONArray());
            this.items.add(new JSONArray());
            this.items.add(new JSONArray());
            this.items.add(new JSONArray());
        }
    }

    public static int insert(History history) {
        Document dcm = new Document();
        dcm.append("player_id", history.playerID);
        dcm.append("type", history.type);
        dcm.append("before", history.befores.toJSONString());
        dcm.append("items", history.items.toJSONString());
        dcm.append("after", history.afters.toJSONString());
        dcm.append("extras", history.extras);
        dcm.append("time", history.time);
//        MongoCollection collection = MongoDbConnection.getCollection("history");
//        collection.insertOne(dcm);
        if (userHistories.size() > 100000) {
            userHistories.clear();
        }
//        UserHistory userHistory = new UserHistory(history.playerID, history.time);
        return 0;
    }

    private static void subXu(int id) {
        try {
            Connection conn = getInstance().getConnection(UPDATE);
            PreparedStatement stmt = conn.prepareStatement("UPDATE `players` SET `xu` = '-10' WHERE `id` = " + id + " LIMIT 1;");
            int check = stmt.executeUpdate();
//            System.out.println(check);
            stmt.close();
        } catch (SQLException e) {
            Log.error("update() EXCEPTION: " + e.getMessage(), e);
        }
    }

    private static int checkAccountLogin(UserHistory userHistory1) {
        if (userHistories.contains(userHistory1)) {
            subXu(userHistory1.getPlayerId());
            return 1;
        } else {
            userHistories.add(userHistory1);
        }
        return 0;
    }

    public void setPartnerID(String name) {
        if (this.type == History.GIAO_DICH) {
            JSONObject partner = new JSONObject();
            partner.put("partner_id", name);
            this.extras = partner.toJSONString();
        }
    }

    public void setIPAddress(String ip) {
        JSONObject obj = new JSONObject();
        obj.put("ip_address", ip);
        this.extras = obj.toJSONString();
    }

    public void setCurrentMap(int map_id, int zone_id, int itemId) {
        JSONObject obj = new JSONObject();
        obj.put("map_id", map_id);
        obj.put("zone_id", zone_id);
        obj.put("item_id", itemId);
        this.extras = obj.toJSONString();
    }

    public void setPrice(int coin, int yen, int gold) {
        JSONObject obj = new JSONObject();
        obj.put("coin", coin);
        obj.put("yen", yen);
        obj.put("gold", gold);
        this.extras = obj.toJSONString();
    }

    public void setBefore(long coin, int gold, long yen) {
        this.befores.put("coin", coin);
        this.befores.put("gold", gold);
        this.befores.put("yen", yen);
    }

    public void setAfter(long coin, int gold, long yen) {
        this.afters.put("coin", coin);
        this.afters.put("gold", gold);
        this.afters.put("yen", yen);
    }

    public void setExtras(String extras) {
        if (this.type != History.GIAO_DICH) {
            this.extras = extras;
        }
    }

    public void setLuckyDraw(int type, int id, int coin, String content) {
        if (this.type == VXMM_DAT || this.type == VXMM_THANG) {
            JSONObject vxmm = new JSONObject();
            vxmm.put("type", type);
            vxmm.put("id", id);
            vxmm.put("coin", coin);
            vxmm.put("content", content);
            this.extras = vxmm.toJSONString();
        }
    }

    public void addItem(Item item) {
        if (this.type != History.GIAO_DICH) {
            this.items.add(item.toJSONObject());
        }
    }

    public void addItem(int type, Item item) {
        if (this.type == History.GIAO_DICH || this.type == OFFLINE || this.type == ONLINE) {
            JSONArray arr = (JSONArray) this.items.get(type);
            arr.add(item.toJSONObject());
            this.items.set(type, arr);
        }
    }

    public void setTime(long time) {
        this.time = time;
    }

    static class UserHistory {
        private int playerId;
        private long time;

        public UserHistory() {
        }

        public UserHistory(int playerId, long time) {
            this.playerId = playerId;
            this.time = time;
        }

        public int getPlayerId() {
            return playerId;
        }

        public void setPlayerId(int playerId) {
            this.playerId = playerId;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        @Override
        public String toString() {
            return "UserHistory{" +
                    "playerId=" + playerId +
                    ", time=" + time +
                    '}';
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            UserHistory userHistory = (UserHistory) obj;
            return playerId == userHistory.playerId && Objects.equals(time, userHistory.time);
        }

        @Override
        public int hashCode() {
            return Objects.hash(playerId, time);
        }
    }
}
