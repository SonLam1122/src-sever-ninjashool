/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nsoz.event;

import com.nsoz.bot.Bot;
import com.nsoz.bot.SantaClaus;
import com.nsoz.bot.move.SantaClausMove;
import com.nsoz.constants.*;
import com.nsoz.effect.Effect;
import com.nsoz.effect.EffectAutoDataManager;
import com.nsoz.event.eventpoint.EventPoint;
import com.nsoz.item.Item;
import com.nsoz.item.ItemFactory;
import com.nsoz.lib.RandomCollection;
import com.nsoz.map.Map;
import com.nsoz.map.MapManager;
import com.nsoz.map.Tree;
import com.nsoz.map.zones.Zone;
import com.nsoz.mob.Mob;
import com.nsoz.model.Char;
import com.nsoz.model.InputDialog;
import com.nsoz.model.Menu;
import com.nsoz.npc.Npc;
import com.nsoz.npc.NpcFactory;
import com.nsoz.option.ItemOption;
import com.nsoz.server.GlobalService;
import com.nsoz.server.ServerManager;
import com.nsoz.store.ItemStore;
import com.nsoz.store.StoreManager;
import com.nsoz.util.NinjaUtils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Source được chia sẻ miễn phí tại: nsotien.com
 */
public class Noel extends Event {

    public static final String TOP_DECORATION_GIFT_BOX = "decoration_gift_box";
    public static final String TOP_KILL_REINDEER_KING = "kill_reindeer_king";
    public static final String TOP_CHOCOLATE_CAKE = "chocholate_cake";
    public static final String TOP_KILL_SNOWMAN = "kill_snowman";

    public static final String RECEIVED_GIFT = "received_gift";
    private static final int LAM_BANH_KHUC_DAU_TAY = 0;
    private static final int LAM_BANH_KHUC_CHOCOLATE = 1;
    private static final int LAM_HOP_QUA = 2;
    private static final int DOI_DIEM_NGUOI_TUYET_XU = 3;
    private static final int DOI_DIEM_NGUOI_TUYET_LUONG = 4;
    private RandomCollection<Integer> vipItems = new RandomCollection<>();
    private ZonedDateTime start, end;

    public Noel() {
        setId(Event.NOEL);
        endTime.set(2024, 12, 31, 21, 59, 59);
        itemsThrownFromMonsters.add(100, ItemName.BO);
        itemsThrownFromMonsters.add(100, ItemName.KEM);
        itemsThrownFromMonsters.add(100, ItemName.DUONG_BOT);
        itemsThrownFromMonsters.add(100, ItemName.BO);
        itemsThrownFromMonsters.add(100, ItemName.KEM);
        itemsThrownFromMonsters.add(100, ItemName.DUONG_BOT);
        itemsThrownFromMonsters.add(100, ItemName.BO);
        itemsThrownFromMonsters.add(100, ItemName.KEM);
        itemsThrownFromMonsters.add(100, ItemName.DUONG_BOT);

        keyEventPoint.add(EventPoint.DIEM_TIEU_XAI);
        keyEventPoint.add(TOP_DECORATION_GIFT_BOX);
        keyEventPoint.add(TOP_KILL_REINDEER_KING);
        keyEventPoint.add(TOP_CHOCOLATE_CAKE);
        keyEventPoint.add(TOP_KILL_SNOWMAN);
        keyEventPoint.add(RECEIVED_GIFT);

//        itemsRecFromGoldItem.add(0.5, ItemName.SHIRAIJI);
//        itemsRecFromGoldItem.add(0.5, ItemName.HAJIRO);
//        itemsRecFromGoldItem.add(2, ItemName.PHUONG_HOANG_BANG);
//        itemsRecFromGoldItem.add(1, ItemName.PET_UNG_LONG);
//        itemsRecFromGoldItem.add(2, ItemName.GAY_TRAI_TIM);
//        itemsRecFromGoldItem.add(2, ItemName.GAY_MAT_TRANG);
//
//        itemsRecFromGold2Item.add(0.5, ItemName.SHIRAIJI);
//        itemsRecFromGold2Item.add(0.5, ItemName.HAJIRO);
//        itemsRecFromGold2Item.add(2, ItemName.PHUONG_HOANG_BANG);
//        itemsRecFromGold2Item.add(1, ItemName.PET_UNG_LONG);
//        itemsRecFromGold2Item.add(2, ItemName.GAY_TRAI_TIM);
//        itemsRecFromGold2Item.add(2, ItemName.GAY_MAT_TRANG);
//
//
//        vipItems.add(1, ItemName.PHUONG_HOANG_BANG);
//        vipItems.add(2, ItemName.PET_UNG_LONG);
//        vipItems.add(2, ItemName.TUAN_LOC);
//        vipItems.add(2, ItemName.HAKAIRO_YOROI);
//        vipItems.add(2, ItemName.SHIRAIJI);
//        vipItems.add(2, ItemName.HAJIRO);
//        vipItems.add(2, ItemName.GAY_TRAI_TIM);
//        vipItems.add(2, ItemName.GAY_MAT_TRANG);
//        timerSpawnSantaClaus();
    }

    private void timerSpawnSantaClaus() {
        LocalDateTime localNow = LocalDateTime.now();
        ZoneId currentZone = ZoneId.of("Asia/Ho_Chi_Minh");
        ZonedDateTime zonedNow = ZonedDateTime.of(localNow, currentZone);
        start = zonedNow.withMonth(12).withDayOfMonth(24).withHour(22).withMinute(0).withSecond(0);
        end = zonedNow.withMonth(12).withDayOfMonth(25).withHour(2).withMinute(0).withSecond(0);
        if (zonedNow.isAfter(start) && zonedNow.isBefore(end)) {
            start = zonedNow.plusMinutes(5);// thời gian khởi động server
        }
        if (zonedNow.compareTo(start) <= 0) {
            Duration duration = Duration.between(zonedNow, start);
            long initalDelay = duration.getSeconds();
            Runnable runnable = new Runnable() {
                public void run() {
                    spawnSantaClaus();
                }
            };
            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
            scheduler.scheduleAtFixedRate(runnable, initalDelay, 24 * 60 * 60, TimeUnit.SECONDS);

        }

    }

    private void spawnSantaClaus() {
        GlobalService.getInstance().chat("server", "Ông già Noel đã xuất hiện, hãy tới nhặt quà dưới gốc Cây thông.");
        int[] maps = {MapName.TRUONG_HIROSAKI, MapName.TRUONG_OOKAZA, MapName.TRUONG_HARUNA, MapName.LANG_CHAI, MapName.LANG_CHAKUMI,
                MapName.LANG_ECHIGO, MapName.LANG_FEARRI, MapName.LANG_KOJIN, MapName.LANG_OSHIN, MapName.LANG_SANZU, MapName.LANG_SHIIBA, MapName.LANG_TONE};

        for (int mapID : maps) {
            Map map = MapManager.getInstance().find(mapID);
            Zone z = map.rand();
            Npc npc = z.getNpc(NpcName.CAY_THONG);
            if (npc != null) {
                Bot bot = new SantaClaus(-NinjaUtils.nextInt(100000, 200000));
                bot.setDefault();
                bot.recovery();
                bot.setXY((short) npc.cx, (short) npc.cy);
                bot.setMove(new SantaClausMove(npc));
                z.join(bot);
            }
        }
    }

    public void initEffectCool() {
        NinjaUtils.schedule(() -> {
            GlobalService.getInstance().chat("Người tuyết", "Màn đêm đã buông, làn gió lạnh lẽo đang thổi tới trên khắp bản đồ, các bạn hãy cẩn thận nhé!");
            ServerManager.getChars().stream().forEach((Char _char) -> {
                _char.serverMessage("Lạnh quá, sức đánh và khả năng hồi phục của bạn bị giảm đi 50%, hãy tìm gosho để mua lãnh dược!");
            });
        }, 6, 0, 0);
        NinjaUtils.schedule(() -> {
            GlobalService.getInstance().chat("Người tuyết", "Trời sáng rồi, thật tuyệt!");
        }, 18, 0, 0);
    }

    @Override
    public void initStore() {
        StoreManager.getInstance().addItem((byte) StoreManager.TYPE_MISCELLANEOUS, ItemStore.builder()
                .id(996)
                .itemID(ItemName.CHOCOLATE)
                .gold(20)
                .expire(ConstTime.FOREVER)
                .build());
        StoreManager.getInstance().addItem((byte) StoreManager.TYPE_MISCELLANEOUS, ItemStore.builder()
                .id(997)
                .itemID(ItemName.DAU_TAY)
                .coin(100000)
                .expire(ConstTime.FOREVER)
                .build());
        List<ItemOption> options = new ArrayList<>();
        StoreManager.getInstance().addItem((byte) StoreManager.TYPE_MISCELLANEOUS, ItemStore.builder()
                .id(998)
                .itemID(ItemName.TUAN_LOC)
                .gold(1000)
                .options(options)
                .expire(ConstTime.MONTH)
                .build());
        StoreManager.getInstance().addItem((byte) StoreManager.TYPE_MISCELLANEOUS, ItemStore.builder()
                .id(999)
                .itemID(ItemName.LANH_DUOC)
                .gold(5)
                .expire(ConstTime.DAY * 7)
                .build());
//        StoreManager.getInstance().addItem((byte) StoreManager.TYPE_MISCELLANEOUS, ItemStore.builder()
//                .id(1000)
//                .itemID(ItemName.QUA_TRANG_TRI)
//                .gold(25)
//                .expire(ConstTime.FOREVER)
//                .build());
        StoreManager.getInstance().addItem((byte) StoreManager.TYPE_MISCELLANEOUS, ItemStore.builder()
                .id(1001)
                .itemID(ItemName.HOP_QUA_TRANG_TRI)
                .gold(25)
                .expire(ConstTime.FOREVER)
                .build());
    }

    @Override
    public void action(Char p, int type, int amount) {
        if (isEnded()) {
            p.serverMessage("Sự kiện đã kết thúc");
            return;
        }
        switch (type) {
            case LAM_BANH_KHUC_DAU_TAY:
                makeStrawberryCake(p, amount);
                break;
            case LAM_BANH_KHUC_CHOCOLATE:
                makeChocolateCake(p, amount);
                break;

            case LAM_HOP_QUA:
                makeGiftBox(p, amount);
                break;

            case DOI_DIEM_NGUOI_TUYET_XU:
                snowmanKilledCoin(p, amount);
                break;

            case DOI_DIEM_NGUOI_TUYET_LUONG:
                snowmanKilledGold(p, amount);
                break;
        }
    }

    private void makeStrawberryCake(Char p, int amount) {
        int[][] itemRequires = new int[][]{{ItemName.BO, 2}, {ItemName.KEM, 2}, {ItemName.DUONG_BOT, 2}, {ItemName.DAU_TAY, 1}};
        int itemIdReceive = ItemName.BANH_KHUC_CAY_DAU_TAY;
        boolean isDone = makeEventItem(p, amount, itemRequires, 0, 0, 0, itemIdReceive);
        if (isDone) {
            p.getEventPoint().addPoint(Noel.TOP_DECORATION_GIFT_BOX, amount);
            p.getEventPoint().addPoint(EventPoint.DIEM_TIEU_XAI, amount);
        }
    }

    private void makeChocolateCake(Char p, int amount) {
        int[][] itemRequires = new int[][]{{ItemName.BO, 2}, {ItemName.KEM, 2}, {ItemName.DUONG_BOT, 2}, {ItemName.CHOCOLATE, 1}};
        int itemIdReceive = ItemName.BANH_KHUC_CAY_CHOCOLATE;
        boolean isDone = makeEventItem(p, amount, itemRequires, 0, 0, 0, itemIdReceive);
        if (isDone) {
            p.getEventPoint().addPoint(Noel.TOP_CHOCOLATE_CAKE, amount);
            p.getEventPoint().addPoint(EventPoint.DIEM_TIEU_XAI, amount);
        }
    }

    private void makeGiftBox(Char p, int amount) {
        int[][] itemRequires = new int[][]{{ItemName.TRAI_CHAU, 3}, {ItemName.RUY_BANG, 3}, {ItemName.CHUONG_VANG, 3}};
        int itemIdReceive = ItemName.HOP_QUA_TRANG_TRI;
        boolean isDone = makeEventItem(p, amount, itemRequires, 20, 0, 0, itemIdReceive);
        if (isDone) {
            p.getEventPoint().addPoint(EventPoint.DIEM_TIEU_XAI, amount);
        }
    }

    private void snowmanKilledCoin(Char p, int amount) {
        if (isCanExchangeSnowmanSkilledPoint(p, amount)) {
            useEventItem(p, amount, 0, 300000, itemsRecFromCoinItem);
            if (NinjaUtils.nextInt(1000) == 0) {
                Item item = new Item(ItemName.LAM_SON_DA);
                p.addItemToBag(item);
            }
            p.getEventPoint().subPoint(TOP_KILL_SNOWMAN, 20 * amount);
        }
    }

    private void snowmanKilledGold(Char p, int amount) {
        if (isCanExchangeSnowmanSkilledPoint(p, amount)) {
            useEventItem(p, amount, 20, 0, itemsRecFromGold2Item);
            if (NinjaUtils.nextInt(2000) == 0) {
                Item item = new Item(ItemName.TRUC_BACH_THIEN_LU);
                p.addItemToBag(item);
            }
            p.getEventPoint().subPoint(TOP_KILL_SNOWMAN, 20 * amount);
        }
    }

    private boolean isCanExchangeSnowmanSkilledPoint(Char p, int amount) {
        if (p.getEventPoint().getPoint(TOP_KILL_SNOWMAN) < 20 * amount) {
            p.getService().npcChat(NpcName.TIEN_NU,
                    "Bạn cần tối thiểu " + NinjaUtils.getCurrency(20 * amount)
                            + " điểm tiêu diệt người tuyết mới có thể trao đổi.");
            return false;
        }
        return true;

    }

    @Override
    public void menu(Char p) {
        p.menus.clear();
        p.menus.add(new Menu(CMDMenu.EXECUTE, "Làm bánh", () -> {
            p.menus.add(new Menu(CMDMenu.EXECUTE, "Bánh khúc dâu tây", () -> {
                p.setInput(new InputDialog(CMDInputDialog.EXECUTE, "Bánh khúc dâu tây", () -> {
                    InputDialog input = p.getInput();
                    try {
                        int number = input.intValue();
                        action(p, LAM_BANH_KHUC_DAU_TAY, number);
                    } catch (NumberFormatException e) {
                        if (!input.isEmpty()) {
                            p.inputInvalid();
                        }
                    }
                }));
                p.getService().showInputDialog();
            }));
            p.menus.add(new Menu(CMDMenu.EXECUTE, "Bánh khúc chocolate", () -> {
                p.setInput(new InputDialog(CMDInputDialog.EXECUTE, "Bánh khúc chocolate", () -> {
                    InputDialog input = p.getInput();
                    try {
                        int number = input.intValue();
                        action(p, LAM_BANH_KHUC_CHOCOLATE, number);
                    } catch (NumberFormatException e) {
                        if (!input.isEmpty()) {
                            p.inputInvalid();
                        }
                    }
                }));
                p.getService().showInputDialog();
            }));
            p.getService().openUIMenu();
        }));
        p.menus.add(new Menu(CMDMenu.EXECUTE, "Đổi lồng đèn", () -> {
            p.menus.clear();
            p.menus.add(new Menu(CMDMenu.EXECUTE, "2 triệu xu", () -> {
                p.setCommandBox(Char.DOI_LONG_DEN_XU);
                List<Item> list = p.getListItemByID(ItemName.LONG_DEN_TRON, ItemName.LONG_DEN_CA_CHEP,
                        ItemName.LONG_DEN_MAT_TRANG, ItemName.LONG_DEN_NGOI_SAO);
                p.getService().openUIShopTrungThu(list, "Đổi lồng đèn 2 triệu xu", "Đổi (2 triệu xu)");
            }));
            p.menus.add(new Menu(CMDMenu.EXECUTE, "500 lượng", () -> {
                p.setCommandBox(Char.DOI_LONG_DEN_LUONG);
                List<Item> list = p.getListItemByID(ItemName.LONG_DEN_TRON, ItemName.LONG_DEN_CA_CHEP,
                        ItemName.LONG_DEN_MAT_TRANG, ItemName.LONG_DEN_NGOI_SAO);
                p.getService().openUIShopTrungThu(list, "Đổi lồng đèn 500 lượng", "Đổi (500l)");
            }));
            p.getService().openUIMenu();
        }));
        p.menus.add(new Menu(CMDMenu.EXECUTE, "Đua TOP", () -> {
            p.menus.clear();
                p.menus.add(new Menu(CMDMenu.EXECUTE, "BXH bánh dâu", () -> {
                    viewTop(p, TOP_DECORATION_GIFT_BOX, "Top bánh dâu", "%d. %s ");
                }));
                p.menus.add(new Menu(CMDMenu.EXECUTE, "Quà top bánh dâu", () -> {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Top 1 : \n" +
                            "- 1 Cải trang Santa +10 \n" +
                            "- Tuần lộc với thuộc tính chống bị PK vĩnh viễn\n" +
                            "- 5 rương huyền bí\n\n" +
                            "Top 2 : \n" +
                            "- 2 Cải trang Santa +10\n" +
                            "- Tuần lộc vĩnh viễn\n" +
                            "- 3 rương huyền bí\n\n" +
                            "Top 3 : \n" +
                            "- Cải trang Santa +10 \n" +
                            "- Tuần lộc vĩnh viễn \n" +
                            "- 1 rương huyền bí\n\n" +
                            "Top 4-10 : \n" +
                            "- Cải trang Santa +1\n" +
                            "- Tuần lộc 6 tháng");
                    p.getService().showAlert("Phần thưởng", sb.toString());
                }));
                if (isEnded()) {
                    int ranking = getRanking(p, TOP_DECORATION_GIFT_BOX);
                    if (ranking <= 10 && p.getEventPoint().getRewarded(TOP_DECORATION_GIFT_BOX) == 0) {
                        p.menus.add(new Menu(CMDMenu.EXECUTE, String.format("Nhận Thưởng TOP %d", ranking), () -> {
                            receiveReward(p, TOP_DECORATION_GIFT_BOX);
                        }));
                    }
                }
                p.menus.add(new Menu(CMDMenu.EXECUTE, "BXH bánh chocolate", () -> {
                    viewTop(p, TOP_CHOCOLATE_CAKE, "Top bánh chocolate", "%d. %s");
                }));
                p.menus.add(new Menu(CMDMenu.EXECUTE, "Quà top bánh chocolate", () -> {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Top 1\n" +
                            "Hỏa kỳ Lân vĩnh viễn chọn chỉ số\n" +
                            "Tuần lộc vv chống đồ sát\n" +
                            "Lồng đèn trang bị 2 vĩnh viễn 7 dòng chọn chỉ số\n\n" +
                            "Top 2\n" +
                            "3 Hỏa kỳ lân vĩnh viễn ( random chỉ số )\n" +
                            "3 mặt nạ trang bị 2 vĩnh viễn random\n\n" +
                            "Top 3 \n" +
                            "2 Hỏa kỳ lân vĩnh viễn ( random )\n" +
                            "2 mặt nạ trang bị 2 vĩnh viễn random\n\n" +
                            "Top 4 - 6\n" +
                            "3 mặt nạ 2 vĩnh viễn ( random )\n" +
                            "2 rương huyền bí\n\n" +
                            "Top 7 - 10\n" +
                            "2 mặt nạ 2 vĩnh viễn ( random )\n" +
                            "1 rương huyền bí");
                    p.getService().showAlert("Phần thưởng", sb.toString());
                }));
                if (isEnded()) {
                    int ranking = getRanking(p, TOP_CHOCOLATE_CAKE);
                    if (ranking <= 10 && p.getEventPoint().getRewarded(TOP_CHOCOLATE_CAKE) == 0) {
                        p.menus.add(new Menu(CMDMenu.EXECUTE, String.format("Nhận Thưởng TOP %d", ranking), () -> {
                            receiveReward(p, TOP_CHOCOLATE_CAKE);
                        }));
                    }
                }

            p.getService().openUIMenu();
        }));
        p.menus.add(new Menu(CMDMenu.EXECUTE, "Hướng dẫn", () -> {
            StringBuilder sb = new StringBuilder();
            sb.append("- Số lần làm bánh dâu : ").append(NinjaUtils.getCurrency(p.getEventPoint().getPoint(TOP_DECORATION_GIFT_BOX))).append("\n");
            sb.append("- Số lần làm bánh chocolate : ").append(NinjaUtils.getCurrency(p.getEventPoint().getPoint(TOP_CHOCOLATE_CAKE))).append("\n");
            sb.append("=== CÔNG THỨC ===").append("\n");
            sb.append("- Bánh khúc Dâu tây: 2 Bơ + 2 Kem + 2 Đường bột + 1 Dâu tây.").append("\n");
            sb.append("- Bánh khúc Chocolate: 2 Bơ + 2 Kem + 2 Đường bột + 1 Chocolate.").append("\n");
            sb.append("- Hộp quà : mua tại goshoo").append("\n");
            sb.append("Xem chi tiết sự kiện tại : nsokiss.me");
            p.getService().showAlert("Hướng Dẫn", sb.toString());
        }));

    }


    @Override
    public void initMap(Zone zone) {
        Map map = zone.map;
        int mapID = map.id;
        switch (mapID) {
            case MapName.KHU_LUYEN_TAP:
                break;
            case MapName.TRUONG_OOKAZA:
                zone.addNpc(NpcFactory.getInstance().newNpc(99, NpcName.CAY_THONG, 1426, 552, 0));
                zone.addNpc(NpcFactory.getInstance().newNpc(99, NpcName.CAY_THONG, 705, 672, 0));
                zone.addTree(Tree.builder().id(EffectAutoDataManager.CAY_THONG).x((short) 1426).y((short) 552).build());
                zone.addTree(Tree.builder().id(EffectAutoDataManager.CAY_THONG).x((short) 784).y((short) 648).build());
                break;
            case MapName.TRUONG_HARUNA:
                zone.addNpc(NpcFactory.getInstance().newNpc(99, NpcName.CAY_THONG, 502, 408, 0));
                zone.addNpc(NpcFactory.getInstance().newNpc(99, NpcName.CAY_THONG, 1621, 384, 0));
                zone.addTree(Tree.builder().id(EffectAutoDataManager.CAY_THONG).x((short) 502).y((short) 408).build());
                zone.addTree(Tree.builder().id(EffectAutoDataManager.CAY_THONG).x((short) 1863).y((short) 360).build());
                zone.addTree(Tree.builder().id(EffectAutoDataManager.CAY_THONG).x((short) 2048).y((short) 360).build());
                break;
            case MapName.TRUONG_HIROSAKI:
                zone.addNpc(NpcFactory.getInstance().newNpc(99, NpcName.CAY_THONG, 1207, 168, 0));
                zone.addNpc(NpcFactory.getInstance().newNpc(99, NpcName.CAY_THONG, 227, 408, 0));
                zone.addTree(Tree.builder().id(EffectAutoDataManager.CAY_THONG).x((short) 1207).y((short) 168).build());
                break;

            case MapName.LANG_TONE:
                zone.addNpc(NpcFactory.getInstance().newNpc(99, NpcName.CAY_THONG, 1330, 192, 0));
                zone.addNpc(NpcFactory.getInstance().newNpc(99, NpcName.CAY_THONG, 2051, 264, 0));
                zone.addNpc(NpcFactory.getInstance().newNpc(99, NpcName.CAY_THONG, 494, 216, 0));
                zone.addTree(Tree.builder().id(EffectAutoDataManager.CAY_THONG).x((short) 1427).y((short) 264).build());
                break;

            case MapName.LANG_KOJIN:
                zone.addNpc(NpcFactory.getInstance().newNpc(99, NpcName.CAY_THONG, 621, 288, 0));
                break;

            case MapName.LANG_CHAI:
                zone.addNpc(NpcFactory.getInstance().newNpc(99, NpcName.CAY_THONG, 1804, 384, 0));
                break;

            case MapName.LANG_SANZU:
                zone.addNpc(NpcFactory.getInstance().newNpc(99, NpcName.CAY_THONG, 320, 288, 0));
                break;

            case MapName.LANG_CHAKUMI:
                zone.addNpc(NpcFactory.getInstance().newNpc(99, NpcName.CAY_THONG, 626, 312, 0));
                break;

            case MapName.LANG_ECHIGO:
                zone.addNpc(NpcFactory.getInstance().newNpc(99, NpcName.CAY_THONG, 360, 360, 0));
                break;

            case MapName.LANG_OSHIN:
                zone.addNpc(NpcFactory.getInstance().newNpc(99, NpcName.CAY_THONG, 921, 408, 0));
                break;

            case MapName.LANG_SHIIBA:
                zone.addNpc(NpcFactory.getInstance().newNpc(99, NpcName.CAY_THONG, 583, 408, 0));
                break;

            case MapName.LANG_FEARRI:
                zone.addNpc(NpcFactory.getInstance().newNpc(99, NpcName.CAY_THONG, 611, 312, 0));
                break;

            case MapName.RUNG_DAO_SAKURA:
                Mob monster = new Mob(zone.getMonsters().size(), (short) MobName.NGUOI_TUYET, 3000, (byte) 0, (short) 1928, (short) 240, false, true, zone);
                zone.addMob(monster);
                break;
        }
    }

    public void receiveReward(Char p, String key) {
        int ranking = getRanking(p, key);
        if (ranking > 10) {
            p.getService().serverDialog("Bạn không đủ điều kiện nhận phần thưởng");
            return;
        }
        if (p.getEventPoint().getRewarded(key) == 1) {
            p.getService().serverDialog("Bạn đã nhận phần thưởng rồi");
            return;
        }
        if (p.getSlotNull() < 10) {
            p.getService().serverDialog("Bạn cần để hành trang trống tối thiểu 10 ô");
            return;
        }

        if (key == TOP_DECORATION_GIFT_BOX) {
            topDecorationGiftBox(ranking, p);
        } else if (key == TOP_CHOCOLATE_CAKE) {
            topChocolateCake(ranking, p);
        } else if (key == TOP_KILL_REINDEER_KING) {
            topKillReindeerKing(ranking, p);
        }
        p.getEventPoint().setRewarded(key, 1);
    }

    public void topDecorationGiftBox(int ranking, Char p) {
        Item mount = ItemFactory.getInstance().newItem(ItemName.PHUONG_HOANG_BANG);
        int tickId = p.gender == 1 ? ItemName.GAY_MAT_TRANG : ItemName.GAY_TRAI_TIM;
        Item fashionStick = ItemFactory.getInstance().newItem(tickId);
        Item tree = ItemFactory.getInstance().newItem(ItemName.TRUC_BACH_THIEN_LU);
        if (ranking == 1) {
            mount.options.add(new ItemOption(ItemOptionName.NE_DON_ADD_POINT_TYPE_1, 200));
            mount.options.add(new ItemOption(ItemOptionName.CHINH_XAC_ADD_POINT_TYPE_1, 100));
            mount.options.add(new ItemOption(ItemOptionName.TAN_CONG_KHI_DANH_CHI_MANG_POINT_PERCENT_TYPE_1, 100));
            mount.options.add(new ItemOption(ItemOptionName.CHI_MANG_ADD_POINT_TYPE_1, 100));

            tree.setQuantity(10);
            p.addItemToBag(tree);
            for (int i = 0; i < 3; i++) {
                Item mysteryChest = ItemFactory.getInstance().newItem(ItemName.RUONG_HUYEN_BI);
                p.addItemToBag(mysteryChest);
            }
        } else if (ranking == 2) {
            tree.setQuantity(5);
            p.addItemToBag(tree);
            Item mysteryChest = ItemFactory.getInstance().newItem(ItemName.RUONG_HUYEN_BI);
            p.addItemToBag(mysteryChest);
        } else if (ranking >= 3 && ranking <= 5) {
            mount.expire = System.currentTimeMillis() + ConstTime.DAY * 90L;
            fashionStick.expire = System.currentTimeMillis() + ConstTime.DAY * 90L;
            tree.setQuantity(3);
            p.addItemToBag(tree);
            for (int i = 0; i < 2; i++) {
                Item blueChest = ItemFactory.getInstance().newItem(ItemName.RUONG_BACH_NGAN);
                p.addItemToBag(blueChest);
            }
        } else {
            mount.expire = System.currentTimeMillis() + ConstTime.DAY * 30L;
            fashionStick.expire = System.currentTimeMillis() + ConstTime.DAY * 30L;
            Item blueChest = ItemFactory.getInstance().newItem(ItemName.RUONG_BACH_NGAN);
            p.addItemToBag(blueChest);
        }

        p.addItemToBag(mount);
        p.addItemToBag(fashionStick);
    }

    public void topChocolateCake(int ranking, Char p) {
        Item pet = ItemFactory.getInstance().newItem(ItemName.PET_UNG_LONG);
        int maskId = p.gender == 1 ? ItemName.SHIRAIJI : ItemName.HAJIRO;
        Item mask = ItemFactory.getInstance().newItem(maskId);
        Item tree = ItemFactory.getInstance().newItem(ItemName.TRUC_BACH_THIEN_LU);
        if (ranking == 1) {
            pet.options.add(new ItemOption(ItemOptionName.HP_TOI_DA_ADD_POINT_TYPE_1, 3000));
            pet.options.add(new ItemOption(ItemOptionName.MP_TOI_DA_ADD_POINT_TYPE_1, 3000));
            pet.options.add(new ItemOption(ItemOptionName.CHI_MANG_POINT_TYPE_1, 100)); // chi mang
            pet.options.add(new ItemOption(ItemOptionName.TAN_CONG_ADD_POINT_PERCENT_TYPE_8, 10));
            pet.options.add(new ItemOption(ItemOptionName.MOI_5_GIAY_PHUC_HOI_MP_POINT_TYPE_1, 200));
            pet.options.add(new ItemOption(ItemOptionName.MOI_5_GIAY_PHUC_HOI_HP_POINT_TYPE_1, 200));
            pet.options.add(new ItemOption(ItemOptionName.KHONG_NHAN_EXP_TYPE_0, 1));

            tree.setQuantity(10);
            p.addItemToBag(tree);
            for (int i = 0; i < 3; i++) {
                Item mysteryChest = ItemFactory.getInstance().newItem(ItemName.RUONG_HUYEN_BI);
                p.addItemToBag(mysteryChest);
            }
        } else if (ranking == 2) {
            tree.setQuantity(5);
            p.addItemToBag(tree);
            Item mysteryChest = ItemFactory.getInstance().newItem(ItemName.RUONG_HUYEN_BI);
            p.addItemToBag(mysteryChest);
        } else if (ranking >= 3 && ranking <= 5) {
            pet.expire = System.currentTimeMillis() + ConstTime.DAY * 90L;
            mask.expire = System.currentTimeMillis() + ConstTime.DAY * 90L;
            tree.setQuantity(3);
            p.addItemToBag(tree);
            for (int i = 0; i < 2; i++) {
                Item blueChest = ItemFactory.getInstance().newItem(ItemName.RUONG_BACH_NGAN);
                p.addItemToBag(blueChest);
            }
        } else {
            pet.expire = System.currentTimeMillis() + ConstTime.DAY * 30L;
            mask.expire = System.currentTimeMillis() + ConstTime.DAY * 90L;
            Item blueChest = ItemFactory.getInstance().newItem(ItemName.RUONG_BACH_NGAN);
            p.addItemToBag(blueChest);
        }

        p.addItemToBag(pet);
        p.addItemToBag(mask);
    }

    public void topKillReindeerKing(int ranking, Char p) {
        Item pet = ItemFactory.getInstance().newItem(ItemName.TUAN_LOC);
        Item tree = ItemFactory.getInstance().newItem(ItemName.TRUC_BACH_THIEN_LU);
        if (ranking == 1) {
            pet.expire = -1;
            tree.setQuantity(10);
            p.addItemToBag(tree);
            for (int i = 0; i < 3; i++) {
                Item mysteryChest = ItemFactory.getInstance().newItem(ItemName.RUONG_HUYEN_BI);
                p.addItemToBag(mysteryChest);
            }
        } else if (ranking == 2) {
            pet.expire = -1;
            tree.setQuantity(5);
            p.addItemToBag(tree);
            Item mysteryChest = ItemFactory.getInstance().newItem(ItemName.RUONG_HUYEN_BI);
            p.addItemToBag(mysteryChest);
        } else if (ranking >= 3 && ranking <= 5) {
            pet.expire = System.currentTimeMillis() + ConstTime.DAY * 90L;
            tree.setQuantity(3);
            p.addItemToBag(tree);
            for (int i = 0; i < 2; i++) {
                Item blueChest = ItemFactory.getInstance().newItem(ItemName.RUONG_BACH_NGAN);
                p.addItemToBag(blueChest);
            }
        } else {
            pet.expire = System.currentTimeMillis() + ConstTime.DAY * 30L;
            Item blueChest = ItemFactory.getInstance().newItem(ItemName.RUONG_BACH_NGAN);
            p.addItemToBag(blueChest);
        }
        p.addItemToBag(pet);
    }

    @Override
    public void useItem(Char p, Item item) {

        switch (item.id) {
            case ItemName.BANH_KHUC_CAY_CHOCOLATE:
                if (p.getSlotNull() == 0) {
                    p.warningBagFull();
                    return;
                }
                useEventItem(p, item.id, itemsRecFromGoldItem);
                break;
            case ItemName.BANH_KHUC_CAY_DAU_TAY:
                if (p.getSlotNull() == 0) {
                    p.warningBagFull();
                    return;
                }
                useEventItem(p, item.id, itemsRecFromCoinItem);
                break;
            case ItemName.LAM_SON_DA:
            case ItemName.TRUC_BACH_THIEN_LU:
                if (p.getSlotNull() == 0) {
                    p.warningBagFull();
                    return;
                }
                useVipEventItem(p, item.id == ItemName.LAM_SON_DA ? 1 : 2, vipItems);
                p.removeItem(item.index, 1, true);
                break;
            case ItemName.LANH_DUOC:
                int time = 6 * 60 * 60 * 1000;
                short param = 2;
                byte templateID = 45;
                Effect eff = p.getEm().findByID(templateID);
                if (eff != null) {
                    eff.addTime(time);
                    p.getEm().setEffect(eff);
                } else {
                    Effect effect = new Effect(templateID, time, param);
                    effect.param2 = item.id;
                    p.getEm().setEffect(effect);
                }
                p.removeItem(item.index, 1, true);
                break;
            case ItemName.HOP_QUA_TRANG_TRI:
            if (p.getSlotNull() == 0) {
                    p.warningBagFull();
                    return;
                }
                Npc npc = p.zone.getNpc(NpcName.CAY_THONG);
                if (npc == null) {
                    p.serverMessage("Hãy tìm Cây Thông để sử dụng.");
                    return;
                }
                int distance = NinjaUtils.getDistance(npc.cx, npc.cy, p.x, p.y);
                if (distance > 100) {
                    p.serverMessage("Hãy lại gần Cây Thông để sử dụng.");
                    return;
                }
                useEventItem(p, item.id, itemsRecFromCoinItem);
//                p.getEventPoint().addPoint(Noel.TOP_DECORATION_GIFT_BOX, 1);
                break;
            // fix quà trang trí k cần ấn vào cây thong bỏ đoạn code bên Char.java trong npcCayThong
            case ItemName.QUA_TRANG_TRI:

                if (p.getSlotNull() == 0) {
                    p. warningBagFull();
                    return;
                }
                Npc npc1 = p.zone.getNpc(NpcName.CAY_THONG);
                if (npc1 == null) {
                    p.serverMessage("Hãy tìm Cây Thông để sử dụng.");
                    return;
                }
                int distance1 = NinjaUtils.getDistance(npc1.cx, npc1.cy, p.x, p.y);
//                System.out.println(distance1);
                if (distance1 > 100) {
                    p.serverMessage("Hãy lại gần Cây Thông để sử dụng.");
                    return;
                }
//                p.getEventPoint().addPoint(Noel.TOP_DECORATION_GIFT_BOX, 1);
                p.getEventPoint().addPoint(EventPoint.DIEM_TIEU_XAI, 1);
                useEventItem(p, item.id, itemsRecFromCoinItem);
                break;
            case ItemName.HOP_QUA_NOEL:
                break;

        }
    }

    public boolean isCoolTime() {
        Calendar rightNow = Calendar.getInstance();
        int hour = rightNow.get(Calendar.HOUR_OF_DAY);
        if (hour > 18 || hour < 6) {
            return true;
        }
        return false;
    }

}
