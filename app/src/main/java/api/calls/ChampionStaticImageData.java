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
 * /api/lol/static-data/{region}/v1.2/champion
 * dataById = true
 * champData = image
 * Created by Oscar on 3/17/2016.
 */
public class ChampionStaticImageData extends AsyncTask<Void, Void, Void> {
    private AsyncCallback asyncCallback;
    private URL url;
    private boolean success;

    public String getJsonResponse() {
        return jsonResponse;
    }

    private String jsonResponse;

    public boolean isSuccess() {
        return success;
    }

    @Override
    protected Void doInBackground(Void... params) {
        loadJSON(url);
        return null;
    }

    @Override
    protected void onPostExecute(Void v) {
        //parse info
        if (jsonResponse != null) {
            try {
                JSONObject jsonOb = new JSONObject(jsonResponse);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            success = true;
        } else {
            Log.d("myapp", "ChampionStaticImageData jsonResponse was null");
            success = false;
        }
        asyncCallback.championStaticImageDataCallBack();
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

    public interface AsyncCallback{
        void championStaticImageDataCallBack();
    }

    public void registerCallBack(AsyncCallback cb){
        this.asyncCallback = cb;
    }

    public ChampionStaticImageData(String region){
        try {
            url = new URL("https://global.api.pvp.net/api/lol/static-data/"+region+"/v1.2/champion?dataById=true&champData=image&api_key="+ MainActivity.API_KEY);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
