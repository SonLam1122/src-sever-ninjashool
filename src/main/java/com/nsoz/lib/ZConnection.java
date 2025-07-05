/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nsoz.lib;

import com.nsoz.db.jdbc.DbManager;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Source được chia sẻ miễn phí tại: nsotien.com
 */
public class ZConnection {

    private Connection connection;
    private int timeOut;

    public ZConnection(int timeOut) {
        this.timeOut = timeOut;
    }

    public Connection getConnection() {
        try {
            if (connection != null) {
                if (!connection.isValid(timeOut)) {
                    connection.close();
                }
            }
            if (connection == null || connection.isClosed()) {
                connection = DbManager.getInstance().getConnection();
                return getConnection();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return connection;
    }
}
