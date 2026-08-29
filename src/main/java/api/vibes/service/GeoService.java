/*
 * 
 */
package api.vibes.service;


import org.springframework.stereotype.Service;

/**
 * Service for Geo.
 * <p>
 * 
 */
@Service
public class GeoService {


    public double H(double lat1, double lon1, double lat2, double lon2)
    {
        // distance between latitudes and longitudes
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
 
        // convert to radians
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);
 
        // apply formulae
        double a = Math.pow(Math.sin(dLat / 2), 2) +
            Math.pow(Math.sin(dLon / 2), 2) *
            Math.cos(lat1) *
            Math.cos(lat2);
        double rad = 6371;
        double c = 2 * Math.asin(Math.sqrt(a));
        return rad * c;
    }

    public double H(double[] latLon1, double[] latLon2)
    {
        return H(latLon1[0], latLon1[1], latLon2[0], latLon2[1]);
    }

    public double kmToMeters(double kilometers) {
        double meter = kilometers * 1000.0;
        // double centimeter = kilometers * 100000.0;
		// double millimeter = kilometers * 1000000.0;
        return meter;
    }

    public double kmToMin(double kilometers) {
        /** 1 km/h = 16.66667 m/min; */
        double kmmin = 16.66667;
        return (kilometers * kmmin);
    }

    /* \/
    * dis(meters(point-person, center-local-point)) < dis(meters(center-local-point, max-point-local))
    * */
    public boolean maxProxRadiusPointLocaltion(double[] centerLocalPoint, double[] maxLocalPoint, double[] pointPerson) {
        double disCenterMaxPoint = kmToMeters(H(centerLocalPoint, maxLocalPoint));
        double disPersonPointer = kmToMeters(H(centerLocalPoint, pointPerson));
        return (disPersonPointer < disCenterMaxPoint);
    }

    /* \/
    * dis(M(point-person, center-local-point)) < dis(M(center-local-point, max-point-local)) &&
    * dis(M(point-person, center-local-point)) < dis(M(center-local-point, min-point-local))
    * */
    public boolean innerRadiusPointsLocal(double[] centerLocalPoint, double[] minToCenterLocalPoint, double[] maxToCenterLocalPoint, double[] pointPerson) {
        return (
            maxProxRadiusPointLocaltion(centerLocalPoint, maxToCenterLocalPoint, pointPerson) &&
            maxProxRadiusPointLocaltion(centerLocalPoint, minToCenterLocalPoint, pointPerson)
        );
    }

    /* \/
    * mid -> (extreme-point-1, extreme-point-2)
    * dis_m_person -> dis(M(point-person, mid))
    * dis_m_person < dis(M(mid, extreme-point-1)) &&
    * dis_m_person < dis(M(mid, extreme-point-2))
    * */
    public boolean innerRadiusExtremePointsLocal(double[] minToCenterLocalPoint, double[] maxToCenterLocalPoint, double[] pointPerson) {
        double[] centerLocalPoint = midPoint(maxToCenterLocalPoint, minToCenterLocalPoint);
        return (
            maxProxRadiusPointLocaltion(centerLocalPoint, maxToCenterLocalPoint, pointPerson) &&
            maxProxRadiusPointLocaltion(centerLocalPoint, minToCenterLocalPoint, pointPerson)
        );
    }

    /**
     * middle -> (Lat-Long-1 ----------[*]---------- Lat-Long-2)
     */
    private double[] midPoint(double lat1, double lon1, double lat2, double lon2) {
        double dLon = Math.toRadians(lon2 - lon1);

        //convert to radians
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);
        lon1 = Math.toRadians(lon1);

        double Bx = Math.cos(lat2) * Math.cos(dLon);
        double By = Math.cos(lat2) * Math.sin(dLon);
        double lat3 = Math.atan2(Math.sin(lat1) + Math.sin(lat2), Math.sqrt((Math.cos(lat1) + Bx) * (Math.cos(lat1) + Bx) + By * By));
        double lon3 = lon1 + Math.atan2(By, Math.cos(lat1) + Bx);

        //print out in degrees
        // System.out.println(Math.toDegrees(lat3) + " " + Math.toDegrees(lon3));
        return new double[]{Math.toDegrees(lat3), Math.toDegrees(lon3)};
    }

    private double[] midPoint(double[] latLon1, double[] latLon2) {
        return midPoint(latLon1[0], latLon1[1], latLon2[0], latLon2[1]);
    }

    public void testes(){

        /*\/ points unid; */
        double[] centerLocalPoint = new double[]{-10.914093, -37.090636};
        double[] maxLocalPoint = new double[]{-10.915913, -37.090830};
        double[] minLocalPoint = new double[]{-10.912945, -37.090431};


        // double[] personpoint = new double[]{-10.913040, -37.089658}; // << fora;
        // double[] personpoint = new double[]{-10.914359, -37.089348}; // << dentro;
        double[] personpoint = new double[]{-10.913453, -37.091828}; // << dentro;
        // double[] personpoint = new double[]{-10.915273, -37.091720}; // << fora;
        // double[] personpoint = new double[]{-10.912123, -37.091205}; // << fora;
        // double[] personpoint = new double[]{-10.913177, -37.090249}; // << dentro;
        // double[] personpoint = new double[]{-10.914009, -37.089541}; // << dentro;
        // double[] personpoint = new double[]{-10.914493, -37.091215}; // << dentro;
        // double[] personpoint = new double[]{-10.912871, -37.091030}; // << fora;
        // double[] personpoint = new double[]{-10.913956, -37.092501}; // << fora;
        // double[] personpoint = new double[]{-10.914925, -37.089323}; // << fora;
        // double[] personpoint = new double[]{-10.913967, -37.089551}; // << dentro;

        System.out.println( "prox radius max point: " + maxProxRadiusPointLocaltion(centerLocalPoint, maxLocalPoint, personpoint) );
        System.out.println( "prox radius min point: " + maxProxRadiusPointLocaltion(centerLocalPoint, minLocalPoint, personpoint) );
        System.out.println( "center min max inner: " + innerRadiusPointsLocal(centerLocalPoint, minLocalPoint, maxLocalPoint, personpoint) );
        System.out.println( "mid min max inner: " + innerRadiusExtremePointsLocal(minLocalPoint, maxLocalPoint, personpoint) );

    }

}
