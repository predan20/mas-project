package mas.behaviour;

import jade.core.behaviours.TickerBehaviour;

import java.awt.Point;
import java.util.List;

import mas.agent.Sapper;
import blockworld.Env;

public class SapperBehaviour extends TickerBehaviour {
	private static final long TICK_PERIOD = 1000; //one second
	private blockworld.Agent agent;
	
	public SapperBehaviour(Sapper a, blockworld.Agent agent) {
		super(a, TICK_PERIOD);
		this.agent = agent;
	}

	@Override
	public void onTick() {
		//if holding a bomb drop in a trap
		if(getBlockworldAgent().atCapacity()){
			dropBomb();
			return;
		}
		
		//sense bombs and pickup the closest one
		Env env = Env.getEnv();
		
		List<Point> bombs = env.senseBombs(getBlockworldAgent().getName());
		Point p = getClosest(bombs);
		if(p != null){
			pickupBomb(p);
			dropBomb();
		}
		
		//if no bombs were sensed move a bit in case they are out of range
		if(bombs == null || bombs.isEmpty()){
			//walk();
		}
	}

	private void walk() {
		Env env = Env.getEnv();
		
		Point currentPosition = getBlockworldAgent().getPosition();
		if(currentPosition.getX() + env.getSenseRange() < env.getWidth()){
			goTo(getInnerPoint(currentPosition.getX() + env.getSenseRange(), 
								currentPosition.getY()));
		}
		
		currentPosition = getBlockworldAgent().getPosition();
		if(currentPosition.getY() + env.getSenseRange() < env.getHeight()){
			goTo(getInnerPoint(currentPosition.getX(), 
								currentPosition.getY()  + env.getSenseRange()));
		}
		
		currentPosition = getBlockworldAgent().getPosition();
		if(currentPosition.getX() - env.getSenseRange() > 0){
			goTo(getInnerPoint(currentPosition.getX() - env.getSenseRange(), 
								currentPosition.getY()));
		}
		
		currentPosition = getBlockworldAgent().getPosition();
		if(currentPosition.getY() - env.getSenseRange() < 0){
			goTo(getInnerPoint(currentPosition.getX(), 
								currentPosition.getY()  - env.getSenseRange()));
		}
	}
	
	private Point getInnerPoint(double px, double py){
		double x = px;
		double y = py;
		
		Env env = Env.getEnv();
		if(px > env.getWidth() - env.getSenseRange()){
			x = env.getWidth() - env.getSenseRange();
		}
		if(px < env.getSenseRange()){
			x = env.getSenseRange();
		}
		if(py > env.getHeight() - env.getSenseRange()){
			y = env.getHeight() - env.getSenseRange();
		}
		if(py < env.getSenseRange()){
			y = env.getSenseRange();
		}
		
		return new Point((int)x, (int)y);
	}

	private void dropBomb() {
		//if not holding a bomb - do nothing
		if(!getBlockworldAgent().atCapacity()){
			return;
		}
		
		Point trap = getClosest(((Sapper)myAgent).getTraps());
		//if not traps are set - do nothing
		if(trap == null){
			return;
		}
		
		goTo(trap);
		
		Env.getEnv().drop(getBlockworldAgent().getName());
	}

	private void pickupBomb(Point bomb) {
		goTo(bomb);
		
		Env.getEnv().pickup(getBlockworldAgent().getName());	
	}
	
	public void goTo(Point location){
		Env env = Env.getEnv();
		
		Point currentPosition = getBlockworldAgent().getPosition();
		
		while(!location.equals(currentPosition)){
			if(currentPosition.x > location.x){
				env.west(getBlockworldAgent().getName());
			}
			if(currentPosition.x < location.x){
				env.east(getBlockworldAgent().getName());
			}
			
			if(currentPosition.y > location.y){
				env.north(getBlockworldAgent().getName());
			}
			if(currentPosition.y < location.y){
				env.south(getBlockworldAgent().getName());
			}
			
			currentPosition = getBlockworldAgent().getPosition();
		}
	}
	
	private Point getClosest(List<Point> locations){
		Point currentPosition = getBlockworldAgent().getPosition();
		Point closest = null;
		
		for(Point trap : locations){
			if(closest == null){
				closest = trap;
				continue;
			}
			if(currentPosition.distance(closest) > currentPosition.distance(trap)){
				closest = trap;
			}
		}
		
		return closest;
	}
	
	public blockworld.Agent getBlockworldAgent(){
		return agent;
	}

}
