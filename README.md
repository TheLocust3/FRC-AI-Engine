# FRC AI Engine

### What is it?  
An AI engine designed to run a robot completely autonomously for an entire FRC match. For testing, it uses the [FRC-Game-Simulator](https://github.com/TheLocust3/FRC-Game-Simulator) which removes as many complexities as possible so that only high level operations remain, like pickup ball and shoot. In the end, the engine doesn't actually know if it is controlling a real robot or a simulated one because all the specifics are abstracted away.  
  
### To-Do  
 - Finish bindings for the simulator
 - Write algorithms to determine whether to trust vision data or not
 - Write a pathfinding algorithm to move the robot around obstacles
 - Write loads of bindings that bind these operations to the real robot
 - Write a neural network to control overall game strategyg
