/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package compilateur;

import java.util.Scanner;



public class SyntaxParser {

    private Token[] tokens;
    private int i;
    private Token tc;
    private boolean test = true;
public int line; 
    public SyntaxParser(Token[] tokens) {
        this.tokens = tokens;
        this.i = 0;
        this.tc = tokens[i]; // premier token
    }

    // -----------------------------
    // Méthodes utilitaires
    // -----------------------------
    private void motCle(String mot) {
        if (tc.value.equals(mot)) {
            avancer();
        } else {
            erreur("mot-clé " + mot + " attendu");
        }
    }

    private boolean motCourant(String mot) {
        return tc.value.equals(mot);
    }

    private void symbole(String s) {
        if (tc.value.equals(s)) {
            avancer();
        } else {
            erreur("symbole " + s + " attendu");
        }
    }

    private void identificateur() {
        if (tc.type.equals("IDENTIFIER")) {
            avancer();
        } else {
            erreur("identificateur attendu");
        }
    }

    private void avancer() {
        i++;
        if (i < tokens.length && tokens[i] != null) {
            tc = tokens[i];
        } else {
            tc = new Token("EOF", "#",line);
        }
    }
    private void erreur(String msg) {
    System.out.println("Erreur syntaxique: " + msg +
        " | token: " + tc.type + " : " + tc.value +
        " (ligne " + tc.line + ")");
    test = false;
    avancer();
}


  

    // -----------------------------
    // Axiome externe
    // -----------------------------
    public void Z() {
        Program(); // appel de la grammaire
        if (tc.value.equals("#") && test) {
            System.out.println("Analyse acceptée ✅");
        } else {
            System.out.println("Fin de chaîne inattendue| dernier token: " + tc.type + ":" + tc.value);       

        }
    }

    // -----------------------------
    // Grammaire
    // -----------------------------
    // Program → ClassDecl
    private void Program() {
        ClassDecl();
    }

    // ClassDecl → ["public"] "class" IDENTIFIER "{" MethodDeclList "}"
    private void ClassDecl() {
        if (motCourant("public")) motCle("public");
        motCle("class");
        identificateur();
        symbole("{");
        MethodDeclList();
        symbole("}");
    }

    // MethodDeclList → { MethodDecl }
    private void MethodDeclList() {
        while (motCourant("public")) {
            MethodDecl();
        }
    }

    // MethodDecl → "public" "static" "void" IDENTIFIER "(" ParamList ")" "{" StmtList "}"
    private void MethodDecl() {
        motCle("public");
        motCle("static");
        motCle("void");
        identificateur();
        symbole("(");
        ParamList();
        symbole(")");
        symbole("{");
        StmtList();
        symbole("}");
    }

    // ParamList → [ Param { "," Param } ]
    private void ParamList() {
        if (tc.value.equals("String")) {
            motCle("String");
            symbole("[");
            symbole("]");
            identificateur();
        } else if (tc.type.equals("IDENTIFIER")) {
            identificateur();
            while (motCourant(",")) {
                symbole(",");
                identificateur();
            }
        }
    }

   // StmtList → { Statement } ; s'arrête avant '}', 'break', 'case', 'default'
private void StmtList() {
    while (!motCourant("}") 
           && !motCourant("break") 
           && !motCourant("case") 
           && !motCourant("default") 
           && !tc.type.equals("EOF")) {
        Statement();
    }
}


    // Statement → VarDecl | AssignStmt | CompareStmt | SwitchStmt | PrintStmt
    private void Statement() {
        if (tc.value.equals("int") || tc.value.equals("double") || tc.value.equals("char")
                || tc.value.equals("String") || tc.value.equals("boolean")) {
            VarDecl();
        } else if (tc.type.equals("IDENTIFIER")) {
            AssignStmt();
        } else if (tc.value.equals("if")) {
            CompareStmt();
        } else if (tc.value.equals("switch")) {
            SwitchStmt();
        } else if (tc.value.equals("System")) {
            PrintStmt();
        } else {
            erreur("Instruction non reconnue");
            //avancer();
        }
    }

    // PrintStmt → System . out . println ( Expression ) ;
    private void PrintStmt() {
        motCle("System");
        symbole(".");
        motCle("out");
        symbole(".");
        motCle("println");
        symbole("(");
        Expression();
        symbole(")");
        symbole(";");
    }

    // VarDecl → Type IDENTIFIER ["=" Expression] ";"
    private void VarDecl() {
        Type();
        identificateur();
        if (motCourant("=")) {
            symbole("=");
            Expression();
        }
        symbole(";");
    }

    // AssignStmt → IDENTIFIER "=" Expression ";"
    private void AssignStmt() {
        identificateur();
        symbole("=");
        Expression();
        symbole(";");
    }

    // CompareStmt → "if" "(" Expression CompareOp Expression ")" "{" StmtList "}"
    private void CompareStmt() {
        motCle("if");
        symbole("(");
        Expression();
        CompareOp();
        Expression();
        symbole(")");
        symbole("{");
        StmtList();
        symbole("}");
    }

    // SwitchStmt → "switch" "(" IDENTIFIER ")" "{" CaseList "}"
    private void SwitchStmt() {
        motCle("switch");
        symbole("(");
        identificateur();
        symbole(")");
        symbole("{");
        CaseList();
        symbole("}");
    }

    // CaseList → { CaseStmt } [ DefaultStmt ]
    private void CaseList() {
        while (motCourant("case")) {
            CaseStmt();
        }
        if (motCourant("default")) {
            DefaultStmt();
        }
    }

    // CaseStmt → "case" Constant ":" StmtList "break" ";"
    private void CaseStmt() {
        motCle("case");
        Expression(); // constante
        symbole(":");
        StmtList();
        motCle("break");
        symbole(";");
    }

    // DefaultStmt → "default" ":" StmtList
    private void DefaultStmt() {
        motCle("default");
        symbole(":");
        StmtList();
    }

   // Expression → Term { "+" Term }
private void Expression() {
    Term();
    while (motCourant("+")) {
        symbole("+");
        Term();
    }
}

private void Term() {
    if (tc.type.equals("IDENTIFIER") || tc.type.equals("NUMBER") || tc.type.equals("STRING")) {
        avancer();
    } else if (tc.value.equals("true") || tc.value.equals("false")) {
        avancer();
    } else {
        erreur("Expression attendue");
    }
}


    // Type → "int" | "double" | "char" | "String" | "boolean"
    private void Type() {
        if (tc.value.equals("int") || tc.value.equals("double") || tc.value.equals("char")
                || tc.value.equals("String") || tc.value.equals("boolean")) {
            avancer();
        } else {
            erreur("Type attendu");
        }
    }

    // CompareOp → "==" | "<" | ">" | "<=" | ">=" | "!="
    private void CompareOp() {
        if (tc.value.equals("==") || tc.value.equals("<") || tc.value.equals(">")
                || tc.value.equals("<=") || tc.value.equals(">=") || tc.value.equals("!=")) {
            avancer();
        } else {
            erreur("Opérateur de comparaison attendu");
        }
    }
}
