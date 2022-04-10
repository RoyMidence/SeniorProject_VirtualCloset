package com.example.menu;

public class Outfit {

    private String outfitID;
    private String outfitName;

    public String getOutfitID() {
        return outfitID;
    }

    public void setOutfitID(String outfitID) {
        this.outfitID = outfitID;
    }

    public String getOutfitName() {
        return outfitName;
    }

    public void setOutfitName(String outfitName) {
        this.outfitName = outfitName;
    }

    public Outfit(String outfitID, String outfitName) {
        this.outfitID = outfitID;
        this.outfitName = outfitName;
    }


}
