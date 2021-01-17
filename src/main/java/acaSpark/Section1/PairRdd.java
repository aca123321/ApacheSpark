package acaSpark.Section1;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.codehaus.janino.Java;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.List;

public class PairRdd {

    public static void main(String[] args) {

        Logger.getLogger("org.apache").setLevel(Level.WARN);

        //Objective: to count the number of occurrence of each level
        List<String> inputData = new ArrayList<>();
        inputData.add("WARN: Tuesday 4 September 0405");
        inputData.add("ERROR: Tuesday 4 September 0408");
        inputData.add("FATAL: Wednesday 5 September 1632");
        inputData.add("ERROR: Friday 7 September 1854");
        inputData.add("WARN: Saturday 8 September 1942");

        SparkConf conf = new SparkConf().setAppName("Pair RDDs").setMaster("local[*]");
        JavaSparkContext sc = new JavaSparkContext(conf);

        JavaRDD<String> logRDD = sc.parallelize(inputData);

        // Pair RDDs can have multiple instances of the same key
        JavaPairRDD<String, Integer> logPairRDD = logRDD.mapToPair( logString -> {
            String[] cols = logString.split(":");
            String level = cols[0];
//            String date = cols[1];
            return new Tuple2<>(level, 1);
        });

        JavaPairRDD<String, Integer> levelCountRDD = logPairRDD.reduceByKey((a, b) -> {return a+b;});
        levelCountRDD.foreach(a -> {
            System.out.println(a._1 + ": " + a._2);
        });

        sc.close();
    }

}
