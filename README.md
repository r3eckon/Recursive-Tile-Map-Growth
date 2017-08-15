# Recursive Tile Map Growth

A simple recursive algorithm used to generate corridors and rooms inside of a grid based tile map. 
The core of the algorithm has been uploaded alongside a built version for demonstration purposes.

![](http://i.imgur.com/TEuDaw9.png)


## To use the built jar, make sure you have the lastest verson of Java installed.

**G** to generate a new map

**Arrow Keys** to move the camera around

**- and =** to zoom

## Short Explanation

The algorithm begins by checking that it is still within the bounds of the map, then checks neighboring tiles to make sure no tile types that should be avoided are nearby ( such as Rooms for Corridor tiles )

After checks are complete, the **Corridor** tile type is added to the current position. Tile Types are stored a an Enum in another class and only represent an arbitrary type. Many more types can be used.

The algorithm then proceeds to RNG rolls, which implies generating multiple **float** type numbers from 0 to 1 and checking if they are lower than the **Branch**, **Turn**, **End** and **Room** parameters to induce their respective effects to the current process. Since we are checking if the generated number is lower, a parameter of 1 will ensure the effect happens every time.

- **Branch** causes the corridor to branch into a perpendicular corridor, the direction of which is determined by a boolean random roll.

- **Turn** converts a currently branching corridor into a single "turn" by calling return once the first branch has completed.

- **End** suspends the current corridor by immediately calling return.

- **Room** adds an entrance and calls the room generation algorithm to grow a room, the style and type of which depending on the orientation of the entrance.

More of those random parameters can be added to generate more desirable results.
