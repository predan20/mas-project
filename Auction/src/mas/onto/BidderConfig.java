package mas.onto;

import mas.agent.strategy.Strategy;

public class BidderConfig {
    private int budget;
    private Strategy strategy;
    
    public BidderConfig(int budget, Strategy strategy) {
        this.budget = budget;
        this.strategy = strategy;
    }

    public int getBudget() {
        return budget;
    }

    public Strategy getStrategy() {
        return strategy;
    }
    
}
