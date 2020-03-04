import java.io.*;
import java.util.Arrays;

/**
 *@author ridha.kchouk
 * 
 */
public class kPPV {
	/**
	 *  Variables g�n�rales pour le traitement des donn�es Iris (r�f�rentiel UCI)
	 * NbEx : nombre de donn�es par classe dans l'ensemble de donn�es
	 * NbClasses : Nombre de classes � reconna�tre
	 * NbFeatures : Dimensionnalit� de l'ensemble de donn�es
	 * NbExLearning : Nombre d'exemples par classe utilis�s pour l'apprentissage (il y a les
	 * les premiers dans le stockage des donn�es pour chaque classe)
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
		* X est un exemple � classer (� prendre en donn�es - exemples de tests-)
		* Double X[] = nouveau Double [NbFeatures] ;
		* distances : tableau permettant de stocker toutes les distances entre l'exemple donn� X et toutes
		* exemples dans la s�rie d'apprentissage, en utilisant ComputeDistances
		*/
		Double distances[] = new Double[NbClasses * NbExLearning];
		Double X[] = { 5.4, 3.7, 1.5, 0.2 };
		distances = ComputeDistances(X, distances);

		System.out.println("On calcul la distance par rapport aux donn�es suivantes : { " + X[0] + "; " + X[1] + "; "
				+ X[2] + "; " + X[3] + " }");
		System.out.println("Liste des distances :");
		for (int i = 0; i < distances.length; i++) {
			System.out.println("Distance n� " + (i + 1) + " = " + distances[i]);
		}

		System.out.println("classe d'appartenance : " + one_ppv(X, distances));
		k_ppv(distances,3);

		confusion(data, distances);
		//knnconfusion(data,0);

	}

	/**
	 * 
	 * @implNote en cours de dev
	 * 
	 * */
	public static void knnconfusion(Double data[][][],int knn) {
		
		Double tab[] = new Double[4];
		for (int i = 0; i < NbClasses; i++) {

			for (int j = 0; j < NbEx; j++) {

				for(int k = 0 ; k<NbFeatures;k++) {
//					System.out.print(data[i][j][k]+"\t");
					tab[k]=data[i][j][k];
				}
				
			}


		}
	}

	
	/**
	 * Question 4
	 * 
	 * @param data[][][]
	 * 		tableau � trois dimension contenant respectivement [nombre_de_classe][nombre_element_par_classe][nombre_de_caracteristique]
	 * 
	 * @param distances[]
	 * 		tableau contenant des distances de type Double
	 * 
	 * @return retourne une matrice de confusion
	 * 
	 * */
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

		System.out.println("Confusion KPPV : ");
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
	 *@param x[]
	 *		tableau contenant 4 �l�ments de type Double.
	 *		il s'agit des coordonn�es.
	 *
	 *@param k
	 *		permet de d�terminer combien de voisin la fonction doit
	 *		retourner
	 * 
	 * @return retourne une liste de distance des k voisins plus proche
	 */
	
	public static Double[] k_ppv(Double x[], int k) {

		Double[] res = new Double[k];
		Arrays.sort(x);

		System.out.println("Distances des K voisins les plus proches : ");
		for (int i = 0; i < k; i++) {
			res[i] = x[i];
			System.out.println("Voisin n�" + (i + 1) + " " + res[i]);
		}

		return res;
	}

	
	/**
	 * Question 2 :
	 *@param x[]
	 *		tableau contenant 4 �l�ments de type Double.
	 *		il s'agit des coordonn�es.
	 *
	 *@param distances[]
	 *		initialement tableau vide de Type double qui sera rempli
	 *		au fur et � mesure en fonction du param�tre x[].
	 *		Contiendra une liste de distance entre le param x[] et les autres points
	 *		
	 *@return retourne une liste de distance
	 */
	private static Double[] ComputeDistances(Double x[], Double distances[]) {
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
	 *@param x[]
	 *		tableau contenant 4 �l�ments de type Double.
	 *		il s'agit des coordonn�es.
	 *
	 *@param distances[]
	 *		Tableau contenant plusieurs �l�ments de type Double.
	 *		il s'agit d'une liste de distance par rapport au param�tre x[].
	 *
	 *@return retourne la classe de x[].
	 *
	 **/
	private static int one_ppv(Double x[], Double distances[]) {
		int indexClass = -1;
		Double res[] = ComputeDistances(x, distances);
		Double resultat = res[0];
		for (int i = 0; i < res.length; i++) {
			if (res[i] < resultat) {
				resultat = res[i];
				indexClass = i;
			}
		}
		if (indexClass < 25) {
			return 0;
		}
		if (indexClass > 24 && indexClass < 50) {
			return 1;
		}
		if (indexClass > 49) {
			return 2;
		}

		// Retourne -1 en cas d'erreur
		return -1;

	}

	
	/**
	 *cette fonction permet la lecture du fichier de donn�es
	 *et remplit la variable "data" de ces donn�es
	 * 
	 **/
	private static void ReadFile() {

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
