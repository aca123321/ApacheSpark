package acaSpark.Section1;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.Scanner;

public class CourseRankings {

    public static void main(String[] args) {
        Logger.getLogger("org.apache").setLevel(Level.WARN);

        SparkConf conf = new SparkConf().setAppName("Course Chapter count").setMaster("local[*]");
        JavaSparkContext sc = new JavaSparkContext(conf);


        JavaRDD<String> viewslines = sc.textFile("src/main/resources/viewing figures/views-1.csv");
        JavaRDD<String> views2lines = sc.textFile("src/main/resources/viewing figures/views-2.csv");
        JavaRDD<String> views3lines = sc.textFile("src/main/resources/viewing figures/views-3.csv");
        viewslines = viewslines.union(views2lines);
        viewslines = viewslines.union(views3lines);
        JavaPairRDD<String, String> chapterUsers = viewslines.mapToPair( line -> {
            String[] cols = line.split(",");
            return new Tuple2<>(cols[1], cols[0]);
        });
        chapterUsers = chapterUsers.distinct();


        JavaRDD<String> chapterslines = sc.textFile("src/main/resources/viewing figures/chapters.csv");

        chapterslines = chapterslines.cache();

        JavaPairRDD<String, Integer> courseChapterCountRDD = chapterslines.mapToPair(line -> {
            String[] cols = line.split(",");
            return new Tuple2<>(cols[1], 1);
        });
        courseChapterCountRDD = courseChapterCountRDD.reduceByKey( (a,b) -> {return a+b;});

        JavaPairRDD<String, String> chapterCourse = chapterslines.mapToPair(line -> {
            String[] cols = line.split(",");
            return new Tuple2<>(cols[0], cols[1]);
        });

        JavaPairRDD<String, Tuple2<String, String>> chapterUsersCourse = chapterUsers.join(chapterCourse);

//        chapterUsersCourse.foreach(elem -> {
//            String chapter = elem._1;
//            String user = elem._2._1;
//            String course = elem._2._2;
//
//            System.out.println(chapter + " " + user + " " + course);
//        });

        JavaPairRDD<Tuple2<String, String>, Integer> courseUserPairCourse = chapterUsersCourse.mapToPair(elem -> {
            String user = elem._2._1;
            String course = elem._2._2;
            return new Tuple2<>(new Tuple2<>(course, user), 1);
        });

        JavaPairRDD<Tuple2<String, String>, Integer> courseUserPairViewCount = courseUserPairCourse.reduceByKey((a, b) -> {
            return a+b;
        });

//        courseUserPairViewCount.foreach(elem -> {
//            System.out.println(elem._1._1 + " " + elem._1._2 + " " + elem._2);
//        });

        JavaPairRDD<String, Tuple2<String, Integer>> courseUserViewCountPair = courseUserPairViewCount.mapToPair(elem -> {
            return new Tuple2<>(elem._1._1, new Tuple2<>(elem._1._2, elem._2));
        });

//        courseUserViewCountPair.foreach(elem -> {
//            System.out.println(elem._2._1 + " has seen " + elem._2._2 + " chapters of " + elem._1);
//        });

        JavaPairRDD<String, Tuple2<Tuple2<String, Integer>, Integer>> course_userViewPair_chapterCount_Pair = courseUserViewCountPair.join(courseChapterCountRDD);

        JavaPairRDD<String, Integer> courseScores = course_userViewPair_chapterCount_Pair.mapToPair(elem -> {
            String course, user;
            Integer viewCount, chapterCount, score;
            Double percent;

            course = elem._1;
            user = elem._2._1._1;
            viewCount = elem._2._1._2;
            chapterCount = elem._2._2;

            percent = ((Double.valueOf(viewCount))/(Double.valueOf(chapterCount)))*Double.valueOf(100);
            if(percent > 90) {
                score = 10;
            }
            else if(percent > 50) {
                score = 4;
            }
            else if(percent > 25) {
                score = 2;
            }
            else {
                score = 0;
            }
            return new Tuple2<>(course, score);
        });

        courseScores = courseScores.reduceByKey((a,b) -> {return a+b;});

        JavaRDD<String> titleslines = sc.textFile("src/main/resources/viewing figures/titles.csv");
        JavaPairRDD<String, String> courseTitle = titleslines.mapToPair(line -> {
            String[] cols = line.split(",");
            return new Tuple2<>(cols[0], cols[1]);
        });
        JavaPairRDD<String, Tuple2<Integer, String>> courseScoreTitle = courseScores.join(courseTitle);

//        courseScoreTitle.foreach(elem -> {
//            System.out.println(elem._2._2 + " has a score of " + elem._2._1);
//        });

        JavaPairRDD<Integer, String> scoreTitle = courseScoreTitle.mapToPair(elem -> {
            return new Tuple2<>(elem._2._1, elem._2._2);
        });

        scoreTitle = scoreTitle.sortByKey(false);

        scoreTitle.collect().forEach(elem -> {
            System.out.println(elem._2 + " has a score of " + elem._1);
        });

        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        
        sc.close();
    }

}
