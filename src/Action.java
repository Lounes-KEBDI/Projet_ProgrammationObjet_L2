public abstract class Action {
    private Individu acteur;

    public Action(Individu I){
        this.acteur = I;
    }

    public Individu getActeur(){
        return acteur;
    }

}

class Deplacement extends Action{
    private Direction Dir;

    public Deplacement(Individu I,Direction d){
        super(I);
        this.Dir= d;
    }
    public Direction getDir() {
        return Dir;
    }
}

class Braquage extends Action{
    public Braquage(Individu I){
        super(I);
    }


}

class Tirer extends Action{
    public Tirer(Individu I){ super(I);}
}