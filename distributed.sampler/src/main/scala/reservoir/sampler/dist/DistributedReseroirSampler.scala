package reservoir.sampler.dist

import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable
import scala.util.Random


object MinOrder extends Ordering[Tuple] {
  def compare(x: Tuple, y: Tuple) = y.key compare x.key
}

object DistributedReservoirSampler {


  def main(args: Array[String]): Unit = {
    val max_num = 10
    var circular = new Circular(1 until max_num toList)
    val config = new SparkConf().setAppName("TestStack")
    val random = new Random(12345)
    val reservoir_size = 2
    config.setMaster("local")
    val sparkContext = new SparkContext(config)
    val result = sparkContext.textFile("/root/test/data1.txt")
      .map(line => (circular.next, List(line)))
      .reduceByKey(_ ++ _)
      .map(lineGroup => createMapperSideSampler(lineGroup._2, random, reservoir_size))
      .reduce(createFinalReservoir)

    val finalStr = result.map(x => x.value).mkString
    println(finalStr)
  }

  def createFinalReservoir(first: List[Tuple], second: List[Tuple]): List[Tuple] = {


    val minHeap = new mutable.PriorityQueue[Tuple]()(MinOrder)

    first.foreach(
      tuple => {
        minHeap.enqueue(tuple)
      }
    )

    second.foreach {
      tuple => {

        val current_random = tuple.key;
        if (current_random > minHeap.head.key) {
          minHeap.dequeue();
          minHeap.enqueue(tuple);
        }

      }
    }
    minHeap.toList
  }

  def createMapperSideSampler(lines: List[String], random: Random, reservoir_size: Int): List[Tuple] = {


    val minHeap = new mutable.PriorityQueue[Tuple]()(MinOrder)

    var current_position = 0
    var current_random = 0
    lines.foreach { current_line =>
      for (i <- 0 to current_line.length()-1) {
        if (current_position < reservoir_size) {
          current_random = random.nextInt();
          minHeap.enqueue(new Tuple(current_line.charAt(i),
            current_random));
          current_position += 1;
        } else {
          current_random = random.nextInt();
          if (current_random > minHeap.head.key) {
            minHeap.dequeue();
            minHeap.enqueue(new Tuple(current_line.charAt(i),
              current_random));
          }
        }

      }

    }

    minHeap.toList
  }
}
