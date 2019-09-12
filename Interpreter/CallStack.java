package Interpreter;

import java.util.HashMap;
import java.util.Stack;

public class CallStack {
    public Stack<ActivationRecord> activeRecords = new Stack<>();

    public void push(ActivationRecord activeRecord){
        activeRecords.push(activeRecord);
    }

    public ActivationRecord pop(){
        return activeRecords.pop();
    }

    public ActivationRecord peek(){
        return activeRecords.peek();
    }

    public String toString(){
        return "Call Stack:\n" + activeRecords;
    }
}
