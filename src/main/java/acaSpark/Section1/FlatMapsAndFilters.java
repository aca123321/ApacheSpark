package acaSpark.Section1;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FlatMapsAndFilters {

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

        JavaRDD<String> sentences = sc.parallelize(inputData);

        // a.split(" ") returns a String[], but we need an Iterator Object,
        // so we convert it to an ArrayList type and return its iterator
        JavaRDD<String> words = sentences.flatMap((a) -> {return Arrays.asList(a.split(" ")).iterator();});

        //Remove all single digit numbers from the RDD
        JavaRDD<String> filteredWords = words.filter((word) -> { return word.length()>1; });

        filteredWords.foreach(a -> {System.out.println(a);});
    }

}
