/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package compilateur;

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

    // ------------------------------------------------------------
    // Vérification appartenance aux tableaux
    // ------------------------------------------------------------
    static boolean inOperators(char c) {
 for (int j = 0; j < operators.length; j++) {
    char op =  operators[j];  
    if( op == c) {            
        return true;         
    }}
 return false;}
 static boolean inSeparators(char c) {
   for (int j = 0; j < separators.length; j++) {
    char s = separators[j];  
    if (s == c) {            
        return true;         
    }}
 return false;}


 
    static boolean inKeywords(String w) {
        for (String k : keywords) if (k.equals(w)) return true;
        for (String k : customKeywords) if (k.equals(w)) return true;
        return false;
    }

        


    // ------------------------------------------------------------
    // Gestion des commentaires
    // ------------------------------------------------------------
    static int skipComment(String code, int i) {
        int n = code.length();

        // commentaire //
        if (i+1<n && code.charAt(i)=='/' && code.charAt(i+1)=='/') {
            i += 2;
            while (i<n && code.charAt(i)!='\n') i++;
            return i;
        }

        // commentaire /* ... */
        if (i+1<n && code.charAt(i)=='/' && code.charAt(i+1)=='*') {
            i += 2;
            while (i+1<n && !(code.charAt(i)=='*' && code.charAt(i+1)=='/')) i++;
            return i+2;
        }

        return i;
    }

    // ------------------------------------------------------------
    // Classification finale d’un mot
    // ------------------------------------------------------------
    static void classify(String mot) {

    if (mot.length() == 0) return; // rien à classer

    boolean trouvé = false; // indicateur si on a trouvé le type

    // ------------------------
    // Vérification mots-clés
    // ------------------------
    for (int i = 0; i < keywords.length; i++) {
        if (mot.equals(keywords[i])) {
            System.out.println("KEYWORD     : " + mot);
            trouvé = true;
            break;
        }
    }
    if (!trouvé) {
        for (int i = 0; i < customKeywords.length; i++) {
            if (mot.equals(customKeywords[i])) {
                System.out.println("KEYWORD     : " + mot);
                trouvé = true;
                break;
            }
        }
    }

    // ------------------------
    // Vérification nombre
    // ------------------------
    if (!trouvé) {
        boolean allDigits = true;
        for (int i = 0; i < mot.length(); i++) {
            char c = mot.charAt(i);
            if (!(c >= '0' && c <= '9')) {
                allDigits = false;
                break;
            }
        }
        if (allDigits) {
            System.out.println("NUMBER      : " + mot);
            trouvé = true;
        }
    }

    // ------------------------
    // Vérification chaîne
    // ------------------------
    if (!trouvé) {
        if (mot.charAt(0) == '"' && mot.charAt(mot.length() - 1) == '"') {
            System.out.println("STRING      : " + mot);
            trouvé = true;
        }
    }

    // ------------------------
    // Vérification identificateur
    // ------------------------
    if (!trouvé) {
        char c0 = mot.charAt(0);
        if ((c0 >= 'a' && c0 <= 'z') || (c0 >= 'A' && c0 <= 'Z') || c0 == '_') {
            System.out.println("IDENTIFIER  : " + mot);
            trouvé = true;
        }
    }

    // ------------------------
    // Sinon → token inconnu
    // ------------------------
    if (!trouvé) {
       System.out.println("token iconnu  : " + mot);
    }}}

    
    
    

