package SymbolTable;

import java.util.ArrayList;
public class VarEntry extends SymTabEntry{
    public String m_visibility = "";

    public VarEntry(String kind, String type, String name, ArrayList<Integer> dims, String Visibility){
        super(kind, type, name, null);
        m_dims = dims;
        m_visibility = Visibility;
    }

    public String toString(){
            return String.format("%-12s", "| " + m_kind) +
                    String.format("%-12s", "| " + m_name) +
                    String.format("%-12s", "| " + m_type) +
                    String.format("%-12s", "| " + m_dims) +
                    String.format("%-8s", "| " + m_size) +
                    String.format("%-8s"  , "| " + m_offset) +
                    "|";

    }
}
