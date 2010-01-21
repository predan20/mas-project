package mas;

import jade.core.AID;
import jade.core.Agent;
import jade.core.ServiceException;
import jade.core.behaviours.Behaviour;
import jade.core.messaging.TopicManagementHelper;
import jade.lang.acl.ACLMessage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import mas.agent.Auctioneer;
import mas.agent.Bidder;
import mas.agent.strategy.AllInStrategy;
import mas.agent.strategy.MUDutch;
import mas.agent.strategy.MUDutchAllOrNothing;
import mas.agent.strategy.Strategy;
import mas.behaviour.auctioneer.english.SingleUnitEnglishAuction;
import mas.behaviour.auctioneer.dutch.MultiUnitDutchAuction;
import mas.onto.AuctionDescription;
import mas.onto.BidderConfig;
import mas.onto.Good;

public class AgentUtil {
    
    /**
     * Strategy name to strategy class mapping.
     * All possible bidder strategies should be added here.
     */
    public static final Map<String, Class<? extends Strategy>> strategies = new HashMap<String, Class<? extends Strategy>>();
    static{
        strategies.put("ALL_IN", AllInStrategy.class);
        strategies.put("MUD", MUDutch.class);
        strategies.put("MUDAON", MUDutchAllOrNothing.class);
    }
    
    
    public static void addAuctionTopicReceiver(Agent agent, ACLMessage msg){
        TopicManagementHelper topicHelper;
        try {
            topicHelper = (TopicManagementHelper) agent
                    .getHelper(TopicManagementHelper.SERVICE_NAME);
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }
        AID jadeTopic = topicHelper.createTopic(Constants.AUCTION_TOPIC);
        msg.addReceiver(jadeTopic);
    }
    
    /**
     * Reads the auction description (goods, initial prize, etc.) from the configuration file.
     * @return {@link AuctionDescription}
     */
    public static AuctionDescription readAuctionDescription(String configFile){
        //load the properties file
        Properties props = new Properties();
        try {
            props.load(new FileInputStream(configFile));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        
        String auctionType = null;
        int minStep = 1;
        String[] goodTypes = null;
        String[] goodCounts = null;
        String[] initialPrizes = null;
        String[] reservationPrizes = null;
        
        //iterate over the keys and read the corresponding values
        for (Object key : props.keySet()){
            String k = (String)key;
            if(Constants.CONFIG_AUCTION_TYPE.equals(k)){
                auctionType = props.getProperty(k);
            }else if(Constants.CONFIG_AUCTION_GOOD_TYPES.equals(k)){
                goodTypes = props.getProperty(k).split(",");
            }else if(Constants.CONFIG_AUCTION_GOOD_COUNTS.equals(k)){
                goodCounts = props.getProperty(k).split(",");
            }else if(Constants.CONFIG_AUCTION_GOOD_INITIAL_PRIZE.equals(k)){
                initialPrizes = props.getProperty(k).split(",");
            }else if(Constants.CONFIG_AUCTION_GOOD_RESERVATION_PRIZE.equals(k)){
                reservationPrizes = props.getProperty(k).split(",");
            }else if(Constants.CONFIG_AUCTION_STEP.equals(k)){
                minStep = Integer.parseInt(props.getProperty(k));
            }
            
        }
        
        if(goodTypes.length != goodCounts.length){
            throw new RuntimeException("Number of good types and good counts do not match!.");
        }
        
        //construct the AuctionDescription instance from the parsed data
        Set<Good> goods = new HashSet<Good>();
        for(int i = 0; i < goodTypes.length; i++){
            String goodType = goodTypes[i];
            int count = Integer.parseInt(goodCounts[i]);
            int initialPrize = Integer.parseInt(initialPrizes[i]);
            int reservationPrize = Integer.parseInt(reservationPrizes[i]);
            
            try {
                Class<?> clazz = Class.forName(goodType);
                
                Class<? extends Good>  goodClass = clazz.asSubclass(Good.class);
                    
                Good good = goodClass.newInstance();
                good.setAvailableCount(count);
                good.setInitialPrize(initialPrize);
                good.setReservationPrize(reservationPrize);
                
                goods.add(good);
                
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Specified good type not found. Must be a fully qualified class name.", e);
            } catch (ClassCastException e) {
                throw new RuntimeException("Specified good type must be subclass of mas.onto.Good.", e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            }
        }
        
        return new AuctionDescription(auctionType, minStep, goods);
    }

    /**
     * Creates the appropriate agent behavior for the given auction description. 
     * @param desc The auction description.
     * @param agent The auctioneer to pass when constructing the behavior instance.
     * @return Behaviour instance
     */
    public static Behaviour createAuctioneerBehaviour(AuctionDescription desc, Auctioneer agent){
        if(AuctionDescription.ENGLISH_AUCTION.equals(desc.getAuctionType())){
            if(desc.getGoods().size() == 1 && desc.getGoods().iterator().next().getAvailableCount() == 1){
                return new SingleUnitEnglishAuction(agent, desc);
            }
        }
        else if (AuctionDescription.DUTCH_AUCTION.equals(desc.getAuctionType())){
            if(desc.getGoods().size() == 1){
                return new MultiUnitDutchAuction(agent, desc);
            }
        }
        return null; 
    }
    
    /**
     * Creates the appropriate agent behavior for the given auction description. 
     * @param desc The auction description.
     * @param agent The bidder to pass when constructing the behavior instance.
     * @return Behaviour instance
     */
    public static Behaviour createBidderBehaviour(AuctionDescription desc, Bidder agent){
        if(AuctionDescription.ENGLISH_AUCTION.equals(desc.getAuctionType())){
            if(desc.getGoods().size() == 1 && desc.getGoods().iterator().next().getAvailableCount() == 1){
                return new mas.behaviour.bidder.english.SingleUnitEnglishAuction(agent, desc);
            }
        }
        else if (AuctionDescription.DUTCH_AUCTION.equals(desc.getAuctionType())){
        	if(desc.getGoods().size() == 1){
                return new mas.behaviour.bidder.dutch.MultiUnitDutchAuction(agent, desc);
            }
        }
        return null; 
    }
    
    /**
     * Reads the auction description (goods, initial prize, etc.) from the configuration file.
     * @return {@link AuctionDescription}
     */
    public static BidderConfig readBidderConfig(String configFile, Bidder bidder){
        //load the properties file
        Properties props = new Properties();
        try {
            props.load(new FileInputStream(configFile));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        
        //construct the agent specific keys
        String agentName = bidder.getLocalName();
        String budgetKey = Constants.CONFIG_BIDDER_PREFIX + agentName + "." + Constants.CONFIG_BIDDER_BUDGET;
        String strategyKey = Constants.CONFIG_BIDDER_PREFIX + agentName + "." + Constants.CONFIG_BIDDER_STRATEGY;
        
        //iterate over the keys and read the corresponding values
        Strategy strategy = null;
        int budget = 0;
        for (Object key : props.keySet()){
            String k = (String)key;
            if(budgetKey.equals(k)){
                budget = Integer.parseInt(props.getProperty(k));
            }else if(strategyKey.equals(k)){
                String strategyName = props.getProperty(k);
                strategy = createStrategy(strategyName, bidder);
            }
        }
        
        //construct the BidderConfig instance from the parsed data
        return new BidderConfig(budget, strategy);
    }
    
    public static Strategy createStrategy(String strategyName, Bidder bidder){
        Class<? extends Strategy> strategyClass = strategies.get(strategyName);
        
        try {
            Constructor<? extends Strategy> constr = strategyClass.getConstructor(Bidder.class);
            
            return constr.newInstance(bidder);
        
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
