package reservoir.sampler.dist

object MinOrderComparator extends Ordering[Tuple] {
  def compare(x: Tuple, y: Tuple) = y.key compare x.key
}

