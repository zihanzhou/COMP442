package SymbolTable;

import java.util.ArrayList;
import java.util.List;

public class SymTab {
    public String                 m_name       = null;
    public ArrayList<SymTabEntry> m_symlist    = null;
    public int                    m_size       = 0;
    public int                    m_tablelevel = 0;
    public SymTab                 m_uppertable = null;


    public SymTab(int level, SymTab uppertable){
        m_tablelevel = level;
        m_name = null;
        m_symlist = new ArrayList<SymTabEntry>();
        m_uppertable = uppertable;
    }

    public SymTab(int level, String name, SymTab uppertable){
        m_tablelevel = level;
        m_name = name;
        m_symlist = new ArrayList<SymTabEntry>();
        m_uppertable = uppertable;
    }

    public void addEntry(SymTabEntry p_entry){
        m_symlist.add(p_entry);
    }

    public SymTabEntry lookupName(String p_tolookup) {
        SymTabEntry returnvalue = new SymTabEntry();
        boolean found = false;
        for( SymTabEntry rec : m_symlist) {
            if (rec.m_name.equalsIgnoreCase(p_tolookup)) {
                returnvalue = rec;
                found = true;
                return returnvalue;
            }
        }
        if (!found) {
            if (m_uppertable != null) {
                returnvalue = m_uppertable.lookupName(p_tolookup);
            }
        }
        return returnvalue;
    }

    public SymTabEntry lookupName(SymTab table, String p_tolookup){
        SymTabEntry returnvalue = new SymTabEntry();
        boolean found = false;
        for( SymTabEntry rec : table.m_symlist) {
            if (rec.m_name.equalsIgnoreCase(p_tolookup)) {
                returnvalue = rec;
                found = true;
            }
        }
        if (!found) {
            returnvalue.m_name = "Fail to Find";
        }
        return returnvalue;
    }

    public SymTabEntry lookupName(String p_tolookup, int p_paramSize) {
        SymTabEntry returnvalue = new SymTabEntry();
        boolean found = false;
        for( SymTabEntry rec : m_symlist) {
            if (rec.m_name.equalsIgnoreCase(p_tolookup) && (rec.m_params.size() == p_paramSize)) {
                returnvalue = rec;
                found = true;
                return returnvalue;
            }
        }
        if (!found) {
            if (m_uppertable != null) {
                returnvalue = m_uppertable.lookupName(p_tolookup, p_paramSize);
            }
        }
        return returnvalue;
    }


    public String toString(){
        String stringtoreturn = new String();
        String prelinespacing = new String();
        for (int i = 0; i < this.m_tablelevel; i++)
            prelinespacing += "|    ";

        String new_name = "";
        if (m_uppertable != null) {
            new_name = m_uppertable.m_name + "::" + m_name;
        } else {
            new_name = m_name;
        }
        stringtoreturn += "\n" + prelinespacing + "=====================================================\n";
        stringtoreturn += prelinespacing + String.format("%-25s" , "| table: " + m_name) + String.format("%-27s" , " scope offset: " + m_size) + "|\n";
        stringtoreturn += prelinespacing        + "=====================================================\n";
        for (int i = 0; i < m_symlist.size(); i++){
            stringtoreturn +=  prelinespacing + m_symlist.get(i).toString() + '\n';
        }
        stringtoreturn += prelinespacing        + "=====================================================";
        return stringtoreturn;
    }
}
