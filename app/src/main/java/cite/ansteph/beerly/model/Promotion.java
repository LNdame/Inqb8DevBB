package cite.ansteph.beerly.model;

/**
 * Created by loicstephan on 2017/10/19.
 */

public class Promotion {


    int id, establishment_id,beer_id;
    String     title;
    String start_date,end_date,status;
    double price;

    Beer beer;

    public Promotion() {
    }

    public Promotion(int id, int establishment_id, int beer_id, String title, String start_date, String end_date, String status, double price) {
        this.id = id;
        this.establishment_id = establishment_id;
        this.beer_id = beer_id;
        this.title = title;
        this.start_date = start_date;
        this.end_date = end_date;
        this.status = status;
        this.price = price;
    }

    public Promotion(String title, String start_date, String end_date, double price, Beer beer) {
        this.title = title;
        this.start_date = start_date;
        this.end_date = end_date;
        this.price = price;
        this.beer = beer;
    }

    public Promotion(int establishment_id, int beer_id, String title, String start_date, String end_date, String status, double price) {
        this.establishment_id = establishment_id;
        this.beer_id = beer_id;
        this.title = title;
        this.start_date = start_date;
        this.end_date = end_date;
        this.status = status;
        this.price = price;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEstablishment_id() {
        return establishment_id;
    }

    public void setEstablishment_id(int establishment_id) {
        this.establishment_id = establishment_id;
    }

    public int getBeer_id() {
        return beer_id;
    }

    public void setBeer_id(int beer_id) {
        this.beer_id = beer_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Beer getBeer() {
        return beer;
    }

    public void setBeer(Beer beer) {
        this.beer = beer;
    }
}
