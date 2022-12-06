package com.softaai.realtimelocation.model;

public class SharedLocation {

    private String mUserId;
    private LatLong mLocation;
    private String mName;
    private String mPhotoUrl;

    public SharedLocation() {
        mUserId = "";
        mLocation = new LatLong();
        mName = "";
        mPhotoUrl = "";
    }

    public String getUserId() {
        return mUserId;
    }

    public LatLong getLocation() {
        return mLocation;
    }

    public String getName() {
        return mName;
    }

    public String getPhotoUrl() {
        return mPhotoUrl;
    }

    public void setUserId(String userId) {
        mUserId = userId;
    }

    public void setLocation(LatLong location) {
        mLocation = location;
    }

    public void setName(String name) {
        mName = name;
    }

    public void setPhotoUrl(String photoUrl) {
        mPhotoUrl = photoUrl;
    }

    public static class LatLong {
        public double latitude;
        public double longitude;

        public LatLong() {
            this(0.0, 0.0);
        }

        public LatLong(double lat, double lng) {
            latitude = lat;
            longitude = lng;
        }
    }

}
