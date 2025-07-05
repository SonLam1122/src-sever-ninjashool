/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nsoz.model;

import com.nsoz.constants.ItemName;
import com.nsoz.constants.TaskName;
import com.nsoz.convert.Converter;
import com.nsoz.item.Item;
import com.nsoz.item.ItemFactory;
import com.nsoz.server.GlobalService;
import com.nsoz.store.ItemStore;
import com.nsoz.store.StoreManager;
import com.nsoz.util.NinjaUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Source được chia sẻ miễn phí tại: nsotien.com
 */
public class SelectCard extends AbsSelectCard {

    public static final long EXPIRE_3_DAY = 3 * 24 * 60 * 60 * 1000;
    public static final long EXPIRE_7_DAY = 7 * 24 * 60 * 60 * 1000;

    public static final long EXPIRE_20_DAY = 20 * 24 * 60 * 60 * 1000;
    private static final SelectCard instance = new SelectCard();

    public static SelectCard getInstance() {
        return instance;
    }

    @Override
    protected void init() {
// lật hình


        add(Card.builder().id(ItemName.TUI_VAI_CAP_3).rate(1).build());
        add(Card.builder().id(ItemName.MAT_NA_KINMOU).rate(2).expire(EXPIRE_3_DAY).build());
        add(Card.builder().id(ItemName.MAT_NA_KINMOU_NU).rate(2).expire(EXPIRE_3_DAY).build());
        add(Card.builder().id(ItemName.MAT_NA_VEGETA).rate(1.5).expire(EXPIRE_3_DAY).build());
        add(Card.builder().id(ItemName.MAT_NA_KUNOICHI).rate(1.5).expire(EXPIRE_3_DAY).build());
        add(Card.builder().id(ItemName.MAT_NA_SUPER_BROLY).rate(1).expire(EXPIRE_3_DAY).build());
        add(Card.builder().id(ItemName.MAT_NA_ONNA_BUGEISHA).rate(1).expire(EXPIRE_3_DAY).build());
        add(Card.builder().id(ItemName.HUYET_SAC_HUNG_LANG).rate(1).expire(EXPIRE_3_DAY).build());
        add(Card.builder().id(ItemName.HUYET_SAC_HUNG_LANG).rate(0.05).build());
        add(Card.builder().id(ItemName.CHIM_TINH_ANH).rate(0.5).build());
        add(Card.builder().id(ItemName.DA_CAP_5).rate(10).build());
        add(Card.builder().id(ItemName.DA_CAP_6).rate(10).build());
        add(Card.builder().id(ItemName.DA_CAP_7).rate(10).build());
        add(Card.builder().id(ItemName.DA_CAP_8).rate(10).build());
        add(Card.builder().id(ItemName.DA_CAP_9).rate(7).build());
        add(Card.builder().id(ItemName.YEN).rate(10).quantity(10000).build());
        add(Card.builder().id(ItemName.YEN).rate(10).quantity(20000).build());
        add(Card.builder().id(ItemName.YEN).rate(10).quantity(30000).build());
        add(Card.builder().id(ItemName.YEN).rate(10).quantity(50000).build());
        add(Card.builder().id(ItemName.YEN).rate(10).quantity(100000).build());
        add(Card.builder().id(ItemName.YEN).rate(10).quantity(200000).build());
        add(Card.builder().id(ItemName.GA_TAY).rate(10).build());
        add(Card.builder().id(ItemName.TOM_HUM).rate(10).build());
        add(Card.builder().id(ItemName.LANG_BAO).rate(0.1).build());
        add(Card.builder().id(ItemName.KHI_BAO).rate(0.1).build());
        add(Card.builder().id(ItemName.DUI_HEO_SUA_CAO_CAP).rate(5).build());
        add(Card.builder().id(ItemName.BAO_HIEM_SO_CAP).rate(5).build());
        add(Card.builder().id(ItemName.SASHIMI_CAO_CAP).rate(5).build());
        add(Card.builder().id(ItemName.GA_QUAY_CAO_CAP).rate(5).build());
        add(Card.builder().id(ItemName.HOAN_COT_CHI_CHU_SO_CAP).rate(10).build());
        add(Card.builder().id(ItemName.HOAN_COT_CHI_CHU_TRUNG_CAP).rate(10).build());
        add(Card.builder().id(ItemName.MANH_NON_100_NAM).rate(1).build());
        add(Card.builder().id(ItemName.MANH_NON_100_NU).rate(1).build());
        add(Card.builder().id(ItemName.MANH_AO_100_NAM).rate(1).build());
        add(Card.builder().id(ItemName.MANH_AO_100_NU).rate(1).build());
        add(Card.builder().id(ItemName.MANH_QUAN_100_NAM).rate(1).build());
        add(Card.builder().id(ItemName.MANH_QUAN_100_NU).rate(1).build());
        add(Card.builder().id(ItemName.MANH_GANG_100_NAM).rate(1).build());
        add(Card.builder().id(ItemName.MANH_GANG_100_NU).rate(1).build());
        add(Card.builder().id(ItemName.MANH_GIAY_100_NAM).rate(1).build());
        add(Card.builder().id(ItemName.MANH_GIAY_100_NU).rate(1).build());
        add(Card.builder().id(ItemName.MANH_PHU_100).rate(1).build());
        add(Card.builder().id(ItemName.MANH_BOI_100).rate(1).build());
        add(Card.builder().id(ItemName.MANH_NHAN_100).rate(1).build());
        add(Card.builder().id(ItemName.MANH_LIEN_100).rate(1).build());
        add(Card.builder().id(ItemName.HOA_THACH).rate(30).build());
        add(Card.builder().id(ItemName.PHONG_THACH).rate(20).build());
        add(Card.builder().id(ItemName.THUY_THACH).rate(20).build());
        add(Card.builder().id(ItemName.DA_PHUC_SINH).rate(10).build());
        add(Card.builder().id(ItemName.DA_VO_THUONG).rate(10).build());
        add(Card.builder().id(ItemName.DA_CHINH_PHUC).rate(10).build());
        add(Card.builder().id(ItemName.NHAM_THACH_).rate(5).build());
        add(Card.builder().id(ItemName.PHA_LE).rate(5).build());
        add(Card.builder().id(ItemName.HOA_TUYET).rate(5).build());
        add(Card.builder().id(ItemName.THE_BAI_KINH_NGHIEM_GIA_TOC_SO).rate(5).build());
        add(Card.builder().id(ItemName.THE_BAI_KINH_NGHIEM_GIA_TOC_TRUNG).rate(3).build());
        add(Card.builder().id(ItemName.THE_BAI_KINH_NGHIEM_GIA_TOC_CAO).rate(1).build());

        // svc 6x
        add(Card.builder().id(ItemName.SACH_VO_CONG_PAWARAIKOU).rate(3).build());
        add(Card.builder().id(ItemName.SACH_VO_CONG_TOTOGAI).rate(3).build());
        add(Card.builder().id(ItemName.SACH_VO_CONG_KITSUKEMAGUMA).rate(3).build());
        add(Card.builder().id(ItemName.SACH_VO_CONG_TOTAAIGO).rate(3).build());
        add(Card.builder().id(ItemName.SACH_VO_CONG_IKENNOTTO).rate(3).build());
        add(Card.builder().id(ItemName.SACH_VO_CONG_OOENJO).rate(3).build());
        add(Card.builder().id(ItemName.SACH_VO_CONG_MAAJIZANGEKI).rate(2).build());
        add(Card.builder().id(ItemName.SACH_VO_CONG_BAANINGUFUKIYA).rate(2).build());
        add(Card.builder().id(ItemName.SACH_VO_CONG_FURIIZUKATTO).rate(2).build());
        add(Card.builder().id(ItemName.SACH_VO_CONG_FUROOZUNKYUUSEN).rate(2).build());
        add(Card.builder().id(ItemName.SACH_VO_CONG_BAASUTOSUTOOMU).rate(2).build());
        add(Card.builder().id(ItemName.SACH_VO_CONG_KOUGEKITENRAI).rate(2).build());

        // todo: bật test beta
        add(Card.builder().id(ItemName.SACH_VO_CONG_KIEM_80).rate(0.02).build());
        add(Card.builder().id(ItemName.SACH_VO_CONG_TIEU_80).rate(0.02).build());
        add(Card.builder().id(ItemName.SACH_VO_CONG_KUNAI_80).rate(0.02).build());
        add(Card.builder().id(ItemName.SACH_VO_CONG_CUNG_80).rate(0.07).build());
        add(Card.builder().id(ItemName.SACH_VO_CONG_DAO_80).rate(0.07).build());
        add(Card.builder().id(ItemName.SACH_VO_CONG_QUAT_80).rate(0.07).build());
    }



    @Override
    protected Card reward(@NotNull Char p, Card card) {
        int itemID = 0;

        // nếu rơi vào đồ này chưa kích hoạt sẽ ra yên
        itemID = p.user.activated == 0 && checkItem(card.getId()) ? ItemName.YEN : card.getId();

        int quantity = card.getQuantity();
        if (itemID == ItemName.YEN) {
            p.addYen(quantity);
            p.serverMessage("Bạn nhận được " + NinjaUtils.getCurrency(quantity) + " Yên");
        } else {
            Item item = ItemFactory.getInstance().newItem(itemID);
            long expire = card.getExpire();

            // nếu rơi vào đồ vĩnh viễn thì chưa kích hoạt sẽ ra hạn
            if (expire == -1 ) {
                item.expire = (p.user.activated == 1) || item.template.isUpToUp ? -1 : System.currentTimeMillis() + (p.user.activated == 0 ? 86400000 * 3 : 0);
                if (p.user.activated == 1 && itemID == ItemName.HUYET_SAC_HUNG_LANG || itemID == ItemName.SACH_VO_CONG_KIEM_80
                        || itemID == ItemName.SACH_VO_CONG_TIEU_80 || itemID == ItemName.SACH_VO_CONG_KUNAI_80) {
                    GlobalService.getInstance().chat("server", "Chúc mừng " + p.name + " lật hình nhận được " + item.template.name + " vĩnh viễn");
                }
            } else {
                item.expire = System.currentTimeMillis() + expire;
            }


            // lật đồ ra trang bị
            if (NinjaUtils.nextInt(2000) == 0 ) {
                int itemLevel = p.level / 10 * 10;
                if (itemLevel < 10) {
                    itemLevel = 10;
                }
                if (itemLevel > 80) {
                    itemLevel = 80;
                }
                List<ItemStore> list = StoreManager.getInstance().getListEquipmentWithLevelRange(itemLevel, itemLevel + 9);
                if (!list.isEmpty()) {
                    int rd = NinjaUtils.nextInt(list.size());
                    ItemStore itemStore = list.get(rd);
                    if (itemStore != null) {
                        itemID = itemStore.getItemID();
                        item = Converter.getInstance().toItem(itemStore, Converter.MAX_OPTION);
                        card = Card.builder().id(itemID).build();
                    }
                }
            }
            p.addItemToBag(item);
        }
        return card;
    }

    @Override
    protected boolean isCanSelect(Char p) {
        int index = p.getIndexItemByIdInBag(ItemName.PHIEU_MAY_MAN);
        if (index == -1 || p.bag[index] == null || !p.bag[index].has()) {
            p.serverDialog("Bạn không có phiếu may mắn!");
            return false;
        }
        if (p.getSlotNull() == 0) {
            p.serverDialog("Không đủ chỗ trống.");
            return false;
        }
        return true;
    }

    @Override
    protected void selecctCardSuccessful(@NotNull Char p) {
        int index = p.getIndexItemByIdInBag(ItemName.PHIEU_MAY_MAN);
        p.removeItem(index, 1, true);
        if (p.taskId == TaskName.NV_THU_TAI_MAY_MAN) {
            if (p.taskMain != null && p.taskMain.index == 3) {
                p.updateTaskCount(1);
            }
        }
    }

    private boolean checkItem(int itemId){
        return itemId == ItemName.THE_BAI_KINH_NGHIEM_GIA_TOC_SO || itemId == ItemName.THE_BAI_KINH_NGHIEM_GIA_TOC_CAO || itemId == ItemName.THE_BAI_KINH_NGHIEM_GIA_TOC_TRUNG;
    }
}
