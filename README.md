# Market-Checkouts
Simulation of Market Checkouts (Concurrency and Parallelism)
Project description


# Supermarket Checkout

## A big supermarket chain wants a simulation of their checkouts. They regularly face questions such as:
* If we use six checkouts instead of five, how much does that reduce the waiting time for each customer?
* Is it worth it?
* Is it useful to open another "5 items or less" checkout? Or should it be "10 items or less"?

## There is some new technology on the market: scanners that would reduce the scanning time for each item (they are much more reliable). But three of the new scanners cost as much as five of the old ones -
* is it worth buying them? 
* Does that really make a difference?

## Implementation
* a variable number of functioning checkouts (1 to 8).
* the number of products for each trolley to be generated randomly within a user specified range (1 to 200).
* the time for each product to be entered at the checkout to be generated randomly within a user specified range (.5 to 6).
* one or more checkouts to have a restriction on the number of items e.g. "5 items or less". 
* the rate the customer arrive at the checkouts to be generated randomly within a user specified range (0 to 60).

## Implementations record: 
* total wait time for each customer
* total utilization for each checkout
* total products processed
* average customer wait time
* average checkout utilization
* average products per trolley
* The number of lost customers (Customers will leave the store if they need to join a queue more than six deep)


