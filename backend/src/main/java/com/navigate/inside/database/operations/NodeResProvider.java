package com.navigate.inside.database.operations;


import com.navigate.inside.objects.Node;
import com.navigate.inside.objects.Room;
import com.navigate.inside.utils.Constants;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;


public class NodeResProvider {
    /*mysql queries*/
    private static final String INSERT_NODE = "INSERT INTO " + Constants.Node + " (" +
            Constants.BEACONID + ", " +
            Constants.Junction + ", " +
            Constants.Elevator + ", " +
            Constants.Building + ", " +
            Constants.Floor + ", " +
            Constants.Outside + ", " +
            Constants.NessOutside + ", " +
            Constants.Direction + ", " +
            Constants.Image +
            ")" +
            " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
    private static final String Get_All_Nodes = "SELECT * FROM " + Constants.Node + ";";
    private static final String GET_IMAGE = "SELECT " + Constants.PHOTO + " FROM " + Constants.Relation +
            " WHERE " + Constants.FirstID + " =? AND " + Constants.SecondID + " =?; ";
    private static final String GET_IMAGE_FOR_NODE = "SELECT " + Constants.Image + " FROM " + Constants.Node + " WHERE " + Constants.BEACONID + "=?;";
    private static final String DELETE_ITEM_BY_ID = "DELETE FROM " + Constants.Node + " WHERE " + Constants.BEACONID + "=?;";
    private static final String GET_ITEM_BY_ID = "SELECT * FROM " + Constants.Node + " WHERE " + Constants.BEACONID + "=?;";
    private static final String UPDATE_ITEM = "UPDATE " +
            Constants.Node +
            " SET " +
            Constants.Junction + "=?, " +
            Constants.Elevator + "=?, " +
            Constants.Building + "=?, " +
            Constants.Floor + "=?, " +
            Constants.Outside + "=?, " +
            Constants.NessOutside + "=?, " +
            Constants.Direction + "=?, " +
            Constants.Image + "=? " +
            " WHERE " + Constants.BEACONID + "=?;";
    private static final String UPDATE_IMAGE = "UPDATE " + Constants.Relation +
            " SET " +
            Constants.PHOTO + "=?, " +
            Constants.Direction + "=?" +
            " WHERE " + Constants.FirstID + " =? AND" + Constants.SecondID + " =?;";
    private static final String INSERT_IMAGE = "INSERT INTO " + Constants.Relation + " ("
            + Constants.FirstID + ", "
            + Constants.SecondID + ", "
            + Constants.PHOTO + ", "
            + Constants.Direction + ", "
            + Constants.DIRECT +
            ") VALUES (?,?,?,?,?);";
    private static final String GET_NEIGHBOURS = "SELECT " + Constants.SecondID + ", " + Constants.Direction + " FROM " + Constants.Relation + " WHERE " + Constants.FirstID + " =?;";

    public List<Node> getAllNodes(Connection conn) throws SQLException {

        List<Node> results = new ArrayList<>();

        ResultSet rs = null;
        PreparedStatement ps = null;

        try {
            ps = conn.prepareStatement(Get_All_Nodes);

            rs = ps.executeQuery();
            while (rs.next()) {
                boolean Junction;

                String id = (String) rs.getObject(1);
                if (rs.getInt(2) == 0) {
                    Junction = false;
                } else {
                    Junction = true;
                }
                boolean Elevator;
                if (rs.getInt(3) == 0) {
                    Elevator = false;
                } else {
                    Elevator = true;
                }
                String Building = rs.getString(4);
                String Floor = rs.getString(5);

                boolean Outside;
                boolean NessOutside;
                if (rs.getInt(6) == 0) {
                    Outside = false;
                } else {
                    Outside = true;
                }

                if (rs.getInt(7) == 0) {
                    NessOutside = false;
                } else {
                    NessOutside = true;
                }

                // get neighbours
                Node n = new Node(id, Junction, Elevator, Building, Floor);
                List<Node.Edge> list = getNeighbours(n, conn);
                if (list != null)
                    n.setNeigbours(list);
                // get rooms for node
                RoomResProvider roomResProvider = new RoomResProvider();
                List<Room> rooms = roomResProvider.getRoomsForNode(n.getId(), conn);
                if (rooms != null)
                    n.setRoomsNearBy(rooms);

                n.setOutside(Outside);
                n.setNessOutside(NessOutside);
                results.add(n);


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

        return results;
    }

    public boolean pairNodes(String first, String second, int dir, byte[] img, Connection conn) throws SQLException {
        boolean result = false;
        ResultSet rs = null;
        ResultSet rs1 = null;
        PreparedStatement ps = null;
        PreparedStatement stt = null;

        try {
            if (img == null) {
                img = getImage(first, second, conn);
            }
            stt = (PreparedStatement) conn.prepareStatement(GET_IMAGE);
            stt.setString(1, second);
            stt.setInt(2, dir);
            if (stt.execute()) {
                rs1 = stt.getResultSet();
                if (rs1.next()) {
                    // its execute update
                    ps = (PreparedStatement) conn.prepareStatement(UPDATE_IMAGE);

                    if (img != null) {
                        InputStream is = new ByteArrayInputStream(img);
                        ps.setBlob(1, is);

                    } else {

                        ps.setNull(1, Types.BLOB);
                    }
                    ps.setInt(2, dir);
                    // where
                    ps.setString(3, first);
                    ps.setString(4, second);
                    ps.execute();

                    result = true;

                } else {

                    // its execute insert
                    ps = (PreparedStatement) conn.prepareStatement(INSERT_IMAGE);

                    ps.setString(1, first);
                    ps.setString(2, second);

                    if (img != null) {
                        InputStream is = new ByteArrayInputStream(img);
                        ps.setBlob(3, is);

                    } else {
                        ps.setNull(3, Types.BLOB);
                    }
                    ps.setInt(4, dir);
                    ps.setBoolean(5, false);
                    ps.execute();

                    result = true;

                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
            if (rs1 != null) {
                try {
                    rs1.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }

            if (stt != null) {
                try {
                    stt.close();
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

    private List<Node.Edge> getNeighbours(Node n, Connection conn) throws SQLException {
        ResultSet rs = null;
        PreparedStatement ps = null;
        List<Node.Edge> list = new ArrayList<>();

        try{
            ps = conn.prepareStatement(GET_NEIGHBOURS);
            ps.setString(1, n.getId());

            rs = ps.executeQuery();
            while(rs.next()){

                String id = (String) rs.getObject(1);
                int dir = rs.getInt(2);

                list.add(new Node.Edge(id, dir));

            }
        }catch (SQLException e){
            throw e;
        } catch (Throwable e) {
            e.printStackTrace();

        }finally {
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

        return list;
    }

    public byte[] getImage(String itemId, String id2, Connection conn)
            throws SQLException {

        byte[] result = null;

        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            if (id2.equals("-1")){
                ps = conn.prepareStatement(GET_IMAGE_FOR_NODE);
                ps.setString(1, itemId);
            }else{
                ps = conn.prepareStatement(GET_IMAGE);

                ps.setString(1, itemId);
                ps.setString(2, id2);
            }


            rs = ps.executeQuery();

            while (rs.next()) {

                Blob imageBlob = rs.getBlob(1);
                if (imageBlob != null) {
                    result = imageBlob.getBytes(1, (int) imageBlob.length());
                }
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

        return result;
    }

    public boolean insertItem(Node item, Connection conn) throws SQLException{
        boolean result = false;
        ResultSet rs = null;
        ResultSet rs1 = null;
        PreparedStatement ps = null;
        PreparedStatement stt = null;

        try {

            String id = item.getId();
            boolean elevator = item.isElevator();
            boolean junction = item.isJunction();
            int direction = item.getDirection();
            boolean nesoutside = item.isNessOutside();
            boolean outside = item.isOutside();
            String building = item.getBuilding();
            String floor = item.getFloor();
            byte[] img = item.getImage();

            if (img == null) {
                img = getImage(id, "-1", conn);
            }

            stt = (PreparedStatement) conn.prepareStatement(GET_ITEM_BY_ID);
            stt.setString(1, id);

            if (stt.execute()) {
                rs1 = stt.getResultSet();
                if (rs1.next()) {
                    // its execute update
                    ps = (PreparedStatement) conn.prepareStatement(UPDATE_ITEM);

                    ps.setBoolean(1, junction);
                    ps.setBoolean(2, elevator);
                    ps.setString(3, building);
                    ps.setString(4, floor);
                    ps.setBoolean(5, outside);
                    ps.setBoolean(6, nesoutside);
                    ps.setInt(7, direction);
                    if (img != null) {
                        InputStream is = new ByteArrayInputStream(img);
                        ps.setBlob(8, is);

                    } else {

                        ps.setNull(8, Types.BLOB);
                    }

                    // where
                    ps.setString(9, id);
                    ps.execute();

                    result = true;

                } else {

                    // its execute insert
                    ps = (PreparedStatement) conn.prepareStatement(INSERT_NODE);

                    ps.setString(1, id);
                    ps.setBoolean(2, junction);
                    ps.setBoolean(3, elevator);
                    ps.setString(4, building);
                    ps.setString(5, floor);
                    ps.setBoolean(6, outside);
                    ps.setBoolean(7, nesoutside);
                    ps.setInt(8, direction);
                    if (img != null) {
                        InputStream is = new ByteArrayInputStream(img);
                        ps.setBlob(9, is);

                    } else {

                        ps.setNull(9, Types.BLOB);
                    }

                    ps.execute();

                    result = true;

                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
            if (rs1 != null) {
                try {
                    rs1.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }

            if (stt != null) {
                try {
                    stt.close();
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

    public boolean deleteItem(String item, Connection conn) throws SQLException{
        boolean result = false;
        PreparedStatement ps = null;

        try {

            if (item != null) {

                ps = (PreparedStatement) conn.prepareStatement(DELETE_ITEM_BY_ID);

                ps.setString(1, item);

                ps.execute();

                result = true;
            }

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
