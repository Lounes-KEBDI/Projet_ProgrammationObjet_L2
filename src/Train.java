import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.awt.Graphics;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

public class Train {
    private final int nb_Wagons;
    private ToiGon[][] plateau;

    private ArrayList<Individu> individusBord;

    double NERVOSITE_MARSHALL ;

    private int numAction;

    public Train(int w, double nervosite){
        this.nb_Wagons= w;
        this.plateau = new ToiGon[2][w];
        this.individusBord= new ArrayList<Individu>();
        this.numAction=0;
        this.NERVOSITE_MARSHALL = nervosite;
        for (int i=0; i<2; i++){
            for (int j=0; j<w; j++){
                boolean b;
                if (i==0) {
                    b = false;
                }else {
                    b = true;
                }
                this.plateau[i][j]=new ToiGon(b,j);
            }
        }
        Individu S = new Sherif("Sherif",0,0);
        this.plateau[0][0].ajouteIndividu(S);
        this.individusBord.add(S);
        Butin M = new Magot();
        this.plateau[0][0].ajouteButin(M);
    }

    public int getNb_Wagons() {
        return nb_Wagons;
    }

    public ToiGon[][] getPlateau() {
        return plateau;
    }

    public ArrayList<Individu> getIndividusBord() {
        return individusBord;
    }

    public int getNumAction() {
        return numAction;
    }

    public double getNERVOSITE_MARSHALL() {
        return NERVOSITE_MARSHALL;
    }

    public int getIndiceIndividu(Individu I){
        for (int i=0; i<this.individusBord.size();i++){
            if(this.individusBord.get(i).getNom()==I.getNom()) {
                return i;
            }
        }
        return -1;
    }

    public int[] getPositionIndividu(Individu I){
        int[] res = new int[2];
        for(int i =0; i<2;i++){
            for(int j =0; j<nb_Wagons; j++){
                if(plateau[i][j].estPresent(I)){
                    res[0]=i;
                    res[1]=j;
                    return res;
                }
            }
        }
        return null;
    }
    public boolean estPresent(Individu I){
        if(getPositionIndividu(I)==null){
            return false;
        }else{
            return true;
        }
    }

    public void ajouteIndividu(Individu I){
        if(!estPresent(I)) {
            this.plateau[I.getHauteur()][I.getPosition()].ajouteIndividu(I);
            this.individusBord.add(I);
        }
    }

    public void supprimeIndividu(Individu I){
        if(estPresent(I)) {
            this.plateau[I.getHauteur()][I.getPosition()].supprimeIndividu(I);
            this.individusBord.remove(this.getIndiceIndividu(I));
        }
    }

    public void ajouteButin(Butin B,int h,int p ){

        this.plateau[h][p].ajouteButin(B);
    }

    public void deplaceIndividu(Individu I, Direction d){
        int i= getIndiceIndividu(I);
        switch (d) {
            case AVANT:
                if (I.getPosition() != 0) {
                    this.plateau[I.getHauteur()][I.getPosition()].supprimeIndividu(I);
                    if (I instanceof Bandit) {
                        Individu I2 = new Bandit(I.getNom(), I.getHauteur(), I.getPosition()-1,I.getSomme(), ((Bandit) I).getNbBalle(), ((Bandit) I).getActions(),((Bandit) I).getButins() );
                        this.plateau[I2.getHauteur()][I2.getPosition()].ajouteIndividu(I2);
                        this.individusBord.set(i, I2);
                    }
                    if (I instanceof Sherif) {
                        Individu I2 = new Sherif(I.getNom(),I.getHauteur(), I.getPosition() - 1);
                        this.plateau[I2.getHauteur()][I2.getPosition()].ajouteIndividu(I2);
                        this.individusBord.set(i, I2);
                    }
                    System.out.println(I.getNom() + " avance vers l'avant.");
                } else {
                    System.out.println(I.getNom() + " est déja dans la locomotive.");
                }
                break;
            case ARRIERE:
                if (I.getPosition() != this.nb_Wagons - 1) {
                    this.plateau[I.getHauteur()][I.getPosition()].supprimeIndividu(I);
                    if (I instanceof Bandit) {
                        Individu I2 = new Bandit(I.getNom(), I.getHauteur(), I.getPosition()+1,I.getSomme(), ((Bandit) I).getNbBalle(), ((Bandit) I).getActions(),((Bandit) I).getButins() );
                        this.plateau[I2.getHauteur()][I2.getPosition()].ajouteIndividu(I2);
                        this.individusBord.set(i, I2);
                    }
                    if (I instanceof Sherif) {
                        Individu I2 = new Sherif(I.getNom(), I.getHauteur(),I.getPosition() + 1);
                        this.plateau[I2.getHauteur()][I2.getPosition()].ajouteIndividu(I2);
                        this.individusBord.set(i, I2);
                    }
                    System.out.println(I.getNom() + " avance vers l'arriere.");
                } else {
                    System.out.println(I.getNom() + " est déja dans le dernier wagon.");
                }
                break;
            case HAUT:
                if (I.getHauteur() != 1) {
                    this.plateau[I.getHauteur()][I.getPosition()].supprimeIndividu(I);
                    if (I instanceof Bandit) {
                        Individu I2 = new Bandit(I.getNom(), I.getHauteur()+1, I.getPosition(),I.getSomme(), ((Bandit) I).getNbBalle(), ((Bandit) I).getActions(),((Bandit) I).getButins() );
                        this.plateau[I2.getHauteur()][I2.getPosition()].ajouteIndividu(I2);
                        this.individusBord.set(i, I2);
                    }
                    if (I instanceof Sherif) {
                        Individu I2 = new Sherif(I.getNom(), I.getHauteur() + 1, I.getPosition());
                        this.plateau[I2.getHauteur()][I2.getPosition()].ajouteIndividu(I2);
                        this.individusBord.set(i, I2);
                    }
                    System.out.println(I.getNom() + " grimpe sur le toit.");
                } else {
                    System.out.println(I.getNom() + " est déja sur le toit.");
                }
                break;
            case BAS:
                if (I.getHauteur() != 0) {
                    this.plateau[I.getHauteur()][I.getPosition()].supprimeIndividu(I);
                    if (I instanceof Bandit) {
                        Individu I2 = new Bandit(I.getNom(), I.getHauteur()-1, I.getPosition(),I.getSomme(), ((Bandit) I).getNbBalle(), ((Bandit) I).getActions(),((Bandit) I).getButins() );
                        this.plateau[I2.getHauteur()][I2.getPosition()].ajouteIndividu(I2);
                        this.individusBord.set(i, I2);
                    }
                    if (I instanceof Sherif) {
                        Individu I2 = new Sherif(I.getNom(), I.getHauteur() - 1, I.getPosition());
                        this.plateau[I2.getHauteur()][I2.getPosition()].ajouteIndividu(I2);
                        this.individusBord.set(i, I2);
                    }
                    System.out.println(I.getNom() + " descent dans le wagon.");
                } else {
                    System.out.println(I.getNom() + " est déja dans le wagon.");
                }
                break;
        }
    }

    Butin lacheButin(Individu I){
        // Vérifie d'abord si I est un Bandit et s'il a des butins
        if (I instanceof Bandit && !((Bandit) I).getButins().isEmpty()) {
            Random random = new Random();
            int k=this.getIndiceIndividu(I);
            int i = random.nextInt(((Bandit) I).getButins().size());
            int newSomme = I.getSomme() - ((Bandit) I).getButins().get(i).getValeur();
            ArrayList<Butin> newButins = ((Bandit) I).getButins();
            Butin b = newButins.remove(i);
            Individu I2 = new Bandit(I.getNom(), I.getHauteur(), I.getPosition(), newSomme, ((Bandit) I).getNbBalle(), ((Bandit) I).getActions(), newButins);
            this.plateau[I.getHauteur()][I.getPosition()].supprimeIndividu(I);
            this.plateau[I2.getHauteur()][I2.getPosition()].ajouteIndividu(I2);
            this.individusBord.set(k, I2);
            return b;
        } else {
            return null;
        }
    }

    void chasseBandit(){
        ToiGon w= this.getPlateau()[this.getIndividusBord().get(0).getHauteur()][this.getIndividusBord().get(0).getPosition()];
        if(w.getindividusBord().size()>1){
            for(int i=0; i<w.getindividusBord().size();i++){
                Individu I=w.getindividusBord().get(i);
                if(I instanceof Bandit) {
                    int k= this.getIndiceIndividu(I);
                    Butin b = this.lacheButin(I);
                    w.ajouteButin(b);
                    this.getPlateau()[I.getHauteur()][I.getPosition()] = w;
                    this.deplaceIndividu(this.getIndividusBord().get(k), Direction.HAUT);
                }
            }
        }
    }


    void braquage(Individu I){
        ToiGon w = this.getPlateau()[I.getHauteur()][I.getPosition()];
        if (w.getContenu().size() != 0) {
            int i = this.getIndiceIndividu(I);
            Random random = new Random();
            int b = random.nextInt(w.getContenu().size());
            if(w.getContenu().get(b)!=null) {
                int newSomme = I.getSomme() + w.getContenu().get(b).getValeur();
                ArrayList<Butin> newButins = ((Bandit) I).getButins();
                newButins.add(w.getContenu().get(b));
                Individu I2 = new Bandit(I.getNom(), I.getHauteur(), I.getPosition(), newSomme, ((Bandit) I).getNbBalle(), ((Bandit) I).getActions(), newButins);
                w.enleveButin(b);
                this.plateau[I.getHauteur()][I.getPosition()].supprimeIndividu(I);
                this.plateau[I2.getHauteur()][I2.getPosition()].ajouteIndividu(I2);
                this.individusBord.set(i, I2);
                plateau[I.getHauteur()][I.getPosition()] = w;
                System.out.println(I.getNom() + " Braque!!!!");
            }
        }
    }

    void tirer(Individu I) {
        int k=this.getIndiceIndividu(I);
        ToiGon w = this.getPlateau()[this.getIndividusBord().get(k).getHauteur()][this.getIndividusBord().get(k).getPosition()];
        if(((Bandit) I).getNbBalle()>0) {
            ((Bandit) I).decrement_NbBalles();
            if (w.getindividusBord().size() > 1) {
                for (int i = 0; i < w.getindividusBord().size(); i++) {
                    Individu I2 = w.getindividusBord().get(i);
                    if (i != w.getIndiceIndividu(I)) {
                        if (I instanceof Bandit) {
                            Butin b = this.lacheButin(I2);
                            w.ajouteButin(b);
                            this.getPlateau()[I.getHauteur()][I.getPosition()] = w;
                        }
                    }
                }
            }
        }
    }

    public void ajouteAction(Action A, int i){
        Individu I = this.getIndividusBord().get(i);
        ((Bandit) I).ajouteActions(A);
        Individu I2 = new Bandit(I.getNom(), I.getHauteur(), I.getPosition(), I.getSomme(), ((Bandit) I).getNbBalle(), ((Bandit) I).getActions(), ((Bandit) I).getButins()) ;
        this.plateau[I.getHauteur()][I.getPosition()].supprimeIndividu(I);
        this.plateau[I2.getHauteur()][I2.getPosition()].ajouteIndividu(I2);
        this.individusBord.set(i, I2);

    }

    public void supprimeAction(int iA, int iI){
        Individu I = this.getIndividusBord().get(iI);
        ((Bandit) I).supprimeActions(iA);
        Individu I2 = new Bandit(I.getNom(), I.getHauteur(), I.getPosition(), I.getSomme(), ((Bandit) I).getNbBalle(), ((Bandit) I).getActions(), ((Bandit) I).getButins()) ;
        this.plateau[I.getHauteur()][I.getPosition()].supprimeIndividu(I);
        this.plateau[I2.getHauteur()][I2.getPosition()].ajouteIndividu(I2);
        this.individusBord.set(iI, I2);
    }

    public int aQuiLeTour(){
        if(numAction!=0){
            return-1;
        }
        for(int i = 1; i < individusBord.size(); i++){
            Individu I = this.getIndividusBord().get(i);
            if(((Bandit) I).getActions().size() < 3){
                return i;
            }
        }
        this.numAction=3;
        return -1;
    }

    public void decrementeNumAction(){
        this.numAction=this.numAction-1;
    }

    public void activer( Individu I , Action a){
        if (a instanceof Deplacement) {
            this.deplaceIndividu(I,((Deplacement) a).getDir());
        } else if (a instanceof Braquage) {
            this.braquage(I);
        }else if(a instanceof Tirer){
            this.tirer(I);
        }
    }

    public void afficheTrain() {
        for (int i = 1; i >= 0; i--) {
            for (int j = 0; j < nb_Wagons; j++) {
                ArrayList<Individu> individus = plateau[i][j].getindividusBord();
                if (individus.size() == 0) {
                    System.out.print("  [ ]  ");
                } else {
                    System.out.print("  [");
                    for (int k = 0; k < individus.size(); k++) {

                        if (individus.get(k) instanceof Bandit) {
                            System.out.print("B"+individus.get(k).getSomme());

                        } else if (individus.get(k) instanceof Sherif) {
                            System.out.print("S");
                        }
                    }
                    System.out.print("]");
                }
            }
            System.out.println("\n");
        }
    }

    public int fin_de_jeux(){
        for(int i=1;i<this.individusBord.size();i++){
            if(((Bandit) individusBord.get(i)).a_le_magot()){
                return i;
            }
        }
        return -1;
    }

}

class ToiGon{
    private boolean estToit;
    private int numWagon;
    private ArrayList<Individu> individusBord;

    private ArrayList<Butin> contenu;

    public ToiGon(boolean b, int w){
        this.estToit = b;
        this.numWagon = w;
        this.individusBord = new ArrayList<Individu>();
        this.contenu = new ArrayList<Butin>();
    }

    public boolean isEstToit() {
        return estToit;
    }

    public int getNumWagon() {
        return numWagon;
    }

    public ArrayList<Butin> getContenu() {
        return contenu;
    }

    public ArrayList<Individu> getindividusBord() {
        return individusBord;
    }

    public int getIndiceIndividu(Individu I){
        for (int i=0; i<this.individusBord.size();i++){
            if(this.individusBord.get(i)==I) {
                return i;
            }
        }
        return -1;
    }


    public boolean estPresent(Individu I){
        if(this.getIndiceIndividu(I)==-1){
            return false;
        }else{
            return true;
        }
    }

    public void ajouteIndividu(Individu I){
        if (!estPresent(I)) {
            this.individusBord.add(I);
        }
    }

    public void supprimeIndividu(Individu I){
        if (estPresent(I)){
            this.individusBord.remove(getIndiceIndividu(I));
        }
    }

    public void ajouteButin(Butin B){
        this.contenu.add(B);
    }

    public void enleveButin(int i){
        this.contenu.remove(i);
    }
}






