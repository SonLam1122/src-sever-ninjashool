package com.nsoz.event;

import com.nsoz.constants.*;
import com.nsoz.effect.EffectAutoDataManager;
import com.nsoz.event.eventpoint.EventPoint;
import com.nsoz.item.Item;
import com.nsoz.item.ItemFactory;
import com.nsoz.item.ItemManager;
import com.nsoz.lib.RandomCollection;
import com.nsoz.map.Map;
import com.nsoz.map.Tree;
import com.nsoz.map.zones.Zone;
import com.nsoz.model.Char;
import com.nsoz.model.InputDialog;
import com.nsoz.model.Menu;
import com.nsoz.model.RandomItem;
import com.nsoz.npc.NpcFactory;
import com.nsoz.store.ItemStore;
import com.nsoz.store.StoreManager;
import com.nsoz.util.NinjaUtils;

import java.util.List;

public class TrungThuNew extends Event {
    public static final int HOA_PHUC_SINH = 9;
    public static final String TOP_LONG_DEN = "release_lanterns";
    public static final String TOP_BANH_TRUNG_THU = "use_moon_cake";
    public static final long EXPIRE_7_DAY = 604800000L;
    public static final long EXPIRE_30_DAY = 2592000000L;
    private static final int DOI_BACH_HO = 0;
    private static final int VU_KHI_THOI_TRANG_7_NGAY = 1;
    private static final int VU_KHI_THOI_TRANG_30_NGAY = 2;
    private static final int QUA_DAC_BIET = 9;
    private static final int BANH_THAP_CAM = 3;
    private static final int BANH_DEO = 4;
    private static final int BANH_DAU_XANH = 5;
    private static final int BANH_PIA = 6;
    private static final int HOP_BANH_THUONG = 7;
    private static final int HOP_BANH_THUONG_HANG = 8;

    public TrungThuNew() {
        setId(Event.TRUNG_THU);
        keyEventPoint.add(EventPoint.DIEM_TIEU_XAI);
        keyEventPoint.add(TOP_BANH_TRUNG_THU);
        keyEventPoint.add(TOP_LONG_DEN);
        endTime.set(2023, 12, 12, 23, 59, 59);
        itemsThrownFromMonsters.add(1, ItemName.TRUNG);
        itemsThrownFromMonsters.add(2, ItemName.BOT_MI);
        itemsThrownFromMonsters.add(1, ItemName.HAT_SEN);
        itemsThrownFromMonsters.add(1, ItemName.DUONG);
        itemsThrownFromMonsters.add(1, ItemName.DAU_XANH);
        itemsThrownFromMonsters.add(1, ItemName.MUT);
    }

    @Override
    public void initStore() {
        StoreManager.getInstance().addItem((byte) StoreManager.TYPE_MISCELLANEOUS, ItemStore.builder()
                .id(998)
                .itemID(ItemName.GIAY_GOI_THUONG)
                .coin(30000)
                .expire(ConstTime.FOREVER)
                .build());
        StoreManager.getInstance().addItem((byte) StoreManager.TYPE_MISCELLANEOUS, ItemStore.builder()
                .id(999)
                .itemID(ItemName.GIAY_GOI_CAO_CAP)
                .gold(25)
                .expire(ConstTime.FOREVER)
                .build());
        StoreManager.getInstance().addItem((byte) StoreManager.TYPE_MISCELLANEOUS, ItemStore.builder()
//                .id(665)
                .itemID(ItemName.GIAY_THONG_HANH)
                .gold(20)
                .expire(ConstTime.FOREVER)
                .build());
        StoreManager.getInstance().addItem((byte) StoreManager.TYPE_MISCELLANEOUS, ItemStore.builder()
//                .id(665)
                .itemID(ItemName.LONG_DEN)
                .coin(120000)
                .expire(ConstTime.FOREVER)
                .build());
    }

    @Override
    public void useItem(Char _char, Item item) {
        if (item.id == ItemName.HOP_BANH_THUONG || item.id == ItemName.HOP_BANH_THUONG_HANG) {
            if (_char.getSlotNull() == 0) {
                _char.warningBagFull();
                return;
            }
            RandomCollection<Integer> rc = item.id == ItemName.HOP_BANH_THUONG ? itemsRecFromCoinItem : itemsRecFromGold2Item;
            boolean isDone = useEventItem(_char, item.id, rc);
        } else if (item.id == ItemName.LONG_DEN) {
            if (_char.getSlotNull() == 0) {
                _char.warningBagFull();
                return;
            }
            boolean isDone = useEventItem(_char, item.id, itemsRecFromGold2Item);
            if (isDone) {
                _char.getEventPoint().addPoint(TrungThuNew.TOP_LONG_DEN, 1);
                _char.getEventPoint().addPoint(EventPoint.DIEM_TIEU_XAI, 1);
            }
            _char.zone.getService().addEffectAuto((byte) 7, (short) _char.x, _char.y, (byte) 0, (short) 1);
        }
    }

    @Override
    public void action(Char p, int type, int amount) {
        switch (type) {
            case BANH_THAP_CAM:
                banhThapCam(p, amount);
                break;
            case BANH_DAU_XANH:
                banhDauXanh(p, amount);
                break;
            case BANH_DEO:
                banhDeo(p, amount);
                break;
            case BANH_PIA:
                banhPia(p, amount);
                break;
            case HOP_BANH_THUONG:
                hopBanhThuong(p, amount);
                break;
            case HOP_BANH_THUONG_HANG:
                hopBanhThuongHang(p, amount);
                break;
            case HOA_PHUC_SINH:
                hoaPhucSinh(p, amount);
                break;
            case DOI_BACH_HO:
                doiBachHo(p);
                break;
            case VU_KHI_THOI_TRANG_7_NGAY:
                doiVuKhiThoiTrang(p, ItemName.BANH_TRUNG_THU_PHONG_LOI, 10, EXPIRE_7_DAY);
                break;
            case VU_KHI_THOI_TRANG_30_NGAY:
                doiVuKhiThoiTrang(p, ItemName.BANH_TRUNG_THU_BANG_HOA, 20, EXPIRE_30_DAY);
                break;
        }
    }

    public void doiBachHo(Char p) {
        int amount = 10;
        List<Item> list = p.getListItemByID(ItemName.BANH_TRUNG_THU_PHONG_LOI);
        if (list.size() < amount) {
            p.getService().npcChat(NpcName.TIEN_NU, "Không đủ Bánh trung thu phong lôi");
            return;
        }

        for (Item item : list.subList(0, amount)) {
            p.removeItem(item.index, 1, true);
        }
        Item item = ItemFactory.getInstance().newItem(ItemName.BACH_HO);
        item.setQuantity(1);
        item.isLock = false;
        item.expire = System.currentTimeMillis() + EXPIRE_30_DAY;
        p.addItemToBag(item);
    }

    public void doiVuKhiThoiTrang(Char p, int itemID, int amount, long expire) {
        List<Item> list = p.getListItemByID(itemID);
        if (list.size() < amount) {
            p.getService().npcChat(NpcName.TIEN_NU, "Không đủ " + ItemManager.getInstance().getItemName(itemID));
            return;
        }

        for (Item item : list.subList(0, amount)) {
            p.removeItem(item.index, 1, true);
        }

        if (p.gender == 1) {
            itemID = ItemName.GAY_MAT_TRANG;
        } else {
            itemID = ItemName.GAY_TRAI_TIM;
        }
        Item item = ItemFactory.getInstance().newItem(itemID);
        item.setQuantity(1);
        item.isLock = false;
        if (expire == -1) {
            item.expire = -1;
        } else {
            item.expire = System.currentTimeMillis() + expire;
        }
        p.addItemToBag(item);
    }

    public void banhThapCam(Char p, int amount) {
        int[][] itemRequires = new int[][]{{ItemName.BOT_MI, 10}, {ItemName.TRUNG, 5}, {ItemName.DUONG, 5}, {ItemName.DAU_XANH, 5}};
        int itemIdReceive = ItemName.BANH_THAP_CAM;
        makeEventItem(p, amount, itemRequires, 0, 0, 15000, itemIdReceive);
    }

    public void banhDeo(Char p, int amount) {
        int[][] itemRequires = new int[][]{{ItemName.BOT_MI, 10}, {ItemName.TRUNG, 5}, {ItemName.DUONG, 5}, {ItemName.DAU_XANH, 5}};
        int itemIdReceive = ItemName.BANH_DEO;
        makeEventItem(p, amount, itemRequires, 0, 0, 15000, itemIdReceive);
    }

    public void banhDauXanh(Char p, int amount) {
        int[][] itemRequires = new int[][]{{ItemName.BOT_MI, 10}, {ItemName.TRUNG, 5}, {ItemName.DUONG, 5}, {ItemName.DAU_XANH, 5}};
        int itemIdReceive = ItemName.BANH_DAU_XANH;
        makeEventItem(p, amount, itemRequires, 0, 0, 15000, itemIdReceive);
    }

    public void banhPia(Char p, int amount) {
        int[][] itemRequires = new int[][]{{ItemName.BOT_MI, 10}, {ItemName.TRUNG, 5}, {ItemName.DUONG, 5}, {ItemName.DAU_XANH, 5}};
        int itemIdReceive = ItemName.BANH_PIA;
        makeEventItem(p, amount, itemRequires, 0, 0, 15000, itemIdReceive);
    }

    public void hopBanhThuong(Char p, int amount) {
        int[][] itemRequires = new int[][]{{ItemName.GIAY_GOI_THUONG, 1}, {ItemName.BANH_THAP_CAM, 1}, {ItemName.BANH_DEO, 1}, {ItemName.BANH_DAU_XANH, 1}, {ItemName.BANH_PIA, 1}};
        int itemIdReceive = ItemName.HOP_BANH_THUONG;
        boolean checkt = makeEventItem(p, amount, itemRequires, 0, 0, 0, itemIdReceive);
        if (checkt) {
//            p.getEventPoint().addPoint(TrungThuNew.TOP_BANH_TRUNG_THU, amount);
//            p.getEventPoint().addPoint(EventPoint.DIEM_TIEU_XAI, amount);
        }
    }

    public void hopBanhThuongHang(Char p, int amount) {
        int[][] itemRequires = new int[][]{{ItemName.GIAY_GOI_CAO_CAP, 1}, {ItemName.BANH_THAP_CAM, 1}, {ItemName.BANH_DEO, 1}, {ItemName.BANH_DAU_XANH, 1}, {ItemName.BANH_PIA, 1}};
        int itemIdReceive = ItemName.HOP_BANH_THUONG_HANG;
        boolean check = makeEventItem(p, amount, itemRequires, 0, 0, 0, itemIdReceive);
        if (check) {
            p.getEventPoint().addPoint(TrungThuNew.TOP_BANH_TRUNG_THU, amount);
            p.getEventPoint().addPoint(EventPoint.DIEM_TIEU_XAI, amount);
        }
    }

    public void doiHoaPhucSinh(Char p, int type) {
        int point = type == 1 ? 10000 : 10000;
        if (p.getEventPoint().getPoint(EventPoint.DIEM_TIEU_XAI) < point) {
            p.getService().npcChat(NpcName.TIEN_NU,
                    "Bạn cần tối thiểu " + NinjaUtils.getCurrency(point) + " điểm sự kiện mới có thể đổi được vật này.");
            return;
        }

        if (p.getSlotNull() == 0) {
            p.getService().npcChat(NpcName.TIEN_NU, p.language.getString("BAG_FULL"));
            return;
        }

        Item item = ItemFactory.getInstance().newItem(type == 1 ? ItemName.HOA_THIEN_DIEU : ItemName.HOA_DA_YEN);
        p.addItemToBag(item);
        p.getEventPoint().subPoint(EventPoint.DIEM_TIEU_XAI, point);
    }

    public void hoaPhucSinh(Char _char, int itemId) {
        if (_char.getSlotNull() == 0) {
            _char.warningBagFull();
            return;
        }

        int itemIndex = _char.getIndexItemByIdInBag(itemId);

        if (itemIndex != -1) {
            RandomCollection<Integer> rc = RandomItem.LINH_VAT;
            useVipEventItem(_char, itemId == ItemName.HOA_THIEN_DIEU ? 1 : 2, rc);
            _char.removeItem(itemIndex, 1, true);
        } else {
            _char.getService().npcChat((short) NpcName.KIRIKO, "Hãy tìm đúng loài hoa rồi đến gặp ta");
        }
    }

    public void escortFinish(Char p) {
        RandomCollection<Integer> rc = itemsRecFromGold2Item;
        p.addExp(15000000);
        int itemId = rc.next();
        Item itm = ItemFactory.getInstance().newItem(itemId);
        itm.initExpire();
        if (itm.id == ItemName.THONG_LINH_THAO) {
            itm.setQuantity(NinjaUtils.nextInt(5, 10));
        }
        p.addItemToBag(itm);
    }

    @Override
    public void menu(Char p) {
        p.menus.clear();
        p.menus.add(new Menu(CMDMenu.EXECUTE, "Làm bánh", () -> {
            p.menus.clear();
            p.menus.add(new Menu(CMDMenu.EXECUTE, "Bánh Thập Cẩm", () -> {
                p.setInput(new InputDialog(CMDInputDialog.EXECUTE, "Bánh Thập Cẩm", () -> {
                    InputDialog input = p.getInput();
                    try {
                        int number = input.intValue();
                        action(p, BANH_THAP_CAM, number);
                    } catch (Exception e) {
                        if (!input.isEmpty()) {
                            p.inputInvalid();
                        }
                    }
                }));
                p.getService().showInputDialog();
            }));
            p.menus.add(new Menu(CMDMenu.EXECUTE, "Bánh Dẻo", () -> {
                p.setInput(new InputDialog(CMDInputDialog.EXECUTE, "Bánh Dẻo", () -> {
                    InputDialog input = p.getInput();
                    try {
                        int number = input.intValue();
                        action(p, BANH_DEO, number);
                    } catch (Exception e) {
                        if (!input.isEmpty()) {
                            p.inputInvalid();
                        }
                    }
                }));
                p.getService().showInputDialog();
            }));
            p.menus.add(new Menu(CMDMenu.EXECUTE, "Bánh Đậu xanh", () -> {
                p.setInput(new InputDialog(CMDInputDialog.EXECUTE, "Bánh Đậu xanh", () -> {
                    InputDialog input = p.getInput();
                    try {
                        int number = input.intValue();
                        action(p, BANH_DAU_XANH, number);
                    } catch (Exception e) {
                        if (!input.isEmpty()) {
                            p.inputInvalid();
                        }
                    }
                }));
                p.getService().showInputDialog();

            }));
            p.menus.add(new Menu(CMDMenu.EXECUTE, "Bánh Pía", () -> {
                p.setInput(new InputDialog(CMDInputDialog.EXECUTE, "Bánh Pía", () -> {
                    InputDialog input = p.getInput();
                    try {
                        int number = input.intValue();
                        action(p, BANH_PIA, number);
                    } catch (Exception e) {
                        if (!input.isEmpty()) {
                            p.inputInvalid();
                        }
                    }
                }));
                p.getService().showInputDialog();
            }));
            p.getService().openUIMenu();
        }));
        p.menus.add(new Menu(CMDMenu.EXECUTE, "Làm hộp bánh", () -> {
            p.menus.clear();
            p.menus.add(new Menu(CMDMenu.EXECUTE, "Hộp bánh thường", () -> {
                p.setInput(new InputDialog(CMDInputDialog.EXECUTE, "Hộp bánh thường", () -> {
                    InputDialog input = p.getInput();
                    try {
                        int number = input.intValue();
                        action(p, HOP_BANH_THUONG, number);
                    } catch (Exception e) {
                        if (!input.isEmpty()) {
                            p.inputInvalid();
                        }
                    }
                }));
                p.getService().showInputDialog();
            }));
            p.menus.add(new Menu(CMDMenu.EXECUTE, "Hộp bánh thượng hạng", () -> {
                p.setInput(new InputDialog(CMDInputDialog.EXECUTE, "Hộp bánh thượng hạng", () -> {
                    InputDialog input = p.getInput();
                    try {
                        int number = input.intValue();
                        action(p, HOP_BANH_THUONG_HANG, number);
                    } catch (Exception e) {
                        if (!input.isEmpty()) {
                            p.inputInvalid();
                        }
                    }
                }));
                p.getService().showInputDialog();
            }));
            p.getService().openUIMenu();
        }));
        p.menus.add(new Menu(CMDMenu.EXECUTE, "Đổi quà", () -> {
            p.menus.clear();
            p.menus.add(new Menu(CMDMenu.EXECUTE, "Bạch hổ 30 ngày", () -> {
                action(p, DOI_BACH_HO, 1);
            }));
            p.menus.add(new Menu(CMDMenu.EXECUTE, "Vũ khí thời trang 7 ngày", () -> {
                action(p, VU_KHI_THOI_TRANG_7_NGAY, 1);
            }));
            p.menus.add(new Menu(CMDMenu.EXECUTE, "Vũ khí thời trang 30 ngày", () -> {
                action(p, VU_KHI_THOI_TRANG_30_NGAY, 1);
            }));
            p.getService().openUIMenu();
        }));
        p.menus.add(new Menu(CMDMenu.EXECUTE, "Đổi lồng đèn", () -> {
            p.menus.clear();
            p.menus.add(new Menu(CMDMenu.EXECUTE, "10.000.000 xu", () -> {
                p.setCommandBox(Char.DOI_LONG_DEN_XU);
                List<Item> list = p.getListItemByID(ItemName.LONG_DEN_TRON, ItemName.LONG_DEN_CA_CHEP, ItemName.LONG_DEN_MAT_TRANG, ItemName.LONG_DEN_NGOI_SAO);
                p.getService().openUIShopTrungThu(list, "Đổi lồng đèn 10 triệu xu", "Đổi");
            }));
            p.menus.add(new Menu(CMDMenu.EXECUTE, "25000 lượng", () -> {
                p.setCommandBox(Char.DOI_LONG_DEN_LUONG);

                List<Item> list = p.getListItemByID(ItemName.LONG_DEN_TRON, ItemName.LONG_DEN_CA_CHEP, ItemName.LONG_DEN_MAT_TRANG, ItemName.LONG_DEN_NGOI_SAO);
                p.getService().openUIShopTrungThu(list, "Đổi lồng đèn 25000 lượng", "Đổi");
            }));
            p.getService().openUIMenu();
        }));

        p.menus.add(new Menu(CMDMenu.EXECUTE, "Hoa phục sinh", () -> {
            p.menus.clear();
            p.menus.add(new Menu(CMDMenu.EXECUTE, "Hoa thiên diệu", () -> {
                doiHoaPhucSinh(p, 1);
            }));
            p.menus.add(new Menu(CMDMenu.EXECUTE, "Hoa dạ yến", () -> {
                doiHoaPhucSinh(p, 2);
            }));
            p.menus.add(new Menu(CMDMenu.EXECUTE, "Điểm sự kiện", () -> {
                p.getService().showAlert("Hướng dẫn", "- Điểm sự kiện: " + NinjaUtils.getCurrency(p.getEventPoint().getPoint(EventPoint.DIEM_TIEU_XAI))
                        + "\n\nBạn có thể quy đổi điểm sự kiện như sau\n- Hoa thiên diệu: 10.000 điểm\n- Hoa dạ yến: 10.000 điểm\n");
            }));
            p.getService().openUIMenu();
        }));

        p.menus.add(new Menu(CMDMenu.EXECUTE, "Đua Top", () -> {
            p.menus.clear();
            p.menus.add(new Menu(CMDMenu.EXECUTE, "Thả Lồng Đèn", () -> {
                p.menus.clear();
                p.menus.add(new Menu(CMDMenu.EXECUTE, "Bảng xếp hạng", () -> {
                    viewTop(p, TOP_LONG_DEN, "Thả Lồng Đèn", "%d. %s đã thả %s lồng đèn");
                }));
                p.menus.add(new Menu(CMDMenu.EXECUTE, "Phần thưởng", () -> {
                    StringBuilder sb = new StringBuilder();
                    sb.append("TOP 1:").append("\n");
                    sb.append("- 1 thẻ đổi tên\n");
                    sb.append("- Thú Cưỡi v.v MCS (tự chọn: Bạch Hổ, Phượng Hoàng Băng, Hỏa Kỳ Lân)\n");
                    sb.append("- Mặt nạ thỏ v.v \n");
                    sb.append("- Nhật tủ lam phong/Thiên nguyệt chi nữ v.v MCS\n");
                    sb.append("- Lồng Đèn Thời Trang v.v Ramdom\n");
                    sb.append("- 1 Cúp Lưu Niệm\n");
                    sb.append(" ").append("\n");

                    sb.append("TOP 2:").append("\n");
                    sb.append("- Thú Cưỡi v.v MCS (tự chọn: Bạch Hổ, Phượng Hoàng Băng, Hỏa Kỳ Lân)\n");
                    sb.append("- Lồng Đèn Thời Trang v.v Ramdom\n");
                    sb.append("- 1 Cúp Lưu Niệm\n");
                    sb.append(" ").append("\n");

                    sb.append("TOP 3:").append("\n");
                    sb.append("- Thú Cưỡi v.v Ramdom (tự chọn: Bạch Hổ, Phượng Hoàng Băng, Hỏa Kỳ Lân)\n");
                    sb.append("- Lồng Đèn Thời Trang v.v Ramdom\n");
                    sb.append("- 1 Cúp Lưu Niệm\n");
                    sb.append(" ").append("\n");

                    sb.append("TOP 4 - 5:").append("\n");
                    sb.append("- Lồng Đèn Thời Trang v.v Ramdom\n");
                    sb.append("- Nhật tủ lam phong/Thiên nguyệt chi nữ 1 tháng\n");
                    sb.append(" ").append("\n");

                    sb.append("Top 6 - 10:").append("\n");
                    sb.append("- Lồng Đèn Thời Trang 2 tháng Ramdom\n");
                    sb.append("- Nhật tủ lam phong/Thiên nguyệt chi nữ 1 tháng\n");
                    p.getService().showAlert("Phần thưởng", sb.toString());
                }));
                if (isEnded()) {
                    int ranking = getRanking(p, TOP_LONG_DEN);
                    if (ranking <= 10 && p.getEventPoint().getRewarded(TOP_LONG_DEN) == 0) {
                        p.menus.add(new Menu(CMDMenu.EXECUTE, String.format("Nhận Thưởng TOP %d", ranking), () -> {
//                            receiveReward(p, TOP_LONG_DEN);
                        }));
                    }
                }
                p.getService().openUIMenu();
            }));
            p.menus.add(new Menu(CMDMenu.EXECUTE, "Bánh Trung Thu", () -> {
                p.menus.clear();
                p.menus.add(new Menu(CMDMenu.EXECUTE, "Bảng xếp hạng", () -> {
                    viewTop(p, TOP_BANH_TRUNG_THU, "Bánh Trung Thu", "%d. %s đã làm %s hộp bánh");
                }));
                p.menus.add(new Menu(CMDMenu.EXECUTE, "Phần thưởng", () -> {
                    StringBuilder sb = new StringBuilder();
                    sb.append("TOP 1:").append("\n");
                    sb.append("- 1 thẻ đổi tên\n");
                    sb.append("- Lồng Đèn Thời Trang v.v Ramdom\n");
                    sb.append("- Mặt nạ oni v.v\n");
                    sb.append("- 500.000 lượng\n");
                    sb.append("- Cúp Lưu Niệm\n");
                    sb.append(" ").append("\n");

                    sb.append("TOP 2:").append("\n");
                    sb.append("- Lồng Đèn Thời Trang v.v Ramdom\n");
                    sb.append("- Mặt nạ oni v.v\n");
                    sb.append("- 300.000 lượng\n");
                    sb.append("- Cúp Lưu Niệm\n");
                    sb.append(" ").append("\n");

                    sb.append("TOP 3:").append("\n");
                    sb.append("- Lồng Đèn Thời Trang v.v Ramdom\n");
                    sb.append("- 300.000 lượng\n");
                    sb.append("- Cúp Lưu Niệm\n");
                    sb.append(" ").append("\n");

                    sb.append("TOP 4 - 5:").append("\n");
                    sb.append("- Lồng Đèn Thời Trang 2 tháng Ramdom\n");
                    sb.append("- 100.000 lượng\n");
                    sb.append(" ").append("\n");

                    sb.append("TOP 6 - 10:").append("\n");
                    sb.append("- Lồng Đèn Thời Trang 1 tháng Ramdom\n");

                    p.getService().showAlert("Phần thưởng", sb.toString());
                }));
                if (isEnded()) {
                    int ranking = getRanking(p, TOP_BANH_TRUNG_THU);
                    if (ranking <= 10 && p.getEventPoint().getRewarded(TOP_BANH_TRUNG_THU) == 0) {
                        p.menus.add(new Menu(CMDMenu.EXECUTE, String.format("Nhận Thưởng TOP %d", ranking), () -> {
//                            receiveReward(p, TOP_BANH_TRUNG_THU);
                        }));
                    }
                }
                p.getService().openUIMenu();
            }));
            p.getService().openUIMenu();
        }));

        p.menus.add(new Menu(CMDMenu.EXECUTE, "Hướng dẫn", () -> {
            StringBuilder sb = new StringBuilder();
            sb.append("* Làm bánh trung thu").append("\n");
            sb.append(" ").append("\n");
            sb.append("- Trong quá trình diễn ra sự kiện các ninja có level từ 30 trở lên đánh quái +- 7 level sẽ có tỉ lệ nhận được các nguyên liệu sau:").append("\n");
            sb.append("+ Bột mì, trứng, hạt sen, đường, đậu xanh, mứt").append("\n");
            sb.append("- Dùng Thiên nhãn phù hay Khai nhãn phù có thể tăng tỉ lệ rơi nguyên liệu").append("\n");
            sb.append("- Khi đã có đủ nguyên liệu các bạn có thể đến các làng gặp NPC Tiên Nữ để làm ra những chiếc bánh trung thu thơm ngon với công thức như sau:").append("\n");
            sb.append("+ Bánh Thập Cẩm = 10 Bột + 5 Trứng + 5 Hạt sen + 5 Đường + 5 Mứt.").append("\n");
            sb.append("+ Bánh Dẻo = 10 Bột + 5 Hạt sen + 5 Đường + 5 Mứt.").append("\n");
            sb.append("+ Bánh Đậu xanh = 10 Bột + 5 Trứng + 5 Đường + 5 Đậu xanh.").append("\n");
            sb.append("+ Bánh Pía = 10 Bột + 5 Trứng + 5 Đường + 5 Đậu xanh.").append("\n");
            sb.append("- Bánh trung thu khóa").append("\n");
            sb.append("- Tôi sẽ thu mỗi bạn một ít Yên cho tiền công làm bánh.").append("\n");
            sb.append("+ Hộp bánh thường = 4 loại bánh + 1 giấy gói thường.").append("\n");
            sb.append("+ Hộp bánh thượng hạng = 4 loại bánh + 1 giấy gói cao cấp. Có thể giao dịch. Khi làm sẽ tăng 1 điểm TOP sự kiện").append("\n");
            sb.append("giấy gói thường và giấy gói cao cấp bán ở NPC Goosho . Có thể giao dịch").append("\n");

            sb.append(" ").append("\n");
            sb.append("--------------").append("\n");
            sb.append(" ").append("\n");
            sb.append("* Thả lồng đèn").append("\n");
            sb.append(" ").append("\n");
            sb.append("- Lồng đèn được bán tại NPC Goosho").append("\n");
            sb.append("- Khi thả lồng đèn sẽ nhận được vp ngâu nhiên. Có thể giao dịch. Khi sử dụng sẽ tăng 1 điểm TOP sự kiện").append("\n");


            sb.append(" ").append("\n");
            sb.append("--------------").append("\n");
            sb.append(" ").append("\n");
            sb.append("* Rước đèn trung thu").append("\n");
            sb.append(" ").append("\n");
            sb.append("- Tại trường và các map sẽ xuất hiện lồng đèn.  ").append("\n");
            sb.append("- Các ninja hay dùng Giấy thông hành mua tại NPC Goosho để rước đèn về gặp Hiệu trường và nhận thưởng").append("\n");
            p.getService().showAlert("Hướng dẫn", sb.toString());
        }));
    }

    @Override
    public void initMap(Zone zone) {
        boolean isTrungThu = NinjaUtils.isTrungThu();
        Map map = zone.map;
        int mapID = map.id;
        switch (mapID) {
            case MapName.KHU_LUYEN_TAP:
                break;
            case MapName.TRUONG_OOKAZA:
                zone.addTree(Tree.builder().id(EffectAutoDataManager.CAY_TRUNG_THU).x((short) 1426).y((short) 552).build());
                zone.addTree(Tree.builder().id(EffectAutoDataManager.CAY_TRUNG_THU_2).x((short) 784).y((short) 648).build());
                if (isTrungThu) {
                    zone.addTree(Tree.builder().id(EffectAutoDataManager.THA_LONG_DEN).x((short) 1426).y((short) 552).build());
                    zone.addTree(Tree.builder().id(EffectAutoDataManager.THA_LONG_DEN).x((short) 784).y((short) 648).build());
                }
                break;
            case MapName.TRUONG_HARUNA:
                zone.addTree(Tree.builder().id(EffectAutoDataManager.CAY_TRUNG_THU).x((short) 502).y((short) 408).build());
                zone.addTree(Tree.builder().id(EffectAutoDataManager.CAY_TRUNG_THU).x((short) 1863).y((short) 360).build());
                zone.addTree(Tree.builder().id(EffectAutoDataManager.CAY_TRUNG_THU).x((short) 2048).y((short) 360).build());
                if (isTrungThu) {
                    zone.addTree(Tree.builder().id(EffectAutoDataManager.THA_LONG_DEN).x((short) 502).y((short) 408).build());
                    zone.addTree(Tree.builder().id(EffectAutoDataManager.THA_LONG_DEN).x((short) 1863).y((short) 360).build());
                    zone.addTree(Tree.builder().id(EffectAutoDataManager.THA_LONG_DEN).x((short) 2048).y((short) 360).build());
                }
                break;
            case MapName.TRUONG_HIROSAKI:
                zone.addNpc(NpcFactory.getInstance().newNpc(99, NpcName.LONG_DEN_2, 1307, 168, 0));
                zone.addTree(Tree.builder().id(EffectAutoDataManager.CAY_TRUNG_THU).x((short) 1207).y((short) 168).build());
                if (isTrungThu) {
                    zone.addTree(Tree.builder().id(EffectAutoDataManager.THA_LONG_DEN).x((short) 1207).y((short) 168).build());
                }
                break;
        }
    }
}
