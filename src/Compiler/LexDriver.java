package Compiler;

import java.io.*;
import java.util.Scanner;

import AST.Node;
import Visitors.ComputeMemSizeVisitor;
import Visitors.SymbolTable.*;
import Visitors.TagsBasedCodeGenerationVisitor;

public class LexDriver {

    public static void main(String[] args) throws IOException {
        System.out.print("File to read(default:polynomial.src):");
        Scanner scanner = new Scanner(System.in);
        String pathname = scanner.nextLine();
        if (pathname.equals("")){
            pathname = "polynomialsemanticerrors.src";
        }

        File filename = new File(pathname);
        Lexer lex = new Lexer(filename);
        DataList<Token> token = lex.Lex();
        lex.close();

        Parse parser = new Parse(token);
        parser.set_path(filename);
        parser.parse();
        parser.close();
        String outSemanticerrors = "outsemanticerrors/" + filename.getName().substring(0, filename.getName().length() - 4) + ".outsemanticerrors";
        BufferedWriter out_Semanticerrors = new BufferedWriter(new FileWriter(outSemanticerrors));

        Node Ast = parser.ProgNode;

        SymTabCreationVisitor ST = new SymTabCreationVisitor(filename);
        ST.setOutErrors(out_Semanticerrors);

        TypeCheckingVisitor TC = new TypeCheckingVisitor(filename);
        TC.setOutErrors(out_Semanticerrors);

        Ast.accept(ST);
        Ast.accept(TC);

        out_Semanticerrors.close();
        File ifErrors = new File(outSemanticerrors);
        if (ifErrors.length() == 0) {
            ComputeMemSizeVisitor CM = new ComputeMemSizeVisitor(filename);
            TagsBasedCodeGenerationVisitor TB = new TagsBasedCodeGenerationVisitor(filename);
            Ast.accept(CM);
            Ast.accept(TB);
        } else {
            System.out.println("SemanticErrors detected, not going to generate code and offset sizes");
        }
    }
}
