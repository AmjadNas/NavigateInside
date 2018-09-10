package com.navigate.inside.objects;


import com.navigate.inside.utils.Constants;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.List;




public class Node {

    private String id;
    private boolean Junction;
    private boolean Elevator;
    private String Building;
    private String Floor;
    private boolean Outside;
    private boolean NessOutside;
    private List<Edge> Neighbours;
    private List<Room> RoomsNearby;
    private int direction;
    private byte[] image;

    public Node(String id,boolean Junction, boolean Elevator,String Building,String Floor, byte[] image){
        this.id = id;
        this.Junction=Junction;
        this.Elevator=Elevator;
        this.Building=Building;
        this.Floor=Floor;
        this.image = image;
        Neighbours = new ArrayList<>();
        RoomsNearby = new ArrayList<>();
    }
    public Node(String id,boolean Junction, boolean Elevator,String Building,String Floor){
        this.id = id;
        this.Junction=Junction;
        this.Elevator=Elevator;
        this.Building=Building;
        this.Floor=Floor;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public int getDirection() {
        return direction;
    }


  /*  public ArrayList<Node> getNeighbours() {
        ArrayList<Node> node = new ArrayList<>();
        for(Pair p : Neighbours){
            node.add((Node) p);
        }
        return node;
    }
*/
    public List<Room> getRoomsNearby() {
        return RoomsNearby;
    }


    public void AddNeighbour(Edge Neighbour){
        if(!Neighbours.contains(Neighbour))
            Neighbours.add(Neighbour);
    }

    public void AddNearbyRoom(Room Room){
        RoomsNearby.add(Room);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isJunction() {
        return Junction;
    }

    public void setJunction(boolean junction) {
        Junction = junction;
    }

    public boolean isElevator() {
        return Elevator;
    }

    public void setElevator(boolean elevator) {
        Elevator = elevator;
    }

    public String getBuilding() {
        return Building;
    }

    public void setBuilding(String building) {
        Building = building;
    }

    public String getFloor() {
        return Floor;
    }

    public void setFloor(String floor) {
        Floor = floor;
    }

    public boolean isOutside() {
        return Outside;
    }

    public void setOutside(boolean outside) {
        Outside = outside;
    }

    public boolean isNessOutside() {
        return NessOutside;
    }

    public void setNessOutside(boolean nessOutside) {
        NessOutside = nessOutside;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;

        return id == node.id;

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Node{" +
                "id=" + id +
                '}';
    }

    public void setDirection(int d) {

        direction = d;
    }


    public JSONObject toJson(){
        JSONObject object = new JSONObject();
        JSONArray arr = new JSONArray(), nbers = new JSONArray();

        object.put(Constants.BEACONID,id);
        object.put(Constants.Junction,Junction);
        object.put(Constants.Elevator,Elevator);
        object.put(Constants.Building,Building);
        object.put(Constants.Floor,Floor);
        object.put(Constants.Outside,Outside);
        object.put(Constants.NessOutside,NessOutside);
        for(Room n : RoomsNearby)
            arr.add(n.toJson());

        for (Edge e : Neighbours)
            nbers.add(e.toJson());

        object.put(Constants.ROOMS, arr);
        object.put(Constants.Node, nbers);
        return object;
    }

    public static String toJson(List<Node> nodes){

        try {
            JSONObject object = new JSONObject();
            JSONArray arr = new JSONArray();

            if (nodes == null) {
                return null;
            }

            if (nodes.size() == 0) {
                return null;
            }


            for (Node it : nodes) {
                if (it != null) {
                    arr.add(it.toJson());
                }
            }

            object.put(Constants.Node, arr);
            return object.toString();

        } catch (JSONException e) {
            return "";
        }

    }

    public void setRoomsNearBy(List<Room> roomsNearBy) {
        this.RoomsNearby = roomsNearBy;
    }

    public void setNeigbours(List<Edge> neigbours) {
        this.Neighbours = neigbours;
    }

    public static class Edge {
        String id;
        int direction;

        public Edge(String id, int dir) {

            this.id = id;
            direction = dir;
        }

        JSONObject toJson() {
            JSONObject obj = new JSONObject();
            obj.put(Constants.BEACONID, id);
            obj.put(Constants.Direction, direction);

            return obj;
        }
    }
}
