// Agent Seller in project ElectronicMarket.mas2j

/* Initial beliefs and rules */

/* Initial goals */

!init.

/* Plans */
//initializes the agents' beliefs with items to be offered on the market.
//each item is identified by name and price.
//for each offer it is specified if the agent should search for a request or wait for a buyer to approach him
+!init : .my_name(seller1)  <- +offer(dvd, 100, wait);
								+offer(camera, 50, search).
+!init : .my_name(seller2)  <- +offer(dvd, 150, search);
								+offer (dvd, 70, search);
							   +offer(camera, 10, wait).				

//sends message to the matchmaker that an item is requested when a request is added to the belief base.
+offer(Name, Price, _) : true <- .send(matchmaker, tell, offer(Name, Price)).

//plan for executing a search goal by sending search request to the matchmaker
+!search(Name, Price) : true <- .send(matchmaker, tell, search(request, Name, Price)).
//handle search results
+search_result(item(Name, Price, request, Buyer), request(OriginName, OriginPrice))[source(matchmaker)] : true 
	<- .print ("Item found");
		+negotiate(item(Name, Price, request, Buyer), request(OriginName, OriginPrice));
		-search_result(item(Name, Price, request, Buyer), request(OriginName, OriginPrice))[source(matchmaker)].
+empty_search(Name, Price)[source(matchmaker)] : true 
	<- .print("No matching item found!");
		-empty_search(Name, Price)[source(matchmaker)].

//search again only if there is new request added		
+request_added(Name, Price)[source(matchmaker)] 
	: offer(Name, OriginPrice, search)
	<-  -request_added(Name, Price)[source(matchmaker)];
		!search(Name, OriginPrice).


//negotiation rules
+negotiate(item(Name, Price, request, Buyer), request(OriginName, OriginPrice)) : true 
	<- .print("Sending proposal. Name ", Name, " price ", Price);
		.send(Buyer, tell, proposal(Name, Price)).
+accept(Name,Price)[source(Buyer)] : negotiate(item(Name, Price, request, Buyer), request(OriginName, OriginPrice))
	<- -negotiate(item(Name, Price, request, Buyer), request(OriginName, OriginPrice));
		-offer(Name, OriginPrice, _);
		.send(matchmaker, tell, remove_offer(Name, OriginPrice)).
+reject(Name,Price)[source(Buyer)] : negotiate(item(Name, Price, request, Buyer), request(OriginName, OriginPrice))
	<- -negotiate(item(Name, Price, request, Buyer), request(OriginName, OriginPrice));
		.print("Proposal Name ", Name, " price ", Price, " rejected!");
		-reject(Name,Price)[source(Buyer)].

+proposal(Name, Price)[source(Buyer)] : offer(Name, Price, _) 
	<-  -offer(Name, Price, _);
		-proposal(Name, Price)[source(Buyer)];
		.send(Buyer, tell, accept(Name,Price));
		.print("Item ", Name," and price ",Price, " sold to ", Buyer, "!");
		.send(matchmaker, tell, remove_offer(Name, Price)).
+proposal(Name, Price)[source(Buyer)] : not offer(Name, Price, _) 
	<-  -proposal(Name, Price)[source(Buyer)];
		.send(Buyer, tell, reject(Name,Price)).

