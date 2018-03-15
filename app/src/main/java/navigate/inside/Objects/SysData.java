package navigate.inside.Objects;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Queue;

public class SysData {
    //Its should contain Nodes List
    private static SysData instance = null;
    private ArrayList<Node> AllNodes;


    private SysData(){
        AllNodes = new ArrayList<>();
        InitializeData();
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

    public Node getNodeById(String ID){
        for(Node n : AllNodes){
            if(Integer.parseInt(ID) == n.getId()){
                return n;
            }
        }
        return null;
    }

    private void InitializeData(){
        //Main Bulding , Floor 600
        Node n6001 = new Node(6001,false,false,"Main","600");
        Node n6002 = new Node(6002,true,false,"Main","600");
        Node n6003 = new Node(6003,false,false,"Main","600");
        Node n6004 = new Node(6004,false,false,"Main","600");
        Node n6005 = new Node(6005,false,false,"Main","600");
        Node n6006 = new Node(6006,false,true,"Main","600");
        Node n6007 = new Node(6007,false,false,"Main","600");
        Node n6008 = new Node(6008,true,false,"Main","600");
        Node n6009 = new Node(6009,true,false,"Main","600");
        Node n6010 = new Node(6010,false,false,"Main","600");
        Node n6011 = new Node(6011,false,false,"Main","600");
        Node n6012 = new Node(6012,false,false,"Main","600");
        Node n6013 = new Node(6013,false,false,"Main","600");
        Node n6014 = new Node(6014,false,false,"Main","600");
        Node n6015 = new Node(6015,false,false,"Main","600");

        AllNodes.add(n6001);
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
        AllNodes.add(n6013);
        AllNodes.add(n6014);
        AllNodes.add(n6015);


        //Rabin Bulding , Floor 7000
        Node n70001 = new Node(70001,false,false,"Rabin","7000");
        Node n70002 = new Node(70002,false,false,"Rabin","7000");
        Node n70003 = new Node(70003,false,false,"Rabin","7000");
        Node n70004 = new Node(70004,false,false,"Rabin","7000");
        Node n70005 = new Node(70005,false,false,"Rabin","7000");
        Node n70006 = new Node(70006,false,false,"Rabin","7000");
        Node n70007 = new Node(70007,true,false,"Rabin","7000");
        Node n70008 = new Node(70008,false,true,"Rabin","7000");
        Node n70009 = new Node(70009,true,false,"Rabin","7000");
        Node n70010 = new Node(70010,false,false,"Rabin","7000");
        Node n70011 = new Node(70011,false,false,"Rabin","7000");
        Node n70012 = new Node(70012,false,false,"Rabin","7000");
        Node n70013 = new Node(70013,false,true,"Rabin","7000");
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
        Node n60001 = new Node(60001,false,false,"Rabin","6000");
        Node n60002 = new Node(60002,false,false,"Rabin","6000");
        Node n60003 = new Node(60003,false,false,"Rabin","6000");
        Node n60004 = new Node(60004,true,false,"Rabin","6000");
        Node n60005 = new Node(60005,false,true,"Rabin","6000");
        Node n60006 = new Node(60006,false,false,"Rabin","6000");
        Node n60007 = new Node(60007,true,false,"Rabin","6000");
        Node n60008 = new Node(60008,false,false,"Rabin","6000");
        Node n60009 = new Node(60009,false,false,"Rabin","6000");
        Node n60010 = new Node(60010,false,false,"Rabin","6000");
        Node n60011 = new Node(60011,false,true,"Rabin","6000");

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
        n6001.AddNeighbour(n6004);
        n6001.AddNeighbour(n6005);

        n6003.AddNeighbour(n6002);
        n6003.AddNeighbour(n6004);

        n6004.AddNeighbour(n6001);
        n6004.AddNeighbour(n6003);
        n6004.AddNeighbour(n6005);

        n6005.AddNeighbour(n6004);
        n6005.AddNeighbour(n6006);
        n6005.AddNeighbour(n6007);

        n6006.AddNeighbour(n6005);
        n6006.AddNeighbour(n6007);

        n6007.AddNeighbour(n6005);
        n6007.AddNeighbour(n6006);
        n6007.AddNeighbour(n6008);

        n6008.AddNeighbour(n6007);
        n6008.AddNeighbour(n6009);
        n6008.AddNeighbour(n6013);

        n6009.AddNeighbour(n6008);
        n6009.AddNeighbour(n6010);
        n6009.AddNeighbour(n6011);
        n6009.AddNeighbour(n6013);

        n6010.AddNeighbour(n6009);
        n6010.AddNeighbour(n6011);
        n6010.AddNeighbour(n6012);

        n6011.AddNeighbour(n6009);
        n6011.AddNeighbour(n6010);
        n6011.AddNeighbour(n6014);

        n6012.AddNeighbour(n6010);
        n6012.AddNeighbour(n6013);
        n6012.AddNeighbour(n6014);

        n6013.AddNeighbour(n6008);
        n6013.AddNeighbour(n6009);
        n6013.AddNeighbour(n6012);
        n6013.AddNeighbour(n6014);
        n6013.AddNeighbour(n6015);

        //Adding Neighbours to Points in Rabin building , floor 7000
        n70001.AddNeighbour(n70002);
        n70001.AddNeighbour(n70003);
        n70001.AddNeighbour(n70007);
        n70001.AddNeighbour(n70008);
        n70001.AddNeighbour(n70009);
        n70001.AddNeighbour(n70010);
        n70001.AddNeighbour(n70013);

        n70002.AddNeighbour(n70001);
        n70002.AddNeighbour(n70004);
        n70002.AddNeighbour(n70006);
        n70002.AddNeighbour(n70003);
        n70002.AddNeighbour(n70007);
        n70002.AddNeighbour(n70008);
        n70002.AddNeighbour(n70009);
        n70002.AddNeighbour(n70010);
        n70002.AddNeighbour(n70013);

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
        n60011.AddNeighbour(n60008);

        //Connecting Junctions and ELevators
        //Junctions :
        n70007.setJunction(true);
        n60004.setJunction(true);
        n70009.setJunction(true);
        n60007.setJunction(true);
        n70008.setElevator(true);
        n60005.setElevator(true);

        n70007.AddNeighbour(n60004);
        n60004.AddNeighbour(n70007);

        n70009.AddNeighbour(n60007);
        n60007.AddNeighbour(n70009);

        n70008.AddNeighbour(n60005);
        n60005.AddNeighbour(n70008);

    }



}
