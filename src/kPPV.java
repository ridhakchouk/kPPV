import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

/**
 * @author ridha.kchouk
 * 
 */
public class kPPV {
	
	/**
	 * Variables générales pour le traitement des données Iris (référentiel UCI)
	 * NbEx : nombre de données par classe dans l'ensemble de données NbClasses :
	 * Nombre de classes à reconnaître NbFeatures : Dimensionnalité de l'ensemble de
	 * données NbExLearning : Nombre d'exemples par classe utilisés pour
	 * l'apprentissage (il y a les les premiers dans le stockage des données pour
	 * chaque classe)
	 * 
	 */
	
	static int NbEx = 50, NbClasses = 3, NbFeatures = 4, NbExLearning = 25;
	static Double data[][][] = new Double[NbClasses][NbEx][NbFeatures];// there are 50*3 exemples at all. All have 4
																		// features

	public static void main(String[] args) {
		System.out.println("Starting kPPV");
		ReadFile();
		
		
		/**
		 * 
		 * X est un exemple à classer (à prendre en données - exemples de tests-) Double
		 * X[] = nouveau Double [NbFeatures] ; distances : tableau permettant de stocker
		 * toutes les distances entre l'exemple donné X et toutes exemples dans la série
		 * d'apprentissage, en utilisant ComputeDistances
		 */
		
		Double distances[] = new Double[NbClasses * NbExLearning];
		Double X[] = { 4.8,3.4,1.9,0.2 };

		System.out.println("On calcul la distance par rapport aux données suivantes : { " + X[0] + "; " + X[1] + "; "
				+ X[2] + "; " + X[3] + " }");
		System.out.println("Calcul des distances...");
		distances = ComputeDistances(X, distances);
		String rep = "";
		Scanner sc = new Scanner(System.in);
		System.out.println("Voir les distances ? (Oui/Non)");
		while (!rep.equals("Oui") || !rep.equals("Non")) {
			rep = sc.next();

			if (rep.equals("Oui")) {
				System.out.println("Liste des distances :");
				for (int i = 0; i < distances.length; i++) {
					System.out.println("Distance n° " + (i + 1) + " = " + distances[i]);
				}
				break;
			} else if (rep.equals("Non")) {
				break;
			}

		}

		confusion(data, distances);
		
		System.out.println("Entrée le paramètre K pour les K plus proches voisins : ");
		int k = sc.nextInt();
		kppv(X, distances, k);
		sc.close();

		

	}

	/**
	 * Question 4
	 * 
	 * @param data[][][]  tableau à trois dimension contenant respectivement
	 *                    [nombre_de_classe][nombre_element_par_classe][nombre_de_caracteristique]
	 * 
	 * @param distances[] tableau contenant des distances de type Double
	 * 
	 * @return retourne une matrice de confusion
	 * 
	 */
	public static int[][] confusion(Double data[][][], Double distances[]) {
		int conf_mat[][] = new int[3][3];
		Double x[] = new Double[4];

		for (int i = 0; i < NbClasses; i++) {
			for (int j = NbExLearning; j < NbEx; j++) {
				for (int k = 0; k < NbFeatures; k++) {
					x[k] = data[i][j][k];
				}
				int classe = one_ppv(x, distances);
				// System.out.println(i);
				conf_mat[i][classe] += 1;
			}
		}

		Double somme = 0.0;
		int item1 = 0;
		int item2 = 0;

		System.out.println("Confusion : ");
		System.out.println();
		System.out.println("\t0\t1\t2");
		System.out.print("\t-------------------");
		for (int i = 0; i < conf_mat.length; i++) {
			System.out.println();
			System.out.print(i + " |");
			for (int j = 0; j < conf_mat[i].length; j++) {
				System.out.print("\t" + conf_mat[i][j] + " ");

				item1 += conf_mat[i][j];

				if (i == j) {

					item2 += conf_mat[i][j];
				}
			}
		}

		somme = (double) (item2 * 100.0) / (item1);
		System.out.println();
		System.out.println();
		System.out.println("Rate : " + somme + "%");
		System.out.println("Error : " + (100.0 - somme) + "%");
		return conf_mat;
	}

	/**
	 * Question 5 ..
	 * 
	 * @param x[] 			tableau contenant 4 éléments de type Double. il s'agit des
	 *           			coordonnées.
	 *
	 * @param distances[] 	Tableau contenant plusieurs éléments de type Double. il
	 *                 	  	s'agit d'une liste de distance par rapport au paramètre
	 *                  	x[].
	 *
	 *
	 * @param k  			permet de déterminer combien de voisin la fonction doit retourner
	 * 
	 * 
	 * @return retourne la classe majoritaire (prédite)
	 */

	public static int kppv(Double x[], Double distances[], int k) {

		Arrays.sort(distances);
		Double k_dist_proche[] = new Double[k];
		System.out.println("Les " + k + " voisins les plus proches sont à une distance de :");
		for (int i = 0; i < k; i++) {
			k_dist_proche[i] = distances[i];
			System.out.println("Voisin plus proche n°"+(i+1)+" :"+distances[i]);
		}
		
		
		System.out.println("Classe la plus fréquente : "+one_ppv_v2(x, k_dist_proche));
		return one_ppv_v2(x, k_dist_proche);

	}

	/**
	 * Question 2 :
	 * 
	 * @param x[]         tableau contenant 4 éléments de type Double. il s'agit des
	 *                    coordonnées.
	 *
	 * @param distances[] initialement tableau vide de Type double qui sera rempli
	 *                    au fur et à mesure en fonction du paramètre x[].
	 *                    Contiendra une liste de distance entre le param x[] et les
	 *                    autres points
	 * 
	 * @return retourne une liste de distance
	 **/
	public static Double[] ComputeDistances(Double x[], Double distances[]) {
		int t = 0;
		for (int i = 0; i < NbClasses; i++) {

			for (int j = 0; j < NbExLearning; j++) {

				// calcul distance euclidienne
				distances[t] = Math.sqrt(Math.pow(x[0] - data[i][j][0], 2) + Math.pow(x[1] - data[i][j][1], 2)
						+ Math.pow(x[2] - data[i][j][2], 2) + Math.pow(x[3] - data[i][j][3], 2));
				t++;
			}
		}
		return distances;
	}

	/**
	 * Question 3 :
	 * 
	 * @param x[]         tableau contenant 4 éléments de type Double. il s'agit des
	 *                    coordonnées.
	 *
	 * @param distances[] Tableau contenant plusieurs éléments de type Double. il
	 *                    s'agit d'une liste de distance par rapport au paramètre
	 *                    x[].
	 *
	 * @return retourne la classe de x[].
	 *
	 **/
	public static int one_ppv(Double x[], Double distances[]) {
		Double res[] = ComputeDistances(x, distances);
		Double resultat = res[0];
		int indexClass = 99999;
		for (int i = 0; i < res.length; i++) {
			if (res[i] < resultat) {
				resultat = res[i];
				indexClass = i;

			}
		}
		if (indexClass < NbExLearning) {
			return 0;
		}
		if (indexClass > NbExLearning - 1 && indexClass < NbExLearning * 2) {
			return 1;
		}
		if (indexClass > (NbExLearning * 2) - 1) {
			return 2;
		}

		// Retourne -1 en cas d'erreur
		return -1;

	}
	
	public static int one_ppv_v2(Double x[], Double distances[]) {
		Double res[] =distances;
		Double resultat = res[0];
		int indexClass = 99999;
		for (int i = 0; i < res.length; i++) {
			if (res[i] < resultat) {
				resultat = res[i];
				indexClass = i;

			}
		}
		if (indexClass < NbExLearning) {
			return 0;
		}
		if (indexClass > NbExLearning - 1 && indexClass < NbExLearning * 2) {
			return 1;
		}
		if (indexClass > (NbExLearning * 2) - 1) {
			return 2;
		}

		// Retourne -1 en cas d'erreur
		return -1;

	}
	
	
	

	/**
	 * cette fonction permet la lecture du fichier de données et remplit la variable
	 * "data" de ces données
	 * 
	 **/
	public static void ReadFile() {

		String line, subPart;
		int classe = 0, n = 0;
		try {
			BufferedReader fic = new BufferedReader(new FileReader("iris.data"));
			while ((line = fic.readLine()) != null) {
				for (int i = 0; i < NbFeatures; i++) {
					subPart = line.substring(i * NbFeatures, i * NbFeatures + 3);
					data[classe][n][i] = Double.parseDouble(subPart);
					// System.out.println(data[classe][n][i]+" "+classe+" "+n);
				}
				if (++n == NbEx) {
					n = 0;
					classe++;
				}
			}
			fic.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

}

//-------------------End of class kPPV-------------------------
