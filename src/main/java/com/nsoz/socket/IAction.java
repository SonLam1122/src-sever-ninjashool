package com.nsoz.socket;

import org.json.JSONObject;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author HIEU HIV
 */
interface IAction {

    abstract void call(JSONObject json);

}
