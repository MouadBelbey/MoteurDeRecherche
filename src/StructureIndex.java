//Mouad Belbey et Van Nam Vu 

public class StructureIndex {
	NoeudDoc premier;// premier noeud de la liste : voie d entree dans la structure

	// constructeur : initialise un premier noeud doc vide
	public StructureIndex() {
		premier = null;
	}
	
	// methode permettant de verifier si la liste est vide
	public boolean estVide() {
		
		if (premier == null) {
			return true;
		}
		return false;

	}
	
	// methode de calcul de la longueur totale de la liste
	public int longueur() {
		
		int compte = 0;// compteur

		NoeudDoc noeudActuel = this.premier;// on commence par compter le premier noeud

		while (noeudActuel != null) {
			// tant que l on atteint pas "null", on continue de suivre tous les noeuds et de
			// les compter
			compte++;
			noeudActuel = noeudActuel.getProchain();

		}

		return compte;// on renvoie le compteur
	}
	
	// methode permettant d ajouter un noeud de doc en premiere position
	public void ajoutPremierDoc(String path) {
		
		NoeudDoc nDoc = new NoeudDoc(null);
		nDoc.setProchain(this.premier);
		this.premier = nDoc;
		nDoc.setIndexDoc(); //set index pour structure indexation
		nDoc.traitementDoc(path); //pour traiter le Doc
		
	}
	
	// methode permettant d ajouter un noeud a la liste (en derniere position)
	public void ajoutNoeudDoc(String path) {
		
		NoeudDoc nDoc = new NoeudDoc(null);

		if (estVide()) { //si liste doc est vide
			
			this.premier = nDoc;
			nDoc.setIndexDoc(); //set index doc pour structure indexation
			nDoc.traitementDoc(path); //pour traiter le Doc
		}

		else {
			NoeudDoc noeudActuel = this.premier;
			while (noeudActuel.getProchain() != null) {
				noeudActuel = noeudActuel.getProchain();
			}

			noeudActuel.setProchain(nDoc);
			nDoc.setIndexDoc(); //set index doc pour structure indexation
			nDoc.traitementDoc(path); //pour traiter le Doc
		}

	}

	//fonction affichage structure indexation - return string pour affichage sur interface
	public String affichage() {

		String structure = ""; // stockage du contenu
		if (!estVide()) {
			NoeudDoc noeudActuel = this.premier;
			while (noeudActuel != null) { // pour chaque noeud disponible
				
				structure += "Doc " + noeudActuel.getIndex() + " : \n";
				structure += noeudActuel.printNoeudsMot();
				noeudActuel = noeudActuel.getProchain();// mouvement vers le prochain noeud

			}
			structure += "null";// on rajoute le null de fin
			System.out.println(structure);// on affiche le contenu
			return structure;
		}
		return structure;
	}
	
}
