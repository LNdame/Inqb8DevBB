package cite.ansteph.beerly.model;

import java.io.Serializable;

/**
 * Created by loicstephan on 2018/02/06.
 */

public class BeerlyEvent implements Serializable {

    int id;

    String name, address,liqour_license,last_inspection_date, contact_person, contact_number ;
    String establishment_url, latitude,longitude,status;
    String startDate, endDate;
    String description;


    byte [] logo;

    EstablType establType;


    String main_picture_url, picture_2_url, picture_3_url;


    ///extra criteria
    int promoNumber ;


    public BeerlyEvent() {
    }


    public BeerlyEvent(int id, String name, String address, String liqour_license, String last_inspection_date, String contact_person, String contact_number, String establishment_url, String latitude, String longitude, String status, byte[] logo, EstablType establType, String main_picture_url, String picture_2_url, String picture_3_url, int promoNumber) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.liqour_license = liqour_license;
        this.last_inspection_date = last_inspection_date;
        this.contact_person = contact_person;
        this.contact_number = contact_number;
        this.establishment_url = establishment_url;
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = status;
        this.logo = logo;
        this.establType = establType;
        this.main_picture_url = main_picture_url;
        this.picture_2_url = picture_2_url;
        this.picture_3_url = picture_3_url;
        this.promoNumber = promoNumber;
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

    public String getLiqour_license() {
        return liqour_license;
    }

    public void setLiqour_license(String liqour_license) {
        this.liqour_license = liqour_license;
    }

    public String getLast_inspection_date() {
        return last_inspection_date;
    }

    public void setLast_inspection_date(String last_inspection_date) {
        this.last_inspection_date = last_inspection_date;
    }

    public String getContact_person() {
        return contact_person;
    }

    public void setContact_person(String contact_person) {
        this.contact_person = contact_person;
    }

    public String getContact_number() {
        return contact_number;
    }

    public void setContact_number(String contact_number) {
        this.contact_number = contact_number;
    }

    public String getEstablishment_url() {
        return establishment_url;
    }

    public void setEstablishment_url(String establishment_url) {
        this.establishment_url = establishment_url;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getMain_picture_url() {
        return main_picture_url;
    }

    public void setMain_picture_url(String main_picture_url) {
        this.main_picture_url = main_picture_url;
    }

    public String getPicture_2_url() {
        return picture_2_url;
    }

    public void setPicture_2_url(String picture_2_url) {
        this.picture_2_url = picture_2_url;
    }

    public String getPicture_3_url() {
        return picture_3_url;
    }

    public void setPicture_3_url(String picture_3_url) {
        this.picture_3_url = picture_3_url;
    }

    public int getPromoNumber() {
        return promoNumber;
    }

    public void setPromoNumber(int promoNumber) {
        this.promoNumber = promoNumber;
    }


    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
