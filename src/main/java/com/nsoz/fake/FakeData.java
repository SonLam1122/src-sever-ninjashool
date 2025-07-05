package com.nsoz.fake;

import com.nsoz.constants.SQLStatement;
import com.nsoz.db.jdbc.DbManager;
import com.nsoz.server.Config;
import com.nsoz.server.NinjaSchool;
import com.nsoz.server.Server;
import com.nsoz.util.Log;
import com.nsoz.util.NinjaUtils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

public class FakeData {
    public static void main(String[] args)  {

       try {
           if (Config.getInstance().load()) {
               if (!DbManager.getInstance().start()) {
                   return;
               }
               String logFileName = "logs/fake_user.txt";
               FileWriter fileWriter = new FileWriter(logFileName, true); // Tên tệp tin là "login_log_YYYY-MM-dd.txt"
               BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
               PreparedStatement stmt = DbManager.getInstance().getConnection(DbManager.GAME).prepareStatement(SQLStatement.FAKE_USER);


               for (int i = 0; i < 100; i++) {
                   String username = generateRandomUsername();
                   String otp = generateRandomOTP();
                   String phoneNumber = generateRandomPhoneNumber();
                   stmt.setString(1, username);
                   stmt.setInt(2, 123123);
                   stmt.setString(3, otp);
                   stmt.setString(4, phoneNumber);
                   stmt.setInt(5, 1);
                   stmt.executeUpdate();
                   String logMessage = String.format("stt: %d -  username: %s - OTP: %s - phone: %s - password : 123123 \n -------------------------------- \n", i + 1, username, otp, phoneNumber);
                   bufferedWriter.write(logMessage);
                   bufferedWriter.newLine();
               }
               stmt.close();
               bufferedWriter.close();
           } else {
               Log.error("Vui long kiem tra lai cau hinh!");
           }
       }catch (SQLException e){
           System.out.println(e.getMessage());
       } catch (IOException e) {
           System.out.println(e.getMessage());
       }
    }

    private static String generateRandomUsername() {
        String characters = "abcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder username = new StringBuilder();

        for (int i = 0; i < 7; i++) {
            int index = NinjaUtils.nextInt(characters.length());
            username.append(characters.charAt(index));
        }

        return username.toString();
    }

    private static String generateRandomOTP() {
        Random rand = new Random();
        int otp = 100000 + rand.nextInt(900000); // Random 6-digit OTP without leading zeros
        return String.valueOf(otp);
    }

    private static String generateRandomPhoneNumber() {
        int prefix = 300000000 + NinjaUtils.nextInt(700000000); // Random 10-digit phone number starting with 03
        return "0" + prefix;
    }
}
