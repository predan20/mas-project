// Agent Matchmaker in project ElectronicMarket.mas2j

/* Initial beliefs and rules */
cheapest(item (Name, Price, Type, Agent)):- item (Name, Price, Type, Agent)& not( thereAreCheaper(item (Name, Price, Type, Agent))).
thereAreCheaper(item (Name, Price, Type, Agent)):- item (Name, Price, Type, Agent) & item (Name, Price2, Type, _)& Price>Price2.



/* Initial goals */

!start.

/* Plans */

+!start : true <- .print("Electronic Market open!").

//handle messages for registering requested and offered items
+offer(Name, Price)[source(A)] :  true 
	<-  +item(Name, Price, offer, A);
		.print("Item ", Name, " offered by ", A, ".");
		.broadcast(tell, offer_added(Name, Price)).
+requested(Name, Price)[source(A)] : true 
	<-  +item(Name, Price, request, A);
		.print("Item ", Name, " requested.");
		.broadcast(tell, request_added(Name, Price)).

//search plan when there is complete match (both name and price) 
+search(Type, Name, Price)[source(A)] : item(Name, Price, Type, B) 
	<- .send(A, tell, search_result(item(Name, Price, Type, B), request(Name, Price))); 
		-search(Type, Name, Price)[source(A)];
		.print("Match with item ", Name," and price ",Price," from ", B, " for ", A).
//search plan when there is no price specified. matches the lowest possible price
+search(offer, Name, 0)[source(A)] : cheapest(item (Name, Price, offer, B)) 
	<- .print("Match with item ", Name," and price ",Price," from ", B, " for ", A);
		.send(A, tell, search_result(item(Name, Price, offer, B),request(Name, 0))); 
		-search(offer, Name, 0)[source(A)].
//search failure.
+search(Type, Name, Price)[source(A)] 
	: ((Price > 0) & (not item(Name, Price, Type, B))) | ((Price=0) & (not item(Name, _, Type, B)))
	<-  .send(A, tell, empty_search(Name, Price)); 
		-search(Type, Name, Price)[source(A)];
		.print("Search empty").

//update beliefs in case item is sold
+remove_offer(Name, Price)[source(A)] : item(Name, Price, offer, A)
	<-  -item(Name, Price, offer, A);
		-cheapest(item (Name, Price, offer, A));
		.print("Offered item removed").
+remove_request(Name, Price)[source(A)] : item(Name, Price, request, A)
	<- -item(Name, Price, request, A);
		.print("Requested item removed").
										



