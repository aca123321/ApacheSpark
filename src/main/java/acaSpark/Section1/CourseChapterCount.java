package acaSpark.Section1;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

public class CourseChapterCount {

    public static void main(String[] args) {

        Logger.getLogger("org.apache").setLevel(Level.WARN);

        SparkConf conf = new SparkConf().setAppName("Course Chapter count").setMaster("local[*]");
        JavaSparkContext sc = new JavaSparkContext(conf);

        JavaRDD<String> lines = sc.textFile("src/main/resources/viewing figures/chapters.csv");
        JavaPairRDD<String, Integer> courseCountRDD = lines.mapToPair(line -> {
            String[] cols = line.split(",");
            return new Tuple2<>(cols[1], 1);
        });

        courseCountRDD = courseCountRDD.reduceByKey( (a,b) -> {return a+b;});

        courseCountRDD.foreach(elem -> {System.out.println("Course " + elem._1 + " has " + elem._2 + " chapters");});
    }

}
