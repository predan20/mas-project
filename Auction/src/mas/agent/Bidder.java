package mas.agent;

import java.util.HashMap;
import java.util.Map;

import jade.content.lang.sl.SLCodec;
import jade.core.Agent;
import jade.core.ServiceException;
import jade.core.messaging.TopicManagementHelper;
import mas.AgentUtil;
import mas.Constants;
import mas.agent.strategy.Strategy;
import mas.behaviour.bidder.HandleAuctionRequest;
import mas.onto.AuctionDescription;
import mas.onto.AuctionOntology;
import mas.onto.BidderConfig;
import mas.onto.Good;
/**
 * JADE agent representing a bidder in an auction.
 * It has single sequential behavior containing the following behaviors:
 * <ol>
 * <li>
 * <li>
 * <li>
 * </ol>
 */
public class Bidder extends Agent {
	
	private BidderConfig bidderState;
	private AuctionDescription auction;
    private Map<Good, Integer> lastPrizes = new HashMap<Good, Integer>();
    

	@Override
    protected void setup() {
        //init configuration
        loadConfiguration();
        
        // register to the AUCTION topic
        try {
            TopicManagementHelper topicHelper = (TopicManagementHelper) this
                    .getHelper(TopicManagementHelper.SERVICE_NAME);
            topicHelper.register(topicHelper
                    .createTopic(Constants.AUCTION_TOPIC));
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }
        
        getContentManager().registerOntology(AuctionOntology.getInstance());
        getContentManager().registerLanguage(new SLCodec());
        
        addBehaviour(new HandleAuctionRequest(this));
    }
    
    /**
     * Loads this bidder's specific configuration - budget, strategy, etc.
     */
    private void loadConfiguration() {
        String configFile = "config.properties";
        
        //check if the config file was passed as an argument
        Object[] args = getArguments();
        if(args != null && args.length > 0){
            configFile = (String)args[0];
        }
        
        //read the configuration
        bidderState = AgentUtil.readBidderConfig(configFile, this);
    }

    public Strategy getCurrentStrategy() {
        return getBidderState().getStrategy();
    }
    
    public BidderConfig getBidderState(){
        return bidderState;
    }
    
    public AuctionDescription getAuction() {
		return auction;
	}

	public void setAuction(AuctionDescription auction) {
		this.auction = auction;
	}
	
	public int getLastPrize(Good good){
		return lastPrizes.get(good);
	}
	
	public void setLastPrize(Good good, int value){
		lastPrizes.put(good, value);
	}
}
