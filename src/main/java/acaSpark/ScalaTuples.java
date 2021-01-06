package acaSpark;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

public class ScalaTuples {

    public static void main(String[] args) {

        // SCALA TUPLES ARE IMMUTABLE

        Logger.getLogger("org.apache").setLevel(Level.WARN);

        SparkConf conf = new SparkConf().setAppName("ScalaTuples").setMaster("local[*]");
        JavaSparkContext sc = new JavaSparkContext(conf);

        List<Double> in = new ArrayList<>();
        in.add(20.5);
        in.add(30.0);
        in.add(100.3);
        in.add(10.66);

        JavaRDD<Double> rdd = sc.parallelize(in);
        JavaRDD<Double> tripled = rdd.map(a -> 3*a);

        JavaRDD<Tuple2<Double, Double>> numWithItsTriple = rdd.map((a) -> {return new Tuple2<>(a, 3*a);});

        numWithItsTriple.foreach((a) -> {
            System.out.println(a._1 + ", " + a._2);
        });
    }

}
