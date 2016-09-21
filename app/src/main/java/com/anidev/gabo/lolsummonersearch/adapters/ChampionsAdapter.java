package com.anidev.gabo.lolsummonersearch.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.Collections;

import com.anidev.gabo.lolsummonersearch.MainActivity;
import com.anidev.gabo.lolsummonersearch.R;

/**
 * Populate the scrollview in activity_ranked.xml
 * Created by Oscar on 3/27/2016.
 */
public class ChampionsAdapter extends BaseAdapter {
    private Context context;

    public ChampionsAdapter(Context context){
        this.context = context;
    }

    @Override
    public int getCount() {
        return MainActivity.rankedChampObjects.size() - 1; //don't count the overall item
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView == null){
            return populate(inflater, parent, position + 1);
        }
        else{
            return populate(inflater, parent, position + 1);
        }

    }

    private View populate(LayoutInflater inflater, ViewGroup parent, int position){
        //sort the list
        for(int i = 0; i < MainActivity.rankedChampObjects.size(); i++){
            int highestIndex = i;
            for(int j = i; j < MainActivity.rankedChampObjects.size(); j++){
                if(MainActivity.rankedChampObjects.get(highestIndex).getTotalSessionsPlayed() < MainActivity.rankedChampObjects.get(j).getTotalSessionsPlayed()){
                    highestIndex = j;
                }
            }
            Collections.swap(MainActivity.rankedChampObjects, i, highestIndex);
        }
        // get layout from custom scrollview item
        View listView = inflater.inflate(R.layout.custom_listview_item, parent, false);
        // Champion name
        ((TextView) listView.findViewById(R.id.grid_item_label)).setText(MainActivity.rankedChampObjects.get(position).getName());
        // Champion image
        ((ImageView) listView.findViewById(R.id.grid_item_image)).setImageBitmap(MainActivity.rankedChampObjects.get(position).getChampionIcon());
        // games value text view
        ((TextView) listView.findViewById(R.id.tv_games_played_value)).setText(String.valueOf(MainActivity.rankedChampObjects.get(position).getTotalSessionsPlayed()));
        // win ratio text view
        String winRatio = new DecimalFormat("##.##").format(((double) MainActivity.rankedChampObjects.get(position).getTotalSessionsWon() / ((double) MainActivity.rankedChampObjects.get(position).getTotalSessionsPlayed())) * 100)+"%";
        ((TextView) listView.findViewById(R.id.tv_grid_win_ratio_value)).setText(winRatio);
        return listView;
    }
}
