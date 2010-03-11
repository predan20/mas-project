// Agent Seller in project ElectronicMarket.mas2j

/* Initial beliefs and rules */

/* Initial goals */

!init.

/* Plans */

+!init : .my_name(seller)  <- +offer(dvd, 200);
							   +offer(camera, 50).
+offer(Name, Price) : true <- .send(matchmaker, tell, offer(Name, Price)).
