package Interpreter;

import logger.Logger;

import java.util.HashMap;

public class ActivationRecord {
    public HashMap<String,Double> members;
    public String name;
    public ARType type;
    public int nestingLevel;

    public ActivationRecord(String name, ARType type, int nestingLevel){
        this.name = name; // program's name and procedure's names
        this.type = type;
        this.nestingLevel = nestingLevel;
        this.members = new HashMap<>();
    }

    public void setItem(String key, Double value){
        members.put(key,value);
    }

    public Double getItem(String key){
        return members.get(key);
    }

    public String toString(){
        return name + "-" + type + " lvl: " + nestingLevel + "\n" + members + "\n";
    }
}
