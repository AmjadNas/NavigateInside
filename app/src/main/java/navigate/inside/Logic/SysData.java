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
    //Its should contain Nodes List
    private static SysData instance = null;
    private ArrayList<Node> AllNodes;
    private DataBase db;

    private SysData(){
        AllNodes = new ArrayList<>();
        //InitializeData();
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

    public BeaconID getNodeIdByRoom(String room){
        for(Node n : AllNodes)
            for (Room m : n.getRooms()) {
                if(room.equals(m.GetRoomName()) || room.equals(m.GetRoomNum())){
                    return n.get_id();
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

    public Node getNodeByBeaconID(BeaconID bid) {
        for (Node node : AllNodes)
            if (bid.equals(node.get_id()))
                return node;

        return null;

    }

    public Bitmap getImageForNode(BeaconID id) {
        Bitmap img = db.getNodeImage(id.toString());

        return img;
    }

    public void InitializeData(){
        //Main Bulding , Floor 600
        db.getNodes(AllNodes);
       /* Node n6001 = new Node(new BeaconID(UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"),6001,6001),false,false,"Main","600");
        Node n6002 = new Node(new BeaconID(UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"),6002,6002),true,false,"Main","600");
        Node n6003 = new Node(new BeaconID(UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"),6003,6003),false,false,"Main","600");
        Node n6004 = new Node(new BeaconID(UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"),6004,6004),false,false,"Main","600");
        Node n6005 = new Node(new BeaconID(UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"),6005,6005)  ,false,false,"Main","600");
        Node n6006 = new Node(new BeaconID(UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"),6006,6006) ,false,true,"Main","600");
        Node n6007 = new Node(new BeaconID(UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"),6007,6007) ,false,false,"Main","600");
        Node n6008 = new Node(new BeaconID(UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"),6008,6008) ,true,false,"Main","600");
        Node n6009 = new Node(new BeaconID(UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"),6009,6009) ,true,false,"Main","600");
        Node n6010 = new Node(new BeaconID(UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"),6010,6010) ,false,false,"Main","600");
        Node n6011 = new Node(new BeaconID(UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"),6011,6011) ,false,false,"Main","600");
        Node n6012 = new Node(new BeaconID(UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"),6012,6012) ,false,false,"Main","600");
        Node n6013 = new Node(new BeaconID(UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"),6013,6013) ,false,false,"Main","600");
        Node n6014 = new Node(new BeaconID(UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"),6014,6014) ,false,false,"Main","600");
        Node n6015 = new Node(new BeaconID(UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"),6015,6015) ,false,false,"Main","600");

        AllNodes.add(n6001);
        n6001.getRooms().add(new Room("6001","6001"));
        AllNodes.add(n6002);
        AllNodes.add(n6003);
        AllNodes.add(n6004);
        AllNodes.add(n6005);
        AllNodes.add(n6006);
        AllNodes.add(n6007);
        AllNodes.add(n6008);
        AllNodes.add(n6009);
        AllNodes.add(n6010);
        AllNodes.add(n6011);
        AllNodes.add(n6012);
        n6012.getRooms().add(new Room("6012","6012"));
        AllNodes.add(n6013);
        AllNodes.add(n6014);
        AllNodes.add(n6015);


        //Rabin Bulding , Floor 7000
        Node n70001 = new Node(new BeaconID(UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"),70001,70001) ,false,false,"Rabin","7000");
        Node n70002 = new Node(new BeaconID(UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"),70002,70002) ,false,false,"Rabin","7000");
        Node n70003 = new Node(new BeaconID(UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"),70003,70003) ,false,false,"Rabin","7000");
        Node n70004 = new Node(new BeaconID(UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"),70004,70004) ,false,false,"Rabin","7000");
        Node n70005 = new Node(new BeaconID(UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"),70005,70005) ,false,false,"Rabin","7000");
        Node n70006 = new Node(new BeaconID(UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"),70006,70006) ,false,false,"Rabin","7000");
        Node n70007 = new Node(new BeaconID(UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"),70007,70007) ,true,false,"Rabin","7000");
        Node n70008 = new Node(new BeaconID(UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"),70008,70008) ,false,true,"Rabin","7000");
        Node n70009 = new Node(new BeaconID(UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"),70009,70009) ,true,false,"Rabin","7000");
        Node n70010 = new Node(new BeaconID(UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"),70010,70010) ,false,false,"Rabin","7000");
        Node n70011 = new Node(new BeaconID(UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"),70011,70011) ,false,false,"Rabin","7000");
        Node n70012 = new Node(new BeaconID(UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"),70012,70012) ,false,false,"Rabin","7000");
        Node n70013 = new Node(new BeaconID(UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"),70013,70013) ,false,true,"Rabin","7000");
        AllNodes.add(n70001);
        AllNodes.add(n70002);
        AllNodes.add(n70003);
        AllNodes.add(n70004);
        AllNodes.add(n70005);
        AllNodes.add(n70006);
        AllNodes.add(n70007);
        AllNodes.add(n70008);
        AllNodes.add(n70009);
        AllNodes.add(n70010);
        AllNodes.add(n70011);
        AllNodes.add(n70012);
        AllNodes.add(n70013);


        //Rabin Building , Floor 6000
        Node n60001 = new Node(new BeaconID(UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"),60001,60001) ,false,false,"Rabin","6000");
        Node n60002 = new Node(new BeaconID(UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"),60002,60002) ,false,false,"Rabin","6000");
        Node n60003 = new Node(new BeaconID(UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"),60003,60003) ,false,false,"Rabin","6000");
        Node n60004 = new Node(new BeaconID(UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"),60004,60004) ,true,false,"Rabin","6000");
        Node n60005 = new Node(new BeaconID(UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"),60005,60005) ,false,true,"Rabin","6000");

        Node n60006 = new Node(new BeaconID(UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"),60006,60006) ,false,false,"Rabin","6000");
        Node n60007 = new Node(new BeaconID(UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"),60007,60007) ,true,false,"Rabin","6000");
        Node n60008 = new Node(new BeaconID(UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"),60008,60008) ,false,false,"Rabin","6000");
        Node n60009 = new Node(new BeaconID(UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"),60009,60009) ,false,false,"Rabin","6000");
        Node n60010 = new Node(new BeaconID(UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"),60010,60010) ,false,false,"Rabin","6000");
        Node n60011 = new Node(new BeaconID(UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"),60011,60011) ,false,true,"Rabin","6000");

        AllNodes.add(n60001);
        AllNodes.add(n60002);
        AllNodes.add(n60003);
        AllNodes.add(n60004);
        AllNodes.add(n60005);
        AllNodes.add(n60006);
        AllNodes.add(n60007);
        AllNodes.add(n60008);
        AllNodes.add(n60009);
        AllNodes.add(n60010);
        AllNodes.add(n60011);

        //Adding Neighbours to Points in main building
        n6001.AddNeighbour(new Pair<>(n6004,180));
        n6001.AddNeighbour(new Pair<>(n6005,180));

        n6003.AddNeighbour(new Pair<>(n6002,0));
        n6003.AddNeighbour(new Pair<>(n6004,0));

        n6004.AddNeighbour(new Pair<>(n6001,0));
        n6004.AddNeighbour(new Pair<>(n6003,0));
        n6004.AddNeighbour(new Pair<>(n6005,180));

        n6005.AddNeighbour(new Pair<>(n6004,0));
        n6005.AddNeighbour(new Pair<>(n6006,0));
        n6005.AddNeighbour(new Pair<>(n6007,0));

        n6006.AddNeighbour(new Pair<>(n6005,0));
        n6006.AddNeighbour(new Pair<>(n6007,0));

        n6007.AddNeighbour(new Pair<>(n6005,0));
        n6007.AddNeighbour(new Pair<>(n6006,0));
        n6007.AddNeighbour(new Pair<>(n6008,0));

        n6008.AddNeighbour(new Pair<>(n6007,0));
        n6008.AddNeighbour(new Pair<>(n6009,0));
        n6008.AddNeighbour(new Pair<>(n6013,0));

        n6009.AddNeighbour(new Pair<>(n6008,0));
        n6009.AddNeighbour(new Pair<>(n6010,0));
        n6009.AddNeighbour(new Pair<>(n6011,0));
        n6009.AddNeighbour(new Pair<>(n6013,0));

        n6010.AddNeighbour(new Pair<>(n6009,0));
        n6010.AddNeighbour(new Pair<>(n6011,0));
        n6010.AddNeighbour(new Pair<>(n6012,0));

        n6011.AddNeighbour(new Pair<>(n6009,0));
        n6011.AddNeighbour(new Pair<>(n6010,0));
        n6011.AddNeighbour(new Pair<>(n6014,0));

        n6012.AddNeighbour(new Pair<>(n6010,0));
        n6012.AddNeighbour(new Pair<>(n6013,0));
        n6012.AddNeighbour(new Pair<>(n6014,0));

        n6013.AddNeighbour(new Pair<>(n6008,0));
        n6013.AddNeighbour(new Pair<>(n6009,0));
        n6013.AddNeighbour(new Pair<>(n6012,0));
        n6013.AddNeighbour(new Pair<>(n6014,0));
        n6013.AddNeighbour(new Pair<>(n6015,0));*/
/*
        //Adding Neighbours to Points in Rabin building , floor 7000
        n70001.AddNeighbour(new Pair<>(n70002,0));
        n70001.AddNeighbour(new Pair<>(n70003,0));
        n70001.AddNeighbour(new Pair<>(n70007,0));
        n70001.AddNeighbour(new Pair<>(n70008,0));
        n70001.AddNeighbour(new Pair<>(n70009,0));
        n70001.AddNeighbour(new Pair<>(n70010,0));

        n70002.AddNeighbour(new Pair<>(n70001,0));
        n70002.AddNeighbour(new Pair<>(n70004,0));
        n70002.AddNeighbour(new Pair<>(n70006,0));
        n70002.AddNeighbour(new Pair<>(n70003,0));
        n70002.AddNeighbour(new Pair<>(n70007,0));
        n70002.AddNeighbour(new Pair<>(n70008,0));
        n70002.AddNeighbour(new Pair<>(n70009,0));
        n70002.AddNeighbour(new Pair<>(n70010,0));
        n70002.AddNeighbour(new Pair<>(n70013,0));
        n70001.AddNeighbour(new Pair<>(n70013,0));*/
/*
        n70003.AddNeighbour(n70001);
        n70003.AddNeighbour(n70002);
        n70003.AddNeighbour(n70007);
        n70003.AddNeighbour(n70008);
        n70003.AddNeighbour(n70009);
        n70003.AddNeighbour(n70010);
        n70003.AddNeighbour(n70013);

        n70004.AddNeighbour(n70002);
        n70004.AddNeighbour(n70006);
        n70004.AddNeighbour(n70005);

        n70005.AddNeighbour(n70004);

        n70006.AddNeighbour(n70004);
        n70006.AddNeighbour(n70002);

        n70007.AddNeighbour(n70001);
        n70007.AddNeighbour(n70002);
        n70007.AddNeighbour(n70003);
        n70007.AddNeighbour(n70008);
        n70007.AddNeighbour(n70009);
        n70007.AddNeighbour(n70010);
        n70007.AddNeighbour(n70013);

        n70008.AddNeighbour(n70001);
        n70008.AddNeighbour(n70002);
        n70008.AddNeighbour(n70003);
        n70008.AddNeighbour(n70007);
        n70008.AddNeighbour(n70009);
        n70008.AddNeighbour(n70010);
        n70008.AddNeighbour(n70013);

        n70009.AddNeighbour(n70001);
        n70009.AddNeighbour(n70002);
        n70009.AddNeighbour(n70003);
        n70009.AddNeighbour(n70007);
        n70009.AddNeighbour(n70008);
        n70009.AddNeighbour(n70010);
        n70009.AddNeighbour(n70013);

        n70010.AddNeighbour(n70001);
        n70010.AddNeighbour(n70002);
        n70010.AddNeighbour(n70003);
        n70010.AddNeighbour(n70007);
        n70010.AddNeighbour(n70008);
        n70010.AddNeighbour(n70009);
        n70010.AddNeighbour(n70011);
        n70010.AddNeighbour(n70012);
        n70010.AddNeighbour(n70013);

        n70011.AddNeighbour(n70010);
        n70011.AddNeighbour(n70012);

        n70012.AddNeighbour(n70010);
        n70012.AddNeighbour(n70011);

        n70013.AddNeighbour(n70001);
        n70013.AddNeighbour(n70002);
        n70013.AddNeighbour(n70003);
        n70013.AddNeighbour(n70007);
        n70013.AddNeighbour(n70008);
        n70013.AddNeighbour(n70009);
        n70013.AddNeighbour(n70010);

        //Adding Neighbours to Points in Rabin building , floor 6000
        n60001.AddNeighbour(n60002);
        n60001.AddNeighbour(n60003);
        n60001.AddNeighbour(n60004);
        n60001.AddNeighbour(n60005);
        n60001.AddNeighbour(n60006);
        n60001.AddNeighbour(n60007);
        n60001.AddNeighbour(n60008);
        n60001.AddNeighbour(n60011);

        n60002.AddNeighbour(n60001);
        n60002.AddNeighbour(n60003);

        n60003.AddNeighbour(n60001);
        n60003.AddNeighbour(n60002);

        n60004.AddNeighbour(n60001);
        n60004.AddNeighbour(n60005);
        n60004.AddNeighbour(n60006);
        n60004.AddNeighbour(n60007);
        n60004.AddNeighbour(n60008);
        n60004.AddNeighbour(n60011);

        n60005.AddNeighbour(n60001);
        n60005.AddNeighbour(n60004);
        n60005.AddNeighbour(n60006);
        n60005.AddNeighbour(n60007);
        n60005.AddNeighbour(n60008);
        n60005.AddNeighbour(n60011);

        n60006.AddNeighbour(n60001);
        n60006.AddNeighbour(n60004);
        n60006.AddNeighbour(n60005);
        n60006.AddNeighbour(n60007);
        n60006.AddNeighbour(n60008);
        n60006.AddNeighbour(n60011);

        n60007.AddNeighbour(n60001);
        n60007.AddNeighbour(n60004);
        n60007.AddNeighbour(n60005);
        n60007.AddNeighbour(n60006);
        n60007.AddNeighbour(n60008);
        n60007.AddNeighbour(n60011);

        n60008.AddNeighbour(n60001);
        n60008.AddNeighbour(n60004);
        n60008.AddNeighbour(n60005);
        n60008.AddNeighbour(n60006);
        n60008.AddNeighbour(n60007);
        n60008.AddNeighbour(n60009);
        n60008.AddNeighbour(n60010);
        n60008.AddNeighbour(n60011);

        n60009.AddNeighbour(n60008);
        n60009.AddNeighbour(n60010);

        n60010.AddNeighbour(n60008);
        n60010.AddNeighbour(n60009);

        n60011.AddNeighbour(n60001);
        n60011.AddNeighbour(n60004);
        n60011.AddNeighbour(n60005);
        n60011.AddNeighbour(n60006);
        n60011.AddNeighbour(n60007);
        n60011.AddNeighbour(n60008);*/

        //Connecting Junctions and ELevators
        //Junctions :
        /*n70007.setJunction(true);
        n60004.setJunction(true);
        n70009.setJunction(true);
        n60007.setJunction(true);
        n70008.setElevator(true);
        n60005.setElevator(true);*/

      /*  n70007.AddNeighbour(n60004);
        n60004.AddNeighbour(n70007);

        n70009.AddNeighbour(n60007);
        n60007.AddNeighbour(n70009);

        n70008.AddNeighbour(n60005);
        n60005.AddNeighbour(n70008);*/

    }
    public void saveNode(BeaconID bid,  String floar, String building, boolean junction, boolean Elevator, boolean outside, Bitmap img,int dir) {
        Node node = new Node(bid,junction, Elevator, building, floar);
        node.setOutside(outside);
        node.setDirection(dir);
        db.insertNode(Node.getContentValues(node));
        db.insertImage(bid, img);

        AllNodes.add(node);

    }


    public void linkNodes(String s1, String s2, int direction, boolean isdirect) {

        db.insertRelation(s1,s2, direction, isdirect);
    }

    public void insertRoomToNode(String bid, String num, String nm) {

        Node node = getNodeByBeaconID(BeaconID.from(bid));
        node.AddRoom(new Room(num, nm));
        db.insertRoom(bid, nm, num);
    }
}
