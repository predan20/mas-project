package mas.agent;

import jade.content.lang.sl.SLCodec;
import jade.core.Agent;
import jade.core.ServiceException;
import jade.core.behaviours.SequentialBehaviour;
import jade.core.messaging.TopicManagementHelper;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import mas.Constants;
import mas.agent.strategy.Strategy;
import mas.behaviour.bidder.HandleAuctionRequest;
import mas.behaviour.bidder.ReceiveInitialPrize;
import mas.onto.AuctionOntology;
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
	
	private int budget;
    private Strategy currentStrategy;
    
    /**
     * Strategy name to strategy class map.
     * All possible strategies should be added here.
     */
    public static final Map<String, Class<? extends Strategy>> strategies = new HashMap<String, Class<? extends Strategy>>();
    static{
        strategies.put(null, null);
    }

    @Override
    protected void setup() {
        //init strategy
        //initStrategy();
        
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
        
        SequentialBehaviour playerBehaviour = new SequentialBehaviour();
        playerBehaviour.addSubBehaviour(new HandleAuctionRequest(this));
        playerBehaviour.addSubBehaviour(new ReceiveInitialPrize(this));
        
        addBehaviour(playerBehaviour);
    }
    
    /**
     * Checks the passed agent arguments and instantiates the required {@link Strategy} instance.
     * Defautls to the ALLD strategy.
     */
    private void initStrategy() {
        Object[] args = getArguments();
        if(args != null && args.length > 0){
            String strategyName = (String)args[0];
            Class<? extends Strategy> strategyClass = strategies.get(strategyName);
            
            try {
                Constructor<? extends Strategy> constr = strategyClass.getConstructor(Bidder.class);
                this.currentStrategy = constr.newInstance(this);
            } catch (SecurityException e) {
                throw new RuntimeException(e);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public Strategy getCurrentStrategy() {
        return currentStrategy;
    }

    public void setCurrentStrategy(Strategy currentStrategy) {
        this.currentStrategy = currentStrategy;
    }
}
