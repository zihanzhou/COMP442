package Visitors;

import AST.Node;
import Visitors.SymbolTable.SymTabCreationVisitor;
import Visitors.SymbolTable.TypeCheckingVisitor;
import Compiler.*;

import java.io.File;
import java.io.IOException;

public class semanticanalyzerdriver {

    public static void main(String[] args) throws IOException {

        String pathname = "polynomialsemanticerrors.src";
        File filename = new File(pathname);
        Lexer lex = new Lexer(filename);
        DataList<Token> token = new DataList<Token>();
        token = lex.Lex();
        lex.close();
        Parser parser = new Parser(token);
        parser.set_path(filename);
        parser.parse();
        parser.close();

        Node Ast = parser.return_Node();
        SymTabCreationVisitor ST = new SymTabCreationVisitor(filename);
        TypeCheckingVisitor TC = new TypeCheckingVisitor(filename);
        Ast.accept(ST);
        Ast.accept(TC);




    }
}
