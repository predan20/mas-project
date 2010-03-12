// Agent Seller in project ElectronicMarket.mas2j

/* Initial beliefs and rules */

/* Initial goals */

!init.

/* Plans */

+!init : .my_name(seller1)  <- +offer(dvd, 100);
								+offer(dvd, 50).
+!init : .my_name(seller2)  <- +offer(dvd, 150);
								+offer (dvd, 70);
							   +offer(camera, 10).							   
+offer(Name, Price) : true <- .send(matchmaker, tell, offer(Name, Price)).

+proposal(Name, Price)[source(Buyer)] : offer(Name, Price) 
	<-  -offer(Name, Price);
		.send(Buyer, tell, accept(Name,Price));
		.print("Item with ", Name," and price ",Price, " sold to ", Buyer, "!");
		-proposal(Name, Price)[source(Buyer)];
		.send(matchmaker, tell, remove_offer(Name, Price)).
+proposal(Name, Price)[source(Buyer)] : not offer(Name, Price) 
	<-  -proposal(Name, Price)[source(Buyer)];
		.send(Buyer, tell, reject(Name,Price)).

