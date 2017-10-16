package reservoir.sampler.dist

import scala.collection.mutable.Queue

class Circular[A](list: Seq[A]) extends  java.io.Serializable{

  val elements = new Queue[A] ++= list
  var pos = 0

  def next = {
    if (pos == elements.length)
      pos = 0
    val value = elements(pos)
    pos = pos + 1
    value
  }

  def hasNext = !elements.isEmpty
  def add(a: A): Unit = { elements += a }
  override def toString = elements.toString

}









