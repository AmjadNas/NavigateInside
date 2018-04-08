package navigate.inside.Objects;

import java.util.ArrayList;

import navigate.inside.Utills.Constants;

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
    private ArrayList<Node> Neighbours;
    private ArrayList<String> RoomsNearby;
    private int direction;

    public Node(int id,boolean Junction,boolean Elevator,String Building,String Floor){
        this.id = id;
        this.Junction=Junction;
        this.Elevator=Elevator;
        this.Building=Building;
        this.Floor=Floor;
        Neighbours = new ArrayList<>();
        RoomsNearby = new ArrayList<>();
    }

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
        return Neighbours;
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

    public void AddNeighbour(Node Neighbour){
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

    public static String getDirection(int mAzimuth, int direction){
        int diff = mAzimuth - direction;
        String dir = null;
        if(diff < 44 && diff > (-44) ){
            dir = "Go forward " + diff;
        }else if( diff < (359) && diff > (315) ){
            dir = "Go forward " + diff;
        }else if( diff < (-45) && diff > (-135) ){
            dir = "Go Right " + diff;
        }else if( diff < (315) && diff > 225 ){
            dir = "Go Right " + diff;
        }else if( diff < (-135) && diff > (-225) ){
            dir = "Turn around " + diff;
        }else if( diff < (225) && diff > (135) ){
            dir = "Turn around " + diff;
        }else if( diff < (135) && diff > (45) ){
            dir = "Go Left " + diff;
        }else if( diff < (-225) && diff > (-315) ){
            dir = "Go Left " + diff;
        }
        return dir;
    }
}
