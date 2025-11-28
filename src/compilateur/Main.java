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
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import compilateur.SyntaxParser;
/**
 *
 * @author InfoPro
 */
public class Main {

    public static void main(String[] args) {
        String code = "";

        // Lecture du fichier texte
        try {
            BufferedReader br = new BufferedReader(new FileReader("src/compilateur/programme.txt"));
            String ligne;
            while ((ligne = br.readLine()) != null) {
                code = code + ligne + "\n";
            }
            br.close();
        } catch (IOException e) {
            System.out.println("Erreur lecture fichier : " + e.getMessage());
            return;
        }

        // Ajout du sentinelle #
        code = code + "#";
boolean inString = false;
        int i = 0;
        int etat = 0;
        String mot = "";
int line = 1; // compteur de lignes
        while (true) {
            char c = code.charAt(i);
            if (c == '\n') {
        line++; // incrémenter à chaque saut de ligne
        i++;
        continue;
    }
// ✅ Vérification de fin de chaîne
            if (c == '#') {
               Compilateur.classify(mot,line); // dernier mot accumulé
                Token.addToken("EOF", "#",line); // ajout du token de fin
                break; // sortir de la boucle
            }
            if (!inString && c == '"') {
        // terminer le mot en cours s’il existe
        Compilateur.classify(mot,line);
        mot = "";
        inString = true;
        mot = "\""; // commence la chaîne
        i++;
        continue;
    }

    // lecture de chaîne jusqu’au prochain "
    if (inString) {
        mot += c;
        if (c == '"') {
            // fin de chaîne rencontrée
            Compilateur.classify(mot,line); // sera classé comme STRING
            mot = "";
            inString = false;
        }
        i++;
        continue;
    }

            i = skipComment(code, i);
            if (i >= code.length()) {
                break;
            }
            c = code.charAt(i);

            int colonne = col(c);
            int suivant = mat[etat][colonne];

           if (inOperators(c)) {
    classify(mot,line);
    mot = "";

    // Vérifier les opérateurs composés
    if (c == '=' && i + 1 < code.length() && code.charAt(i + 1) == '=') {
        Token.addToken("OPERATOR", "==",line);
        i += 2; // avancer de deux caractères
    } else if (c == '!' && i + 1 < code.length() && code.charAt(i + 1) == '=') {
        Token.addToken("OPERATOR", "!=",line);
        i += 2;
    } else if (c == '<' && i + 1 < code.length() && code.charAt(i + 1) == '=') {
        Token.addToken("OPERATOR", "<=",line);
        i += 2;
    } else if (c == '>' && i + 1 < code.length() && code.charAt(i + 1) == '=') {
        Token.addToken("OPERATOR", ">=",line);
        i += 2;
        } else if (c == '&' && i + 1 < code.length() && code.charAt(i + 1) == '&') {
        Token.addToken("OPERATOR", "&&",line); i += 2;
    } else if (c == '|' && i + 1 < code.length() && code.charAt(i + 1) == '|') {
        Token.addToken("OPERATOR", "||",line); i += 2;
    } else {
        // opérateurs simples
        Token.addToken("OPERATOR", String.valueOf(c),line);
        i++;
    }

    etat = 0;
    continue;
}


            if (inSeparators(c)) {
                classify(mot,line);
                mot = "";
                Token.addToken("SEPARATOR", String.valueOf(c),line);
                etat = 0;
                i++;
                continue;
            }

            if (colonne == 4) {
                classify(mot,line);
                mot = "";
                etat = 0;
                i++;
                continue;
            }

            if (suivant == -1) {
                classify(mot,line);
                mot = "";
                etat = 0;
                i++;
                continue;
            }

            mot = mot + c;
            etat = suivant;
            i++;
        }
    }}