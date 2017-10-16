package cite.ansteph.beerly.model;

/**
 * Created by loicstephan on 2017/10/13.
 */

public class Establishment {

    int id;

    String name, address;


    byte [] logo;

    EstablType establType;

    public Establishment() {
    }


    public Establishment(int id, String name, String address, byte[] logo, EstablType establType) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.logo = logo;
        this.establType = establType;
    }


    public Establishment(int id, String name, String address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }

    public Establishment(String name, String address) {
        this.name = name;
        this.address = address;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public byte[] getLogo() {
        return logo;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    public EstablType getEstablType() {
        return establType;
    }

    public void setEstablType(EstablType establType) {
        this.establType = establType;
    }
}
