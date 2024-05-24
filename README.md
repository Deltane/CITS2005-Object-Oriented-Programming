│   └── StudentStats.java
└── test/...
Each directory (itertools, studentapi, etc.) is a Java package of the same name. There are 9 programming tasks spread between itertools and studentstats. You should complete all tasks. You are advised to read all tasks before beginning implementation. You are advised but not required to implement tasks in the order they are given below.
Tasks are labelled with comments of the form `// TASK(N): Implement ...` throughout the code. Note (2024-05-13): You are strongly encouraged to read all the provided code in `itertools`, `studentapi`, and `studentstats`, as this will aid your understanding.
For each task, details are provided in the documentation comments in the code, as well as below:

itertools
Iterators are a powerful tool in Object Oriented Programming. Java already provides a simple iterator interface (java.util.Iterator), but it is possible to make much more powerful and versatile iterators. The itertools package is a library of tools for working with iterators. You have been asked to implement a number of methods within itertools. You are permitted and expected to add your own new .java source files within itertools as appropriate.
The itertools library provides the `DoubleEndedIterator<T>` interface, which represents an iterator that can be consumed either from the front or the back, and the `RangeIterator` class, as an example implementation of `DoubleEndedIterator`.
A number of methods in this package use interfaces from Java's java.util.function package, which represents different kinds of mathematical functions as objects so we can pass them as arguments to methods. Example implementations of these interfaces are included in the template source and tests.

TASK(1): Implement take
See src/itertools/Itertools.java
Given an iterator and a number of elements, the `take` method returns an iterator over that number of elements taken from the iterator (or as many as it contains, if less than that number).
Elements should be consumed from the given iterator only as needed.
This concept is called "laziness", in that it does not perform computation until it is required. In the case of iterators, this means that you should not simply copy elements into a data structure and then provide an iterator over that data structure, as this would pull a number of elements from the given iterator that may never be required.

TASK(2): Implement reversed
See src/itertools/Itertools.java
The `reversed` method returns a (double ended) iterator in the reverse order of the one given.
Elements should be consumed from the given iterator only as needed.
Note (2024-05-21): The documentation comments for this method say to return a "(double ended) iterator", but the signature only requires an `Iterator<T>`. You may assume that the documentation is a typo. There is no need to change the function signature. I originally intended to require a `DoubleEndedIterator<T>`, but apparently forgot to change the return type. Therefore implementing an `Iterator<T>` is sufficient to pass the tests and get the mark. There is no need to implement a `DoubleEndedIterator<T>`, though you are welcome to do so, so long as the tests still pass.

TASK(3): Implement filter
See src/itertools/Itertools.java
The `filter` method returns an iterator over only the elements of a given iterator that satisfy a given predicate. A predicate is a function used to determine if a particular property holds for an item. An example predicate could be "this integer is even", for which 4 would satisfy the predicate but 7 would not.
Elements should be consumed from the given iterator only as needed (though it may be necessary to consume elements to determine whether there is a next element that satisfies the predicate).
Java's `java.util.function.Predicate` interface can be used by calling `pred.test(x)`, and will return `true` if and only if `x` satisfies the predicate.

TASK(4): Implement map (single ended)
See src/itertools/Itertools.java
The `map` method returns an iterator over the elements of a given iterator with a given function applied to each element.
That is, given a function `f` and an iterator over the elements `a, b, c, ...`, returns an iterator over `f(a), f(b), f(c), ...`.
This allows us to "transform" an iterator, applying a function to each element as it is retrieved, rather than having to consume the iterator, transforming and storing each element, and then iterating over the stored collection.
Elements should be consumed from the given iterator only as needed.
Java's `java.util.function.Function` interface can be used by calling `f.apply(x)` and will return `f(x)`.

TASK(5): Implement map (double ended)
See src/itertools/Itertools.java
Implement a double ended version of `map`.

TASK(6): Implement zip
See src/itertools/Itertools.java
The `zip` method returns an iterator over the results of combining each pair of elements from a pair of given iterators using a given function.
That is, given a function `f` and iterators over the elements `a, b, c, ...` and `x, y, z, ...` returns an iterator over `f(a, x), f(b, y), f(c, z), ...`.
The iterator ends when either input iterator ends.
Elements should be consumed from the given iterators only as needed.
Java's `java.util.function.BiFunction` interface can be used by calling `f.apply(x, y)` and will return `f(x, y)`.

TASK(7): Implement reduce
See src/itertools/Itertools.java
The `reduce` method returns the result of combining all the elements from the given iterator using the given function.
Each element is combined with the current value using the given function.
For example, given a function `f`, an initial value `x`, and an iterator over the elements `a, b, c`, returns `f(f(f(x, a), b), c)`.
An example of a common reduction would be "sum", where we reduce an iterator over integers using the addition function to compute the sum of every element in the iterator.
Java's `java.util.function.BiFunction` interface can be used by calling `f.apply(x, y)` and will return `f(x, y)`.

studentapi
There is no reason to modify or add any code in this package.
The studentapi package contains no tasks, but rather provides interfaces to a simulated API that you will be working with in the next section. You are advised to read the documentation comments for the interfaces in this package carefully.
Notably, the student list API is paginated, returning not a single student at a time, but a short list of students. Pagination is a common technique in online APIs to reduce the number of API calls needed to retrieve the whole list, while not making the result of any one API call too large.
Also, the `getPage()` API call is unreliable, and may sometimes time out before successfully completing, throwing a `QueryTimedOutException` to indicate as such.

studentstats
The studentstats package represents a hypothetical software tool we are building to compute some basic statistics about student records, such as the average mark for a unit or the most recently enrolled students at the university who have completed a particular unit.
We would like to be able to use tools from the itertools library to implement these methods elegantly. To that end we will need to write an iterator over the list of students retrieved from the studentapi.

TASK(8): Implement StudentListIterator
See src/studentstats/StudentListIterator.java
Implement a `DoubleEndedIterator` over the list of student records pulled from the student API.
Since calls to `getPage()` may fail with a `QueryTimedOutException`, your implementation should retry the connection in case it was just a momentary failure. A retry quota is given when constructing the iterator. If the API is still not reachable after exceeding the retry quota, you should raise an `ApiUnreachableException`.
The iterator should not simply load the entire list and then iterate over it, as if we need to access only a prefix or suffix of the list, this would be extremely inefficient.

TASK(9): Implement unitNewestStudents
See src/studentstats/StudentStats.java
Finally, we can use the `StudentListIterator` you have just implemented to write methods for computing some useful statistics. An example method to compute the average mark for a unit is already implemented for you. You are asked to implement the `unitNewestStudents` method.
The `unitNewestStudents` method returns an iterator over the students who have taken a given unit, from newest to oldest. Student IDs are assigned in strictly increasing order as students enrol, and the student API lists student records in order from oldest to newest student ID.
You should implement this method using the tools you have written for the itertools package. You are permitted to write additional helper classes inside studentstats.java.
