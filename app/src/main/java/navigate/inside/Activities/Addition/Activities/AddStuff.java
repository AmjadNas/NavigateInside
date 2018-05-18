package navigate.inside.Activities.Addition.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import navigate.inside.Objects.Node;
import navigate.inside.Objects.Room;
import navigate.inside.R;


public class AddStuff extends AppCompatActivity {

    private TextView sNode, gNode;
    private String txtSNode, txtGNode;
    private CheckBox chElevator;
    private Button search, add, relate, addroom;
    

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_get_directions);

        initView();
    }

    private void initView(){
    //todo DISABLED TEMPORARILY
        /*sNode = (TextView)view.findViewById(R.id.start);
        gNode = (TextView)view.findViewById(R.id.goal);
        chElevator = (CheckBox)view.findViewById(R.id.elevator);
        search = (Button) view.findViewById(R.id.search);*/

        addroom = (Button) findViewById(R.id.addRoom_btn);
        add = (Button) findViewById(R.id.addnode);
        relate = (Button) findViewById(R.id.relaetnode);

        //search.setOnClickListener(this);

        addroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(AddStuff.this, AddRoomActivity.class);
                startActivity(intent);
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddStuff.this, AddNodeActivity.class);
                startActivity(intent);
            }
        });
        relate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddStuff.this, ADNEBERACtivity.class);
                startActivity(intent);
            }
        });
    }



}
