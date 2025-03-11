import java.util.Random;
abstract class Butin {
    int valeur;

    public int getValeur() {
        return valeur;
    }
}

class Bijoux extends Butin{
    public Bijoux(){
        this.valeur=500;
    }
}

class Bourse extends Butin{
    public Bourse(){
        Random random = new Random();

        this.valeur=random.nextInt(501);
    }
}

class Magot extends Butin{
    public Magot(){
        this.valeur = 200;
    }
}