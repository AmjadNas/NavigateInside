package navigate.inside.Logic;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;

import navigate.inside.Objects.Node;
import navigate.inside.Objects.SysData;

public class PathFinder {
    //
    private static PathFinder instance = null;
    private Queue<Node> queue;

    private SysData data;
    private PathFinder(){
        //Constructor
        data = SysData.getInstance();
    }

    public static PathFinder getInstance(){
        if(instance == null){
            instance = new PathFinder();
        }
        return instance;
    }

    public ArrayList<Node> FindPath(String SNode,String GNode , boolean ok){
        ArrayList<Node> path = new ArrayList<>();
        Node StartNode = data.getNodeById(SNode);
        Node FinishNode = data.getNodeById(GNode);
        boolean check = true;
        queue = new LinkedList<>();
        queue.add(StartNode);
        StartNode.setVisited(true);

        while(!queue.isEmpty() && check){

            Node Father = queue.remove();
            if(Father.getId() == FinishNode.getId()){
                check=false;
                break;
            }
            for(Node n:Father.getNeighbours()){
                if(n.isJunction() && ok){
                    continue;
                }
                if(!n.isVisited()){
                    n.setVisited(true);
                    n.setFather(Father);
                    queue.add(n);
                }
            }

        }
        path.add(FinishNode);
        while(FinishNode.getFather()!=null){
            path.add(FinishNode.getFather());
            FinishNode = FinishNode.GetFather();
        }
        Collections.reverse(path);

        setFathersNull();

         return path;
    }

    private void setFathersNull(){
        for(Node n : data.getAllNodes()){
            n.setFather(null);
            n.setVisited(false);
        }
    }






}
