package com.example.menu;

public class ClothingItem {
    private int clothingID;
    private int userID;

    private String name;
    private String brand;
    private String type;
    private String pattern;
    private String fit;
    private String size;
    private String color1;
    private String color2;
    private String material;
    private String desc;
    private String status;
    private String occasion;

    private boolean spring;
    private boolean summer;
    private boolean fall;
    private boolean winter;
    private boolean all;


    // Accessor methods
    public int getClothingID() { return clothingID; }
    public int getUserID() { return userID; }
    public String getName() { return name; }
    public String getBrand() { return brand; }
    public String getType() { return type; }
    public String getPattern() {return pattern;}
    public String getFit() { return fit; }
    public String getSize() { return size; }
    public String getColor1() { return color1; }
    public String getColor2() { return color2; }
    public String getMaterial() { return material; }
    public String getDesc() { return desc; }
    public String getStatus() { return status; }
    public String getOccasion() { return occasion; }
    public boolean isSpring() { return spring; }
    public boolean isSummer() { return summer; }
    public boolean isFall() { return fall; }
    public boolean isWinter() { return winter; }
    public boolean isAll() { return all; }



    // Mutator methods
    public void setClothingID(int clothingID) { this.clothingID = clothingID; }
    public void setUserID(int userID) { this.userID = userID; }
    public void setName(String name) { this.name = name; }
    public void setBrand(String brand) { this.brand = brand; }
    public void setType(String type) { this.type = type; }
    public void setPattern(String pattern) {this.pattern = pattern;}
    public void setFit(String fit) { this.fit = fit; }
    public void setSize(String size) { this.size = size; }
    public void setColor1(String color1) { this.color1 = color1; }
    public void setColor2(String color2) { this.color2 = color2; }
    public void setMaterial(String material) { this.material = material; }
    public void setDesc(String desc) { this.desc = desc; }
    public void setStatus(String status) { this.status = status; }
    public void setOccasion(String occasion) { this.occasion = occasion; }
    public void setSpring(boolean spring) { this.spring = spring; }
    public void setSummer(boolean summer) { this.summer = summer; }
    public void setFall(boolean fall) { this.fall = fall; }
    public void setWinter(boolean winter) { this.winter = winter; }
    public void setAll(boolean all) { this.all = all; }


    public ClothingItem(String clothingID, String name, String brand, String type,String pattern, String fit, String size,
                        String color1, String color2, String material, String desc, String status,  String userID,
                        String occasion, boolean spring, boolean summer, boolean fall, boolean winter, boolean all) {
        this.clothingID = Integer.parseInt(clothingID);
        this.userID = Integer.parseInt(userID);
        this.name = name;
        this.brand = brand;
        this.type = type;
        this.pattern = pattern;
        this.fit = fit;
        this.size = size;
        this.color1 = color1;
        this.color2 = color2;
        this.material = material;
        this.desc = desc;
        this.status = status;
        this.occasion = occasion;
        this.spring = spring;
        this.summer = summer;
        this.fall = fall;
        this.winter = winter;
        this.all = all;
    }

    public ClothingItem() {
        this.clothingID = -1;
        this.userID = -1;
        this.name = "name";
        this.brand = "brand";
        this.type = "type";
        pattern = "pattern";
        this.fit = "fit";
        this.size = "size";
        this.color1 = "color1";
        this.color2 = "color2";
        this.material = "material";
        this.desc = "desc";
        this.status = "status";
        this.occasion = "occasion";
        this.spring = false;
        this.summer = false;
        this.fall = false;
        this.winter = false;
        this.all = true;
    }

    public ClothingItem(ClothingItem copy) {
        this.clothingID = copy.clothingID;
        this.userID = copy.userID;
        this.name = copy.name;
        this.brand = copy.brand;
        this.type = copy.type;
        this.pattern = copy.pattern;
        this.fit = copy.fit;
        this.size = copy.size;
        this.color1 = copy.color1;
        this.color2 = copy.color2;
        this.material = copy.material;
        this.desc = copy.desc;
        this.status = copy.status;
        this.occasion = copy.occasion;
        this.spring = copy.spring;
        this.summer = copy.summer;
        this.fall = copy.fall;
        this.winter = copy.winter;
        this.all = copy.all;
    }
}
