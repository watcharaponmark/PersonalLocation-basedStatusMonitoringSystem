package biz.markgo.senior_project.tracksharelocations.PlaceStatus.Nav_Fragment.PlaceSharing.SettingStatus.Time;

public class TimeDataset {
    private static String start_time  = "เริ่มต้น";
    private static String end_time = "สิ้นสุด" ;
    private static String duration_time = "0:15";

    public static String getStart_time() {
        return start_time;
    }

    public static String getEnd_time() {
        return end_time;
    }

    public static void setEnd_time(String end_time) {
        TimeDataset.end_time = end_time;
    }

    public static void setStart_time(String start_time) {
        TimeDataset.start_time = start_time;
    }

    public static String getDuration_time() {
        return duration_time;
    }

    public static void setDuration_time(String timer) {

        duration_time = timer;
    }
}
