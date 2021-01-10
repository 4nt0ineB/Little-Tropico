# Little Tropico
Very little game made with Java, inspired by Tropico or Reigns. ~~In console first, with a GUI later~~ Game with a GUI.

This game is based on the original game Tropico or Reign. The main goal of this project was to learn more on java developpement and on production.

### Make your own scenarios and events 
Little game of management simulation. You are the dictator of an island and your role is to make it prosper despite of the events that will occur.


```Bash
Game directory
│
│   trpc_xx.jar
│   Tropico.exe
│   README.txt
│
├───resources/
│   └───view/
│   └───scenarios/            <──── Append you custom scenario here. Unique scenario name !
│   │   │   sandbox.json
    │   │   coldwar.json 
    │   │   ...          
    │	       
    ├───events
    │   ├───Commons           <──── The events from "Commons" directory will be in every games.
    │   │       base1.json
    │   │
    │   │ 		      <──── Make your own events and groups them together in a package 
    │   │		                 (e.g: the package "naturaldisasters" will contain for example 
    │   │				  "tsunami1.json","tsunami2.json","uricane.json", etc...")
    │   └───"Custom event package"    
    │           event_custom1.json
```


To make your owns you can take example on the scenarios and events already created, or the "blueprint.json" (in the resources folder) where all parameters are explained.

Make sure that all events / event packages / scenarios / have unique names

Present in the game at the moment : 

    ▀ Factions: Capitalists, Communists, Liberals, Religious, Militarists, Ecologists, Nationalists, Loyalists

    ▀ Fields: Industry, Agriculture,
