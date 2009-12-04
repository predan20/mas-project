package mas.behaviour;

import jade.core.ServiceException;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.messaging.TopicManagementHelper;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

import java.awt.Point;
import java.util.List;

import mas.Constants;
import mas.agent.BlockworldLogisticsAgent;
import mas.agent.Carrier;
import blockworld.Agent;
import blockworld.Env;

/**
 * Cyclic behavior for carrying bombs to traps in the block world. This behavior
 * waits for a message in the {link {@link Constants#BOMB_FOUND_TOPIC} topic,
 * goes to the bomb, picks it up and drops it in the closest trap.
 * 
 */
public class CarrierBehaviour extends CyclicBehaviour {
	public CarrierBehaviour(Carrier a) {
		super(a);

		// register to the BOMB topic
		try {
			TopicManagementHelper topicHelper = (TopicManagementHelper) myAgent
					.getHelper(TopicManagementHelper.SERVICE_NAME);
			topicHelper.register(topicHelper
					.createTopic(Constants.BOMB_FOUND_TOPIC));
		} catch (ServiceException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void action() {
		// if holding a bomb drop in a trap
		if (getBlockworldAgent().atCapacity()) {
			dropBomb();
			block();
			return;
		}

		TopicManagementHelper topicHelper;
		try {
			topicHelper = (TopicManagementHelper) myAgent
					.getHelper(TopicManagementHelper.SERVICE_NAME);
		} catch (ServiceException e) {
			throw new RuntimeException(e);
		}

		MessageTemplate bombTopicMessage = MessageTemplate
				.MatchTopic(topicHelper.createTopic(Constants.BOMB_FOUND_TOPIC));
		ACLMessage message = myAgent.blockingReceive(bombTopicMessage);

		if (message != null) {
			Point bomb;
			try {
				bomb = (Point) message.getContentObject();
			} catch (UnreadableException e) {
				throw new RuntimeException(e);
			}
			if (Env.getEnv().isBomb(bomb) != null) {
				pickupBomb(bomb);
				dropBomb();
			}
		}
	}

	// NEW

	private boolean nextStepBFS(Point target) {
		Env env = Env.getEnv();
		Point[] queue;
		int height = env.getHeight();
		int width = env.getWidth();
		int length=200*200;
		queue = new Point[length];
		int[] parent = new int[length];
		boolean[][] seen = new boolean[200][200];
		for (int i = 0; i < height; i++)
			for (int j = 0; j < width; j++)
				seen[i][j] = false;

		int first = 0;
		int last = 1;

		queue[first] = getBlockworldAgent().getPosition();
		while ((first != last) && (!queue[first].equals(target))) {
			int x = (int) queue[first].getX();
			int y = (int) queue[first].getY();
			Point pr = new Point(x + 1, y);
			if ((x + 1 < env.getWidth()) && (env.isFree(pr) || pr.equals(target))//changed
					&& (!seen[x + 1][y])) { // right
				seen[x + 1][y] = true;
				parent[last] = first;
				queue[last++] = pr;
			}

			Point pd = new Point(x, y + 1);
			if ((y + 1 < env.getHeight()) && (env.isFree(pd) || pd.equals(target))//changed
					&& (!seen[x][y + 1])) {// down
				seen[x][y + 1] = true;
				parent[last] = first;
				queue[last++] = pd;
			}

			Point pl = new Point(x - 1, y);
			if ((x > 0) && (env.isFree(pl) || pl.equals(target))
					&& (!seen[x - 1][y])) { // left
				seen[x - 1][y] = true;
				parent[last] = first;
				queue[last++] = pl;
			}

			Point pu = new Point(x, y - 1);
			if ((y > 0) && (env.isFree(pu) || pu.equals(target))
					&& (!seen[x][y - 1])) {// up
				seen[x][y - 1] = true;
				parent[last] = first;
				queue[last++] = pu;
			}

			first++;
		}
	
		if (queue[first] != null && queue[first].equals(target)) {
			while (parent[first] != 0)
				first = parent[first];
			goTo(queue[first]);
			return true;
		}
		return false;
	}

	private void dropBomb() {
		// if not holding a bomb - do nothing
		if (!getBlockworldAgent().atCapacity()) {
			return;
		}

		Point trap = getClosest(((Carrier) myAgent).getTraps());
		// if not traps are set - do nothing
		if (trap == null) {
			return;
		}
		// NEW
		while ((!trap.equals(getBlockworldAgent().getPosition()))
				&& nextStepBFS(trap))
			;

		Env.getEnv().drop(getBlockworldAgent().getName());
	}

	private void pickupBomb(Point bomb) {
		//we calculate the next position each time because the env can change at any moment
		while ((!bomb.equals(getBlockworldAgent().getPosition()))
				&& (nextStepBFS(bomb)))
			;
		Env.getEnv().pickup(getBlockworldAgent().getName());
	}

	private Point getClosest(List<Point> locations) {
		Point currentPosition = getBlockworldAgent().getPosition();
		Point closest = null;

		for (Point trap : locations) {
			if (closest == null) {
				closest = trap;
				continue;
			}
			if (currentPosition.distance(closest) > currentPosition
					.distance(trap)) {
				closest = trap;
			}
		}

		return closest;
	}

	private Agent getBlockworldAgent() {
		return ((BlockworldLogisticsAgent) myAgent).getBlockworldAgent();
	}

	private void goTo(Point p) {
		((BlockworldLogisticsAgent) myAgent).goTo(p);
	}
}
