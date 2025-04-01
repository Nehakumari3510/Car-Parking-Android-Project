package com.example.carparkingapp.google_map;

public class ParkingLocation {
    private int parkingId;
    private String parkingName;
    private String city;
    private String parkingLocation;
    private String address1;
    private String address2;
    private double latitude;
    private double longitude;
    private String physicalAppearance;
    private String parkingOwnership;
    private String parkingSurface;
    private String hasCctv;
    private String hasBoomBarrier;
    private String ticketGenerated;
    private String entryExitGates;
    private String weeklyOff;
    private String parkingTiming;
    private String vehicleTypes;
    private int carCapacity;
    private int twoWheelerCapacity;
    private String parkingType;
    private String paymentModes;
    private String carParkingCharge;
    private String twoWheelerParkingCharge;
    private String allowsPrepaidPasses;
    private String providesValetServices;
    private String valueAddedServices;
    private String notes;
    private int totalSlots;
    private int availableSlots;

    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public String getParkingName() { return parkingName; }
    public int getTotalSlots() { return totalSlots; }
    public int getAvailableSlots() { return availableSlots; }

}
