package mas.onto;

import jade.content.Concept;

public class InitialPrize implements Concept {
    private int ammount;
    
    public InitialPrize(){}
    
    public InitialPrize(int ammount){
        this.ammount = ammount;
    }

    public int getAmmount() {
        return ammount;
    }
}
