# Recursive Tile Map Growth

A simple recursive algorithm used to generate corridors and rooms inside of a grid based tile map. 
The core of the algorithm has been uploaded alongside a built version for demonstration purposes.

![](https://i.imgur.com/FAp9Rkj.png)


## To use the built jar, make sure you have the lastest verson of Java installed.

### The title of the window will show you the current generation parameters as well as more useful information.

**Original Build** is the original version of the algorithm, to compare progress.

**Model Build** is the newest version of the algorithm **with** animated showoff.

**Model Build - Realtime Generation** is the newest version of the algorithm **without** animated showoff.

## Controls

**G** to generate a new map

**Arrow Keys** to move the camera around

**- and =** to zoom

**Numpad** to select an alternate floor

## General Explanation

The algorithm begins by checking that it is still within the bounds of the map, then checks neighboring tiles to make sure no tile types that should be avoided are nearby ( such as Rooms for Corridor tiles )

After checks are complete, the **Corridor** tile type is added to the current position. Tile Types are stored a an Enum in another class and only represent an arbitrary type. Many more types can be used.

The algorithm then proceeds to RNG rolls, which implies generating multiple **float** type numbers from 0 to 1 and checking if they are lower than the **Branch**, **Turn**, **End**,**Room**,**Stairs** and **Model** to induce their respective effects to the current process. Since we are checking if the generated number is lower, a parameter of 1 will ensure the effect happens every time.

- **Branch** causes the corridor to branch into a perpendicular corridor, the direction of which is determined by a boolean random roll.

- **Turn** converts a currently branching corridor into a single "turn" by calling return once the first branch has completed.

- **End** suspends the current corridor by immediately calling return.

- **Room** adds an entrance and calls the room generation algorithm to grow a room, the style and type of which depending on the orientation of the entrance.

- **Stairs** adds stairwells towards the top or bottom of the dungeon, also creating new corridors to grow a new floor.

- **Model** adds a pre made model by chosing randomly from the input list of models.

More of those random parameters can be added to generate more desirable results.

### Models

**Models** have been recently added and combine the manual level design capabilities of a human to the growth of the level by randomly chosing from a set of premade tile type arrays. Those models, which can be of any size and span multiple floors, are then rotated and placed by the algorithm.

Code samples for model placement, rotation and creation are included in the Source folder.

### Deadend Removal

**Deadends** can now be found and removed using a couple algorithms, which have been added to the Source folder. 

Essentially, any **Corridor** type tile that has less than 2 non empty immediate neighbors can be considered to be deadends. They are found and removed after the map is generated to give them a cleaner layout, as seen below.

**Example 1 - Before**

![](http://i.imgur.com/GXrUtDr.png)

**Example 1 - After**

![](http://i.imgur.com/RUcvX9Y.png)

**Example 2 - Before**

![](http://i.imgur.com/qD2KEnF.png)

**Example 2 - After**

![](http://i.imgur.com/DfaXJdv.png)

Of course, it is also possible to to single passes of the find+remove algorithms to simply shorten the deadends rather than completely removing them.

### Post Generation Triggers

**Post Generation Triggers** have recently been added as a way to invoke generation algorithms after the main layout has been generated. 

For example, a room may include an alternate exit or entrance which is not initlally connected to the rest of the layout.

PGTriggers allows the storage of which position on your map require post generation actions, which action to execute and in what orientation.

For the case of our alternate room entrance, simply adding a PGTrigger at the position of this entrance with the "Grow Corridor" behavior will start a new corridor from this point and hopefully connect that alternate entrance to another part of your layout.

PGTriggers can also be used to generate extra rooms, stairs and models.
