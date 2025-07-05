/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nsoz.constants;

/**
 * @author Admin
 */
public class SQLStatement {

    // điểm vxmm
    public static final String UPDATE_TOP_VXMM = "UPDATE `players` SET `topvxmm` = ? WHERE `id` = ? LIMIT 1;";
    public static final String GET_ALL_NPC = "SELECT * FROM `npc`;";
    public static final String GET_ALL_MONSTER = "SELECT * FROM `monster`;";
    public static final String GET_ALL_MAP = "SELECT * FROM `map`;";
    public static final String GET_ALL_TASK_TEMPLATE = "SELECT * FROM `task_template`;";
    public static final String LOAD_CLASS = "SELECT * FROM `clazz`;";
    public static final String LOAD_SKILL_TEMPLATE = "SELECT * FROM `skill_template` WHERE `class` = ?;";
    public static final String LOAD_SKILL = "SELECT * FROM `skill` WHERE `template_id` = ? ORDER BY `point` ASC;";
    public static final String SAVE_DATA_PLAYER = "UPDATE `players` SET `xu` = ?, `xuInBox` = ?, `yen` = ?, `point` = ?, `spoint` = ?, `saveCoordinate` = ?, `numberCellBag` = ?, `numberCellBox` = ?, `last_logout_time` = ?, `clan` = ?, `taskId` = ?, `rewardPB` = ?, `message` = ?, `data` = ?, `skill` = ?, `potential` = ?, `map` = ?, `equiped` = ?, `bag` = ?, `box` = ?, `mount` = ?, `task` = ?, `fashion` = ?, `class` = ?, `effect` = ?, `friends` = ?, `head2` = ?, `weapon` = ?, `body` = ?, `leg` = ?, `enemies` = ?, `onCSkill` = ?, `onKSkill` = ?, `onOSkill` = ?, `mask_box` = ?, `collection_box` = ?, `bijuu` = ?, `online` = ?, `quatichluy` = ?,`topvxmm` = ?,`topBoss` = ?,`thannong` = ?  WHERE `id` = ? LIMIT 1";
    public static final String GET_GIFT_CODE = "SELECT * FROM `gift_codes` WHERE `code` = ? AND (`server_id` = ? OR `server_id` = 0) AND (expires_at IS NULL OR expires_at > now()) LIMIT 1;";
    public static final String UPDATE_GIFT_CODE = "UPDATE `gift_codes` SET `status` = 1, `updated_at` = ? WHERE `id` = ? LIMIT 1;";
    public static final String INSERT_USED_GIFT_CODE = "INSERT INTO `gift_code_histories`(`player_id`,`user_id`, `gift_code`, `updated_at`) VALUES (?, ?, ?, ?)";
    public static final String CHECK_EXIST_USED_GIFT_CODE = "SELECT * FROM `gift_code_histories` WHERE `gift_code` = ? AND (`player_id` = ? OR `user_id` = ?) LIMIT 1;";
    public static final String GET_USER = "SELECT * FROM `users` WHERE `username` = ? LIMIT 1;";

    // fix đổi tên nv
    public static final String CHECK_USERNAME = "SELECT * FROM `users` WHERE `username` = ? LIMIT 1;";
    public static final String CHECK_PLAYER_NAME = "SELECT * FROM `players` WHERE `name` = ? LIMIT 1;";
    public static final String UPDATE_USERNAME = "UPDATE `users` SET `username` = ? WHERE `id` = ? LIMIT 1;";
    public static final String UPDATE_PLAYER_NAME = "UPDATE `players` SET `name` = ? WHERE `id` = ? LIMIT 1;";
    public static final String CheckClanName = "SELECT * FROM `clan` WHERE `name` = ? LIMIT 1;";
    public static final String UpdateClanName = "UPDATE `clan` SET `name` = ? WHERE `id` = ? LIMIT 1;";
    public static final String DELETE_CLAN = "DELETE FROM `clan` WHERE `id` = ? LIMIT 1;";
    public static final String UPDATE_CLAN_MAIN_PLAYER_NAME = "UPDATE `clan` SET `main_name` = ? WHERE `id` = ? LIMIT 1;";
    public static final String UPDATE_CLAN_ASS_PLAYER_NAME = "UPDATE `clan` SET `assist_name` = ? WHERE `id` = ? LIMIT 1;";
    public static final String UPDATE_CLAN_MEM_PLAYER_NAME = "UPDATE `clan_member` SET `name` = ? WHERE `id` = ? LIMIT 1;";
    public static final String UPDATE_AMOUNT_UNPAID = "UPDATE `users` SET `amount_unpaid` = ? WHERE `id` = ? LIMIT 1;";
    public static final String UPDATE_GOLD = "UPDATE `users` SET `luong` = ? WHERE `id` = ? LIMIT 1;";
    public static final String UPDATE_NINJA= "UPDATE `users` SET `ninja` = ? WHERE `id` = ? LIMIT 1;";
    public static final String UPDATE_COIN = "UPDATE `players` SET `xu` = ? WHERE `id` = ? LIMIT 1;";
    public static final String UPDATE_YEN = "UPDATE `players` SET `yen` = ? WHERE `id` = ? LIMIT 1;";
    public static final String LOAD_NOTIFY = "SELECT * FROM `options` WHERE `key` = ?   LIMIT 1;";
    public static final String ADD_GOLD = "UPDATE `users` SET `luong` = `luong` + ? WHERE `id` = ? LIMIT 1;";
    public static final String ADD_COIN = "UPDATE `players` SET `xu` = `xu` + ? WHERE `id` = ? LIMIT 1;";
    public static final String ADD_YEN = "UPDATE `players` SET `yen` = `yen` + ? WHERE `id` = ? LIMIT 1;";
    public static final String UPDATE_MESSAGE = "UPDATE `players` SET `message` = ? WHERE `id` = ? LIMIT 1;";
    public static final String INSERT_ITEM_TO_STALL = "INSERT INTO `shinwa`(`server_id`, `seller`, `item`, `price`, `status`, `time`) VALUES (?,?,?,?,?,?)";
    public static final String GET_ALL_STORE = "SELECT * FROM `stores`";
    public static final String GET_STORE_DATA = "SELECT * FROM `store_data` WHERE `store` = ?";

    public static final String UPDATE_PRODUCT = "UPDATE `shinwa` SET `status` = ?, `time` = ? WHERE `id` = ? LIMIT 1;";
    public static final String LOAD_EVENT_POINT = "SELECT `event_points`.*, `players`.`name` FROM `event_points`, `players` WHERE `event_points`.`event_id` = ? AND `event_points`.`server_id` = ? AND `players`.`id` = `event_points`.`player_id`;";
    public static final String INSERT_EVENT_POINT = "INSERT INTO `event_points`(`event_id`, `player_id`, `point`, `server_id`) VALUES (?,?,?,?)";
    public static final String UPDATE_EVENT_POINT = "UPDATE `event_points` SET `point` = ? WHERE `id` = ? LIMIT 1;";
    public static final String LOAD_PLAYER = "SELECT * FROM `players` WHERE `id` = ? LIMIT 1;";
    public static final String FAKE_USER = "INSERT INTO `users`(`username`,`password`, `otp`, `sodienthoai`,`activated` ) VALUES (?, ?, ?, ?,?)";
    public static final String NJ_EFF = "INSERT INTO `nj_eff2`(`info`) VALUES (?)";

}
