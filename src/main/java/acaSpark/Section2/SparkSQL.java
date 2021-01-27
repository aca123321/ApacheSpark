package acaSpark.Section2;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.api.java.function.FilterFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class SparkSQL {

    public static void main(String[] args) {

        Logger.getLogger("org.apache").setLevel(Level.WARN);

        SparkSession spark = SparkSession.builder().appName("Spark SQL").master("local[*]")
                                                    .config("spark.sql.warehouse.dir", "file:///d:/tmp/")
                                                    .getOrCreate();

        Dataset<Row> dataset = spark.read().option("header", true).csv("src/main/resources/exams/students.csv");
//        dataset.show();

        Dataset<Row> mathData = dataset.filter("subject='Math' AND year>2006");
//        Dataset<Row> mathData = dataset.filter((Row row) -> {
//            return (row.getAs("subject").equals("Math") && Integer.parseInt(row.getAs("year")) > 2006);
//        });
        mathData.show();
        System.out.println("There are " + mathData.count() + " math records from the year > " + 2006);

        spark.close();
    }

}
