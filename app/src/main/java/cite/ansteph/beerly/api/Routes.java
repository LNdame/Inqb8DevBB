package cite.ansteph.beerly.api;

/**
 * Created by loicstephan on 2017/10/19.
 */

public interface Routes {

    /****************************************************---Domain---*************************************************************/

    //String DOMAIN="http://10.0.0.6/proimakapi/v1/";
    String DOMAIN="http://ec2-13-58-20-88.us-east-2.compute.amazonaws.com/";
    // String DOMAIN="https://inqb8.co.za/courses/";
    String API_VERSION="api/";
    String COU_VERSION="course_v1/";

    /****************************************************---Route---*************************************************************/

    String URL_REGISTER_BEERLOVERS = DOMAIN+API_VERSION+"store_beer_lover";
    String URL_RETRIEVE_EST=DOMAIN+API_VERSION+ "get_establishments"    ;//     DOMAIN+CA_VERSION+"register_candidate";

    String URL_RETRIEVE_LOVER_PROFILE=DOMAIN+API_VERSION+ "get_beer_lover/%s"    ;


    /****************************************************---COMMON MESSAGE---*************************************************************/

    public static final String ERROR_RESPONSE= "error";
    public static final String MSG_RESPONSE= "message";
    public static final String KEY_OTP = "otp";
}
