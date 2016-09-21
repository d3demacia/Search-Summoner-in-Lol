package api.calls;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import api.objects.ChampionRankedObject;
import com.anidev.gabo.lolsummonersearch.MainActivity;

/**
 * Static Data
 * Created by Oscar on 3/20/2016.
 */
public class AllChampionsSquareImage extends AsyncTask<Void, Void, Void> {
    private AsyncCallback asyncCallback;
    private Map<Integer, URL> championImageIdsMap;
    private Map<Integer, Bitmap> championIdBitmapMap;
    private boolean success;

    public boolean isSuccess() {
        return success;
    }

    public AllChampionsSquareImage(List<ChampionRankedObject> champObjects){
        success = false;
        this.championImageIdsMap = new HashMap<>();
        this.championIdBitmapMap = new HashMap<>();
        try {
            for(int i=0; i < champObjects.size(); i++){
                if(champObjects.get(i).getId() != 0){
                    championImageIdsMap.put(champObjects.get(i).getId(), new URL("http://ddragon.leagueoflegends.com/cdn/" + MainActivity.imageVersion + "/img/champion/" + champObjects.get(i).getKey() + ".png"));
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        this.success = false;
    }

    @Override
    protected Void doInBackground(Void... params) {
        int i = 0; //count images obtained
        //Traverse map and obtain images, place into new map to preserve id
        for (Object o : championImageIdsMap.entrySet()) {
            Map.Entry pair = (Map.Entry) o;
            if ((Integer) pair.getKey() != 0) {
                try {
                    championIdBitmapMap.put((Integer) pair.getKey(), BitmapFactory.decodeStream((InputStream) ((URL) pair.getValue()).getContent()));
                    i++;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Log.d("myapp", i + " champion images obtained.");
        return null;
    }

    @Override
    protected void onPostExecute(Void v){
        success = true;
        asyncCallback.allChampionSquaresCallBack();
    }

    public interface AsyncCallback{
        void allChampionSquaresCallBack();
    }

    public Map<Integer, Bitmap> getChampionIdBitmapMap() {
        return championIdBitmapMap;
    }

    public void registerCallBack(AsyncCallback cb){
        this.asyncCallback = cb;
    }
}
