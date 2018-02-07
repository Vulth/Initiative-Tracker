# Initiative Tracker

This is an initiative tracker I wrote to be used for D&D to make it easier for me to roll initiative for a bunch of enemies all at once. 
By making pre made monster groups into text files, it can be useful for quickly loading up a fight and keeping the game moving smoothly.

## File Format

Each character in a file is to be given their own line. Make sure that each character in a fight has a unique name, or else the functions may not work properly. Examples of each file have been included.

### PC File format 

```
Frodo Baggins | 36 36
```

The program will concatenate all words up to the vertical line into the character name 

The first number is current health 

The second number is maximum health 

### NPC File format

```
3 Goblin 1 | 10 10
```

First number is the total modifier to initiative the NPC has

All words up to the vertical line make up the NPC name

NPC's current health

NCP's maximum health
