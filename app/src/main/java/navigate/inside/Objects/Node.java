package navigate.inside.Objects;

import java.util.ArrayList;

public class Node {

    int id;
    boolean Junction;
    boolean Elevator;
    String Building;
    String Floor;
    boolean Outside;
    boolean NessOutside;
    private Node Father=null;
    ArrayList<Node> Neighbours;
    ArrayList<String> RoomsNearby;

    public Node(int id,boolean Junction,boolean Elevator,String Building,String Floor){
        this.id = id;
        this.Junction=Junction;
        this.Elevator=Elevator;
        this.Building=Building;
        this.Floor=Floor;
        Neighbours = new ArrayList<>();
        RoomsNearby = new ArrayList<>();
    }

    public void SetFather(Node Father){
        this.Father=Father;
    }

    public Node GetFather(){
        return Father;
    }

    public void SetFatherNull(){
        this.Father=null;
    }

    public void AddNeighbour(Node Neighbour){
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


}
