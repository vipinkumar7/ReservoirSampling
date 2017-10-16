package reservoir.sampler.dist

import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable
import scala.util.Random


object DistributedReservoirSampler {


  def main(args: Array[String]): Unit = {
    val max_num = if (args.length > 0) args(0).toInt else 10
    var circular = new Circular(1 until max_num toList)
    val config = new SparkConf().setAppName("sampler")
    val random = if (args.length >= 2) new Random() else new Random(12345)
    val reservoir_size = if (args.length >= 3 ) args(2).toInt else 5

    val input_file = if (args.length >= 4) args(3).toString else "/root/test/data1.txt"
    val master_url = if (args.length == 5) args(4).toString else "local"
    config.setMaster(master_url)
    val sparkContext = new SparkContext(config)
    val result = sparkContext.textFile(input_file)
      .map(line => (circular.next, List(line)))
      .reduceByKey(_ ++ _)
      .map(lineGroup => createMapperSideSampler(lineGroup._2, random, reservoir_size))
      .reduce(createFinalReservoir)

    val finalStr = result.map(x => x.value).mkString
    println(finalStr)
  }

  def createFinalReservoir(first: List[Tuple], second: List[Tuple]): List[Tuple] = {


    val minHeap = new mutable.PriorityQueue[Tuple]()(MinOrderComparator)

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


    val minHeap = new mutable.PriorityQueue[Tuple]()(MinOrderComparator)

    var current_position = 0
    var current_random = 0
    lines.foreach { current_line =>
      for (i <- 0 to current_line.length() - 1) {
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
