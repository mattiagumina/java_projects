package delivery;

public class Item {
    
    private String description;
    private double price;
    private String category;
    private int prepTime;

    public Item(String description, double price, String category, int prepTime) {
        this.description = description;
        this.price = price;
        this.category = category;
        this.prepTime = prepTime;
    }
    
    // "[CATEGORIA] DESCRIZIONE : PREZZO"
    public String toString(){
        return ("[" + category + "] " + description + " : " + String.format("%.2f", price));
    }

    public boolean match(String search) {
        return description.toLowerCase().contains(search.toLowerCase());
    }
    
    public String getDescription(){
        return description;
    }
    
    public String getCategory(){
        return category;
    }
    
    public int getTime(){
        return prepTime;
    }
    
    @Override
    public int hashCode(){
        return description.hashCode();
    }

    public double getPrice() {
        return price;
    }

}
