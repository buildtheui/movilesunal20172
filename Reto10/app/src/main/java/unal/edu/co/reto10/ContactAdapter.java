package unal.edu.co.reto10;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by FABIAN on 13/11/2017.
 */

public class ContactAdapter extends ArrayAdapter<Contact> {
    private final Context context;
    private final ArrayList<Contact> values;
    private ArrayList<Contact> arraylist;

    public ContactAdapter(Context context, ArrayList<Contact> list) {
        super(context, R.layout.row_layout, list);
        this.context = context;
        this.values = list;
        this.arraylist = new ArrayList<Contact>();
        this.arraylist.addAll(list);
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_layout, parent, false);

        TextView lvBusinessName = (TextView) rowView.findViewById(R.id.lvCountry);
        TextView lvCategory = (TextView) rowView.findViewById(R.id.lvAge);

        lvBusinessName.setText(values.get(position).getPa_s());
        lvCategory.setText("Edad: " + values.get(position).getEdad_a_os());

        return rowView;
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        values.clear();
        if (charText.length() == 0) {
            values.addAll(arraylist);
        }
        else
        {
            for (Contact wp : arraylist)
            {
                if (wp.getEdad_a_os().toLowerCase(Locale.getDefault()).contains(charText)
                        || wp.getPa_s().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    values.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}
