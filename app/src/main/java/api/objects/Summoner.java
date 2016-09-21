package api.objects;

import android.graphics.Bitmap;

/**
 *
 * Created by Oscar on 3/22/2016.
 */
public class Summoner {
    private String formattedName;
    private long id;
    private long revisionDate;
    private int profileIconId;
    private long level;
    private String division;
    private String tier;
    private long ranked_solo_wins;
    private long ranked_solo_losses;
    private Bitmap profileIcon;

    public Summoner(String formattedName, long id, long revisionDate, int profileIconId, long level) {
        this.formattedName = formattedName;
        this.id = id;
        this.revisionDate = revisionDate;
        this.profileIconId = profileIconId;
        this.level = level;
    }

    public String getFormattedName() {
        return formattedName;
    }

    public void setFormattedName(String formattedName) {
        this.formattedName = formattedName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getRevisionDate() {
        return revisionDate;
    }

    public void setRevisionDate(long revisionDate) {
        this.revisionDate = revisionDate;
    }

    public int getProfileIconId() {
        return profileIconId;
    }

    public void setProfileIconId(int profileIconId) {
        this.profileIconId = profileIconId;
    }

    public long getLevel() {
        return level;
    }

    public void setLevel(long level) {
        this.level = level;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getTier() {
        return tier;
    }

    public void setTier(String tier) {
        this.tier = tier;
    }

    public long getRanked_solo_wins() {
        return ranked_solo_wins;
    }

    public void setRanked_solo_wins(long ranked_solo_wins) {
        this.ranked_solo_wins = ranked_solo_wins;
    }

    public long getRanked_solo_losses() {
        return ranked_solo_losses;
    }

    public void setRanked_solo_losses(long ranked_solo_losses) {
        this.ranked_solo_losses = ranked_solo_losses;
    }

    public Bitmap getProfileIcon() {
        return profileIcon;
    }

    public void setProfileIcon(Bitmap profileIcon) {
        this.profileIcon = profileIcon;
    }
}
