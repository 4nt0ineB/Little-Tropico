package dutinfo.console;


import dutinfo.game.*;
import dutinfo.game.events.Event;
import dutinfo.game.events.Scenario;
import dutinfo.game.society.Faction;
import dutinfo.game.society.Field;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.fusesource.jansi.*;
import java.util.*;

import static org.fusesource.jansi.Ansi.Color.*;
import static org.fusesource.jansi.Ansi.ansi;

/**
 * Tropico App main
 *
 */
public class App
{



    public static void main( String[] args )
    {
        startConsole();
    }

    private static void startConsole(){
        //AnsiConsole.systemInstall(); -- décommenter avant de faire le jar
        System.out.println( Color.ANSI_BBLUE +
                "\n████████╗██████╗  ██████╗ ██████╗ ██╗ ██████╗ ██████╗\n" +
                  "╚══██╔══╝██╔══██╗██╔═══██╗██╔══██╗██║██╔════╝██╔═══██╗\n" +
                  "   ██║   ██████╔╝██║   ██║██████╔╝██║██║     ██║   ██║\n" +
                  "   ██║   ██╔══██╗██║   ██║██╔═══╝ ██║██║     ██║   ██║\n" +
                  "   ██║   ██║  ██║╚██████╔╝██║     ██║╚██████╗╚██████╔╝\n" +Color.ANSI_ORANGE +
                  "                                  ##########\n"+
                  "                              ###################\n"+
                  "                          #############"+Color.ANSI_GREEN+",,,,.....,;;"+Color.ANSI_ORANGE+"                   \n"+
                  "                        ############"+Color.ANSI_GREEN+",``  ,,,,...._ ''`,.''``.,"+Color.ANSI_ORANGE+"          \n"+
                  "_______________________############"+Color.ANSI_GREEN+",`   ,'"+Color.ANSI_ORANGE+"__"+Color.ANSI_GREEN+",,,,,.;:,"+Color.ANSI_ORANGE+"________"+Color.ANSI_GREEN+"\n"+
                  "                                 ,` ,'',,`` ,.,..,.  .,   . ,.  ``,    \n"+
                  "                                 ,` ,'',,`` ,.,..,.  .,   . ,.  ``,    \n"+
                  "                                ,`,' ,`  ,,`      "+Color.ANSI_GREY+"|__|"+Color.ANSI_GREEN+" ',  ', `,   `,  \n"+
                  "                              ,','  ; ,,`        "+Color.ANSI_GREY+"|__|"+Color.ANSI_GREEN+"    ',  ', `,  ', \n"+
                  "                             :.'  .`,`          "+Color.ANSI_GREY+"|__|"+Color.ANSI_GREEN+"       ', ',  ',  ; \n"+
                  "                                  :`           "+Color.ANSI_GREY+"|__|"+Color.ANSI_GREEN+"         ;  ;    ; ;\n"+
                  "                         "+Color.ANSI_YELLOW+"___,,,---....___     "+Color.ANSI_GREY+"|__|"+Color.ANSI_GREEN+"           ; ;     ;\n"+
                  "                  "+Color.ANSI_YELLOW+"__,--''                ```-"+Color.ANSI_GREY+"|__|"+Color.ANSI_YELLOW+",___        "+Color.ANSI_GREEN+";;';\n"+
                  "              "+Color.ANSI_YELLOW+"_,-'                          "+Color.ANSI_GREY+"|__|"+Color.ANSI_YELLOW+"     ```--,,_"+Color.ANSI_GREEN+";:.\n"+
                  "            "+Color.ANSI_YELLOW+",'                             "+Color.ANSI_GREY+"|__|"+Color.ANSI_YELLOW+"              `',,      \n"+
                  "            "+Color.ANSI_YELLOW+"|                             "+Color.ANSI_GREY+"|__|"+Color.ANSI_YELLOW+"                   `',   \n"+
                  "     "+Color.ANSI_GREY+"_"+Color.ANSI_YELLOW+"      \\,                            "+Color.ANSI_GREY+"|__|"+Color.ANSI_YELLOW+"                      `, \n"+
                  "    "+Color.ANSI_GREY+"| \\."+Color.ANSI_YELLOW+"      \\_                                                     ,`\n"+
                  "    "+Color.ANSI_GREY+"|___\\"+Color.ANSI_YELLOW+"       `-,,._____                  Antoine B. & Timothée L.,'  \n"+
                  "                          `'`'`'`'----,,.,,_____..,,,,-----`'`'`'`'   \n"+
                  "                           "+Color.ANSI_GREY+" __                                         \n"+
                  "                          .// |                               |\\        \n"+
                  "                        .//   |                                         \n"+
                  "                       /______|\n"

        );

        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n"+
                Color.ANSI_ITALIC+"| In Tropico you play the role of a young dictator on a tropical island.|" +
                "\n| Freshly elected as President, you will have the heavy task of making  |" +
                "\n| this new mini-republic prosper.                                       |\n"+
                "+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++"
        );

        System.out.println(Color.ANSI_CYAN+""+Color.ANSI_BOLD+"Enter any to continue: ");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();




        int menu = 1;

        while (menu == 1){
            System.out.println(Color.ANSI_RESET+""+ Color.ANSI_CYAN+""+
                    "╔══-----------MENU-------------╗\n" +
                    "(1)         New Game           |\n" +
                    "|-|----------------------------|\n" +
                    "(2)          Scores            |\n" +
                    "|-|----------------------------|\n" +
                    "(3)           Quit             |\n"+
                    "╚══----------------------------╝\n"
            );
            int a = 0;
            do {
                try {
                    System.out.print(Color.ANSI_CYAN+ "Choice: ");
                    a = scanner.nextInt();
                    System.out.print("\n");
                }catch (InputMismatchException e) {
                    System.out.print(Color.ANSI_RED +"Invalid. \n");
                    a = 0;
                }
                scanner.nextLine();
            }while(a ==0 || a > 3);

            switch (a){
                case 1:
                    startGameConsole();
                    break;
                case 2:
                    System.out.print(Color.ANSI_RED +"Not implemented. \n");
                    break;
                default:
                    menu = 0;
            }

        }

        AnsiConsole.systemUninstall();
    }

    private static void startGameConsole(){
        Game game = Game.initGame();
        StringBuilder sb = new StringBuilder();
        Scanner scanner = new Scanner(System.in);

        int partyParameters = 1;
        while(partyParameters == 1){


            // Chose a scenario
            List<Scenario> scenarios = game.getScenarios();
            int numScenar = 0;
            do {
                try {
                    sb.append("╔══-----------Scenarios--("+scenarios.size()+")-----------╗\n");
                    for(int i = 0; i < scenarios.size(); i++){
                        sb.append(i).append(")").append(scenarios.get(i));
                    }
                    System.out.print(sb);
                    sb.setLength(0);
                    System.out.print("\n");
                    System.out.print(Color.ANSI_CYAN+ "Pic a scenario: ");
                    numScenar = scanner.nextInt();
                    System.out.print("\n");
                }catch (InputMismatchException e) {
                    System.out.print(Color.ANSI_RED +"Invalid. \n");
                    numScenar = 0;
                }
                scanner.nextLine();
            }while(numScenar < 0 || numScenar > scenarios.size()-1);

            // Island's name and President name
            String islandName = "";
            String presidentName = "";
            do {
                try {

                    System.out.print("══----------- What's the name of your island? -----------══\n");
                    System.out.print(Color.ANSI_CYAN+ "Pic a name: ");
                    islandName = scanner.nextLine();
                    System.out.print("\n");
                    System.out.print("══----------- What's your name ? -----------══\n");
                    System.out.print(Color.ANSI_CYAN+ "Pic a name: ");
                    presidentName = scanner.nextLine();

                }catch (InputMismatchException e) {
                    System.out.print(Color.ANSI_RED +"Invalid. \n");
                    islandName = "";
                    presidentName = "";
                }
                scanner.nextLine();
            }while(islandName.isEmpty() || presidentName.isEmpty());




            break;


        }

    }

    private enum Color{
        ANSI_ITALIC("\u001B[3m"),
        ANSI_BOLD ("\u001B[1m"),
        ANSI_RESET ("\u001B[0m"),
        ANSI_GREY ("\033[38;5;59m"),
        ANSI_BLACK ("\u001B[30m"),
        ANSI_RED ("\u001B[31m"),
        ANSI_GREEN ("\u001B[32m"),
        ANSI_YELLOW ("\u001B[33m"),
        ANSI_BLUE ("\u001B[34m"),
        ANSI_PURPLE ("\u001B[35m"),
        ANSI_CYAN ("\u001B[36m"),
        ANSI_WHITE ("\u001B[37m"),
        ANSI_ORANGE ("\033[38;5;202m"),
        ANSI_BBLUE ("\033[38;5;33m"),
        ANSI_BLACK_BACKGROUND ("\u001B[40m"),
        ANSI_RED_BACKGROUND("\u001B[41m"),
        ANSI_GREEN_BACKGROUND ("\u001B[42m"),
        ANSI_YELLOW_BACKGROUND ("\u001B[43m"),
        ANSI_BLUE_BACKGROUND ("\u001B[44m"),
        ANSI_PURPLE_BACKGROUND("\u001B[45m"),
        ANSI_CYAN_BACKGROUND("\u001B[46m"),
        ANSI_WHITE_BACKGROUND("\u001B[47m");

        private final String str;
        Color(String str){
            this.str = str;
        }


        @Override
        public java.lang.String toString() {
            return str;
        }
    }
}
