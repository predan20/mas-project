// Agent Buyer in project ElectronicMarket.mas2j

/* Initial beliefs and rules */
//empty initialy as different beliefs are added for different buyers using a plan.

/* Initial goals */
!init.

/* Plans */

//initializes the agents' beliefs with items to be requsted on the market.
//each item is identified by name and price. if the price is 0 then it is considered as not specified.
//for each request it is specified if the agent should search for an offer or wait for a seller to approach him
+!init : .my_name(buyer1) <- +request(dvd, 70, search);
							 +request(camera, 50, wait).
+!init : .my_name(buyer2) <- +request(dvd, 0, search);
							 +request(camera, 50, wait).							 

//sends message to the matchmaker that an item is requested when a request is added to the belief base.
+request(Name, Price, search) : true <- .send(matchmaker, tell, requested(Name, Price));!search(Name, Price).
+request(Name, Price, wait) : true <- .send(matchmaker, tell, requested(Name, Price)).

//plan for executing a search goal by sending search request to the matchmaker
+!search(Name, Price) : true <- .send(matchmaker, tell, search(offer, Name, Price)).
//handle search results
+search_result(item(Name, Price, offer, Seller), request(OriginName, OriginPrice))[source(matchmaker)] : true 
	<- .print ("Item found");
		+negotiate(item(Name, Price, offer, Seller), request(OriginName, OriginPrice));
		-search_result(item(Name, Price, offer, Seller), request(OriginName, OriginPrice))[source(matchmaker)].
+empty_search(Name, Price)[source(matchmaker)] : true 
	<- .print("No matching item found!");
		-empty_search(Name, Price)[source(matchmaker)];
		!search(Name, Price).

//negotiation rules
+negotiate(item(Name, Price, offer, Seller), request(OriginName, OriginPrice)) : true 
	<- .print("Sending proposal. Name ", Name, " price ", Price);
		.send(Seller, tell, proposal(Name, Price)).
+accept(Name,Price)[source(Seller)] : negotiate(item(Name, Price, offer, Seller), request(OriginName, OriginPrice))
	<- -negotiate(item(Name, Price, offer, Seller), request(OriginName, OriginPrice));
		-request(Name, _);
		.send(matchmaker, tell, remove_request(Name, Price)).
+reject(Name,Price)[source(Seller)] : negotiate(item(Name, Price, offer, Seller), request(OriginName, OriginPrice))
	<- -negotiate(item(Name, Price, offer, Seller), request(OriginName, OriginPrice));
		.print("Proposal Name ", Name, " price ", Price, " rejected!");
		-reject(Name,Price)[source(Seller)];
		!search(Name, OriginPrice).
