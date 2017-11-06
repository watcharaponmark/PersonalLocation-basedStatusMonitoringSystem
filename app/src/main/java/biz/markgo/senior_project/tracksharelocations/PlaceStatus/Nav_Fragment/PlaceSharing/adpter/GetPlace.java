package biz.markgo.senior_project.tracksharelocations.PlaceStatus.Nav_Fragment.PlaceSharing.adpter;
public class GetPlace {
    private String sharing_status ;
    private String place_id ;
    private String place_name;
    private String sub_district;
    private String district;
    private String province;
    private String zip_code;
    private String country_code;
    private String latitude;
    private String longitude;
    private String member_id ;

    public  GetPlace(){

    }

    public void setSharing_status(String sharing_status) {
        this.sharing_status = sharing_status;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    public void setPlace_name(String place_name) {
        this.place_name = place_name;
    }

    public void setSub_district(String sub_district) {
        this.sub_district = sub_district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public void setZip_code(String zip_code) {
        this.zip_code = zip_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getSharing_status() {
        return sharing_status;
    }

    public String getMember_id() {
        return member_id;
    }

    public String getPlace_id() {
        return place_id;
    }

    public String getPlace_name() {
        return place_name;
    }

    public String getSub_district() {
        return sub_district;
    }

    public String getDistrict() {
        return district;
    }

    public String getProvince() {
        return province;
    }

    public String getZip_code() {
        return zip_code;
    }

    public String getCountry_code() {
        return country_code;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

}
