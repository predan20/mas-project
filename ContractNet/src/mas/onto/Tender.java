package mas.onto;

import jade.content.Predicate;

import java.util.List;

public class Tender implements Predicate {
    private List<ConfigTender> subtenders = null;
    private int totalPrice;
    
    public Tender() {}
    
    public Tender(List<ConfigTender> subtenders) {
        this.subtenders = subtenders;
    }
    
    public int getTotalPrice() {
        if(totalPrice == 0){
            for(ConfigTender ct : subtenders){
                for(Component component : ct.getComponents()){
                    totalPrice += component.getPrice() * component.getCount();
                }
            }
        }
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<ConfigTender> getSubtenders() {
        return subtenders;
    }

    public void setSubtenders(List<ConfigTender> subtenders) {
        this.subtenders = subtenders;
    }
    
    

}
