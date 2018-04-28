package com.navigate.inside.objects;

import com.letgo.objects.Item;
import com.navigate.inside.utils.Constants;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javafx.util.Pair;

/**
 * Created by StiPro on 4/23/2018.
 */

public class Node {

    private int id;
    private boolean Junction;
    private boolean Elevator;
    private String Building;
    private String Floor;
    private boolean Outside;
    private boolean NessOutside;
    private boolean Visited=false;
    private Node Father=null;
    private ArrayList<Pair<Node,Integer>> Neighbours;
    private ArrayList<String> RoomsNearby;
    private int direction;
//    private Bitmap image = null;

    public Node(int id,boolean Junction,boolean Elevator,String Building,String Floor){
        this.id = id;
        this.Junction=Junction;
        this.Elevator=Elevator;
        this.Building=Building;
        this.Floor=Floor;
        Neighbours = new ArrayList<>();
        RoomsNearby = new ArrayList<>();
    }

    /*public Bitmap getImage(){
        return image;
    }*/

   /*public void setImage(Bitmap image){
        this.image=image;
    }*/
    public int getDirection() {
        return direction;
    }

    public void setFather(Node Father){
        this.Father=Father;
    }

    public Node getFather() {
        return Father;
    }

    public ArrayList<Node> getNeighbours() {
        ArrayList<Node> node = new ArrayList<>();
        for(Pair p : Neighbours){
            node.add((Node) p.getKey());
        }
        return node;
    }

    public ArrayList<String> getRoomsNearby() {
        return RoomsNearby;
    }

    public boolean isVisited() {
        return Visited;
    }

    public void setVisited(boolean visited) {
        Visited = visited;
    }

    public void SetFatherNull(){
        this.Father=null;
    }

    public void AddNeighbour(Pair<Node,Integer> Neighbour){
        if(!Neighbours.contains(Neighbour))
            Neighbours.add(Neighbour);
    }

    public void AddNearbyRoom(String Room){
        RoomsNearby.add(Room);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
        return id;
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
        JSONObject object = null;
        object.put(Constants.ID,id);
        object.put(Constants.Junction,Junction);
        object.put(Constants.Elevator,Elevator);
        object.put(Constants.Building,Building);
        object.put(Constants.Floor,Floor);
        object.put(Constants.Outside,Outside);
        object.put(Constants.NessOutside,NessOutside);

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



}
