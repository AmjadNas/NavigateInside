package navigate.inside.Logic;


import java.util.ArrayList;

import navigate.inside.Objects.Node;
import navigate.inside.Objects.SysData;

public class PathFinder {
    //
    public static PathFinder instance = null;
    private SysData data;
    private PathFinder(){
        //Constructor
        data = SysData.getInstance();
    }

    public PathFinder getInstance(){
        if(instance == null){
            instance = new PathFinder();
        }
        return instance;
    }

    public ArrayList<Node> FindPath(String SNode,String GNode , boolean ok){
    return null;
    }







}
