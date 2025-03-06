public class Nave {
    
    private int dimensione;
    private Posizione[] posizione;
    private Boolean affondata = false;

    public static class Posizione{
        public int x;
        public int y;
    }

    public Nave(int dim){ //metodo costruttore: in base alla dimensione della nave inizializzo l'array posizione, in cui salvo le caselle occupate dalla nave
        dimensione = dim;
        setDim();
    }
    
    private void setDim(){ //in base alla dimensione della nave inizializzo i caselle dell'array posizione
        switch (dimensione) {
            case 1:
                posizione = new Posizione[1];
                for(int i = 0; i < 1; i++){
                    posizione[i] = new Posizione();
                }
                break;

            case 2:
                posizione = new Posizione[2];
                for(int i = 0; i < 2; i++){
                    posizione[i] = new Posizione();
                }
                break;

            case 3:
                posizione = new Posizione[3];
                for(int i = 0; i < 3; i++){
                    posizione[i] = new Posizione();
                }
                break;
        
            case 4:
                posizione = new Posizione[4];
                for(int i = 0; i < 4; i++){
                    posizione[i] = new Posizione();
                }
                break;

            default:
                break;
        }
    }

    public void setPosizioniOccupate(int part_x, int part_y, int orientamento){ //in base all'orientamento della nave ed alla sua casella di partenza imposto quali caselle la nave occupa
        switch (orientamento) {
            case 1:
                for(int i = 0; i < dimensione; i++){
                    posizione[i].x = part_x - i;
                    posizione[i].y = part_y;
                }
                break;

            case 2:
                for(int i = 0; i < dimensione; i++){
                    posizione[i].x = part_x + i;
                    posizione[i].y = part_y;
                }
                break;

            case 3:
                for(int i = 0; i < dimensione; i++){
                    posizione[i].x = part_x;
                    posizione[i].y = part_y - i;
                }
                break;

            case 4:
                for(int i = 0; i < dimensione; i++){
                    posizione[i].x = part_x;
                    posizione[i].y = part_y + i;
                }
                break;
        
            default:
                break;
        }
    }

    public Boolean controllaInteg(Casella[][] tabella){ //metodo per controllare se la nave è stata affondata o no
        Boolean affondata = true; //di default assumo che la nave è stata affondata 
        
        for(int i = 0; i < dimensione; i++){
            if(tabella[posizione[i].x][posizione[i].y].getColpita() == false){
                affondata = false; //se una casella in cui la nave è presente non è stata colpita porto affondata a false
            }
        }

        return affondata;
    }

    public void setAffondata(){
        affondata = true;
    }

    public Boolean getAffondata(){
        return affondata;
    }

    public void setZonaFranca(Casella[][] tabella){
        for(int i = 0; i < dimensione; i++){
            for(int j = -1; j <= 1; j++){
                for(int k = -1; k <= 1; k++){
                    if((posizione[i].x + k >= 0 && posizione[i].x + k <= 9) && (posizione[i].y + j >= 0 && posizione[i].y + j <= 9)){
                        tabella[posizione[i].x + k][posizione[i].y + j].setControllata();
                    }
                }
            }
        }
    }
}
