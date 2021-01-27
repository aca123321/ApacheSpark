package acaSpark.Section1;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        List<Double> in = new ArrayList<>();
        in.add(20.5);
        in.add(30.6);

        Logger.getLogger("org.apache").setLevel(Level.WARN);

        SparkConf conf = new SparkConf().setAppName("Spark is starting").setMaster("local[*]");
        JavaSparkContext sc = new JavaSparkContext(conf);

        JavaRDD<Double> rdd = sc.parallelize(in);
        JavaRDD<Double> tripled = rdd.map((a) -> {return 3*a;});
        tripled.foreach((val) -> {
            System.out.println(val);
        });

        JavaRDD<Integer> ones = rdd.map( val -> {return 1;});

        Double sum = tripled.reduce((a,b) -> {return a+b;});
        Double product = rdd.reduce((a,b) -> {return a*b;});

        int count = ones.reduce((a,b) -> {return a+b;});
        // OR better
        count = (int) rdd.count(); //count() method returns long type, so casting to int is needed

        System.out.println("sum: " + sum);
        System.out.println("product: " + product);
        System.out.println("count: " + count);

        sc.close();
    }

}

