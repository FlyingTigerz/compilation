package CONTROL_FLOW;

import MIPS.*;

import java.io.PrintWriter;
import java.util.*;
import java.util.HashSet;

//c = a + b
public class CFG_Node {

    public int commandIndex;
    public String command;
    public Set<CFG_Node> edgesOut;
    public Set<Integer> liveOut, liveIn, liveOutTag, liveInTag;
    public Set<Integer> def;// defined temp in this command
    public Set<Integer> use;// temps that we use in order to assign the def temp.
    private final Integer[] tempVarsForFormat;//all temps names

    public CFG_Node(String command, Set<Integer> def, Set<Integer> use, Integer... tempVarsForFormat) {
        this.command = command;
        this.def = def;
        this.use = use;
        this.tempVarsForFormat = tempVarsForFormat;

        edgesOut = new HashSet<>();
        liveOut = new HashSet<>();
        liveIn = new HashSet<>();
        liveOutTag = new HashSet<>();
        liveInTag = new HashSet<>();
    }

    public void addEdge(CFG_Node node){
        edgesOut.add(node);
    }

    public void updateLiveIn() {
        this.liveInTag.clear();
        this.liveInTag.addAll(this.liveOut);
        this.liveInTag.removeAll(this.def);
        this.liveInTag.addAll(this.use);
    }
    public void updateLiveOut() {
        this.liveOutTag.clear();
        for (CFG_Node succ : this.edgesOut){
            this.liveOutTag.addAll(succ.liveInTag);
        }
    }

    public void printToFile(PrintWriter fileWriter, Map<Integer, Integer> regMap) {
        if (!this.def.isEmpty() && !this.liveOut.containsAll(this.def)){
            return;
        }
        Integer[] registerFormat = new Integer[tempVarsForFormat.length];
        for (int i = 0; i < registerFormat.length; i++) {
            registerFormat[i] = regMap.get(tempVarsForFormat[i]);
        }

        fileWriter.printf(command, (Object[])registerFormat);
    }
}
