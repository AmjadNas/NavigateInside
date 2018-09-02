package navigate.inside.Logic;


import android.graphics.Bitmap;
import android.util.Log;
import android.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;

import navigate.inside.Objects.BeaconID;
import navigate.inside.Objects.Node;

public class PathFinder {
    //
    private static PathFinder instance = null;
    private ArrayList< Pair<Node,Integer>> path;

    private SysData data;

    private PathFinder(){
        data = SysData.getInstance();
    }

    public static PathFinder getInstance(){
        if(instance == null){
            instance = new PathFinder();
        }
        return instance;
    }

    // BFS to find path
    public ArrayList<Pair<Node,Integer>> FindPath(BeaconID SNode,BeaconID GNode , boolean ok){
        path = new ArrayList<>();
        Node first = data.getNodeByBeaconID(SNode);
        Node FinishNode =  data.getNodeByBeaconID(GNode);

        if(first != null && FinishNode != null) {
            Pair<Node, Integer> StartNode = new Pair<>(first, 0);
            boolean check = true;
            Queue<Pair<Node, Integer>> queue;

            queue = new LinkedList<>();
            queue.add(StartNode);
            StartNode.first.setVisited(true);
            Pair<Node, Integer> Father = null;

            while (!queue.isEmpty() && check) {

                Father = queue.remove();
                if (Father.first.equals(FinishNode)) {

                    check = false;
                    break;
                }
                for (Pair<Node, Integer> p : Father.first.getNeighbours()) {
                    if (p.first.isJunction() && ok) {
                        continue;
                    }
                    if (!p.first.isVisited()) {
                        p.first.setVisited(true);
                        p.first.setFather(Father);
                        queue.add(p);
                    }
                }
            }
            if (Father != null) {
                path.add(Father);
                while (Father.first.getFather() != null) {
                    path.add(Father.first.getFather());
                    Father = Father.first.getFather();
                }
                Collections.reverse(path);
                path.remove(0);
                setFathersNull();
            }
        }
        return path;
    }

    private void setFathersNull(){
        for(Node n : data.getAllNodes()){
            n.setFather(null);
            n.setVisited(false);
        }
    }

    public ArrayList<Pair<Node,Integer>> getPath() {
        return path;
    }

    public int getIndexOfNode(BeaconID id){
        int index = 0;
        for (Pair<Node,Integer> p : path){
            if(p.first.get_id().equals(id))
                return index;
            index++;
        }
        return -1;
    }

    public Bitmap getImage(int position) {

        return data.getImageForNode(path.get(position).first.get_id());
    }
}
