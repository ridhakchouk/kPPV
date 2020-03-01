import java.io.*;
import java.util.Arrays;

/**
 * @author hubert.cardot
 */
public class kPPV {
	// General variables for dealing with Iris data (UCI repository)
	// NbEx: number of data per class in dataset
	// NbClasses: Number of classes to recognize
	// NbFeatures: Dimensionality of dataset
	// NbExLearning: Number of exemples per class used for learning (there are the
	// first ones in data storage for each class)

	static int NbEx = 50, NbClasses = 3, NbFeatures = 4, NbExLearning = 25;
	static Double data[][][] = new Double[NbClasses][NbEx][NbFeatures];// there are 50*3 exemples at all. All have 4
																		// features

	public static void main(String[] args) {
		System.out.println("Starting kPPV");
		ReadFile();

		// X is an exemple to classify (to take into data -test examples-)
		// Double X[] = new Double[NbFeatures];
		// distances: table to store all distances between the given exemple X and all
		// exemples in learning set, using ComputeDistances
		Double distances[] = new Double[NbClasses * NbExLearning];
		Double X[] = { 5.4, 3.7, 1.5, 0.2 };
		distances = ComputeDistances(X, distances);

//		System.out.println("On calcul la distance par rapport aux données suivantes : { " + X[0] + "; " + X[1] + "; "
//				+ X[2] + "; " + X[3] + " }");
//		System.out.println("Liste des distances :");
//		for (int i = 0; i < distances.length; i++) {
//			System.out.println("Distance n° " + (i + 1) + " = " + distances[i]);
//		}

//		System.out.println("classe d'appartenance : " + one_ppv(X, distances));
//		k_ppv(distances,3);
//		preparation du K-PPV

		// confusion(data, distances);
		knnconfusion(data,0);

	}

	public static void knnconfusion(Double data[][][],int knn) {
		
		Double tab[] = new Double[4];
		for (int i = 0; i < NbClasses; i++) {

			for (int j = 0; j < NbEx; j++) {

				for(int k = 0 ; k<NbFeatures;k++) {
//					System.out.print(data[i][j][k]+"\t");
					tab[k]=data[i][j][k];
				}
				
				System.out.println();
			}
			System.out.println();

		}
	}

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

	// This function returnes a list of K NN where k is ask to be entered by user
	// question 5
	public static Double[] k_ppv(Double[] tab, int k) {

		Double[] res = new Double[k];
		Arrays.sort(tab);

		System.out.println("Distances des K voisins les plus proches : ");
		for (int i = 0; i < k; i++) {
			res[i] = tab[i];
			System.out.println("Voisin n°" + (i + 1) + " " + res[i]);
		}

		return res;
	}

	// This method return a list of distance according to an array which is
	// containing coordinates
	private static Double[] ComputeDistances(Double x[], Double distances[]) {
		// ---compute the distance between an input data x to test and all examples in
		// training set (in data)
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

	// This method return the class of X
	private static int one_ppv(Double x[], Double distances[]) {
		int indexClass = 0;
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

		return -1;

	}

	// â€”â€”-Reading data from iris.data file
	// 1 line -> 1 exemple
	// 50 first lines are 50 exemples of class 0, next 50 of class 1 and 50 of class
	// 2
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
