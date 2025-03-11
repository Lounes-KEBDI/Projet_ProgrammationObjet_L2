import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;
import java.io.*;
import javax.imageio.ImageIO;
import javax.sound.sampled.*;

public class Modele {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Démarrage du Jeu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(0, 1));
        JTextField nombreJoueursField = new JTextField();
        JComboBox<String> difficulteComboBox = new JComboBox<>(new String[]{"Facile", "Moyen", "Difficile"});
        JButton startButton = new JButton("Démarrer le jeu");
        JCheckBox musiqueCheckBox = new JCheckBox("Activer la musique");

        panel.add(new JLabel("Nombre de joueurs:"));
        panel.add(nombreJoueursField);
        panel.add(new JLabel("Difficulté:"));
        panel.add(difficulteComboBox);
        panel.add(musiqueCheckBox);
        panel.add(startButton);
        frame.add(panel, BorderLayout.CENTER);

        frame.setVisible(true);

        startButton.addActionListener(e -> {
            try {
                int nombreDeJoueurs = Integer.parseInt(nombreJoueursField.getText());
                String difficulte = (String) difficulteComboBox.getSelectedItem();
                ArrayList<String> nomsDesJoueurs = new ArrayList<>();

                for (int i = 0; i < nombreDeJoueurs; i++) {
                    String nom = JOptionPane.showInputDialog(frame,
                            "Entrez le nom du joueur " + (i + 1) + ":");
                    nomsDesJoueurs.add(nom);
                }

                int tailleDuTrain;
                int nombreDeBijoux;
                int nombreDeBourses;
                double nervositeMarshall;
                switch (difficulte) {
                    case "Facile":
                        tailleDuTrain = 6;
                        nombreDeBijoux = 2;
                        nombreDeBourses = 3;
                        nervositeMarshall = 0.3;
                        break;
                    case "Moyen":
                        tailleDuTrain = 7;
                        nombreDeBijoux = 3;
                        nombreDeBourses = 5;
                        nervositeMarshall = 0.5;
                        break;
                    case "Difficile":
                        tailleDuTrain = 7;
                        nombreDeBijoux = 5;
                        nombreDeBourses = 8;
                        nervositeMarshall = 1;
                        break;
                    default:
                        tailleDuTrain = 7;
                        nombreDeBijoux = 3;
                        nombreDeBourses = 5;
                        nervositeMarshall = 0.3;
                }

                Train t = new Train(tailleDuTrain, nervositeMarshall);
                for (String nom : nomsDesJoueurs) {
                    t.ajouteIndividu(new Bandit(nom, 1, tailleDuTrain-1, 0, 6, new ArrayList<>(), new ArrayList<>()));
                }

                Random random = new Random();
                for (int i = 0; i < nombreDeBijoux; i++) {
                    t.ajouteButin(new Bijoux(), 0, random.nextInt(tailleDuTrain));
                }
                for (int i = 0; i < nombreDeBourses; i++) {
                    t.ajouteButin(new Bourse(), 0, random.nextInt(tailleDuTrain));
                }

                t.afficheTrain();

                frame.dispose();
                new Tvue(t, musiqueCheckBox.isSelected());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Veuillez entrer un nombre valide de joueurs.",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}

class Tvue extends JPanel {
    private JFrame frame;
    private Train t;
    private JButton action;
    private JComboBox choixAction;
    private JButton validation;

    private Image fond;

    private Image panneau;
    private Image wagonImage;
    private Image jointure;
    private Image Sherif;
    private Image Bourse;

    private Image Magot;

    private Image Bijoux;

    private Image Bandit;
    public Tvue(Train t, boolean musique){
        this.t = t;
        this.frame = new JFrame("Joue ou je te bute!!");

        if(musique) {
            playSound("images/son.wav");
        }

        this.action = new JButton("Activer");
        String c[] = {"Monter", "Descendre", "Avant", "Arriere", "Braquage","Tirer"};
        this.choixAction = new JComboBox(c);
        this.validation = new JButton("Valider");

        Controleur controleur = new Controleur(this);
        action.addActionListener(controleur);
        validation.addActionListener(controleur);

        frame.setSize(new Dimension(700, 500));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panelBoutons = new JPanel();
        panelBoutons.setLayout(new FlowLayout());
        panelBoutons.add(validation);
        panelBoutons.add(action);

        frame.getContentPane().add(this);
        frame.getContentPane().add(choixAction, BorderLayout.NORTH);
        frame.getContentPane().add(panelBoutons, BorderLayout.SOUTH);

        try {
            fond = ImageIO.read(new File("images/fond.jpg"));
            panneau = ImageIO.read(new File("images/Panneau.png"));
            wagonImage = ImageIO.read(new File("images/wagon.jpg"));
            jointure = ImageIO.read(new File("images/jointure.jpg"));
            Sherif = ImageIO.read(new File("images/Sherif.png"));
            Bandit = ImageIO.read(new File("images/Bandit.png"));
            Bourse = ImageIO.read(new File("images/Bourse.png"));
            Magot =  ImageIO.read(new File("images/Magot.png"));
            Bijoux = ImageIO.read(new File("images/Bijoux.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        frame.setVisible(true);
    }

    public Train getT() {
        return t;
    }

    public JComboBox getChoixAction() {
        return choixAction;
    }

    public JButton getAction() {
        return action;
    }

    public JButton getValidation() {
        return validation;
    }

    public void playSound(String filePath) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Font myFont = new Font("Consolas", Font.BOLD, 12);
        g.setFont(myFont);
        g.drawImage(fond, 0, 0, getWidth(), (int)(getHeight() * (3.0 / 4)), this);
        int width = getWidth() / (this.t.getNb_Wagons()+1);
        int height = (int)(width * (3.5 / 4));
        g.drawImage(panneau,(int)((getWidth()/4.0)*1.5),(int)(height/3.0),(int)(getWidth()/4.0),height*2,this);
        int fondHeight = (int)(getHeight() * (3.0 / 4));
        int taille_perso=(int)(height/2.0);
        int largeur_perso=(int)(taille_perso/1.2);
        int taille_objet = (int)(height/5.0);
        int largeur_objet = (int)(taille_objet/1.2);
        int x=30;
        int y = fondHeight - 2*height;
        int i=1;
        for (int j = 0; j < this.t.getNb_Wagons(); j++) {
            ArrayList<Individu> individus = this.t.getPlateau()[i][j].getindividusBord();
            ArrayList<Butin> butins = this.t.getPlateau()[i][j].getContenu();
            if(individus.size()!=0 && butins.size()==0){
                int x1 = x;
                for (int k = 0; k < individus.size(); k++) {
                    if (individus.get(k) instanceof Bandit) {
                        g.drawImage(Bandit,x1 + 40,y+taille_perso,largeur_perso,taille_perso,this);
                        g.drawString( individus.get(k).getNom()+"("+((Bandit) individus.get(k)).getNbBalle()+")", x1 + 40, y+taille_perso-10);
                    } else if (individus.get(k) instanceof Voyageur) {
                        g.drawString("V", x1 + 40, y + height);
                    } else if (individus.get(k) instanceof Sherif) {
                        g.drawImage(Sherif,x1 + 40,y+taille_perso,largeur_perso,taille_perso,this);
                    }
                    x1 = x1 + 40;
                }
            }else if(individus.size()==0 && butins.size()!=0){
                int x1 = x;
                for (int k = 0; k < butins.size(); k++) {
                    if (butins.get(k) instanceof Bourse) {
                        g.drawImage(Bourse,x1 + 40,y+(taille_objet)*4,largeur_objet,taille_objet,this);
                    } else if (butins.get(k) instanceof Magot) {
                        g.drawImage(Magot,x1 + 40,y+(taille_objet)*4,largeur_objet,taille_objet,this);
                    } else if (butins.get(k) instanceof Bijoux) {
                        g.drawImage(Bijoux,x1 + 40,y+(taille_objet)*4,largeur_objet,taille_objet,this);
                    }
                    x1=x1+40;
                }
            }else{
                int x1 = x;
                for (int k = 0; k < individus.size(); k++) {
                    if (individus.get(k) instanceof Bandit) {
                        g.drawImage(Bandit,x1 + 40,y+taille_perso,largeur_perso,taille_perso,this);
                        g.drawString( individus.get(k).getNom()+"("+((Bandit) individus.get(k)).getNbBalle()+")", x1 + 40, y+taille_perso-10);
                    } else if (individus.get(k) instanceof Voyageur) {
                        g.drawString("V", x1 + 40, y + height);
                    } else if (individus.get(k) instanceof Sherif) {
                        g.drawImage(Sherif,x1 + 40,y+taille_perso,largeur_perso,taille_perso,this);
                    }
                    x1 = x1 + 40;
                }
                for (int k = 0; k < butins.size(); k++) {
                    if (butins.get(k) instanceof Bourse) {
                        g.drawImage(Bourse,x1 + 40,y+(taille_objet)*4,largeur_objet,taille_objet,this);
                    } else if (butins.get(k) instanceof Magot) {
                        g.drawImage(Magot,x1 + 40,y+(taille_objet)*4,largeur_objet,taille_objet,this);
                    } else if (butins.get(k) instanceof Bijoux) {
                        g.drawImage(Bijoux,x1 + 40,y+(taille_objet)*4,largeur_objet,taille_objet,this);
                    }
                    x1 = x1 + 40;
                }
            }
            x=x+width+30;
        }
        x=0;
        y=y+height;
        i=0;
        for (int j = 0; j < this.t.getNb_Wagons(); j++) {
            g.drawImage(jointure, x, y, 30, height, this);
            x=x+30;
            ArrayList<Individu> individus = this.t.getPlateau()[i][j].getindividusBord();
            ArrayList<Butin> butins = this.t.getPlateau()[i][j].getContenu();
            if (individus.size() == 0 && butins.size() == 0) {
                g.drawImage(wagonImage, x, y, width, height, this);
            } else if(individus.size()!=0 && butins.size()==0){
                int x1 = x;
                g.drawImage(wagonImage, x, y, width, height, this);
                for (int k = 0; k < individus.size(); k++) {
                    if (individus.get(k) instanceof Bandit) {
                        g.drawImage(Bandit,x1 + 40,y+taille_perso,largeur_perso,taille_perso,this);
                        g.drawString( individus.get(k).getNom()+"("+((Bandit) individus.get(k)).getNbBalle()+")", x1 + 40, y+taille_perso-10);
                    } else if (individus.get(k) instanceof Voyageur) {
                        g.drawString("V", x1 + 40, y + height);
                    } else if (individus.get(k) instanceof Sherif) {
                        g.drawImage(Sherif,x1 + 40,y+taille_perso,largeur_perso,taille_perso,this);
                    }
                    x1 = x1 + 40;
                }
            }else if(individus.size()==0 && butins.size()!=0){
                int x1 = x;
                g.drawImage(wagonImage, x, y, width, height, this);
                for (int k = 0; k < butins.size(); k++) {
                    if (butins.get(k) instanceof Bourse) {
                        g.drawImage(Bourse,x1 + 40,y+(taille_objet)*4,largeur_objet,taille_objet,this);
                    } else if (butins.get(k) instanceof Magot) {
                        g.drawImage(Magot,x1 + 40,y+(taille_objet)*4,largeur_objet,taille_objet,this);
                    } else if (butins.get(k) instanceof Bijoux) {
                        g.drawImage(Bijoux,x1 + 40,y+(taille_objet)*4,largeur_objet,taille_objet,this);
                    }
                    x1=x1+40;
                }
            }else{
                int x1 = x;
                g.drawImage(wagonImage, x, y, width, height, this);
                for (int k = 0; k < individus.size(); k++) {
                    if (individus.get(k) instanceof Bandit) {
                        g.drawImage(Bandit,x1 + 40,y+taille_perso,largeur_perso,taille_perso,this);
                        g.drawString( individus.get(k).getNom()+"("+((Bandit) individus.get(k)).getNbBalle()+")", x1 + 40, y+taille_perso-10);
                    } else if (individus.get(k) instanceof Voyageur) {
                        g.drawString("V", x1 + 40, y + height);
                    } else if (individus.get(k) instanceof Sherif) {
                        g.drawImage(Sherif,x1 + 40,y+taille_perso,largeur_perso,taille_perso,this);
                    }
                    x1 = x1 + 40;
                }
                for (int k = 0; k < butins.size(); k++) {
                    if (butins.get(k) instanceof Bourse) {
                        g.drawImage(Bourse,x1 + 40,y+(taille_objet)*4,largeur_objet,taille_objet,this);
                    } else if (butins.get(k) instanceof Magot) {
                        g.drawImage(Magot,x1 + 40,y+(taille_objet)*4,largeur_objet,taille_objet,this);
                    } else if (butins.get(k) instanceof Bijoux) {
                        g.drawImage(Bijoux,x1 + 40,y+(taille_objet)*4,largeur_objet,taille_objet,this);
                    }
                    x1 = x1 + 40;
                }
            }
            x=x+width;
        }
        x=(int)(getWidth()/2.2);
        y=(int)(y+height*1.2);
        if(t.aQuiLeTour()!=-1) {
            g.drawString("A " + t.getIndividusBord().get(t.aQuiLeTour()).getNom()+ " de planifier", x, y);
        }else{
            g.drawString("A l'action maintenant !!",x,y);
        }
        for(int j = 1; j < t.getIndividusBord().size(); j++){
            Individu I=t.getIndividusBord().get(j);
            y=(int)(y*1.05);
            g.drawString(I.getNom() +" a planié :",0, y );
            for (int k=0; k<((Bandit) I).getActions().size();k++){
                if(((Bandit) I).getActions().get(k) instanceof Deplacement) {
                    if (((Deplacement) ((Bandit) I).getActions().get(k)).getDir() == Direction.AVANT) {
                        g.drawString("Avant", 120 + k * 80, y);
                    } else if (((Deplacement) ((Bandit) I).getActions().get(k)).getDir() == Direction.ARRIERE) {
                        g.drawString("Arrière", 120 + k * 80, y );
                    } else if (((Deplacement) ((Bandit) I).getActions().get(k)).getDir() == Direction.HAUT) {
                        g.drawString("Haut", 120 + k * 80, y );
                    } else if (((Deplacement) ((Bandit) I).getActions().get(k)).getDir() == Direction.BAS) {
                        g.drawString("Bas", 120 + k * 80, y);
                    }
                }else if(((Bandit) I).getActions().get(k) instanceof Braquage){
                    g.drawString("Braquage", 120 +k*80, y);
                }else{
                    g.drawString("Tire", 120 +k*80, y);
                }
            }
            g.drawString(I.getNom() +" a voler ("+I.getSomme()+"):",getWidth()/2, y );
            for(int k=0; k<((Bandit) I).getButins().size();k++){
                if (((Bandit) I).getButins().get(k) instanceof Bourse) {
                    g.drawImage(Bourse,getWidth()/2+160 + k*80,y-(taille_objet/2),largeur_objet,taille_objet,this);
                } else if (((Bandit) I).getButins().get(k) instanceof Magot) {
                    g.drawImage(Magot,getWidth()/2+160 + k*80,y-(taille_objet/2),largeur_objet,taille_objet,this);
                } else if (((Bandit) I).getButins().get(k) instanceof Bijoux) {
                    g.drawImage(Bijoux,getWidth()/2+160 + k*80,y-(taille_objet/2),largeur_objet,taille_objet,this);
                }
            }
        }
    }
}

class Controleur implements ActionListener {

    Tvue Tvue;

    public Controleur(Tvue Tvue){
        this.Tvue= Tvue;
    }
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==Tvue.getValidation()) {
            String selectedAction = (String) Tvue.getChoixAction().getSelectedItem();
            int i=Tvue.getT().aQuiLeTour();
            if(i!=-1) {
                if (selectedAction.equals("Monter")) {
                    Action a1 = new Deplacement(Tvue.getT().getIndividusBord().get(i), Direction.HAUT);
                    Tvue.getT().ajouteAction(a1, i);
                } else if (selectedAction.equals("Descendre")) {
                    Action a1 = new Deplacement(Tvue.getT().getIndividusBord().get(i), Direction.BAS);
                    Tvue.getT().ajouteAction(a1, i);
                } else if (selectedAction.equals("Avant")) {
                    Action a1 = new Deplacement(Tvue.getT().getIndividusBord().get(i), Direction.AVANT);
                    Tvue.getT().ajouteAction(a1, i);
                } else if (selectedAction.equals("Arriere")) {
                    Action a1 = new Deplacement(Tvue.getT().getIndividusBord().get(i), Direction.ARRIERE);
                    Tvue.getT().ajouteAction(a1, i);
                } else if (selectedAction.equals("Braquage")) {
                    Action a2 = new Braquage(Tvue.getT().getIndividusBord().get(i));
                    Tvue.getT().ajouteAction(a2, i);
                }else if (selectedAction.equals("Tirer")) {
                    Action a2 = new Tirer(Tvue.getT().getIndividusBord().get(i));
                    Tvue.getT().ajouteAction(a2, i);
                }
                Tvue.repaint();
            }
        }else if(e.getSource()==Tvue.getAction()){
            if(Tvue.getT().aQuiLeTour()==-1) {
                Tvue.getT().decrementeNumAction();

                Random random = new Random();
                double probabiliteDeplacement = random.nextDouble();
                Action Q = null;
                if (probabiliteDeplacement <= Tvue.getT().getNERVOSITE_MARSHALL()) {
                    Random random1 = new Random();
                    int c = random1.nextInt(2);
                    if (c == 0) {
                        Q = new Deplacement(this.Tvue.getT().getIndividusBord().get(0), Direction.ARRIERE);
                    } else {
                        Q = new Deplacement(this.Tvue.getT().getIndividusBord().get(0), Direction.AVANT);
                    }
                    Tvue.getT().activer(this.Tvue.getT().getIndividusBord().get(0), Q);
                }
                for (int i = 1; i < Tvue.getT().getIndividusBord().size(); i++) {
                    active(Tvue.getT().getIndividusBord().get(i));
                }
                if(Tvue.getT().fin_de_jeux()==-1) {
                    Tvue.getT().afficheTrain();
                    Tvue.repaint();
                }else{
                    Tvue.setVisible(false);

                    int indexGagnant = Tvue.getT().fin_de_jeux();
                    for(int i=1 ;i< Tvue.getT().getIndividusBord().size();i++) {
                        if (Tvue.getT().getIndividusBord().get(i).getSomme() > Tvue.getT().getIndividusBord().get(indexGagnant).getSomme()) {
                            indexGagnant = i;
                        }
                    }
                    String nomGagnant = Tvue.getT().getIndividusBord().get(indexGagnant).getNom();

                    JFrame frameGagnant = new JFrame("Gagnant du jeu");
                    frameGagnant.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frameGagnant.setSize(300, 200);
                    frameGagnant.setLayout(new BorderLayout());

                    JLabel labelGagnant = new JLabel("Le gagnant est : " + nomGagnant, JLabel.CENTER);
                    frameGagnant.add(labelGagnant, BorderLayout.CENTER);

                    frameGagnant.setLocationRelativeTo(null);
                    frameGagnant.setVisible(true);
                }
            }
        }
    }

    public void active(Individu I) {
        Tvue.getT().activer(I, ((Bandit) I).getActions().get(0));
        Tvue.getT().supprimeAction(0, Tvue.getT().getIndiceIndividu(I));
        Tvue.getT().chasseBandit();
    }
}
