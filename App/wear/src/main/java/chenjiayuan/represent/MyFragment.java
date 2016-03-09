package chenjiayuan.represent;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by chenjiayuan on 3/8/16.
 */
public class MyFragment extends Fragment {

    //private View fragmentView;
    private View.OnClickListener listener;
    private String t;
    private String te;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_my, container, false);
        TextView mTitle = (TextView) fragmentView.findViewById(R.id.title);
        TextView mDescription = (TextView) fragmentView.findViewById(R.id.description);
        mTitle.setText(t);
        mDescription.setText(te);
        fragmentView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View view) {
                if (listener != null) {
                    listener.onClick(view);
                }
            }

        });
        return fragmentView;
    }
    public void setTitle (String title) {
        t=title;
    }
    public String getTitle() {
        return t;
    }
    public void setText (String text) {
        te=text;
    }
    public void setOnClickListener(final View.OnClickListener listener) {
        this.listener = listener;
    }

}
