import jade.core.Agent;
import java.util.Iterator;
import java.lang.Thread;
import java.awt.Point;
import java.util.LinkedList;
import blockworld.*;

public class HelloWorldAgent extends Agent
{
	protected void setup() 
	{
		Env e = Env.getEnv();
		e.addAgent(getAID().getLocalName());
		if (getAID().getLocalName().equals("Peter"))		
			e.enter(getAID().getLocalName(), 3.0, 4.0, "red");
		else
			e.enter(getAID().getLocalName(), 5.0, 6.0, "blue");
		int i = 0;
		while (i < 5)
		{
			e.east(getAID().getLocalName());
			LinkedList<Point> bombs = e.senseBombs(getAID().getLocalName());
			Iterator it = bombs.iterator();
			while (it.hasNext())
			{
  				Point bombPos = (Point)it.next();
				System.out.println(bombPos.getX() + ", " + bombPos.getY());
			}
			try
			{			
			  	Thread.sleep(2000);
			}
				catch(Exception ex) {};
				++i;

			}
			e.pickup(getAID().getLocalName());
        	}
}