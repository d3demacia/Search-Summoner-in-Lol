package api.calls;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import com.anidev.gabo.lolsummonersearch.MainActivity;

/**
 * /api/lol/{region}/v1.4/summoner/by-name/{summonerNames}
 * Created by Oscar on 3/16/2016.
 */
public class SummonerByName extends AsyncTask<Void, Void, Void> {

    private URL mUrl;
    private String jsonResponse;
    private AsyncCallback asyncCallback;
    //returned
    private String name;
    private long id;
    private long revisionDate;
    private int profileIconId;
    private long level;
    private boolean isSuccess;

    public interface AsyncCallback{
        void summonerCallBack();
    }

    public void registerCallBack(AsyncCallback cb){
        this.asyncCallback = cb;
    }

    public SummonerByName(String name){
        this.isSuccess = false;
        this.name = name;
        //create the url to access
        try {
            this.mUrl = new URL("https://"+MainActivity.mRegionCode+".api.pvp.net/api/lol/"+MainActivity.mRegionCode+"/v1.4/summoner/by-name/"+name+"?api_key="+ MainActivity.API_KEY);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Void doInBackground(Void... params) {
        loadJSON(mUrl);
        return null;
    }

    @Override
    protected void onPostExecute(Void v){
        //parse info
        if(jsonResponse != null){
            isSuccess = true;
            try {
                JSONObject summOb = new JSONObject(jsonResponse).getJSONObject(name);
                this.id = summOb.getLong("id");
                this.name = summOb.getString("name");
                this.revisionDate = summOb.getLong("revisionDate");
                this.profileIconId = summOb.getInt("profileIconId");
                this.level = summOb.getLong("summonerLevel");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else{
            Log.d("myapp", "SummonerByName jsonResponse was null");
            isSuccess = false;
        }
        asyncCallback.summonerCallBack();
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

    public String getJsonResponse(){
        return jsonResponse;
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    public long getRevisionDate() {
        return revisionDate;
    }

    public int getProfileIconId() {
        return profileIconId;
    }

    public long getLevel() {
        return level;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void printInfo(){
        Log.d("myapp", jsonResponse + "");
        Log.d("myapp", getName() + "\n" +
                        getId() + "\n" +
                        getLevel() + "\n" +
                        getProfileIconId() + "\n" +
                        getRevisionDate());
    }
}
