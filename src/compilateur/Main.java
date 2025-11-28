/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package compilateur;

import static compilateur.Compilateur.classify;
import static compilateur.Compilateur.col;
import static compilateur.Compilateur.inOperators;
import static compilateur.Compilateur.inSeparators;
import static compilateur.Compilateur.mat;
import static compilateur.Compilateur.skipComment;
import java.util.Scanner;

/**
 *
 * @author InfoPro
 */
public class Main {
  // ------------------------------------------------------------
    // PROGRAMME PRINCIPAL
    // ------------------------------------------------------------
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        String code = "";

        System.out.println("Écris ton programme Java :");
        System.out.println("(Ligne vide = fin)\n");

        while (true) {
            String ligne = sc.nextLine();

            if (ligne.equals("")) break;

            code += ligne + "\n";
        }

        int i = 0, n = code.length();
        int etat = 0;
        String mot = "";

        while (i < n) {

            i = skipComment(code, i);
            if (i >= n) break;

            char c = code.charAt(i);
            int colonne = col(c);
            int suivant = mat[etat][colonne];

            // -------------------------------
            // CAS : opérateur simple
            // -------------------------------
            if (inOperators(c)) {
                classify(mot);
                mot = "";
                System.out.println("OPERATOR    : " + c);
                etat = 0;
               i++;
         
continue;
            }

            // -------------------------------
            // CAS : séparateur
            // -------------------------------
            if (inSeparators(c)) {
                classify(mot);
                mot = "";
                System.out.println("SEPARATOR   : " + c);
                etat = 0;
                i++;
         
continue;
            }

            // -------------------------------
            // CAS : espace → fin de mot
            // -------------------------------
            if (colonne == 3) {
                classify(mot);
                mot = "";
                etat = 0;
                i++;
        
continue;
            }

            // -------------------------------
            // CAS : transition impossible
            // -------------------------------
            if (suivant == -1) {
                classify(mot);
                mot = "";
                etat = 0;
                i++;
        
continue;
            }

            // -------------------------------
            // AJOUT DU CARACTÈRE AU MOT
            // -------------------------------
            mot += c;
            etat = suivant;
            i++;

            // -------------------------------
            // FIN DU MOT ?  
            // si le prochain caractère est incompatible
            // -------------------------------
            if (i < n) {
                char next = code.charAt(i);
                int colNext = col(next);

                if (colNext == 3            // espace
                 || inOperators(next)       // opérateur
                 || inSeparators(next)) {   // séparateur

                    classify(mot);
                    mot = "";
                    etat = 0;
                }
            }
        }

        // dernier mot
        classify(mot);
    }}  

