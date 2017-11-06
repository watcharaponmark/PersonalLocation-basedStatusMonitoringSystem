package biz.markgo.senior_project.tracksharelocations.API;

public class URL_API {

    private static final String API_member_status_check = "http://api-location-monitoring.markgo.biz/member/member_status_check.php";
    //MemberStatusCheck1234
    private static final String API_register = "http://api-location-monitoring.markgo.biz/member/register.php";
    //register1234
    private static final String API_get_member = "http://api-location-monitoring.markgo.biz/member/get_member.php";
    //getMember1234
    private static final String API_add_newplaces = "http://api-location-monitoring.markgo.biz/place/add_newplaces.php";
    //Mark13942180
    private static final String API_get_place = "http://api-location-monitoring.markgo.biz/place/get_place.php";
    //getplace1234
    private static final String API_get_settingplace = "http://api-location-monitoring.markgo.biz/place/get_statusplace.php";
    //getSettingplace1234
    private static final String API_find_tracking = "http://api-location-monitoring.markgo.biz/tracking/find_tracking.php";
    //FindTracking1234
    private static final String API_get_place_tracking = "http://api-location-monitoring.markgo.biz/tracking/get_place_tracking.php";
    //getplace1234
    private static final String API_update_profile = "http://api-location-monitoring.markgo.biz/member/profile_update.php";
    //ProfileUpdate1234
    private static final String API_add_statusPlace = "http://api-location-monitoring.markgo.biz/place/add_statusplace.php";
    //Mark13942180
    private static final String API_delete_place = "http://api-location-monitoring.markgo.biz/place/delete_place.php";
    //DeletePlace1234
    private static final String API_delete_statusplace = "http://api-location-monitoring.markgo.biz/place/delete_statusplace.php";
   //DeleteSettingPlace1234
    private static final String API_update_statusplace = "http://api-location-monitoring.markgo.biz/place/update_statusplace.php";
    //UpdateStatusPlace1234
    private static final String API_update_place = "http://api-location-monitoring.markgo.biz/place/update_place.php";
    //UpdatePlace1234
    private static final String API_tracking_place = "http://api-location-monitoring.markgo.biz/tracking/tracking_place.php";
    //TrackingPlace1234
    private static final String API_delete_tracking_place = "http://api-location-monitoring.markgo.biz/tracking/delete_tracking_place.php";
    //DeleteTrakingPlace1234
    private static final String API_get_place_tracking_All = "http://api-location-monitoring.markgo.biz/tracking/get_place_traking_all.php";
    //getplace1234
    private static final String API_UpdateToken_FCM = "http://api-location-monitoring.markgo.biz/FirebaseNoti/update_token_fcm.php";
    //Token_FCMUpdate1234
    private static final String API_send_noti_FCM = "http://api-location-monitoring.markgo.biz/FirebaseNoti/send_noti.php";
    //send_noti_FCM1234
    private static final String API_delete_account = "http://api-location-monitoring.markgo.biz/member/delete_account.php";
    //DeleteAccount1234
    private static final String API_get_place_tracking_secret = "http://api-location-monitoring.markgo.biz/tracking/get_place_tracking_secret.php";
    //getPlaceTrackingSecret1234


    public static String getAPI_get_place_tracking_secret() {
        return API_get_place_tracking_secret;
    }
    public static String getAPI_delete_account() {
        return API_delete_account;
    }

    public static String getAPI_send_noti_FCM() {
        return API_send_noti_FCM;
    }

    public static String getAPI_UpdateToken_FCM() {
        return API_UpdateToken_FCM;
    }

    public static String getAPI_get_place_tracking_All() {
        return API_get_place_tracking_All;
    }

    public static String getAPI_delete_tracking_place() {
        return API_delete_tracking_place;
    }

    public static String getAPI_tracking_place() {
        return API_tracking_place;
    }

    public static String getAPI_update_place() {
        return API_update_place;
    }

    public static String getAPI_delete_statusplace() {
        return API_delete_statusplace;
    }

    public static String getAPI_add_statusPlace() {
        return API_add_statusPlace;
    }

    public static String getAPI_update_statusplace() {
        return API_update_statusplace;
    }

    public static String getAPI_member_status_check() {
        return API_member_status_check;
    }

    public static String getAPI_register() {
        return API_register;
    }

    public static String getAPI_get_settingplace() {
        return API_get_settingplace;
    }

    public static String getAPI_get_member() {
        return API_get_member;
    }

    public static String getAPI_delete_place() {
        return API_delete_place;
    }

    public static String getAPI_add_newplaces() {
        return API_add_newplaces;
    }

    public static String getAPI_get_place() {
        return API_get_place;
    }

    public static String getAPI_find_tracking() {
        return API_find_tracking;
    }

    public static String getAPI_get_place_tracking() {
        return API_get_place_tracking;
    }

    public static String getAPI_update_profile() {
        return API_update_profile;
    }
}


//######################################################################################
//send Noti
//url https://fcm.googleapis.com/fcm/send  : POST
//head
//
//Authorization  : key=AAAAZS04TbM:APA91bGnoyhrgGqq2aSVb_jf-ucFqrgUgXA0iTql7ZWbGzUD-HNPNL6T91FodneWmtp4vWip3A0AN9yrDbnwKUB-a3dp0A0r5XRu_CExgHjW6eU_Q59aYk1s7tMoMsBsuWkw1nFWrnQE Content-Type
//        Content-Type : application/json
//body
//{
//        "to" : "cQqHj-WZ-Fk:APA91bFrJTFkZUWtPgejT0ck_vHbuQ75yHmguNomVlIZw9XhrXFg8Rz98apzR544UcSA-HwsKVgDdAioscgAXu43VHqZWv0Q4TA5-360ab8wBTQEpZ0nQ9G37J4DLOJVBxPVoxt8MaYK"
//        "notification" : {
//        "body" : "ฆ่ามันซะ !!!",
//        "title" : "เจ้ามังกรของข้า",
//        "icon" : "ic_launcher"
//        "color":"#f1c40f"
//        }
//        }
//
//
//        {
//        "registration_ids":["Token1","Token2","Token3"],
//        "priority" : "high",
//        "notification" : {
//        "body" : "great match!",
//        "title" : "Portugal vs. Denmark",
//        "icon" : "myicon"
//        }
//        }
//######################################################################################