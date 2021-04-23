package SymbolTable;

import java.util.ArrayList;

public class InheritEntry extends SymTabEntry{
    public InheritEntry(String name){
        super("Inherit", name, "", null);
    }

    public String toString(){
        return 	String.format("%-12s" , "| " + m_kind) +
                String.format("%-12s" , "| " + m_type) +
                String.format("%-12s"  , "  " + m_size) +
                String.format("%-8s"  , "  " ) +
                String.format("%-8s"  , " " )
                + "|";
    }
}
