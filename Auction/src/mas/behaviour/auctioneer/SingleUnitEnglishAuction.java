package mas.behaviour.auctioneer;

import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import mas.agent.Auctioneer;
import mas.onto.AuctionDescription;

public class SingleUnitEnglishAuction extends OneShotBehaviour {
    private final AuctionDescription auctionDescription;
    
    
    public SingleUnitEnglishAuction(Auctioneer agent, AuctionDescription desc){
        super(agent);
        auctionDescription = desc;
    }
    @Override
    public void action() {
        SequentialBehaviour b = new SequentialBehaviour();
        
        b.addSubBehaviour(new AnnouncePrize(getAuctioneer(), auctionDescription.getGoods().iterator().next().getInitialPrize()));
        
        myAgent.addBehaviour(b);

    }
    
    public Auctioneer getAuctioneer(){
        return (Auctioneer) myAgent;
    }

}
