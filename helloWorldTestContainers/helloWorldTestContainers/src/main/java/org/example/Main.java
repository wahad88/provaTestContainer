package org.example;
import java.sql.*;


public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");

        try{
            Class.forName("oracle.jbdc.driver.OracleDriver");
            Connection con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521/xepdb1","boostmytool","password");

            Statement stmt=con.createStatement();

            ResultSet rs=stmt.executeQuery("select * from clients");
            ResultSetMetaData rsMetaData=rs.getMetaData();

            while (rs.next()) {
                System.out.println(rsMetaData.getColumnName(1) + ": " + rs.getInt(1));
                System.out.println(rsMetaData.getColumnName(2) + ": " + rs.getString(2));
                System.out.println(rsMetaData.getColumnName(3) + ": " + rs.getString(3));
                System.out.println(rsMetaData.getColumnName(4) + ": " + rs.getString(4));
                System.out.println(rsMetaData.getColumnName(5) + ": " + rs.getString(5));
                System.out.println("------------------");
            }

            con.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}