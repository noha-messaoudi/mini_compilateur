/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package compilateur;

public class Token {

    public String type;
    public String value;
 public int line; 
    // Tableau pour stocker les tokens
    private static Token[] tokens = new Token[1000];
    private static int tokenCount = 0;

    public Token(String type, String value, int line) {
        this.type = type;
        this.value = value;
         this.line = line;
    }

    // Ajouter un token
    public static void addToken(String type, String value, int line) {
        tokens[tokenCount] = new Token(type, value, line);
        tokenCount++;
    }

    // Récupérer tous les tokens
    public static Token[] getTokens() {
        return tokens;
    }

    // Afficher les tokens (optionnel)
 public static void printTokens() {
    for (int i = 0; i < tokenCount; i++) {
        System.out.println(tokens[i].type + " : " + tokens[i].value);
    }
}

}
