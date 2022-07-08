//Mouad Belbey et Van Nam Vu 

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.ArrayList;
import java.io.File;

import javax.swing.filechooser.FileFilter;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class InterfaceAdmin extends JFrame {

	JMenuBar menuBar;
	JMenu menu1;
	JMenu menu2;
	JButton buttonParcourir;

	JPanel panelStructure = new JPanel(); // panel pour afficher les structures
	JPanel panelTable = new JPanel(); // panel parent qui gere layout des sous-panel
	CardLayout c1 = new CardLayout();

	private JMenuItem Recherche = new JMenuItem("Recherche");
	private JMenuItem Admin = new JMenuItem("Administration");

	StructureIndex list = new StructureIndex();
	StructureInverse listInverse = new StructureInverse();

	public InterfaceAdmin(StructureIndex liste, StructureInverse listeInverse) {
		this.list = liste;
		this.listInverse = listeInverse;

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

		// --- pour passer a la page utilisateur
		Recherche.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				InterfaceUser interfaceUser = new InterfaceUser(list, listInverse);
				dispose();
			}
		});

		// ----------- Partie layout ------------
		panelTable.setLayout(c1);
		panelStructure.setLayout(null);
		panelTable.add(panelStructure, "1");

		panelTable.setBounds(70, 100, 1000, 800);
		add(panelTable, BorderLayout.CENTER);

		// ----------File Chooser - et affichage les structures-----
		JLabel labelStructureIndex = new JLabel("Structure d'indexation :");
		labelStructureIndex.setBounds(10, 50, 170, 10);
		panelStructure.add(labelStructureIndex);

		JTextArea champResultatIndex = new JTextArea(list.affichage());
		champResultatIndex.setEditable(false);
		JScrollPane sp = new JScrollPane(champResultatIndex);
		sp.setBounds(10, 80, 500, 500);
		panelStructure.add(sp);

		JLabel labelStructureInverse = new JLabel("Structure inverse :");
		labelStructureInverse.setBounds(600, 50, 170, 10);
		panelStructure.add(labelStructureInverse);

		JTextArea champResultatInverse = new JTextArea(listInverse.affichage());
		JScrollPane sp1 = new JScrollPane(champResultatInverse);
		champResultatInverse.setEditable(false);
		sp1.setBounds(600, 80, 500, 500);
		panelStructure.add(sp1);

		JPanel panelRecherche = new JPanel();
		buttonParcourir = new JButton("Parcourir");
		buttonParcourir.setBounds(10, 50, 80, 25);
		panelRecherche.add(buttonParcourir);
		panelRecherche.setBounds(10, 0, 700, 60);
		add(panelRecherche, BorderLayout.CENTER);

		buttonParcourir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				JFileChooser chooser = new JFileChooser();
				FileFilter typeTexte = new ExtensionFilter("Format texte", ".txt");

				chooser.addChoosableFileFilter(typeTexte); //ajoute option extension
				chooser.setFileFilter(typeTexte); //set extension par defaut
				chooser.setAcceptAllFileFilterUsed(false); //desactiver option "All type file"
				chooser.setMultiSelectionEnabled(true); //permet de choisir plusieurs fichier en meme temps

				if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					File[] selectedFile = chooser.getSelectedFiles();

					for (int i = 0; i < selectedFile.length; i++) {
						System.out.println("Nom du fichier : " + selectedFile[i].getName() + "\n");
						System.out.println("Chemin absolu : " + selectedFile[i].getAbsolutePath() + "\n");

						if (list.estVide())
							list.ajoutPremierDoc(selectedFile[i].getAbsolutePath());
						else
							list.ajoutNoeudDoc(selectedFile[i].getAbsolutePath());
					}

					StructureInverse listeInverse1 = new StructureInverse();
					listeInverse1.Inverse(list);
					listInverse = listeInverse1;

					String structureIndex = list.affichage(); //String structure indexation
					String structureInverse = listInverse.affichage(); //String structure inverse

					champResultatIndex.setText(structureIndex);
					champResultatInverse.setText(structureInverse);

					c1.show(panelTable, "1");

				}
			}
		});

		pack();
		setTitle("Page Adminstration");
		setSize(1300, 1000);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // ferme fenetre si clique x
		setVisible(true);

	}
}
