package mas.behaviour;

import jade.core.AID;
import jade.core.ServiceException;
import jade.core.behaviours.TickerBehaviour;
import jade.core.messaging.TopicManagementHelper;
import jade.lang.acl.ACLMessage;

import java.awt.Point;
import java.io.IOException;
import java.util.List;

import mas.Constants;
import mas.agent.BlockworldLogisticsAgent;
import blockworld.Agent;
import blockworld.Env;

/**
 * Ticker (cyclic) behavior for sensing bombs and notifying "carrier" agents.
 * This behavior senses the bombs in the environment, notifies others using a
 * message to the {link {@link Constants#BOMB_FOUND_TOPIC} topic. If no bombs
 * are sensed tries to walk the agent around in case the bombs are out of sense
 * range.
 */
public class ExplorerBehaviour extends TickerBehaviour {
	private static final long TICK_PERIOD = 200; // one second
	private boolean[] explored;
	private int noExplored;
	private int[] stack;
	int top;

	public ExplorerBehaviour(BlockworldLogisticsAgent a) {
		super(a, TICK_PERIOD);
		int length = 200*200; 
		explored = new boolean[length];
		stack = new int[length + 1];
		for (int i = 0; i < length; i++)
			explored[i] = false;
		noExplored = 0;
		top = 0;
	}

	@Override
	public void onTick() {
		// sense bombs and notify others
		Env env = Env.getEnv();

		List<Point> bombs = env.senseBombs(getBlockworldAgent().getName());
		for (Point bomb : bombs) {
			notifyOthers(bomb);
		}

		// if no bombs were sensed move a bit in case they are out of range
		if (bombs == null || bombs.isEmpty()) {
			walk();
		}
	}

	private void notifyOthers(Point bomb) {
		TopicManagementHelper topicHelper;
		try {
			topicHelper = (TopicManagementHelper) myAgent
					.getHelper(TopicManagementHelper.SERVICE_NAME);
		} catch (ServiceException e) {
			throw new RuntimeException(e);
		}
		AID jadeTopic = topicHelper.createTopic(Constants.BOMB_FOUND_TOPIC);
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		msg.addReceiver(jadeTopic);
		try {
			msg.setContentObject(bomb);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		myAgent.send(msg);
	}

	private void goTo(Point p) {
		((BlockworldLogisticsAgent) myAgent).goTo(p);
	}

	//for simplicity we limit the sense area to the square that can be comprised inside the circle.
	//notCovered returns true if there are still squares that were not explored in the range of the (x,y) point
	private boolean notCovered(int x, int y) { 
		int limit = (int) Math.floor(Env.getEnv().getSenseRange()
				/ Math.sqrt(2));
		final int startX = Math.max(0, x - limit);
		final int endX = Math.min(Env.getEnv().getHeight(), x + limit);
		final int startY = Math.max(0, y - limit);
		final int maxY = Math.min(Env.getEnv().getWidth(), y + limit);
		final int width = Env.getEnv().getWidth();

		for (int i = startX; i < endX; i++) {
			for (int j = startY; j < maxY; j++)
				if (!explored[i * width + j])
					return true;
		}
		return false;

	}

	//marks as explored all the squares in the sense reach of (x,y) point
	private void cover(int x, int y) {
		int limit = (int) Math.floor(Env.getEnv().getSenseRange()
				/ Math.sqrt(2));
		int startX = Math.max(0, x - limit);
		int endX = Math.min(Env.getEnv().getHeight(), x + limit);
		int startY = Math.max(0, y - limit);
		int endY = Math.min(Env.getEnv().getWidth(), y + limit);
		int width = Env.getEnv().getWidth();
		for (int i = startX; i < endX; i++) {
			for (int j = startY; j < endY; j++)
				if (!explored[i * width + j]) {
					explored[i * width + j] = true;
					noExplored++;
				}
		}

	}

	private void walk() { //based on iterative DFS
		Env env = Env.getEnv();

		Point currentPosition = getBlockworldAgent().getPosition();
		final int x = (int) currentPosition.getX();
		final int y = (int) currentPosition.getY();
		int width = env.getWidth();
		int height = env.getHeight();

		// i look at all the neighbors. the first that i find uncovered, i move
		// towards it and
		// put in on the stack
		// if i can't find any i go back.
		// when i covered all i make the stack empty and reinitialize the
		// explored vector

		// right neighbor
		if ((x + (env.getSenseRange() / Math.sqrt(2)) < width)
				&& (env.isFree(getPoint(x + 1, y)))) {// if i have right neighbor
			if (notCovered(x + 1, y)) {
				stack[top] = (x + 1)* width + y;
				top++; // i add the neighbor to the stack
				cover((int) x + 1, (int) y);
				goTo(getPoint(x + 1, y));
				return; // i am not interested in other neighbors
			}
		}
		// down neighbor
		if ((y + (env.getSenseRange() / Math.sqrt(2)) < height)// if i have lower
				&& (env.isFree(getPoint(x , y + 1))))												// neighbor
			if (notCovered(x, y + 1)) {
				stack[top] = x * width + (y + 1);
				top++; // i add the neighbor to the stack
				cover((int) x, (int) y + 1);
				goTo(getPoint(x, y + 1));
				return; // i am not interested in other neighbors
			}

		// left
		if ((x - (env.getSenseRange() / Math.sqrt(2)) > 0)
				&& (env.isFree(getPoint(x - 1, y))))
			if (notCovered(x - 1, y)) {
				stack[top] = (x - 1) * width + y;
				top++; // i add the neighbor to the stack
				cover(x - 1, y);
				goTo(getPoint(x - 1, y));
				return; // i am not interested in other neighbors
			}

		// up
		if ((y - (env.getSenseRange() / Math.sqrt(2)) > 0)
				&& (env.isFree(getPoint(x , y - 1))))
			if (notCovered(x, y - 1))// if it's not covered (then i can improve by moving that way)
			{
				stack[top] = x * width + (y - 1);
				top++; // i add the neighbor to the stack
				cover(x, y - 1); 
				goTo(getPoint(x, y - 1));
				return; // i am not interested in other neighbors
			}
		
		//reinitialize  //changed condition (to reinitialize when we change dimensions)
		if ((noExplored == height * width) || (top == 0) || ((top>2) && (!env.isFree(getPoint(stack[top-2] / width, stack[top-2] % width))))||(height!=env.getHeight())||(width!=env.getWidth()))// i
		// finished searching
		{
			top = 0;
			height=env.getHeight();
			width=env.getWidth();
			for (int i = 0; i < height * width; i++)
				explored[i] = false;
			noExplored = 0;
			return;
		}

		// in the latter case i try to find the most recent position in the
		// stack
		// from which i can expand
		top--;
		if (top>0)
			goTo(getPoint(stack[top-1] / width, stack[top-1] % width));

	}

	private Point getPoint(double px, double py) {
		double x = px;
		double y = py;
		return new Point((int) x, (int) y);
	}

	private Agent getBlockworldAgent() {
		return ((BlockworldLogisticsAgent) myAgent).getBlockworldAgent();
	}
}
