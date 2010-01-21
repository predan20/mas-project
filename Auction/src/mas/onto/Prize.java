package mas.onto;

import jade.content.Concept;

public class Prize implements Concept {
    private int ammount;
   // private int numberOfItems;
    
    public Prize(){}
    
    public Prize(int ammount/*, int numberOfItems*/){
        this.ammount = ammount;
     //   this.numberOfItems = numberOfItems;
    }

    public int getAmmount() {
        return ammount;
    }
}
