// Agent Seller in project ElectronicMarket.mas2j

/* Initial beliefs and rules */
offer(item1, 100).

/* Initial goals */

!start.

/* Plans */

+!start : offer(Name, Price) <- .send(matchmaker, tell, offer(Name, Price)).

