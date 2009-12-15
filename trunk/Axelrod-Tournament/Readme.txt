README for the Axelrod's tournament assignment

The archive contains:
1. "src" folder containing the JAVA sources
2. "lib" folder with all needed binaries.
3. "doc" folder with generated javadoc
4. "run.bat" - starts a match between 2 players ( TIT-FOR-TAT vs. JOSS by default but can be configured, see bellow)
5. Eclipse project files that can be imported by creating a new project in Eclipse and selecting the "Create project from existing source"

Solution Overview:
    
1. Agents	
We have two types of JADE agents, one representing a player participating in an Axelrod's Tournament and one representing the tournament itself. 
Their corresponding JAVA classes are mas.agent.Player and mas.agent.AxelrodTournament.
The Player agent type accepts as configuration parameter the name of a strategy to follow (see 4.Strategies and 5.Configuration).
In a given match there are 3 agents started, two players and one Axelrod's Tournament. 

2. Communication
The agents communicate to each other by sending ACL messages following the FIPA Request Interaction Protocol.
There is one REGISTER request from each Player to the AxelrodTournament and multiple NEXT_ROUND requests from AxelrodTournament to each Player.
The INFORM response to the NEXT_ROUND request contains the actual action (DEFECT or COOPERATE) that the player wants to perform.

The content of the messages is based on user-defined ontology (mas.onto.AxelrodTournamentOntology) containing concepts representing actions (like REGISTER, DEFECT, etc.).
See mas.onto package.  

3. Behaviors
Player and tournament agents have both single SequentialBehaviour with sub-behaviors corresponding to their lifecycle.
Some of the sub behaviors are handling the communication between the agents and others are for initialization work.
Using sequential behavior guarantees the order of behaviors, for instance that the match will begin only after 2 players have registered, 
	or that the next round will begin only when both players have announced their actions for the previous one.
See mas.behaviour package.
	
4. Strategies
The Player agent accepts as configuration parameter the strategy to use. Then each time the player is asked for the next round the current strategy determines the next action.
Implemented are the Always-defect(ALLD), Tit-for-tat(TFT), Joss(JOSS), Tester(TESTER), History-Based(HB) and Go-by-Majority(MAJORITY) strategies.
History based (HB) - ???
Go-by-Majority(MAJORITY) - Counts the total number of defections and cooperations by the other player. 
						   If the defections outnumber the cooperations, go-by-majority will defect; otherwise this strategy will cooperate.
See mas.agent.strategy

5. Running and Configuration
A match can be started by running "run.bat". TIT-FOR-TAT vs. JOSS is started by default.
To change that, edit the file and pass different strategy name (use the names in brackets from point 4.Strategies) to the Player1 or Player2 agents. 
You can change the number of played rounds by passing an integer to the AxerlrodTournament agent.

	Partial "run.bat":
	... Tournament:mas.agent.AxelrodTournament(60) Player1:mas.agent.Player(TFT) Player2:mas.agent.Player(JOSS)

6. Tournament results
?????