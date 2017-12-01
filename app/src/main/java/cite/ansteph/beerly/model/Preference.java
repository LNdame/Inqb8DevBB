package cite.ansteph.beerly.model;

/**
 * Created by loicstephan on 2017/10/19.
 */

public class Preference {

  int  id, beer_lover_id, beer_id, preference_number;
     String       name, vendor;


    public Preference() {
    }

    public Preference(int id, int beer_lover_id, int beer_id, int preference_number, String name, String vendor) {
        this.id = id;
        this.beer_lover_id = beer_lover_id;
        this.beer_id = beer_id;
        this.preference_number = preference_number;
        this.name = name;
        this.vendor = vendor;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBeer_lover_id() {
        return beer_lover_id;
    }

    public void setBeer_lover_id(int beer_lover_id) {
        this.beer_lover_id = beer_lover_id;
    }

    public int getBeer_id() {
        return beer_id;
    }

    public void setBeer_id(int beer_id) {
        this.beer_id = beer_id;
    }

    public int getPreference_number() {
        return preference_number;
    }

    public void setPreference_number(int preference_number) {
        this.preference_number = preference_number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }
}
