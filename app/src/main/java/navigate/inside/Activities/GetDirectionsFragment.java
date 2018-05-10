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

import navigate.inside.Logic.PathFinder;
import navigate.inside.R;
import navigate.inside.Utills.Constants;


public class GetDirectionsFragment extends Fragment implements View.OnClickListener{

    private TextView sNode, gNode;
    private String txtSNode, txtGNode;
    private CheckBox chElevator;
    private Button button;

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
        sNode = (TextView)view.findViewById(R.id.start);
        gNode = (TextView)view.findViewById(R.id.goal);
        chElevator = (CheckBox)view.findViewById(R.id.elevator);
        button = (Button) view.findViewById(R.id.search);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        txtSNode = sNode.getText().toString();
        txtGNode = gNode.getText().toString();

        PathFinder pf = PathFinder.getInstance();
        boolean b = chElevator.isChecked();

        // if b is true then ignore the stairs (don't expand stairs node) else go through stairs
        if(!pf.FindPath(txtSNode, txtGNode, b).isEmpty()) {

            Intent intent = new Intent(getActivity(), PlaceViewActivity.class);
            intent.putExtra(Constants.INDEX, 0);
            startActivity(intent);
        }else
            Toast.makeText(getContext(), R.string.no_path_found, Toast.LENGTH_LONG).show();
    }
}
