package api.objects;

import org.json.JSONObject;

/**
 * Created by Oscar on 3/17/2016.
 */
public class RankedStatsByIdObject {
    private int id;
    private JSONObject aggreStats;

    public RankedStatsByIdObject(int id, JSONObject aggreStats){
        this.id = id;
        this.aggreStats = aggreStats;
    }

    public int getId() {
        return id;
    }

    public JSONObject getAggreStats() {
        return aggreStats;
    }
}
