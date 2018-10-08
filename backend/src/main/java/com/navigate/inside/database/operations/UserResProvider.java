package com.navigate.inside.database.operations;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class UserResProvider {
    private static final String UPDATE = "UPDATE TABLE users SET updated =? WHERE ID =?;";
    private static final String INSERT = "INSERT INTO TABLE users (ID, updated) VALUES (?,?);";
    private static final String CHECK_FOR_UPDATE = "SELECT updated FROM users WHERE ID =?;";


    public boolean checkForUpdate(String id, Connection conn) throws SQLException{
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean result =  false;
        /*try {
         ps = conn.prepareStatement(CHECK_FOR_UPDATE);
         ps.setString(1,id);
         rs = ps.executeQuery();
         if (rs.first()){
             result =  rs.getBoolean(1);
         }else {
             insertID(id,conn);
         }

        }catch (SQLException e) {
            e.printStackTrace();
        } finally {

            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }*/
        return false;
    }

    public boolean insertID(String id, Connection conn) throws SQLException{
        boolean result = false;
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(INSERT);
            ps.setString(1,id);
            ps.setBoolean(2, false);
            result = ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    public boolean updateID(String id, Connection conn) throws SQLException{
        boolean result = false;
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(UPDATE);
            ps.setBoolean(1,true);
            ps.setString(2,id);
            result = ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
}
