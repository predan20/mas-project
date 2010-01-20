package mas.onto;

import jade.content.Concept;

public class Prize implements Concept {
    private int ammount;
    
    public Prize(){}
    
    public Prize(int ammount){
        this.ammount = ammount;
    }

    public int getAmmount() {
        return ammount;
    }
}
