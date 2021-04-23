package SymbolTable;

import java.util.ArrayList;

public class SymTabEntry {
    public String               m_kind       = null;
    public String               m_type       = null;
    public String               m_name       = null;
    public int                  m_size       = 0;
    public int                  m_offset     = 0;
    public SymTab               m_subtable   = null;
    public ArrayList<Integer>   m_dims       = new ArrayList<Integer>();
    public boolean              isEmpty      = true;
    public ArrayList<VarEntry>  m_params   = new ArrayList<VarEntry>();
    public SymTabEntry() {}

    public SymTabEntry(String kind, String type, String name, SymTab subtable) {
        this.m_kind = kind;
        this.m_type = type;
        this.m_name = name;
        this.m_subtable = subtable;
        this.isEmpty = false;
    }

    public boolean isNull() {
        return (isEmpty);
    }
}
