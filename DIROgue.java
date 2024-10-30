package soumission;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

import code_squelette.*;
import rencontres.*;

public class DIROgue {

	public static void main(String[] args) {

		Scanner scan = new Scanner(System.in);	// Ouvre un scanner pour lire l'entre


		boolean corridor = false; // Si CORRIDORS à été activé
		
		MonLabyrinthe labyrinthe = new MonLabyrinthe(); // Crée un labyrinthe
		Piece[] piece = new Piece[50];  // Crée un tableau de pièces;
		Exterieur exterieur = Exterieur.getExterieur();
		piece[0] = exterieur; // nouveau exterieur dans la piece 0
		int nombreDePieces = 1; // compteur de piece
		String ligne = scan.nextLine(); // Lit la première ligne
		while (ligne != "FIN") { // Si la ligne est fin, arrête la boucle

			String[] mots = ligne.split(" "); //sépare les mots et fait un tableau

			switch (mots[0]) {
				case "piece":	// Si le premier mot est piece

					if (corridor) { //si le corridor est activé
						System.out.println("mettre les corridor");
						break;
					}

					if (piece[Integer.parseInt(mots[1])] != null) { //si la pièce existe déjà
						System.out.println("piece deja existante");
						break;
					}

					piece[Integer.parseInt(mots[1])] = new Piece(Integer.parseInt(mots[1]), RencontreType.valueOf(mots[2].toUpperCase())); //crée une pièce
					nombreDePieces++;


					break;

				case "corridor": // Si le premier mot est corridor
					if (!corridor) {
						System.out.println("ajouter CORRIDORS avant");
						break;

					}
					if (piece[Integer.parseInt(mots[1])] == null || piece[Integer.parseInt(mots[2])] == null) {	//si la piece existe
						System.out.println("piece inexistant");
						break;
					}

					if (Integer.parseInt(mots[1]) == Integer.parseInt(mots[2])) { //si les deux pieces sont les mêes
						System.out.println("meme piece");
						break;
					}

					if (Integer.parseInt(mots[1]) == 0) { //si une des pièces est exterieur
						labyrinthe.ajouteEntree(exterieur, piece[Integer.parseInt(mots[2])]); // Ajoute une entrée
						break;
					}
					if (Integer.parseInt(mots[2]) == 0) { //si une des pièces est exterieur
						labyrinthe.ajouteEntree(exterieur, piece[Integer.parseInt(mots[1])]); // Ajoute une entrée
						break;
					}
					
					labyrinthe.ajouteCorridor(piece[Integer.parseInt(mots[1])], piece[Integer.parseInt(mots[2])]); // Ajoute un corridor entre deux pièces
					break;

				case  "CORRIDORS": // Si le premier mot est CORRIDORS
					boolean corridorCorrect = true;
					for (int i = 0; i < nombreDePieces; i++){
						if (piece[i] == null) {
							System.out.println("piece manquante"); //pour ne pas sauter de piece
							corridorCorrect = false;
							break;
						}
					}

					if (corridorCorrect) {
						// Active la commande corridor qui permet d'ajouter des corridors
						corridor = true; // active corridor

						break;

					}

			}
			ligne = scan.nextLine(); // Lit la prochaine ligne


			
		}
		scan.close(); // Ferme le scanner






		MonAventure aventure = new MonAventure(labyrinthe); // Crée une aventure avec le labyrinth
		genererRapport(aventure); // Génère un rapport
		genererScenario(aventure); // Génère un scénario

	}

	/*
	 * Cette methode n'est pas necessaire pour le TP, c'est ici juste pour vous
	 * demontrer comment utiliser la classe Exterieur.
	 */
	private static void expliquerQuelquesChoses() {
		// ceci est la seule facon de creer une instance de la classe Exterieur!
		Exterieur lExterieur = Exterieur.getExterieur();
		// l'exterieur contient le type de rencontre RencontreType.RIEN
		System.out.println(lExterieur.getRencontreType() == RencontreType.RIEN);
	}




	public static String genererRapport(Aventure a) {
		System.out.println("Rapport:");
		Labyrinthe laby = a.getLabyrinthe(); //recupere le labyrinthe
		Piece[] pieces = laby.getPieces(); //recupere les pieces


		System.out.println("Donjon avec " + laby.nombreDePieces() + " pieces"); //affiche le nombre de pieces
		for (int i = 0; i < laby.nombreDePieces(); i++) {
			if (pieces[i].getRencontreType() == RencontreType.BOSS) {
				break;
			}
			Piece[] piecesConnecte = laby.getPiecesConnectees(pieces[i]); //recupere les pieces connecté
			int nbrPiecesNonNull = 0;
			for (int j = 0; j < 9; j++) { //compte le nombre de pieces non null
				if (piecesConnecte[j] == null) {
					break;
				}
				nbrPiecesNonNull++;
			}
			Piece[] piecesNonNull = new Piece[nbrPiecesNonNull]; //crée un tableau de pieces non null

			for (int k = 0; k < nbrPiecesNonNull; k++) {
				piecesNonNull[k] = piecesConnecte[k];
			}

			System.out.println(pieces[i] + ":" + Arrays.toString(piecesNonNull)); //affiche les pieces connecté
		}

		System.out.println("//le reste du donjon....");

		if (a.estPacifique() == false) { //si le donjon est pacifique
			System.out.println("Non pacifique");
		}else{
			System.out.println("Pacifique");
		}
		if (a.contientBoss() == true) { //si le donjon contient un boss
			System.out.println("Contient un bos");
		}else{
			System.out.println("aucun boss");
		}

		System.out.println("contient " + a.getTresorTotal() + " tresors"); //nombre de trésors


		Piece[] cheminBoss = a.cheminJusquAuBoss(); //chemin jusqu'au boss
		if (cheminBoss != null) {
			for (int i = 0; i < cheminBoss.length; i++) {
				System.out.println(cheminBoss[i]); //affiche le chemin
			}
		}
		return null;
	}

	public static String genererScenario(Aventure a) {

		Piece[] cheminBoss = a.cheminJusquAuBoss();
		if (cheminBoss != null) {
			System.out.println("Scenario:");

			for (int i = 0; i < cheminBoss.length; i++) { //pour chaque piece dans le chemin
				if (cheminBoss[i].getRencontreType() == RencontreType.BOSS) { //si la piece est un boss
					System.out.println("La battaille final!");
				}
				if (cheminBoss[i].getRencontreType() == RencontreType.RIEN) { //si la piece est vide
					System.out.println("Un moment pacifique");
				}
				if (cheminBoss[i].getRencontreType() == RencontreType.MONSTRE) { //si la piece est un monstre
					Monstre x = null;
					int r = new Random().nextInt(3);
					switch (r) { //choisi un monstre aléatoire
						case 0:
							x = new Gobelin();
							break;
						case 1:
							x = new Gargouille();
							break;
						case 2:
							x = new Orque();
							break;
					}
					System.out.println("Un " + x.rencontrer() + " affreux!"); //affiche le monstre
				}
				if (cheminBoss[i].getRencontreType() == RencontreType.TRESOR) { //si la piece a un trésor
					Tresor x = null;
					int r = new Random().nextInt(3);
					switch (r) { //choisi un monstre aléatoire
						case 0:
							x = new SacDeButin();
							break;
						case 1:
							x = new ArtefactMagique();
							break;
						case 2:
							x = new Potion();
							break;
					}
					System.out.println("Un " + x.rencontrer() + " affreux!"); //affiche le trésor
				}
			}
		}

		return null;
	}

}

/*
  piece 1 monstre
  piece 2 rien
  piece 3 tresor
  piece 4 monstre
  piece 5 boss
  piece 6 tresor
  piece 7 monstre
  piece 8 rien
  CORRIDORS
  corridor 1 0
  corridor 1 2
  corridor 3 2
  corridor 3 4
  corridor 4 5
  corridor 0 5
  corridor 0 6
  corridor 6 7
  corridor 7 8
  corridor 4 8
  FIN
 */


