/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nsoz.server;

import com.nsoz.clan.Clan;
import com.nsoz.constants.ConstTime;
import com.nsoz.db.jdbc.DbManager;
import com.nsoz.util.Log;
import com.nsoz.util.NinjaUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author PC
 */
public class Ranked implements Runnable {

    // tính bxh top
    public static final String[] NAME = {"Top Đại gia", "Top Cao Thủ", "Top Gia tộc", "Top Hang động", "Top Nạp", "Top VXMM", "Top Boss"};

    public static final String[] RANKED_NAME = {"%d. %s có %s yên", "%d. %s trình độ cấp %d vào ngày %s",
            "%d. Gia tộc %s có trình độ cấp %d do %s làm tộc trưởng, thành viên %d/%d", "%d. %s nhận được %s rương", "%d. %s", "%d. %s có %s điểm", "%d. %s có %s điểm"};

    public static final Vector[] RANKED = new Vector[7];

    // load bxh
    public static void init() {
        refresh();
//        TimerTask timerTask = new TimerTask() {
//            @Override
//            public void run() {
//
//            }
//        };
//        long delay = 10000; // 10s
//        Timer timer = new Timer("Ranked");
//        timer.schedule(timerTask, 0, delay);
    }

    public static void refresh() {
        initTopDaiGia();
        initTopCaoThu();
        initTopGiaToc();
        initTopHangDong();
        initTopDauTu();
        initTopVXMM();
        initTopBoss();
    }

    public static void initTopDaiGia() {
        try {
            Vector<String> ranked = new Vector<>();
            Connection conn = DbManager.getInstance().getConnection(DbManager.SERVER);
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT `name`, `yen` FROM `players` WHERE `yen` > 0 AND `server_id` = ? ORDER BY `yen` DESC LIMIT 10;");
            stmt.setInt(1, Config.getInstance().getServerID());
            ResultSet res = stmt.executeQuery();
            int i = 1;
            while (res.next()) {
                ranked.add(String.format(RANKED_NAME[0], i, res.getString("name"),
                        NinjaUtils.getCurrency(res.getInt("yen"))));
                i++;
            }
            res.close();
            stmt.close();
            RANKED[0] = ranked;
        } catch (SQLException ex) {
            Log.error("init top dai gia err", ex);
        }
    }

    public static void initTopCaoThu() {
        try {
            Vector<String> ranked = new Vector<>();
            Connection conn = DbManager.getInstance().getConnection(DbManager.SERVER);
            String query = "SELECT `name`, CAST(JSON_EXTRACT(data, \"$.exp\") AS SIGNED) AS `exp`, CAST(JSON_EXTRACT(data, \"$.levelUpTime\") AS SIGNED) AS `levelUpTime`\n" +
                    "FROM players\n" +
                    "WHERE `server_id` = ?\n" +
                    "ORDER BY `exp` DESC, `levelUpTime` ASC\n" +
                    "LIMIT 10;";
//            PreparedStatement stmt = conn.prepareStatement(
//                    "SELECT `name`, CAST(JSON_EXTRACT(data, \"$.exp\") AS INT) AS `exp`, CAST(JSON_EXTRACT(data, \"$.levelUpTime\") AS INT) AS `levelUpTime` FROM players where `server_id` = ? ORDER BY `exp` DESC, `levelUpTime` ASC LIMIT 10;");
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, Config.getInstance().getServerID());
            ResultSet res = stmt.executeQuery();
            ArrayList<CaoThu> list = new ArrayList<>();
            while (res.next()) {
                CaoThu rank = new CaoThu();
                rank.level = NinjaUtils.getLevel(res.getLong("exp"));
                rank.time = res.getLong("levelUpTime");
                rank.name = res.getString("name");
                list.add(rank);
            }
            order(list);
            int i = 1;
            for (CaoThu c : list) {
                int level = c.level;
                String time = NinjaUtils.milliSecondsToDateString(c.time, "yyyy/MM/dd HH:mm:ss aa");
                ranked.add(String.format(RANKED_NAME[1], i, c.name, level, time));
                i++;
            }
            res.close();
            stmt.close();
            RANKED[1] = ranked;
        } catch (SQLException ex) {
            Log.error("init top cao thu", ex);
        }

    }

    public static void initTopGiaToc() {
        try {
            Vector<String> ranked = new Vector<>();
            Connection conn = DbManager.getInstance().getConnection(DbManager.SERVER);
            PreparedStatement stmt = conn
                    .prepareStatement("SELECT `id` FROM `clan` WHERE `level` > 1 AND `server_id` = ? ORDER BY `level` DESC LIMIT 10;");
            stmt.setInt(1, Config.getInstance().getServerID());
            ResultSet res = stmt.executeQuery();
            int i = 1;
            while (res.next()) {
                int id = res.getInt("id");
                Optional<Clan> g = Clan.getClanDAO().get(id);
                if (g != null && g.isPresent()) {
                    Clan clan = g.get();
                    ranked.add(String.format(RANKED_NAME[2], i, clan.getName(), clan.getLevel(), clan.getMainName(),
                            clan.getNumberMember(), clan.getMemberMax()));
                    i++;
                }
            }
            res.close();
            stmt.close();
            RANKED[2] = ranked;
        } catch (SQLException ex) {
            Log.error("init top gia toc", ex);
        }
    }

    public static void initTopHangDong() {
        try {
            Vector<String> ranked = new Vector<>();
            Connection conn = DbManager.getInstance().getConnection(DbManager.SERVER);
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT `name`, `rewardPB` FROM `players` WHERE `rewardPB` > 0 AND `server_id` = ? ORDER BY `rewardPB` DESC LIMIT 10;");
            stmt.setInt(1, Config.getInstance().getServerID());
            ResultSet res = stmt.executeQuery();
            int i = 1;
            while (res.next()) {
                ranked.add(String.format(RANKED_NAME[3], i, res.getString("name"),
                        NinjaUtils.getCurrency(res.getInt("rewardPB"))));
                i++;
            }
            res.close();
            stmt.close();
            RANKED[3] = ranked;
        } catch (SQLException ex) {
            Log.error("init top hang dong", ex);
        }
    }
    public static void initTopDauTu() { // bxh nạp
        try {
            Vector<String> ranked = new Vector<>();
            Connection conn = DbManager.getInstance().getConnection(DbManager.SERVER);
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT players.`name`, users.`total`\n" +
                            "FROM players\n" +
                            "JOIN users ON players.user_id = users.id\n" +
                            "ORDER BY users.total DESC\n" +
                            "LIMIT 10;");
//            stmt.setInt(1, Config.getInstance().getServerID());
            ResultSet res = stmt.executeQuery();
            int i = 1;
            while (res.next()) {
                ranked.add(String.format(RANKED_NAME[4], i, res.getString("name"),
                        NinjaUtils.getCurrency(res.getInt("total"))));
                i++;
            }
            res.close();
            stmt.close();
            RANKED[4] = ranked;
        } catch (SQLException ex) {
            Log.error("top nạp", ex);
        }
    }
    // bxh vxmm
    private static void initTopVXMM() {
        try {
            Vector<String> ranked = new Vector<>();
            Connection conn = DbManager.getInstance().getConnection(DbManager.SERVER);
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT `name`, `topvxmm` FROM `players` WHERE `topvxmm` > 0 AND `server_id` = ? ORDER BY `topvxmm` DESC LIMIT 10;");
            stmt.setInt(1, Config.getInstance().getServerID());
            ResultSet res = stmt.executeQuery();
            int i = 1;
            while (res.next()) {
                ranked.add(String.format(RANKED_NAME[5], i, res.getString("name"),
                        NinjaUtils.getCurrency(res.getInt("topvxmm"))));
                i++;
            }
            res.close();
            stmt.close();
            RANKED[5] = ranked;
        } catch (SQLException ex) {
            Log.error("init top vxmm err", ex);
        }
    }

    private static void initTopBoss() {
        try {
            Vector<String> ranked = new Vector<>();
            Connection conn = DbManager.getInstance().getConnection(DbManager.SERVER);
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT `name`, `topBoss` FROM `players` WHERE `topBoss` > 0 AND `server_id` = ? ORDER BY `topBoss` DESC LIMIT 10;");
            stmt.setInt(1, Config.getInstance().getServerID());
            ResultSet res = stmt.executeQuery();
            int i = 1;
            while (res.next()) {
                ranked.add(String.format(RANKED_NAME[6], i, res.getString("name"),
                        NinjaUtils.getCurrency(res.getInt("topBoss"))));
                i++;
            }
            res.close();
            stmt.close();
            RANKED[6] = ranked;
        } catch (SQLException ex) {
            Log.error("init top săn boss err", ex);
        }
    }



    private static void order(List<CaoThu> ranks) {

        Collections.sort(ranks, new Comparator() {

            public int compare(Object o1, Object o2) {

                Integer level1 = ((CaoThu) o1).level;
                Integer level2 = ((CaoThu) o2).level;
                int sComp = level2.compareTo(level1);
                if (sComp != 0) {
                    return sComp;
                }
                Long x1 = ((CaoThu) o1).time;
                Long x2 = ((CaoThu) o2).time;
                return x1.compareTo(x2);
            }
        });
    }

    @Override
    public void run() {
        while (Server.start) {
            try {
                Thread.sleep(GameData.DELAY_REFESH_RANK);
                refresh();
//                System.out.println("refresh rank");
            } catch (InterruptedException ex) {
                Logger.getLogger(AutoSaveData.class.getName()).log(Level.SEVERE, null, ex);
            }

        }    }
}

class CaoThu {

    public String name;
    public long time;
    public int level;
}
