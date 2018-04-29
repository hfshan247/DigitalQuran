package com.example.hussainfarooq.digitalquran;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.hussainfarooq.digitalquran.model.Ayat;

import java.util.List;

public class AyatAdapter extends ArrayAdapter<Ayat> {

    private List<Ayat> ayats;
    private Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView ayatSource;
        TextView ayatText;
    }

    public AyatAdapter(List<Ayat> data, Context context) {
        super(context, R.layout.search_result, data);
        this.ayats = data;
        this.mContext = context;

    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Ayat ayat = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.search_result, parent, false);
            viewHolder.ayatSource = convertView.findViewById(R.id.ayat_source);
            viewHolder.ayatText = convertView.findViewById(R.id.ayat_text);

            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        if (ayat != null) {
            viewHolder.ayatSource.setText("(" + ayat.getSurah() + ", " + String.valueOf(Integer.parseInt(ayat.getSurahIndex())) + ":" + String.valueOf(ayat.getIndex() + 1) + ")");
            viewHolder.ayatText.setText(ayat.getText());
        }

        return result;
    }

}