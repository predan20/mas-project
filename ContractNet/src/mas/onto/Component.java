package mas.onto;

import jade.content.Concept;

public class Component implements Concept{
    private String manufacturer;
    private String quality;
    private int price;
    private int count;
    
    public String getManufacturer() {
        return manufacturer;
    }
    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }
    public String getQuality() {
        return quality;
    }
    public void setQuality(String quality) {
        this.quality = quality;
    }
    public int getPrice() {
        return price;
    }
    public void setPrice(int price) {
        this.price = price;
    }
    public int getCount() {
        return count;
    }
    public void setCount(int availableCount) {
        this.count = availableCount;
    }
    
    public Component clone(){
        Component res;
        try {
            res = this.getClass().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        
        res.setCount(this.getCount());
        res.setManufacturer(getManufacturer());
        res.setPrice(getPrice());
        res.setQuality(getQuality());
        
        return res;
    }
    
    
    
}
