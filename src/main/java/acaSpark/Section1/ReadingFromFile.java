package acaSpark.Section1;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.util.ArrayList;
import java.util.Arrays;

public class ReadingFromFile {

    public static void main(String[] args) {

        Logger.getLogger("org.apache").setLevel(Level.WARN);

        SparkConf conf = new SparkConf().setAppName("Reading from file").setMaster("local[*]");
//        conf.set("fs.defaultFS", "file://C:/spark-2.4.5-bin-hadoop2.7");
        JavaSparkContext sc = new JavaSparkContext(conf);

        JavaRDD<String> in = sc.textFile("D:\\programming stuff\\Practice\\SparkPractice\\src\\main\\resources\\subtitles\\input.txt");

        JavaRDD<String> words = in.flatMap((line) -> { return Arrays.asList(line.split(" ")).iterator(); });
        words.foreach(word -> {System.out.println(word);});

        sc.close();
    }
}
