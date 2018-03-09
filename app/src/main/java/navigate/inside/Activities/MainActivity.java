package navigate.inside.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import navigate.inside.Logic.PathFinder;
import navigate.inside.Objects.Node;
import navigate.inside.R;

public class MainActivity extends AppCompatActivity {

    private TextView sNode, gNode;
    private String txtSNode, txtGNode;
    private CheckBox chElevator;
    private ListView lView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sNode = (TextView)findViewById(R.id.start);
        gNode = (TextView)findViewById(R.id.goal);
        chElevator = (CheckBox)findViewById(R.id.elevator);
        lView = (ListView)findViewById(R.id.nodeList);
    }

    public void search(View view) {

        txtSNode = sNode.getText().toString();
        txtGNode = gNode.getText().toString();

        PathFinder pf = PathFinder.getInstance();
        boolean b = chElevator.isChecked();

        // if b is true then ignore the stairs (don't expand stairs node) else go through stairs
        List<Node> nodes = pf.FindPath(txtSNode, txtGNode, b);
        List<String> list = new ArrayList<>(nodes.size());

        for(Node n : nodes)
            list.add(n.toString());

        lView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,list));


    }
}
