import java.util.Random;
import java.util.Scanner;

public class GameRules {

    
    private static int dim = 10;

    private static int num_corazz = 1; //numero di corazzate (dim. 4) in gioco
    private static int num_croc = 2; //numero di crociere (dim. 3) in gioco
    private static int num_sott = 3; //numero di sottomarini (dim. 2) in gioco
    private static int num_ass = 4; //numero di navi d'assalto (dim. 1) in gioco

    private static int dim_corazz = 4;
    private static int dim_croc = 3;
    private static int dim_sott = 2;
    private static int dim_ass = 1;
    
    Casella[][] tabella_g = new Casella[dim][dim]; //tabella su cui il giocatore posiziona le sue navi
    Casella[][] tabella_ia = new Casella[dim][dim]; //tabella su cui l'ia posiziona le sue navi
    Scanner tastiera = new Scanner(System.in);
    Random rand = new Random();
    
    //lista delle navi, in cui salvo la posizione della navi, che uso per poter operare i controlli sullo stato delle navi (affondate, colpite, integre)
    Nave[] corazz_g = new Nave[num_corazz];
    Nave[] croc_g = new Nave[num_croc];
    Nave[] sott_g = new Nave[num_sott];
    Nave[] ass_g = new Nave[num_ass];

    Nave[] corazz_ia = new Nave[num_corazz];
    Nave[] croc_ia = new Nave[num_croc];
    Nave[] sott_ia = new Nave[num_sott];
    Nave[] ass_ia = new Nave[num_ass];

    Ia ia = new Ia();

    
    public GameRules(){
        inizTab();
        inizListaNavi();
    }

    private void inizListaNavi(){
        for(int i = 0; i < num_corazz; i++){
            corazz_g[i] = new Nave(dim_corazz);
            corazz_ia[i] = new Nave(dim_corazz);
        }

        for(int j = 0; j < num_croc; j++){
            croc_g[j] = new Nave(dim_croc);
            croc_ia[j] = new Nave(dim_croc);
        }

        for(int k = 0; k < num_sott; k++){
            sott_g[k] = new Nave(dim_sott);
            sott_ia[k] = new Nave(dim_sott);
        }

        for(int l = 0; l < num_ass; l++){
            ass_g[l] = new Nave(dim_ass);
            ass_ia[l] = new Nave(dim_ass);
        }
    }
    
    private void inizTab(){ //metodo per inizializzare gli array tabella_g e tabella_ia (di default i campi sono null)
        for(int i = 0; i<dim; i++){
            for(int j = 0; j<dim; j++){
                tabella_g[i][j] = new Casella();
            }
        }

        for(int i = 0; i<dim; i++){
            for(int j = 0; j<dim; j++){
                tabella_ia[i][j] = new Casella();
            }
        }
    }
    
    public void insNaviIa(int tipo) throws InterruptedException{

        int selezione_c;
        int selezione_r;
        int selezione_orientamento; //variabile per selezionare l'orientamento da dare alla nave
        int dimensione_n = 0; //variabile in cui, in base al tipo, viene salvata la dimensione della nave
        int numero_n = 0;

        Boolean orient_valido = true;
        Boolean p_sinistra = true;
        Boolean p_destra = true;
        Boolean p_alto = true;
        Boolean p_basso = true;
        Boolean sbocco_disp = true;
        Boolean usata = false;

        int sinistra = 1;
        int destra = 2;
        int alto = 3;
        int basso = 4;

        switch (tipo) { //in base al tipo di nave da posizionare seleziono quante navi posizionare e quanto esse sono grandi
            case 1:
                numero_n = num_corazz;
                dimensione_n = dim_corazz;
                break;

            case 2:
                numero_n = num_croc;
                dimensione_n = dim_croc;
                break;

            case 3:
                numero_n = num_sott;
                dimensione_n = dim_sott;
                break;

            case 4:
                numero_n = num_ass;
                dimensione_n = dim_ass;
                break;
        
            default:
                break;
        }
        
        
        for(int i = 0; i < numero_n; i++){
            
            
            do{ //genero delle coordinate casuali da cui posizionare poi la nave
                sbocco_disp = true;
                selezione_c = rand.nextInt(10);
                selezione_r = rand.nextInt(10);

                p_sinistra=controllaOrientamento(selezione_c, selezione_r, dimensione_n, sinistra, tabella_ia);
                p_destra=controllaOrientamento(selezione_c, selezione_r, dimensione_n, destra, tabella_ia);
                p_alto=controllaOrientamento(selezione_c, selezione_r, dimensione_n, alto, tabella_ia);
                p_basso=controllaOrientamento(selezione_c, selezione_r, dimensione_n, basso, tabella_ia);

                if(p_sinistra == false && p_destra == false && p_alto == false && p_basso == false){
                    sbocco_disp = false;
                }

            } while((tabella_ia[selezione_c][selezione_r].getNave() == true) || controllaAdiacenti(selezione_c, selezione_r, tabella_ia) == false || sbocco_disp == false);

            if(tipo != 4){ //se la nave non è di tipo 4 (dimensione 1) stabilisco come orientare la nave
    
                do{
                    orient_valido = true;
                    
                    selezione_orientamento = 1 + rand.nextInt(4);
    
                    switch (selezione_orientamento) { //verifico se l'orientamento selezionato dall'ia è disponibile (la nave non colpisce né il bordo né un'altra nave ecc...)
                        case 1:
                            if(p_sinistra == false) orient_valido = false;
                            break;
    
                        case 2:
                            if(p_destra == false) orient_valido = false;
                            break;
    
                        case 3:
                            if(p_alto == false) orient_valido = false;
                            break;
    
                        case 4:
                            if(p_basso == false) orient_valido = false;
                            break;
                    
                        default:
                            orient_valido = false;
                            break;
                    }
                } while(orient_valido == false);
                
                switch (selezione_orientamento) { //In base all'orientamento selezionato posiziono la nave
                    case 1:
                        for(int j = 0; j<dimensione_n; j++){
                            tabella_ia[selezione_c-j][selezione_r].addNave();
                        }

                        if(tipo == 1){ //aggiungo alla lista delle navi la nave che ho appena inserito
                            corazz_ia[i].setPosizioniOccupate(selezione_c, selezione_r, sinistra);
                        }
                        if(tipo == 2){
                            croc_ia[i].setPosizioniOccupate(selezione_c, selezione_r, sinistra);
                        }
                        if(tipo == 3){
                            sott_ia[i].setPosizioniOccupate(selezione_c, selezione_r, sinistra);
                        }
                        break;
    
                    case 2:
                        for(int k = 0; k<dimensione_n; k++){
                            tabella_ia[selezione_c+k][selezione_r].addNave();
                        }

                        if(tipo == 1){ //aggiungo alla lista delle navi la nave che ho appena inserito
                            corazz_ia[i].setPosizioniOccupate(selezione_c, selezione_r, destra);
                        }
                        if(tipo == 2){
                            croc_ia[i].setPosizioniOccupate(selezione_c, selezione_r, destra);
                        }
                        if(tipo == 3){
                            sott_ia[i].setPosizioniOccupate(selezione_c, selezione_r, destra);
                        }
                        break;
    
                    case 3:
                        for(int l = 0; l<dimensione_n; l++){
                            tabella_ia[selezione_c][selezione_r-l].addNave();
                        }

                        if(tipo == 1){ //aggiungo alla lista delle navi la nave che ho appena inserito
                            corazz_ia[i].setPosizioniOccupate(selezione_c, selezione_r, alto);
                        }
                        if(tipo == 2){
                            croc_ia[i].setPosizioniOccupate(selezione_c, selezione_r, alto);
                        }
                        if(tipo == 3){
                            sott_ia[i].setPosizioniOccupate(selezione_c, selezione_r, alto);
                        }
                        break;
    
                    case 4:
                        for(int m = 0; m<dimensione_n; m++){
                            tabella_ia[selezione_c][selezione_r+m].addNave();
                        }

                        if(tipo == 1){ //aggiungo alla lista delle navi la nave che ho appena inserito
                            corazz_ia[i].setPosizioniOccupate(selezione_c, selezione_r, basso);
                        }
                        if(tipo == 2){
                            croc_ia[i].setPosizioniOccupate(selezione_c, selezione_r, basso);
                        }
                        if(tipo == 3){
                            sott_ia[i].setPosizioniOccupate(selezione_c, selezione_r, basso);
                        }
                        break;
                
                    default:
                        break;
                }

            }
            else{ //se la nave è grande solo una casella e la casella selezionata dall'utente è valida aggiungo direttamente la nave
                tabella_ia[selezione_c][selezione_r].addNave();
                ass_ia[i].setPosizioniOccupate(selezione_c, selezione_r, sinistra); //aggiungo alla lista delle navi la nave che ho appena inserito
            }

            p_alto = true;
            p_basso = true;
            p_destra = true;
            p_sinistra = true;
            orient_valido = true;
        }
    }

    public void insNaviG(int tipo) throws InterruptedException{ //fase 1 del gioco: il giocatore piazza le sue navi sulla griglia. Il metodo viene richiamato più volte al fine di posizionare tutti i tipi di
        //navi e devo passare al metodo un valore int che corrisponde al tipo di nave che voglio posizionare

        int selezione_c; //variabile per selezionare la colonna in cui l'utente vuole posizionare la nave
        int selezione_r; //variabile per selezionare la riga in cui l'utente vuole posizionare la nave
        int selezione_orientamento; //variabile per selezionare l'orientamento da dare alla nave
        int dimensione_n = 0; //variabile in cui, in base al tipo, viene salvata la dimensione della nave
        int numero_n = 0; //variabile in cui, in base al tipo, viene salvato il numero di navi da inserire
        
        //variabili bool usate per determinare in quali orientamenti, in base alla sua posizione e dimensione, la nave può essere posta
        Boolean orient_valido = true;
        Boolean p_sinistra = true;
        Boolean p_destra = true;
        Boolean p_alto = true;
        Boolean p_basso = true;
        Boolean sbocco_disp = true;

        int sinistra = 1;
        int destra = 2;
        int alto = 3;
        int basso = 4;

        switch (tipo) { //in base al tipo di nave da posizionare seleziono quante navi posizionare e quanto esse sono grandi
            case 1:
                numero_n = num_corazz;
                dimensione_n = dim_corazz;
                break;

            case 2:
                numero_n = num_croc;
                dimensione_n = dim_croc;
                break;

            case 3:
                numero_n = num_sott;
                dimensione_n = dim_sott;
                break;

            case 4:
                numero_n = num_ass;
                dimensione_n = dim_ass;
                break;
        
            default:
                break;
        }

        for(int i = 0; i < numero_n; i++){ //posizionamento nave
            
            GameUtils.clearScreen();
            stampaTabG(true);
            
            do{ //faccio selezionare la casella di partenza su cui posizionare una nave e verifico che la casella sia libera e che non abbia alcuna nave adiacente e che abbia almeno un orientamento valido
                
                sbocco_disp = true;
                
                do{
                    System.out.print("Selezionare la colonna della casella di partenza in cui inserire la " + (i+1) + " nave da " + dimensione_n + " caselle: ");
                    selezione_c = tastiera.nextInt();
                    
                    if(selezione_c < 1 || selezione_c > 10){
                        System.out.println("Selezione non valida");
                    }
                } while(selezione_c < 1 || selezione_c > 10);
    
                do{
                    System.out.print("Selezionare la riga della casella di partenza in cui inserire la " + (i+1) + " nave da " + dimensione_n + " caselle: ");
                    selezione_r = tastiera.nextInt();
                    
                    if(selezione_r < 0 || selezione_r > 10){
                        System.out.println("Selezione non valida");
                    }
                } while(selezione_r < 0 || selezione_r > 10);

                if((tabella_g[selezione_c-1][selezione_r-1].getNave() == true) || controllaAdiacenti(selezione_c-1, selezione_r-1, tabella_g) == false){
                    
                    if(controllaAdiacenti(selezione_c-1, selezione_r-1, tabella_g) == false){
                        System.out.println("Sono gia' presenti una o piu' navi adiacenti a questa casella, non e' possibile avere navi adiacenti");
                    }
                    else{
                        System.out.println("Vi e' gia' una nave in questa casella");
                    }
                }

                

                if(p_sinistra == false && p_destra == false && p_alto == false && p_basso == false){
                    sbocco_disp = false;
                    System.out.println("Non sono disponibili orientamenti possibili per questa nave da questa casella");
                }

            } while((tabella_g[selezione_c-1][selezione_r-1].getNave() == true) || controllaAdiacenti(selezione_c-1, selezione_r-1, tabella_g) == false || sbocco_disp == false);

            p_sinistra=controllaOrientamento(selezione_c-1, selezione_r-1, dimensione_n, sinistra, tabella_g);
            p_destra=controllaOrientamento(selezione_c-1, selezione_r-1, dimensione_n, destra, tabella_g);
            p_alto=controllaOrientamento(selezione_c-1, selezione_r-1, dimensione_n, alto, tabella_g);
            p_basso=controllaOrientamento(selezione_c-1, selezione_r-1, dimensione_n, basso, tabella_g);

            //decremento le selezioni del giocatore di 1 in modo da tornare nell'intervallo 0-9 definito dalla dimensione della tabella di gioco
            selezione_c--;
            selezione_r--;
            
            
            if(tipo != 4){ //se la nave non è di tipo 4 (dimensione 1) chiedo l'orientamento in cui posizionare la nave
    
                do{
                    orient_valido = true;
                    
                    System.out.print("Selezionare l'orientamento della nave [1 sinistra - 2 destra - 3 alto - 4 basso]: ");
                    selezione_orientamento = tastiera.nextInt();
    
                    if(selezione_orientamento < 1 || selezione_orientamento > 4){
                        orient_valido = false;
                    }
    
                    switch (selezione_orientamento) { //verifico se l'orientamento selezionato dall'utente è disponibile (la nave non colpisce né il bordo né un'altra nave)
                        case 1:
                            if(p_sinistra == false) orient_valido = false;
                            break;
    
                        case 2:
                            if(p_destra == false) orient_valido = false;
                            break;
    
                        case 3:
                            if(p_alto == false) orient_valido = false;
                            break;
    
                        case 4:
                            if(p_basso == false) orient_valido = false;
                            break;
                    
                        default:
                            break;
                    }
    
                    if(orient_valido == false){
                        System.out.println("Orientamento selezionato non valido (la nave uscirebbe dal bordo, colliderebbe con un'altra nave o verrebbe a trovarsi adiacente ad un'altra nave");
                    }
                } while(orient_valido == false);
                
                switch (selezione_orientamento) { //In base all'orientamento selezionato posiziono la nave
                    case 1:
                        for(int j = 0; j<dimensione_n; j++){
                            tabella_g[selezione_c-j][selezione_r].addNave();
                        }

                        if(tipo == 1){ //aggiungo alla lista delle navi la nave che ho appena inserito
                            corazz_g[i].setPosizioniOccupate(selezione_c, selezione_r, sinistra);
                        }
                        if(tipo == 2){
                            croc_g[i].setPosizioniOccupate(selezione_c, selezione_r, sinistra);
                        }
                        if(tipo == 3){
                            sott_g[i].setPosizioniOccupate(selezione_c, selezione_r, sinistra);
                        }
                        break;
    
                    case 2:
                        for(int k = 0; k<dimensione_n; k++){
                            tabella_g[selezione_c+k][selezione_r].addNave();
                        }
                        
                        if(tipo == 1){ //aggiungo alla lista delle navi la nave che ho appena inserito
                            corazz_g[i].setPosizioniOccupate(selezione_c, selezione_r, destra);
                        }
                        if(tipo == 2){
                            croc_g[i].setPosizioniOccupate(selezione_c, selezione_r, destra);
                        }
                        if(tipo == 3){
                            sott_g[i].setPosizioniOccupate(selezione_c, selezione_r, destra);
                        }
                        break;
    
                    case 3:
                        for(int l = 0; l<dimensione_n; l++){
                            tabella_g[selezione_c][selezione_r-l].addNave();
                        }

                        if(tipo == 1){ //aggiungo alla lista delle navi la nave che ho appena inserito
                            corazz_g[i].setPosizioniOccupate(selezione_c, selezione_r, alto);
                        }
                        if(tipo == 2){
                            croc_g[i].setPosizioniOccupate(selezione_c, selezione_r, alto);
                        }
                        if(tipo == 3){
                            sott_g[i].setPosizioniOccupate(selezione_c, selezione_r, alto);
                        }
                        break;
    
                    case 4:
                        for(int m = 0; m<dimensione_n; m++){
                            tabella_g[selezione_c][selezione_r+m].addNave();
                        }

                        if(tipo == 1){ //aggiungo alla lista delle navi la nave che ho appena inserito
                            corazz_g[i].setPosizioniOccupate(selezione_c, selezione_r, basso);
                        }
                        if(tipo == 2){
                            croc_g[i].setPosizioniOccupate(selezione_c, selezione_r, basso);
                        }
                        if(tipo == 3){
                            sott_g[i].setPosizioniOccupate(selezione_c, selezione_r, basso);
                        }
                        break;
                
                    default:
                        break;
                }

            }
            else{ //se la nave è grande solo una casella e la casella selezionata dall'utente è valida aggiungo direttamente la nave
                tabella_g[selezione_c][selezione_r].addNave();
                ass_g[i].setPosizioniOccupate(selezione_c, selezione_r, sinistra); //aggiungo alla lista delle navi la nave che ho appena inserito
            }

            p_alto = true;
            p_basso = true;
            p_destra = true;
            p_sinistra = true;
            orient_valido = true;
        }
    }

    private Boolean controllaOrientamento(int selezione_c, int selezione_r, int dimensione_n, int orientamento, Casella[][] tabella){

        switch (orientamento) {
            case 1: //orientamento: sinistra
                if(selezione_c+1 < dimensione_n){ //se la nave è troppo vicina al bordo sinistro o tocca una nave alla sua sinistra o viene a trovarsi adiacente ad un'altra nave non può essere messa verso sinistra
                    return false;
                }
                else{
                    for(int n = 1; n<dimensione_n; n++){
                        if(selezione_c-n >= 0){ //per evitare errori IndexOutOfRange
                            if(tabella[selezione_c-n][selezione_r].getNave() == true){
                                return false;
                            }
                            if(controllaAdiacenti(selezione_c-n, selezione_r, tabella) == false){
                                return false;
                            }
                        }   
                    }
                }   
                break;

            case 2: // orientamento: destra
                if((dim - selezione_c) < dimensione_n){ //se la nave è troppo vicina al bordo destro o tocca una nave alla sua destra o viene a trovarsi adiacente ad un'altra nave non può essere messa verso destra
                    return false;
                }
                else{
                    for(int o = 1; o<dimensione_n; o++){
                        if(selezione_c+o <= 9){
                            if(tabella[selezione_c+o][selezione_r].getNave() == true){
                                return false;
                            }
                            if(controllaAdiacenti(selezione_c+o, selezione_r, tabella) == false){
                                return false;
                            }
                        }
                    }
                }
                break;

            case 3: //orientamento: alto
                if(selezione_r+1  < dimensione_n){ //se la nave è troppo vicina al bordo superiore o tocca una nave posta sopra di essa o viene a trovarsi adiacente ad un'altra nave non può essere orientata verso l'alto
                    return false;
                }
                else{
                    for(int p = 1; p<dimensione_n; p++){
                        if(selezione_r-p >= 0){
                            if(tabella[selezione_c][selezione_r-p].getNave() == true){
                                return false;
                            }
                            if(controllaAdiacenti(selezione_c, selezione_r-p, tabella) == false){
                                return false;
                            }
                        }
                    }
                
                }
                break;

            case 4: //orientamento: basso
                if((dim - selezione_r) < dimensione_n){ //se la nave è troppo vicina al bordo inferiore o tocca una nave sotto di essa o viene a trovarsi adiacente ad un'altra nave non può essere messa verso il basso
                    return false;
                }
                else{
                    for(int q = 1; q<dimensione_n; q++){
                        if(selezione_r+q <= 9){
                            if(tabella[selezione_c][selezione_r+q].getNave() == true){
                                return false;
                            }
                            if(controllaAdiacenti(selezione_c, selezione_r+q, tabella) == false){
                                return false;
                            }
                        }       
                    }
                }
                break;
        }

        return true;
    }

    private Boolean controllaAdiacenti(int xCasella, int yCasella, Casella[][] tabella){ //controllo se adiacente alla caselle della nave ci sarebbe un'altra nave
        for(int i = yCasella-1; i <= yCasella + 1; i++ ){ //controllo tutte le caselle esistenti adiacenti alla casella selezionata e se trovo che una di queste caselle contiene una nave restituisco false
            for(int j = xCasella-1; j <=xCasella + 1; j++){
                if((i >= 0 && j >= 0) && (i <= 9 && j<= 9)){
                    if((j != xCasella || i != yCasella) && tabella[j][i].getNave() == true){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public void turnoIa() throws InterruptedException{
        ia.turnoIa();
    }

    public class Ia {

        int selezione_c_colpito = -1;
        int selezione_r_colpito = -1;

        int selezione_c= 0;
        int selezione_r = 0;

        //variabili per salvare la posizione della prima casella che ho colpito di una data nave
        int c_base_nave = -1; 
        int r_base_nave = -1;

        Boolean sinistra_provato = false;
        Boolean destra_provato = false;
        Boolean alto_provato = false;
        Boolean basso_provato = false;
    
        Boolean sinistra_giusto = false;
        Boolean destra_giusto = false;
        Boolean alto_giusto = false;
        Boolean basso_giusto = false;
    
        Boolean sinistra_provando = false;
        Boolean destra_provando = false;
        Boolean alto_provando = false;
        Boolean basso_provando = false;

        public void turnoIa() throws InterruptedException{
            
            Boolean continua = false;

            int n_affondate_prec = 0;
            int n_affondate_durante = 0;
    
            do{
                continua = false;
                n_affondate_durante = 0;
                n_affondate_prec = 0;

                sinistra_provando = false;
                destra_provando = false;
                alto_provando = false;
                basso_provando = false;

                //prima di far iniziare il turno dell'IA conto quante navi del giocatore sono già state affondate in precedenza in modo da rilevare se l'IA affonderà un'altra nave o no
                for(int i = 0; i < num_corazz; i++){
                    if(corazz_g[i].controllaInteg(tabella_g) == true){ 
                        n_affondate_prec++;
                        corazz_g[i].setZonaFranca(tabella_g);
                    }
                }

                for(int i = 0; i < num_ass; i++){
                    if(ass_g[i].controllaInteg(tabella_g) == true){
                        n_affondate_prec++;
                        ass_g[i].setZonaFranca(tabella_g);
                    }
                }

                for(int i = 0; i < num_croc; i++){
                    if(croc_g[i].controllaInteg(tabella_g) == true){
                        n_affondate_prec++;
                        croc_g[i].setZonaFranca(tabella_g);
                    }
                }

                for(int i = 0; i < num_sott; i++){
                    if(sott_g[i].controllaInteg(tabella_g) == true){
                        n_affondate_prec++;
                        sott_g[i].setZonaFranca(tabella_g);
                    }
                }
    
                GameUtils.clearScreen();
    
                System.out.print("\033[53C"); //stampo l'indicazione "turno del nemico" centrata rispetto alla tabella
                System.out.println("Turno del nemico");
                for(int i = 0; i < 3; i++){
                    System.out.print("\n");
                }
                stampaTabG(true); //stampo la tabella del giocatore
                GameUtils.systemPause();

                if(sinistra_giusto == true || destra_giusto == true || alto_giusto == true || basso_giusto == true){ //se so già in che direzione devo colpire perché in precedenza ho già colpito in una data direzione rispetto a un'altra casella continuo a colpire in quella direzione

                    controllo_orient: { //questa label serve per fare in modo che l'IA non spari due caselle più in là invece che una nel caso in cui un orientamento fosse sbagliato
                        if(sinistra_giusto == true){
                            if(selezione_c - 1 >= 0){
                                selezione_c--;
                                sinistra_provando = true;
                            }
                            else{
                                sinistra_giusto = false;
                                destra_giusto = true;
                                sinistra_provando = false;
                                destra_provando = true;
                                selezione_c = c_base_nave + 1;
                                break controllo_orient;
                            }
                        }
                        if(destra_giusto == true){
                            if(selezione_c + 1 <= 9){
                                selezione_c++;
                                destra_provando = true;
                            }
                            else{
                                destra_giusto = false;
                                sinistra_giusto = true;
                                destra_provando = false;
                                sinistra_provando = true;
                                selezione_c = c_base_nave - 1;
                                break controllo_orient;
                            }
                        }
                        if(alto_giusto == true){
                            if(selezione_r - 1 >= 0){
                                selezione_r--;
                                alto_provando = true;
                            }
                            else{
                                alto_giusto = false;
                                basso_giusto = true;
                                alto_provando = false;
                                basso_provando = true;
                                selezione_r = r_base_nave + 1;
                                break controllo_orient;
                            }
                            
                        }
                        if(basso_giusto == true){
                            if(selezione_r + 1 <= 9){
                                selezione_r++;
                                basso_provando = true;
                            }
                            else{
                                basso_giusto = false;
                                alto_giusto = true;
                                basso_provando = false;
                                alto_provando = true;
                                selezione_r = r_base_nave - 1;
                                break controllo_orient;
                            }
                        }
                    }
                }
                else{ //altrimenti procedo a generare delle coordinate random o in modo da colpire le caselle adiacenti a una casella già colpita
                    do{
                        if(selezione_c_colpito == -1 && selezione_r_colpito == -1){ //se non ho colpito nessuna nave precedentemente che non è ancora affondata genero 2 coordinate casuali e sparo
                            selezione_c = rand.nextInt(10);
                            selezione_r = rand.nextInt(10);  
                        }
                        else{ //altrimenti provo a sparare nella casella adiacenti alla casella che ho colpito nelle 4 direzioni
                            selezione_casella: {
                                do{ //in un do while infinito
                                    int scelta = rand.nextInt(4); //genero un numero casuale e, in base a quello, decido in che direzione provare a colpire per aggiungere imprevedibilità all'IA

                                    sel:{
                                        switch (scelta) {
                                            case 0:
                                                if(selezione_c_colpito > 0 && sinistra_provato == false && selezione_c_colpito-1 >= 0 && tabella_g[selezione_c_colpito - 1][selezione_r_colpito].getControllata() == false){ //se la casella colpita in precedenza non è attaccata al bordo sinistro della tabella
                                                    selezione_c = selezione_c_colpito - 1; //sparo nella casella direttamente a sinistra della casella che ho colpito in precedenza
                                                    selezione_r = selezione_r_colpito;
                                                    sinistra_provato = true; //segno che l'orientamento sinistro è stato provato 
                                                    sinistra_provando = true; //segno che sto provando a colpire a sinistra
                                                    break selezione_casella; //salto alla fine del codice labelled seleziona_casella
                                                }
                                                else{
                                                    sinistra_provato = true; //se non riesco a provare l'orientamento sinistro è perché la casella che ho colpito in precedenza si trova contro il bordo sinistro
                                                }
                                                break sel;
    
                                            case 1:
                                                if(selezione_c_colpito < 10 && destra_provato == false && selezione_c_colpito+1 <= 9 && tabella_g[selezione_c_colpito + 1][selezione_r_colpito].getControllata() == false){ //se la casella colpita in precedenza non è attaccata al bordo destro della tabella
                                                    selezione_c = selezione_c_colpito + 1; //sparo nella casella direttamente a destra della casella che ho colpito in precedenza
                                                    selezione_r = selezione_r_colpito; 
                                                    destra_provato = true; //segno che l'orientamento destro è stato provato
                                                    destra_provando = true; 
                                                    break selezione_casella; //salto alla fine del codice labelled seleziona_casella
                                                }
                                                else{
                                                    destra_provato = true; //se non riesco a provare l'orientamento destro è perché la casella che ho colpito in precedenza si trova contro il bordo destro
                                                }
                                                break sel;
    
                                            case 2:
                                                if(selezione_r_colpito > 0 && alto_provato == false && selezione_r-1 >= 0 && tabella_g[selezione_c_colpito][selezione_r_colpito - 1].getControllata() == false){ //se la casella colpita in precedenza non è attaccata al bordo alto della tabella
                                                    selezione_r = selezione_r_colpito - 1; //sparo nella casella direttamente sopra alla casella che ho colpito in precedenza
                                                    selezione_c = selezione_c_colpito;
                                                    alto_provato = true; //segno che l'orientamento alto è stato provato
                                                    alto_provando = true;
                                                    break selezione_casella; //salto alla fine del codice labelled seleziona_casella
                                                }
                                                else{
                                                    alto_provato = true; //se non riesco a provare l'orientamento alto è perché la casella che ho colpito in precedenza si trova contro il bordo alto
                                                }
                                                break sel;
    
                                            case 3:
                                                if(selezione_r_colpito < 10 && basso_provato == false && selezione_r_colpito+1 <= 9 && tabella_g[selezione_c_colpito][selezione_r_colpito + 1].getControllata() == false){ //se la casella colpita in precedenza non è attaccata al bordo basso della tabella
                                                    selezione_r = selezione_r_colpito + 1; //sparo nella casella direttamente sotto alla casella che ho colpito in precedenza
                                                    selezione_c = selezione_c_colpito;
                                                    basso_provato = true; //segno che l'orientamento basso è stato provato
                                                    basso_provando = true;
                                                    break selezione_casella; //salto alla fine del codice labelled seleziona_casella
                                                }
                                                else{
                                                    basso_provato = true; //se non riesco a provare l'orientamento alto è perché la casella che ho colpito in precedenza si trova contro il bordo basso
                                                
                                                }
                                                break sel;
                                            
                                            default:
                                                break sel;
                                        }
                                    }
                                    
                                } while(1 == 1);
                                
                            } //I BREAK FANNO RIPARTIRE IL CODICE DA QUI
                        } 
                    } while(tabella_g[selezione_c][selezione_r].getColpita() == true || tabella_g[selezione_c][selezione_r].getControllata() == true);
                }
    
                
    
                tabella_g[selezione_c][selezione_r].setColpita(); //segno che ho colpito la casella scelta dall'ia

                tabella_g[selezione_c][selezione_r].setAppenaColpita(true);
                GameUtils.clearScreen();
                System.out.print("\033[53C"); //stampo l'indicazione "turno del nemico" centrata rispetto alla tabella
                System.out.println("Turno del nemico");
                for(int i = 0; i < 3; i++){
                    System.out.print("\n");
                }
                stampaTabG(true); //stampo la tabella del giocatore
                tabella_g[selezione_c][selezione_r].setAppenaColpita(false);

                //riconto le navi del giocatore affondate per determinare se l'ia ha affondato una nave o meno
                for(int i = 0; i < num_corazz; i++){
                    if(corazz_g[i].controllaInteg(tabella_g) == true){
                        n_affondate_durante++;
                    }
                }

                for(int i = 0; i < num_ass; i++){
                    if(ass_g[i].controllaInteg(tabella_g) == true){
                        n_affondate_durante++;
                    }
                }

                for(int i = 0; i < num_croc; i++){
                    if(croc_g[i].controllaInteg(tabella_g) == true){
                        n_affondate_durante++;
                    }
                }

                for(int i = 0; i < num_sott; i++){
                    if(sott_g[i].controllaInteg(tabella_g) == true){
                        n_affondate_durante++;
                    }
                }

                if(n_affondate_durante == n_affondate_prec){ //se l'IA non ha affondato nessuna nave
                    if(tabella_g[selezione_c][selezione_r].getNave() == true){ //ma ha colpito una nave
                        continua = true; //dò il diritto all'IA di continuare a colpire
                        if(selezione_c_colpito == -1 && selezione_r_colpito == -1){ //se è la prima casella di una nave che colpisco ne segno la posizione
                            c_base_nave = selezione_c;
                            r_base_nave = selezione_r;
                        }
                        //segno le coordinate in cui l'IA ha colpito
                        selezione_c_colpito = selezione_c; 
                        selezione_r_colpito = selezione_r;
    
                        if(sinistra_provando == true || destra_provando == true || alto_provando == true || basso_provando == true){ //se stavo provando a colpire in un determinato orientamento segno che quell'orientamento era giusto
                            if(sinistra_provando == true) sinistra_giusto = true;
                            if(destra_provando == true) destra_giusto = true;
                            if(alto_provando == true) alto_giusto = true;
                            if(basso_provando == true) basso_giusto = true;
                        }

                        System.out.println("Nave colpita!");
                    }
                    else{ //se invece l'IA non ha colpito e sta provando a colpire in una direzione specifica segno che l'IA deve tirare nella direzione opposta
                        if(sinistra_giusto == true){
                            sinistra_giusto = false;
                            destra_giusto = true;
                            selezione_c = c_base_nave;
                            selezione_r = r_base_nave;
                        }

                        if(destra_giusto == true){
                            destra_giusto = false;
                            sinistra_giusto = true;
                            selezione_c = c_base_nave;
                            selezione_r = r_base_nave;
                        }

                        if(alto_giusto == true){
                            alto_giusto = false;
                            basso_giusto = true;
                            selezione_c = c_base_nave;
                            selezione_r = r_base_nave;
                        }

                        if(basso_giusto == true){
                            basso_giusto = false;
                            alto_giusto = true;
                            selezione_c = c_base_nave;
                            selezione_r = r_base_nave;
                        }
                        System.out.println("Nessuna nave colpita!");
                    }
                }
                else{ //se invece l'IA ha affondato una nave riporto tutti i parametri dell'IA al loro livello base e dò all'IA il diritto di continuare a colpire
                    selezione_c_colpito = -1;
                    selezione_r_colpito = -1;

                    c_base_nave = -1;
                    r_base_nave = -1;

                    sinistra_provato = false;
                    destra_provato = false;
                    alto_provato = false;
                    basso_provato = false;

                    sinistra_giusto = false;
                    destra_giusto = false;
                    alto_giusto = false;
                    basso_giusto = false;

                    sinistra_provando = false;
                    destra_provando = false;
                    alto_provando = false;
                    basso_provando = false;

                    continua = true;
                    System.out.println("Nave affondata!");
                }
                GameUtils.systemPause();
            } while(continua == true && controllaVittoriaIa() == false);
        }
    }
    
    

    public void turnoG() throws InterruptedException{
        int selezione_c;
        int selezione_r;
        Boolean continua = false;

        do{ //se il giocatore colpisce una nave ha il diritto di sparare di nuovo
            continua = false;
            
            GameUtils.clearScreen();

            System.out.print("\033[53C"); //stampo l'indicazione "tabella nemico" centrata rispetto alla tabella
            System.out.println("Tabella nemico");
            for(int i = 0; i < 3; i++){
                System.out.print("\n");
            }
            stampaTabIa(); //stampo la tabella dell'ia
            do{
                do{ //prendo in input la colonna della casella in cui l'utente vuole sparare
                    System.out.print("Selezionare la colonna della casella che si desidera colpire: ");
                    selezione_c = tastiera.nextInt();
                    if(selezione_c < 1 || selezione_c > 10){
                        System.out.println("Selezione non valida");
                    }
                } while(selezione_c < 1 || selezione_c > 10);
    
                do{ //prendo in input la riga della casella in cui l'utente vuole sparare
                    System.out.print("Selezionare la riga della casella che si desidera colpire: ");
                    selezione_r = tastiera.nextInt();
                    if(selezione_r < 1 || selezione_r > 10){
                        System.out.println("Selezione non valida");
                    }
                } while(selezione_r < 1 || selezione_r > 10);
    
                selezione_c--; //decremento gli inserimenti di 1 per poterli utilizzare come indici di tabella
                selezione_r--;
    
                if(tabella_ia[selezione_c][selezione_r].getColpita() == true){ //se la casella selezionata dall'utente e' gia' stata colpita in precedenza faccio ripetere la selezione
                    System.out.println("Casella gia' colpita!");
                }
    
            } while(tabella_ia[selezione_c][selezione_r].getColpita() == true);

            GameUtils.systemPause();
            GameUtils.clearScreen();

            tabella_ia[selezione_c][selezione_r].setColpita();
            if(tabella_ia[selezione_c][selezione_r].getNave() == true){
                continua = true;
            }
            stampaTabIa();

            if(continua == true){ //se ho colpito una nave controllo l'integrità di tutte le navi: se vedo che una è stata affondata e che prima non era già stata affondata, stampo nave affondata e segno che la nave che è stata appena affondata è affondata, altrimenti stampo nave colpita
                Boolean affondata = false;
                
                for(int i = 0; i < num_corazz; i++){
                    if(corazz_ia[i].controllaInteg(tabella_ia) == true && corazz_ia[i].getAffondata() == false){
                        corazz_ia[i].setAffondata();
                        affondata = true;
                    }
                }
        
                for(int j = 0; j < num_croc; j++){
                    if(croc_ia[j].controllaInteg(tabella_ia) == true && croc_ia[j].getAffondata() == false){
                        croc_ia[j].setAffondata();
                        affondata = true;
                    }
                }
        
                for(int k = 0; k < num_sott; k++){
                    if(sott_ia[k].controllaInteg(tabella_ia) == true && sott_ia[k].getAffondata() == false){
                        sott_ia[k].setAffondata();
                        affondata = true;
                    }
                }
        
                for(int l = 0; l < num_ass; l++){
                    if(ass_ia[l].controllaInteg(tabella_ia) == true && ass_ia[l].getAffondata() == false){
                        ass_ia[l].setAffondata();
                        affondata = true;
                    }
                }

                if(affondata == true){
                    System.out.println("Nave affondata!");
                }
                else{
                    System.out.println("Nave colpita!");
                }
            }
            else{
                System.out.println("Nessuna nave colpita!");
            }

            GameUtils.systemPause();
        } while(continua == true && controllaVittoriaG() == false);
    }

    private void stampaTab(Casella[][] tabella, Boolean visibilita) throws InterruptedException{ //nota: per i colori usare le costanti ANSI definite sopra
        //nota: per vedere in dettaglio come funziona il processo di stampa della tabella de-commentare le linee in cui sono presenti i Thread.sleep() (ve ne sono alcuni in questa funzione e alcuni in StampaCasella)
       
        /* - Posizione del cursore:
            033[<L>;<C>H
            mette il cursore alla linea L e colonna C.
        - Muove il cursore su N linee:
            \033[<N>A
        - Muove il cursore giù N linee:
            \033[<N>B
        - Muove il cursore avanti N colonne:
            \033[<N>C
        - Muove il cursore indietro N colonne:
            \033[<N>D

        - Salva la posizione del cursore:
            \033[s
        - Ripristina la posizione del cursore:
            \033[u*/

        String vuota = "vuota";
        String colpita = "centro";
        String mancata = "mancata";
        String conNave = "conNave";
        String orizzontale = "orizzontale";
        String verticale = "verticale";
        String appenaColpita = "a_colpita";

        System.out.print("      ");

        for(int i = 0; i < 10; i++){
            if(i != 0 && i != 9){
                System.out.print("           ");
            }
            if(i == 9){
                System.out.print("          ");
            }
            System.out.print((i+1));
        }
        for(int j = 0; j < 3; j++){
            System.out.print("\n");
        }

        if(visibilita == false){ //se non voglio vedere tutte le navi posizionate
            for(int k = 0; k < dim; k++){
                for(int m = 0; m < dim; m++){
                    //faccio vari aggiustamenti alla posizione del cursore per stampare correttamente la tabella
                    
                    if(m == 0){
                        for(int q = 0; q < 6; q++){
                            System.out.print("\n");
                        } 
                        //faccio spazio per la prossima riga abbassandomi di 6 posizioni
                        
                        System.out.print("\033[6A"); //torno su di 6 posizioni
                        System.out.print("\033[1C");
                    }
                    //Thread.sleep(200);

                    if(tabella[m][k].getAppenaColpita() == true){
                        stampaCasella(appenaColpita);
                    }
                    else{
                        if(tabella[m][k].getNave() == true && tabella[m][k].getColpita() == true){ //se la casella in questione è stata colpita ed ha una nave dentro la stampo in rosso (nave colpita)
                            stampaCasella(colpita);
                        }
                        else{
                            if(tabella[m][k].getColpita() == true){ //se la casella è stata colpita, ma non contiene una nave la rappresento in giallo
                                stampaCasella(mancata);
                            }
                            else{ //se la nave non è ancora stata colpita la rappresento in blu (mare)
                                stampaCasella(vuota);
                            }
                        }
                    }

                    //Thread.sleep(200);
                    System.out.print("\033[4A"); //riporto il cursore in su di quattro posizioni, in quanto il metodo stampaCasella lascia il cursore 4 posizioni sotto da dove è partito
                    System.out.print("\033[11C"); //sposto il cursore a destra di 11 posizioni per non stampare sulla casella appena prodotta
                    //Thread.sleep(200);
                }

                stampaBordo(orizzontale); //stampo il bordo orizzontale della riga di caselle appena prodotta(prima stampo l'interno delle caselle e poi vi costruisco attorno il bordo)
                stampaBordo(verticale);
                //Thread.sleep(200);
                if(k != dim-1){ //vado a capo 45 volte per lasciare spazio tra le righe della tabella
                    for(int n = 0; n < 5; n++){
                        System.out.println("");
                        //Thread.sleep(200);
                    }
                }
                else{ //quando ho prodotto anche l'ultima riga della mia tabella stampo l'ultimo bordo e lascio uno spazio per stampare le scritte che vanno poste sotto alla tabella
                    System.out.print("\033[4B");
                    System.out.print("\033[120D");
                    for(int p = 0; p < 120; p++){
                        System.out.print(ConsoleColors.BLACK + ConsoleColors.CYAN_BACKGROUND  + "-" + ConsoleColors.RESET);
                    }
                    System.out.print("\033[4A");
                    for(int o = 0; o<6; o++){
                        System.out.println("");
                    }
                }
            }

        }
        else{ //se voglio vedere tutte le navi posizionate
            
            for(int k = 0; k < dim; k++){
                for(int m = 0; m < dim; m++){
                    //faccio vari aggiustamenti alla posizione del cursore per stampare correttamente la tabella
                    
                    if(m == 0){
                        for(int q = 0; q < 6; q++){
                            System.out.print("\n");
                        } //faccio spazio per la prossima riga abbassandomi di 6 posizioni
                        
                        
                        System.out.print("\033[6A"); //torno su di 6 posizioni
                        System.out.print("\033[1C");
                    }

                    //Thread.sleep(200);

                    if(tabella[m][k].getAppenaColpita() == true){
                        stampaCasella(appenaColpita);
                    }
                    else{
                        if(tabella[m][k].getNave() == true && tabella[m][k].getColpita() == true){ //in questa modalità di stampa della tabella differenzio solo tra caselle con navi dentro (stampate in blu) e caselle senza navi dentro (stampate in bianco)
                            stampaCasella(colpita);
                        }
                        else{
                            if(tabella[m][k].getNave() == true){
                                stampaCasella(conNave);
                            }
                            else{
                                if(tabella[m][k].getColpita() == true){
                                    stampaCasella(mancata);
                                }
                                else{
                                    stampaCasella(vuota);
                                }
                            }
                        }
                    }
                    
                    System.out.print("\033[4A"); //riporto il cursore in su di quattro posizioni, in quanto il metodo stampaCasella lascia il cursore 4 posizioni sotto da dove è partito
                    System.out.print("\033[11C"); //sposto il cursore a destra di 11 posizioni per non stampare sulla casella appena prodotta
                    //Thread.sleep(200);
                }


                stampaBordo(orizzontale); //stampo il bordo orizzontale della riga di caselle appena prodotta(prima stampo l'interno delle caselle e poi vi costruisco attorno il bordo)
                stampaBordo(verticale);
                //Thread.sleep(200);
                if(k != dim-1){ //vado a capo 45 volte per lasciare spazio tra le righe della tabella
                    for(int n = 0; n < 5; n++){
                        System.out.println("");
                        //Thread.sleep(200);
                    }
                }
                else{ //quando ho prodotto anche l'ultima riga della mia tabella stampo l'ultimo bordo e lascio uno spazio per stampare le scritte che vanno poste sotto alla tabella
                    System.out.print("\033[4B");
                    System.out.print("\033[120D");
                    for(int p = 0; p < 120; p++){
                        System.out.print(ConsoleColors.BLACK + ConsoleColors.CYAN_BACKGROUND  + "-" + ConsoleColors.RESET);
                    }
                    System.out.print("\033[4A");
                    for(int o = 0; o<6; o++){
                        System.out.println("");
                    }
                }
            }
        }
        System.out.print("\033[122C");
        for(int i = 0; i < 10; i++){
            System.out.print("\033[5A");
            System.out.print(10-i);
            System.out.print("\033[1D");
        }
        System.out.print("\033[50B");
        System.out.print("\033[123D");

    }

    private void stampaCasella(String stato) throws InterruptedException{

        String colore = ConsoleColors.WHITE;
        
        if("centro".equals(stato)){ //in base allo stato delle caselle (con nave e colpita, colpita ma senza nave o non ancora colpita) cambio il colore con cui stampo la casella
            colore = ConsoleColors.RED_BACKGROUND;
        }
        if("mancata".equals(stato)){
            colore = ConsoleColors.YELLOW_BACKGROUND;
        }
        if("vuota".equals(stato)){
            colore = ConsoleColors.BLUE_BACKGROUND;
        }
        if("conNave".equals(stato)){
            colore = ConsoleColors.GREEN_BACKGROUND;
        }
        if("a_colpita".equals(stato)){
            colore = ConsoleColors.PURPLE_BACKGROUND;
        }
        
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 10; j++){
                System.out.print(colore + " "); //stampo, per ogni casella, 4 righe di spazi con sfondo colorato (effetto casella piena) formate da 10 caratteri ciascuna
            }
            
            //sposto il cursore in basso di una posizione e lo porto all'inizio della riga dopo aver stampato ogni riga di caratteri per stampare la riga dopo
            System.out.print("\033[1B"); 
            System.out.print("\033[10D");
        }
        
        System.out.print(ConsoleColors.RESET); //riporto il colore di stampa al colore base e lascio un carattere di spazio lateralmente (sposto il cursore a destra di una posizione) per lasciare spazio alla prossima casella
        System.out.print("\033[1C");
    }

    private void stampaBordo(String orient){
        
        if("orizzontale".equals(orient)){ //se devo stampare il bordo orizzontale
            System.out.print("\033[1A"); //sposto il cursore su di uno (lo posiziono sopra alla riga di caselle che devo incorniciare)
            System.out.print("\033[121D"); //sposto il cursore all'inizio della riga 
            for(int i = 0; i < 120; i++){ //stampo una riga da 120 trattini
                System.out.print(ConsoleColors.BLACK + ConsoleColors.CYAN_BACKGROUND + "-" + ConsoleColors.RESET);
            }
            System.out.print("\033[1B"); //riporto il cursore alla posizione in cui l'ho trovato
        }
        else{ //se devo stampare il bordo verticale
            System.out.print("\033[121D"); //porto il cursore all'inizio della riga corrente

            System.out.print(ConsoleColors.BLACK + ConsoleColors.CYAN_BACKGROUND + "|" + ConsoleColors.RESET); //stampo il primo trattino che compone il bordo, dandogli un colore di sfonod e un colore al carattere
            System.out.print("\033[1D"); //porto il cursore in giù di 1

            for(int j = 0; j < 3; j++){ //completo la riga di trattini appena iniziata 
                System.out.print(ConsoleColors.BLACK + ConsoleColors.CYAN_BACKGROUND +  "\033[1B" + "|" + ConsoleColors.RESET); //stampo 3 trattini uno sotto l'altro
                System.out.print("\033[1D"); //prima di stampare il prossimo trattino riporto il cursore nella posizione corretta arretrandolo di 1 posizione
            }
            
            System.out.print("\033[4A"); //porto su il cursore di 4 (altezza casella)
            System.out.print("\033[11C"); //porto il cursore a destra di 11 (larghezza casella), ovvero mi porto nello spazio vuoto tra 2 caselle subito sotto il bordo orizzontale

            for(int l = 0; l < 9; l++){ //stampo tutte le altre righe di trattini
                for(int k = 0; k < 2; k++){ //stampo 2 righe du trattini per ogni spazio vuoto tra le caselle
                    for(int j = 0; j < 4; j++){ //stampo una colonna di 4 trattini di altezza 
                        System.out.print(ConsoleColors.BLACK + ConsoleColors.CYAN_BACKGROUND +  "\033[1B" + "|" + ConsoleColors.RESET); //porto il cursore giù di 1 posizione e stampo un trattino nero su sfondo azzurro
                        System.out.print("\033[1D"); //riporto il cursore nella posizione corretta spostandolo indietro di 1 posizione
                    }
                    System.out.print("\033[4A"); //riporto il cursore alla cima della colonna appena stampata
                    System.out.print("\033[1C"); //mi sposto a destra di 1 per stampare la colonna di trattini successiva, adicacente a quella appena stampata
                }
                System.out.print("\033[10C"); //mi sposto nello spazio vuoto tra 2 caselle successivo
            }

            System.out.print("\033[1B"); //abbasso il cursore di una posizione, in quanto nell'ultima riga vengo a trovarmi con il cursore troppo su di 1 posizione

            System.out.print(ConsoleColors.BLACK + ConsoleColors.CYAN_BACKGROUND + "|" + ConsoleColors.RESET); //stampo il primo trattino dell'ultima colonna
            System.out.print("\033[1D"); //risposto il cursore nella posizione dove ha appena stampato il trattino

            for(int j = 0; j < 3; j++){ //stampo gli altri 3 trattini
                System.out.print(ConsoleColors.BLACK + ConsoleColors.CYAN_BACKGROUND + "\033[1B" + "|" + ConsoleColors.RESET); //stampo il trattino e porto giù il cursore di 1
                System.out.print("\033[1D"); //porto il cursore indietro di una posizione per portarlo in asse con il trattino appena stampato
            }

            System.out.print("\033[3A"); //porto il cursore nella posizione corretta per permettere agli altri metodi di subentrare, spostandolo in su di 3 posizioni
        }
    }



    public void stampaTabG() throws InterruptedException{
        stampaTab(tabella_g, false);
    }

    public void stampaTabIa() throws InterruptedException{
        stampaTab(tabella_ia, false);
    }

    public void stampaTabG(Boolean visibilita) throws InterruptedException{
        stampaTab(tabella_g, visibilita);
    }

    public void stampaTabIa(Boolean visibilita) throws InterruptedException{
        stampaTab(tabella_ia, visibilita);
    }

    public Boolean controllaVittoriaIa(){
        Boolean vittoria = true;
        for(int i = 0; i < num_ass; i++){
            if(ass_g[i].controllaInteg(tabella_g) == false) vittoria = false;
        }

        for(int i = 0; i < num_corazz; i++){
            if(corazz_g[i].controllaInteg(tabella_g) == false) vittoria = false;
        }

        for(int i = 0; i < num_croc; i++){
            if(croc_g[i].controllaInteg(tabella_g) == false) vittoria = false;
        }

        for(int i = 0; i < num_sott; i++){
            if(sott_g[i].controllaInteg(tabella_g) == false) vittoria = false;
        }

        return vittoria;
    }

    public Boolean controllaVittoriaG(){
        Boolean vittoria = true;
        for(int i = 0; i < num_ass; i++){
            if(ass_ia[i].controllaInteg(tabella_ia) == false) vittoria = false;
        }

        for(int i = 0; i < num_corazz; i++){
            if(corazz_ia[i].controllaInteg(tabella_ia) == false) vittoria = false;
        }

        for(int i = 0; i < num_croc; i++){
            if(croc_ia[i].controllaInteg(tabella_ia) == false) vittoria = false;
        }

        for(int i = 0; i < num_sott; i++){
            if(sott_ia[i].controllaInteg(tabella_ia) == false) vittoria = false;
        }

        return vittoria;
    }
}
