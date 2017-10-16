package cite.ansteph.beerly.model;

/**
 * Created by loicstephan on 2017/10/03.
 */

public class Beer {

    int id;

    String name;

    public Beer() {
    }

    public Beer(int id, String name) {
        this.id = id;
        this.name = name;
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
}
