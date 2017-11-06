package biz.markgo.senior_project.tracksharelocations.PlaceStatus.Nav_Fragment.PlaceSharing.adpter;

public class GetSettingPlace {



    private String place_status_id;
    private String place_id ;
    private String name_status ;
    private String status_display;
    private String radius;
    private String day;
    private String start_time;
    private String end_time;
    private String online;
    private String real_place;
    private String offline;
    private String offline_mode;
    private String duration_time;
    private String to_time;
    private String out_place_online;
    private String sharing_status;
    private String secret_code;

    public void setReal_place(String real_place) {
        this.real_place = real_place;
    }

    public void setDuration_time(String duration_time) {
        this.duration_time = duration_time;
    }

    public void setTo_time(String to_time) {
        this.to_time = to_time;
    }

    public void setOffline_mode(String offline_mode) {
        this.offline_mode = offline_mode;
    }

    public void setOut_place_online(String out_place_online) {
        this.out_place_online = out_place_online;
    }

    public String getReal_place() {
        return real_place;
    }

    public String getOffline_mode() {
        return offline_mode;
    }

    public String getDuration_time() {
        return duration_time;
    }

    public String getTo_time() {
        return to_time;
    }

    public String getOut_place_online() {
        return out_place_online;
    }

    public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online;
    }

    public void setOffline(String offline) {
        this.offline = offline;
    }

    public String getOffline() {

        return offline;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setRadius(String radius) {
        this.radius = radius;
    }

    public String getRadius() {

        return radius;
    }

    public String getDay() {
        return day;

    }

    public  GetSettingPlace(){

    }

    public String getPlace_status_id() {
        return place_status_id;
    }

    public String getStatus_display() {
        return status_display;
    }

    public void setName_status(String name_status) {
        this.name_status = name_status;
    }

    public void setPlace_status_id(String place_status_id) {
        this.place_status_id = place_status_id;
    }

    public void setStatus_display(String status_display) {
        this.status_display = status_display;
    }

    public String getName_status() {
        return name_status;

    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public void setSharing_status(String sharing_status) {
        this.sharing_status = sharing_status;
    }

    public void setSecret_code(String secret_code) {
        this.secret_code = secret_code;
    }

    public String getPlace_id() {

        return place_id;
    }

    public String getStart_time() {
        return start_time;
    }

    public String getSharing_status() {
        return sharing_status;
    }

    public String getEnd_time() {
        return end_time;
    }

    public String getSecret_code() {
        return secret_code;
    }
}
