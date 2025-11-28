/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package compilateur;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author InfoPro
 */

public class Compilateur {

    // -----------------------------
    // TABLEAUX des mots-clés, opérateurs, séparateurs
    // -----------------------------
    static String[] keywords = {
        "final", "String", "package", "import", "public", "static", "private", "class", "return", "int", "double", "char",
        "if", "else", "for", "switch", "case", "break", "default", "while", "do", "void", "new",
        "float", "boolean", "catch", "try", "extends", "protected", "super", "this", "System", "out", "println", "err",
        "false", "true"
    };

    static String[] customKeywords = {"noha", "messaoudi"};

    static char[] operators = {'+', '-', '*', '/', '%', '=', '<', '>','!','&','|' };
    static char[] separators = {';', '{', '}', '(', ')', '[', ']', ',','.',':'};

    // -----------------------------
    // MATRICE de transitions
    // colonnes : 0=lettre, 1=chiffre, 2=guillemet, 3=underscore, 4=espace, 5=autres
    // -----------------------------
    static int[][] mat = {
        {1, 5, 3, 1, -1, -1}, 
        {1, 1, -1, 1, 2, -1}, 
        {1, 1, -1, 1, 2, -1}, 
        {3, 3, 4, 3, -1, -1}, 
        {1, -1, -1, -1, -1, -1},
        {1, 5, -1, -1, -1, -1} 
    };

    // -----------------------------
    // Fonction col() 
    // -----------------------------
    static int col(char c) {
        if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
            return 0; // lettre
        }
        if (c >= '0' && c <= '9') {
            return 1; // chiffre
        }
        if (c == '"') {
            return 2; // guillemet
        }
        if (c == '_') {
            return 3; // underscore
        }
        if (c == ' ' || c == '\t' || c == '\n') {
            return 4; // espace
        }
        return 5; // autres
    }

    // -----------------------------
    // Vérification appartenance
    // -----------------------------
    static boolean inOperators(char c) {
        for (char op : operators) {
            if (op==c ) {
                return true;
            }
        }
        return false;
    }

    static boolean inSeparators(char c) {
        for (char s : separators) {
            if (s == c) {
                return true;
            }
        }
        return false;
    }

    static boolean inKeywords(String w) {
        for (String k : keywords) {
            if (k.equals(w)) {
                return true;
            }
        }
        for (String k : customKeywords) {
            if (k.equals(w)) {
                return true;
            }
        }
        return false;
    }

    // -----------------------------
    // Gestion des commentaires
    // -----------------------------
    static int skipComment(String code, int i) {
        // commentaire ligne //
        if (i < code.length() - 1 && code.charAt(i) == '/' && code.charAt(i + 1) == '/') {
            i += 2;
            while (i < code.length() && code.charAt(i) != '\n') {
                i++;
            }
            return i;
        }
        // commentaire bloc /* ... */
        if (i < code.length() - 1 && code.charAt(i) == '/' && code.charAt(i + 1) == '*') {
            i += 2;
            while (i < code.length() - 1) {
                if (code.charAt(i) == '*' && code.charAt(i + 1) == '/') {
                    i += 2;
                    break;
                }
                i++;
            }
            return i;
        }
        return i;
    }

    // -----------------------------
    // Classification finale
    // -----------------------------
   
    static void classify(String mot,int line) {
        if (mot.length() == 0) {
            return;
        }
       

        if (inKeywords(mot)) {
            Token.addToken("KEYWORD", mot, line);
            return;
        }

        boolean allDigits = true;
        for (char c : mot.toCharArray()) {
            if (!(c >= '0' && c <= '9')) {
                allDigits = false;
                break;
            }
        }
        if (allDigits) {
            Token.addToken("NUMBER", mot, line);
            return;
        }

        if (mot.startsWith("\"") && mot.endsWith("\"")) {
            Token.addToken("STRING", mot, line);
            return;
        }

        char c0 = mot.charAt(0);
        if ((c0 >= 'a' && c0 <= 'z') || (c0 >= 'A' && c0 <= 'Z') || c0 == '_') {
            Token.addToken("IDENTIFIER", mot, line);

            return;
        }

        Token.addToken("UNKNOWN", mot, line);

    }

}
