package cite.ansteph.beerly.model;

/**
 * Created by loicstephan on 2017/10/03.
 */

public class Beer {

    int id;

    String name , description, vendor;

    double percentage;

    public Beer() {
    }

    public Beer(int id, String name) {
        this.id = id;
        this.name = name;
    }


    public Beer(int id, String name, String description, String vendor, double percentage) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.vendor = vendor;
        this.percentage = percentage;
    }

    public Beer(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }
}
