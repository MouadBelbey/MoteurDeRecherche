
public class Main {

	public static void main(String[] args) {

		StructureIndex liste = new StructureIndex();
		StructureInverse listeInverse = new StructureInverse();

		liste.ajoutPremierDoc("text/1.txt");
		listeInverse.Inverse(liste);
		
		InterfaceAdmin interfaceAdmin = new InterfaceAdmin(liste,listeInverse);
		
	}

}
