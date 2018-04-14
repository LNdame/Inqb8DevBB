package cite.ansteph.beerly.api.columns;

/**
 * Created by loicstephan on 2018/02/06.
 */

public interface EventColumns extends DataColumns {

    String      EST_ID ="id";
    String   TITLE="title";
    String           STARTDATE="start_date";
    String           ENDDATE="end_date";

    String           ADDRESS="address";
    String    LIQUORLICENCE="liqour_license";
    String            HSLICENCE="hs_license";
    String    DESCRIPTION="description";
    String           CONTACTPERSON="contact_person";
    String   CONTACTNUMBER="contact_number";
    String           URL="event_url";

    String           DATECREATED="created_at";
    String   STATUS="status";
    String          LASTUPDATED="updated_at";
    String URLMAINPIC ="main_picture_url";
    String PIC2 ="picture_2";
    String PIC3 ="picture_3";
    String LATITUDE = "latitude";
    String LONGITUDE = "longitude";



    /*
    "id": 1,
            "title": "Test Eventnnn",
            "start_date": "2018-02-12 07:00:00",
            "end_date": "2018-02-14 07:00:00",
            "status": "active",
            "description": "Test event",
            "deleted_at": null,
            "created_at": "2018-02-12 04:01:27",
            "updated_at": "2018-02-12 04:42:16",
            "longitude": "111111",
            "latitude": "111111",
            "contact_person": "Test",
            "contact_number": "Testing",
            "main_picture_url": "photos/5a811197b46e0.jpg",
            "address": "Testing",
            "event_url": "www.herald.co.zw",
            "picture_2": null,
            "picture_3": null */
}
