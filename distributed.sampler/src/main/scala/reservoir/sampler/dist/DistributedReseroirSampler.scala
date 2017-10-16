package reservoir.sampler.dist

import org.apache.spark.{SparkConf, SparkContext}
import scala.collection.mutable
import scala.util.Random
import joptsimple.OptionParser
import org.apache.spark.mllib.rdd.RDDFunctions._


object DistributedReservoirSampler {

  def main(args: Array[String]): Unit = {


    val optionParser = new OptionParser

    optionParser.
      accepts("random_seed").
      withOptionalArg.ofType(classOf[Boolean])
      .describedAs("is random seed required for random generator ")

    optionParser.
      accepts("sample_size").
      withRequiredArg.ofType(classOf[Integer]).
      describedAs("Sample size for output").defaultsTo(5)

    optionParser.
      accepts("input_file").
      withRequiredArg.ofType(classOf[String]).
      describedAs("Input File to be processed ").defaultsTo("")

    optionParser.accepts("master_url").
      withOptionalArg.ofType(classOf[String]).
      describedAs("Spark master server  ").
      defaultsTo("local")

    optionParser.accepts("h", "show help").forHelp

    optionParser.printHelpOn(System.out)
    var sample_size = 10
    var random_seed = false
    var input_file = "/root"
    var master_url = "local"
    val options = optionParser.parse(args: _*)
    if (options.has("sample_size"))
      sample_size = options.valueOf("sample_size").toString.toInt
    if (options.has("random_seed")) random_seed = true
    if (options.has("input_file"))
      input_file = options.valueOf("input_file").toString
    if (options.has("master_url"))
      master_url = options.valueOf("master_url").toString
    val random = if (random_seed) new Random() else new Random(12345)

    if (!options.has("h"))
      compute(master_url, input_file, random, sample_size)
  }

  def compute(master_url: String, input_file: String, random: Random, reservoir_size: Int): Unit = {

    val config = new SparkConf().setAppName("sampler")
    config.setMaster(master_url)
    val sparkContext = new SparkContext(config)
    val result = sparkContext.textFile(input_file).sliding(10, 10)
      .map(lineGroup => createMapperSideSampler(lineGroup.toList, random, reservoir_size))
      .reduce(createFinalReservoir)
    val finalStr = result.map(x => x.value).mkString
    println(finalStr)
    sparkContext.stop()

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
