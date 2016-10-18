package com.example.android.marvelapitest;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Usuario on 17/10/2016.
 */
public class ComicAdapter extends ArrayAdapter<Comic> {

    public ComicAdapter(Activity context, ArrayList<Comic> comics){
        super(context, 0, comics);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.comic_list_component, parent, false);
        }

        Comic currentComic = getItem(position);

        TextView tituloTextView = (TextView)listItemView.findViewById(R.id.titulo);
        tituloTextView.setText(currentComic.getTitulo());

        TextView heroeTextView = (TextView)listItemView.findViewById(R.id.heroe);
        heroeTextView.setText(currentComic.getHeroe());

        return listItemView;
    }
}
