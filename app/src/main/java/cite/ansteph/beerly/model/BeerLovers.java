package cite.ansteph.beerly.model;

import java.io.Serializable;

/**
 * Created by loicstephan on 2017/10/19.
 */

public class BeerLovers implements Serializable {



    int id, user_id;

    String status, date_of_birth,gender,home_city,firebase_id, first_name,last_name,username,email;

    String created_at;

    /**
     * research criteria*/

    int shot, cocktail ;
    String shotType, cocktailType, referralCode;




    public BeerLovers() {
    }

    public BeerLovers(int id, int user_id, String status, String date_of_birth, String gender, String home_city, String firebase_id, String first_name, String last_name, String username, String email) {
        this.id = id;
        this.user_id = user_id;
        this.status = status;
        this.date_of_birth = date_of_birth;
        this.gender = gender;
        this.home_city = home_city;
        this.firebase_id = firebase_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.username = username;
        this.email = email;
    }


    public BeerLovers(int user_id, String status, String date_of_birth, String gender, String home_city, String firebase_id, String first_name, String last_name, String username, String email) {
        this.user_id = user_id;
        this.status = status;
        this.date_of_birth = date_of_birth;
        this.gender = gender;
        this.home_city = home_city;
        this.firebase_id = firebase_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.username = username;
        this.email = email;
    }


    public BeerLovers(int user_id, String date_of_birth, String gender, String home_city, String firebase_id, String first_name, String last_name, String username, String email) {
        this.user_id = user_id;
        this.date_of_birth = date_of_birth;
        this.gender = gender;
        this.home_city = home_city;
        this.firebase_id = firebase_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.username = username;
        this.email = email;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getHome_city() {
        return home_city;
    }

    public void setHome_city(String home_city) {
        this.home_city = home_city;
    }

    public String getFirebase_id() {
        return firebase_id;
    }

    public void setFirebase_id(String firebase_id) {
        this.firebase_id = firebase_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }


    public int getShot() {
        return shot;
    }

    public void setShot(int shot) {
        this.shot = shot;
    }

    public int getCocktail() {
        return cocktail;
    }

    public void setCocktail(int cocktail) {
        this.cocktail = cocktail;
    }

    public String getShotType() {
        return shotType;
    }

    public void setShotType(String shotType) {
        this.shotType = shotType;
    }

    public String getCocktailType() {
        return cocktailType;
    }

    public void setCocktailType(String cocktailType) {
        this.cocktailType = cocktailType;
    }

    public String getReferralCode() {
        return referralCode;
    }

    public void setReferralCode(String referralCode) {
        this.referralCode = referralCode;
    }
}
