package navigate.inside.Objects;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;

import java.util.ArrayList;

import navigate.inside.R;
import navigate.inside.Utills.Constants;
import navigate.inside.Utills.Converter;

/**
 * Created by StiPro on 4/9/2018.
 */

public class DataBase extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = String.valueOf(R.string.app_name);

    private static final String SQL_CREATE_NODE_TABLE = "CREATE TABLE "+ Constants.Node + " ("+
            Constants.ID + " INTEGER PRIMARY KEY,"+
            Constants.Junction + " BOOLEAN," +
            Constants.Elevator + " BOOLEAN,"+
            Constants.Building + " VARCHAR(25),"+
            Constants.Floor + " VARCHAR(25)," +
            Constants.Outside + " BOOLEAN,"+
            Constants.NessOutside + " BOOLEAN,"+
            Constants.Direction + " INTEGER " +
            Constants.Image +" BLOB " +
            ")";

    private static final String SQL_CREATE_RELATION_TABLE = "CREATE TABLE " + Constants.Relation +" ("+
            Constants.FirstID + " INTEGER PRIMARY KEY,"+
            Constants.SecondID + " INTEGER PRIMARY KEY,"+
            Constants.Direction + "INTEGER ,"+
             "CONSTRAINT PK2 PRIMARY KEY (" + Constants.FirstID + "," + Constants.SecondID +")" +
            " )"
            ;


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
          Constants.ID,
                Constants.Junction,
                Constants.Elevator,
                Constants.Building,
                Constants.Floor,
                Constants.Outside,
                Constants.NessOutside,
                Constants.Direction,
                Constants.Image
        };

        Cursor r = db.query(Constants.Node,projection,null,null,null,null,null);

        while(r.moveToNext()){

        Node n = new Node(r.getInt(0),Boolean.valueOf(r.getString(1)),Boolean.valueOf(r.getString(2)),r.getString(3),r.getString(4));
            n.setOutside(Boolean.valueOf(r.getString(5)));
            n.setNessOutside(Boolean.valueOf(r.getString(6)));
            n.setDirection(r.getInt(7));
            byte[] m = r.getBlob(8);
            n.setImage(Converter.decodeImage(m));
            nodes.add(n);

        }
        r.close();
        db.close();
    }

    public void getNodesRelation(){

    }

}
