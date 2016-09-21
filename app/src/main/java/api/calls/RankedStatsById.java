package api.calls;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import api.objects.RankedStatsByIdObject;
import com.anidev.gabo.lolsummonersearch.MainActivity;

/**
 * /api/lol/{region}/v1.3/stats/by-summoner/{summonerId}/ranked
 * Created by Oscar on 3/17/2016.
 */
public class RankedStatsById extends AsyncTask<Void, Void, Void>{
    private AsyncCallback asyncCallback;
    private URL url;
    private String jsonResponse;
    private boolean isSuccess;
    private List<RankedStatsByIdObject> rankedChampionObjects;

    public interface AsyncCallback{
        void rankedStatsByIdCallBack();
    }

    public void registerCallBack(AsyncCallback cb){
        this.asyncCallback = cb;
    }

    public RankedStatsById(long id){
        //set url from params
        try {
            url = new URL("https://"+MainActivity.mRegionCode+".api.pvp.net/api/lol/"+MainActivity.mRegionCode+"/v1.3/stats/by-summoner/"+id+"/ranked?season="+MainActivity.mSeasonCode+"&api_key="+ MainActivity.API_KEY);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    //background operations
    @Override
    protected Void doInBackground(Void... params) {
        loadJSON(url);
        return null;
    }

    //post execution
    @Override
    protected void onPostExecute(Void v){
        //parse info
        if(jsonResponse != null){
            try {
                JSONObject summOb = new JSONObject(jsonResponse);
                JSONArray champObjectArray = summOb.getJSONArray("champions");
                rankedChampionObjects = new ArrayList<>();
                for(int i=0; i < champObjectArray.length(); i++){
                    int id = champObjectArray.getJSONObject(i).getInt("id");
                    JSONObject data = champObjectArray.getJSONObject(i).getJSONObject("stats");
                    rankedChampionObjects.add(new RankedStatsByIdObject(id, data));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            isSuccess = true;
        }
        else{
            Log.d("myapp", "RankedStatsById jsonResponse was null");
            isSuccess = false;
        }
        asyncCallback.rankedStatsByIdCallBack();
    }

    private void loadJSON(URL url){
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            String strTemp;
            while(null != (strTemp = br.readLine())){
                jsonResponse = strTemp;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void printInfo(){
        Log.d("myapp", jsonResponse + "");
        for(int i=0; i < rankedChampionObjects.size(); i++){
            Log.d("myapp", rankedChampionObjects.get(i).getId() + "\n" + rankedChampionObjects.get(i).getAggreStats() + "\n");
        }
    }

    public List<RankedStatsByIdObject> getRankedChampionObjects(){
        return rankedChampionObjects;
    }
}
