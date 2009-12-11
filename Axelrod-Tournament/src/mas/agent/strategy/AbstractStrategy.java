package mas.agent.strategy;

import java.util.List;

import mas.agent.Player;
import mas.onto.PlayerAction;


public abstract class AbstractStrategy implements Strategy {
    private Player player;
    
    public AbstractStrategy(Player player){
        this.player = player;
    }
    
    protected List<PlayerAction> getOponentHistory(){
        return player.getOponentHistory();
    }
    
    protected List<PlayerAction> getOwnHistory(){
        return player.getOwnHistory();
    }
    
    protected int getRound(){
        return getOwnHistory().size() + 1;
    }
    
    protected Player getPlayer(){
        return this.player;
    }

}
