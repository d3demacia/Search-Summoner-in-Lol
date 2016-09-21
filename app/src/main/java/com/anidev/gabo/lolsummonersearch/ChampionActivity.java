package com.anidev.gabo.lolsummonersearch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;

public class ChampionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_champion);
        ((TextView)findViewById(R.id.tv_season_champ)).setText(MainActivity.selectedSeason);
        ((TextView)findViewById(R.id.tv_act_champ_summoner_name)).setText(MainActivity.summoner.getFormattedName());
        ((ImageView)findViewById(R.id.iv_champion_icon)).setImageBitmap(RankedActivity.selectedChampion.getChampionIcon());
        ((TextView)findViewById(R.id.tv_champion_name)).setText(RankedActivity.selectedChampion.getName());
        ((TextView)findViewById(R.id.tv_champion_title)).setText(RankedActivity.selectedChampion.getTitle());

        ((TextView)findViewById(R.id.tv_act_champ_games)).setText(String.valueOf(RankedActivity.selectedChampion.getTotalSessionsPlayed()));
        ((TextView)findViewById(R.id.tv_act_champ_games_won)).setText(String.valueOf(RankedActivity.selectedChampion.getTotalSessionsWon()));
        ((TextView)findViewById(R.id.tv_act_champ_games_lost)).setText(String.valueOf(RankedActivity.selectedChampion.getTotalSessionsLost()));
        String winRatio = getWinPercentage(RankedActivity.selectedChampion.getTotalSessionsWon(), RankedActivity.selectedChampion.getTotalSessionsLost()) + "%";
        ((TextView)findViewById(R.id.tv_act_champs_win_ratio)).setText(winRatio);
        ((TextView)findViewById(R.id.tv_average_kills)).setText(getRoundedValue(RankedActivity.selectedChampion.getTotalChampionKills(), RankedActivity.selectedChampion.getTotalSessionsPlayed()));
        ((TextView)findViewById(R.id.tv_average_deaths)).setText(getRoundedValue(RankedActivity.selectedChampion.getTotalDeathsPerSession(), RankedActivity.selectedChampion.getTotalSessionsPlayed()));
        ((TextView)findViewById(R.id.tv_average_assists)).setText(getRoundedValue(RankedActivity.selectedChampion.getTotalAssists(),RankedActivity.selectedChampion.getTotalSessionsPlayed()));
        ((TextView)findViewById(R.id.tv_average_minions)).setText(getRoundedValue(RankedActivity.selectedChampion.getTotalMinionKills(),RankedActivity.selectedChampion.getTotalSessionsPlayed()));
        ((TextView)findViewById(R.id.tv_average_double_kills)).setText(getRoundedValue(RankedActivity.selectedChampion.getTotalDoubleKills(), RankedActivity.selectedChampion.getTotalSessionsPlayed()));
        ((TextView)findViewById(R.id.tv_average_triple_kills)).setText(getRoundedValue(RankedActivity.selectedChampion.getTotalTripleKills(), RankedActivity.selectedChampion.getTotalSessionsPlayed()));
        ((TextView)findViewById(R.id.tv_average_quadra_kills)).setText(getRoundedValue(RankedActivity.selectedChampion.getTotalQuadraKills(), RankedActivity.selectedChampion.getTotalSessionsPlayed()));
        ((TextView)findViewById(R.id.tv_average_penta_kills)).setText(getRoundedValue(RankedActivity.selectedChampion.getTotalPentaKills(), RankedActivity.selectedChampion.getTotalSessionsPlayed()));
    }

    private String getWinPercentage(int w, int l){
        String winPercentageString;
        double winPer = (double)(w)/((double)(w)+(double)(l));
        winPer = winPer*100;
        winPercentageString = new DecimalFormat("##.##").format(winPer);
        return winPercentageString;
    }

    private String getRoundedValue(int n, int d){
        String sigFigs;
        double sigFigsDub = (double)(n)/((double)(d));
        sigFigs = new DecimalFormat("#######.##").format(sigFigsDub);
        return sigFigs;
    }
}
