package acaSpark.Unrelated;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.ArrayList;

public class eightOutOf10catsDoesCountdown {

    private static int[] arr, vis;
    private static String ops;
    private static Double ans;
    private static int n;
    private static ArrayList<ArrayList<Integer>> perm;

    public static Double evaluate(String s) throws ScriptException {
        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
        ScriptEngine scriptEngine = scriptEngineManager.getEngineByName("JavaScript");

        Double ret = (Double)scriptEngine.eval("9 + 15 + 30");
        return ret;
    }

    public static void permute(int state, ArrayList<Integer> curArr)
    {
        if(state == n-1)
        {
            for(int i=0;i<n;i++)
            {
                if(vis[i] == 0)
                {
                    curArr.add(arr[i]);
                    break;
                }
            }
            ArrayList<Integer> temp = (ArrayList<Integer>) curArr.clone();
            perm.add(temp);
            curArr.remove(state);
        }

        for(int i=0;i<n;i++)
        {
            if(vis[i] == 0)
            {
                curArr.add(arr[i]);
                vis[i] = 1;
                permute(state+1, curArr);
                curArr.remove(state);
                vis[i] = 0;
            }
        }
    }

    public static ArrayList<String> getValidStrings()
    {
        ArrayList<String> ret = new ArrayList<>();

        for (ArrayList<Integer> curArr : perm)
        {
            StringBuilder s = new StringBuilder();
            for (int i = 0; i < curArr.size(); i++)
            {
                int num = curArr.get(i);
                s.append(num);
                if(i != curArr.size()-1)
                {

                }
            }
        }

        return ret;
    }

    public static void main(String[] args) {

        arr = new int[]{75, 1, 3, 1, 3, 6};
        n = 3;
        ans = 576.0;
        vis = new int[]{0, 0, 0, 0, 0, 0};
        ops = "+-*/";

        perm = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> curArr = new ArrayList<>();

        permute(0, curArr);
//        System.out.println("Number of permutations for " + n + " elements = " + perm.size());
        ArrayList<String> validStrings = getValidStrings();

        for(String s: validStrings)
        {
            System.out.println(s);
        }

//        for(int i=0;i<perm.size();i++)
//        {
//            curArr = perm.get(i);
//            for(int j=0;j<curArr.size();j++)
//            {
//                System.out.print(curArr.get(j) + " ");
//            }
//            System.out.print("\n");
//        }
    }

}
