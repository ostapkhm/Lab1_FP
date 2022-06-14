# Lab1_FP
First functional programming lab


The task was to write Set class with such methods:

1.union(a: Set[A], b: Set[A]): Set[A]<br />
2.intersection(a: Set[A], b: Set[A]): Set[A]<br />
3.difference(a: Set[A], b: Set[B]): Set[B]<br />
4.remove(s: Set[A], a: A): Set[A]<br />

Set was renamed to MySet 

The additional task was to write map and flatMap, namely:
1.map[A, B](set: MySet[A], f: A => B): MySet[B]<br />
2.flatMap[A, B](set: MySet[A], f: A => MySet[B]): MySet[B]<br />