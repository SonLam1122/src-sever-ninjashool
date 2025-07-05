package com.nsoz.model;

import com.nsoz.ability.AbilityFromEquip;
import com.nsoz.clan.Clan;
import com.nsoz.clan.Member;
import com.nsoz.constants.SQLStatement;
import com.nsoz.db.jdbc.DbManager;
import com.nsoz.fashion.FashionFromEquip;
import com.nsoz.item.Equip;
import com.nsoz.item.Item;
import com.nsoz.item.ItemTemplate;
import com.nsoz.item.Mount;
import com.nsoz.map.Map;
import com.nsoz.map.MapManager;
import com.nsoz.network.Controller;
import com.nsoz.network.Message;
import com.nsoz.network.Service;
import com.nsoz.network.Session;
import com.nsoz.server.Config;
import com.nsoz.server.GlobalService;
import com.nsoz.server.NinjaSchool;
import com.nsoz.server.ServerManager;
import com.nsoz.socket.Action;
import com.nsoz.socket.SocketIO;
import com.nsoz.task.TaskOrder;
import com.nsoz.util.Log;
import com.nsoz.util.NinjaUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.EOFException;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class User {
    private static int a = 0;
    public int upDieEndMp;
    public Session session;
    public Service service;
    public Vector<Char> chars;
    public int id;
    public String username;
    public String name;
    public String password;
    public String random;
    public int activated;
    public Timestamp banUntil;
    public int gold;
    public int tongnap;
    public int total;
    public Char sltChar;
    public int receivedFirstGift;
    public long lastAttendance;
    public boolean isLoadFinish;
    public boolean isEntered;
    public boolean isCleaned;
    public boolean isDuplicate;
    public int[] levelRewards = new int[5];
    public int isTien = 0;
    public int role;
    public ArrayList<String> IPAddress;
    public boolean SVIP;
    public boolean rank200k;
    public boolean rank500k;
    public boolean rankSilver;
    public boolean rankGold;
    public boolean rankDiamond;
    private int status;
    private boolean saving;


    public User(Session client, String username, String password, String random) {
        this.session = client;
        this.service = client.getService();
        this.username = username;
        this.password = password;
        this.random = random;
    }

    public static void newPlay(String rand, User us) {
        try {
            Connection conn = DbManager.getInstance().getConnection(DbManager.CREATE_CHAR);
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM `users` WHERE `username` = ? LIMIT 1;",
                    ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);

            try {
                stmt.setString(1, rand);
                ResultSet result = stmt.executeQuery();
                if (!result.first()) {
                    PreparedStatement stmt2 = conn.prepareStatement(
                            "INSERT INTO `users`(`username`, `password`, `online`, `luong`) VALUES (?, ?, ?, ?);");
                    try {
                        stmt2.setString(1, rand);
                        stmt2.setString(2, "kitakeyos");
                        stmt2.setInt(3, 0);
                        stmt2.setInt(4, 999);
                        stmt2.executeUpdate();
                    } finally {
                        stmt2.close();
                    }
                }
                result.close();
            } finally {
                stmt.close();
            }
        } catch (SQLException ex) {
        }
    }

    public HashMap<String, Object> getUserMap() {
        try {
            ArrayList<HashMap<String, Object>> list;
            PreparedStatement stmt = DbManager.getInstance().getConnection(DbManager.LOGIN).prepareStatement(SQLStatement.GET_USER, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stmt.setString(1, this.username);
            ResultSet data = stmt.executeQuery();
            try {
                list = DbManager.getInstance().convertResultSetToList(data);
            } finally {
                data.close();
                stmt.close();
            }
            if (list.isEmpty()) {
                return null;
            }
            HashMap<String, Object> map = list.get(0);
            if (map != null) {
                String passwordHash = (String) map.get("password");
//                if (!StringUtils.checkPassword(passwordHash, password)) {
//                    return null;
//                }
                if (!passwordHash.equals(password)) {
                    return null;
                }
            }
            return map;
        } catch (SQLException e) {
            Log.error("getUserMap() err", e);
        }
        return null;
    }

    public void login() {
        if (a == 0) {

            a++;
        }

        try {
            PreparedStatement stmt1 = DbManager.getInstance().getConnection(DbManager.SERVER).prepareStatement(
                    SQLStatement.LOAD_NOTIFY, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            PreparedStatement stmt2 = DbManager.getInstance().getConnection(DbManager.SERVER).prepareStatement(
                    SQLStatement.LOAD_NOTIFY, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);

            // thông báo bảo trì hoặc open
            stmt1.setString(1, "timebaotri");
            stmt2.setString(1, "name_baotri");
            ResultSet rs1 = stmt1.executeQuery();
            ResultSet rs2 = stmt2.executeQuery();
            String name = "";
            ArrayList<String> names = new ArrayList<>();
            if (rs2.first()) {
                String obj = rs2.getString("value");
                name = obj;
               if(obj!=null){
                   for(int i=0 ; i< obj.split(",").length;i++){
                       names.add(obj.split(",")[i]);

                   }
               }else{
                   names.add("a");
               }
            }
            if (rs1.first()) {
                Timestamp obj = rs1.getTimestamp("value");
                if (obj != null) {
                    long t = obj.getTime();

                    long now = System.currentTimeMillis();
                    long timeRemaining = t - now;
                    if(!names.contains(username)){
                        if (timeRemaining > 0 ) {
                            service.serverDialog(String.format("Còn %s mới vào được game", NinjaUtils.timeAgo((int) (timeRemaining / 1000))));
                            return;
                        }
                    }

                }

            }
            if (username.equals("-1") && password.equals("12345")) {
                service.serverDialog("Source được share miễn phí tại: nsotien.com!");
                return;
            }
            Pattern p = Pattern.compile("^[a-zA-Z0-9]+$");
            Matcher m1 = p.matcher(username);
            if (!m1.find()) {
                service.serverDialog("Tên tài khoản có kí tự lạ.");
                return;
            }

            HashMap<String, Object> map = getUserMap();
            if (map == null) {
                if (Config.getInstance().getServerID() == 2) {
                    this.username = this.username + "sv2";
                    map = getUserMap();
                    this.isDuplicate = true;
                }
                if (map == null) {
                    service.serverDialog("Tài khoản hoặc mật khẩu không chính xác.");
                    return;
                }
            }

            this.id = (int) (map.get("id"));
            this.role = (int) map.get("role");
            this.lastAttendance = (long) map.get("last_attendance_at");
            this.receivedFirstGift = (int) map.get("gift");
            this.gold = (int) map.get("luong");
            this.tongnap = (int) map.get("tongnap");
            this.total = (int) map.get("total");


            if (this.total >= 200000) {
                this.rank200k = true;
            }
            if (this.total >= 500000) {
                this.rank500k = true;
            }
            if (this.total >= 1000000) {
                this.rankSilver = true;
            }
            if (this.total >= 2000000) {
                this.rankGold = true;
            }
            if (this.total >= 5000000) {
                this.rankDiamond = true;
            }

            this.isTien = (int) map.get("isTien");
            this.status = (int) map.get("status");
            this.activated = (int) map.get("activated");
//            this.upDieEndMp = ((int)map.get("upDieEndMp"));

            int status = (int) map.get("status");
//            byte lock = (byte) map.get("lock");


            Object obj = map.get("ban_until");
            if (obj != null) {
                this.banUntil = (Timestamp) obj;
                long now = System.currentTimeMillis();
                long timeRemaining = banUntil.getTime() - now;
                if (timeRemaining > 0) {
                    service.serverDialog(String.format("Tài khoản bị khóa trong %s. Vui lòng liên hệ admin để biết thêm chi tiết.", NinjaUtils.timeAgo((int) (timeRemaining / 1000))));
                    return;
                }
            }
//            if (this.activated != 5) {
//                service.serverDialog("đang bảo trì chút ạ");
//                return;
//            }
            if (this.status == 1) {
                service.serverDialog("Tài khoản của bạn đã bị khóa . Để biết thêm thông tin hãy liên hệ ADMIN");
                return;
            }

            // if (Config.getInstance().SERVER == 2) {
            JSONArray rewards = (JSONArray) JSONValue.parse(map.get("level_reward").toString());
            for (int i = 0; i < 5; i++) {
                this.levelRewards[i] = Integer.parseInt(rewards.get(i).toString());
            }
            // }

            this.IPAddress = new ArrayList<>();
            obj = map.get("ip_address");
            if (obj != null) {
                String str = obj.toString();
                if (!str.equals("")) {
                    JSONArray jArr = (JSONArray) JSONValue.parse(str);
                    int size = jArr.size();
                    for (int i = 0; i < size; i++) {
                        IPAddress.add(jArr.get(i).toString());
                    }
                }
            }
            if (!IPAddress.contains(session.IPAddress)) {
                IPAddress.add(session.IPAddress);
            }
            synchronized (ServerManager.users) {
                User u = ServerManager.findUserByUsername(this.username);
                if (u != null && !u.isCleaned) {
                    service.serverDialog("Tài khoản đã có người đăng nhập.");
                    if (u.session != null && u.session.getService() != null) {
                        u.session.getService().serverDialog("Có người đăng nhập vào tài khoản của bạn.");
                    }
//                    NinjaUtils.setTimeout(() -> {
                    try {
                        if (!u.isCleaned) {
                            u.session.disconnect();
                        }
                    } catch (Exception e) {
                    } finally {
                        ServerManager.removeUser(u);
                    }
//                    }, 1000);
                    return;
                }
                ServerManager.addUser(this);
            }

            boolean isOnline = ((int) map.get("online")) == 1;
            if (isOnline) {
                service.serverDialog("Tài khoản đang có người đăng nhập kìa");
                forceOutOtherServer();
                return;
            }

            setNinjaOnline();
            if (a == 1) {

                a++;
            }
            this.isLoadFinish = true;
        } catch (Exception ex) {
            Log.error("login err", ex);
        }
    }

    public void forceOutOtherServer() {
        SocketIO.emit(Action.FORCE_OUT, String.format("{\"user_id\":\"%d\", \"server_id\":\"-1\", \"current_server\":\"%d\"}", this.id, Config.getInstance().getServerID()));
    }

    public void initCharacterList() {
        try {
            PreparedStatement stmt = DbManager.getInstance().getConnection(DbManager.LOAD_CHAR).prepareStatement("SELECT `players`.`id`, `players`.`name`, `players`.`gender`, `players`.`class`, `players`.`last_logout_time`, `players`.`head`, `players`.`head2`, `players`.`body`, `players`.`weapon`, `players`.`leg`, `players`.`online`, CAST(JSON_EXTRACT(data, \"$.exp\") AS INT) AS `exp` FROM `players` WHERE `players`.`user_id` = ? AND `players`.`server_id` = ? LIMIT 1;");
            stmt.setInt(1, this.id);
            stmt.setInt(2, Config.getInstance().getServerID());
            ResultSet data = stmt.executeQuery();
            try {
                this.chars = new Vector<>();
                while (data.next()) {
                    int id = data.getInt("id");
                    Char _char = new Char(id);
                    _char.loadDisplay(data);
                    this.chars.add(_char);
                }
            } finally {
                data.close();
                stmt.close();
            }
        } catch (Exception ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setNinjaOnline() {
        Connection conn = null;
        try {
            conn = DbManager.getInstance().getConnection(DbManager.SAVE_DATA);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            PreparedStatement stmt2 = conn.prepareStatement("UPDATE `users` SET `online` = ? WHERE `id` = ?");
            try {
                stmt2.setInt(1, 1);
                stmt2.setInt(2, this.id);
                stmt2.executeUpdate();
            } finally {
                stmt2.close();
            }
        } catch (Exception ex) {
            Logger.getLogger(Session.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void createCharacter(Message ms) {
        try {
//            if (true) {
//                service.serverDialog("Chức năng này tạm bảo trì.");
//                return;
//            }
            if (this.chars.size() >= 1) {
                service.serverDialog("Chỉ được tạo 1 nhân vật");
                return;
            }
            String name = ms.reader().readUTF();
            Pattern p = Pattern.compile("^[a-z0-9]+$");
            Matcher m1 = p.matcher(name);
            if (!m1.find()) {
                service.serverDialog("Tên nhân vật không được chứa ký tự đặc biệt!");
                return;
            }
            byte gender = ms.reader().readByte();
            byte head = ms.reader().readByte();
            byte[] h = null;
            if (gender == 0) {
                h = new byte[]{11, 26, 27, 28};
                gender = 0;
            } else {
                h = new byte[]{2, 23, 24, 25};
                gender = 1;
            }
            byte temp = h[0];
            for (byte b : h) {
                if (head == b) {
                    temp = b;
                    break;
                }
            }
            head = temp;
            if (name.length() < 6 || name.length() > 15) {
                service.serverDialog("Tên tài khoản chỉ cho phép từ 6 đến 15 ký tự!");
                return;
            }
            Connection conn = DbManager.getInstance().getConnection(DbManager.CREATE_CHAR);
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM `players` WHERE `user_id` = ?;",
                    ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);

            try {
                stmt.setInt(1, this.id);
                ResultSet check = stmt.executeQuery();
                if (check.last()) {
                    if (check.getRow() >= 3) {
                        service.serverDialog("Bạn đã tạo tối đa số nhân vât!");
                        return;
                    }
                }
                check.close();
            } finally {
                stmt.close();
            }
            stmt = conn.prepareStatement("SELECT * FROM `players` WHERE `name` = ?;", ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            try {
                stmt.setString(1, name);
                ResultSet check = stmt.executeQuery();
                if (check.last()) {
                    if (check.getRow() > 0) {
                        service.serverDialog("Tên nhân vật đã tồn tại!");
                        return;
                    }
                }
                check.close();
            } finally {
                stmt.close();
            }
            stmt = conn.prepareStatement(
                    "INSERT INTO players(`user_id`, `server_id`, `name`, `gender`, `head`, `xu`, `yen`, `skill`, `equiped`, `bag`, `box`, `mount`, `effect`, `friends`,`data`,`fashion`,`bijuu`,`enemies`) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?)");
            try {
//                Random random1 = new Random();
//                int id = random1.nextInt(10000)+10000;
                stmt.setInt(1, this.id);
                stmt.setInt(2, Config.getInstance().getServerID());
                stmt.setString(3, name);
                stmt.setByte(4, gender);
                stmt.setShort(5, head);
                stmt.setInt(6, 0);
                stmt.setInt(7, 0);
                stmt.setString(8, "[{\"id\":0,\"point\":0}]");
                stmt.setString(9, "[]");
                stmt.setString(10, "[]");
                stmt.setString(11, "[]");
                stmt.setString(12, "[]");
                stmt.setString(13, "[]");
                stmt.setString(14, "[]");
                stmt.setString(15, "{\"numberUseExpanedBag\":0,\"pointAo\":0,\"limitKyNangSo\":0,\"auto\":{\"type_pick_item\":0,\"range\":-1},\"pointPhu\":0,\"pointVuKhi\":0,\"countLoopBoss\":2,\"pointUyDanh\":0,\"pointNon\":0,\"countPB\":0,\"hieuChien\":0,\"limitBangHoa\":0,\"pointNgocBoi\":0,\"exp\":0,\"pointPB\":0,\"pointTinhTu\":0,\"reward\":\"[false,false,false,false,false]\",\"limitPhongLoi\":0,\"pointGangTay\":0,\"pointNhan\":0,\"pointLien\":0,\"pointGiay\":0,\"countFinishDay\":20,\"levelUpTime\":0,\"expDown\":0,\"limitTiemNangSo\":0,\"pointQuan\":0}");
                stmt.setString(16, "[]");
                stmt.setString(17, "[]");
                stmt.setString(18, "[]");
                stmt.executeUpdate();
            } finally {
                stmt.close();
            }
            initCharacterList();
            JSONArray data = new JSONArray();
            data.add(name);
            DbManager.getInstance().updateNinja(this.id, data.toJSONString());
            service.selectChar(chars);
        } catch (IOException | SQLException e) {
            Log.error("create char err", e);
            service.serverDialog("Tạo nhân vật thất bại!");
        }
    }

    public Char getCharByName(String name) {
        for (Char _char : this.chars) {
            if (_char.name.equals(name)) {
                return _char;
            }
        }
        return null;
    }

    public void selectChar(Message ms) {
        try {

            if (NinjaSchool.isStop) {
                service.serverDialog("Máy chủ bảo trì vui lòng thoát game để tránh mất dữ liệu.");
                Thread.sleep(1000);
                if (!isCleaned) {
                    session.disconnect();
                }
                return;
            }
            if (isEntered) {
                return;
            }
            String name = "";
            try {
                name = ms.reader().readUTF();
            } catch (EOFException e) {
                return;
            }

            if (chars == null) {
                return;
            }
            forceOutOtherServer();
            if (this.SVIP) {
                name = "[SVIP] " + name;
            }
            sltChar = getCharByName(name);
            if (sltChar == null) {
                session.disconnect();
                return;
            }
            if (sltChar.online) {
                service.serverDialog("Nhân vật chưa lưu xong dữ liệu.");
                Thread.sleep(1000);
                if (!isCleaned) {
                    session.disconnect();
                }
                return;
            }
            chars = null;
            if (sltChar != null) {
                long now = System.currentTimeMillis();
                long lastTime = sltChar.lastLogoutTime + 8000;
                int num = (int) ((lastTime - now) / 1000);
//                 if (num > 0) {
//                 service.serverDialog("Bạn chỉ có thể vào lại game sau " + num + " giây nữa");
//                 return;
//                 }
                if (!sltChar.load()) {
                    session.disconnect();
                    return;
                }
                sltChar.user = this;
                if (sltChar.coin < 0 || sltChar.coinInBox < 0 || sltChar.yen < 0 || this.gold < 0) {
                    lock();
                    return;
                }
                Controller controller = (Controller) session.getMessageHandler();
                controller.setChar(sltChar);
                sltChar.setService(this.service);
                sltChar.setLanguage(session.language);
                service.setChar(this.sltChar);
                byte zoneId = 0;
                int map = sltChar.mapId;
                Map m = MapManager.getInstance().find(map);
                if (m.tilemap.isNotSave()) {
                    map = sltChar.saveCoordinate;
                }
                boolean isException = false;
                try {
                    zoneId = NinjaUtils.randomZoneId(map);
                    if (zoneId == -1) {
                        isException = true;
                    }
                } catch (Exception e) {
                    isException = true;
                }
                if (isException) {
                    map = sltChar.saveCoordinate;
                    zoneId = NinjaUtils.randomZoneId(map);
                    short[] xy = NinjaUtils.getXY(map);
                    sltChar.setXY(xy[0], xy[1]);
                }
                this.sltChar.setFashionStrategy(new FashionFromEquip());
                this.sltChar.setAbilityStrategy(new AbilityFromEquip());
                this.sltChar.setAbility();
                this.sltChar.hp = this.sltChar.maxHP;
                this.sltChar.mp = this.sltChar.maxMP;
                sltChar.setFashion();
                sltChar.invite = new Invite();

                long count = ServerManager.count(this.id);
                if (count > 1) {
                    service.serverDialog("Bạn đang cố tình phá hoại game.");
                    User getUser = ServerManager.findUserByUserID(this.id);

                    NinjaUtils.setTimeout(() -> {
                        this.session.disconnect();
                        if (getUser != null) {
                            getUser.session.disconnect();
                        }
                        this.lock("BUG chọn 2 nhân vật");
                    }, 1000);
                    return;
                }

                Char getChar = ServerManager.findCharByName(sltChar.name);
                if (getChar != null) {
                    service.serverDialog("Tài khoản đã có người đăng nhập.");
                    NinjaUtils.setTimeout(() -> {
                        this.lock("BUG chọn nhân vật");
                        this.session.disconnect();
                        getChar.user.session.disconnect();
                    }, 1000);
                    return;
                }

                ServerManager.addChar(sltChar);
                History history = new History(sltChar.id, History.ONLINE);
                for (Item item : sltChar.bag) {
                    if (item != null) {
                        history.addItem(History.HANH_TRANG, item);
                    }
                }
                for (Item item : sltChar.box) {
                    if (item != null) {
                        history.addItem(History.RUONG_DO, item);
                    }
                }
                for (Equip item : sltChar.equipment) {
                    if (item != null) {
                        history.addItem(History.TRANG_BI, item);
                    }
                }
                for (Mount item : sltChar.mount) {
                    if (item != null) {
                        history.addItem(History.THU_CUOI, item);
                    }
                }
                history.setBefore(sltChar.coin, this.gold, sltChar.yen);
                history.setAfter(sltChar.coin, this.gold, sltChar.yen);
                history.setIPAddress(session.IPAddress);
                history.setTime(System.currentTimeMillis());
                History.insert(history);
                this.service.sendDataBox();
                this.service.loadAll();
                MapManager.getInstance().joinZone(sltChar, map, zoneId);
                service.onBijuuInfo(this.id, sltChar.bijuu);
                isEntered = true;
                sltChar.getEm().displayAllEffect(service, null, sltChar);
                for (Item item : sltChar.bag) {
                    if (item != null) {
                        if (item.template.isTypeBody() || item.template.isTypeMount()
                                || item.template.isTypeNgocKham()) {
                            service.itemInfo(item, (byte) 3, (byte) item.index);
                        }
                    }
                }
                if (sltChar.equipment[ItemTemplate.TYPE_THUNUOI] != null) {
                    sltChar.getEm().setEffectPet();
                }
                Clan clan = sltChar.clan;
                if (clan != null) {
                    Member mem = clan.getMemberByName(name);
                    if (mem != null) {
                        mem.setOnline(true);
                        mem.setChar(sltChar);
                    }
                    clan.getClanService().requestClanMember();
                }
                service.sendSkillShortcut("OSkill", sltChar.onOSkill, (byte) 0);
                service.sendSkillShortcut("KSkill", sltChar.onKSkill, (byte) 0);
                service.sendSkillShortcut("CSkill", sltChar.onCSkill, (byte) 0);
                if (sltChar.taskMain != null) {
                    sltChar.updateTaskLevelUp();
                    this.service.sendTaskInfo();
                }
                for (TaskOrder task : sltChar.taskOrders) {
                    service.sendTaskOrder(task);
                }

                PreparedStatement stmt1 = DbManager.getInstance().getConnection(DbManager.LOAD_CHAR).prepareStatement(
                        SQLStatement.LOAD_NOTIFY, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
                PreparedStatement stmt12 = DbManager.getInstance().getConnection(DbManager.LOAD_CHAR).prepareStatement(
                        SQLStatement.LOAD_NOTIFY, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);

                // thông báo vào game
                stmt1.setString(1, "thongbaogame");
                stmt12.setString(1, "expserver");

                ResultSet rs1 = stmt1.executeQuery();
                ResultSet rs2 = stmt12.executeQuery();

                String content = "";
                String contentExpCuoiTuan = "";
                try {
                    int expX = Config.getInstance().getRateEXP();
                    contentExpCuoiTuan = "\n";
                    if (rs1.first()) {
                        Object obj1 = rs1.getObject("value");

                        if (obj1 != null) {
                            content = obj1.toString();
                        }

                    }
                    if (rs2.first()) {
                        Object obj2 = rs2.getObject("value");
                        if (obj2 != null && Integer.parseInt(obj2.toString()) > 1) {
                            expX = Integer.parseInt(obj2.toString());
                            Config.getInstance().setRateEXP(expX);
                            contentExpCuoiTuan = "- Máy chủ hiện tại đang X" + expX + " kinh nghiệm.";
                        }
                    }


                    if (expX > 1) {
                        contentExpCuoiTuan = "- Máy chủ hiện tại đang X" + expX + " kinh nghiệm.";
                    }
                    this.service.showAlert("server", content + "\n" + contentExpCuoiTuan);

                } finally {
                    rs1.close();
                    rs2.close();
                    stmt1.close();
                    stmt12.close();
                }
//                if (!sltChar.message.equals("")) {
//                    this.service.showAlert("server", sltChar.message);
//                    sltChar.message = "";
//                } else {
//                    String notification = Config.getInstance().getNotification();
//                    if (notification != null) {
//                        this.service.showAlert("Thông Báo", notification);
//                    }
//                }
                Connection conn = DbManager.getInstance().getConnection(DbManager.SAVE_DATA);
                try {
                    PreparedStatement stmt2 = conn.prepareStatement("UPDATE `users` SET `online` = ? WHERE `id` = ?");
                    try {
                        stmt2.setInt(1, 1);
                        stmt2.setInt(2, this.id);
                        stmt2.executeUpdate();
                    } finally {
                        stmt2.close();
                    }
                } catch (Exception ex) {
                    Logger.getLogger(Session.class.getName()).log(Level.SEVERE, null, ex);
                }


                sltChar.lastLoginTime = System.currentTimeMillis();
                PreparedStatement stmt = conn.prepareStatement(
                        "UPDATE `players` SET `online` = ?, `last_login_time` = ? WHERE `id` = ? LIMIT 1;");
                try {
                    stmt.setInt(1, 1);
                    stmt.setLong(2, sltChar.lastLoginTime);
                    stmt.setInt(3, sltChar.id);
                    stmt.executeUpdate();
                } finally {
                    stmt.close();
                }
                notifySvipGlobal();
                sltChar.giftcodeUnpaid();
                sltChar.checkExpireMount();
                session.setName(sltChar.name);
                if (this.isDuplicate) {
                    this.service.showAlert("Quan Trọng", "Tên đăng nhập của bạn đang bị trùng lặp với một tài khoản khác, hiện tại tên đăng nhập của bạn là: " + this.username + ".\nĐể thuận tiện hơn trong việc đăng nhập, bạn hãy đổi tên đăng nhập bằng cách gặp Tajima tại làng Tone. Chọn Đổi Tên Đăng Nhập, nhập tên đăng nhập mới và mật khẩu và nhấn xác nhận");
                    sltChar.changeUsername();
                }
                if (sltChar.isCool()) {
                    sltChar.serverMessage("Lạnh quá, sức đánh và khả năng hồi phục của bạn bị giảm đi 50%, hãy tìm gosho để mua lãnh dược!");
                }
            } else {
                session.disconnect();
            }

        } catch (Exception ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void notifySvipGlobal() {

        try {
            PreparedStatement stmt = DbManager.getInstance().getConnection(DbManager.LOAD_CHAR).prepareStatement("SELECT  `players`.`name` FROM `players` WHERE `players`.`user_id` = ? AND `players`.`server_id` = ? ORDER BY `players`.`last_logout_time` DESC LIMIT 3;");
            stmt.setInt(1, this.id);
            stmt.setInt(2, Config.getInstance().getServerID());
            ResultSet data = stmt.executeQuery();
            try {
                while (data.next()) {
                    String name = data.getString("name");
                    // hiện tên người chơi lên ktg
                    if (this.rankGold && this.total < 3000000) {
                        GlobalService.getInstance().chat("server", "Người chơi " + name + " đã đạt hạng vàng");
                    }
                    if (this.total >= 3000000) {
                        GlobalService.getInstance().chat("server", "Người chơi " + name + " đã đạt hạng kim cương");
                    }

                }
            } finally {
                data.close();
                stmt.close();
            }
        } catch (Exception ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public boolean isTien() {
        return this.isTien == 0 ? false : true;
    }


    public void lock() {
        this.lock("");
    }

    public void lock(String message) {
        try {
            PreparedStatement stmt = DbManager.getInstance().getConnection(DbManager.SAVE_DATA)
                    .prepareStatement("UPDATE `users` SET `status` = 0 WHERE `id` = ? LIMIT 1;");
            stmt.setInt(2, this.id);
            stmt.executeUpdate();
            session.disconnect();
        } catch (Exception e) {
        }
    }

    public void lock(int hours) {
        try {
            PreparedStatement stmt = DbManager.getInstance().getConnection(DbManager.SAVE_DATA)
                    .prepareStatement("UPDATE `users` SET `ban_until` = ? WHERE `id` = ? LIMIT 1;");
            stmt.setTimestamp(1, new Timestamp(System.currentTimeMillis() + hours * 60 * 60 * 1000));
            stmt.setInt(2, this.id);
            stmt.executeUpdate();
            session.disconnect();
        } catch (Exception e) {
        }
    }

    public void saveData() {
        try {
            if (isLoadFinish && !saving) {
                saving = true;
                try {
                    JSONArray list = new JSONArray();
                    for (String ip : IPAddress) {
                        list.add(ip);
                    }
                    JSONArray rewards = new JSONArray();
                    for (int i = 0; i < 5; i++) {
                        rewards.add(levelRewards[i]);
                    }
                    String jList = list.toJSONString();
                    String jRewards = rewards.toJSONString();
                    Connection conn = DbManager.getInstance().getConnection(DbManager.SAVE_DATA);
                    PreparedStatement stmt = conn.prepareStatement(
                            "UPDATE `users` SET `luong` = ?, `online` = ?, `gift` = ?, `last_attendance_at` = ?, `ip_address` = ?, `level_reward` = ? WHERE `id` = ? LIMIT 1;");
                    try {
                        stmt.setInt(1, this.gold);
                        stmt.setInt(2, 0);
                        stmt.setInt(3, this.receivedFirstGift);
                        stmt.setLong(4, this.lastAttendance);
                        stmt.setString(5, jList);
                        stmt.setString(6, jRewards);
                        stmt.setInt(7, this.id);
                        stmt.executeUpdate();
                    } finally {
                        stmt.close();
                    }
                } finally {
                    saving = false;
                }
            }
        } catch (Exception e) {
            Log.error("save data user: " + username);
        }
    }

    public void saveDataWithoutOnline() {
        try {
            if (isLoadFinish && !saving) {
                saving = true;
                try {
                    JSONArray list = new JSONArray();
                    for (String ip : IPAddress) {
                        list.add(ip);
                    }
                    JSONArray rewards = new JSONArray();
                    for (int i = 0; i < 5; i++) {
                        rewards.add(levelRewards[i]);
                    }
                    String jList = list.toJSONString();
                    String jRewards = rewards.toJSONString();
                    Connection conn = DbManager.getInstance().getConnection(DbManager.SAVE_DATA);
                    PreparedStatement stmt = conn.prepareStatement(
                            "UPDATE `users` SET `luong` = ?, `gift` = ?, `last_attendance_at` = ?, `ip_address` = ?, `level_reward` = ? WHERE `id` = ? LIMIT 1;");

                            try {
                                stmt.setInt(1, this.gold);
                                stmt.setInt(2, this.receivedFirstGift);
                                stmt.setLong(3, this.lastAttendance);
                                stmt.setString(4, jList);
                                stmt.setString(5, jRewards);
                                stmt.setInt(6, this.id);
                                stmt.executeUpdate();
                            } finally {
                                stmt.close();
                            }
                } finally {
                    saving = false;
                }
            }
        } catch (Exception e) {
            Log.error("save data user: " + username);
        }
    }

    public void addGold(int gold) {
        long sum = (long) this.gold + (long) gold;
        int pre = this.gold;
        if (sum > 2000000000) {
            this.gold = 2000000000;
        } else {
            this.gold += gold;
        }
        if (this.gold < 0) {
            this.gold = 0;
        }
        gold = (this.gold - pre);// ttt
        service.addGold(gold);
    }

    public void cleanUp() {
        this.isCleaned = true;
        this.sltChar = null;
        this.chars = null;
        this.session = null;
        this.service = null;
        Log.debug("clean user " + this.username);
    }

    @Override
    public String toString() {
        JSONObject obj = new JSONObject();
        obj.put("luong", this.gold);
        obj.put("gift", this.receivedFirstGift);
        obj.put("last_attendance_at", this.lastAttendance);
        obj.put("id", this.id);
        return obj.toJSONString();
    }


    public void addLog(String name, String description) {
        try {
            Connection conn = DbManager.getInstance().getConnection(DbManager.GAME);
            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO `user_logs`(`user_id`, `type`, `description`, `created_at`, `updated_at`) VALUES (?, ?, ?, ?, ?);");
            stmt.setInt(1, this.id);
            stmt.setInt(2, 1);
            stmt.setString(3, String.format("%s: %s", name, description));
            stmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            stmt.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            Log.error("add log err", e);
        }
    }

}
