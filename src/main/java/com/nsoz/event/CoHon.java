package com.nsoz.event;

import com.nsoz.constants.*;
import com.nsoz.event.eventpoint.EventPoint;
import com.nsoz.item.Item;
import com.nsoz.item.ItemFactory;
import com.nsoz.lib.RandomCollection;
import com.nsoz.map.zones.Zone;
import com.nsoz.model.Char;
import com.nsoz.model.InputDialog;
import com.nsoz.model.Menu;
import com.nsoz.option.ItemOption;

import java.time.ZonedDateTime;

public class CoHon extends Event {
    public static final String TOP_MAM_CO_HON = "top_mam_co_hon";
    public static final String TOP_HO_LO = "top_ho_lo";
    private static final int LAM_MAM = 0;
    private static final int LAM_MAM_CO_HON_BAC = 1;
    private static final int LAM_MAM_CO_HON_VANG = 2;
    private static final int LAM_HO_LO = 3;
    private RandomCollection<Integer> vipItems = new RandomCollection<>();
    private ZonedDateTime start, end;

    public CoHon() {
        setId(Event.CO_HON);
        endTime.set(2023, 4, 12, 23, 59, 59);
        itemsThrownFromMonsters.add(5, ItemName.GAO);
        itemsThrownFromMonsters.add(5, ItemName.MUOI);
        itemsThrownFromMonsters.add(5, ItemName.BANH);
        itemsThrownFromMonsters.add(5, ItemName.TIEN_GIAY);

        keyEventPoint.add(EventPoint.DIEM_TIEU_XAI);
        keyEventPoint.add(TOP_MAM_CO_HON);
        keyEventPoint.add(TOP_HO_LO);

        itemsMamCung.add(10, ItemName.DA_CAP_4);
        itemsMamCung.add(10, ItemName.DA_CAP_5);
        itemsMamCung.add(10, ItemName.DA_CAP_6);
        itemsMamCung.add(10, ItemName.DA_CAP_7);
        itemsMamCung.add(10, ItemName.DA_CAP_8);
        itemsMamCung.add(0.02, ItemName.MAT_NA_SUPER_BROLY);
        itemsMamCung.add(0.02, ItemName.MAT_NA_ONNA_BUGEISHA);


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

        itemsRecFromGold2Item.add(0.5, ItemName.SHIRAIJI);
        itemsRecFromGold2Item.add(0.5, ItemName.HAJIRO);
        itemsRecFromGold2Item.add(2, ItemName.PHUONG_HOANG_BANG);
        itemsRecFromGold2Item.add(1, ItemName.PET_UNG_LONG);
        itemsRecFromGold2Item.add(2, ItemName.GAY_TRAI_TIM);
        itemsRecFromGold2Item.add(2, ItemName.GAY_MAT_TRANG);


        itemRuongMayMan.add(1, ItemName.PHUONG_HOANG_BANG);
        itemRuongMayMan.add(1, ItemName.BACH_HO);
        itemRuongMayMan.add(1, ItemName.HOA_KY_LAN);
        itemRuongMayMan.add(2, ItemName.PET_UNG_LONG);
        itemRuongMayMan.add(2, ItemName.TUAN_LOC);
        itemRuongMayMan.add(2, ItemName.HAKAIRO_YOROI);
        itemRuongMayMan.add(2, ItemName.SHIRAIJI);
        itemRuongMayMan.add(2, ItemName.HAJIRO);
        itemRuongMayMan.add(2, ItemName.GAY_TRAI_TIM);
        itemRuongMayMan.add(2, ItemName.GAY_MAT_TRANG);
    }


    @Override
    public void initStore() {
//        StoreManager.getInstance().addItem((byte) StoreManager.TYPE_MISCELLANEOUS, ItemStore.builder()
//                .id(996)
//                .itemID(ItemName.MAM_CUNG_CO_HON_BAC)
//                .coin(20000)
//                .expire(ConstTime.FOREVER)
//                .build());
//        StoreManager.getInstance().addItem((byte) StoreManager.TYPE_MISCELLANEOUS, ItemStore.builder()
//                .id(996)
//                .itemID(ItemName.MAM_CUNG_CO_HON_VANG)
//                .gold(25)
//                .expire(ConstTime.FOREVER)
//                .build());
//
//        StoreManager.getInstance().addItem((byte) StoreManager.TYPE_MISCELLANEOUS, ItemStore.builder()
//                .id(996)
//                .itemID(ItemName.MAY_DO_OAN_HON)
//                .gold(100)
//                .expire(ConstTime.FOREVER)
//                .build());
    }

    @Override
    public void action(Char p, int type, int amount) {
        if (isEnded()) {
            p.serverMessage("Sự kiện đã kết thúc!");
            return;
        }
        switch (type) {
            case LAM_MAM:
                lamMam(p, amount);
                break;
            case LAM_MAM_CO_HON_BAC:
                lamMamCoHonBac(p, amount);
                break;
            case LAM_MAM_CO_HON_VANG:
                lamMamCoHonVang(p, amount);
                break;
            case LAM_HO_LO:
                lamHoLo(p, amount);
                break;
        }
    }

    private void lamMam(Char p, int amount) {
        int[][] itemRequires = new int[][]{{ItemName.GAO, 5}, {ItemName.BANH, 5}, {ItemName.MUOI, 5}, {ItemName.TIEN_GIAY, 5}};
        int itemIdReceive = ItemName.MAM_CUNG_CO_HON;
        makeEventItem(p, amount, itemRequires, 0, 0, 30000, itemIdReceive);
    }

    private void lamMamCoHonBac(Char p, int amount) {
        int[][] itemRequires = new int[][]{{ItemName.GAO, 3}, {ItemName.BANH, 4}, {ItemName.MUOI, 3}, {ItemName.TIEN_GIAY, 4}};
        int itemIdReceive = ItemName.MAM_CUNG_CO_HON_BAC;
        boolean isDone = makeEventItem(p, amount, itemRequires, 0, 30000, 0, itemIdReceive);
        if (isDone) {
//            p.getEventPoint().addPoint(CoHon.TOP_MAM_CO_HON, amount);
//            p.getEventPoint().addPoint(EventPoint.DIEM_TIEU_XAI, amount);
        }
    }

    private void lamMamCoHonVang(Char p, int amount) {
        int[][] itemRequires = new int[][]{{ItemName.GAO, 4}, {ItemName.BANH, 3}, {ItemName.MUOI, 4}, {ItemName.TIEN_GIAY, 3}};
        int itemIdReceive = ItemName.MAM_CUNG_CO_HON_VANG;
        boolean isDone = makeEventItem(p, amount, itemRequires, 15, 0, 0, itemIdReceive);
        if (isDone) {
//            p.getEventPoint().addPoint(CoHon.TOP_MAM_CO_HON, 2*amount);
//            p.getEventPoint().addPoint(EventPoint.DIEM_TIEU_XAI, 2*amount);
        }
    }

    private void lamHoLo(Char p, int amount) {
        int[][] itemRequires = new int[][]{{ItemName.OAN_HON_SK, 2}};
        int itemIdReceive = ItemName.HO_LO;
        boolean isDone = makeEventItem(p, amount, itemRequires, 0, 80000, 0, itemIdReceive);
        if (isDone) {
//            p.getEventPoint().addPoint(CoHon.TOP_HO_LO, 2*amount);
//            p.getEventPoint().addPoint(EventPoint.DIEM_TIEU_XAI, 2*amount);
        }
    }

    @Override
    public void menu(Char p) {
        p.menus.clear();
        p.menus.add(new Menu(CMDMenu.EXECUTE, "Làm Mâm Cúng Cô Hồn Thường", () -> {
            p.setInput(new InputDialog(CMDInputDialog.EXECUTE, "Làm Mâm Cúng Cô Hồn Thường", () -> {
                InputDialog input = p.getInput();
                try {
                    int number = input.intValue();
                    action(p, LAM_MAM, number);
                } catch (NumberFormatException e) {
                    if (!input.isEmpty()) {
                        p.inputInvalid();
                    }
                }
            }));
            p.getService().showInputDialog();
        }));
        p.menus.add(new Menu(CMDMenu.EXECUTE, "Làm Mâm Cúng Cô Hồn Bạc", () -> {
            p.setInput(new InputDialog(CMDInputDialog.EXECUTE, "Làm Mâm Cúng Cô Hồn Bạc", () -> {
                InputDialog input = p.getInput();
                try {
                    int number = input.intValue();
                    action(p, LAM_MAM_CO_HON_BAC, number);
                } catch (NumberFormatException e) {
                    if (!input.isEmpty()) {
                        p.inputInvalid();
                    }
                }
            }));
            p.getService().showInputDialog();
        }));
        p.menus.add(new Menu(CMDMenu.EXECUTE, "Làm Mâm Cúng Cô Hồn Vàng", () -> {
            p.setInput(new InputDialog(CMDInputDialog.EXECUTE, "Làm Mâm Cúng Cô Hồn Vàng", () -> {
                InputDialog input = p.getInput();
                try {
                    int number = input.intValue();
                    action(p, LAM_MAM_CO_HON_VANG, number);
                } catch (NumberFormatException e) {
                    if (!input.isEmpty()) {
                        p.inputInvalid();
                    }
                }
            }));
            p.getService().showInputDialog();
        }));
        p.menus.add(new Menu(CMDMenu.EXECUTE, "Đổi hồ lô", () -> {
            p.setInput(new InputDialog(CMDInputDialog.EXECUTE, "Đổi hồ lô", () -> {
                InputDialog input = p.getInput();
                try {
                    int number = input.intValue();
                    action(p, LAM_HO_LO, number);
                } catch (NumberFormatException e) {
                    if (!input.isEmpty()) {
                        p.inputInvalid();
                    }
                }
            }));
            p.getService().showInputDialog();
        }));
        p.menus.add(new Menu(CMDMenu.EXECUTE, "Đua TOP", () -> {
            p.menus.clear();
            p.menus.add(new Menu(CMDMenu.EXECUTE, "Cúng cô hồn", () -> {
                p.menus.clear();
                p.menus.add(new Menu(CMDMenu.EXECUTE, "Bảng xếp hạng", () -> {
                    viewTop(p, TOP_MAM_CO_HON, "Cúng cô hồn", "%d. %s đã cúng %s lần");
                }));
                p.menus.add(new Menu(CMDMenu.EXECUTE, "Phần thưởng", () -> {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Top 1:").append("\n");
                    sb.append("- Vũ khí thời trang v.v\n");
                    sb.append("- Pet Thần Chết v.v\n");
                    sb.append("- 5 Rương huyền bí\n");
                    sb.append("- Cúp Lưu Niệm\n\n");
                    sb.append("Top 2:").append("\n");
                    sb.append("- Vũ khí thời trang v.v\n");
                    sb.append("- Pet Bóng Ma v.v\n");
                    sb.append("- 3 Rương huyền bí\n");
                    sb.append("- Cúp Lưu Niệm\n\n");
                    sb.append("Top 3:").append("\n");
                    sb.append("- Pet Thần Chết v.v\n");
                    sb.append("- 2 Rương huyền bí\n\n");
                    sb.append("Top 4 - 6:").append("\n");
                    sb.append("- Pet Thần Chết 2 tháng\n\n");
                    sb.append("Top 7 - 10:").append("\n");
                    sb.append("- Pet Thần Chết 1 tháng\n\n");
                    p.getService().showAlert("Phần thưởng", sb.toString());
                }));
                if (isEnded()) {
                    int ranking = getRanking(p, TOP_MAM_CO_HON);
                    if (ranking <= 10 && p.getEventPoint().getRewarded(TOP_MAM_CO_HON) == 0) {
                        p.menus.add(new Menu(CMDMenu.EXECUTE, String.format("Nhận Thưởng TOP %d", ranking), () -> {
                            receiveReward(p, TOP_MAM_CO_HON);
                        }));
                    }
                }
                p.getService().openUIMenu();
            }));
            p.menus.add(new Menu(CMDMenu.EXECUTE, "Diệt trừ yêu tinh", () -> {
                p.menus.clear();
                p.menus.add(new Menu(CMDMenu.EXECUTE, "Bảng xếp hạng", () -> {
                    viewTop(p, TOP_HO_LO, "Diệt trừ yêu tinh", "%d. %s đã tiêu diệt %s yêu tinh");
                }));
                p.menus.add(new Menu(CMDMenu.EXECUTE, "Phần thưởng", () -> {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Top 1:").append("\n");
                    sb.append("- Hỏa Kỳ Lân v.v MCS\n");
                    sb.append("- Mặt Nạ Hổ v.v MCS\n");
                    sb.append("- 5 rương huyền bí\n");
                    sb.append("- Cúp Lưu Niệm\n\n");
                    sb.append("Top 2:").append("\n");
                    sb.append("- Hỏa Kỳ Lân v.v MCS\n");
                    sb.append("- Mặt Nạ Hổ v.v MCS\n");
                    sb.append("- 3 rương huyền bí\n");
                    sb.append("- Cúp Lưu Niệm\n\n");
                    sb.append("Top 3:").append("\n");
                    sb.append("- Hỏa Kỳ Lân v.v MCS\n");
                    sb.append("- Mặt Nạ Hổ v.v MCS\n");
                    sb.append("- 3 rương huyền bí\n");
                    sb.append("- Cúp Lưu Niệm\n\n");
                    sb.append("Top 4 - 6:").append("\n");
                    sb.append("- Hỏa Kỳ Lân 2 tháng\n");
                    sb.append("- Mặt Nạ Hổ 1 tháng\n\n");
                    sb.append("Top 6 - 10:").append("\n");
                    sb.append("- Hỏa Kỳ Lân 1 tháng\n");
                    p.getService().showAlert("Phần thưởng", sb.toString());
                }));
                if (isEnded()) {
                    int ranking = getRanking(p, TOP_HO_LO);
                    if (ranking <= 10 && p.getEventPoint().getRewarded(TOP_HO_LO) == 0) {
                        p.menus.add(new Menu(CMDMenu.EXECUTE, String.format("Nhận Thưởng TOP %d", ranking), () -> {
                            receiveReward(p, TOP_HO_LO);
                        }));
                    }
                }
                p.getService().openUIMenu();
            }));
            p.getService().openUIMenu();
        }));
        p.menus.add(new Menu(CMDMenu.EXECUTE, "Hướng dẫn", () -> {
            StringBuilder sb = new StringBuilder();
            sb.append("-- HƯỚNG DẪN --").append("\n");
            sb.append("- Mâm cúng : 5 gạo + 5 bánh + 5 muối + 5 tiền giấy + 30k yên. ").append("\n");
            sb.append("- Mâm cúng bạc : 3 gạo + 3 bánh + 4 muối + 4 tiền giấy + 30k xu.").append("\n");
            sb.append("- Mâm cúng vàng : 3 gạo + 3 bánh + 4 muối + 4 tiền giấy + 15 lượng.").append("\n");
            sb.append("- Hồ lô : 2 oan hồn + 80k xu.").append("\n");
            sb.append("- Oan Hồn rớt tại VĐMQ khi bạn sử dụng thêm Máy Dò Oan Hồn tại shop Ghoso.").append("\n");
            p.getService().showAlert("Hướng Dẫn", sb.toString());
        }));
    }

    @Override
    public void initMap(Zone zone) {
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

        if (key == TOP_MAM_CO_HON) {
            topCoHon(ranking, p);
        } else if (key == TOP_HO_LO) {
            topHoLo(ranking, p);
        }
        p.getEventPoint().setRewarded(key, 1);
    }

    public void topCoHon(int ranking, Char p) {
        int tickId = p.gender == 1 ? ItemName.GAY_MAT_TRANG : ItemName.GAY_TRAI_TIM;
        Item fashionStick = ItemFactory.getInstance().newItem(tickId);
        Item petthanchet = ItemFactory.getInstance().newItem(ItemName.THAN_CHET);
        if (ranking == 1) {
            for (int i = 0; i < 5; i++) {
                Item mysteryChest = ItemFactory.getInstance().newItem(ItemName.RUONG_HUYEN_BI);
                p.addItemToBag(mysteryChest);
            }
            petthanchet.expire = System.currentTimeMillis() + ConstTime.DAY * 6000L;
            p.addItemToBag(petthanchet);
            p.addItemToBag(fashionStick);
        } else if (ranking == 2) {
            for (int i = 0; i < 3; i++) {
                Item mysteryChest = ItemFactory.getInstance().newItem(ItemName.RUONG_HUYEN_BI);
                p.addItemToBag(mysteryChest);
            }
            petthanchet.expire = System.currentTimeMillis() + ConstTime.DAY * 6000L;
            p.addItemToBag(petthanchet);
            p.addItemToBag(fashionStick);
        } else if (ranking == 3) {
            for (int i = 0; i < 3; i++) {
                Item mysteryChest = ItemFactory.getInstance().newItem(ItemName.RUONG_HUYEN_BI);
                p.addItemToBag(mysteryChest);
            }
            fashionStick.expire = System.currentTimeMillis() + ConstTime.DAY * 6000L;
            p.addItemToBag(petthanchet);
        } else if (ranking >= 4 && ranking <= 6) {
            petthanchet.expire = System.currentTimeMillis() + ConstTime.DAY * 60L;
            p.addItemToBag(petthanchet);
        } else if (ranking >= 7 && ranking <= 10) {
            petthanchet.expire = System.currentTimeMillis() + ConstTime.DAY * 30L;
            p.addItemToBag(petthanchet);
        }
    }

    public void topHoLo(int ranking, Char p) {
        Item mount = ItemFactory.getInstance().newItem(ItemName.HOA_KY_LAN);
        int tickId = p.gender == 1 ? ItemName.MAT_NA_HO : ItemName.MAT_NA_HO;
        Item fashionStick = ItemFactory.getInstance().newItem(tickId);
        if (ranking == 1) {
            mount.options.add(new ItemOption(ItemOptionName.NE_DON_ADD_POINT_TYPE_1, 200));
            mount.options.add(new ItemOption(ItemOptionName.CHINH_XAC_ADD_POINT_TYPE_1, 100));
            mount.options.add(new ItemOption(ItemOptionName.TAN_CONG_KHI_DANH_CHI_MANG_POINT_PERCENT_TYPE_1, 100));
            mount.options.add(new ItemOption(ItemOptionName.CHI_MANG_ADD_POINT_TYPE_1, 100));
            p.addItemToBag(mount);
            fashionStick.expire = System.currentTimeMillis() + ConstTime.DAY * 6000L;
            p.addItemToBag(fashionStick);
            for (int i = 0; i < 5; i++) {
                Item mysteryChest = ItemFactory.getInstance().newItem(ItemName.RUONG_HUYEN_BI);
                p.addItemToBag(mysteryChest);
            }
        } else if (ranking == 2) {
            for (int i = 0; i < 3; i++) {
                Item mysteryChest = ItemFactory.getInstance().newItem(ItemName.RUONG_HUYEN_BI);
                p.addItemToBag(mysteryChest);
            }
            mount.options.add(new ItemOption(ItemOptionName.NE_DON_ADD_POINT_TYPE_1, 200));
            mount.options.add(new ItemOption(ItemOptionName.CHINH_XAC_ADD_POINT_TYPE_1, 100));
            mount.options.add(new ItemOption(ItemOptionName.TAN_CONG_KHI_DANH_CHI_MANG_POINT_PERCENT_TYPE_1, 100));
            mount.options.add(new ItemOption(ItemOptionName.CHI_MANG_ADD_POINT_TYPE_1, 100));
            p.addItemToBag(mount);
            fashionStick.expire = System.currentTimeMillis() + ConstTime.DAY * 6000L;
            p.addItemToBag(fashionStick);
        } else if (ranking == 3) {
            mount.options.add(new ItemOption(ItemOptionName.NE_DON_ADD_POINT_TYPE_1, 200));
            mount.options.add(new ItemOption(ItemOptionName.CHINH_XAC_ADD_POINT_TYPE_1, 100));
            mount.options.add(new ItemOption(ItemOptionName.TAN_CONG_KHI_DANH_CHI_MANG_POINT_PERCENT_TYPE_1, 100));
            mount.options.add(new ItemOption(ItemOptionName.CHI_MANG_ADD_POINT_TYPE_1, 100));
            p.addItemToBag(mount);
            fashionStick.expire = System.currentTimeMillis() + ConstTime.DAY * 6000L;
            p.addItemToBag(fashionStick);
            for (int i = 0; i < 3; i++) {
                Item blueChest = ItemFactory.getInstance().newItem(ItemName.RUONG_HUYEN_BI);
                p.addItemToBag(blueChest);
            }
        } else if (ranking >= 4 && ranking <= 6) {
            mount.options.add(new ItemOption(ItemOptionName.NE_DON_ADD_POINT_TYPE_1, 200));
            mount.options.add(new ItemOption(ItemOptionName.CHINH_XAC_ADD_POINT_TYPE_1, 100));
            mount.options.add(new ItemOption(ItemOptionName.TAN_CONG_KHI_DANH_CHI_MANG_POINT_PERCENT_TYPE_1, 100));
            mount.options.add(new ItemOption(ItemOptionName.CHI_MANG_ADD_POINT_TYPE_1, 100));
            mount.expire = System.currentTimeMillis() + ConstTime.DAY * 60L;
            p.addItemToBag(mount);
            fashionStick.expire = System.currentTimeMillis() + ConstTime.DAY * 30L;
            p.addItemToBag(fashionStick);
        } else if (ranking >= 7 && ranking <= 10) {
            mount.options.add(new ItemOption(ItemOptionName.NE_DON_ADD_POINT_TYPE_1, 200));
            mount.options.add(new ItemOption(ItemOptionName.CHINH_XAC_ADD_POINT_TYPE_1, 100));
            mount.options.add(new ItemOption(ItemOptionName.TAN_CONG_KHI_DANH_CHI_MANG_POINT_PERCENT_TYPE_1, 100));
            mount.options.add(new ItemOption(ItemOptionName.CHI_MANG_ADD_POINT_TYPE_1, 100));
            mount.expire = System.currentTimeMillis() + ConstTime.DAY * 30L;
            p.addItemToBag(mount);
            fashionStick.expire = System.currentTimeMillis() + ConstTime.DAY * 30L;
            p.addItemToBag(fashionStick);
        }
    }

    @Override
    public void useItem(Char p, Item item) {
        switch (item.id) {
            //mam thuong
            case ItemName.MAM_CUNG_CO_HON:
                if (p.getSlotNull() == 0) {
                    p.warningBagFull();
                    return;
                }
                useEventItem(p, item.id, itemsMamCung);
                return;
            //mam bac
            case ItemName.MAM_CUNG_CO_HON_BAC:
                if (p.getSlotNull() == 0) {
                    p.warningBagFull();
                    return;
                }
                useEventItem(p, item.id, itemsRecFromCoinItem);
                return;
            //mam vang
            case ItemName.MAM_CUNG_CO_HON_VANG:
                if (p.getSlotNull() == 0) {
                    p.warningBagFull();
                    return;
                }
                useEventItem(p, item.id, itemsRecFromGold2Item);
                return;
            //ho lo
            case ItemName.HO_LO: {
                if (p.getSlotNull() == 0) {
                    p.warningBagFull();
                    return;
                }
                //mỗi 1 hồ lô sẽ được 2 điểm
//                p.getEventPoint().addPoint(CoHon.TOP_HO_LO, 2);
//                p.getEventPoint().addPoint(EventPoint.DIEM_TIEU_XAI, 2);
                useEventItem(p, item.id, itemsRecFromGold2Item);
                return;
            }

            case ItemName.HOP_QUA_MAY_MAN: {
                if (p.getSlotNull() == 0) {
                    p.warningBagFull();
                    return;
                }
                useEventItem(p, item.id, itemRuongMayMan);
            }

        }

    }
}
