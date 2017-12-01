package cite.ansteph.beerly.api.columns;

/**
 * Created by loicstephan on 2017/10/19.
 */

public interface BeerLoversColumns extends DataColumns {






   String ID = "id";
    String         USER_ID = "user_id";
String STATUS ="status";
    String FIRST_NAME = "first_name";
    String MIDDLE_NAME = "middle_name";
    String LAST_NAME = "last_name";
    String EMAIL = "email";
    String USERNAME = "username";

    String  DATE_OF_BIRTH ="date_of_birth";
    String  TERMS_CONDITIONS_ACCEPT="terms_conditions_accept";
    String  CREATED_AT="created_at";
    String  UPDATED_AT="updated_at";
    String  DELETED_AT="deleted_at";
    String  GENDER="gender";
    String HOME_CITY="home_city";
    String REFERRAL_CODE="referal_code";
    String FIREBASE_ID="firebase_id";
    String COCKTAIL="cocktail";
    String        COCKTAIL_TYPE="cocktail_type";
    String SHOT="shot";
    String         SHOT_TYPE="shot_type";
    String         INVITATION_CODE="invitation_code";





}


/*`id` int(10) UNSIGNED NOT NULL,
  `user_id` int(10) UNSIGNED NOT NULL,
  `status` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `date_of_birth` datetime DEFAULT CURRENT_TIMESTAMP,
  `terms_conditions_accept` tinyint(1) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `deleted_at` timestamp NULL DEFAULT NULL,
  `gender` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `home_city` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `referal_code` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `firebase_id` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `cocktail` tinyint(1) DEFAULT NULL,
  `cocktail_type` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `shot` tinyint(1) DEFAULT NULL,
  `shot_type` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL */