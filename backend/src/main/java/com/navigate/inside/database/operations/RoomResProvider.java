package com.navigate.inside.database.operations;

import com.navigate.inside.objects.Room;
import com.navigate.inside.utils.Constants;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoomResProvider {

    private static final String INSERT_ROOM_TO_NODE = "INSERT INTO " + Constants.Room + " (" +
            Constants.BEACONID + ", " + Constants.NUMBER + ", " + Constants.NAME + ") VALUES (?, ?, ?);";
    private static final String GET_ROOMS_FOR_NODE = "SELECT " + Constants.NAME + ", " + Constants.NUMBER + " FROM" + Constants.Room + " WHERE " + Constants.BEACONID + " =?;";

    public boolean insertRoom(String nID, Room room, Connection conn) throws SQLException {
        boolean result = false;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try{
            ps = conn.prepareStatement(INSERT_ROOM_TO_NODE);
            ps.setString(1, nID);
            ps.setString(2, room.GetRoomNum());
            ps.setString(3, room.GetRoomName());

            ps.execute();

            result = true;
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
        return result;
    }

    public List<Room> getRoomsForNode(String nID, Connection conn) throws SQLException {
        List<Room> rooms = new ArrayList<>();
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            String name, number;
            Room r;
            ps = conn.prepareStatement(GET_ROOMS_FOR_NODE);

            ps.setString(1, nID);
            rs = ps.executeQuery();

            while (rs.next()){
                name = rs.getNString(1);
                number = rs.getNString(2);
                rooms.add(new Room(number, name));

            }

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
