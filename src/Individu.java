import java.util.ArrayList;
import java.util.Random;

public class Individu {
    private String nom;
    private int hauteur;
    private int position;
    private int somme;


    public Individu(String nom,int hauteur,int position, int somme){
        this.nom = nom;
        this.hauteur=hauteur;
        this.position = position;
        this.somme = somme;
    }

    public String getNom() {
        return nom;
    }

    public int getHauteur() {
        return hauteur;
    }

    public int getPosition() {
        return position;
    }

    public int getSomme() {
        return somme;
    }


    public void modifiePosition(int nb){

        this.position= this.position+nb;
    }


}
class Bandit extends Individu{
    private int NbBalle;
    private ArrayList<Action> actions;

    private ArrayList<Butin> butins;

    public Bandit (String nom, int hauteur,int position,int somme, int balle, ArrayList<Action> actions, ArrayList<Butin> butins){
        super(nom,hauteur, position,somme);
        this.NbBalle = balle;
        this.actions=actions;
        this.butins = butins;
    }

    public int getNbBalle() {

        return NbBalle;
    }

    public ArrayList<Action> getActions() {
        return actions;
    }

    public ArrayList<Butin> getButins() {
        return butins;
    }

    public void decrement_NbBalles(){
        this.NbBalle=this.NbBalle-1;
    }

    public void ajouteActions(Action A){
        if (this.actions.size() < 3){
            this.actions.add(A);
        }else{
            System.out.println("Limite d'actions préparées atteinte.");
        }
    }

    public void supprimeActions(int i){
        if(i<this.actions.size()){
            this.actions.remove(i);
        }
    }

    public boolean a_le_magot(){
        for(int i=0;i<this.butins.size();i++){
            if(butins.get(i) instanceof Magot){
                return true;
            }
        }
        return false;
    }
}

class Sherif extends Individu{
    public Sherif(String nom, int hauteur,int position) {

        super(nom,hauteur, position, 0);
    }
}

class Voyageur extends Individu{
    private int somme;

    public Voyageur(String nom, int hauteur,int position, int somme){
        super(nom, hauteur,position,somme);
    }
}

