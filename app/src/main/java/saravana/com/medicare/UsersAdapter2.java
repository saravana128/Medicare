package saravana.com.medicare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by saravana perumal on 04-03-2017.
 */

public class UsersAdapter2 extends ArrayAdapter<User2> {
    public UsersAdapter2(Context context, ArrayList<User2> sub) {
        super(context, 0, sub);
    }

    @Override
    public View getView(final int position2, View convertView2, ViewGroup parent2) {
        // Get the data item for this position
        final User2 user = getItem(position2);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView2 == null) {
            convertView2 = LayoutInflater.from(getContext()).inflate(R.layout.listviewsymptom, parent2, false);
        }
        // Lookup view for data population

        TextView event123= (TextView) convertView2.findViewById(R.id.symNamw);
        CheckBox l7=(CheckBox) convertView2.findViewById(R.id.checkBox);
        event123.setText(user.title);
        ListView le=(ListView) convertView2.findViewById(R.id.tgh);






        // Populate the data into the template view using the data object





        // Return the completed view to render on screen
        return convertView2;
    }
}