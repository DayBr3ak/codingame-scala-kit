package codingame.scala.ghostcell

import codingame.scala.graph.{Edge, ShortestPath}
import org.scalatest.{FlatSpec, Matchers}

class FactoryAnalysisSpec extends FlatSpec with Matchers {

  behavior of "Factory Analysis Spec"

  it should "just necessary cyborgs" in {
    val from = Factory(0, 1, 100, 0, 0)
    val to = Factory(1, 0, 50, 1, 0)
    FactoryAnalysis.conquer(to, from, Vector.empty, createState(from, to)) should be(51)
  }

  it should "just necessary cyborgs with zero oppo" in {
    val from = Factory(0, 1, 4, 0, 0)
    val to = Factory(1, -1, 0, 1, 0)
    FactoryAnalysis.conquer(to, from, Vector.empty, createState(from, to)) should be(3)
  }

  it should "just necessary cyborgs for oppo" in {
    val from = Factory(0, 1, 100, 0, 0)
    val to = Factory(1, -1, 1, 1, 0)
    FactoryAnalysis.conquer(to, from, Vector.empty, createState(from, to)) should be(4)
  }

  it should "sent all cyborgs" in {
    val from = Factory(0, 1, 49, 0, 0)
    val to = Factory(1, 0, 50, 1, 0)
    FactoryAnalysis.conquer(to, from, Vector.empty, createState(from, to)) should be(49)
  }

  it should "move to all neutral factories" in {
    val from = Factory(0, 1, 6, 0, 0)
    val n1 = Factory(1, 0, 2, 1, 0)
    val n2 = Factory(2, 0, 2, 1, 0)
    val state = GhostCellGameState(itineraries = GhostCellGrahp.shortestPath(3, Vector(Edge(0, 1, 1), Edge(0, 2, 2), Edge(1, 2, 1))), factories = Vector(from, n1, n2), troops = Vector.empty, bombs = Vector.empty)
    FactoryAnalysis.movePlans(state) should be(Vector(MoveAction(0, 1, 3), MoveAction(0, 2, 3)))
  }

  it should "move to alley and oppo" in {
    val from = Factory(0, 1, 6, 1, 0)
    val n1 = Factory(1, 1, 1, 1, 0)
    val n2 = Factory(2, -1, 2, 1, 0)
    val state = GhostCellGameState(itineraries = ShortestPath.shortestItinearies(3, Vector(
            Edge(0, 1, 1),
            Edge(0, 2, 1),
            Edge(1, 0, 1),
            Edge(1, 2, 1),
            Edge(2, 0, 1),
            Edge(2, 1, 1)
          )), factories = Vector(from, n1, n2), troops = Vector(Troop(3, -1, 2, 1, 4, 2)), bombs = Vector.empty)
    FactoryAnalysis.movePlans(state) should be(Vector(MoveAction(0, 1, 1), MoveAction(0, 2, 5)))
  }

  it should "find available cyborgs" in {
    val src = Factory(0, 1, 1, 1, 0)
    val n1 = Factory(1, 1, 2, 1, 0)
    val n2 = Factory(2, -1, 2, 1, 0)
    val state = GhostCellGameState(itineraries = ShortestPath.shortestItinearies(3, Vector(
            Edge(0, 1, 1),
            Edge(0, 2, 1),
            Edge(1, 0, 1),
            Edge(1, 2, 1),
            Edge(2, 0, 1),
            Edge(2, 1, 1)
          )), factories = Vector(src, n1, n2), troops = Vector(
            Troop(3, -1, 2, 0, 4, 1),
            Troop(4, 1, 1, 0, 1, 1)
          ), bombs = Vector.empty)
    FactoryAnalysis.available(src, state) should be(0)
  }

  it should "move enough cyborgs to target with multiple src" in {
    val src = Factory(0, 1, 0, 0, 0)
    val n1 = Factory(1, -1, 0, 1, 0)
    val n2 = Factory(2, 1, 100, 1, 0)
    val state = GhostCellGameState(itineraries = ShortestPath.shortestItinearies(3, Vector(
            Edge(0, 1, 6),
            Edge(0, 2, 1),
            Edge(1, 0, 6),
            Edge(1, 2, 4),
            Edge(2, 0, 1),
            Edge(2, 1, 4)
          )), factories = Vector(src, n1, n2), troops = Vector(Troop(226, 1, 2, 1, 1, 1), Troop(227, 1, 2, 1, 1, 2), Troop(228, 1, 2, 1, 1, 3), Troop(229, 1, 2, 1, 1, 4)), bombs = Vector.empty)
    FactoryAnalysis.movePlans(state) should be(Vector(MoveAction(2, 1, 2)))
  }

  it should "attack should be synchronized" in {
    val from = Factory(0, 1, 3, 1, 0)
    val n1 = Factory(1, 1, 2, 1, 0)
    val n2 = Factory(2, -1, 1, 1, 0)
    val state = GhostCellGameState(itineraries = ShortestPath.shortestItinearies(3, Vector(
            Edge(0, 1, 1),
            Edge(0, 2, 1),
            Edge(1, 0, 1),
            Edge(1, 2, 1),
            Edge(2, 0, 1),
            Edge(2, 1, 1)
          )), factories = Vector(from, n1, n2), troops = Vector.empty, bombs = Vector.empty)
    FactoryAnalysis.movePlans(state) should be(Vector(MoveAction(0, 2, 3), MoveAction(1, 2, 1)))
  }


  def createState(from: Factory, to: Factory): GhostCellGameState = {
    GhostCellGameState(itineraries = ShortestPath.shortestItinearies(2, Vector(Edge(0, 1, 1))), factories = Vector(from, to), troops = Vector.empty, bombs = Vector.empty)
  }
}
