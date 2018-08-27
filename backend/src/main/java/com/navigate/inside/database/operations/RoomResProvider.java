package com.navigate.inside.database.operations;

import com.navigate.inside.objects.Room;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoomResProvider {

    private static final String INSERT_ROOM_TO_NODE = "";
    private static final String GET_ROOMS_FOR_NODE = "";

    public boolean insertRoom(String nID, Room room) throws SQLException{
        return false;
    }

    public List<Room> getRoomsForNode(String nID){
        List<Room> rooms = new ArrayList<>();


        return rooms;
    }
}
