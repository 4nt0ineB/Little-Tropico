package dutinfo.console;

import dutinfo.game.*;
import dutinfo.game.events.Event;
import dutinfo.game.events.Scenario;
import dutinfo.game.society.Faction;
import dutinfo.game.society.Field;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.*;

/**
 * Tropico App main
 *
 */
public class App
{
    public static final String ANSI_ITALIC = "\u001B[3m";
    public static final String ANSI_BOLD = "\u001B[1m";

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREY = "\033[38;5;59m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String ANSI_ORANGE = "\033[38;5;208m";

    public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";


    public static void main( String[] args )
    {
        Game game = Game.initGame();
        startGameConsole(game);
    }



    private static void startGameConsole(Game game){

        System.out.println( ANSI_BLUE +
                "\n████████╗██████╗  ██████╗ ██████╗ ██╗ ██████╗ ██████╗\n" +
                  "╚══██╔══╝██╔══██╗██╔═══██╗██╔══██╗██║██╔════╝██╔═══██╗\n" +
                  "   ██║   ██████╔╝██║   ██║██████╔╝██║██║     ██║   ██║\n" +
                  "   ██║   ██╔══██╗██║   ██║██╔═══╝ ██║██║     ██║   ██║\n" +
                  "   ██║   ██║  ██║╚██████╔╝██║     ██║╚██████╗╚██████╔╝\n" +ANSI_ORANGE +
                  "                                  ##########\n"+
                  "                              ###################\n"+
                  "                          #############"+ANSI_GREEN+",,,,.....,;;"+ANSI_ORANGE+"                   \n"+
                  "                        ############"+ANSI_GREEN+",``  ,,,,...._ ''`,.''``.,"+ANSI_ORANGE+"          \n"+
                  "_______________________############"+ANSI_GREEN+",`   ,'"+ANSI_ORANGE+"__"+ANSI_GREEN+",,,,,.;:,"+ANSI_ORANGE+"________"+ANSI_GREEN+"\n"+
                  "                                 ,` ,'',,`` ,.,..,.  .,   . ,.  ``,    \n"+
                  "                                 ,` ,'',,`` ,.,..,.  .,   . ,.  ``,    \n"+
                  "                                ,`,' ,`  ,,`      "+ANSI_GREY+"|__|"+ANSI_GREEN+" ',  ', `,   `,  \n"+
                  "                              ,','  ; ,,`        "+ANSI_GREY+"|__|"+ANSI_GREEN+"    ',  ', `,  ', \n"+
                  "                             :.'  .`,`          "+ANSI_GREY+"|__|"+ANSI_GREEN+"       ', ',  ',  ; \n"+
                  "                                  :`           "+ANSI_GREY+"|__|"+ANSI_GREEN+"         ;  ;    ; ;\n"+
                  "                         "+ANSI_YELLOW+"___,,,---....___     "+ANSI_GREY+"|__|"+ANSI_GREEN+"           ; ;     ;\n"+
                  "                  "+ANSI_YELLOW+"__,--''                ```-"+ANSI_GREY+"|__|"+ANSI_YELLOW+",___        "+ANSI_GREEN+";;';\n"+
                  "              "+ANSI_YELLOW+"_,-'                          "+ANSI_GREY+"|__|"+ANSI_YELLOW+"     ```--,,_"+ANSI_GREEN+";:.\n"+
                  "            "+ANSI_YELLOW+",'                             "+ANSI_GREY+"|__|"+ANSI_YELLOW+"              `',,      \n"+
                  "            "+ANSI_YELLOW+"|                             "+ANSI_GREY+"|__|"+ANSI_YELLOW+"                   `',   \n"+
                  "     "+ANSI_GREY+"_"+ANSI_YELLOW+"      \\,                            "+ANSI_GREY+"|__|"+ANSI_YELLOW+"                      `, \n"+
                  "    "+ANSI_GREY+"| \\."+ANSI_YELLOW+"      \\_                                                     ,`\n"+
                  "    "+ANSI_GREY+"|___\\"+ANSI_YELLOW+"       `-,,._____                  Antoine B. & Timothée L.,'  \n"+
                  "                          `'`'`'`'----,,.,,_____..,,,,-----`'`'`'`'   \n"+
                  "                           "+ANSI_GREY+" __                                         \n"+
                  "                          .// |                               |\\        \n"+
                  "                        .//   |                                         \n"+
                  "                       /______|\n"

                );

        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++"+
                ANSI_ITALIC+"| In Tropico you play the role of a young dictator on a tropical island.|" +
                "\n| Freshly elected as President, you will have the heavy task of making  |" +
                "\n| this new mini-republic prosper.                                       |\n"+
                "+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++"
        );

        System.out.println(ANSI_CYAN+ANSI_BOLD+"Press any to continue: ");

        boolean menu = true;
        /*
        while (menu){




            boolean party = true;
            while(party){

            break;




            }
            break;
        }
         */
    }

}
