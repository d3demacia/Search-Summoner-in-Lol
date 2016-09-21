package com.anidev.gabo.lolsummonersearch;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import api.calls.AllChampionsSquareImage;
import api.calls.ChampionStaticImageData;
import api.calls.LeagueById;
import api.calls.ProfileIcon;
import api.calls.RankedStatsById;
import api.calls.SummonerByName;
import api.objects.ChampionRankedObject;
import api.objects.RankedStatsByIdObject;
import api.objects.Summoner;

public class MainActivity extends AppCompatActivity implements SummonerByName.AsyncCallback, RankedStatsById.AsyncCallback,
        LeagueById.AsyncCallback{

    public static final String API_KEY = "RGAPI-EB0545DD-DFC2-4ACC-A21B-82E00AC441FA";
    private ProgressDialog progress;
    private Spinner mRegionsSpinner;
    private Spinner mSeasonsSpinner;
    private Button mSearchButton;
    public static String mRegionCode;
    public static String mSeasonCode;
    public static String imageVersion;
    protected static String selectedSeason;
    public static Summoner summoner;
    public static List<ChampionRankedObject> rankedChampObjects;
    //API endpoint calls
    private SummonerByName summonerObject;
    private RankedStatsById rankedStatsObject;
    private ChampionStaticImageData champImageDataObject; //champion static data with image field selected
    private AllChampionsSquareImage allChampionsSquareImageObject;
    private LeagueById leagueObject;
    //booleans
    private boolean summIsDone = false;
    private boolean rankStatsIsDone = false;
    private boolean champImageDataIsDone = false;
    private boolean profileIconIsDone = false;
    private boolean allChampSquaresIsDone = false;
    private boolean leagueIsDone = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //initialize view components
        initializeRegionsSpinner();
        initializeSeasonsSpinner();
        mSearchButton = (Button)findViewById(R.id.search_main_btn);
        ((TextView)findViewById(R.id.version)).setText(R.string.version);
        progress = new ProgressDialog(this);
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        Log.d("myapp", "onRestart");
        summoner = null;
        rankedChampObjects = null;
        reset();
    }

    //search button onClick method
    public void search(View v){
        EditText nameInput = (EditText)findViewById(R.id.name_input);
        if(nameInput.getText().toString().equals("")){
            Toast toast = Toast.makeText(this, "Please enter a name into the search field.", Toast.LENGTH_LONG);
            toast.show();
            reset();
        }
        else{
            mSearchButton.setEnabled(false);
            //get values from the view components
            setRegionCode(mRegionsSpinner.getSelectedItem().toString());
            setSeasonCode(mSeasonsSpinner.getSelectedItem().toString());
            Log.d("myapp", "New search - " + nameInput.getText().toString().toLowerCase().replace(" ", ""));
            //set progress dialog
            progress.setTitle("Loading information for " + nameInput.getText().toString());
            progress.setMessage("Please wait while loading...");
            progress.show();
            //execute first call
            summonerObject = new SummonerByName(nameInput.getText().toString().toLowerCase().replace(" ", ""));
            summonerObject.registerCallBack(this);
            summonerObject.execute();
        }
    }

    //1st call finished
    //SummonerByName endpoint
    //Executed when the summoner by name endpoint has finished loading.
    @Override
    public void summonerCallBack() {
        if(summonerObject.isSuccess()){
            Log.d("myapp", "Success: summonerCallBack");
            summoner = new Summoner(summonerObject.getName(), summonerObject.getId(),summonerObject.getRevisionDate(), summonerObject.getProfileIconId(),summonerObject.getLevel());
            progress.setTitle("Loading information for " + summoner.getFormattedName());//update the name on the loading dialog
            //execute ranked stats
            rankedStatsObject = new RankedStatsById(summoner.getId());
            rankedStatsObject.registerCallBack(this);
            rankedStatsObject.execute();
        }
        else{
            Log.d("myapp", "Fail: summonerCallBack");
            Toast toast = Toast.makeText(this, "That summoner doesn't exist. Please try another name.", Toast.LENGTH_LONG);
            toast.show();
            reset();
        }
        summIsDone = true;
        if(isAllDone()){
            doneOperation();
        }
    }

    //3rd call finished
    //RankedStatsById endpoint
    //Executed when the ranked stats has finished loading.
    @Override
    public void rankedStatsByIdCallBack(){
        if(rankedStatsObject.isSuccess()){
            Log.d("myapp", "Success: rankedStatsByIdCallBack");
            //print the champion ids and their aggregated statistics
            //rankedStatsObject.printInfo();
            //create a structured list of champion objects
            List<RankedStatsByIdObject> rankedChampRawObjects = rankedStatsObject.getRankedChampionObjects();
            rankedChampObjects = new ArrayList<>();
            for(int i=0; i < rankedChampRawObjects.size(); i++){
                RankedStatsByIdObject ro = rankedChampRawObjects.get(i);
                ChampionRankedObject co = new ChampionRankedObject();
                //set fields
                try {
                    co.setId(ro.getId());
                    co.setTotalDeathsPerSession(ro.getAggreStats().getInt("totalDeathsPerSession"));
                    co.setTotalSessionsPlayed(ro.getAggreStats().getInt("totalSessionsPlayed"));
                    co.setTotalDamageTaken(ro.getAggreStats().getInt("totalDamageTaken"));
                    co.setTotalQuadraKills(ro.getAggreStats().getInt("totalQuadraKills"));
                    co.setTotalTripleKills(ro.getAggreStats().getInt("totalTripleKills"));
                    co.setTotalMinionKills(ro.getAggreStats().getInt("totalMinionKills"));
                    co.setMaxChampionsKilled(ro.getAggreStats().getInt("maxChampionsKilled"));
                    co.setTotalDoubleKills(ro.getAggreStats().getInt("totalDoubleKills"));
                    co.setTotalPhysicalDamageDealt(ro.getAggreStats().getInt("totalPhysicalDamageDealt"));
                    co.setTotalChampionKills(ro.getAggreStats().getInt("totalChampionKills"));
                    co.setTotalAssists(ro.getAggreStats().getInt("totalAssists"));
                    co.setMostChampionKillsPerSession(ro.getAggreStats().getInt("mostChampionKillsPerSession"));
                    co.setTotalDamageDealt(ro.getAggreStats().getInt("totalDamageDealt"));
                    co.setTotalFirstBlood(ro.getAggreStats().getInt("totalFirstBlood"));
                    co.setTotalSessionsLost(ro.getAggreStats().getInt("totalSessionsLost"));
                    co.setTotalSessionsWon(ro.getAggreStats().getInt("totalSessionsWon"));
                    co.setTotalMagicDamageDealt(ro.getAggreStats().getInt("totalMagicDamageDealt"));
                    co.setTotalGoldEarned(ro.getAggreStats().getInt("totalGoldEarned"));
                    co.setTotalPentaKills(ro.getAggreStats().getInt("totalPentaKills"));
                    co.setTotalTurretsKilled(ro.getAggreStats().getInt("totalTurretsKilled"));
                    co.setMostSpellsCast(ro.getAggreStats().getInt("mostSpellsCast"));
                    co.setMaxNumDeaths(ro.getAggreStats().getInt("maxNumDeaths"));
                    co.setTotalUnrealKills(ro.getAggreStats().getInt("totalUnrealKills"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //add to list
                rankedChampObjects.add(co);
            }
            //execute league
            leagueObject = new LeagueById(summoner.getId(), summoner.getFormattedName());
            leagueObject.registerCallBack(this);
            leagueObject.execute();
            //execute champion static image data

        }
        else{
            Log.d("myapp", "Fail: rankedStatsByIdCallBack");
            Toast toast = Toast.makeText(this, summoner.getFormattedName() + " doesn't have any ranked stats for " +
                    mSeasonsSpinner.getSelectedItem().toString() + ". Please try another season.", Toast.LENGTH_LONG);
            toast.show();
            reset();
            Log.d("myapp", "Reset.");
        }
        rankStatsIsDone = true;
        if(isAllDone()){
            doneOperation();
        }
    }

    //league call finished
    @Override
    public void leagueCallBack() {
        if(leagueObject.isSuccess()){
            Log.d("myapp", "Success: leagueCallback");
            //set summoner info
            summoner.setDivision(leagueObject.getDivision());
            summoner.setTier(leagueObject.getTier());
            summoner.setRanked_solo_wins(leagueObject.getWins());
            summoner.setRanked_solo_losses(leagueObject.getLosses());
        }
        else{
            Log.d("myapp", "Fail: leagueCallback");
        }
        leagueIsDone = true;
        if(isAllDone()){
            doneOperation();
        }
    }

    //4th call finished
    //ChampionStaticData endpoint - image
    //Executed when static data image has finished loading.


    //5th call finished
    //Profile icon static data endpoint


    //6th call finished
    //All champion square images


    //do when everything is completed
    private void doneOperation(){
        //get the id '0' and get total sessions won and lost
        for(ChampionRankedObject obj : rankedChampObjects){
            if(obj.getId() == 0){
                //overall stats found
                summoner.setRanked_solo_wins(obj.getTotalSessionsWon());
                summoner.setRanked_solo_losses(obj.getTotalSessionsLost());
                Log.d("myapp", "Wins, losses: " + obj.getTotalSessionsWon() + " " + obj.getTotalSessionsLost());
            }
        }
        Log.d("myapp", "All calls done.");
        Intent intent = new Intent(this, RankedActivity.class);
        progress.dismiss(); //dismiss progress spinner
        startActivity(intent);
    }

    private boolean isAllDone(){
        return summIsDone && rankStatsIsDone && leagueIsDone;
    }

    private void reset(){
        summIsDone = false;
        rankStatsIsDone = false;
        champImageDataIsDone = false;
        profileIconIsDone = false;
        allChampSquaresIsDone = false;
        leagueIsDone = false;
        progress.dismiss();
        progress = new ProgressDialog(this);
        mSearchButton.setEnabled(true);
    }

    //dynamically initialize a spinner for the regions
    private void initializeRegionsSpinner(){
        mRegionsSpinner = (Spinner) findViewById(R.id.spr_regions);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mRegionsSpinner.getBackground().setColorFilter(getResources().getColor(R.color.indigoDark, getTheme()), PorterDuff.Mode.SRC_ATOP);
        }
        else{
            mRegionsSpinner.getBackground().setColorFilter(getResources().getColor(R.color.indigoDark), PorterDuff.Mode.SRC_ATOP);
        }
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.regions_array, R.layout.custom_spinner);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Apply the adapter to the spinner
        mRegionsSpinner.setAdapter(adapter);
    }

    //dynamically initialize a spinner for the seasons
    private void initializeSeasonsSpinner(){
        mSeasonsSpinner = (Spinner) findViewById(R.id.spr_seasons);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mSeasonsSpinner.getBackground().setColorFilter(getResources().getColor(R.color.indigoDark, getTheme()), PorterDuff.Mode.SRC_ATOP);
        }
        else{
            mSeasonsSpinner.getBackground().setColorFilter(getResources().getColor(R.color.indigoDark), PorterDuff.Mode.SRC_ATOP);
        }
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.seasons, R.layout.custom_spinner);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Apply the adapter to the spinner
        mSeasonsSpinner.setAdapter(adapter);
    }

    //translate the season spinner selected object into api mnemonic
    private void setSeasonCode(String season){
        selectedSeason = season;
        switch(season){
            case "Season 6":
                mSeasonCode = "SEASON2016";
                break;
            case "Season 5":
                mSeasonCode = "SEASON2015";
                break;
            case "Season 4":
                mSeasonCode = "SEASON2014";
                break;
            case "Season 3":
                mSeasonCode = "SEASON3";
                break;
            default:
                mSeasonCode = "NULL";
                break;
        }
    }

    //translate the region spinner selected object into an api mnemonic
    private void setRegionCode(String region){
        switch (region) {
            case "North America":
                mRegionCode = "na";
                break;
            case "Brazil":
                mRegionCode = "br";
                break;
            case "EU Nordic/East":
                mRegionCode = "eune";
                break;
            case "EU West":
                mRegionCode = "euw";
                break;
            case "Korea":
                mRegionCode = "kr";
                break;
            case "Latin North":
                mRegionCode = "lan";
                break;
            case "Latin South":
                mRegionCode = "las";
                break;
            case "Oceania":
                mRegionCode = "oce";
                break;
            case "PBE":
                mRegionCode = "pbe";
                break;
            case "Russia":
                mRegionCode = "ru";
                break;
            case "Turkey":
                mRegionCode = "tr";
                break;
            default:
                mRegionCode = "NULL";
                break;
        }
    }
}
