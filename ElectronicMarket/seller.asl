// Agent Seller in project ElectronicMarket.mas2j

/* Initial beliefs and rules */

/* Initial goals */

!init.

/* Plans */

+!init : .my_name(seller)  <- +offer(dvd, 100);
								+offer(dvd, 50);
								+offer (dvd, 70);
							   +offer(camera, 50).
+offer(Name, Price) : true <- .send(matchmaker, tell, offer(Name, Price)).

+proposal(Name, Price)[source(Buyer)] : offer(Name, Price) 
	<-  .send(Buyer, tell, accept(Name,Price));
	    -offer(Name, Price);
		.print("Item with ", Name," and price ",Price, " sold!");
		.send(matchmaker, tell, remove_offer(Name)).
