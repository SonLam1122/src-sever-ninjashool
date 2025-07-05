package com.nsoz.event;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nsoz.constants.*;
import com.nsoz.db.jdbc.DbManager;
import com.nsoz.event.eventpoint.EventPoint;
import com.nsoz.event.eventpoint.Point;
import com.nsoz.item.Item;
import com.nsoz.item.ItemFactory;
import com.nsoz.item.ItemManager;
import com.nsoz.lib.RandomCollection;
import com.nsoz.map.zones.Zone;
import com.nsoz.model.Char;
import com.nsoz.option.ItemOption;
import com.nsoz.server.Config;
import com.nsoz.server.GlobalService;
import com.nsoz.util.Log;
import com.nsoz.util.NinjaUtils;
import lombok.Getter;
import lombok.Setter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public abstract class Event {

    public static final int NGAY_PHU_NU_VIET_NAM = 0;
    public static final int KOROKING = 1;
    public static final int TRUNG_THU = 2;
    public static final int HALLOWEEN = 3;
    public static final int NOEL = 4;
    public static final int HE = 7;
    public static final int LUNAR_NEW_YEAR = 5;
    public static final int WOMENS_DAY = 6;
    public static final int CO_HON = 7;


    public static final byte DOI_BANG_LUONG = 0;
    public static final byte DOI_BANG_XU = 1;

    public static final long EXPIRE_7_DAY = 604800000L;
    public static final long EXPIRE_30_DAY = 2592000000L;

    public static final RandomCollection<Integer> DOI_PHAN_TU = new RandomCollection<>();
    private static Event instance;

    static {
        DOI_PHAN_TU.add(1, ItemName.LONG_DEN_BUOM_BUOM_THOI_TRANG);
        DOI_PHAN_TU.add(1, ItemName.LONG_DEN_HOA_SEN_THOI_TRANG);
        DOI_PHAN_TU.add(1, ItemName.LONG_DEN_KEO_QUAN_THOI_TRANG);
        DOI_PHAN_TU.add(1, ItemName.LONG_DEN_MAT_TRANG_THOI_TRANG);
        DOI_PHAN_TU.add(1, ItemName.LONG_DEN_MAT_TROI_THOI_TRANG);
        DOI_PHAN_TU.add(1, ItemName.LONG_DEN_NGOI_SAO_THOI_TRANG);
        DOI_PHAN_TU.add(1, ItemName.LONG_DEN_TRAI_TIM_THOI_TRANG);
        DOI_PHAN_TU.add(1, ItemName.LONG_DEN_TRON_THOI_TRANG);
    }

    @Getter
    @Setter
    protected int id;
    protected List<EventPoint> eventPoints;
    @Getter
    protected RandomCollection<Integer> itemsThrownFromMonsters;
    @Getter
    protected RandomCollection<Integer> itemsRecFromCoinItem;
    @Getter
    protected RandomCollection<Integer> itemsRecFromGoldItem;
    protected RandomCollection<Integer> itemsMamCung;
    @Getter
    protected RandomCollection<Integer> itemsRecFromGold2Item;
    @Getter
    protected RandomCollection<Integer> itemRuongMayMan;
    protected Set<String> keyEventPoint;
    protected Calendar endTime = Calendar.getInstance();

    public Event() {
        itemsThrownFromMonsters = new RandomCollection<>();
        itemsRecFromCoinItem = new RandomCollection<>();
        itemsRecFromGoldItem = new RandomCollection<>();
        itemsRecFromGold2Item = new RandomCollection<>();
        itemRuongMayMan = new RandomCollection<>();
        itemsMamCung = new RandomCollection<>();
        eventPoints = new ArrayList<>();
        keyEventPoint = new TreeSet<>();
        initRandomItem();
    }

    public static void init() {
        if (Config.getInstance().getEvent() != null) {
            try {
                instance = (Event) Class.forName(Config.getInstance().getEvent()).newInstance();
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
                Log.error(ex.getMessage(), ex);
            }
        }
    }

    public static Event getEvent() {
        return instance;
    }

    public static boolean isEvent() {
        return (instance != null && !instance.isEnded());
    }

    public static boolean isTrungThu() {
        return isEvent() && instance instanceof TrungThuNew;
    }

    public static boolean isCoHon() {
        return isEvent() && instance instanceof CoHon;
    }

    public static boolean isKoroKing() {
        return isEvent() && instance instanceof KoroKing;
    }

    public static boolean isVietnameseWomensDay() {
        return isEvent() && instance instanceof VietnameseWomensDay;
    }

    public static boolean isInternationalWomensDay() {
        return isEvent() && instance instanceof InternationalWomensDay;
    }

    public static boolean isHalloween() {
        return isEvent() && instance instanceof Halloween;
    }

    public static boolean isNoel() {
        return isEvent() && instance instanceof Noel;
    }

    public static boolean isLunarNewYear() {
        return isEvent() && instance instanceof LunarNewYear;
    }

    public static LunarNewYear getLunarNewYear() {
        if (instance instanceof LunarNewYear) {
            return (LunarNewYear) instance;
        }
        return null;
    }

    public static void useVipEventItem(Char _char, int type, RandomCollection<Integer> rc) {
        int itemId = rc.next();
        Item itm = ItemFactory.getInstance().newItem(itemId);
        itm.isLock = false;
        itm.expire = System.currentTimeMillis();

        long month = NinjaUtils.nextInt(1, type == 2 ? 3 : 2);
        long expire = month * ConstTime.MONTH / 3;
        itm.expire += expire;

        if (type == 2 && NinjaUtils.nextInt(1, 35) == 1) {
            itm.expire += expire;
        }

        if (itm.id == ItemName.MAT_NA_HO) {
            itm.randomOptionTigerMask();
        }

        _char.addItemToBag(itm);
    }

    // đổi lồng đèn
    public void doiLongDen(Char p, byte type, int index) {
        List<Item> list = p.getListItemByID(ItemName.LONG_DEN_TRON, ItemName.LONG_DEN_CA_CHEP,
                ItemName.LONG_DEN_MAT_TRANG, ItemName.LONG_DEN_NGOI_SAO);
        if (index < 0 || index >= list.size()) {
            return;
        }
        if (!isEvent()) {
            p.getService().npcChat(NpcName.TIEN_NU, "Sự kiện đã kết thúc!");
            return;
        }
        if (type == DOI_BANG_LUONG) {
            if (p.user.gold < 500) {
                p.getService().npcChat(NpcName.TIEN_NU, "Không đủ 500 lượng!");
                return;
            }
            p.addGold(-500);
        }
        if (type == DOI_BANG_XU) {
            if (p.coin < 2000000) {
                p.getService().npcChat(NpcName.TIEN_NU, "Không đủ 2 triệu xu!");
                return;
            }
            p.addCoin(-2000000);
        }
        Item item = list.get(index);
        p.removeItem(item.index, 1, true);
        int itemID = DOI_PHAN_TU.next();
        Item itm = ItemFactory.getInstance().newItem(itemID);
        for (ItemOption o : item.options) {
            itm.options.add(o);
        }

        if (NinjaUtils.nextInt(200) == 0) {
            itm.options.add(new ItemOption(ItemOptionName.MIEN_GIAM_SAT_THUONG_POINT_PERCENT_TYPE_8,
                    NinjaUtils.nextInt(1, 30)));
        }

        if (type == DOI_BANG_LUONG) {
            itm.randomOptionLongDen();
        }

        itm.expire = System.currentTimeMillis() + EXPIRE_30_DAY;
        p.addItemToBag(itm);
        p.getService().endDlg(true);
    }

    public abstract void initStore();

    public int randomItemID() {
        return itemsThrownFromMonsters.next();
    }

    public abstract void action(Char p, int type, int amount);

    public abstract void menu(Char p);

    public void useItem(Char _char, Item item) {
    }

    public EventPoint createEventPoint() {
        EventPoint eventPoint = new EventPoint();
        keyEventPoint.forEach((key) -> {
            eventPoint.add(new Point(key, 0, 0,0, 0));
        });
        return eventPoint;
    }

    public void loadEventPoint() {
        try {
            eventPoints.clear();
            PreparedStatement ps = DbManager.getInstance().getConnection(DbManager.GAME)
                    .prepareStatement(SQLStatement.LOAD_EVENT_POINT);
            ps.setInt(1, this.id);
            ps.setInt(2, Config.getInstance().getServerID());
            ResultSet rs = ps.executeQuery();
            Gson g = new Gson();
            while (rs.next()) {
                EventPoint eventPoint = createEventPoint();
                int id = rs.getInt("id");
                int playerID = rs.getInt("player_id");
                String name = rs.getString("name");
                ArrayList<Point> points = g.fromJson(rs.getString("point"), new TypeToken<ArrayList<Point>>() {
                }.getType());
                eventPoint.setId(id);
                eventPoint.setPlayerID(playerID);
                eventPoint.setPlayerName(name);
                eventPoint.setPoints(points);
                eventPoint.addIfMissing(keyEventPoint);
                eventPoints.add(eventPoint);
            }
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(Event.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addEventPoint(EventPoint eventPoint) {
        synchronized (eventPoints) {
            eventPoints.add(eventPoint);
        }
    }

    public void removeEventPoint(EventPoint eventPoint) {
        synchronized (eventPoints) {
            eventPoints.remove(eventPoint);
        }
    }

    public EventPoint findEventPointByPlayerID(int playerID) {
        synchronized (eventPoints) {
            for (EventPoint ev : eventPoints) {
                if (ev.getPlayerID() == playerID) {
                    return ev;
                }
            }
            return null;
        }
    }

    public abstract void initMap(Zone zone);

    // use only 1 item
    public boolean useEventItem(Char p, int itemId, RandomCollection<Integer> rc) {
        int[][] itemRequires = new int[][]{{itemId, 1}};
        return useEventItem(p, 1, itemRequires, 0, 0, 0, rc);
    }

    public boolean useEventItem(Char p, int number, int[][] itemRequire, int gold, int coin, int yen,
                                RandomCollection<Integer> rc) {
        return makeEventItem(p, number, itemRequire, gold, coin, yen, rc, -1);
    }

    public boolean useEventItem(Char p, int number, int gold, int coin, RandomCollection<Integer> rc) {
        return makeEventItem(p, number, new int[][]{}, gold, coin, 0, rc, -1);
    }

    public boolean makeEventItem(Char p, int number, int[][] itemRequire, int gold, int coin, int yen,
                                 int itemIdReceive) {
        return makeEventItem(p, number, itemRequire, gold, coin, yen, null, itemIdReceive);
    }

    public boolean makeEventItem(Char p, int number, int[][] itemRequire, int gold, int coin, int yen,
                                 RandomCollection<Integer> rc, int itemIdReceive) {
        if (number < 1) {
            p.getService().npcChat(NpcName.TIEN_NU, "Số lượng tối thiểu là 1.");
            return false;
        }

        if (number > 1000) {
            p.getService().npcChat(NpcName.TIEN_NU, "Số lượng tối đa là 1.000.");
            return false;
        }

        int priceGold = number * gold;
        int priceCoin = number * coin;
        int priceYen = number * yen;

        for (int i = 0; i < itemRequire.length; i++) {
            int itemId = itemRequire[i][0];
            int amount = itemRequire[i][1] * number;
            int index = p.getIndexItemByIdInBag(itemId);
            if (index == -1 || p.bag[index] == null || !p.bag[index].has(amount)) {
                p.getService().npcChat(NpcName.TIEN_NU, "Không đủ " + ItemManager.getInstance().getItemName(itemId));
                return false;
            }
        }
        if (p.yen < priceYen) {
            p.getService().npcChat(NpcName.TIEN_NU, "Không đủ yên");
            return false;
        } else if (p.user.gold < priceGold) {
            p.getService().npcChat(NpcName.TIEN_NU, "Không đủ lượng");
            return false;
        } else if (p.coin < priceCoin) {
            p.getService().npcChat(NpcName.TIEN_NU, "Không đủ xu");
            return false;
        } else if (rc != null && p.getSlotNull() < number) {
            p.getService().npcChat(NpcName.TIEN_NU, p.language.getString("BAG_FULL"));
            return false;
        } else if (itemIdReceive != -1 && p.getSlotNull() < 1) {
            p.getService().npcChat(NpcName.TIEN_NU, p.language.getString("BAG_FULL"));
            return false;
        }

        if (priceYen > 0) {
            p.addYen(-priceGold);
        }
        if (priceGold > 0) {
            p.addGold(-priceGold);
        }

        if (priceCoin > 0) {
            p.addCoin(-priceCoin);
        }
        String itemName = "";
        for (int i = 0; i < itemRequire.length; i++) {
            int itemId = itemRequire[i][0];
            Item itm = ItemFactory.getInstance().newItem(itemId);
            // gọi tên vật phẩm sự kiện
            if (itm.id == ItemName.HOP_MA_QUY || itm.id == ItemName.KEO_TAO) {
                itemName = itm.template.name;
            }
            int amount = itemRequire[i][1] * number;
            int index = p.getIndexItemByIdInBag(itemId);
            p.removeItem(index, amount, true);
        }


        // todo : exp sự kiện và ktg sự kiện
        if (rc != null) {
            for (int i = 0; i < number; i++) {
                int exp = NinjaUtils.nextInt(10000000, 15000000);
                p.addExp(exp);
                int itemId = rc.next();
                Item itm = ItemFactory.getInstance().newItem(itemId);
                itm.initExpire();
                // ktg sự kiện
                // todo : Sửa lại mở iteem
                if (itm.id == ItemName.THONG_LINH_THAO) {
                    itm.setQuantity(NinjaUtils.nextInt(5, 10));
                } else if (itm.id == ItemName.MAT_NA_HO) {
                    itm.randomOptionTigerMask();
                }else  if (p.user.activated == 0  && (itm.id == ItemName.RUONG_HUYEN_BI || itm.id == ItemName.RUONG_BACH_NGAN
                        || itm.id == ItemName.THE_BAI_KINH_NGHIEM_GIA_TOC_SO || itm.id == ItemName.THE_BAI_KINH_NGHIEM_GIA_TOC_TRUNG) ) {
                    p.addExp(exp);
                } else if (p.user.activated == 1 && (itm.id == ItemName.RUONG_HUYEN_BI || itm.id == ItemName.RUONG_BACH_NGAN)) {

                        GlobalService.getInstance().chat("server", "Chúc mừng " + p.name + " mở " + itemName + " nhận được " + itm.template.name);

                    p.addItemToBag(itm);
                } else {
                    if (p.user.activated == 1) {
                        if (itemId == ItemName.TA_LINH_MA || itemId == ItemName.XICH_TU_MA || itemId == ItemName.PHONG_THUONG_MA ||itemId == ItemName.MAT_NA_THO ||itemId == ItemName.MAT_NA_THO_NU ) {
                            if (NinjaUtils.nextInt(0, 10000) < 5) {
                                itm.expire = -1;
                                GlobalService.getInstance().chat("server", "Chúc mừng " + p.name + " mở " + itemName + " nhận được " + itm.template.name + " vĩnh viễn");
                            }
                        }
                        if (itemId == ItemName.MAT_NA_SHIN_AH || itemId == ItemName.MAT_NA_VO_DIEN || itemId == ItemName.MAT_NA_ONI ||itemId == ItemName.MAT_NA_KUMA ||itemId == ItemName.MAT_NA_INU ) {
                            if (NinjaUtils.nextInt(0, 10000) < 3) {
                                itm.expire = -1;
                                GlobalService.getInstance().chat("server", "Chúc mừng " + p.name + " mở " + itemName + " nhận được " + itm.template.name + " vĩnh viễn");
                            }
                        }
                    }
                    p.addItemToBag(itm);
                }
            }

        } else if (itemIdReceive != -1) {
            Item itm = ItemFactory.getInstance().newItem(itemIdReceive);
            itm.setQuantity(number);
            p.addItemToBag(itm);
            if (priceGold > 0) {
                p.getEventPoint().addPoint(EventPoint.DIEM_TIEU_XAI, 1);
            }
        }
        return true;
    }

    public void viewTop(Char p, String key, String title, String format) {
        List<EventPoint> list = eventPoints.stream().sorted((o1, o2) -> {
            int p1 = o1.getPoint(key);
            int p2 = o2.getPoint(key);
            return p2 - p1;
        }).limit(10).filter(t -> t.getPoint(key) > 0).collect(Collectors.toList());
        StringBuilder sb = new StringBuilder();
        int rank = 1;
        for (EventPoint t : list) {
            sb.append(String.format(format, rank++, t.getPlayerName(), NinjaUtils.getCurrency(t.getPoint(key))))
                    .append("\n");

        }
        p.getService().showAlert(title, sb.toString());
    }

//    public int getPoint(Char p, String key) {
//        List<EventPoint> list = eventPoints.stream().sorted((o1, o2) -> {
//            int p1 = o1.getPoint(key);
//            int p2 = o2.getPoint(key);
//            return p2 - p1;
//        }).limit(10).filter(t -> t.getPoint(key) > 0).collect(Collectors.toList());
//        StringBuilder sb = new StringBuilder();
//
//        for (EventPoint t : list) {
//            if (t.getPlayerName().equals(p.name)) {
//                return t.getPoint(key);
//            }
//
//        }
//        return 0;
//    }


    public int getPoint(Char p, String key) {
        for (EventPoint t : eventPoints) {
            if (t.getPlayerName().equals(p.name)) {
                return t.getPoint(key);
            }
        }
        return 0;
    }

//    public int getPontAll(String key) {
//        List<EventPoint> list = eventPoints.stream().sorted((o1, o2) -> {
//            int p1 = o1.getPoint(key);
//            int p2 = o2.getPoint(key);
//            return p2 - p1;
//        }).limit(10).filter(t -> t.getPoint(key) > 0).collect(Collectors.toList());
//        StringBuilder sb = new StringBuilder();
//
//        for (EventPoint t : list) {
//
//            return t.getPoint(key);
//
//
//        }
//        return 0;
//    }

    public int getPontAll(String key) {
        int point = 0 ;
        for (EventPoint t : eventPoints) {
            point +=t.getPoint(key);
        }
        return point;
    }
    public int getRanking(Char p, String key) {
        List<EventPoint> list = eventPoints.stream().sorted((o1, o2) -> {
            int p1 = o1.getPoint(key);
            int p2 = o2.getPoint(key);
            return p2 - p1;
        }).limit(10).filter(t -> t.getPoint(key) > 0).collect(Collectors.toList());
        int rank = 0;
        for (EventPoint t : list) {
            rank++;
            if (t.getPlayerName().equals(p.name)) {
                return rank;
            }
        }
        return 99;
    }

    public boolean isEnded() {
        return endTime.getTime().getTime() - System.currentTimeMillis() <= 0;
    }

    public void initRandomItem() {

        // todo: random mở item làm bằng xu


        itemsRecFromGoldItem.add(1, ItemName.RUONG_BAU_VAT);
        itemsRecFromGoldItem.add(1, ItemName.RUONG_THAN_BI);
        itemsRecFromCoinItem.add(1, ItemName.HOP_VU_KHI);
        itemsRecFromCoinItem.add(5, ItemName.GAY_MAT_TRANG);
        itemsRecFromCoinItem.add(5, ItemName.GAY_TRAI_TIM);
        itemsRecFromCoinItem.add(15, ItemName.BANH_RANG);
        itemsRecFromCoinItem.add(5, ItemName.BACH_HO);
        itemsRecFromCoinItem.add(5, ItemName.PHUONG_HOANG_BANG);
        itemsRecFromCoinItem.add(5, ItemName.HOA_KY_LAN);
        itemsRecFromCoinItem.add(5, ItemName.LAN_SU_VU);
        itemsRecFromCoinItem.add(1, ItemName.HARLEY_DAVIDSON);
        itemsRecFromCoinItem.add(1, ItemName.XE_MAY);
        itemsRecFromCoinItem.add(2, ItemName.MAT_NA_SHIN_AH);
        itemsRecFromCoinItem.add(2, ItemName.MAT_NA_VO_DIEN);
        itemsRecFromCoinItem.add(2, ItemName.MAT_NA_ONI);
        itemsRecFromCoinItem.add(2, ItemName.MAT_NA_KUMA);
        itemsRecFromCoinItem.add(2, ItemName.MAT_NA_INU);
        itemsRecFromCoinItem.add(0.03, ItemName.RUONG_BACH_NGAN);
        itemsRecFromCoinItem.add(0.01, ItemName.RUONG_HUYEN_BI);
        itemsRecFromCoinItem.add(0.1, ItemName.BAT_BAO);
        itemsRecFromCoinItem.add(20, ItemName.DA_DANH_VONG_CAP_1);
        itemsRecFromCoinItem.add(15, ItemName.DA_DANH_VONG_CAP_2);
        itemsRecFromCoinItem.add(5, ItemName.DA_DANH_VONG_CAP_3);
        itemsRecFromCoinItem.add(5, ItemName.HOA_TUYET);
        itemsRecFromCoinItem.add(5, ItemName.PHA_LE);
        itemsRecFromCoinItem.add(5, ItemName.NHAM_THACH_);
        itemsRecFromCoinItem.add(80, ItemName.THE_BAI_KINH_NGHIEM_GIA_TOC_SO);
        itemsRecFromCoinItem.add(40, ItemName.THE_BAI_KINH_NGHIEM_GIA_TOC_TRUNG);
        itemsRecFromCoinItem.add(80, ItemName.DA_CAP_8);
        itemsRecFromCoinItem.add(30, ItemName.DA_CAP_9);
        itemsRecFromCoinItem.add(50, ItemName.MANH_NON_JIRAI_);
        itemsRecFromCoinItem.add(50, ItemName.MANH_AO_JIRAI_);
        itemsRecFromCoinItem.add(50, ItemName.MANH_QUAN_JIRAI_);
        itemsRecFromCoinItem.add(50, ItemName.MANH_GANG_TAY_JIRAI_);
        itemsRecFromCoinItem.add(50, ItemName.MANH_GIAY_JIRAI_);
        itemsRecFromCoinItem.add(50, ItemName.MANH_PHU_JIRAI_);
        itemsRecFromCoinItem.add(50, ItemName.MANH_DAY_CHUYEN_JIRAI_);
        itemsRecFromCoinItem.add(50, ItemName.MANH_NGOC_BOI_JIRAI_);
        itemsRecFromCoinItem.add(50, ItemName.MANH_NHAN_JIRAI_);
        itemsRecFromCoinItem.add(50, ItemName.MANH_NON_JUMITO);
        itemsRecFromCoinItem.add(50, ItemName.MANH_AO_JUMITO);
        itemsRecFromCoinItem.add(50, ItemName.MANH_QUAN_JUMITO);
        itemsRecFromCoinItem.add(50, ItemName.MANH_GANG_TAY_JUMITO);
        itemsRecFromCoinItem.add(50, ItemName.MANH_GIAY_JUMITO);
        itemsRecFromCoinItem.add(50, ItemName.MANH_PHU_JUMITO);
        itemsRecFromCoinItem.add(50, ItemName.MANH_DAY_CHUYEN_JUMITO);
        itemsRecFromCoinItem.add(50, ItemName.MANH_NGOC_BOI_JUMITO);
        itemsRecFromCoinItem.add(50, ItemName.MANH_NHAN_JUMITO);
        itemsRecFromCoinItem.add(80, ItemName.MINH_MAN_DAN);
        itemsRecFromCoinItem.add(80, ItemName.LONG_LUC_DAN);
        itemsRecFromCoinItem.add(80, ItemName.KHANG_THE_DAN);
        itemsRecFromCoinItem.add(80, ItemName.SINH_MENH_DAN);
        itemsRecFromCoinItem.add(80, ItemName.TUI_VAI_CAP_3);
        itemsRecFromCoinItem.add(30, ItemName.GIAY_RACH);
        itemsRecFromCoinItem.add(20, ItemName.GIAY_BAC);
        itemsRecFromCoinItem.add(15, ItemName.GIAY_VANG);
        itemsRecFromCoinItem.add(40, ItemName.HUYEN_TINH_NGOC);
        itemsRecFromCoinItem.add(40, ItemName.HUYET_NGOC);
        itemsRecFromCoinItem.add(40, ItemName.LAM_TINH_NGOC);
        itemsRecFromCoinItem.add(40, ItemName.LUC_NGOC);
        itemsRecFromCoinItem.add(5, ItemName.XICH_NHAN_NGAN_LANG);
        itemsRecFromCoinItem.add(20, ItemName.HOAN_LUONG_CHI_THAO);
        itemsRecFromCoinItem.add(30, ItemName.GA_TAY);
        itemsRecFromCoinItem.add(30, ItemName.TOM_HUM);
        itemsRecFromCoinItem.add(30, ItemName.HAGGIS);
        itemsRecFromCoinItem.add(50, ItemName.LONG_DEN_TRON);
        itemsRecFromCoinItem.add(50, ItemName.LONG_DEN_CA_CHEP);
        itemsRecFromCoinItem.add(50, ItemName.LONG_DEN_NGOI_SAO);
        itemsRecFromCoinItem.add(50, ItemName.LONG_DEN_MAT_TRANG);
        itemsRecFromCoinItem.add(15, ItemName.VIEN_LINH_HON_CAP_1);
        itemsRecFromCoinItem.add(12, ItemName.VIEN_LINH_HON_CAP_2);
        itemsRecFromCoinItem.add(9, ItemName.VIEN_LINH_HON_CAP_3);
        itemsRecFromCoinItem.add(30, ItemName.BUI_LINH_HON);
        itemsRecFromCoinItem.add(25, ItemName.MAT_NA_SUPER_BROLY);
        itemsRecFromCoinItem.add(25, ItemName.MAT_NA_ONNA_BUGEISHA);
        itemsRecFromCoinItem.add(25, ItemName.THONG_LINH_THAO);


        // todo: random mở item làm bằng lượng
        itemsRecFromGoldItem.add(1, ItemName.RUONG_BAU_VAT);
        itemsRecFromGoldItem.add(1, ItemName.RUONG_THAN_BI);
        itemsRecFromGoldItem.add(1, ItemName.HOP_VU_KHI);
        itemsRecFromGoldItem.add(5, ItemName.GAY_MAT_TRANG);
        itemsRecFromGoldItem.add(5, ItemName.GAY_TRAI_TIM);
        itemsRecFromGoldItem.add(15, ItemName.BANH_RANG);
        itemsRecFromGoldItem.add(5, ItemName.BACH_HO);
        itemsRecFromGoldItem.add(5, ItemName.PHUONG_HOANG_BANG);
        itemsRecFromGoldItem.add(5, ItemName.HOA_KY_LAN);
        itemsRecFromGoldItem.add(5, ItemName.LAN_SU_VU);
        itemsRecFromGoldItem.add(2, ItemName.HARLEY_DAVIDSON);
        itemsRecFromGoldItem.add(2, ItemName.XE_MAY);
        itemsRecFromGoldItem.add(2, ItemName.MAT_NA_SHIN_AH);
        itemsRecFromGoldItem.add(2, ItemName.MAT_NA_VO_DIEN);
        itemsRecFromGoldItem.add(2, ItemName.MAT_NA_ONI);
        itemsRecFromGoldItem.add(2, ItemName.MAT_NA_KUMA);
        itemsRecFromGoldItem.add(2, ItemName.MAT_NA_INU);
        itemsRecFromGoldItem.add(0.03, ItemName.RUONG_BACH_NGAN);
        itemsRecFromGoldItem.add(0.01, ItemName.RUONG_HUYEN_BI);
        itemsRecFromGoldItem.add(0.1, ItemName.BAT_BAO);
        itemsRecFromGoldItem.add(20, ItemName.DA_DANH_VONG_CAP_1);
        itemsRecFromGoldItem.add(15, ItemName.DA_DANH_VONG_CAP_2);
        itemsRecFromGoldItem.add(5, ItemName.DA_DANH_VONG_CAP_3);
        itemsRecFromGoldItem.add(5, ItemName.HOA_TUYET);
        itemsRecFromGoldItem.add(5, ItemName.PHA_LE);
        itemsRecFromGoldItem.add(5, ItemName.NHAM_THACH_);
        itemsRecFromGoldItem.add(80, ItemName.THE_BAI_KINH_NGHIEM_GIA_TOC_SO);
        itemsRecFromGoldItem.add(40, ItemName.THE_BAI_KINH_NGHIEM_GIA_TOC_TRUNG);
        itemsRecFromGoldItem.add(80, ItemName.DA_CAP_8);
        itemsRecFromGoldItem.add(30, ItemName.DA_CAP_9);
        itemsRecFromGoldItem.add(50, ItemName.MANH_NON_JIRAI_);
        itemsRecFromGoldItem.add(50, ItemName.MANH_AO_JIRAI_);
        itemsRecFromGoldItem.add(50, ItemName.MANH_QUAN_JIRAI_);
        itemsRecFromGoldItem.add(50, ItemName.MANH_GANG_TAY_JIRAI_);
        itemsRecFromGoldItem.add(50, ItemName.MANH_GIAY_JIRAI_);
        itemsRecFromGoldItem.add(50, ItemName.MANH_PHU_JIRAI_);
        itemsRecFromGoldItem.add(50, ItemName.MANH_DAY_CHUYEN_JIRAI_);
        itemsRecFromGoldItem.add(50, ItemName.MANH_NGOC_BOI_JIRAI_);
        itemsRecFromGoldItem.add(50, ItemName.MANH_NHAN_JIRAI_);
        itemsRecFromGoldItem.add(50, ItemName.MANH_NON_JUMITO);
        itemsRecFromGoldItem.add(50, ItemName.MANH_AO_JUMITO);
        itemsRecFromGoldItem.add(50, ItemName.MANH_QUAN_JUMITO);
        itemsRecFromGoldItem.add(50, ItemName.MANH_GANG_TAY_JUMITO);
        itemsRecFromGoldItem.add(50, ItemName.MANH_GIAY_JUMITO);
        itemsRecFromGoldItem.add(50, ItemName.MANH_PHU_JUMITO);
        itemsRecFromGoldItem.add(50, ItemName.MANH_DAY_CHUYEN_JUMITO);
        itemsRecFromGoldItem.add(50, ItemName.MANH_NGOC_BOI_JUMITO);
        itemsRecFromGoldItem.add(50, ItemName.MANH_NHAN_JUMITO);
        itemsRecFromGoldItem.add(80, ItemName.MINH_MAN_DAN);
        itemsRecFromGoldItem.add(80, ItemName.LONG_LUC_DAN);
        itemsRecFromGoldItem.add(80, ItemName.KHANG_THE_DAN);
        itemsRecFromGoldItem.add(80, ItemName.SINH_MENH_DAN);
        itemsRecFromGoldItem.add(80, ItemName.TUI_VAI_CAP_3);
        itemsRecFromGoldItem.add(30, ItemName.GIAY_RACH);
        itemsRecFromGoldItem.add(20, ItemName.GIAY_BAC);
        itemsRecFromGoldItem.add(10, ItemName.GIAY_VANG);
        itemsRecFromGoldItem.add(40, ItemName.HUYEN_TINH_NGOC);
        itemsRecFromGoldItem.add(40, ItemName.HUYET_NGOC);
        itemsRecFromGoldItem.add(40, ItemName.LAM_TINH_NGOC);
        itemsRecFromGoldItem.add(40, ItemName.LUC_NGOC);
        itemsRecFromGoldItem.add(5, ItemName.XICH_NHAN_NGAN_LANG);
        itemsRecFromGoldItem.add(20, ItemName.HOAN_LUONG_CHI_THAO);
        itemsRecFromGoldItem.add(30, ItemName.GA_TAY);
        itemsRecFromGoldItem.add(30, ItemName.TOM_HUM);
        itemsRecFromGoldItem.add(30, ItemName.HAGGIS);
        itemsRecFromGoldItem.add(50, ItemName.LONG_DEN_TRON);
        itemsRecFromGoldItem.add(50, ItemName.LONG_DEN_CA_CHEP);
        itemsRecFromGoldItem.add(50, ItemName.LONG_DEN_NGOI_SAO);
        itemsRecFromGoldItem.add(50, ItemName.LONG_DEN_MAT_TRANG);
        itemsRecFromGoldItem.add(15, ItemName.VIEN_LINH_HON_CAP_1);
        itemsRecFromGoldItem.add(12, ItemName.VIEN_LINH_HON_CAP_2);
        itemsRecFromGoldItem.add(9, ItemName.VIEN_LINH_HON_CAP_3);
        itemsRecFromGoldItem.add(30, ItemName.BUI_LINH_HON);
        itemsRecFromGoldItem.add(25, ItemName.MAT_NA_SUPER_BROLY);
        itemsRecFromGoldItem.add(25, ItemName.MAT_NA_ONNA_BUGEISHA);
        itemsRecFromGoldItem.add(25, ItemName.THONG_LINH_THAO);
        itemsRecFromGoldItem.add(25, ItemName.XICH_LAN_HOA_);
        itemsRecFromGoldItem.add(25, ItemName.TAM_LUC_DIEP);









    }
}
