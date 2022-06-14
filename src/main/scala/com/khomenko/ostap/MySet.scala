package com.khomenko.ostap

import scala.annotation.tailrec

enum MySet[+A]:
  case Empty
  case NonEmpty private[MySet](a: A, rest: MySet[A])


object MySet:
  def toMySet[A](xs: scala.collection.immutable.Set[A]): MySet[A] =
    xs.foldRight(Empty: MySet[A])((a, aSet) => NonEmpty(a, aSet))

  def toScalaSet[A](xs: MySet[A]): scala.collection.immutable.Set[A] =
    foldRight(xs, scala.collection.immutable.Set.empty)((x, z) => z + x)

  def makeSet[A](xs: A*): MySet[A] = {
    if (xs.isEmpty) Empty
    else xs.foldRight(Empty: MySet[A])((el, z) => insert(z, el))
  }

  def foldRight[A, B](set: MySet[A], z: B)(f: (A, B) => B): B =
    set match {
      case Empty => z
      case NonEmpty(x, xs) => f(x, foldRight(xs, z)(f))
    }

  @tailrec
  def foldLeft[A, B](set: MySet[A], z: B)(f: (B, A) => B): B =
    set match {
      case Empty => z
      case NonEmpty(h, t) => foldLeft(t, f(z, h))(f)
    }

  def filter[A](list: MySet[A])(f: A => Boolean): MySet[A] = {
    foldRight(list, Empty: MySet[A])((x, z) =>
      if (f(x)) NonEmpty(x, z) else z)
  }

  def toString[A](set: MySet[A]): String = {
    foldLeft(set, "")((z, x) =>
      if (z.isEmpty) z + x.toString
      else z + " " + x.toString)
  }

  def contains[A](set: MySet[A], el: A): Boolean = {
    foldLeft(set, false)((z, x) => if (x == el) true else z)
  }

  def insert[A](set: MySet[A], el: A): MySet[A] = {
    if (!contains(set, el)) {
      NonEmpty(el, set)
    }
    else set
  }

  def size(set: MySet[Any]): Int = {
    foldLeft(set, 0)((z, _) => z + 1)
  }

  def remove[A](set: MySet[A], el: A): MySet[A] = {
    if (contains(set, el)) {
      filter(set)(_ != el)
    }
    else set
  }

  def union[A](set1: MySet[A], set2: MySet[A]): MySet[A] = {
    set2 match {
      case Empty => set1
      case NonEmpty(a, rest) => foldRight(set1, set2)((x, z) =>
        if (contains(z, x)) z else NonEmpty(x, z))
    }
  }

  def intersection[A](set1: MySet[A], set2: MySet[A]): MySet[A] = {
    set2 match {
      case Empty => Empty
      case NonEmpty(a, rest) => foldRight(set1, Empty: MySet[A])((x, z) =>
        if (contains(set2, x)) NonEmpty(x, z) else z)
    }
  }

  def difference[A, B](set1: MySet[A], set2: MySet[B]): MySet[B] = {
    set1 match {
      case Empty => set2
      case NonEmpty(a, rest) => foldRight(set2, Empty: MySet[B])((x, z) =>
        if (contains(set1, x)) z else NonEmpty(x, z))
    }
  }

  def map[A, B](set: MySet[A], f: A => B): MySet[B] = {
    foldRight(set, Empty:MySet[B])((x, z) => NonEmpty(f(x), z))
  }

  def flatMap[A, B](set: MySet[A], f: A => MySet[B]) = {
    foldRight(set, Empty:MySet[B])((x, z) => foldRight(f(x), z)(NonEmpty(_, _)))
  }