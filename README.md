distributed-leader-election
===========================

Implementation of a basic distributed consensus majority algorithm where
an arbitrary number of nodes independently elect a leader (Master)

Split brain case (2 elected leaders) is prevented by ensuring that only the larger half of nodes (which is assumed to be online, but unreachable) will have a leader, guaranteeing that there will be at most one leader at any point in time.

Scripting / Command Console
----
A simple command console allows interactive or scripted testing or replaying of distributed scenario.

For example: This script

>online all  
sleep 20  
offline 1  
sleep 20  
online 1  
sleep 20  

... produces the following output
>1 setup  
2 setup  
3 setup  
4 setup  
5 setup  
\>[Node[id=1], Node[id=2], Node[id=3], Node[id=4], Node[id=5]] onlined  
\>sleeping 20s  
Node[id=3] detects change of master to 1  
Node[id=4] detects change of master to 1  
Node[id=5] detects change of master to 1  
Node[id=1] becomes master  
Node[id=2] detects change of master to 1  
\>[Node[id=1]] offlined  
\>sleeping 20s  
Node[id=2] becomes master  
Node[id=3] detects change of master to 2  
Node[id=5] detects change of master to 2  
Node[id=4] detects change of master to 2  
\>[Node[id=1]] onlined  
\>sleeping 20s  
Node[id=1] gives up master  
Node[id=5] detects change of master to 1  
Node[id=1] becomes master  
Node[id=4] detects change of master to 1  
Node[id=3] detects change of master to 1  
Node[id=2] gives up master  

License
----
Apache 2

Credits
----
- Inspired from a [NodeJS implementation](https://github.com/andreyvit/majority.js) and related [SO question](http://stackoverflow.com/questions/4523185/how-to-elect-a-master-node-among-the-nodes-running-in-a-cluster)

- For a robust documented protocol see [Paxos](http://en.wikipedia.org/wiki/Paxos_(computer_science))

