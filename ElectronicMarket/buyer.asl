// Agent Buyer in project ElectronicMarket.mas2j

/* Initial beliefs and rules */
request(item1, 100).
request(item2, 50).


/* Initial goals */

!start.

/* Plans */

+!start : request(Name, Price) <- .send(matchmaker, tell, request(Name, Price));!search(Name, Price).
+!search(Name, Price) : true <- .send(matchmaker, tell, search(offer, Name, Price)).

+empty_search(Name, Price)[source(matchmaker)] : true <- .print("No matching item found!");-empty_search(Name, Price)[source(matchmaker)];!search(Name, Price).
+item(ID, Name, Price, Type, B)[source(matchmaker)] : request(Name, Price) <- .print ("Item found").
