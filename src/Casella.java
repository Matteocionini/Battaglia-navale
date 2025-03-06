public class Casella {

    private Boolean nave = false;
    private Boolean colpita = false;
    private Boolean controllata = false;
    private Boolean appenaColpita = false;

    public void addNave(){
        nave = true;
     }

    public void setColpita(){
        colpita = true;
    }

    public Boolean getNave(){
        return nave;
    }

    public Boolean getColpita(){
        return colpita;
    }

    public void setControllata(){
        controllata = true;
    }

    public Boolean getControllata(){
        return controllata;
    }

    public void setAppenaColpita(Boolean stato){
        appenaColpita = stato;
    }

    public Boolean getAppenaColpita(){
        return appenaColpita;
    }
}
