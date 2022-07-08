//Mouad Belbey et Van Nam Vu 

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class InterfaceUser extends JFrame {

	JLabel labelRecherche;
	JLabel labelResultat;
	JLabel labelContenu;
	JTextField champRecherche;
	JTextArea champContenu;
	JButton buttonRecherche;

	JMenuBar menuBar;
	JMenu menu1;
	JMenu menu2;
	private JMenuItem Recherche = new JMenuItem("Recherche");
	private JMenuItem Admin = new JMenuItem("Administration");

	JPanel panelDefaut = new JPanel(); // panel vide par defaut
	JPanel panelRecherche = new JPanel(); // panel pour les composantes de recherche
	JPanel panelResultat = new JPanel(); // panel pour les composantes du resultat
	JPanel panelTable = new JPanel(); // panel parent qui gere layout des sous-panel

	CardLayout c1 = new CardLayout();

	StructureIndex liste = new StructureIndex();
	StructureInverse listeInverse = new StructureInverse();

	public InterfaceUser(StructureIndex liste, StructureInverse listeInverse) {
		this.liste = liste;
		this.listeInverse = listeInverse;

		// ----------- Partie pour le menu ------
		menuBar = new JMenuBar();

		menu1 = new JMenu("Pour utilisateur");
		menu1.add(Recherche);
		menu2 = new JMenu("Pour administrateur");
		menu2.add(Admin);

		menuBar.add(menu1);
		menuBar.add(menu2);
		menuBar.setSize(500, 300);
		add(menuBar, BorderLayout.PAGE_START);

		// ----------- Partie layout ------------
		panelTable.setLayout(c1);
		panelResultat.setLayout(null);
		panelTable.add(panelDefaut, "1");
		panelTable.add(panelResultat, "2");

		panelTable.setBounds(70, 100, 1500, 1000);
		add(panelTable, BorderLayout.CENTER);

		// Action pour passer a la page Administration
		Admin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				InterfaceAdmin interfaceAdmin = new InterfaceAdmin(liste, listeInverse);
				dispose();
			}
		});

		// ------- Partie recherche --------
		labelRecherche = new JLabel("Requete :");
		labelRecherche.setBounds(10, 10, 300, 300);

		champRecherche = new JTextField(40);
		champRecherche.setBounds(10, 30, 300, 300);

		buttonRecherche = new JButton("Recherche");
		buttonRecherche.setBounds(10, 50, 80, 25);

		panelRecherche.add(labelRecherche);
		panelRecherche.add(champRecherche);
		panelRecherche.add(buttonRecherche);

		panelRecherche.setBounds(10, 0, 700, 60);
		add(panelRecherche, BorderLayout.CENTER);

		// ---Partie reponse : tableau pour la reponse finale + champtext pour le
		// contenu du doc
		labelResultat = new JLabel(""); // affiche nombre de resultat
		labelResultat.setBounds(0, -90, 300, 300);
		panelResultat.add(labelResultat);

		labelContenu = new JLabel("Contenu du doc :"); // affiche nombre de resultat
		labelContenu.setBounds(510, -90, 300, 300);
		panelResultat.add(labelContenu);

		champContenu = new JTextArea(""); // affiche contenu du doc
		champContenu.setEditable(false);
		champContenu.setLineWrap(true);
		champContenu.setWrapStyleWord(true);
		champContenu.setBounds(510, 80, 500, 500);
		panelResultat.add(champContenu);

		String[] enteteReponseFinale = { "Index du document", "Score", "contenu" };

		DefaultTableModel tableModelReponse = new DefaultTableModel(enteteReponseFinale, 0);

		JTable reponseFinale = new JTable(tableModelReponse);
		JScrollPane sp = new JScrollPane(reponseFinale);
		sp.setBounds(0, 80, 500, 500);
		reponseFinale.getColumnModel().getColumn(2).setMinWidth(0);
		reponseFinale.getColumnModel().getColumn(2).setMaxWidth(0);
		panelResultat.add(sp);

		// Action pour le button recherche
		buttonRecherche.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				champContenu.setText(""); // initiale champResultat pour le contenu
				tableModelReponse.setRowCount(0);// initialise tableau de reponse finale

				String text = champRecherche.getText(); // mot a recherche
				String[][] reponseFinal = null; // tableau de la reponse finale

				if (!text.equals(""))
					reponseFinal = fonctionRecherche(text);

				// ajout les donnees dans tableau affichage
				if (reponseFinal != null) {

					reponseFinal = mergeSort(reponseFinal); // trier la reponse

					labelResultat.setText("Il y a " + reponseFinal.length + " resultats !");

					for (int i = 0; i < reponseFinal.length; i++) {// ajout la reponse dans tableau
						tableModelReponse.addRow(reponseFinal[i]);
					}

				} else { // si la reponse final est null

					labelResultat.setText("Il y a " + 0 + " resultats !");
				}

				c1.show(panelTable, "2");
			}
		});

		// selectionner 1 ligne du tableau reponse pour afficher la contenu
		reponseFinale.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {

				String text = tableModelReponse.getValueAt(reponseFinale.getSelectedRow(), 2).toString();

				champContenu.setText(text);

			}
		});

		// ------

		pack();
		setTitle("Page Utilisateur");
		setSize(1300, 1000);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // ferme fenetre si clique x
		setVisible(true);
	}

	// fonction recherche la reponse finale
	public String[][] fonctionRecherche(String text) {

		String[] texte = traitementRequete(text); // table contient des mots de recherche

		String[][] reponseFinal = null; // reponse finale
		String[][] reponseTemp1 = null; // pour 1er mot

		// boucle de recherche chaque mot dans texte
		for (int k = 0; k < texte.length; k++) {

			if (k == 0) { // premier mot

				reponseFinal = rechercheDoc(texte[k]);

				// cree une copie pour prochain etape
				if (reponseFinal != null) {

					reponseTemp1 = copierArray(reponseFinal);

				} else
					return null; // aucun doc trouve ! -> mot n'est pas dans la structure

			} else {// les prochains mots

				String[][] reponseTemp2 = null; // liste reponse pour les autres mots

				reponseTemp2 = rechercheDoc(texte[k]);

				if (reponseTemp2 != null) {

					// compare les reponses
					outloop: for (int a = 0; a < reponseTemp1.length; a++) { // liste reponse deja constitue

						inloop: for (int b = 0; b < reponseTemp2.length; b++) { // liste reponse avec le mot courant

							if (reponseTemp1[a][0].equals(reponseTemp2[b][0])) { // accumule les frequences

								int frequence = Integer.parseInt(reponseTemp1[a][1])
										+ Integer.parseInt(reponseTemp2[b][1]);

								reponseFinal = updateFrequence(reponseFinal, reponseTemp1[a][0], frequence);

								continue outloop;

							} else {// continue la comparaison

								continue inloop;
							}

						}

						// si le doc est seulement dans reponseTemp1 --> enleve de la liste reponse
						// finale
						reponseFinal = enleveElementArray(reponseFinal, reponseTemp1[a]);
					}

					if (reponseFinal.length == 0) {
						// verifie si la liste de reponse est vide -> retourne reponse null
						return null;

					} else

						// reinitialise temp1 pour prochain mot
						reponseTemp1 = copierArray(reponseFinal);

				} else
					return null; // si prochain mot n'est pas dans aucun document
			}
		}

		return reponseFinal;

	}

	// pour traiter la requete et enleve les mots repetes
	public String[] traitementRequete(String texte) {

		String text = texte.replaceAll("\\p{Punct}", " ");
		//String text = texte.replaceAll("[^A-z0-9]", " ");
		String[] requete = text.split("[ \n]+");
		ArrayList<String> requeteTraite = new ArrayList<String>();

		// pour convertir les majuscule en minuscule
		for (int i = 0; i < requete.length; i++) {

			if (requete[i].length() > 0) {
				char premierLettre = requete[i].charAt(0);

				if (Character.isUpperCase(premierLettre)) { // convertir en minuscule

					requete[i] = requete[i].toLowerCase();
				}
			}
		}

		// filtrer les mots repetes et mot vide
		for (String mot : requete) {
			
			if (requeteTraite.size() == 0 && mot.length() > 0) {
				requeteTraite.add(mot);
			} else if (!requeteTraite.contains(mot) && mot.length() > 0) {
				requeteTraite.add(mot);
			}
		}

		// pour convertir String arraylist to String array
		String[] requeteFinal = new String[requeteTraite.size()];
		for (int k = 0; k < requeteTraite.size(); k++) {
			requeteFinal[k] = requeteTraite.get(k);
		}

		return requeteFinal;
	}

	// fonction pour copier array de array
	public String[][] copierArray(String[][] array) {
		String[][] reponseTemp1 = new String[array.length][2];

		for (int i = 0; i < array.length; i++) {
			reponseTemp1[i][0] = array[i][0];
			reponseTemp1[i][1] = array[i][1];
		}

		return reponseTemp1;

	}

	// fonction pour recherche les doc contient le mot
	public String[][] rechercheDoc(String mot) {
		String[][] reponse = null;

		NoeudMot noeudMotActuel = listeInverse.noeudMotPremier;

		while (noeudMotActuel != null) {

			if (noeudMotActuel.getMot().equalsIgnoreCase(mot)) {

				NoeudDoc noeudDocActuel = noeudMotActuel.NoeudDocPremier;
				reponse = new String[noeudMotActuel.longueur()][];

				int i = 0;

				while (noeudDocActuel != null) {

					reponse[i] = noeudDocActuel.getListeDoc();
					i++;
					noeudDocActuel = noeudDocActuel.getProchain();
				}
				return reponse;

			}
			noeudMotActuel = noeudMotActuel.getProchain(); // continue recheche
		}

		return reponse; // si aucun mot est trouve -> reponse vide
	}

	// fonction update frequence pour reponse finale -- icrementer score
	public String[][] updateFrequence(String[][] array, String doc, int frequence) {

		if (array.length > 0) {

			for (int i = 0; i < array.length; i++) {
				if (array[i][0].equals(doc)) {
					array[i][1] = "" + frequence; // icrement score
					break;
				}
			}

			return array;
		}
		return null;
	}

	// fonction enleve un doc qui ne contient pas le mot
	// Parametre : array Reponse finale, doc "element" a enlever
	public String[][] enleveElementArray(String[][] array, String[] element) {

		String[][] liste = array; // liste reponse avant enlever
		ArrayList<String[]> reponse = new ArrayList<String[]>(); // arraylist pour creer nouveau liste reponse finale
		String[][] reponseFinal = null; // liste reponse finale apres enleve element

		if (liste.length > 0) {
			reponseFinal = new String[liste.length - 1][];

			for (int i = 0; i < liste.length; i++) {
				if (liste[i][0] != element[0]) { // ajouter tous les doc saut le doc "element"
					String[] doc = liste[i];
					reponse.add(doc);
				}
			}

			// pour convertir String arraylist to String array
			for (int k = 0; k < reponse.size(); k++) {
				reponseFinal[k] = reponse.get(k);
			}
			return reponseFinal;
		}
		return reponseFinal;
	}

	// fonction trier : methode MergeSort
	public String[][] mergeSort(String[][] array) {
		String[][] array1, array2;

		if (array.length <= 1)
			return array; // condition arret

		// separe en partie
		String[][] first = new String[array.length / 2][];
		String[][] second = new String[array.length - first.length][];

		System.arraycopy(array, 0, first, 0, first.length); // premier partie
		System.arraycopy(array, first.length, second, 0, second.length); // 2e partie

		// trier
		array1 = mergeSort(first);
		array2 = mergeSort(second);

		return merge(array1, array2);

	}

	// fonction merge
	public String[][] merge(String[][] array1, String[][] array2) {
		String[][] result = new String[array1.length + array2.length][];
		int i = 0, j = 0, k = 0;

		// copier à chaque boucle celui qui est plus grand score
		while (i < array1.length && j < array2.length) {

			int frequenceArray1 = Integer.parseInt(array1[i][1]);
			int frequenceArray2 = Integer.parseInt(array2[j][1]);

			if (frequenceArray1 > frequenceArray2) { // inserer par ordre decroissant
				result[k] = array1[i];
				i++;
			} else {
				result[k] = array2[j];
				j++;
			}
			k++;
		}
		// copier ce qui reste dans array1 ou array2
		if (i < array1.length)
			System.arraycopy(array1, i, result, k, array1.length - i); // le array2 est epuise
		if (j < array2.length)
			System.arraycopy(array2, j, result, k, array2.length - j); // le array1 est epuise
		return result;

	}

}
