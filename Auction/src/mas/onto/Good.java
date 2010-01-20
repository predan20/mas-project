package mas.onto;

import jade.content.Concept;

public class Good implements Concept {
    private int initialPrize;
    private int availableCount;
    private int reservationPrize;
    
    public Good(){}
    
    public Good(int initialPrize, int availableCount, int reservationPrize) {
        this.initialPrize = initialPrize;
        this.availableCount = availableCount;
        this.reservationPrize = reservationPrize;
    }
    
    public int getAvailableCount() {
        return availableCount;
    }

    public void setAvailableCount(int availableCount) {
        this.availableCount = availableCount;
    }

    public int getReservationPrize() {
        return reservationPrize;
    }

    public void setReservationPrize(int reservationPrize) {
        this.reservationPrize = reservationPrize;
    }

    public int getInitialPrize() {
        return initialPrize;
    }

    public void setInitialPrize(int initialPrize) {
        this.initialPrize = initialPrize;
    }

}
