package com.com.driver.webservice;

/**
 * Created by grepixinfotech on 22/07/16.
 */
public interface Constants {
    public static String SETTINGS_NAME="fhjadfjksahdfsdsa.czxch";
    //    String DRIVER_BASE_URL = "http://35.160.185.249/Taxi/index.php";
    String DRIVER_BASE_URL = "http://appicial.com/HireTaxi/1.0.1/index.php";

    String UPDATE_PROFILE = DRIVER_BASE_URL + "/driverapi/updatedriverprofile?";
    String FORGET_PASSWORD = DRIVER_BASE_URL + "/driverapi/forgetpassword?";
    String DRIVER_TRIP_HISTORY = DRIVER_BASE_URL + "/tripapi/gettrips?";
    String GET_CAR_CATEGORY = DRIVER_BASE_URL + "/categoryapi/getcategories?";
    String GET_CAR_NAMES = DRIVER_BASE_URL + "/carapi/getcars?";
    String GET_CATEGORIES = DRIVER_BASE_URL + "/categoryapi/getcategories?";
    String UPDATE_TRIP_URL = DRIVER_BASE_URL + "/tripapi/updatetrip?";
    String GET_TRIP_URL = DRIVER_BASE_URL + "/tripapi/gettrips?";
    String GET_NEAR_BY_USERS = DRIVER_BASE_URL + "/userapi/getnearbyuserlists?";
    String GET_CONSTANTS_API = DRIVER_BASE_URL + "/constantapi/getconstants?";
    String D_LICENSE_IMAGE_PATH = "driver_license";
    String D_PROFILE_IMAGE_PATH = "driver_image";
    String D_RC_IMAGE_PATH = "driver_rc";
    String D_INSURANCE_IMAGE_PATH = "driver_insurance";
    String IMAGE_BASE_URL = "http://appicial.com/HireTaxi/images/originals/";

    String CAR_DETAIL="http://appicial.com/sareeie/SareeirApp/cardata.php";





//    String IOS_NOTIFICATION_URL = "http://35.160.185.249/Taxi1.2/push/RiderPushNotification.php?";
//    String ANDROID_NOTIFICATION_URL = "http://35.160.185.249/Taxi1.2/push/RiderAndroidPushNotification.php?";

    String URL_COMMAN_NOTIFICATION = "http://appicial.com/HireTaxi/push/RiderAndroidIosPushNotification.php";
    String End_Trip = "End Trip";
    String Begin_Trip = "Begin Trip";
    String Arrive = "Arrive";
    String Accept_yes = "Accept yes";
    String Request = "request";
    String Home_Page = "Home Page";
    String Get_Request = "Get Request";

    public interface Urls {
        String URL_BASE = "http://appicial.com/HireTaxi/1.0.1/index.php";
        String URL_DRIVER_REGISTER = URL_BASE + "/driverapi/registration?";
        String URL_DRIVER_LOGIN = URL_BASE + "/driverapi/login?";
        String URL_ACCEPT_TRIP_REQUEST = URL_BASE + "/tripapi/tripaccept?";
        String URL_TO_SHARE = "https://play.google.com/store/apps/details?id=com.driver.hire_me&hl=en";
        String EMAIL_FOR_SUPPORT = "info@appicial.com";
        String URL_CREATE_TRIP = URL_BASE +"/tripapi/save?";
        String URL_USER_UPDATE_TRIP = URL_BASE + "/tripapi/updatetrip?";
        String URL_PAYMENT_SAVE = URL_BASE + "/paymentapi/save?";
    }

    public interface Keys {
        String Request = "request";
        String TRIP_ID = "trip_id";
        String DRIVER_ID = "driver_id";
        String TRIP_STATUS = "trip_status";
        String ANDROID = "android";
        String IOS = "ios";
        boolean IS_YSE_NO_DIALOG = true;


        String API_KEY = "api_key";
        String TRIP_DATE = "trip_date";
        String TRIP_PICK_LOC = "trip_from_loc";
        String TRIP_DEST_LOC = "trip_to_loc";
        String TRIP_PAY_AMOUNT = "trip_pay_amount";
        String TRIP_PAY_MODE = "trip_pay_mode";
        String TRIP_PAY_STATUS = "trip_pay_status";

        String TRIP_DISTANCE = "trip_distance";



    }


    public interface TripStatus {
        String ACCEPT = "accept";
        String ARRIVE = "arrive";
        String BEGIN = "begin";
        String END = "end";
        String REJECTED = "rejected";
        String DRIVER_CANCEL = "driver_cancel";

    }
    public interface Message{
        //TRIP MESSAGES
        String REQUEST = "Hey, You received a new Trip Request. Act Fast";
        String REJECTED = "Ohh, Your Trip Request has been cancelled. Try Again";
        String ACCEPTED = "Wow, Your Trip Request has been confirmed. Get Ready";
        String END = "Your trip completed successfully. Thanks";
        String ARRIVE = "Hey, Cab will arrive soon. Be Ready";
        String BEGIN = "Your trip has been started. Good Luck";
        String DRIVER_CANCEL = "Driver has cancelled the Trip. Ask him";
        String PAID = "Payment done. Thanks";
        String CASH = "Please collect cash from Rider.";
        String PAYPAL = "User made the payment.";

    }

}
