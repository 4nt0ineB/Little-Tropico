# Tropico
Very little game made with Java, inspired by Tropico or Reigns (in console first, with a GUI after).

This game is developed in an open source evolutionary logic. 
So, **you can add more events and scenarios to the game** in the subfolder *src > main > ressources*
in adding a folder with the name of your choice (where you will put your events) for the scenario. 
And then by adding it to the json in *src > main > ressources > init_scenarios.json*.

```Bash
Tropico
│   LICENSE
│   README.md
├───src
│   └───main
│       │
│       └───resources
│               init_scenarios.json
│               test.json
|               └─── """scenario's folder name"""
│                       yourevent1.json
│                       yourevent2.json
│                       youreventx.json
```

