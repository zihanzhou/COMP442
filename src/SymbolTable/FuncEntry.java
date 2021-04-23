package SymbolTable;

import java.util.ArrayList;

public class FuncEntry extends SymTabEntry{

    public String m_visible = "";
    public String returnAndParam = "";

    public FuncEntry(String type, String name, ArrayList<VarEntry> params, String Visibility, SymTab table){
        super(new String("func"), type, name, table);
        m_params = params;
        m_visible = Visibility;
        returnAndParam += "(";
        for (VarEntry var : m_params){
            returnAndParam += var.m_type + " ";
        }
        returnAndParam += ")" + type;
    }

    public String toString(){
        return 	String.format("%-12s" , "| " + m_kind) +
                String.format("%-12s" , "| " + m_name) +
                String.format("%-12s" , "| " + returnAndParam) +
                String.format("%-12s", "| " + m_visible) +
                "|" + m_subtable.toString();
    }
}
