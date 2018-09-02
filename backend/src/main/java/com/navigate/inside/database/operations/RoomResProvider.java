package com.navigate.inside.database.operations;

import com.navigate.inside.objects.Room;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoomResProvider {

    private static final String INSERT_ROOM_TO_NODE = "";
    private static final String GET_ROOMS_FOR_NODE = "";

    public boolean insertRoom(String nID, Room room) throws SQLException {
        return false;
    }

    public List<Room> getRoomsForNode(String nID, Connection conn) throws SQLException {
        List<Room> rooms = new ArrayList<>();
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(GET_ROOMS_FOR_NODE);

            ps.setString(1, nID);
        //TODO

        } catch (SQLException e) {
            throw e;
        } catch (Throwable e) {
            e.printStackTrace();

        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }

            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return rooms;
    }
}
