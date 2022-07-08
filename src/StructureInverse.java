//Mouad Belbey et Van Nam Vu 

/*Cette class sert a construire la structure inverse
 *Les mots de cette structure sera trie par insertion au fur et a mesure
 *car la liste de mot a deja trie selon Structure Indexation
 */

public class StructureInverse {

	StructureIndex liste = new StructureIndex(); // pour recuperer la liste creer

	NoeudMot noeudMotPremier = null; // pour creer liste verticale de noeud mot

	//constructeur
	public StructureInverse() {
		noeudMotPremier = null;
	}

	// fonction pour inverse StructureIndex
	public void Inverse(StructureIndex liste) {
		this.liste = liste;
		NoeudDoc noeudDocActuel = liste.premier; // recuperer les docs de la structure indexation

		while (noeudDocActuel != null) { // passe chaque noeud doc

			NoeudMot noeudMotActuel = noeudDocActuel.NoeudMotPremier; // mot actuelle de structure index

			while (noeudMotActuel != null) { // passe chaque noeud de mot

				insertionMot(noeudMotActuel.getMot(), noeudMotActuel.getFrequence(), noeudDocActuel.getContenu(),
						noeudDocActuel);

				noeudMotActuel = noeudMotActuel.getProchain();

			}

			noeudDocActuel = noeudDocActuel.getProchain();
		}

	}

	// methode permettant de verifier si la liste est vide
	public boolean estVide() {

		if (noeudMotPremier == null) {
			return true;
		}
		return false;

	}
	
	// pour insertion en ordre alphabetique les mots
	// Parametre : mot, frequence du mot, contenu du doc correspondant, et noeudDoc correspondant
	public void insertionMot(String mot, int frequence, String contenu, NoeudDoc noeudDoc) {

		if (estVide()) { // quand la liste est vide
			ajoutPremierMot(mot, frequence, contenu, noeudDoc);
		}

		else if (this.noeudMotPremier.getMot().compareTo(mot) > 0) { //si le mot actuel est avant noeudMot premier
			ajoutPremierMot(mot, frequence, contenu, noeudDoc);
		}

		else { //si le mot est apres le premier noeudMot
			ajoutNoeudMot(mot, frequence, contenu, noeudDoc);
		}
	}

	// methode permettant d ajouter directement un mot en premiere position
	// Parametre : mot, frequence du mot, contenu du doc, et noeudDoc correspondant
	public void ajoutPremierMot(String motNoeud, int frequence, String contenu, NoeudDoc noeudDoc) {
		
		NoeudMot n = new NoeudMot(motNoeud, null);// creation du noeud qui sera positionne en premiere position
		ajoutPremierMot(n, frequence, contenu, noeudDoc);

	}

	// methode permettant d ajouter un noeud en premiere position
	// Parametre : NoeudMot, frequence du mot, contenu du doc, et noeudDoc correspondant
	public void ajoutPremierMot(NoeudMot noeudMot, int frequence, String contenu, NoeudDoc noeudDoc) {
		
		noeudMot.setProchain(this.noeudMotPremier);
		this.noeudMotPremier = noeudMot;
		this.noeudMotPremier.ajoutNoeudDoc(noeudDoc, frequence, contenu);
	}
	
	// Methode d ajout d un mot dans la liste selon ordre alphabetique
	// Parametre : mot, frequence du mot, contenu du doc, et noeudDoc correspondant
	public void ajoutNoeudMot(String mot, int frequence, String contenu, NoeudDoc noeudDoc) {

		NoeudMot n = new NoeudMot(mot, null);
		ajoutNoeudMot(n, frequence, contenu, noeudDoc);

	}

	// methode permettant d ajouter un noeudMot en ordre alphabetique
	// Parametre : NoeudMot, frequence du mot, contenu du doc, et noeudDoc correspondant
	public void ajoutNoeudMot(NoeudMot noeudMot, int frequence, String contenu, NoeudDoc noeudDoc) {

		if (estTrouveMot(noeudMot.getMot()) == true) { //si c'est le meme mot
			NoeudMot noeudMotActuel = this.noeudMotPremier;

			while (noeudMotActuel != null) {
				
				if (noeudMotActuel.getMot().equalsIgnoreCase(noeudMot.getMot())) { // ignore Maj ou Min
					
					noeudMotActuel.ajoutNoeudDoc(noeudDoc, frequence, contenu); //ajout noeudDoc dans le noeudMot correspondant
				}
				noeudMotActuel = noeudMotActuel.getProchain();
			}

		} else { // insertion en ordre directement pour que la StructureInverse soit trier
			
			NoeudMot noeudMotActuel = this.noeudMotPremier; //noeud mot actuel de structure inverse

			while (noeudMotActuel.getProchain() != null
					&& noeudMotActuel.getProchain().getMot().compareTo(noeudMot.getMot()) < 0) {
				noeudMotActuel = noeudMotActuel.getProchain();
			}

			noeudMot.setProchain(noeudMotActuel.getProchain());
			noeudMotActuel.setProchain(noeudMot); //ajout noeudMot a cette position

			noeudMot.ajoutNoeudDoc(noeudDoc, frequence, contenu); //ajout noeud doc pour ce noeud mot

		}

	}
	
	// pour verifier si mot est existe --> ajoute noeud index Doc dans la noeudMot
	public boolean estTrouveMot(String mot) {

		NoeudMot noeudMotActuel = this.noeudMotPremier;

		while (noeudMotActuel != null) {

			if (noeudMotActuel.getMot().equalsIgnoreCase(mot)) { // ignore Maj ou Min
				return true;
			}
			noeudMotActuel = noeudMotActuel.getProchain();
		}
		return false;
	}

	// fonction affichage structure inverse - return string pour affichage sur interface
	public String affichage() {
		
		String structureInverse = ""; // stockage du contenu
		if (!estVide()) {
			NoeudMot noeudActuel = this.noeudMotPremier;
			while (noeudActuel != null) {
				// pour chaque noeud disponible
				structureInverse += "Mot " + noeudActuel.getMot() + " : \n";
				structureInverse += noeudActuel.printNoeudsDoc();
				noeudActuel = noeudActuel.getProchain();// mouvement vers le prochain noeud

			}
			structureInverse += "null";// on rajoute le null de fin
			System.out.println(structureInverse);// on affiche le contenu
			return structureInverse;
		}
		return structureInverse;
	}

}
