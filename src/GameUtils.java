import java.util.Scanner;

public class GameUtils {
    
    public static void clearScreen(){ //metodo per pulire lo schermo
        for(int i = 0; i < 500; i++){
            System.out.println("");
            System.out.flush();
        }
    }

    public static void systemPause(){ //metodo per mettere in pausa il sistema
        Scanner tastiera = new Scanner(System.in);
        System.out.print("Premere invio per continuare...");
        tastiera.nextLine();
    }
}
