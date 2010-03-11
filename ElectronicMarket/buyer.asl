// Agent Buyer in project ElectronicMarket.mas2j

/* Initial beliefs and rules */

/* Initial goals */
!init.

/* Plans */

+!init : .my_name(buyer1) <- +request(dvd, 100, wait);
							 +request(camera, 50, wait).
+!init : .my_name(buyer2) <- +request(dvd, 0, search);
							 +request(camera, 50, wait).							 

+request(Name, Price, search) : true <- .send(matchmaker, tell, requested(Name, Price));!search(Name, Price).
+request(Name, Price, wait) : true <- .send(matchmaker, tell, requested(Name, Price)).

+!search(Name, Price) : true <- .send(matchmaker, tell, search(offer, Name, Price)).
+empty_search(Name, Price)[source(matchmaker)] : true <- .print("No matching item found!");-empty_search(Name, Price)[source(matchmaker)];!search(Name, Price).

+item(ID, Name, Price, offer, Seller)[source(matchmaker)] : true <- .print ("Item found");+negotiate(item(ID, Name, Price, offer, Seller)).

+negotiate(item(ID, Name, Price, offer, Seller)) : true <- .send(Seller, tell, proposal(Name, Price)).
