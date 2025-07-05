/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nsoz.event;

import com.nsoz.constants.*;
import com.nsoz.effect.Effect;
import com.nsoz.effect.EffectAutoDataManager;
import com.nsoz.event.eventpoint.EventPoint;
import com.nsoz.item.Item;
import com.nsoz.item.ItemFactory;
import com.nsoz.lib.RandomCollection;
import com.nsoz.map.Map;
import com.nsoz.map.Tree;
import com.nsoz.map.zones.Zone;
import com.nsoz.model.Char;
import com.nsoz.model.InputDialog;
import com.nsoz.model.Menu;
import com.nsoz.option.ItemOption;
import com.nsoz.server.GlobalService;
import com.nsoz.store.ItemStore;
import com.nsoz.store.StoreManager;
import com.nsoz.util.NinjaUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Source được chia sẻ miễn phí tại: nsotien.com
 */
public class Halloween extends Event {

    public static final String TOP_DEVIL_BOX = "devil_box";
    public static final String TOP_KEO_TAO = "keo_tao";
    public static final String INVITATION_NUMBER = "invitation_number";
    private static final int HOP_MA_QUY = 0;
    private static final int KEO_TAO = 1;
    private static final int CHIA_KHOA = 2;
    private static final int MA_VAT = 3;
    private static final int THOI_TRANG = 4;
    protected RandomCollection<Integer> itemsThrownFromMonsters2;
    private RandomCollection<Integer> rd = new RandomCollection<>();
    private RandomCollection<Integer> vipItems = new RandomCollection<>();

    public Halloween() {

        // TODO: time chạy sự kiện
        setId(Event.HALLOWEEN);
        endTime.set(2024, 11, 12, 8, 50, 59);

        keyEventPoint.add(EventPoint.DIEM_TIEU_XAI);
        keyEventPoint.add(TOP_DEVIL_BOX);
        keyEventPoint.add(TOP_KEO_TAO);
        keyEventPoint.add(INVITATION_NUMBER);

        // TODO: rơi nlsk khi đánh quái
        itemsThrownFromMonsters.add(100, ItemName.QUA_TAO);
        itemsThrownFromMonsters.add(100, ItemName.QUA_TAO);
        itemsThrownFromMonsters.add(100, ItemName.QUA_TAO);
        itemsThrownFromMonsters.add(100, ItemName.QUA_TAO);
        itemsThrownFromMonsters.add(100, ItemName.QUA_TAO);
        itemsThrownFromMonsters.add(100, ItemName.MAT_ONG);
        itemsThrownFromMonsters.add(100, ItemName.MAT_ONG);
        itemsThrownFromMonsters.add(100, ItemName.MAT_ONG);
        itemsThrownFromMonsters.add(100, ItemName.MAT_ONG);
        itemsThrownFromMonsters.add(100, ItemName.MAT_ONG);
        itemsThrownFromMonsters.add(100, ItemName.TAN_LINH);
        itemsThrownFromMonsters.add(100, ItemName.TAN_LINH);
        itemsThrownFromMonsters.add(100, ItemName.TAN_LINH);
        itemsThrownFromMonsters.add(100, ItemName.TAN_LINH);
        itemsThrownFromMonsters.add(100, ItemName.TAN_LINH);
        itemsThrownFromMonsters.add(100, ItemName.XUONG_THU);
        itemsThrownFromMonsters.add(100, ItemName.XUONG_THU);
        itemsThrownFromMonsters.add(100, ItemName.XUONG_THU);
        itemsThrownFromMonsters.add(100, ItemName.XUONG_THU);
        itemsThrownFromMonsters.add(100, ItemName.XUONG_THU);


//        itemsThrownFromMonsters2 = new RandomCollection<>();
//        itemsThrownFromMonsters2.add(1, ItemName.H);
//        itemsThrownFromMonsters2.add(1, ItemName.A);
//        itemsThrownFromMonsters2.add(2, ItemName.L);
//        itemsThrownFromMonsters2.add(1, ItemName.O);
//        itemsThrownFromMonsters2.add(1, ItemName.W);
//        itemsThrownFromMonsters2.add(2, ItemName.E);
//        itemsThrownFromMonsters2.add(1, ItemName.N);



// TODO: tỉ lệ đồ kẹo táo

        itemsRecFromCoinItem.add(30, ItemName.MAT_NA_THO);
        itemsRecFromCoinItem.add(30, ItemName.MAT_NA_THO_NU);
        itemsRecFromGoldItem.add(1, ItemName.TA_LINH_MA);
        itemsRecFromGoldItem.add(1, ItemName.PHONG_THUONG_MA);
        itemsRecFromGoldItem.add(1, ItemName.XICH_TU_MA);


        // TODO: tỉ lệ đồ hộp ma quỷ

        itemsRecFromCoinItem.add(30, ItemName.MAT_NA_THO);
        itemsRecFromCoinItem.add(30, ItemName.MAT_NA_THO_NU);
        itemsRecFromGoldItem.add(10, ItemName.TA_LINH_MA);
        itemsRecFromGoldItem.add(10, ItemName.PHONG_THUONG_MA);
        itemsRecFromGoldItem.add(10, ItemName.XICH_TU_MA);


    }


    // todo: sử dụng item sự kiện
    @Override
    public void useItem(Char _char, Item item) {

        if (item.id == ItemName.THU_MOI_LE_HOI) {
            _char.getEventPoint().addPoint(INVITATION_NUMBER, 1);
            _char.serverMessage(
                    "Số lượt tham gia lễ hội hoá trang: " + _char.getEventPoint().find(INVITATION_NUMBER).getPoint());
            _char.removeItem(item.index, 1, true);
        } else if (item.id == ItemName.BI_MA) {
            int time = 8 * 60 * 60 * 1000;
            short param = 2;
            byte templateID = 43;
            Effect eff = _char.getEm().findByID(templateID);
            if (eff != null) {
                eff.addTime(time);
                _char.getEm().setEffect(eff);
            } else {
                Effect effect = new Effect(templateID, time, param);
                effect.param2 = item.id;
                _char.getEm().setEffect(effect);
            }
            _char.removeItem(item.index, 1, true);
        } else if (item.id == ItemName.KEO_TAO) {
            if (_char.getSlotNull() == 0) {
                _char.warningBagFull();
                return;
            }
            useEventItem(_char, item.id, itemsRecFromCoinItem);
            _char.getEventPoint().addPoint(TOP_KEO_TAO, 1);
        } else if (item.id == ItemName.HOP_MA_QUY) {
            int indexItem = _char.getIndexItemByIdInBag(ItemName.CHIA_KHOA);
            if (indexItem == -1) {
                _char.serverMessage("Hãy mua chìa khóa ở npc Goosho để mở hộp ma quỷ");
                return;
            }
            if (_char.getSlotNull() == 0) {
                _char.warningBagFull();
                return;
            }
            int[][] itemRequires = new int[][]{{ItemName.HOP_MA_QUY, 1}, {ItemName.CHIA_KHOA, 1}};
            boolean isDone = useEventItem(_char, 1, itemRequires, 0, 0, 0, itemsRecFromGoldItem);
            _char.getEventPoint().addPoint(TOP_DEVIL_BOX, 1);

        } else if (item.id == ItemName.GAY_PHEP || item.id == ItemName.CHOI_BAY) {
            if (_char.getSlotNull() == 0) {
                _char.warningBagFull();
                return;
            }
            useVipEventItem(_char, item.id == ItemName.GAY_PHEP ? 1 : 2, vipItems);
            _char.removeItem(item.index, 1, true);
        }
    }

    public int randomItemID2() {
        return itemsThrownFromMonsters2.next();
    }

    @Override
    public void action(Char p, int type, int amount) {
        if (isEnded()) {
            p.serverMessage("Sự kiện đã kết thúc");
            return;
        }
        switch (type) {
            case CHIA_KHOA:
                makeKey(p, amount);
                break;

            case HOP_MA_QUY:
                makeDevilBox(p, amount);
                break;

            case KEO_TAO:
                makeAppleCandy(p, amount);
                break;

            case MA_VAT:
                makeMagicItem(p, amount);
                break;

            case THOI_TRANG:
                makeFashionItem(p);
                break;
        }
    }

    // TODO: đổi hộp ma quỷ
    public void makeDevilBox(Char p, int amount) {
        int[][] itemRequires = new int[][]{{ItemName.XUONG_THU, 5}, {ItemName.TAN_LINH, 2},
                {ItemName.MA_VAT, 1}};
        int itemIdReceive = ItemName.HOP_MA_QUY;
        boolean isDone = makeEventItem(p, amount, itemRequires, 0, 0, 0, itemIdReceive);
        if (isDone) {
            p.getEventPoint().addPoint(EventPoint.DIEM_TIEU_XAI, amount);
        }
    }

    // TODO: đổi kẹo táo
    public void makeAppleCandy(Char p, int amount) {
        int[][] itemRequires = new int[][]{{ItemName.QUA_TAO, 2}, {ItemName.MAT_ONG, 3}};
        int itemIdReceive = ItemName.KEO_TAO;
        makeEventItem(p, amount, itemRequires, 0, 100000, 0, itemIdReceive);
    }

    // TODO: đổi chữ lấy chìa khóa và ma vật
    public void makeKey(Char p, int amount) {
        int[][] itemRequires = new int[][]{{ItemName.H, 1}, {ItemName.A, 1}, {ItemName.L, 2}, {ItemName.O, 1},
                {ItemName.W, 1}, {ItemName.E, 2}, {ItemName.N, 1}};
        int itemIdReceive = ItemName.CHIA_KHOA;
        makeEventItem(p, amount, itemRequires, 0, 0, 0, itemIdReceive);
    }

    public void makeMagicItem(Char p, int amount) {
        int[][] itemRequires = new int[][]{{ItemName.H, 1}, {ItemName.A, 1}, {ItemName.L, 2}, {ItemName.O, 1},
                {ItemName.W, 1}, {ItemName.E, 2}, {ItemName.N, 1}};
        int itemIdReceive = ItemName.MA_VAT;
        makeEventItem(p, amount, itemRequires, 0, 0, 0, itemIdReceive);
    }

    // TODO: đổi đồ thời trang
    public void makeFashionItem(Char p) {
        if (p.user.gold < 1000) {
            p.getService().npcChat(NpcName.TIEN_NU, "Cần 500 lượng để đổi.");
            return;
        }
        int index = p.getIndexItemByIdInBag(ItemName.KEO_TAO);
        Item itm = null;
        if (index != -1) {
            itm = p.bag[index];
        }
        if (itm == null || !itm.has(100)) {
            p.getService().npcChat(NpcName.TIEN_NU, "Không đủ kẹo táo.");
            return;
        }
        p.addGold(-1000);
        p.removeItem(index, 100, true);
        int maskId = p.gender == 1 ? ItemName.SHIRAIJI : ItemName.HAJIRO;
        Item item = ItemFactory.getInstance().newItem(maskId);
        item.expire = System.currentTimeMillis() + (long) (86400000 * 7);
        item.isLock = true;
        p.addItemToBag(item);
    }

    // TODO: đổi quà từ điểm tiêu sài
    public void makeMagicWeapon(Char p, int type) {
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

        Item item = ItemFactory.getInstance().newItem(type == 1 ? ItemName.GAY_PHEP : ItemName.CHOI_BAY);
        p.addItemToBag(item);
        p.getEventPoint().subPoint(EventPoint.DIEM_TIEU_XAI, point);
    }

    // TODO: menu npc
    @Override
    public void menu(Char p) {

        if (!isEnded()) {
            p.menus.add(new Menu(CMDMenu.EXECUTE, "Làm hộp ma quỷ", () -> {
                p.setInput(new InputDialog(CMDInputDialog.EXECUTE, "Hộp ma quỷ", () -> {
                    InputDialog input = p.getInput();
                    try {
                        int number = input.intValue();
                        action(p, HOP_MA_QUY, number);
                    } catch (Exception e) {
                        if (!input.isEmpty()) {
                            p.inputInvalid();
                        }
                    }
                }));
                p.getService().showInputDialog();
            }));
            p.menus.add(new Menu(CMDMenu.EXECUTE, "Làm kẹo táo", () -> {
                p.setInput(new InputDialog(CMDInputDialog.EXECUTE, "Kẹo táo", () -> {
                    InputDialog input = p.getInput();
                    try {
                        int number = input.intValue();
                        action(p, KEO_TAO, number);
                    } catch (Exception e) {
                        if (!input.isEmpty()) {
                            p.inputInvalid();
                        }
                    }
                }));
                p.getService().showInputDialog();
            }));

            p.menus.add(new Menu(CMDMenu.EXECUTE, "Đổi đồ thời trang", () -> {
                action(p, THOI_TRANG, 1);
            }));

        }
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
        p.menus.add(new Menu(CMDMenu.EXECUTE, "Top sự kiện", () -> {
            p.menus.clear();
            p.menus.add(new Menu(CMDMenu.EXECUTE, "Bxh hộp ma quỷ", () -> {
                if (p.user.isTien()) {
                    viewTop(p, TOP_DEVIL_BOX, "Top hộp ma quỷ", "%d. %s đã mở %s hộp ma quỷ");
                } else {
                    viewTop(p, TOP_DEVIL_BOX, "Top hộp ma quỷ", "%d. %s ");
                }
            }));
            p.menus.add(new Menu(CMDMenu.EXECUTE, "Bxh kẹo táo", () -> {
                if (p.user.isTien()) {
                    viewTop(p, TOP_KEO_TAO, "Top kẹo táo", "%d. %s đã mở %s kẹo táo");
                } else {
                viewTop(p, TOP_KEO_TAO, "Top kẹo táo", "%d. %s ");
                }
            }));

            p.menus.add(new Menu(CMDMenu.EXECUTE, "Nhận top 5k", () -> {
                int pont = getPoint(p, TOP_DEVIL_BOX);
                if (p.getEventPoint().getRewarded(TOP_DEVIL_BOX) == 1) {
                    p.serverDialog("Bạn đã nhận thưởng mốc này rồi");
                    return;
                }
                if (pont >= 5000) {
                    int idItem = NinjaUtils.nextInt(814, 818);
                    Item mount = ItemFactory.getInstance().newItem(idItem);

                    ArrayList<ItemOption> options = new ArrayList<>();
                    options.add(new ItemOption(0, NinjaUtils.nextInt(200, 500))); // tấn công ngoai
                    options.add(new ItemOption(1, NinjaUtils.nextInt(200, 500))); // tấn công nội
                    options.add(new ItemOption(2, NinjaUtils.nextInt(100, 150))); // kháng
                    options.add(new ItemOption(3, NinjaUtils.nextInt(100, 150))); // kháng
                    options.add(new ItemOption(4, NinjaUtils.nextInt(100, 150))); // kháng

                    options.add(new ItemOption(5, NinjaUtils.nextInt(50, 100))); // né đòn
                    options.add(new ItemOption(6, NinjaUtils.nextInt(1000, 2000))); // hp tối đa

                    options.add(new ItemOption(8, NinjaUtils.nextInt(50, 200))); // vật công ngoại
                    options.add(new ItemOption(9, NinjaUtils.nextInt(50, 200))); // vật công nội

                    options.add(new ItemOption(57, NinjaUtils.nextInt(80, 120))); // cộng tiềm năng cho tất cả
                    options.add(new ItemOption(58, NinjaUtils.nextInt(20, 30))); // cộng % tiềm năng
                    options.add(new ItemOption(87, NinjaUtils.nextInt(1000, 5000))); // tấn công
                    mount.expire = System.currentTimeMillis() + ConstTime.DAY * 15;
                    mount.isLock = false;

                    for (int i = 1; i <= 7; i++) {
                        int indexRandom = NinjaUtils.nextInt(options.size());
                        mount.options.add(options.get(indexRandom));
                        options.remove(indexRandom);
                    }
                    if (p.getSlotNull() < 6) {
                        p.getService().npcChat(NpcName.TIEN_NU, "Hãy chừa 6 ô trống trong hành trang để nhận quà.");
                        return;
                    }
                    p.addItemToBag(mount);
                    p.getEventPoint().setRewarded(TOP_DEVIL_BOX, 1);
                } else {
                    p.serverDialog("Bạn chỉ có " + pont + " điểm bạn cần " + (5000 - pont) + " điểm nữa");
                }
            }));

            p.menus.add(new Menu(CMDMenu.EXECUTE, "Nhận top 50k", () -> {
                int pont = getPoint(p, TOP_DEVIL_BOX);
                if (p.getEventPoint().getRewarded50k(TOP_DEVIL_BOX) == 1) {
                    p.serverDialog("Bạn đã nhận thưởng mốc này rồi");
                    return;
                }
                if (pont >= 50000) {
                    int idItem = NinjaUtils.nextInt(814, 818);
                    Item mount = ItemFactory.getInstance().newItem(idItem);

                    ArrayList<ItemOption> options = new ArrayList<>();
                    options.add(new ItemOption(0, NinjaUtils.nextInt(200, 500))); // tấn công ngoai
                    options.add(new ItemOption(1, NinjaUtils.nextInt(200, 500))); // tấn công nội
                    options.add(new ItemOption(2, NinjaUtils.nextInt(100, 150))); // kháng
                    options.add(new ItemOption(3, NinjaUtils.nextInt(100, 150))); // kháng
                    options.add(new ItemOption(4, NinjaUtils.nextInt(100, 150))); // kháng

                    options.add(new ItemOption(5, NinjaUtils.nextInt(50, 100))); // né đòn
                    options.add(new ItemOption(6, NinjaUtils.nextInt(1000, 2000))); // hp tối đa

                    options.add(new ItemOption(8, NinjaUtils.nextInt(50, 200))); // vật công ngoại
                    options.add(new ItemOption(9, NinjaUtils.nextInt(50, 200))); // vật công nội

                    options.add(new ItemOption(57, NinjaUtils.nextInt(80, 120))); // cộng tiềm năng cho tất cả
                    options.add(new ItemOption(58, NinjaUtils.nextInt(20, 30))); // cộng % tiềm năng
                    options.add(new ItemOption(87, NinjaUtils.nextInt(1000, 5000))); // tấn công
                    mount.isLock = false;

                    for (int i = 1; i <= 7; i++) {
                        int indexRandom = NinjaUtils.nextInt(options.size());
                        mount.options.add(options.get(indexRandom));
                        options.remove(indexRandom);
                    }
                    if (p.getSlotNull() < 6) {
                        p.getService().npcChat(NpcName.TIEN_NU, "Hãy chừa 6 ô trống trong hành trang để nhận quà.");
                        return;
                    }
                    p.addItemToBag(mount);
                    p.getEventPoint().setRewarded50k(TOP_DEVIL_BOX, 1);
                } else {
                    p.serverDialog("Bạn chỉ có " + pont + " điểm bạn cần " + (50000 - pont) + " điểm nữa");
                }

            }));

            p.menus.add(new Menu(CMDMenu.EXECUTE, "Quà 50K toàn server", () -> {
                int pont = getPontAll(TOP_DEVIL_BOX);
                if (p.getEventPoint().getRewardedAll(TOP_DEVIL_BOX) == 1) {
                    p.serverDialog("Bạn đã nhận thưởng mốc này rồi");
                    return;
                }
                if (pont >= 50000) {
                    Item itm = ItemFactory.getInstance().newItem(ItemName.HOA_KY_LAN);
                    itm.isLock = false;
                    itm.expire = System.currentTimeMillis() + ConstTime.DAY * 3;
                    if (p.getSlotNull() < 6) {
                        p.getService().npcChat(NpcName.TIEN_NU, "Hãy chừa 6 ô trống trong hành trang để nhận quà.");
                        return;
                    }
                    p.addItemToBag(itm);
                    p.getEventPoint().setRewardedAll(TOP_DEVIL_BOX, 1);
                } else {
                    p.serverDialog("Toàn server chỉ có " + pont + " điểm cần thêm " + (50000 - pont) + " điểm nữa mới nhận được");
                }
            }));
            p.menus.add(new Menu(CMDMenu.EXECUTE, "Quà đua top", () -> {
                StringBuilder sb = new StringBuilder();
                sb.append("Top 1 :\n" +
                        "\n" +
                        "1 Thú cưỡi Hỏa kì lân vĩnh viễn\n" +
                        "2 Rương Huyền bí\n" +
                        "1 Pet bí rễ hành có chống pk 30 ngày\n" +
                        "\n" +
                        "Top 2-10\n" +
                        "\n" +
                        "1 Thú cưỡi Hỏa kì lân max 5* 30 ngày\n" +
                        "2 Rương Bạch Ngân\n" +
                        "1 pet bí rễ hành có chống pk 15 ngày\n" +
                        "\n" +
                        "Lưu ý :\n" +
                        "- Tối thiểu sử dụng 50,000 kẹo táo được xếp top . Ai xếp trước tính người đó\n" +
                        "\n" +
                        "- Có 3 bộ option được chọn khi nhận thưởng hỏa kì lân\n" +
                        "* Công :\n" +
                        "Tăng tấn công %\n" +
                        "Chí mạng\n" +
                        "Chính xác\n" +
                        "Tấn công khi đánh chí mạng\n" +
                        "\n" +
                        "* Thủ :\n" +
                        "Chịu sát thương cho chủ\n" +
                        "Giảm sát thương\n" +
                        "Hp tối đa\n" +
                        "Kháng tất cả\n" +
                        "\n" +
                        "* Hồi phục :\n" +
                        "Mỗi 5s hồi phục mp\n" +
                        "Mỗi 5s hồi phục hp\n" +
                        "Né đòn\n" +
                        "Cộng tiềm năng");
                p.getService().showAlert("Phần thưởng", sb.toString());
            }));
            if (isEnded()) {
                int ranking = getRanking(p, TOP_KEO_TAO);
                if (ranking <= 10 && p.getEventPoint().getRewarded(TOP_KEO_TAO) == 0) {
                    p.menus.add(new Menu(CMDMenu.EXECUTE, String.format("Nhận Thưởng TOP %d", ranking), () -> {
                        receiveReward(p, TOP_KEO_TAO);
                    }));
                }
            }
            p.getService().openUIMenu();
        }));
        p.menus.add(new Menu(CMDMenu.EXECUTE, "Hướng dẫn", () -> {
            StringBuilder sb = new StringBuilder();
//            sb.append("- Điểm tiêu xài: ")
//                    .append(NinjaUtils.getCurrency(p.getEventPoint().getPoint(EventPoint.DIEM_TIEU_XAI))).append("\n");
            sb.append("- Đổi kẹo táo : 2 quả táo + 3 mật ong + 100k xu \n" +
                    "- Đổi hộp ma quỷ : 5 xương thú + 2 tàn linh + 1 ma vật\n" +
                    "- Mở hộp ma quỷ cần có chìa khóa mua tại goosho\n" +
                    "- Đổi đồ thời trang : 1000 lượng + 100 kẹo táo\n" +
                    "\n" +
                    "- Top 10 ăn kẹo táo cơ hội nhận hỏa kì lân 5* xem tại mục Top sự kiện\n\n" +
                    "- Cá nhân cán mốc mở 5k hộp ma quỷ : 1 mặt nạ trang bị 2 với 7 dòng random hạn 15 ngày\n\n" +
                    "- Cá nhân cán mốc mở 50k hộp ma quỷ : 1 mặt nạ trang bị 2 với 7 dòng random vĩnh viễn\n\n" +
                    "- Toàn server cán mốc 50k hộp ma quỷ : ai cũng nhận được 1 hỏa kì lận 3 ngày");
            p.getService().showAlert("Hướng Dẫn", sb.toString());
        }));
    }


    // TODO: cửa hàng goosho
    @Override
    public void initStore() {
        StoreManager.getInstance().addItem((byte) StoreManager.TYPE_MISCELLANEOUS, ItemStore.builder()
                .id(998)
                .itemID(ItemName.CHIA_KHOA)
                .coin(10000)
                .expire(ConstTime.FOREVER)
                .build());

        StoreManager.getInstance().addItem((byte) StoreManager.TYPE_MISCELLANEOUS, ItemStore.builder()
                .id(999)
                .itemID(ItemName.MA_VAT)
                .gold(10)
                .expire(ConstTime.FOREVER)
                .build());

//        StoreManager.getInstance().addItem((byte) StoreManager.TYPE_MISCELLANEOUS, ItemStore.builder()
//                .id(1000)
//                .itemID(ItemName.BI_MA)
//                .gold(5)
//                .expire(ConstTime.FOREVER)
//                .build());

        List<ItemOption> options = new ArrayList<ItemOption>();
        options.add(new ItemOption(ItemOptionName.MOI_NUA_GIAY_HOI_PHUC_POINT_HP_VA_MP_TYPE_8, 400));
        StoreManager.getInstance().addItem((byte) StoreManager.TYPE_MISCELLANEOUS, ItemStore.builder()
                .id(1000)
                .itemID(ItemName.BI_RE_HANH)
                .gold(500)
                .expire(ConstTime.NSO)
                .options(options)
                .build());

        List<ItemOption> options2 = new ArrayList<ItemOption>();
        options2.add(new ItemOption(ItemOptionName.HP_TOI_DA_POINT_TYPE_1, 2000));
        options2.add(new ItemOption(ItemOptionName.CONG_THEM_TIEM_NANG_ADD_POINT_PERCENT_TYPE_0, 25));
        StoreManager.getInstance().addItem((byte) StoreManager.TYPE_MISCELLANEOUS, ItemStore.builder()
                .id(1001)
                .itemID(ItemName.JACK_HOLLOW)
                .gold(1000)
                .expire(ConstTime.NSO)
                .options(options2)
                .build());

//        StoreManager.getInstance().addItem((byte) StoreManager.TYPE_MISCELLANEOUS, ItemStore.builder()
//                .id(1002)
//                .itemID(ItemName.THU_MOI_LE_HOI)
//                .gold(20)
//                .expire(ConstTime.FOREVER)
//                .build());
    }

    //TODO: quà top sự kiện
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
        if (p.getSlotNull() < 6) {
            p.getService().serverDialog("Bạn cần để hành trang trống tối thiểu 6 ô");
            return;
        }

        Item mount = ItemFactory.getInstance().newItem(ItemName.HOA_KY_LAN);
        Item choiBay = ItemFactory.getInstance().newItem(ItemName.CHOI_BAY);
        int maskId = p.gender == 1 ? ItemName.SHIRAIJI : ItemName.HAJIRO;
        Item mask = ItemFactory.getInstance().newItem(maskId);

        if (ranking == 1) {
            mount.options.add(new ItemOption(ItemOptionName.NE_DON_ADD_POINT_TYPE_1, 200));
            mount.options.add(new ItemOption(ItemOptionName.CHINH_XAC_ADD_POINT_TYPE_1, 100));
            mount.options.add(new ItemOption(ItemOptionName.TAN_CONG_KHI_DANH_CHI_MANG_POINT_PERCENT_TYPE_1, 100));
            mount.options.add(new ItemOption(ItemOptionName.CHI_MANG_ADD_POINT_TYPE_1, 100));
            mount.options.add(new ItemOption(ItemOptionName.CONG_THEM_TIEM_NANG_ADD_POINT_PERCENT_TYPE_0, 20));
            mount.options.add(new ItemOption(ItemOptionName.TAN_CONG_ADD_POINT_PERCENT_TYPE_8, 20));

            mask.options.add(new ItemOption(125, 3000));
            mask.options.add(new ItemOption(117, 3000));
            mask.options.add(new ItemOption(94, 10));
            mask.options.add(new ItemOption(136, 20));
            mask.options.add(new ItemOption(127, 10));
            mask.options.add(new ItemOption(130, 10));
            mask.options.add(new ItemOption(131, 10));

            choiBay.setQuantity(10);
            p.addItemToBag(choiBay);
            for (int i = 0; i < 3; i++) {
                Item mysteryChest = ItemFactory.getInstance().newItem(ItemName.RUONG_HUYEN_BI);
                p.addItemToBag(mysteryChest);
            }
        } else if (ranking == 2) {
            choiBay.setQuantity(5);
            p.addItemToBag(choiBay);
            Item mysteryChest = ItemFactory.getInstance().newItem(ItemName.RUONG_HUYEN_BI);
            p.addItemToBag(mysteryChest);
        } else if (ranking >= 3 && ranking <= 5) {
            mount.expire = System.currentTimeMillis() + 86400000 * 90;
            choiBay.setQuantity(3);
            p.addItemToBag(choiBay);
            for (int i = 0; i < 2; i++) {
                Item blueChest = ItemFactory.getInstance().newItem(ItemName.RUONG_HUYEN_BI);
                p.addItemToBag(blueChest);
            }
        } else {
            mount.expire = System.currentTimeMillis() + 86400000 * 30;
            Item blueChest = ItemFactory.getInstance().newItem(ItemName.RUONG_HUYEN_BI);
            p.addItemToBag(blueChest);
        }

        p.addItemToBag(mount);
        p.addItemToBag(mask);

        p.getEventPoint().setRewarded(key, 1);
    }

    // TODO: cây effect
    @Override
    public void initMap(Zone zone) {
        Map map = zone.map;
        int mapID = map.id;
        switch (mapID) {
            case MapName.KHU_LUYEN_TAP:
                break;
            case MapName.TRUONG_OOKAZA:
                zone.addTree(Tree.builder().id(EffectAutoDataManager.CAY_HALLOWEEN).x((short) 1426).y((short) 552).build());
                zone.addTree(Tree.builder().id(EffectAutoDataManager.CAY_HALLOWEEN).x((short) 784).y((short) 648).build());
                break;
            case MapName.TRUONG_HARUNA:
                zone.addTree(Tree.builder().id(EffectAutoDataManager.CAY_HALLOWEEN).x((short) 502).y((short) 408).build());
                zone.addTree(Tree.builder().id(EffectAutoDataManager.CAY_HALLOWEEN).x((short) 1863).y((short) 360).build());
                zone.addTree(Tree.builder().id(EffectAutoDataManager.CAY_HALLOWEEN).x((short) 2048).y((short) 360).build());
                break;
            case MapName.TRUONG_HIROSAKI:
                zone.addTree(Tree.builder().id(EffectAutoDataManager.CAY_HALLOWEEN).x((short) 1207).y((short) 168).build());
                break;

            case MapName.LANG_TONE:
                zone.addTree(Tree.builder().id(EffectAutoDataManager.CAY_HALLOWEEN).x((short) 1427).y((short) 264).build());
                break;

            case MapName.LANG_KOJIN:
                zone.addTree(Tree.builder().id(EffectAutoDataManager.CAY_HALLOWEEN).x((short) 621).y((short) 288).build());
                break;

            case MapName.LANG_CHAI:
                zone.addTree(Tree.builder().id(EffectAutoDataManager.CAY_HALLOWEEN).x((short) 1804).y((short) 384).build());
                break;

            case MapName.LANG_SANZU:
                zone.addTree(Tree.builder().id(EffectAutoDataManager.CAY_HALLOWEEN).x((short) 320).y((short) 288).build());
                break;

            case MapName.LANG_CHAKUMI:
                zone.addTree(Tree.builder().id(EffectAutoDataManager.CAY_HALLOWEEN).x((short) 626).y((short) 312).build());
                break;

            case MapName.LANG_ECHIGO:
                zone.addTree(Tree.builder().id(EffectAutoDataManager.CAY_HALLOWEEN).x((short) 360).y((short) 360).build());
                break;

            case MapName.LANG_OSHIN:
                zone.addTree(Tree.builder().id(EffectAutoDataManager.CAY_HALLOWEEN).x((short) 921).y((short) 408).build());
                break;

            case MapName.LANG_SHIIBA:
                zone.addTree(Tree.builder().id(EffectAutoDataManager.CAY_HALLOWEEN).x((short) 583).y((short) 408).build());
                break;

            case MapName.LANG_FEARRI:
                zone.addTree(Tree.builder().id(EffectAutoDataManager.CAY_HALLOWEEN).x((short) 611).y((short) 312).build());
                break;
        }
    }

}
