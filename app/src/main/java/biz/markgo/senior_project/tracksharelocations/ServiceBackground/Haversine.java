package biz.markgo.senior_project.tracksharelocations.ServiceBackground;

public class Haversine {

    public static final double R = 6371; // In kilometers

    public static double haversine(double lat1, double lng1, double lat2, double lng2) {

        lat1 = Math.toRadians(lat1);
        lng1 = Math.toRadians(lng1);
        lat2 = Math.toRadians(lat2);
        lng2 = Math.toRadians(lng2);

        double dlon = lng2 - lng1;
        double dlat = lat2 - lat1;
        double a = Math.pow((Math.sin(dlat/2)),2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dlon/2),2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        return R * c*(1000);
    }
    //haversine(14.5994205, 99.5400684, 14.599607499999998 , 99.54002734374998)
}
