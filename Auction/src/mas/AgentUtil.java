package mas;

import jade.core.AID;
import jade.core.Agent;
import jade.core.ServiceException;
import jade.core.messaging.TopicManagementHelper;
import jade.lang.acl.ACLMessage;

public class AgentUtil {
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
}
