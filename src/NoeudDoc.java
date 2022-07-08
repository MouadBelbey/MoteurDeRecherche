//Mouad Belbey et Van Nam Vu 

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.lang.*;

public class NoeudDoc {

	/*
	 * Classe noeudDoc : structure representant un index du document. Est suivi par
	 * un prochain noeud (selon l'ordre le la liste)
	 */

	private String index = "d"; // pour index doc
	private static int compteur = 1; // compteur pour index doc
	private String texte; // contient le contenu du doc
	public String[] listMot; // tableau contient les mots du doc
	private NoeudDoc prochain;
	NoeudMot NoeudMotPremier; // pour la stucture indexation

	private ArrayList<String> listeDoc = new ArrayList<String>(); // stock index de document et frequence

	// constructeur : prend en parametres le prochain noeud
	public NoeudDoc(NoeudDoc n) {
		this.prochain = n;
		NoeudMotPremier = null; // pour initialiser noeud mot premier

	}

	// pour structure indexation vertical - get le prochain noeud du noeud actuel
	public NoeudDoc getProchain() {
		return this.prochain;
	}

	// pour structure indexation vertical - set le prochain noeud du noeud actuel
	public void setProchain(NoeudDoc n) {
		this.prochain = n;
	}

	// pour structure indexation - set index
	public void setIndexDoc() {
		this.index += compteur;
		compteur++;
		listeDoc.add(index);
	}

	// pour structure indexation et structure inverse - conserver contenu du doc
	public void setContenu(String texte) {
		this.texte = texte;
		listeDoc.add(texte);
	}

	// pour recuperer index de document
	public String getIndex() {
		return index;
	}

	// pour recuperer le conteunu du doc
	public String getContenu() {
		return texte;
	}

	// ----------- Partie traitement un doc pour la structure indexation --------

	// fonction principale pour traiter un doc
	public void traitementDoc(String path) {
		try {
			this.texte = lireFichier(path); // lire le fichier
			coupeMot(texte); // creer directement les noeuds de mots
			triMot();

		} catch (IOException e) {

			System.out.println("Fichier non existant !");
		}
	}

	// fonction lire fichier doc - return string texte
	public String lireFichier(String pathFichier) throws IOException {

		BufferedReader reader = new BufferedReader(new FileReader(pathFichier));
		String ligne = "";
		String texteLu = "";

		while ((ligne = reader.readLine()) != null) {

			texteLu += ligne + " ";

		}
		return texteLu;

	}

	// fonction pour couper en mot et les ajoute dans les noeuds de mot
	public void coupeMot(String texte) {

		try {
			texte = texte.replaceAll("[^A-z0-9]", " "); // pour eliminer les symboles bizarres

			listMot = texte.split(" +");

			// pour convertir les majuscule en minuscule
			for (int i = 0; i < listMot.length; i++) {
				char premierLettre = listMot[i].charAt(0);

				if (Character.isUpperCase(premierLettre)) { // convertir les mot Majuscule
															// en minuscule
					listMot[i] = listMot[i].toLowerCase();
				}
			}

			// pour ajouter les mots dans les noeuds de mot
			for (int i = 0; i < listMot.length; i++) {

				if (estVideNoeudMot()) { // si c'est premier mot
					ajoutPremierMot(listMot[i]);

				} else {

					if (estTrouveMot(listMot[i]) == true) // si le mot est existe deja
						continue;
					else // si le mot n'est pas encore existe
						ajoutNoeudMot(listMot[i]);
				}
			}
			
		} catch (StringIndexOutOfBoundsException e) {
			System.out.println("Doc est vide !");
		}
	}

	// fonction pour chercher le mot dans les noeuds de mots
	public boolean estTrouveMot(String mot) {

		NoeudMot noeudMotActuel = this.NoeudMotPremier;

		while (noeudMotActuel != null) {

			if (noeudMotActuel.getMot().equalsIgnoreCase(mot)) { // compare mot en ignorant Maj ou Min
				noeudMotActuel.increaseFrequence(); // increment frequence
				return true;
			}
			noeudMotActuel = noeudMotActuel.getProchain();
		}
		return false;
	}

	// methode permettant de verifier si la liste est vide
	public boolean estVideNoeudMot() {

		if (NoeudMotPremier == null) {
			return true;
		}
		return false;

	}

	// methode de calcul de la longueur totale de la liste de noeud mot
	public int longueur() {
		int compte = 0;// compteur

		NoeudMot noeudActuel = this.NoeudMotPremier;// on commence par compter le premier noeud

		while (noeudActuel != null) {
			// tant que l on atteint pas "null", on continue de suivre tous les noeuds et de
			// les compter
			compte++;
			noeudActuel = noeudActuel.getProchain();

		}

		return compte;// on renvoie le compteur

	}

	// methode permettant d'ajouter directement un mot en premiere position
	public void ajoutPremierMot(String mot) {

		NoeudMot n = new NoeudMot(mot, null); // creation du noeud qui sera positionne en premiere position
		ajoutPremierMot(n);

	}

	// methode permettant d'ajouter un noeud en premiere position
	public void ajoutPremierMot(NoeudMot n) {

		n.setProchain(this.NoeudMotPremier);
		this.NoeudMotPremier = n;
	}

	// Methode d ajout d un mot a la fin de la liste
	public void ajoutNoeudMot(String mot) {

		NoeudMot n = new NoeudMot(mot, null);
		ajoutNoeudMot(n);

	}

	// methode permettant d ajouter un noeud a la liste (en derniere position)
	public void ajoutNoeudMot(NoeudMot n) {

		if (estVideNoeudMot()) {
			this.NoeudMotPremier = n;
		} else {
			NoeudMot noeudActuel = this.NoeudMotPremier;
			while (noeudActuel.getProchain() != null) { //cherche le dernier noeud de mot
				noeudActuel = noeudActuel.getProchain();
			}
			noeudActuel.setProchain(n);

		}

	}

	// pour trier les noeuds de mots
	public void triMot() {
		NoeudDoc nouvelleListe = new NoeudDoc(null);

		NoeudMot noeudMotActuel = this.NoeudMotPremier;

		NoeudMot noeudMotProchain;

		while (noeudMotActuel != null) {

			noeudMotProchain = noeudMotActuel.getProchain();
			nouvelleListe.insertionMot(noeudMotActuel); 
			noeudMotActuel = noeudMotProchain; //passe au prochain noeud

		}
		
		this.NoeudMotPremier = nouvelleListe.NoeudMotPremier; //mettre a jour avec liste noeud mot triee
	}

	// Pour inserer selon ordre
	public void insertionMot(NoeudMot n) {
		
		if (estVideNoeudMot()) {
			this.ajoutPremierMot(n);
		}

		else if (this.NoeudMotPremier.getMot().compareTo(n.getMot()) > 0) { //si le mot courant est avant le premier mot
			n.setProchain(this.NoeudMotPremier);
			this.NoeudMotPremier = n;
		}

		else {
			NoeudMot nouedActuel = this.NoeudMotPremier;
			while (nouedActuel.getProchain() != null && nouedActuel.getProchain().getMot().compareTo(n.getMot()) < 0) {
				nouedActuel = nouedActuel.getProchain();
			}

			n.setProchain(nouedActuel.getProchain());
			nouedActuel.setProchain(n);
		}
	}

	// methode d'affichage du contenu de la liste mots - structure indexation
	public String printNoeudsMot() {
		String mots = ""; // stockage du contenu
		if (!estVideNoeudMot()) {
			NoeudMot noeudActuel = this.NoeudMotPremier;
			while (noeudActuel != null) {
				// pour chaque noeud disponible
				mots += "   -> " + noeudActuel.toString() + " \n";
				noeudActuel = noeudActuel.getProchain();// mouvement vers le prochain noeud

			}
			mots += "   -> null\n\n";// on rajoute le null de fin

		}
		return mots;
	}

	// ------------- les fonctions pour la structure inverse ---------

	// set index doc pour la structure inverse
	public void setIndex(String index) {
		this.index = index;
		listeDoc.add(index);
	}

	// set frequence mot pour structure inverse
	public void setFrequence(int frequence) {
		// System.out.println(listeDoc.toString());
		listeDoc.add(frequence + "");
	}

	// get frequence pour structure inverse
	public int getFrequence() {
		// System.out.println(listeDoc.toString());
		return Integer.parseInt(listeDoc.get(1));
	}

	// pour recuperer array index doc et frequence
	public String[] getListeDoc() {
		String[] listDoc = listeDoc.toArray(new String[listeDoc.size()]);

		return listDoc;
	}
}
