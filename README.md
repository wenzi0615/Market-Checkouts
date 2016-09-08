# Market-Checkouts
Simulation of Market Checkouts (Concurrency and Parallelism)
Project description


Supermarket Checkout
A big supermarket chain wants a simulation of their checkouts. They regularly face questions such as:
•	If we use six checkouts instead of five, how much does that reduce the waiting time for each customer?
•	Is it worth it?
•	Is it useful to open another "5 items or less" checkout? Or should it be "10 items or less"?
There is some new technology on the market: 
scanners that would reduce the scanning time for each item (they are much more reliable). But three of the new scanners cost as much as five of the old ones -
•	is it worth buying them? 
•	Does that really make a difference?
They want you to simulate checkout queues to answer these and similar questions.
Implementation
Your implementation should allow for:
•	a variable number of functioning checkouts (1 to 8).
•	the number of products for each trolley to be generated randomly within a user specified range (1 to 200).
•	the time for each product to be entered at the checkout to be generated randomly within a user specified range (.5 to 6).
•	one or more checkouts to have a restriction on the number of items e.g. "5 items or less". 
•	the rate the customer arrive at the checkouts to be generated randomly within a user specified range (0 to 60).
Your implementations should record the following: 
•	total wait time for each customer
•	total utilization for each checkout
•	total products processed
•	average customer wait time
•	average checkout utilization
•	average products per trolley
•	The number of lost customers (Customers will leave the store if they need to join a queue more than six deep)
A skeletal implementation of a graphical checkout application has been provided. You do not have to use this code and are free to change it or develop your own independently. A separate thread must be used for each checkout with an appropriate synchronization scheme of your choosing. 
Your solution should provide both safety and liveness.
Work should be presented both to the class in a 10 minute presentation with questions and as a folder complete with 
•	a cover page and a table of contents 
•	a class diagram using UML notation 
•	class documentation in the same format as the java class libraries API
•	documentation which include a description of the class, attributes and methods.
•	detailed test strategy 


