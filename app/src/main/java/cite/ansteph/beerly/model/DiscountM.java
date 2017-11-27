package cite.ansteph.beerly.model;

/**
 * Created by loicstephan on 2017/11/27.
 */

public class DiscountM {

    int firebaseID, establishmentID;

    String establishmentName, timecreated;

    public DiscountM() {
    }

    public DiscountM(int firebaseID, int establishmentID, String establishmentName, String timecreated) {
        this.firebaseID = firebaseID;
        this.establishmentID = establishmentID;
        this.establishmentName = establishmentName;
        this.timecreated = timecreated;
    }

    public DiscountM(String establishmentName, String timecreated) {
        this.establishmentName = establishmentName;
        this.timecreated = timecreated;
    }

    public int getFirebaseID() {
        return firebaseID;
    }

    public void setFirebaseID(int firebaseID) {
        this.firebaseID = firebaseID;
    }

    public int getEstablishmentID() {
        return establishmentID;
    }

    public void setEstablishmentID(int establishmentID) {
        this.establishmentID = establishmentID;
    }

    public String getEstablishmentName() {
        return establishmentName;
    }

    public void setEstablishmentName(String establishmentName) {
        this.establishmentName = establishmentName;
    }

    public String getTimecreated() {
        return timecreated;
    }

    public void setTimecreated(String timecreated) {
        this.timecreated = timecreated;
    }
}
