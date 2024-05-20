package org.example.bot.handlers;

public class Distance {
    public double distance(double latitude1, double longitude1, double latitude2, double longitude2, String unit) {
        if ((latitude1 == latitude2) && (longitude1 == longitude2)) {
            return 0;
        } else {
            double theta = longitude1 - longitude2;
            double dist = Math.sin(Math.toRadians(latitude1)) * Math.sin(Math.toRadians(latitude2)) +
                    Math.cos(Math.toRadians(latitude1)) * Math.cos(Math.toRadians(latitude2)) *
                            Math.cos(Math.toRadians(theta));
            dist = Math.acos(dist);
            dist = Math.toDegrees(dist);
            dist = dist * 60 * 1.1515;
            if (unit.equals("K")) {
                dist = dist * 1.609344;
            } else if (unit.equals("N")) {
                dist = dist * 0.8684;
            }
            return (dist);
        }
    }
}
