README for the English Auction assignment

The archive contains:
1. "src" folder containing the JAVA sources
2. "lib" folder with all needed binaries.
4. "run_english.bat" - starts an english single unit auction with 4 bidders
4. "run_dutch.bat" - starts a dutch multi-unit auction with 4 bidders
5. Eclipse project files that can be imported by creating a new project in Eclipse and selecting the "Create project from existing source"
6. confige.properties - initial state of the english auction.
7. configd.properties - initial state of the dutch auction.

NOTE: There is a 20sec timeout in the beginning in order the Sniffer agent to be started.

Solution Overview:
    
1. Agents	
mas.agent.Auctioneer and  mas.agent.Bidder

2. Communication
The agents communicate to each other by sending ACL messages either to the AUCTION topic or personally.

2.1. English single unit auction
	1.The auctioneer sends a FIPA-REQUEST with the auction configuration.
	2.Bidders send bids to the auctioneer only.
	3.The auctioneer announces the successful bids to the AUCTION topic.
	4.Bidders receive the successful bids and bid again if possible.
	5.After 5 sec timeout with no bids the Auctioneer announces the winner (including the prize to pay) to the topic.
	6. In case of the reservation prize is not met or no bids were announced the Auctioneer sends a FAILURE message to the topic. 

2.2. Dutch multi unit auction
	1.The auctioneer sends a FIPA-REQUEST with the auction configuration.
	2.The auctioneer sends prices (lowering) to the AUCTION topic.
	3.The Bidders receive the prices and decide if they want to bid.
	3.The auctioneer announces the successful bids to the AUCTION topic.
	4.Bidders receive the successful bids and bid again if possible.
	5.After a timeout with no bids the Auctioneer lowers the price and reannounces it
	6.The auction ends when the rezervation price is met or when all the goods are exhausted.


The content of the messages is based on user-defined ontology (mas.onto.AuctionOntology) containing concepts (like REGISTER, BID, GOOD, WINNER etc.).
See mas.onto package.

3. Behaviors
See mas.behaviour.auctioneer and mas.behaviour.bidder packages.
	
4. Strategies
The configuration file contains the strategy to use. Then each time the bidder should bid it asks the strategy for the next bid.
Implemented strategies:
All In (ALL_IN) -  The agent tries to bid his full budget.
Single unit English with risk factor (RISK_FACTOR) - The agent increases the last highest bid with the announced minimum step and sends it based on probability risk factor.

MultiUnitDutchAllOrNothing (MUDAON)
- the agent bids only if there are enough items and takes them all if he has enough budget in order to have them all
- there is also a risk factor involved, which may, even if the conditions above are true, result in not bidding (hoping for a lower price)

MultiUnitDutch (MUD)
- the agent bids one item at a time, eventually to get lower prices for the others, but also to get rid of some competitors that use the AllOrNothing strategy
- here there is too a risk factor that functions in the same way.
See mas.agent.strategy

5. Running and Configuration
A match can be started by running the "run_english.bat" or "run_dutch.bat".
To change the initial setup, edit the files.


