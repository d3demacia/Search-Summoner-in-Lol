package api.calls;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import com.anidev.gabo.lolsummonersearch.MainActivity;

/**
 * Static Data
 * Created by Oscar on 3/20/2016.
 */
public class ProfileIcon extends AsyncTask<Void, Void, Bitmap> {
    private AsyncCallback asyncCallback;
    private URL url;
    private Bitmap profileIcon;
    private boolean success;

    public Bitmap getProfileIcon() {
        return profileIcon;
    }

    public void setProfileIcon(Bitmap profileIcon) {
        this.profileIcon = profileIcon;
    }

    public boolean isSuccess() {
        return success;
    }

    public interface AsyncCallback{
        void profileIconCallBack();
    }
    public void registerCallBack(AsyncCallback cb){
        this.asyncCallback = cb;
    }

    public ProfileIcon(int profileIconId){
        try {
            url = new URL("http://ddragon.leagueoflegends.com/cdn/" + MainActivity.imageVersion + "/img/profileicon/" + profileIconId + ".png");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        this.success = false;
    }

    //Get image
    @Override
    protected Bitmap doInBackground(Void... params) {
        Bitmap icon = null;
        try {
            icon = BitmapFactory.decodeStream((InputStream) url.getContent());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return icon;
    }

    @Override
    protected void onPostExecute(Bitmap bm){
        if(bm != null){
            setProfileIcon(bm);
            success = true;
        }
        asyncCallback.profileIconCallBack();
    }



}
