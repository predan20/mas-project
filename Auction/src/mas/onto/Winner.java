package mas.onto;

import jade.content.Concept;
import jade.core.AID;

public class Winner implements Concept {
    private AID winner;
    private int soldItems;
    private int soldPrice;
   // private int numberOfItems;
    
    public Winner(){}
    
    public Winner(AID winner, int soldItems, int soldPrice){
        this.winner= winner;
        this.soldItems = soldItems;
        this.soldPrice = soldPrice;
    }

	public AID getWinner() {
		return winner;
	}

	public void setWinner(AID winner) {
		this.winner = winner;
	}

	public int getSoldItems() {
		return soldItems;
	}

	public void setSoldItems(int soldItems) {
		this.soldItems = soldItems;
	}

	public int getSoldPrice() {
		return soldPrice;
	}

	public void setSoldPrice(int soldPrice) {
		this.soldPrice = soldPrice;
	}

   
}
