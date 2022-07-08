//Mouad Belbey et Van Nam Vu 

import java.util.ArrayList;

public class NoeudMot {

	/*
	 * Classe noeudMot : structure representant un mot du document. Est suivi par un
	 * prochain noeud (selon l'ordre le la liste)
	 */

	private ArrayList<String> listeMot = new ArrayList<String>(); // contient le mot et la frequence
	private NoeudMot prochain;

	NoeudDoc NoeudDocPremier = null; // noeud doc pour la structure inverse

	// constructeur : prend en parametres le prochain noeud ainsi que le mot contenu
	public NoeudMot(String m, NoeudMot n) {

		listeMot.add(m); // mot
		listeMot.add("1"); // frequence du mot
		this.prochain = n;

	}

	// pour get le prochain noeud du noeud actuel
	public NoeudMot getProchain() {
		return this.prochain;
	}

	// pour set le prochain noeud du noeud actuel
	public void setProchain(NoeudMot n) {
		this.prochain = n;
	}
	
	// pour recuperer mot
	public String getMot() {
		return listeMot.get(0);
	}

	// pour recuperer la frequence
	public int getFrequence() {
		return Integer.parseInt(listeMot.get(1));
	}

	// pour incrementer la frequence
	public void increaseFrequence() {
		listeMot.set(1, Integer.parseInt(listeMot.get(1)) + 1 + "");
	}

	// pour print noeud mot
	public String toString() {
		return "Mot : " + listeMot.get(0) + " - frequence : " + listeMot.get(1);
	}

	// ------------pour la structure inverse ----------

	// methode permettant de verifier si la liste noeud doc de la structure inverse
	// est vide
	public boolean noeudDocEstVide() {
		if (this.NoeudDocPremier == null) {
			return true;
		}
		return false;
	}

	// methode de calcul de la longueur totale de la liste
	public int longueur() {
		int compte = 0;// compteur

		NoeudDoc noeudActuel = this.NoeudDocPremier;// on commence par compter le premier noeud

		while (noeudActuel != null) {
			// tant que l on atteint pas "null", on continue de suivre tous les noeuds et de
			// les compter
			compte++;
			noeudActuel = noeudActuel.getProchain();

		}

		return compte;// on renvoie le compteur

	}

	// ajout 1er noeud Doc
	public void ajoutPremierDoc(NoeudDoc n) {

		this.NoeudDocPremier = n;
		this.NoeudDocPremier.setProchain(null);

	}

	// methode permettant d ajouter un noeud a la liste (en derniere position)
	public void ajoutNoeudDoc(NoeudDoc noeudDoc, int frequence, String texte) {

		NoeudDoc nDoc = new NoeudDoc(null);
		nDoc.setIndex(noeudDoc.getIndex()); // set index doc
		nDoc.setFrequence(frequence); // set frequence du mot dans index doc correspondant
		nDoc.setContenu(texte); // set contenu du doc sert a afficher au utilisateur

		if (noeudDocEstVide()) { // si c'est le premier doc
			this.NoeudDocPremier = nDoc;
		}

		else { // ajoute a la fin de la liste
			NoeudDoc noeudActuel = this.NoeudDocPremier;

			while (noeudActuel.getProchain() != null) {
				noeudActuel = noeudActuel.getProchain();
			}

			noeudActuel.setProchain(nDoc);
		}

	}

	// fonction pour print la liste des docs contient le mot du noeud mot -> pour
	// structure inverse
	public String printNoeudsDoc() {

		String mots = ""; // stockage du contenu
		if (!noeudDocEstVide()) {
			NoeudDoc noeudActuel = this.NoeudDocPremier;

			while (noeudActuel != null) { // pour chaque noeud doc disponible

				mots += "   -> " + noeudActuel.getIndex() + " : " + noeudActuel.getFrequence() + "\n";
				noeudActuel = noeudActuel.getProchain(); // passer au prochain noeud doc

			}

			mots += "   -> null\n"; // on rajoute le null de fin
			return mots;

		} else
			return mots;

	}

}
