/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nsoz.event;

import com.nsoz.constants.*;
import com.nsoz.event.eventpoint.EventPoint;
import com.nsoz.item.Item;
import com.nsoz.item.ItemFactory;
import com.nsoz.lib.RandomCollection;
import com.nsoz.map.Map;
import com.nsoz.map.zones.Zone;
import com.nsoz.model.Char;
import com.nsoz.model.InputDialog;
import com.nsoz.model.Menu;
import com.nsoz.option.ItemOption;
import com.nsoz.server.Events;
import com.nsoz.store.ItemStore;
import com.nsoz.store.StoreManager;
import com.nsoz.util.NinjaUtils;

import java.time.ZonedDateTime;
import java.util.Calendar;

/**
 * Source được chia sẻ miễn phí tại: nsotien.com
 */
public class He extends Event {


    public static final String TOP_CAU_CA = "cauca_top";
    public static final String TOP_DIEU_VAI = "dieuvai_top";
    public static final String TOP_KEM = "kem_top";
    private static final int LAM_DIEU_GIAY = 0;
    private static final int LAM_DIEU_VAI = 1;

    private static final int LAM_HU_KEM = 2;

    private RandomCollection<Integer> vipItems = new RandomCollection<>();
    private ZonedDateTime start, end;

    public He() {
        setId(Event.HE);
//        Events.event = Events.SUMMER;
        endTime.set(2023, 4, 20, 23, 59, 59);
        itemsThrownFromMonsters.add(25, ItemName.TRE);
        itemsThrownFromMonsters.add(25, ItemName.DAY);
        itemsThrownFromMonsters.add(20, ItemName.GIAY2);
        itemsThrownFromMonsters.add(20, ItemName.VAI);
        itemsThrownFromMonsters.add(20, ItemName.KEM_DAU);
        itemsThrownFromMonsters.add(20, ItemName.KEM_CHOCOLATE);
        itemsThrownFromMonsters.add(20, ItemName.KEM_OC_QUE);
        itemsThrownFromMonsters.add(25, ItemName.KEM_SUA);

        keyEventPoint.add(EventPoint.DIEM_TIEU_XAI);
        keyEventPoint.add(TOP_CAU_CA);
        keyEventPoint.add(TOP_DIEU_VAI);
        keyEventPoint.add(TOP_KEM);

        itemsRecFromGoldItem.add(0.5, ItemName.SHIRAIJI);
        itemsRecFromGoldItem.add(0.5, ItemName.HAJIRO);
        itemsRecFromGoldItem.add(2, ItemName.PHUONG_HOANG_BANG);
        itemsRecFromGoldItem.add(1, ItemName.PET_UNG_LONG);
        itemsRecFromGoldItem.add(2, ItemName.GAY_TRAI_TIM);
        itemsRecFromGoldItem.add(2, ItemName.GAY_MAT_TRANG);
        itemsRecFromGoldItem.add(15, ItemName.DA_DANH_VONG_CAP_1);
        itemsRecFromGoldItem.add(12, ItemName.DA_DANH_VONG_CAP_2);
        itemsRecFromGoldItem.add(9, ItemName.DA_DANH_VONG_CAP_3);
        itemsRecFromGoldItem.add(7, ItemName.DA_DANH_VONG_CAP_4);
        itemsRecFromGoldItem.add(5, ItemName.DA_DANH_VONG_CAP_5);
        itemsRecFromGoldItem.add(15, ItemName.VIEN_LINH_HON_CAP_1);
        itemsRecFromGoldItem.add(12, ItemName.VIEN_LINH_HON_CAP_2);
        itemsRecFromGoldItem.add(9, ItemName.VIEN_LINH_HON_CAP_3);
        itemsRecFromGoldItem.add(7, ItemName.VIEN_LINH_HON_CAP_4);
        itemsRecFromGoldItem.add(5, ItemName.VIEN_LINH_HON_CAP_5);
        itemsRecFromGoldItem.add(15, ItemName.DA_PHUC_SINH);
        itemsRecFromGoldItem.add(15, ItemName.DA_VO_THUONG);
        itemsRecFromGoldItem.add(15, ItemName.DA_CHINH_PHUC);

        itemsRecFromGold2Item.add(0.5, ItemName.SHIRAIJI);
        itemsRecFromGold2Item.add(0.5, ItemName.HAJIRO);
        itemsRecFromGold2Item.add(2, ItemName.PHUONG_HOANG_BANG);
        itemsRecFromGold2Item.add(1, ItemName.PET_UNG_LONG);
        itemsRecFromGold2Item.add(2, ItemName.GAY_TRAI_TIM);
        itemsRecFromGold2Item.add(2, ItemName.GAY_MAT_TRANG);

        vipItems.add(1, ItemName.PHUONG_HOANG_BANG);
        vipItems.add(2, ItemName.PET_UNG_LONG);
        vipItems.add(2, ItemName.TUAN_LOC);
        vipItems.add(2, ItemName.HAKAIRO_YOROI);
        vipItems.add(2, ItemName.SHIRAIJI);
        vipItems.add(2, ItemName.HAJIRO);
        vipItems.add(2, ItemName.GAY_TRAI_TIM);
        vipItems.add(2, ItemName.GAY_MAT_TRANG);
        // timerSpawnSantaClaus();
    }


    @Override
    public void initStore() {
        StoreManager.getInstance().addItem((byte) StoreManager.TYPE_MISCELLANEOUS, ItemStore.builder()
                .id(910)
                .itemID(ItemName.CAN_CAU_CA)
                .coin(70000)
                .expire(ConstTime.FOREVER)
                .build());
//        StoreManager.getInstance().addItem((byte) StoreManager.TYPE_MISCELLANEOUS, ItemStore.builder()
//                .id(997)
//                .itemID(ItemName.DAU_TAY)
//                .coin(100000)
//                .expire(ConstTime.FOREVER)
//                .build());
//        StoreManager.getInstance().addItem((byte) StoreManager.TYPE_MISCELLANEOUS, ItemStore.builder()
//                .id(998)
//                .itemID(ItemName.TUAN_THU_LENH)
//                .gold(20)
//                .expire(ConstTime.FOREVER)
//                .build());
//        List<ItemOption> options = new ArrayList<>();
//        StoreManager.getInstance().addItem((byte) StoreManager.TYPE_MISCELLANEOUS, ItemStore.builder()
//                .id(999)
//                .itemID(ItemName.TUAN_LOC)
//                .gold(500)
//                .options(options)
//                .expire(ConstTime.MONTH)
//                .build());
//        StoreManager.getInstance().addItem((byte) StoreManager.TYPE_MISCELLANEOUS, ItemStore.builder()
//                .id(1000)
//                .itemID(ItemName.LANH_DUOC)
//                .gold(20)
//                .expire(ConstTime.DAY * 7)
//                .build());
//        StoreManager.getInstance().addItem((byte) StoreManager.TYPE_MISCELLANEOUS, ItemStore.builder()
//                .id(1001)
//                .itemID(ItemName.QUA_TRANG_TRI)
//                .gold(20)
//                .expire(ConstTime.FOREVER)
//                .build());
    }

    @Override
    public void action(Char p, int type, int amount) {
        if (isEnded()) {
            p.serverMessage("Sự kiện đã kết thúc");
            return;
        }
        switch (type) {
            case LAM_DIEU_GIAY:
                makeDieuGiay(p, amount);
                break;
            case LAM_DIEU_VAI:
                makeDieuVai(p, amount);
                break;
            case LAM_HU_KEM:
                makeHuKem(p, amount);
                break;
        }
    }

    private void makeDieuGiay(Char p, int amount) {
        int[][] itemRequires = new int[][]{{ItemName.TRE, 5}, {ItemName.DAY, 6}, {ItemName.GIAY2, 5}};
        int itemIdReceive = ItemName.DIEU_GIAY;
        boolean isDome = makeEventItem(p, amount, itemRequires, 0, 30000, 0, itemIdReceive);
        if (isDome) {
            p.getEventPoint().addPoint(EventPoint.DIEM_TIEU_XAI, amount);
        }
    }

    private void makeHuKem(Char p, int amount) {
        int[][] itemRequires = new int[][]{{ItemName.KEM_OC_QUE, 3}, {ItemName.KEM_SUA, 2}, {ItemName.KEM_CHOCOLATE, 3}, {ItemName.KEM_DAU, 2}};
        int itemIdReceive = ItemName.HU_KEM_DAM;
        boolean isDone = makeEventItem(p, amount, itemRequires, 15, 0, 0, itemIdReceive);
        if (isDone) {
            p.getEventPoint().addPoint(He.TOP_KEM, amount);
            p.getEventPoint().addPoint(EventPoint.DIEM_TIEU_XAI, amount);
        }
    }

    private void makeDieuVai(Char p, int amount) {
        int[][] itemRequires = new int[][]{{ItemName.TRE, 6}, {ItemName.DAY, 6}, {ItemName.VAI, 5}};
        int itemIdReceive = ItemName.DIEU_VAI;
        boolean isDone = makeEventItem(p, amount, itemRequires, 15, 0, 0, itemIdReceive);
        //System.out.println(isDone);
        if (isDone) {
            p.getEventPoint().addPoint(He.TOP_DIEU_VAI, amount);
            p.getEventPoint().addPoint(EventPoint.DIEM_TIEU_XAI, amount);

        }
    }


    @Override
    public void menu(Char p) {


        p.menus.add(new Menu(CMDMenu.EXECUTE, "Làm diều giấy", () -> {
            p.setInput(new InputDialog(CMDInputDialog.EXECUTE, "Làm diều giấy", () -> {
                InputDialog input = p.getInput();
                try {
                    int number = input.intValue();
                    action(p, LAM_DIEU_GIAY, number);
                } catch (NumberFormatException e) {
                    if (!input.isEmpty()) {
                        p.inputInvalid();
                    }
                }
            }));
            p.getService().showInputDialog();
        }));
        p.menus.add(new Menu(CMDMenu.EXECUTE, "Làm diều vải", () -> {
            p.setInput(new InputDialog(CMDInputDialog.EXECUTE, "làm diều vải", () -> {
                InputDialog input = p.getInput();
                try {
                    int number = input.intValue();
                    action(p, LAM_DIEU_VAI, number);
                } catch (NumberFormatException e) {
                    if (!input.isEmpty()) {
                        p.inputInvalid();
                    }
                }
            }));
            p.getService().showInputDialog();
        }));
        p.menus.add(new Menu(CMDMenu.EXECUTE, "Làm hũ kem", () -> {
            p.setInput(new InputDialog(CMDInputDialog.EXECUTE, "làm hũ kem", () -> {
                InputDialog input = p.getInput();
                try {
                    int number = input.intValue();
                    action(p, LAM_HU_KEM, number);
                } catch (NumberFormatException e) {
                    if (!input.isEmpty()) {
                        p.inputInvalid();
                    }
                }
            }));
            p.getService().showInputDialog();
        }));
//        p.menus.add(new Menu(CMDMenu.EXECUTE, "Dế Ngọc", () -> {
//            if (p.getSlotNull() == 0) {
//                p.getService().npcChat((short) 33, "Hành trang của bạn không có đủ chỗ trống");
//                return;
//            }
//            int indexItem = p.getIndexItemByIdInBag(ItemName.DE_NGOC);
//            if (indexItem != -1) {
//                RandomCollection<Integer> rc = RandomItem.LINH_VAT;
//                Event.useVipEventItem(p, 1, rc);
//                p.removeItem(indexItem, 1, true);
//            } else {
//                p.getService().npcChat((short) 33, "Hãy tìm linh vật rồi đến gặp ta");
//            }
//        }));
//        p.menus.add(new Menu(CMDMenu.EXECUTE, "Bọ Vàng", () -> {
//            if (p.getSlotNull() == 0) {
//                p.getService().npcChat((short) 33, "Hành trang của bạn không có đủ chỗ trống");
//                return;
//            }
//            int indexItem = p.getIndexItemByIdInBag(ItemName.BO_VANG);
//            if (indexItem != -1) {
//                RandomCollection<Integer> rc = RandomItem.LINH_VAT;
//                Event.useVipEventItem(p, 2, rc);
//                p.removeItem(indexItem, 1, true);
//            } else {
//                p.getService().npcChat((short) 33, "Hãy tìm linh vật rồi đến gặp ta");
//            }
//        }));
        p.menus.add(new Menu(CMDMenu.EXECUTE, "Đổi điểm ", () -> {
            p.menus.clear();
            p.menus.add(new Menu(CMDMenu.EXECUTE, "Đổi dế ngọc", () -> {
                makeDeNgoc(p, 1);
            }));
            p.menus.add(new Menu(CMDMenu.EXECUTE, "Đổi bọ vàng", () -> {
                makeBoVang(p, 1);
            }));
            p.menus.add(new Menu(CMDMenu.EXECUTE, "Điểm sự kiện", () -> {
                p.getService().showAlert("Hướng dẫn", "- Điểm sự kiện: "
                        + NinjaUtils.getCurrency(p.getEventPoint().getPoint(EventPoint.DIEM_TIEU_XAI))
                        + "\n\nBạn có thể quy đổi điểm sự kiện như sau\n- Dế ngọc: 10.000 điểm\n- Bọ vàng: 10.000 điểm\n");
            }));
            p.getService().openUIMenu();
        }));
//        p.menus.add(new Menu(CMDMenu.EXECUTE, "Bướm vàng", () -> {
//            if (p.getSlotNull() == 0) {
//                p.getService().npcChat((short) 33, "Hành trang của bạn không có đủ chỗ trống");
//                return;
//            }
//            int indexItem = p.getIndexItemByIdInBag(ItemName.BUOM_VANG);
//            if (indexItem != -1) {
//                RandomCollection<Integer> rc = RandomItem.LINH_VAT;
//                Event.useVipEventItem(p, 2, rc);
//                p.removeItem(indexItem, 1, true);
//            } else {
//                p.getService().npcChat((short) 33, "Hãy tìm linh vật rồi đến gặp ta");
//            }
//        }));

        p.menus.add(new Menu(CMDMenu.EXECUTE, "Đua TOP", () -> {
            p.menus.clear();
            p.menus.add(new Menu(CMDMenu.EXECUTE, "Câu cá", () -> {
                p.menus.clear();
                p.menus.add(new Menu(CMDMenu.EXECUTE, "Bảng xếp hạng", () -> {
                    viewTop(p, TOP_CAU_CA, "Câu cá", "%d. %s đã câu %s lần");
                }));
                p.menus.add(new Menu(CMDMenu.EXECUTE, "Phần thưởng", () -> {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Top 1:").append("\n");
                    sb.append("- Phượng Hoàng Băng v.v MCS\n");
                    sb.append("- Gậy thời trang v.v\n");
                    sb.append("- 3 Rương huyền bí\n");
                    sb.append("- 10 Trúc bạch thiên lữ\n\n");
                    sb.append("Top 2:").append("\n");
                    sb.append("- Phượng Hoàng Băng v.v\n");
                    sb.append("- Gậy thời trang v.v\n");
                    sb.append("- 1 Rương huyền bí\n");
                    sb.append("- 5 Trúc bạch thiên lữ\n\n");
                    sb.append("Top 3 - 5:").append("\n");
                    sb.append("- Phượng Hoàng Băng 3 tháng\n");
                    sb.append("- Gậy thời trang 3 tháng\n");
                    sb.append("- 2 Rương bạch ngân\n");
                    sb.append("- 3 Trúc bạch thiên lữ\n\n");
                    sb.append("Top 6 - 10:").append("\n");
                    sb.append("- Phượng Hoàng Băng 1 tháng\n");
                    sb.append("- 1 rương bạch ngân\n");
                    p.getService().showAlert("Phần thưởng", sb.toString());
                }));
                if (isEnded()) {
                    int ranking = getRanking(p, TOP_CAU_CA);
                    if (ranking <= 10 && p.getEventPoint().getRewarded(TOP_CAU_CA) == 0) {
                        p.menus.add(new Menu(CMDMenu.EXECUTE, String.format("Nhận Thưởng TOP %d", ranking), () -> {
                            receiveReward(p, TOP_CAU_CA);
                        }));
                    }
                }
                p.getService().openUIMenu();
            }));
            p.menus.add(new Menu(CMDMenu.EXECUTE, "Top thả diều", () -> {
                p.menus.clear();
                p.menus.add(new Menu(CMDMenu.EXECUTE, "Bảng xếp hạng", () -> {
                    viewTop(p, TOP_DIEU_VAI, "Top thả diều", "%d. %s đã thả %s diều");
                }));
                p.menus.add(new Menu(CMDMenu.EXECUTE, "Phần thưởng", () -> {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Top 1:").append("\n");
                    sb.append("- Pet ứng long v.v MCS\n");
                    sb.append("- SHIRAIJI/HAJIRO v.v MCS\n");
                    sb.append("- 3 rương huyền bí\n");
                    sb.append("- 10 Trúc bạch thiên lữ\n\n");
                    sb.append("Top 2:").append("\n");
                    sb.append("- Pet ứng long v.v\n");
                    sb.append("- SHIRAIJI/HAJIRO v.v\n");
                    sb.append("- 1 rương huyền bí\n");
                    sb.append("- 5 Trúc bạch thiên lữ\n\n");
                    sb.append("Top 3 - 5:").append("\n");
                    sb.append("- Pet ứng long 3 tháng\n");
                    sb.append("- SHIRAIJI/HAJIRO 3 tháng\n");
                    sb.append("- 2 rương bạch ngân\n");
                    sb.append("- 3 Trúc bạch thiên lữ\n\n");
                    sb.append("Top 6 - 10:").append("\n");
                    sb.append("- Pet ứng long 1 tháng\n");
                    sb.append("- 1 rương bạch ngân\n");
                    p.getService().showAlert("Phần thưởng", sb.toString());
                }));
                if (isEnded()) {
                    int ranking = getRanking(p, TOP_DIEU_VAI);
                    if (ranking <= 10 && p.getEventPoint().getRewarded(TOP_DIEU_VAI) == 0) {
                        p.menus.add(new Menu(CMDMenu.EXECUTE, String.format("Nhận Thưởng TOP %d", ranking), () -> {
                            receiveReward(p, TOP_DIEU_VAI);
                        }));
                    }
                }
                p.getService().openUIMenu();
            }));
            p.menus.add(new Menu(CMDMenu.EXECUTE, "Top làm kem", () -> {
                p.menus.clear();
                p.menus.add(new Menu(CMDMenu.EXECUTE, "Bảng xếp hạng", () -> {
                    viewTop(p, TOP_KEM, "Top làm kem", "%d. %s đã làm %s kem");
                }));
                p.menus.add(new Menu(CMDMenu.EXECUTE, "Phần thưởng", () -> {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Top 1:").append("\n");
                    sb.append("- 3 Mặt nạ thời trang v.v \n");
                    sb.append("- SHIRAIJI/HAJIRO v.v MCS\n");
                    sb.append("- 3 rương huyền bí\n");
                    sb.append("- 10 Trúc bạch thiên lữ\n\n");
                    sb.append("Top 2:").append("\n");
                    sb.append("- 2 Mặt nạ thời trang v.v\n");
                    sb.append("- SHIRAIJI/HAJIRO v.v\n");
                    sb.append("- 1 rương huyền bí\n");
                    sb.append("- 5 Trúc bạch thiên lữ\n\n");
                    sb.append("Top 3 - 5:").append("\n");
                    sb.append("- Pet ứng long 3 tháng\n");
                    sb.append("- SHIRAIJI/HAJIRO 3 tháng\n");
                    sb.append("- 2 rương bạch ngân\n");
                    sb.append("- 3 Trúc bạch thiên lữ\n\n");
                    sb.append("Top 6 - 10:").append("\n");
                    sb.append("- Pet ứng long 1 tháng\n");
                    sb.append("- 1 rương bạch ngân\n");
                    p.getService().showAlert("Phần thưởng", sb.toString());
                }));
                if (isEnded()) {
                    int ranking = getRanking(p, TOP_KEM);
                    if (ranking <= 10 && p.getEventPoint().getRewarded(TOP_KEM) == 0) {
                        p.menus.add(new Menu(CMDMenu.EXECUTE, String.format("Nhận Thưởng TOP %d", ranking), () -> {
                            receiveReward(p, TOP_KEM);
                        }));
                    }
                }
                p.getService().openUIMenu();
            }));
            p.getService().openUIMenu();
        }));
        p.menus.add(new Menu(CMDMenu.EXECUTE, "Hướng dẫn", () -> {
            StringBuilder sb = new StringBuilder();
            sb.append("- Số lần câu cá: ")
                    .append(NinjaUtils.getCurrency(p.getEventPoint().getPoint(TOP_CAU_CA))).append("\n");
            sb.append("- Số diều đã làm: ")
                    .append(NinjaUtils.getCurrency(p.getEventPoint().getPoint(TOP_DIEU_VAI))).append("\n");
            sb.append("- Số kem đã làm: ")
                    .append(NinjaUtils.getCurrency(p.getEventPoint().getPoint(TOP_KEM))).append("\n");
            sb.append("===HƯỚNG DẪN===").append("\n");
            sb.append("- Diều giấy: 5 tre + 6 dây + 5 giấy + 30.000 Xu").append("\n");
            sb.append("- Diều vải: 6 tre + 6 dây + 5 vải + 15 lượng.").append("\n");
            sb.append("- Hũ Kem Dầm: 3 Kem ốc quế + 2 Kem sữa + 3 Kem chocolate + 2 Kem dâu + 15 lượng.").append("\n");
            p.getService().showAlert("Hướng Dẫn", sb.toString());
        }));

    }

    public void makeBoVang(Char p, int type) {
        int point = type == 1 ? 10000 : 20000;
        if (p.getEventPoint().getPoint(EventPoint.DIEM_TIEU_XAI) < point) {
            p.getService().npcChat(NpcName.TIEN_NU,
                    "Bạn cần tối thiểu " + NinjaUtils.getCurrency(point)
                            + " điểm sự kiện mới có thể đổi được vật này.");
            return;
        }

        if (p.getSlotNull() == 0) {
            p.getService().npcChat(NpcName.TIEN_NU, p.language.getString("BAG_FULL"));
            return;
        }

        Item item = ItemFactory.getInstance().newItem(ItemName.BO_VANG);
        p.addItemToBag(item);
        p.getEventPoint().subPoint(EventPoint.DIEM_TIEU_XAI, point);
    }

    public void makeDeNgoc(Char p, int type) {
        int point = type == 1 ? 10000 : 20000;
        if (p.getEventPoint().getPoint(EventPoint.DIEM_TIEU_XAI) < point) {
            p.getService().npcChat(NpcName.TIEN_NU,
                    "Bạn cần tối thiểu " + NinjaUtils.getCurrency(point)
                            + " điểm sự kiện mới có thể đổi được vật này.");
            return;
        }

        if (p.getSlotNull() == 0) {
            p.getService().npcChat(NpcName.TIEN_NU, p.language.getString("BAG_FULL"));
            return;
        }

        Item item = ItemFactory.getInstance().newItem(ItemName.DE_NGOC);
        p.addItemToBag(item);
        p.getEventPoint().subPoint(EventPoint.DIEM_TIEU_XAI, point);
    }

    public void makePreciousTree(Char p, int type) {
        int point = type == 1 ? 5000 : 20000;
        if (p.getEventPoint().getPoint(EventPoint.DIEM_TIEU_XAI) < point) {
            p.getService().npcChat(NpcName.TIEN_NU,
                    "Bạn cần tối thiểu " + NinjaUtils.getCurrency(point)
                            + " điểm sự kiện mới có thể đổi được vật này.");
            return;
        }

        if (p.getSlotNull() == 0) {
            p.getService().npcChat(NpcName.TIEN_NU, p.language.getString("BAG_FULL"));
            return;
        }

        Item item = ItemFactory.getInstance().newItem(type == 1 ? ItemName.LAM_SON_DA : ItemName.TRUC_BACH_THIEN_LU);
        p.addItemToBag(item);
        p.getEventPoint().subPoint(EventPoint.DIEM_TIEU_XAI, point);
    }

    @Override
    public void initMap(Zone zone) {
        Map map = zone.map;
        int mapID = map.id;
        switch (mapID) {
            case MapName.KHU_LUYEN_TAP:
                break;
//            case MapName.TRUONG_OOKAZA:
//                zone.addNpc(NpcFactory.getInstance().newNpc(99, NpcName.CAY_THONG, 1426, 552, 0));
//                break;
//            case MapName.TRUONG_HARUNA:
//                zone.addNpc(NpcFactory.getInstance().newNpc(99, NpcName.CAY_THONG, 502, 408, 0));
//                break;
//            case MapName.TRUONG_HIROSAKI:
//                zone.addNpc(NpcFactory.getInstance().newNpc(99, NpcName.CAY_THONG, 1207, 168, 0));
//                break;
//
//            case MapName.LANG_TONE:
//                zone.addNpc(NpcFactory.getInstance().newNpc(99, NpcName.CAY_THONG, 1427, 264, 0));
//                break;
//
//            case MapName.LANG_KOJIN:
//                zone.addNpc(NpcFactory.getInstance().newNpc(99, NpcName.CAY_THONG, 621, 288, 0));
//                break;
//
//            case MapName.LANG_CHAI:
//                zone.addNpc(NpcFactory.getInstance().newNpc(99, NpcName.CAY_THONG, 1804, 384, 0));
//                break;
//
//            case MapName.LANG_SANZU:
//                zone.addNpc(NpcFactory.getInstance().newNpc(99, NpcName.CAY_THONG, 320, 288, 0));
//                break;
//
//            case MapName.LANG_CHAKUMI:
//                zone.addNpc(NpcFactory.getInstance().newNpc(99, NpcName.CAY_THONG, 626, 312, 0));
//                break;
//
//            case MapName.LANG_ECHIGO:
//                zone.addNpc(NpcFactory.getInstance().newNpc(99, NpcName.CAY_THONG, 360, 360, 0));
//                break;
//
//            case MapName.LANG_OSHIN:
//                zone.addNpc(NpcFactory.getInstance().newNpc(99, NpcName.CAY_THONG, 921, 408, 0));
//                break;
//
//            case MapName.LANG_SHIIBA:
//                zone.addNpc(NpcFactory.getInstance().newNpc(99, NpcName.CAY_THONG, 583, 408, 0));
//                break;
//
//            case MapName.LANG_FEARRI:
//                zone.addNpc(NpcFactory.getInstance().newNpc(99, NpcName.CAY_THONG, 611, 312, 0));
//                break;
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

        if (key == TOP_CAU_CA) {
            topCauCa(ranking, p);
        } else if (key == TOP_DIEU_VAI) {
            topChocolateCake(ranking, p);
        } else if (key == TOP_KEM) {
            topChocolateCake(ranking, p);
        }
        p.getEventPoint().setRewarded(key, 1);
    }

    public void topCauCa(int ranking, Char p) {
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


    @Override
    public void useItem(Char p, Item item) {
        switch (item.id) {
            case ItemName.DIEU_VAI:
                if (p.getSlotNull() == 0) {
                    p.warningBagFull();
                    return;
                }
                useEventItem(p, item.id, itemsRecFromGoldItem);
                break;
            case ItemName.DIEU_GIAY:
                if (p.getSlotNull() == 0) {
                    p.warningBagFull();
                    return;
                }
                useEventItem(p, item.id, itemsRecFromCoinItem);
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
