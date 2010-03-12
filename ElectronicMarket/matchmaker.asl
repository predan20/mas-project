// Agent Matchmaker in project ElectronicMarket.mas2j

/* Initial beliefs and rules */
//item (id, name, price, type (offer/request), agent_name)
//update the beliefs in case item is sold
current_item_id(1).
item (ID, Name, Price, Type, Agent) :- current_item_id(ID+1) & not current_item_id(ID).
//from here it's new
cheapest(item (ID, Name, Price, Type, Agent)):- item (ID, Name, Price, Type, Agent)& not( thereAreCheaper(item (ID, Name, Price, Type, Agent))).
thereAreCheaper(item (ID, Name, Price, Type, Agent)):- item (ID, Name, Price, Type, Agent) & item (_, Name, Price2, Type, _)& Price>Price2.



/* Initial goals */

!start.

/* Plans */

+!start : true <- .print("Electronic Market open!").

//handle messages for registering requested and offered items
+offer(Name, Price)[source(A)] :  current_item_id(Id) 
	<-  +item(Id, Name, Price, offer, A);
		.print("Item ", Name, " offered.").
+requested(Name, Price)[source(A)] : current_item_id(X) 
	<-  +item(X, Name, Price, request, A);
		.print("Item ", Name, " requested.").

//search plan when there is complete match (both name and price) 
+search(Type, Name, Price)[source(A)] : item(ID, Name, Price, Type, B) 
	<- .send(A, tell, item(ID, Name, Price, Type, B)); 
		-search(Type, Name, Price)[source(A)];
		.print("Match with item ", Name," and price ",Price," from ", B, " for ", A).
//search plan when there is no price specified
+search(Type, Name, 0)[source(A)] : cheapest(item (ID, Name, Price, Type, B))//here was the old one: item(ID, Name, Price, Type, B) 
	<- .send(A, tell, item(ID, Name, Price, Type, B)); 
		-search(Type, Name, 0)[source(A)];
		.print("Match with item ", Name," and price ",Price," from ", B, " for ", A);.
//search failure.
+search(Type, Name, Price)[source(A)] 
	: ((Price > 0) & (not item(ID, Name, Price, Type, B))) | ((Price=0) & (not item(ID, Name, _, Type, B)))
	<-  .send(A, tell, empty_search(Name, Price)); 
		-search(Type, Name, Price)[source(A)];
		.print("Search failed");.

//update beliefs in case item is sold
+remove_offer(Name)[source(A)] : item(Id, Name, Price, offer, A)
	<- -item(Id, Name, Price, offer, A);
		.print("Offered item removed").
+remove_request(Name)[source(A)] : item(Id, Name, Price, request, A)
	<- -item(Id, Name, Price, request, A);
		.print("Requested item removed").
										



