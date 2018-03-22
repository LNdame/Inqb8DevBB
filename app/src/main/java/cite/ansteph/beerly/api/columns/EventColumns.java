package cite.ansteph.beerly.api.columns;

/**
 * Created by loicstephan on 2018/02/06.
 */

public interface EventColumns extends DataColumns {

    String      EST_ID ="id";
    String   NAME="name";
    String           ADDRESS="address";
    String    LIQUORLICENCE="liqour_license";
    String            HSLICENCE="hs_license";
    String    DATELASTINSPECTION="last_inspection_date";
    String           CONTACTPERSON="contact_person";
    String   CONTACTNUMBER="contact_number";
    String           URL="establishment_url";
    String    GEOTAG="geotag";
    String           DATECREATED="created_at";
    String   STATUS="status";
    String          LASTUPDATED="updated_at";
    String URLMAINPIC ="main_picture_url";
    String LATITUDE = "latitude";
    String LONGITUDE = "longitude";
}
