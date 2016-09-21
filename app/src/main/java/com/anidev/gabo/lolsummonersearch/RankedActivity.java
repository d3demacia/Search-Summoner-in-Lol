package com.anidev.gabo.lolsummonersearch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DecimalFormat;

import api.objects.ChampionRankedObject;
import com.anidev.gabo.lolsummonersearch.adapters.ChampionsAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class RankedActivity extends AppCompatActivity {

    public static ChampionRankedObject selectedChampion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranked);
        setViews();
        selectedChampion = null;
    }
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }


    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }


    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }
    @Override
    protected void onStart(){
        super.onStart();
        selectedChampion = null;
    }
    private AdView mAdView;
    private void setViews(){
        //Season
        TextView season = (TextView)findViewById(R.id.tv_season);
        season.setText(MainActivity.selectedSeason);
        //Profile Icon
        ImageView profileIcon = (ImageView)findViewById(R.id.iv_icon);
        profileIcon.setImageBitmap(MainActivity.summoner.getProfileIcon());
        //Summoner name
        TextView summonerName = (TextView)findViewById(R.id.tv_name);
        summonerName.setText(MainActivity.summoner.getFormattedName());
        //Rank
        TextView rank = (TextView)findViewById(R.id.tv_rank);
        if(!MainActivity.selectedSeason.equals("Season 6")){
            String error = MainActivity.selectedSeason + " rank unavailable.";
            rank.setText(error); //not ranked for current season
        }
        else{
            String rankText = MainActivity.summoner.getTier() + " " + MainActivity.summoner.getDivision();
            rank.setText(rankText); //currently ranked
        }
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mAdView.loadAd(adRequest);
        //Wins
        TextView winsText = (TextView)findViewById(R.id.tv_winsText);
        String winsStr = getString(R.string.wins_txt) + " ";
        winsText.setText(winsStr);
        TextView winsValue = (TextView)findViewById(R.id.tv_winsValueText);
        String winsValueStr = String.valueOf(MainActivity.summoner.getRanked_solo_wins()) + "   ";
        winsValue.setText(winsValueStr);
        //Losses
        TextView lossesText = (TextView)findViewById(R.id.tv_lossesText);
        String lossesStr = getString(R.string.losses_txt) + " ";
        lossesText.setText(lossesStr);
        TextView lossesValue = (TextView)findViewById(R.id.tv_lossesTextValue);
        lossesValue.setText(String.valueOf(MainActivity.summoner.getRanked_solo_losses()));
        //Win Ratio
        TextView winRatioText = (TextView)findViewById(R.id.tv_winRatioText);
        String ratioStr = getString(R.string.win_ratio) + " ";
        winRatioText.setText(ratioStr);
        TextView winRatioValue = (TextView)findViewById(R.id.tv_winRatioValueText);
        String winPerc = new DecimalFormat("##.##").format(((double)MainActivity.summoner.getRanked_solo_wins() / ((double)MainActivity.summoner.getRanked_solo_wins() + (double)MainActivity.summoner.getRanked_solo_losses()))*100)+"%";
        winRatioValue.setText(winPerc);
        //Ranked champs hint
        TextView rankedChampsHint= (TextView)findViewById(R.id.tv_ranked_champs_hint);
        rankedChampsHint.setText(getString(R.string.ranked_champ_hint));
        //Champions scrollview
        ChampionsAdapter championsAdapter = new ChampionsAdapter(this);
        ListView championsListView = (ListView)findViewById(R.id.lv_champions);
        championsListView.setAdapter(championsAdapter);
        championsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                int shiftedPosition = position+1;
                selectedChampion = MainActivity.rankedChampObjects.get(shiftedPosition);
                Log.d("myapp", "Selected champion: " + selectedChampion.getName());
                Intent myIntent = new Intent(RankedActivity.this, ChampionActivity.class);
                RankedActivity.this.startActivity(myIntent);


            }
        });
    }


}
