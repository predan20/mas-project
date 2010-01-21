package mas.onto;

import mas.agent.strategy.Strategy;

public class BidderConfig {
    private int budget;
    private Strategy strategy;
    private int itemsWanted;//changed
    
    public BidderConfig(int budget, Strategy strategy) {
        this.budget = budget;
        this.strategy = strategy;
        this.itemsWanted = 1;
    }
    
    public BidderConfig(int budget, Strategy strategy, int itemsWanted) {
        this.budget = budget;
        this.strategy = strategy;
        this.itemsWanted = itemsWanted;
    }

    public int getBudget() {
        return budget;
    }

    public Strategy getStrategy() {
        return strategy;
    }
    
    
    public int getItemsWanted() {
        return itemsWanted;
    }

	public void setBudget(int budget) {
		this.budget = budget;
	}

	public void setItemsWanted(int itemsWanted) {
		this.itemsWanted = itemsWanted;
	}
}
