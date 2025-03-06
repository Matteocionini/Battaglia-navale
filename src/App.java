import java.io.IOException;
import java.util.Scanner;

/*
Autore: Matteo Cionini
Riproduzione di battagia navale in programmazione ad oggetti con possibilità di giocare contro l'IA.

Nota: affinché il campo di gioco sia visualizzato correttamente, è necessario mettere il terminale a schermo intero

Data inizio progetto: 23/11/2021
Data fine progetto: 8/12/2021
*/

public class App {
    public static void main(String[] args) throws IOException, InterruptedException{
        Scanner tastiera = new Scanner(System.in);
        GameRules gameElems = new GameRules(); //oggetto con cui richiamare le varie fasi di gioco
        Boolean vittoria_g = false;
        Boolean vittoria_ia = false;

        System.out.println("Benvenuti a battaglia navale"); //scritta di benvenuto
        GameUtils.systemPause();
        GameUtils.clearScreen();

        
        for(int i = 1; i<=4; i++){ //inserimento navi del giocatore di tutti i tipi
            gameElems.insNaviG(i);
            System.out.print("\n");
            GameUtils.systemPause();
            GameUtils.clearScreen();
        }

        for(int j = 1; j<=4; j++){
            gameElems.insNaviIa(j);
        }

        do{
            gameElems.turnoG();
            vittoria_g = gameElems.controllaVittoriaG();
            if(vittoria_g == true){
                break;
            }
            gameElems.turnoIa(); 
            vittoria_ia = gameElems.controllaVittoriaIa();
        } while(vittoria_g == false && vittoria_ia == false);

        System.out.println("\n");

        if(vittoria_ia == true){
            System.out.println("L'IA ha vinto!");
        }
        else{
            System.out.println("Hai vinto!");
        }

        GameUtils.systemPause();
    }
}
