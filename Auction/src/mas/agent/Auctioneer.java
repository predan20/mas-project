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

import mas.AgentUtil;
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
    
    /**
     * The current state of the auction.
     */
    private AuctionDescription auctionDescription;
    
    
    @Override
    protected void setup() {
        //sleep for 20 sec, so that the Sniffer agant can be started
//        try {
//            Thread.currentThread().sleep(20000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
        
        // register to the AUCTION topic
        try {
            TopicManagementHelper topicHelper = (TopicManagementHelper) this
                    .getHelper(TopicManagementHelper.SERVICE_NAME);
            topicHelper.register(topicHelper
                    .createTopic(Constants.AUCTION_TOPIC));
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }
        
        // read config file if provided
        Object[] args = getArguments();
        if(args != null && args.length > 0){
            configFile = (String)args[0];
        }
        
        //register the tournament ontology for behaviors which receive messages and use it
        getContentManager().registerOntology(AuctionOntology.getInstance());
        getContentManager().registerLanguage(new SLCodec());

        //add one sequential behavior
        SequentialBehaviour tournamentLifecycle = new SequentialBehaviour(this);
        tournamentLifecycle.addSubBehaviour(new SendAuctionRequest(this, getAuctionDesciption()));
        tournamentLifecycle.addSubBehaviour(AgentUtil.createAuctioneerBehaviour(getAuctionDesciption(), this));
        
        addBehaviour(tournamentLifecycle);
    }


    private AuctionDescription getAuctionDesciption(){
        if(auctionDescription == null){
            auctionDescription = AgentUtil.readAuctionDescription(configFile);
        }
        
        return auctionDescription;
    }
}
