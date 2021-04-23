package SymbolTable;

import java.util.ArrayList;

public class ClassEntry extends SymTabEntry{

    public ArrayList<SymTab> m_inherits   = new ArrayList<SymTab>();

    public ClassEntry(String p_name, SymTab p_subtable){
        super(new String("class"), p_name, p_name, p_subtable);
    }

    public void addInherits(SymTab p_inherit){
        m_inherits.add(p_inherit);
    }

    public ArrayList<SymTab> getInherits(){
        return m_inherits;
    }

    public String toString(){
        return 	String.format("%-12s" , "| " + m_kind) +
                String.format("%-40s" , "| " + m_name) +
                "|" +
                m_subtable;
    }
}
