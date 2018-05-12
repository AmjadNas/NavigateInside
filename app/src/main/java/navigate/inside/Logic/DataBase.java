package navigate.inside.Logic;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.util.Pair;

import com.estimote.mgmtsdk.feature.settings.api.Eddystone;

import java.util.ArrayList;
import java.util.UUID;

import navigate.inside.Objects.BeaconID;
import navigate.inside.Objects.Node;
import navigate.inside.R;
import navigate.inside.Utills.Constants;
import navigate.inside.Utills.Converter;


public class DataBase extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = String.valueOf(R.string.app_name);

    private static final String SQL_CREATE_NODE_TABLE = "CREATE TABLE "+ Constants.Node + " ("+
            Constants.BEACONID + " VARCHAR(100) PRIMARY KEY, "+   // beaconid with format UID:Major:Minor for
            Constants.Junction + " BOOLEAN, " +                   // example fadsfasdf-afda-dasffdfd:1555:54654
            Constants.Elevator + " BOOLEAN, "+
            Constants.Building + " VARCHAR(25), "+
            Constants.Range + " TEXT, "+
            Constants.Floor + " VARCHAR(25), " +
            Constants.Outside + " BOOLEAN, "+
            Constants.NessOutside + " BOOLEAN, "+
            Constants.Direction + " INTEGER, " +
            Constants.Image +" BLOB " +
            ")";

    private static final String SQL_CREATE_RELATION_TABLE = "CREATE TABLE " + Constants.Relation +" ("+
            Constants.FirstID + " VARCHAR(100), "+
            Constants.SecondID + " VARCHAR(100), "+
            Constants.Direction + "INTEGER, "+
            Constants.DIRECT + "BOOLEAN, "+
            "FOREIGN KEY (" + Constants.FirstID + ") REFERENCES " + Constants.Node + " (" + Constants.BEACONID + "), "+
            "FOREIGN KEY (" + Constants.SecondID + ") REFERENCES " + Constants.Node +" (" + Constants.BEACONID + "), " +
            "CONSTRAINT PK1 PRIMARY KEY (" + Constants.FirstID + "," + Constants.SecondID + ")" +
            " )";


    public DataBase(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_NODE_TABLE);
        db.execSQL(SQL_CREATE_RELATION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void getNodes(ArrayList<Node> nodes){
        SQLiteDatabase db = getReadableDatabase();

        String[] projection = {
          Constants.BEACONID,
                Constants.Junction,
                Constants.Elevator,
                Constants.Building,
                Constants.Floor,
                Constants.Outside,
                Constants.NessOutside,
                Constants.Direction,
                Constants.Image,
                Constants.Range
        };

        Cursor r = db.query(Constants.Node,projection,null,null,null,null,null);
        String[] beaconID;
        BeaconID Id;
        int major, minor;
        while(r.moveToNext()){
            beaconID = r.getString(0).split(":");
            major = Integer.parseInt(beaconID[1]);
            minor = Integer.parseInt(beaconID[2]);

            Id = new BeaconID(UUID.fromString(beaconID[0]), major, minor);

            Node n = new Node(Id,Boolean.valueOf(r.getString(1)),Boolean.valueOf(r.getString(2)),r.getString(3),r.getString(4));
            n.setOutside(Boolean.valueOf(r.getString(5)));
            n.setNessOutside(Boolean.valueOf(r.getString(6)));
            n.setDirection(r.getInt(7));
            n.setRoomsRange(r.getString(9));

            nodes.add(n);

        }
        r.close();
        db.close();

        getNodesRelation(nodes);
    }

    public void getNodesRelation(ArrayList<Node> nodes){
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = { Constants.SecondID };

        Cursor r = null;
        String[] beaconID;
        BeaconID Id;
        int major, minor;
        int dir;

        for(Node n : nodes) {
            r = db.query(Constants.Node, projection, Constants.FirstID + " = ?", new String[]{n.get_id().toString()}, null, null, null);
            while (r.moveToNext()) {
                beaconID = r.getString(0).split(":");
                major = Integer.parseInt(beaconID[1]);
                minor = Integer.parseInt(beaconID[2]);
                Id = new BeaconID(UUID.fromString(beaconID[0]), major, minor);

                for (Node nb : nodes)
                    if (nb.get_id().equals(Id) && !nb.equals(n)) {
                        dir = (r.getInt(2) + 180) % 360;
                        if(r.getInt(3) == 1)
                            nb.AddNeighbour(new Pair<Node, Integer>(n, dir));
                        else
                            nb.AddNeighbour(new Pair<Node, Integer>(n, nb.getDirection()));
                        n.AddNeighbour(new Pair<Node, Integer>(nb, r.getInt(2)));

                        break;
                    }
            }
            r.close();
        }
        db.close();
    }

}
