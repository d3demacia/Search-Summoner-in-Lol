package api.calls;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import com.anidev.gabo.lolsummonersearch.MainActivity;

/**
 * /api/lol/static-data/{region}/v1.2/versions
 * Created by Oscar on 3/16/2016.
 */
public class Versions extends AsyncTask<Void, Void, Void> {

    private URL mUrl;
    private String jsonResponse;
    private AsyncCallback asyncCallback;
    private JSONArray versionsJsonArray;
    private boolean success;

    public interface AsyncCallback{
        void versionsCallBack();
    }

    public void registerCallBack(AsyncCallback cb){
        this.asyncCallback = cb;
    }

    public Versions(String region){
        //create the url to access
        try {
            this.mUrl = new URL("https://global.api.pvp.net/api/lol/static-data/"+region+"/v1.2/versions?api_key=" + MainActivity.API_KEY);
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
        if(jsonResponse != null){
            //parse info
            try {
                versionsJsonArray = new JSONArray(jsonResponse);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            success = true;
        }
        else{
            Log.d("myapp", "Versions jsonResponse was null");
            success = false;
        }
        asyncCallback.versionsCallBack();
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

    public JSONArray getVersionsJsonArray() {
        return versionsJsonArray;
    }

    public boolean isSuccess() {
        return success;
    }

    public void printInfo(){
        try {
            Log.d("myapp", versionsJsonArray.get(0).toString() + ", " + versionsJsonArray.get(1).toString() + ", ...");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
