package IR;
import TEMP.TEMP;

import java.util.*;
public class IR_Graph {
    private Map<IRcommand, List<IRcommand>> graph;
    private int size;
    private List<IRcommand> cmdLst;
    private Map<TEMP, Set<TEMP>> interference;
    public  IR_Graph() {
        graph = new HashMap<IRcommand, List<IRcommand>>();
        IR ir = IR.getInstance();
        cmdLst = new LinkedList<IRcommand>();
        IRcommand head = ir.head;
        IRcommandList tail = ir.tail;

        while ((head != null) && (tail != null))
        {
            addCmd(head, tail.head);
            head = tail.head;
            tail = tail.tail;
        }
    }
    private void addCmd(IRcommand cmd, IRcommand next) {
        LinkedList<IRcommand> nextCmds = new LinkedList<IRcommand>();
        if(cmd instanceof IRcommand_Func_Call) {
            nextCmds.add(IR.getInstance().findCmdAtLabel("FUNC_LABEL_" + ((IRcommand_Func_Call) cmd).func.name));
            nextCmds.add(next);
        } else if(cmd instanceof IRcommand_Jump_If_Eq_To_Zero) {
            nextCmds.add(IR.getInstance().findCmdAtLabel(((IRcommand_Jump_If_Eq_To_Zero) cmd).label_name));
            nextCmds.add(next);
        } else if(cmd instanceof IRcommand_Jump_Label) {
            nextCmds.add(IR.getInstance().findCmdAtLabel(((IRcommand_Jump_Label) cmd).label_name));
        } else if(cmd instanceof IRcommand_Label) {
            return;
        }else {
            nextCmds.add(next);
        }

        graph.put(cmd, nextCmds);
        cmdLst.add(cmd);
        size++;
    }

    public void analyzeLiveness() {
        boolean finish = false;
        IRcommand cmd;
        List<IRcommand> nextCmds;
        Set<TEMP> oldRegOut;
        Set<TEMP> oldRegIn;

        while (!finish) {
            finish = true;
            for (int i = size-1; i >= 0; i--) {

                cmd = cmdLst.get(i);
                oldRegOut = cmd.regOut;
                oldRegIn = cmd.regIn;
                nextCmds = graph.get(cmd);
                for (int j = 0; j < nextCmds.size(); j++) {
                    if(nextCmds.get(j) != null) {
                        cmd.regOut.addAll(nextCmds.get(j).regIn);
                    }
                }
                cmd.regIn = cmd.usedRegs();
                cmd.regIn.addAll(cmd.regOut);
                cmd.regIn.remove(cmd.modifiedReg());
//                if(cmd.modifiedReg() == null)
                if(cmd.modifiedReg() != null && cmd.modifiedReg().toString().equals("$t25")) {
                    System.out.println("===== " + cmd.getClass());
                }
                if(!oldRegOut.equals(cmd.regOut) || !oldRegIn.equals(cmd.regIn)) {
                    finish = false;
                }
            }
        }
    }

    public void genInterference() {
        interference = new HashMap<TEMP, Set<TEMP>>();
        Set<TEMP> allTemps = new HashSet<TEMP>();
        Set<TEMP> temps;
        for (IRcommand cmd : graph.keySet()) { allTemps.addAll(cmd.usedRegs()); allTemps.add(cmd.modifiedReg()); }
        for (TEMP temp : allTemps) {
            temps = new HashSet<TEMP>();
            for (IRcommand cmd : graph.keySet()) {
                if(cmd.regIn.contains(temp)) {
                    temps.addAll(cmd.regIn);
                }
            }
            temps.remove(temp);
            interference.put(temp, temps);
        }
        for (TEMP temp : interference.keySet()) {
            System.out.println(temp + " -> " + interference.get(temp));
        }
    }

    public Map<TEMP, Integer> paintInterference(int k) {
        boolean found = true;
        Stack<TEMP> stack = new Stack<TEMP>() ;
        Map<TEMP, Boolean> isPopped = new HashMap<TEMP, Boolean>();
        Map<TEMP, Integer> paint = new HashMap<TEMP, Integer>();
        for (TEMP temp : interference.keySet()) { isPopped.put(temp, false); }
        while(found) {
            found = false;
            for (TEMP temp : interference.keySet()) {
                if (isPopped.get(temp)) {
                    continue;
                }
                int size = 0;
                for (TEMP _temp : interference.get(temp)) {
                    if (!isPopped.get(_temp)) size++;
                }
                if (size < k) { // valid candidate for pop
                    found = true;
                    isPopped.put(temp, true);
                    stack.push(temp);
                }
            }
        }

        IR ir = IR.getInstance();
        while(!stack.isEmpty()) {
            int color = 0;
            int i;
            TEMP temp = stack.pop();
            if(temp == ir.sp || temp == ir.fp || temp == ir.ra || temp == ir.v0 || temp == ir.v1 ||
                    temp == ir.a0 || temp == ir.a1 || temp == ir.s0 || temp == ir.s1) {
                continue;
            }
            isPopped.put(temp, false);
            for (i = 0; i < k; i++) {
                boolean valid = true;
                for (TEMP neighbor : interference.get(temp)) {

                    if(!isPopped.get(neighbor) && paint.get(neighbor) == i) {
                        valid = false;
                    }
                }
                if(valid) {break;}
            }
            paint.put(temp, i);
        }

        // print paint
        for (TEMP temp : paint.keySet()) {
            System.out.println(temp + " := " + paint.get(temp));
        }
        return paint;

    }

    public void PrintMe() {
        System.out.println("Graph:");
        for (Map.Entry<IRcommand,List<IRcommand>> entry : graph.entrySet())
            System.out.println(entry.getKey() + ":\n\t" + entry.getValue() +
                    "\n\tIN:" + entry.getKey().regIn + "\n\tOUT:" + entry.getKey().regOut);
    }


}
