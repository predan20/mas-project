package mas.agent;

import jade.content.lang.sl.SLCodec;
import jade.core.Agent;
import jade.core.ServiceException;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.core.messaging.TopicManagementHelper;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import mas.Constants;
import mas.behaviour.auctioneer.SendAuctionRequest;
import mas.behaviour.auctioneer.SingleUnitEnglishAuction;
import mas.onto.AuctionDescription;
import mas.onto.AuctionOntology;
import mas.onto.Good;
import mas.onto.AuctionDescription.AuctionType;

/**
 * JADE agent representing an auctioneer of an auction.
 * It has single sequential behavior representing its lifecycle.
 * <ol>
 *  <li> 
 * </ol>
 * 
 * As an init param this agent accepts the number of rounds to be played.
 */
public class Auctioneer extends Agent {
    
    /**
     * Configuration file.
     */
    private String configFile = "config.properties";
    
    
    @Override
    protected void setup() {
        try {
            Thread.currentThread().sleep(20000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        
        // register to the AUCTION topic
        try {
            TopicManagementHelper topicHelper = (TopicManagementHelper) this
                    .getHelper(TopicManagementHelper.SERVICE_NAME);
            topicHelper.register(topicHelper
                    .createTopic(Constants.AUCTION_TOPIC));
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }
        
        // read argument for number of rounds if provided
        Object[] args = getArguments();
        if(args != null && args.length > 0){
            configFile = (String)args[0];
        }
        
        //register the tournament ontology for behaviors which receive messages and use it
        getContentManager().registerOntology(AuctionOntology.getInstance());
        getContentManager().registerLanguage(new SLCodec());

        //add one sequential behavior
        SequentialBehaviour tournamentLifecycle = new SequentialBehaviour(this);
        Behaviour auctionRequest = new SendAuctionRequest(this, getAuctionDesciption());
        
        tournamentLifecycle.addSubBehaviour(auctionRequest);
        tournamentLifecycle.addSubBehaviour(new SingleUnitEnglishAuction(this, getAuctionDesciption()));
        
        addBehaviour(tournamentLifecycle);
    }


    private AuctionDescription getAuctionDesciption(){
        Properties props = new Properties();
        try {
            props.load(new FileInputStream(configFile));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        
        AuctionType aType = null;
        String[] goodTypes = null;
        String[] goodCounts = null;
        
        Map<Class<? extends Good>, Integer> goods = new HashMap<Class<? extends Good>, Integer>();
        
        for (Object key : props.keySet()){
            String k = (String)key;
            if(Constants.CONFIG_AUCTION_TYPE.equals(k)){
                String auctionType = props.getProperty(k);
                if("english".equals(auctionType)){
                    aType = AuctionType.ENGLISH;
                }else if("dutch".equals(auctionType)){
                    aType = AuctionType.DUTCH;
                }
            }
            
            if(Constants.CONFIG_AUCTION_GOOD_TYPES.equals(k)){
                goodTypes = props.getProperty(k).split(",");
            }
            
            if(Constants.CONFIG_AUCTION_GOOD_COUNTS.equals(k)){
                goodCounts = props.getProperty(k).split(",");
            }
        }
        
        if(goodTypes.length != goodCounts.length){
            throw new RuntimeException("Number of good types and good counts do not match!.");
        }
        
        for(int i = 0; i < goodTypes.length; i++){
            String goodType = goodTypes[i];
            Integer count = Integer.parseInt(goodCounts[i]);
            try {
                Class<? extends Good>  goodClass = (Class<? extends Good>) Class.forName(goodType);
                goods.put(goodClass, count);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Specified good type not found. Must be a fully qualified class name.", e);
            }
        }
        
        
        return new AuctionDescription(aType, goods);
    }
}
