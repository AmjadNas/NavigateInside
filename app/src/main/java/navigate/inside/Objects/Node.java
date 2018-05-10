package navigate.inside.Objects;

import android.graphics.Bitmap;
import android.media.Image;
import android.util.Pair;

import java.util.ArrayList;

import navigate.inside.R;
import navigate.inside.Utills.Constants;

public class Node {

    private int id;
    private BeaconID _id; // new id to be used
    private boolean Junction;
    private boolean Elevator;
    private String Building;
    private String Floor;
    private boolean Outside;
    private boolean NessOutside;
    private boolean Visited=false;
    private Pair<Node,Integer> Father=null;
    private ArrayList<Pair<Node,Integer>> Neighbours;
    private int direction;
    private String roomsRange;   // must be entered in format x:y (from room x to y)
    private Bitmap image = null;

    public Node(int id,boolean Junction,boolean Elevator,String Building,String Floor){
        this.id = id;
        this.Junction = Junction;
        this.Elevator = Elevator;
        this.Building = Building;
        this.Floor = Floor;
        Neighbours = new ArrayList<>();
    }

    public Node(BeaconID _id,boolean Junction,boolean Elevator,String Building,String Floor){
        this._id = _id;
        this.Junction = Junction;
        this.Elevator = Elevator;
        this.Building = Building;
        this.Floor = Floor;
        Neighbours = new ArrayList<>();
    }

    public BeaconID get_id() {
        return _id;
    }

    public void setRoomsRange(String roomsRange) {
        this.roomsRange = roomsRange;
    }

    public String getRoomsRange() {
        return roomsRange;
    }

    public Bitmap getImage(){
        return image;
    }

    public void setImage(Bitmap image){
        this.image=image;
    }
    public int getDirection() {
        return direction;
    }

    public void setFather(Pair<Node,Integer> Father){
        this.Father=Father;
    }

    public Pair<Node,Integer> getFather() {
        return Father;
    }

    public ArrayList<Pair<Node,Integer>> getNeighbours() {

        return Neighbours;
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
    public int hashCode() {
        return _id.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;

        return _id.equals(node._id);

    }

    @Override
    public String toString() {
        return "Node{" +
                "id=" + _id.getMajor() +
                '}';
    }

    public void setDirection(int d) {

        direction = d;
    }


}
