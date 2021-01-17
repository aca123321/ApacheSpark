package acaSpark.Section1;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.Arrays;
import java.util.List;

public class DeployingOnAWS_EMR {

    public static boolean containsDigits(String s)
    {
        int n = s.length(),i;
        for(i=0;i<n;i++)
        {
            if(Character.isDigit(s.charAt(i)))
            {
                return true;
            }
        }
        return false;
    }


    public static void main(String[] args) {

        Logger.getLogger("org.apache").setLevel(Level.WARN);

        SparkConf conf = new SparkConf().setAppName("Keywords");
        JavaSparkContext sc = new JavaSparkContext(conf);

        JavaRDD<String> subs = sc.textFile("s3n://aca12332-spark/input.txt");

        JavaRDD<String> lines = subs.map(line -> {return line.replaceAll("[^a-zA-Z\\s]","").toLowerCase();});

        JavaRDD<String> words = lines.flatMap((line) -> { return Arrays.asList(line.split(" ")).iterator();});
        words = words.flatMap((line) -> { return Arrays.asList(line.split("/")).iterator();});

        JavaPairRDD<String, Integer> wordPair = words.mapToPair(word -> {
            return new Tuple2<>(word, 1);
        });

        JavaPairRDD<String, Integer> filteredWordPair = wordPair.filter(elem -> {
            String word = elem._1;
            if(Util.isBoring(word) || word.length() == 0)
            {
                return false;
            }
            return true;
        });

        JavaPairRDD<String, Integer> wordCount = filteredWordPair.reduceByKey( (a, b) -> {return a+b;});
        JavaPairRDD<Integer, String> countWord = wordCount.mapToPair((elem) -> {return new Tuple2<>(elem._2, elem._1);});

        JavaPairRDD<Integer, String> SortedWordCount = countWord.sortByKey(false);

        System.out.println(SortedWordCount.getNumPartitions());

        List<Tuple2<Integer, String>> sorted = SortedWordCount.take(10);
        sorted.forEach(System.out::println);

//        SortedWordCount.foreach(elem -> {
//            System.out.println(elem._1 + " -> " + elem._2);
//        });

        sc.close();
    }

}
