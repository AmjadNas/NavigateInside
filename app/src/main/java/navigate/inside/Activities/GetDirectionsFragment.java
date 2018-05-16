package navigate.inside.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.UUID;

import navigate.inside.Logic.PathFinder;
import navigate.inside.Logic.SysData;
import navigate.inside.Objects.BeaconID;
import navigate.inside.Objects.Node;
import navigate.inside.R;
import navigate.inside.Utills.Constants;


public class GetDirectionsFragment extends Fragment implements View.OnClickListener{

    private TextView sNode, gNode;
    private String txtSNode, txtGNode;
    private CheckBox chElevator;
    private Button search, add, relate;

    public GetDirectionsFragment() {

    }

    public static GetDirectionsFragment newInstance() {
        return new GetDirectionsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_get_directions, container, false);
        initView(view);
        return view;
    }

    private void initView(View view){
    //todo DISABLED TEMPORARILY
        /*sNode = (TextView)view.findViewById(R.id.start);
        gNode = (TextView)view.findViewById(R.id.goal);
        chElevator = (CheckBox)view.findViewById(R.id.elevator);*/

        search = (Button) view.findViewById(R.id.search);
        add = (Button) view.findViewById(R.id.addnode);
        relate = (Button) view.findViewById(R.id.relaetnode);

        search.setOnClickListener(this);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddNodeActivity.class);
                startActivity(intent);
            }
        });
        relate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ADNEBERACtivity.class);
                startActivity(intent);
            }
        });
    }

    public void BindText(Node n){
        //sNode.setText(n.getRooms().get(0).GetRoomName());
    }

    @Override
    public void onClick(View v) {
        // todo SEARCH DISABLED TEMPORARILY

        /*
        txtSNode = sNode.getText().toString();
        txtGNode = gNode.getText().toString();

        //temporary
        BeaconID FinishNode = new BeaconID(UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"),886,13607);
        BeaconID StartNode = new BeaconID(UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"),48613,34740);
       // StartNode = SysData.getInstance().getNodeIdByRoom(txtSNode);
        //FinishNode = SysData.getInstance().getNodeIdByRoom(txtGNode);

        if(StartNode == null){
            Toast.makeText(getContext(), "Start Poistion was not found !", Toast.LENGTH_SHORT).show();
        }
        if(FinishNode == null){
            Toast.makeText(getContext(), "Finish goal was not found !", Toast.LENGTH_SHORT).show();
        }

        PathFinder pf = PathFinder.getInstance();
        boolean b = chElevator.isChecked();

        // if b is true then ignore the stairs (don't expand stairs node) else go through stairs
        if(!pf.FindPath(StartNode, FinishNode, b).isEmpty()) {

            Intent intent = new Intent(getActivity(), PlaceViewActivity.class);
            intent.putExtra(Constants.INDEX, 0);
            startActivity(intent);
        }else
            Toast.makeText(getContext(), R.string.no_path_found, Toast.LENGTH_LONG).show();
            */
    }
}
