package CONTROL_FLOW;

import TEMP.TEMP_FACTORY;
import MIPS.MIPSGenerator;
import java.util.*;
import static java.util.Collections.emptySet;
public class CFG {

    public static final int NUM_REG = 10;

    // from CFG build Interference_Graph and find an approbrate coloring.
    public static void colorMe(List<CFG_Node> list_of_commands) {
        List<Set<Integer>> interference_graph = init_Interference_Graph(list_of_commands);
        MIPSGenerator.getInstance().printCommandsToFile(init_Valid_Coloring(interference_graph));
    }

    static Map<Integer, Integer> init_Valid_Coloring(List<Set<Integer>> graph) {

        int graphSize = graph.size();
        Integer[] perm = new Integer[graphSize];
        for (int i = 0; i < graphSize; i++)
            perm[i] = i;
        Arrays.sort(perm, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return graph.get(o2).size() - graph.get(o1).size();
            }
        });

        Map<Integer, Integer> colored = new HashMap<>();

        for (int i : perm) {
            boolean[] notAvailableColors = {false, false, false, false, false, false, false, false};
            Set<Integer> neighbors = graph.get(i);

            for (int neigh : neighbors) {
                if (colored.containsKey(neigh)) notAvailableColors[colored.get(neigh)] = true;
            }
            int reg_Bound = NUM_REG - 1;
            for (int j = 0; j <= reg_Bound; j++) {
                if (notAvailableColors[j] == false) {
                    colored.put(i, j);
                    break;
                }
                if (j == reg_Bound) throw new RuntimeException("We can't color!");
            }
        }
        return colored;
    }


    //geven a CFG got the build the Interference_Graph
    public static List<Set<Integer>> init_Interference_Graph(List<CFG_Node> commands_list) {
        boolean finish = false;
        while (!finish) {
            for (CFG_Node cmd : commands_list) {
                cmd.updateLiveIn();
                cmd.updateLiveOut();
            }

            finish = true;
            for (CFG_Node cmd : commands_list)
                if ((!cmd.liveIn.equals(cmd.liveInTag)) || (!cmd.liveOut.equals(cmd.liveOutTag)))
                    finish = false;
            for (CFG_Node cmd : commands_list) {
                cmd.liveIn.clear();
                cmd.liveIn.addAll(cmd.liveInTag);
                cmd.liveOut.clear();
                cmd.liveOut.addAll(cmd.liveOutTag);
            }
        }

        int size = TEMP_FACTORY.getInstance().getFreshTEMP().getSerialNumber();
        List<Set<Integer>> result = new ArrayList<>(size); // list of neighbors for each var
        for (int i = 0; i < size; i++)
            result.add(new HashSet<>());

        for (CFG_Node cmd : commands_list)
            if (cmd.liveOut.size() > 1)
                init_Clique(cmd.liveOut, result);
        return result;
    }

    // add a clique (with vertices clique_vertices ) to the graph
    static void init_Clique(Set<Integer> clique_vertices, List<Set<Integer>> graph) {
        if (clique_vertices.size() <= NUM_REG) {
            for (Integer i : clique_vertices) {
                graph.get(i).addAll(clique_vertices);
                graph.get(i).remove(i);
            }
        } else throw new RuntimeException("CAN'T COLOR! (there is a a node with a degree > 10");
    }
}
