package com.navigate.inside.database.operations;


import com.navigate.inside.objects.Node;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by StiPro on 4/23/2018.
 */


public class NodeResProvider {

    private static final String Get_All_Nodes = "Select * FROM Node;";

    public List<Node> getAllNodes(Connection conn) throws SQLException {

        List<Node> results = new ArrayList<>();

        ResultSet rs = null;
        PreparedStatement ps = null;

        ps = conn.prepareStatement(Get_All_Nodes);

        rs = ps.executeQuery();

        while(rs.next()){
            boolean Junction;

            Integer id = rs.getInt(1);
            if(rs.getInt(2) == 0){
                 Junction = false;
            }else{
                Junction=true;
            }
            boolean Elevator;
            if(rs.getInt(3) == 0){
                Elevator = false;
            }else{
                Elevator=true;
            }
            String Building = rs.getString(4);
            String Floor =  rs.getString(5);

            boolean Outside;
            boolean NessOutside;
            if(rs.getInt(6) == 0){
                Outside =false;
            }else{
                Outside = true;
            }

            if(rs.getInt(7) == 0){
                NessOutside = false;
            }else{
                NessOutside = true;
            }



           Node n = new Node(id,Junction,Elevator,Building,Floor);
            n.setOutside(Outside);
            n.setNessOutside(NessOutside);
            results.add(n);


        }





        return results;
    }


}
