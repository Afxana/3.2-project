package com.example.project32;

public class Venue {
    private String venueId, name, details, location, imageUrl;
    private int price;

    public Venue() {
    }

    public Venue(String venueId, String name, String details, String location, int price, String imageUrl) {
        this.venueId = venueId;
        this.name = name;
        this.details = details;
        this.location = location;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public String getVenueId() { return venueId; }
    public String getName() { return name; }
    public String getDetails() { return details; }
    public String getLocation() { return location; }
    public int getPrice() { return price; }
    public String getImageUrl() { return imageUrl; }


}