# Tropico
Very little game made with Java, inspired by Tropico or Reigns (in console first, with a GUI after).

This game is developed in an open source evolutionary logic. 
**You can add more events and scenarios to the game** in the subfolder *src > main > ressources > scenarios*
in adding a folder with the name of your choice (where you will put your events) for the scenario. 

```Bash
Tropico
│   LICENSE
│   README.md
├───src
│   └───main
│       │
│       └───resources
│           │    factions.json      <-- add your custom classe names, right here
│           |    fields.json        <-- here your custom field of activity names
|           |
            ├───events
            │   ├───Commons                <-- The events from this folder will be in every games
            │   │       base1.json
            │   │
            |   | <--------------------------- make your own events and put them in a folder here
            │   └───"Custom event package"    
            │           event_custom1.json
            │
            └───scenarios
                    sandbox.json
```

### Format
If you want to make your own scenarios and events make sure to respect the correct format of the .json. To do so, you can check out the example files (explenation in it), or either look by yourself in those already maked.
