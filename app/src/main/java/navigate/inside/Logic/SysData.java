package navigate.inside.Logic;


import android.content.Context;
import android.graphics.Bitmap;
import android.util.Pair;

import java.util.ArrayList;
import java.util.UUID;

import navigate.inside.Network.NetworkConnector;
import navigate.inside.Objects.BeaconID;
import navigate.inside.Objects.Node;
import navigate.inside.Objects.Room;
import navigate.inside.Utills.Constants;

public class SysData {
    // singleton pattern
    private static SysData instance = null;
    // all Nodes List
    private ArrayList<Node> AllNodes;
    // local SQL database object
    private DataBase db;

    private SysData(){
        AllNodes = new ArrayList<>();
    }

    public static SysData getInstance(){
        if(instance == null){
            instance = new SysData();
        }
        return instance;
    }

    public ArrayList<Node> getAllNodes(){
        return AllNodes;
    }

    /**
     * search for which node the given room belongs
     * @param room room name or number or both
     * @return node's id
     */
    public BeaconID getNodeIdByRoom(String room){
        String[] BothItems = room.split("-");
        // if given room name and number
        if (BothItems.length > 1){
            Room r = new Room(BothItems[1], BothItems[0]);
            for(Node n : AllNodes)
                for (Room m : n.getRooms()) {
                    if(r.equals(m)){
                        return n.get_id();
                    }
                }
        }else { // if only room name or number
            for(Node n : AllNodes)
                for (Room m : n.getRooms()) {
                    if(m.GetRoomNum().equals(BothItems[0]) || m.GetRoomName().equals(BothItems[0])){
                        return n.get_id();
                    }
                }
        }
        return null;
    }

    public void initDatBase(Context context){
        db = new DataBase(context);
    }

    public void closeDatabase(){
        if(db != null)
            db.close();
    }

    /**
     *
     * @param bid nod'e id
     * @return node object for given id
     */
    public Node getNodeByBeaconID(BeaconID bid) {
        for (Node node : AllNodes)
            if (bid.equals(node.get_id()))
                return node;

        return null;

    }

    /**
     *
     * @param id starting node id
     * @param id2 destination node id
     * @return image that belongs to an edge between nodes
     */
    public Bitmap getImageForPair(BeaconID id, BeaconID id2) {
        Bitmap img = db.getNodeImage(id.toString(), id2.toString());

        return img;
    }

    /**
     * loads all the data from the local database
     */
    public void InitializeData(){
        db.getNodes(AllNodes);
    }

   /* public boolean insertImageToDB(BeaconID currentBeacon,int num, int dir, Bitmap res) {
        return db.insertImage(currentBeacon, num, dir, res);
    }*/

    /**
     * inserts node into database
     * @param n
     * @return if the insertion was a success
     */
    public boolean insertNode(Node n) {
        if(db.insertNode(Node.getContentValues(n)))
           return AllNodes.add(n);
        return false;
    }

    /**
     * inserts node relation (edge) into database
     * @param string
     * @param string1
     * @param dir
     * @return if the insertion was a success
     */
    public boolean insertNeighbourToNode(String string, String string1, int dir) {
        Node n1 = getNodeByBeaconID(BeaconID.from(string));
        Node n2 = getNodeByBeaconID(BeaconID.from(string1));

        if(db.insertRelation(string, string1, dir, false)){
           // int d = (dir + 180) % 360;
            return n1.AddNeighbour(new Pair<Node, Integer>(n2, dir));
           // n2.AddNeighbour(new Pair<Node, Integer>(n1, d));
        }
        return false;
    }

    /**
     * insert the room that belongs to node
     * @param r room object
     * @param n the node object that contains the room
     */
    public void insertRoomToNode(Room r, Node n) {
        if(db.insertRoom(n.get_id().toString(), r.GetRoomName(), r.GetRoomNum())){
            n.AddRoom(r);
        }

    }

    /**
     * update image in db
     * @param id starting node id
     * @param id2 destination node id
     * @param res given image
     */
    public void updateImage(BeaconID id, BeaconID id2, Bitmap res) {
        db.updateImage(id, id2, res);
    }

    /**
     * clears the database and nodes in the system
     */
    public void clearData() {
        AllNodes.clear();
        db.clearDB();
    }

}
