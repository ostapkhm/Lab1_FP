package com.khomenko.ostap

import munit.{ScalaCheckSuite, Slow}
import org.scalacheck.{Arbitrary, Gen}
import org.scalacheck.Prop.*

import scala.util.Random
import MySet.*

class MySetTest extends ScalaCheckSuite {
  override val scalaCheckTestParameters = super.scalaCheckTestParameters.withMinSuccessfulTests(1000)

  given[A: Arbitrary]: Arbitrary[MySet[A]] = {
    Arbitrary(
      Gen
        .listOf(summon[Arbitrary[A]].arbitrary).map(_.toSet)
        .map(setOfA => toMySet(setOfA))
    )
  }

  property("makeSet should always construct a valid Set") {
    forAll { (xs: Array[Int]) =>
      toScalaSet(makeSet(xs:_*)) == Set(xs:_*)
    }
  }

  property("contains should always contain element passed in makeSet") {
    forAll { (xs: Array[Int]) =>
      (xs.length != 0) ==>
        contains(makeSet(xs:_*), Random.shuffle(xs.toList).head)
    }
  }

  property("insert should increase the size of Set by one if there was no such element otherwise no") {
    forAll { (xs: MySet[Int]) =>
      val prev_size = size(xs)

      forAll((el: Int) =>
        if (contains(xs, el)) size(insert(xs, el)) ==  prev_size
        else size(insert(xs, el)) == prev_size + 1)
    }
  }

  property("size of Set should be equal to the size of scala Set generated from the same Array"){
    forAll { (xs: Array[Int]) =>
      size(makeSet(xs:_*)) == Set(xs:_*).size
    }
  }

  property("remove should decrease the size of Set by one if such element was in Set otherwise no") {
    forAll { (xs: MySet[Int]) =>
      val prev_size = size(xs)

      forAll((el: Int) =>
        if (contains(xs, el)) size(remove(xs, el)) ==  prev_size - 1
        else size(remove(xs, el)) == prev_size)
    }
  }

  property("union of two sets should be equal to the union of two scala sets") {
    forAll { (set1: MySet[Int]) =>
      forAll { (set2: MySet[Int]) =>
        toScalaSet(union(set1, set2)) == toScalaSet(set1).union(toScalaSet(set2))
      }
    }
  }

  property("size of two intersected sets should always be smaller or equal the sum size of two sets "){
    forAll { (set1: MySet[Int]) =>
      forAll { (set2: MySet[Int]) =>
        size(intersection(set1, set2)) <= size(set1) + size(set2)
      }
    }
  }

  property("intersection of two sets should be equal to the intersection of two scala sets") {
    forAll { (set1: MySet[Int]) =>
      forAll { (set2: MySet[Int]) =>
        toScalaSet(intersection(set1, set2)) == toScalaSet(set1).intersect(toScalaSet(set2))
      }
    }
  }

  property("difference(set1, set2) should be equal to the  difference of two scala sets") {
    forAll { (set1: MySet[Int]) =>
      forAll { (set2: MySet[Int]) =>
        toScalaSet(difference(set1, set2)) == toScalaSet(set2).diff(toScalaSet(set1))
      }
    }
  }

  property("difference(set1, set2) should be equal to the  difference of two scala sets") {
    forAll { (set1: MySet[Int]) =>
      forAll { (set2: MySet[Double]) =>
        toScalaSet(difference(set1, set2)) == toScalaSet(set2).diff(toScalaSet(set1))
      }
    }
  }

  property("size of map(set, func) should be equal to the size map on scala set and this func") {
    // generating random function
    forAll {(func:Int => Boolean)=>
      forAll { (set: MySet[Int]) =>
        MySet.size(MySet.map(set, func)) == toScalaSet(set).map(func).size
      }
    }
  }

  property("size of flatMap(set, func) should be equal to the size flatMap on scala set and this func") {
    val func = ((x:Int) => makeSet(x, x + 1, x, x + 2))
    val set_func = ((x:Int) => Set(x, x + 1, x, x + 2))

    forAll { (set: MySet[Int]) =>
      MySet.size(MySet.flatMap(set, func)) == toScalaSet(set).flatMap(set_func).size
    }
  }
}
