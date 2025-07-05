package com.nsoz.server;

import com.nsoz.clan.Clan;
import com.nsoz.constants.MapName;
import com.nsoz.constants.SQLStatement;
import com.nsoz.db.jdbc.DbManager;
import com.nsoz.db.mongodb.MongoDbConnection;
import com.nsoz.effect.*;
import com.nsoz.event.Event;
import com.nsoz.event.Ranking;
import com.nsoz.item.ItemManager;
import com.nsoz.lib.ImageMap;
import com.nsoz.lib.ParseData;
import com.nsoz.map.MapManager;
import com.nsoz.map.TileMap;
import com.nsoz.map.War;
import com.nsoz.map.world.WorldManager;
import com.nsoz.mob.MobManager;
import com.nsoz.mob.MobTemplate;
import com.nsoz.model.*;
import com.nsoz.network.Session;
import com.nsoz.npc.NpcManager;
import com.nsoz.npc.NpcTemplate;
import com.nsoz.option.SkillOption;
import com.nsoz.skill.*;
import com.nsoz.socket.SocketIO;
import com.nsoz.stall.StallManager;
import com.nsoz.store.StoreManager;
import com.nsoz.task.Task;
import com.nsoz.task.TaskTemplate;
import com.nsoz.thiendia.ThienDiaManager;
import com.nsoz.util.Log;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author ASD
 */
public class Server {

    public static final Lock lock = new ReentrantLock();
    public static final ImageMap[][] IMAGE_MAP_ARR = new ImageMap[3][4];
    public static ServerSocket server;
    public static boolean start;
    public static int id;
    public static ArrayList<SkillPaint> sks;
    public static ArrayList<Part> parts;
    public static ArrayList<EffectCharPaint> efs;
    public static ArrayList<ArrowPaint> arrs;
    public static ArrayList<int[]> smallImg;
    public static long[] exps;
    public static byte[][] npcTasks;
    public static byte[][] mapTasks;
    public static JSONArray head_boc_dau, body_jump, body_normal, head_normal, body_boc_dau, head_jump, leg;
    public static byte[] version, map, data, skill;
    public static byte[] nj_arrow, nj_effect, nj_image, nj_part, nj_skill;
    public static long EXP_MAX = 0;
    public static boolean isStop;

    public static void initImageMap() {
        for (int i = 0; i < 4; i++) {
            IMAGE_MAP_ARR[0][i] = ImageMap.builder()
                    .mapID(MapName.RUNG_KAPPA)
                    .zoomLevel((byte) (i + 1))
                    .x(1746)
                    .y(408)
                    .w(80)
                    .h(50)
                    .build();
        }
        for (int i = 0; i < 4; i++) {
            IMAGE_MAP_ARR[1][i] = ImageMap.builder()
                    .mapID(MapName.KHE_NUI_CHOROCHORO)
                    .zoomLevel((byte) (i + 1))
                    .x(880)
                    .y(320)
                    .w(80)
                    .h(50)
                    .build();
        }
        for (int i = 0; i < 4; i++) {
            IMAGE_MAP_ARR[2][i] = ImageMap.builder()
                    .mapID(MapName.DAO_HEBI)
                    .zoomLevel((byte) (i + 1))
                    .x(180)
                    .y(260)
                    .w(80)
                    .h(40)
                    .build();
        }
    }

    public static boolean init() {
        start = false;
//        MongoDbConnection.connect();
        Task.arrTaskTemplate = new ArrayList<>();
        sks = new ArrayList<>();
        parts = new ArrayList<>();
        efs = new ArrayList<>();
        arrs = new ArrayList<>();
        parts = new ArrayList<>();
        smallImg = new ArrayList<>();
        head_boc_dau = new JSONArray();
        head_jump = new JSONArray();
        body_normal = new JSONArray();
        head_normal = new JSONArray();
        body_jump = new JSONArray();
        body_boc_dau = new JSONArray();
        leg = new JSONArray();

        try {

            if (!NpcManager.getInstance().load()) {
                return false;
            }
            if (!MobManager.getInstance().load()) {
                return false;
            }
            if (!MapManager.getInstance().load()) {
                return false;
            }
            Connection conn = DbManager.getInstance().getConnection(DbManager.SERVER);
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM `task`;",
                    ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);

            ResultSet resultSet = stmt.executeQuery();
            resultSet.last();
            int num = resultSet.getRow();
            resultSet.beforeFirst();
            npcTasks = new byte[num][];
            mapTasks = new byte[num][];
            int i = 0;
            while (resultSet.next()) {
                JSONArray jArr = (JSONArray) JSONValue.parse(resultSet.getString("npcs"));
                JSONArray jArr2 = (JSONArray) JSONValue.parse(resultSet.getString("maps"));
                npcTasks[i] = new byte[jArr.size()];
                mapTasks[i] = new byte[jArr.size()];
                for (int a = 0; a < npcTasks[i].length; a++) {
                    npcTasks[i][a] = ((Long) jArr.get(a)).byteValue();
                    mapTasks[i][a] = ((Long) jArr2.get(a)).byteValue();
                }
                i++;
            }
            resultSet.close();
            stmt.close();
            stmt = conn.prepareStatement(SQLStatement.GET_ALL_TASK_TEMPLATE);
            resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                short taskId = resultSet.getShort("taskId");
                String name = resultSet.getString("name");
                String detail = resultSet.getString("detail");
                JSONArray jArr = (JSONArray) JSONValue.parse(resultSet.getString("subnames"));
                String[] subnames = new String[jArr.size()];
                for (int a = 0; a < subnames.length; a++) {
                    subnames[a] = jArr.get(a).toString();
                }
                JSONArray jArr2 = (JSONArray) JSONValue.parse(resultSet.getString("counts"));
                short[] counts = new short[jArr2.size()];
                for (int a = 0; a < counts.length; a++) {
                    counts[a] = Short.parseShort(jArr2.get(a).toString());
                }
                short levelRequire = resultSet.getShort("level_require");
                JSONArray jsonArr = (JSONArray) JSONValue.parse(resultSet.getString("kill_mob"));
                short[][] mobs = new short[jsonArr.size()][2];
                for (int j = 0; j < mobs.length; j++) {
                    JSONArray jMob = (JSONArray) jsonArr.get(j);
                    mobs[j][0] = Short.parseShort(jMob.get(0).toString());
                    mobs[j][1] = Short.parseShort(jMob.get(1).toString());
                }
                jsonArr = (JSONArray) JSONValue.parse(resultSet.getString("pick_item"));
                short[] items = new short[jsonArr.size()];
                for (int j = 0; j < items.length; j++) {
                    items[j] = Short.parseShort(jsonArr.get(j).toString());
                }
                TaskTemplate task = TaskTemplate.builder()
                        .taskId(taskId)
                        .name(name)
                        .detail(detail)
                        .subNames(subnames)
                        .counts(counts)
                        .leveRequire(levelRequire)
                        .mobs(mobs)
                        .items(items)
                        .build();
                Task.arrTaskTemplate.add(task);
            }
            resultSet.close();
            stmt.close();
            stmt = conn.prepareStatement("SELECT * FROM `others`;");
            resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                if (name.equals("head_boc_dau")) {
                    head_boc_dau = (JSONArray) JSONValue.parse(resultSet.getString("value"));
                }
                if (name.equals("head_normal")) {
                    head_normal = (JSONArray) JSONValue.parse(resultSet.getString("value"));
                }
                if (name.equals("head_jump")) {
                    head_jump = (JSONArray) JSONValue.parse(resultSet.getString("value"));
                }
                if (name.equals("body_jump")) {
                    body_jump = (JSONArray) JSONValue.parse(resultSet.getString("value"));
                }
                if (name.equals("body_boc_dau")) {
                    body_boc_dau = (JSONArray) JSONValue.parse(resultSet.getString("value"));
                }
                if (name.equals("body_normal")) {
                    body_normal = (JSONArray) JSONValue.parse(resultSet.getString("value"));
                }
                if (name.equals("leg")) {
                    leg = (JSONArray) JSONValue.parse(resultSet.getString("value"));
                }
                if (name.equals("exp")) {
                    JSONArray value = (JSONArray) JSONValue.parse(resultSet.getString("value"));
                    if (name.equals("exp")) {
                        exps = new long[value.size()];
                        if (Config.getInstance().getMaxLv() == -1) {
                            for (i = 0; i < exps.length; i++) {
                                exps[i] = ((Long) value.get(i)).longValue();
                            }

                        } else {
                            for (i = 0; i < Config.getInstance().getMaxLv()+1; i++) { // max lv 99
                                exps[i] = ((Long) value.get(i)).longValue();
                            }
                        }
                    }
                }
            }
            resultSet.close();
            stmt.close();
            MountDataManager.getInstance().init();
            EffectTemplateManager.getInstance().init();
            EffectDataManager.getInstance().load();
            ItemManager.getInstance().load();
            GameData.getInstance().init();
            StoreManager.getInstance().init();
            if (!StoreManager.getInstance().load()) {
                return false;
            }
            stmt = conn.prepareStatement("SELECT * FROM `nj_skill`;");
            resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                SkillPaint p = new SkillPaint();
                p.id = resultSet.getShort("skillId");
                p.effId = resultSet.getShort("effId");
                p.numEff = resultSet.getByte("numEff");
                JSONArray jA = (JSONArray) JSONValue.parse(resultSet.getString("skillStand"));
                p.skillStand = new SkillInfoPaint[jA.size()];
                for (int k = 0; k < p.skillStand.length; k++) {
                    JSONObject o = (JSONObject) jA.get(k);
                    p.skillStand[k] = new SkillInfoPaint();
                    p.skillStand[k].status = ((Long) o.get("status")).byteValue();
                    p.skillStand[k].effS0Id = ((Long) o.get("effS0Id")).shortValue();
                    p.skillStand[k].e0dx = ((Long) o.get("e0dx")).shortValue();
                    p.skillStand[k].e0dy = ((Long) o.get("e0dy")).shortValue();
                    p.skillStand[k].effS1Id = ((Long) o.get("effS1Id")).shortValue();
                    p.skillStand[k].e1dx = ((Long) o.get("e1dx")).shortValue();
                    p.skillStand[k].e1dy = ((Long) o.get("e1dy")).shortValue();
                    p.skillStand[k].effS2Id = ((Long) o.get("effS2Id")).shortValue();
                    p.skillStand[k].e2dx = ((Long) o.get("e2dx")).shortValue();
                    p.skillStand[k].e2dy = ((Long) o.get("e2dy")).shortValue();
                    p.skillStand[k].arrowId = ((Long) o.get("arrowId")).shortValue();
                    p.skillStand[k].adx = ((Long) o.get("adx")).shortValue();
                    p.skillStand[k].ady = ((Long) o.get("ady")).shortValue();
                }
                jA = (JSONArray) JSONValue.parse(resultSet.getString("skillFly"));
                p.skillfly = new SkillInfoPaint[jA.size()];
                for (int k = 0; k < p.skillfly.length; k++) {
                    JSONObject o = (JSONObject) jA.get(k);
                    p.skillfly[k] = new SkillInfoPaint();
                    p.skillfly[k].status = ((Long) o.get("status")).byteValue();
                    p.skillfly[k].effS0Id = ((Long) o.get("effS0Id")).shortValue();
                    p.skillfly[k].e0dx = ((Long) o.get("e0dx")).shortValue();
                    p.skillfly[k].e0dy = ((Long) o.get("e0dy")).shortValue();
                    p.skillfly[k].effS1Id = ((Long) o.get("effS1Id")).shortValue();
                    p.skillfly[k].e1dx = ((Long) o.get("e1dx")).shortValue();
                    p.skillfly[k].e1dy = ((Long) o.get("e1dy")).shortValue();
                    p.skillfly[k].effS2Id = ((Long) o.get("effS2Id")).shortValue();
                    p.skillfly[k].e2dx = ((Long) o.get("e2dx")).shortValue();
                    p.skillfly[k].e2dy = ((Long) o.get("e2dy")).shortValue();
                    p.skillfly[k].arrowId = ((Long) o.get("arrowId")).shortValue();
                    p.skillfly[k].adx = ((Long) o.get("adx")).shortValue();
                    p.skillfly[k].ady = ((Long) o.get("ady")).shortValue();
                }
                sks.add(p);
            }
            resultSet.close();
            stmt.close();
            stmt = conn.prepareStatement("SELECT * FROM `nj_part`;");
            resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                byte type = resultSet.getByte("type");
                JSONArray jA = (JSONArray) JSONValue.parse(resultSet.getString("part"));
                Part part = new Part(type);
                for (int k = 0; k < part.pi.length; k++) {
                    JSONObject o = (JSONObject) jA.get(k);
                    part.pi[k] = new PartImage();
                    part.pi[k].id = ((Long) o.get("id")).shortValue();
                    part.pi[k].dx = ((Long) o.get("dx")).byteValue();
                    part.pi[k].dy = ((Long) o.get("dy")).byteValue();
                }
                parts.add(part);
            }
            resultSet.close();
            stmt.close();
            stmt = conn.prepareStatement("SELECT * FROM `nj_image`;");
            resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                int[] smallImage = new int[5];
                try {
                    ParseData p = new ParseData(
                            (JSONObject) JSONValue.parse(resultSet.getString("smallImage")));
                    smallImage[0] = p.getInt("id");
                    smallImage[1] = p.getShort("x");
                    smallImage[2] = p.getShort("y");
                    smallImage[3] = p.getShort("w");
                    smallImage[4] = p.getShort("h");
                } catch (ClassCastException c) {
                    JSONArray jA = (JSONArray) JSONValue.parse(resultSet.getString("smallImage"));
                    smallImage[0] = ((Long) jA.get(0)).intValue();
                    smallImage[1] = ((Long) jA.get(1)).intValue();
                    smallImage[2] = ((Long) jA.get(2)).intValue();
                    smallImage[3] = ((Long) jA.get(3)).intValue();
                    smallImage[4] = ((Long) jA.get(4)).intValue();
                }

                smallImg.add(smallImage);
            }
            resultSet.close();
            stmt.close();
            stmt = conn.prepareStatement("SELECT * FROM `nj_arrow`;");
            resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                ArrowPaint p = new ArrowPaint();
                p.id = resultSet.getShort("id");
                JSONArray jA = (JSONArray) JSONValue.parse(resultSet.getString("imgId"));
                p.imgId[0] = ((Long) jA.get(0)).shortValue();
                p.imgId[1] = ((Long) jA.get(1)).shortValue();
                p.imgId[2] = ((Long) jA.get(2)).shortValue();
                arrs.add(p);
            }
            resultSet.close();
            stmt.close();
            stmt = conn.prepareStatement("SELECT * FROM `nj_effect`;");
            resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                EffectCharPaint effectCharInfo = new EffectCharPaint();
                effectCharInfo.idEf = resultSet.getShort("id");
                JSONArray jA = (JSONArray) JSONValue.parse(resultSet.getString("info"));
                effectCharInfo.arrEfInfo = new EffectInfoPaint[jA.size()];


                for (int k = 0; k < effectCharInfo.arrEfInfo.length; k++) {
                    JSONObject o = (JSONObject) jA.get(k);
                    effectCharInfo.arrEfInfo[k] = new EffectInfoPaint();
                    try {
                        if (o.get("imgId") != null) {
                        effectCharInfo.arrEfInfo[k].idImg = ((Long) o.get("imgId")).shortValue();
                        }
                    } catch (NullPointerException nullPointerException) {
                        if ( o.get("id")  != null) {
                        effectCharInfo.arrEfInfo[k].idImg = ((Long) o.get("id")).shortValue();
                    }
                    }
                    effectCharInfo.arrEfInfo[k].dx = ((Long) o.get("dx")).byteValue();
                    effectCharInfo.arrEfInfo[k].dy = ((Long) o.get("dy")).byteValue();
                }




                efs.add(effectCharInfo);
            }
            resultSet.close();
            stmt.close();
            for (long exp : exps) {
                EXP_MAX += exp;
            }
            EXP_MAX -= 1;
            EffectAutoDataManager.getInstance().load();
            ItemManager.getInstance().init();
            setDataArrow();
            setDataEffect();
            setDataImage();
            setDataPart();
            setDataSkill();
            setData();
            setMap();
            ItemManager.getInstance().setData();
            setSkill();
            setVersion();
            Event.init();
            Event event = Event.getEvent();
            if (event != null) {
                event.loadEventPoint();
                event.initStore();
            }
            EffectDataManager.getInstance().setData();
            MapManager.getInstance().init();
            Ranking.loadListLeaderBoard();
            Clan.getClanDAO().load();
            Ranked.init();
            ThienDiaManager.getInstance().init();
            RandomItem.init();
            initImageMap();
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    private static void setVersion() {
        try {
            ByteArrayOutputStream bas = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(bas);
            int ver = Config.getInstance().getDataVersion();
            if (Config.getInstance().getMaxPercentAdd() > 0) {
                ver += 1;
            }
            dos.writeByte(ver);
            dos.writeByte(Config.getInstance().getMapVersion());
            dos.writeByte(Config.getInstance().getSkillVersion());
            dos.writeByte(Config.getInstance().getItemVersion());
            int num = head_jump.size();
            dos.writeByte(num);
            for (int i = 0; i < num; i++) {
                ParseData p = new ParseData((JSONObject) head_jump.get(i));
                JSONArray item = p.getJSONArray("item");
                int lent = item.size();
                dos.writeByte(lent * 3 + 2);
                dos.writeShort(p.getShort("id"));
                dos.writeShort(p.getShort("small"));
                for (int a = 0; a < lent; a++) {
                    ParseData p2 = new ParseData((JSONObject) item.get(a));
                    dos.writeShort(p2.getShort("id"));
                    dos.writeShort(p2.getShort("dx"));
                    dos.writeShort(p2.getShort("dy"));
                }
            }
            for (int i = 0; i < num; i++) {
                ParseData p = new ParseData((JSONObject) head_normal.get(i));
                JSONArray item = p.getJSONArray("item");
                int lent = item.size();
                dos.writeByte(lent * 3 + 2);
                dos.writeShort(p.getShort("id"));
                dos.writeShort(p.getShort("small"));
                for (int a = 0; a < lent; a++) {
                    ParseData p2 = new ParseData((JSONObject) item.get(a));
                    dos.writeShort(p2.getShort("id"));
                    dos.writeShort(p2.getShort("dx"));
                    dos.writeShort(p2.getShort("dy"));
                }
            }
            for (int i = 0; i < num; i++) {
                ParseData p = new ParseData((JSONObject) head_boc_dau.get(i));
                JSONArray item = p.getJSONArray("item");
                int lent = item.size();
                dos.writeByte(lent * 3 + 2);
                dos.writeShort(p.getShort("id"));
                dos.writeShort(p.getShort("small"));
                for (int a = 0; a < lent; a++) {
                    ParseData p2 = new ParseData((JSONObject) item.get(a));
                    dos.writeShort(p2.getShort("id"));
                    dos.writeShort(p2.getShort("dx"));
                    dos.writeShort(p2.getShort("dy"));
                }
            }
            num = leg.size();
            dos.writeByte(num * 2);
            for (int i = 0; i < num; i++) {
                ParseData p = new ParseData((JSONObject) leg.get(i));
                dos.writeShort(p.getShort("id"));
                dos.writeShort(p.getShort("small"));
            }
            num = body_jump.size();
            dos.writeByte(num);
            for (int i = 0; i < num; i++) {
                ParseData p = new ParseData((JSONObject) body_jump.get(i));
                JSONArray item = p.getJSONArray("item");
                int lent = item.size();
                dos.writeByte(lent * 3 + 2);
                dos.writeShort(p.getShort("id"));
                dos.writeShort(p.getShort("small"));
                for (int a = 0; a < lent; a++) {
                    ParseData p2 = new ParseData((JSONObject) item.get(a));
                    dos.writeShort(p2.getShort("id"));
                    dos.writeShort(p2.getShort("dx"));
                    dos.writeShort(p2.getShort("dy"));
                }
            }
            for (int i = 0; i < num; i++) {
                ParseData p = new ParseData((JSONObject) body_normal.get(i));
                JSONArray item = p.getJSONArray("item");
                int lent = item.size();
                dos.writeByte(lent * 3 + 2);
                dos.writeShort(p.getShort("id"));
                dos.writeShort(p.getShort("small"));
                for (int a = 0; a < lent; a++) {
                    ParseData p2 = new ParseData((JSONObject) item.get(a));
                    dos.writeShort(p2.getShort("id"));
                    dos.writeShort(p2.getShort("dx"));
                    dos.writeShort(p2.getShort("dy"));
                }
            }
            for (int i = 0; i < num; i++) {
                ParseData p = new ParseData((JSONObject) body_boc_dau.get(i));
                JSONArray item = p.getJSONArray("item");
                int lent = item.size();
                dos.writeByte(lent * 3 + 2);
                dos.writeShort(p.getShort("id"));
                dos.writeShort(p.getShort("small"));
                for (int a = 0; a < lent; a++) {
                    ParseData p2 = new ParseData((JSONObject) item.get(a));
                    dos.writeShort(p2.getShort("id"));
                    dos.writeShort(p2.getShort("dx"));
                    dos.writeShort(p2.getShort("dy"));
                }
            }
            List<MountData> mountDatas = MountDataManager.getInstance().getMountDatas();
            dos.writeByte(mountDatas.size());
            for (MountData mountData : mountDatas) {
                dos.writeShort(mountData.getItemID());
                short[][] data = mountData.getData();
                for (short[] frames : data) {
                    dos.writeByte(frames.length);
                    for (short frame : frames) {
                        dos.writeShort(frame);
                    }
                }
            }
            dos.flush();
            version = bas.toByteArray();
            dos.close();
            bas.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void setDataArrow() {
        try {
            ByteArrayOutputStream arrows = new ByteArrayOutputStream();
            DataOutputStream ds = new DataOutputStream(arrows);
            ds.writeShort(Server.arrs.size());
            for (ArrowPaint arr : Server.arrs) {
                ds.writeShort(arr.id);
                ds.writeShort(arr.imgId[0]);
                ds.writeShort(arr.imgId[1]);
                ds.writeShort(arr.imgId[2]);
            }
            ds.flush();
            nj_arrow = arrows.toByteArray();
            ds.close();
            arrows.close();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void setDataEffect() {
        try {
            ByteArrayOutputStream effects = new ByteArrayOutputStream();
            DataOutputStream ds = new DataOutputStream(effects);
            ds.writeShort(Server.efs.size());
            for (EffectCharPaint eff : Server.efs) {
                ds.writeShort(eff.idEf);
                ds.writeByte(eff.arrEfInfo.length);
                for (EffectInfoPaint eff2 : eff.arrEfInfo) {
                    ds.writeShort(eff2.idImg);
                    ds.writeByte(eff2.dx);
                    ds.writeByte(eff2.dy);
                }
            }
            ds.flush();
            nj_effect = effects.toByteArray();
            ds.close();
            effects.close();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void setDataImage() {
        try {
            ByteArrayOutputStream image = new ByteArrayOutputStream();
            DataOutputStream ds = new DataOutputStream(image);
            ds.writeShort(Server.smallImg.size());
            for (int[] img : Server.smallImg) {
                ds.writeByte(img[0]);
                ds.writeShort(img[1]);
                ds.writeShort(img[2]);
                ds.writeShort(img[3]);
                ds.writeShort(img[4]);
            }
            ds.flush();
            nj_image = image.toByteArray();
            ds.close();
            image.close();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void setDataPart() {
        try {
            ByteArrayOutputStream parts = new ByteArrayOutputStream();
            DataOutputStream ds = new DataOutputStream(parts);
            ds.writeShort(Server.parts.size());
            for (Part p : Server.parts) {
                ds.writeByte(p.type);
                for (PartImage pi : p.pi) {
                    ds.writeShort(pi.id);
                    ds.writeByte(pi.dx);
                    ds.writeByte(pi.dy);
                }
            }
            ds.flush();
            nj_part = parts.toByteArray();
            ds.close();
            parts.close();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void setDataSkill() {
        try {
            ByteArrayOutputStream skills = new ByteArrayOutputStream();
            DataOutputStream ds = new DataOutputStream(skills);
            ds.writeShort(Server.sks.size());
            for (SkillPaint p : Server.sks) {
                ds.writeShort(p.id);
                ds.writeShort(p.effId);
                ds.writeByte(p.numEff);
                ds.writeByte(p.skillStand.length);
                for (SkillInfoPaint skillStand : p.skillStand) {
                    ds.writeByte(skillStand.status);
                    ds.writeShort(skillStand.effS0Id);
                    ds.writeShort(skillStand.e0dx);
                    ds.writeShort(skillStand.e0dy);
                    ds.writeShort(skillStand.effS1Id);
                    ds.writeShort(skillStand.e1dx);
                    ds.writeShort(skillStand.e1dy);
                    ds.writeShort(skillStand.effS2Id);
                    ds.writeShort(skillStand.e2dx);
                    ds.writeShort(skillStand.e2dy);
                    ds.writeShort(skillStand.arrowId);
                    ds.writeShort(skillStand.adx);
                    ds.writeShort(skillStand.ady);
                }
                ds.writeByte(p.skillfly.length);
                for (SkillInfoPaint skillfly : p.skillfly) {
                    ds.writeByte(skillfly.status);
                    ds.writeShort(skillfly.effS0Id);
                    ds.writeShort(skillfly.e0dx);
                    ds.writeShort(skillfly.e0dy);
                    ds.writeShort(skillfly.effS1Id);
                    ds.writeShort(skillfly.e1dx);
                    ds.writeShort(skillfly.e1dy);
                    ds.writeShort(skillfly.effS2Id);
                    ds.writeShort(skillfly.e2dx);
                    ds.writeShort(skillfly.e2dy);
                    ds.writeShort(skillfly.arrowId);
                    ds.writeShort(skillfly.adx);
                    ds.writeShort(skillfly.ady);
                }
            }
            ds.flush();
            nj_skill = skills.toByteArray();
            ds.close();
            skills.close();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void setMap() {
        try {
            List<TileMap> list = MapManager.getInstance().getTileMaps();
            ByteArrayOutputStream bas = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(bas);
            dos.writeByte(Config.getInstance().getMapVersion());
            dos.writeByte(list.size());
            for (TileMap map : list) {
                dos.writeUTF(map.name);
            }
            List<NpcTemplate> npcs = NpcManager.getInstance().getNpcTemplates();
            dos.writeByte(npcs.size());
            for (NpcTemplate npc : npcs) {
                dos.writeUTF(npc.name);
                dos.writeShort(npc.headId);
                dos.writeShort(npc.bodyId);
                dos.writeShort(npc.legId);
                String[][] menu = npc.menu;
                dos.writeByte(menu.length);
                for (String[] m : menu) {
                    dos.writeByte(m.length);
                    for (String s : m) {
                        dos.writeUTF(s);
                    }
                }
            }
            List<MobTemplate> mobTemplates = MobManager.getInstance().getMobs();
            dos.writeShort(mobTemplates.size());
            for (MobTemplate mob : mobTemplates) {
                dos.writeByte(mob.type);
                dos.writeUTF(mob.name);
                dos.writeInt(mob.hp);
                dos.writeByte(mob.rangeMove);
                dos.writeByte(mob.speed);
            }
            dos.flush();
            map = bas.toByteArray();
            dos.close();
            bas.close();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void setSkill() {
        try {
            ByteArrayOutputStream bas = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(bas);
            dos.writeByte(Config.getInstance().getSkillVersion());
            List<SkillOptionTemplate> optionTemplates = GameData.getInstance().getOptionTemplates();
            dos.writeByte(optionTemplates.size());
            for (SkillOptionTemplate optionTemplate : optionTemplates) {
                dos.writeUTF(optionTemplate.name);
            }
            List<Clazz> clazzs = GameData.getInstance().getClazzs();
            dos.writeByte(clazzs.size());
            for (Clazz clazz : clazzs) {
                List<SkillTemplate> templates = clazz.getSkillTemplates();
                dos.writeUTF(clazz.getName());
                dos.writeByte(templates.size());
                for (SkillTemplate template : templates) {
                    dos.writeByte(template.id);
                    dos.writeUTF(template.name);
                    dos.writeByte(template.maxPoint);
                    dos.writeByte(template.type);
                    dos.writeShort(template.iconId);
                    dos.writeUTF(template.description);
                    dos.writeByte(template.skills.size());
                    for (Skill skill : template.skills) {
                        dos.writeShort(skill.id);
                        dos.writeByte(skill.point);
                        dos.writeByte(skill.level);
                        dos.writeShort(skill.manaUse);
                        dos.writeInt(skill.coolDown);
                        dos.writeShort(skill.dx);
                        dos.writeShort(skill.dy);
                        dos.writeByte(skill.maxFight);
                        dos.writeByte(skill.options.length);
                        for (SkillOption option : skill.options) {
                            dos.writeShort(option.param);
                            dos.writeByte(option.optionTemplate.id);
                        }
                    }
                }
            }
            dos.flush();
            skill = bas.toByteArray();
            dos.close();
            bas.close();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void setData() {
        try {
            ByteArrayOutputStream bas = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(bas);
            int version = Config.getInstance().getDataVersion();
            if (Config.getInstance().getMaxPercentAdd() > 0) {
                version += 1;
            }
            dos.writeByte(version);
            byte[] arrow = nj_arrow;
            dos.writeInt(arrow.length);
            dos.write(arrow);
            byte[] effect = nj_effect;
            dos.writeInt(effect.length);
            dos.write(effect);
            byte[] image = nj_image;
            dos.writeInt(image.length);
            dos.write(image);
            byte[] part = nj_part;
            dos.writeInt(part.length);
            dos.write(part);
            byte[] skill = nj_skill;
            dos.writeInt(skill.length);
            dos.write(skill);
            dos.writeByte(npcTasks.length);
            for (int i = 0; i < npcTasks.length; i++) {
                dos.writeByte(npcTasks[i].length);
                for (int a = 0; a < npcTasks[i].length; a++) {
                    dos.writeByte(npcTasks[i][a]);
                    dos.writeByte(mapTasks[i][a]);
                }
            }
            dos.writeByte(exps.length);
            for (long exp : exps) {
                dos.writeLong(exp);
            }
            dos.writeByte(GameData.UP_CRYSTAL.length);
            for (int num : GameData.UP_CRYSTAL) {
                dos.writeInt(num);
            }
            dos.writeByte(GameData.UP_CLOTHE.length);
            for (int num : GameData.UP_CLOTHE) {
                dos.writeInt(num);
            }
            dos.writeByte(GameData.UP_ADORN.length);
            for (int num : GameData.UP_ADORN) {
                dos.writeInt(num);
            }
            dos.writeByte(GameData.UP_WEAPON.length);
            for (int num : GameData.UP_WEAPON) {
                dos.writeInt(num);
            }
            dos.writeByte(GameData.COIN_UP_CRYSTAL.length);
            for (int num : GameData.COIN_UP_CRYSTAL) {
                dos.writeInt(num);
            }
            dos.writeByte(GameData.COIN_UP_CLOTHE.length);
            for (int num : GameData.COIN_UP_CLOTHE) {
                dos.writeInt(num);
            }
            dos.writeByte(GameData.COIN_UP_ADORN.length);
            for (int num : GameData.COIN_UP_ADORN) {
                dos.writeInt(num);
            }
            dos.writeByte(GameData.COIN_UP_WEAPON.length);
            for (int num : GameData.COIN_UP_WEAPON) {
                dos.writeInt(num);
            }
            dos.writeByte(GameData.GOLD_UP.length);
            for (int num : GameData.GOLD_UP) {
                dos.writeInt(num);
            }
            dos.writeByte(GameData.MAX_PERCENT.length);
            if (Config.getInstance().getMaxPercentAdd() > 0) {
                for (int num : GameData.MAX_PERCENT) {
                    dos.writeInt((int) (num + (num * Config.getInstance().getMaxPercentAdd())));
                }
            } else {
                for (int num : GameData.MAX_PERCENT) {
                    dos.writeInt(num);
                }
            }
            byte[] effData = EffectTemplateManager.getInstance().getData();
            dos.write(effData);
            dos.flush();
            data = bas.toByteArray();
            dos.close();
            bas.close();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void setOffline() {
        try {
            Connection conn = DbManager.getInstance().getConnection(DbManager.GAME);
            PreparedStatement stmt = conn.prepareStatement("UPDATE `users` SET `online` = ?;");
            stmt.setInt(1, 0);
            stmt.executeUpdate();
            stmt.close();

            stmt = conn.prepareStatement("UPDATE `players` SET `online` = ? WHERE `server_id` = ?;");
            stmt.setInt(1, 0);
            stmt.setInt(2, Config.getInstance().getServerID());
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void start() {
        try {
            setOffline();
            LuckyDrawManager.getInstance().add(new LuckyDraw("Vòng xoay thường", LuckyDrawManager.NORMAL));
            LuckyDrawManager.getInstance().add(new LuckyDraw("Vòng xoay vip", LuckyDrawManager.VIP));
            Thread threadLuckyDraw = new Thread(LuckyDrawManager.getInstance());
            threadLuckyDraw.setName("Vòng xoay");
            threadLuckyDraw.start();
            Thread threadStall = new Thread(StallManager.getInstance());
            threadStall.setName("Gian hàng");
            threadStall.start();
            GameData.getInstance().start();
            WorldManager.getInstance().start();
            SocketIO.init();
            SpawnBossManager.getInstance().init();

            // time boss ra
            SpawnBossManager.getInstance().init();
            SpawnBossManager.getInstance().spawn(12, 0, 0, SpawnBossManager.THUONG, SpawnBossManager.RANDOM);
            SpawnBossManager.getInstance().spawn(12, 1, 0, SpawnBossManager.THUONG, SpawnBossManager.RANDOM);
            SpawnBossManager.getInstance().spawn(12, 2, 0, SpawnBossManager.THUONG, SpawnBossManager.RANDOM);

            SpawnBossManager.getInstance().spawn(19, 0, 0, SpawnBossManager.THUONG, SpawnBossManager.RANDOM);
            SpawnBossManager.getInstance().spawn(19, 1, 0, SpawnBossManager.THUONG, SpawnBossManager.RANDOM);
            SpawnBossManager.getInstance().spawn(19, 2, 0, SpawnBossManager.THUONG, SpawnBossManager.RANDOM);

            SpawnBossManager.getInstance().spawn(22, 0, 0, SpawnBossManager.THUONG, SpawnBossManager.RANDOM);
            SpawnBossManager.getInstance().spawn(22, 1, 0, SpawnBossManager.THUONG, SpawnBossManager.RANDOM);
            SpawnBossManager.getInstance().spawn(22, 2, 0, SpawnBossManager.THUONG, SpawnBossManager.RANDOM);

            SpawnBossManager.getInstance().spawn(12, 0, 0, SpawnBossManager.VUNG_DAT_MA_QUY, SpawnBossManager.RANDOM);
            SpawnBossManager.getInstance().spawn(12, 1, 0, SpawnBossManager.VUNG_DAT_MA_QUY, SpawnBossManager.RANDOM);
            SpawnBossManager.getInstance().spawn(12, 2, 0, SpawnBossManager.VUNG_DAT_MA_QUY, SpawnBossManager.RANDOM);

            SpawnBossManager.getInstance().spawn(19, 0, 0, SpawnBossManager.VUNG_DAT_MA_QUY, SpawnBossManager.RANDOM);
            SpawnBossManager.getInstance().spawn(19, 1, 0, SpawnBossManager.VUNG_DAT_MA_QUY, SpawnBossManager.RANDOM);
            SpawnBossManager.getInstance().spawn(19, 2, 0, SpawnBossManager.VUNG_DAT_MA_QUY, SpawnBossManager.RANDOM);

            SpawnBossManager.getInstance().spawn(22, 0, 0, SpawnBossManager.VUNG_DAT_MA_QUY, SpawnBossManager.RANDOM);
            SpawnBossManager.getInstance().spawn(22, 1, 0, SpawnBossManager.VUNG_DAT_MA_QUY, SpawnBossManager.RANDOM);
            SpawnBossManager.getInstance().spawn(22, 2, 0, SpawnBossManager.VUNG_DAT_MA_QUY, SpawnBossManager.RANDOM);

            SpawnBossManager.getInstance().spawn(12, 0, 0, SpawnBossManager.LANG_TRUYEN_THUYET, SpawnBossManager.RANDOM);

            SpawnBossManager.getInstance().spawn(19, 0, 0, SpawnBossManager.LANG_TRUYEN_THUYET, SpawnBossManager.RANDOM);

            SpawnBossManager.getInstance().spawn(22, 0, 0, SpawnBossManager.LANG_TRUYEN_THUYET, SpawnBossManager.RANDOM);



//            SpawnBossManager.getInstance().spawn(6, 0, 0, SpawnBossManager.MAP_BOSS_SU_KIEN, SpawnBossManager.ALL);
//            SpawnBossManager.getInstance().spawn(12, 0, 0, SpawnBossManager.MAP_BOSS_SU_KIEN, SpawnBossManager.ALL);

            Clan.start();
            War.timer(13, 0, 0, War.TYPE_LEVEL_30_TO_50);
            War.timer(16, 0, 0, War.TYPE_ALL_LEVEL);
            War.timer(19, 0, 0, War.TYPE_LEVEL_70_TO_90);
//            AutoMaintenance.maintenance(0, 0, 0);
//            Thread t = new Thread(new AutoSaveData());
//            t.start();
            int port = Config.getInstance().getPort();
            Log.info("Start socket post=" + port);
            server = new ServerSocket(port);
            start = true;
            Thread t = new Thread(new AutoSaveData());
            t.start();
            Thread t2 = new Thread(new Ranked());
            t2.start();
            id = 0;
            Log.info("Start server Success!");
            while (start) {
                try {
                    Socket client = server.accept();
                    if (NinjaSchool.isStop) {
                        client.close();
                        continue;
                    }
                    String ip = client.getInetAddress().getHostAddress();
                    int number = ServerManager.frequency(ip);
                    if (number >= Config.getInstance().getIpAddressLimit()) {
                        client.close();
                        continue;
                    }
                    Session cl = new Session(client, ++id);
                    cl.IPAddress = ip;
                    ServerManager.add(ip);
                } catch (Exception e) {
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void stop() {
        isStop = true;
        GameData.getInstance().close();
        StallManager.getInstance().stop();
        Clan.running = false;
        WorldManager.getInstance().close();
        MapManager.getInstance().close();
        StallManager.getInstance().save();
        List<Clan> clans = Clan.getClanDAO().getAll();
        synchronized (clans) {
            for (Clan clan : clans) {
                Clan.getClanDAO().update(clan);
            }
        }
        try {
            ThienDiaManager.getInstance().update();
        } catch (Exception e) {
            e.printStackTrace();
        }
        close();
    }

    public static void maintance() {
        try {
            NinjaSchool.isStop = true;
            LuckyDrawManager.getInstance().stop();
            Log.info("Đã kích hoạt bảo trì máy chủ");
            String name = "server";
            String text = "Máy chủ bảo trì sau 5 phút, vui lòng thoát game để tránh mất dữ liệu nha các bạn";
            GlobalService.getInstance().chat(name, text);
            GlobalService.getInstance().showAlert(name, text);
            Thread.sleep(240000);
            String text2 = "Máy chủ sẽ bảo trì sau 1 phút";
            GlobalService.getInstance().chat(name, text2);
            GlobalService.getInstance().showAlert(name, text2);
            Log.info("server đóng sau 1 phút.");
            Thread.sleep(60000);
            Log.info("server bắt đầu đóng máy chủ.");
            Server.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveAll() {
        try {
            StallManager.getInstance().save();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            List<Clan> clans = Clan.getClanDAO().getAll();
            synchronized (clans) {
                for (Clan clan : clans) {
                    Clan.getClanDAO().update(clan);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            ThienDiaManager.getInstance().update();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            List<Char> chars = ServerManager.getChars();
            for (Char _char : chars) {
                try {
                    if (!_char.isCleaned) {
                        _char.saveData();
                        if (!_char.user.isCleaned) {
                            _char.user.saveData();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveAlldataWhithoutOnline() {
        try {
            StallManager.getInstance().save();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            List<Clan> clans = Clan.getClanDAO().getAll();
            synchronized (clans) {
                for (Clan clan : clans) {
                    Clan.getClanDAO().update(clan);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            ThienDiaManager.getInstance().update();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            List<Char> chars = ServerManager.getChars();
            for (Char _char : chars) {
                try {
                    if (!_char.isCleaned) {
                        _char.saveData();
                        if (!_char.user.isCleaned) {
                            _char.user.saveDataWithoutOnline();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void close() {
        try {
            List<User> users = ServerManager.getUsers();
            for (User user : users) {
                if (!user.isCleaned) {
                    user.session.closeMessage();
                }
            }
            server.close();
            server = null;
            if (ServerManager.getUsers().isEmpty()) {

            }
            Log.info("End socket");
        } catch (IOException e) {
        }
    }
}
