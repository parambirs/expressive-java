# Java getting closer to Scala's expressiveness

Java will never be as expressive as Scala. Scala's feature set
allows for writing complete DSLs as well as concise, terse code.
That said, Java is getting closer. Since JDK 14, Java has had
preview support for "records" which are similar to Scala's case
classes. In future JDKs, records will have near feature parity
with Scala's case classes.

## The example

For me, the most expressive feature of Scala is its case classes
with pattern matching. Once you've coded with this feature, you'll
never want to return to the alternatives. This repo shows
Java's progress towards equalling Scala's case class feature.
It's unlikely that Java will ever completely match Scala, but it
will get close.

Shown here is a very old interpreter from the [Scala examples
website](https://www.scala-lang.org/old/node/56.html) rewritten
in Scala 3. It's not meant to be a definitive example. However, 
it's simple, fits on one page and shows the expressiveness of case 
classes.

The Java version takes advantage of records and Java's support 
for pattern matching. Record patterns are now available and the 
Java version is nearly identical to the Scala 2 version. Record
patterns are limited to type comparison and extraction and so
can't match on, for example, empty-list but that's about the only
feature missing. Java is still more verbose (the need for `var`
for example) but it's impressive how close it is now. Note that
I had to write a few static methods to eliminate the use of `new`.

However, Scala 3 has significantly improved by adopting the enum 
syntax for sealed hierarchies and eliminating the need for most braces. 
As a result, Scala is now even more visually appealing than before, 
resembling Python and Haskell. While functionally, Java appears to 
have matched Scala's case classes and pattern matching capabilities, 
its use of braces, operators `<` and `>` for generics, and general
verbosity makes it significantly more challenging to read compared 
to Scala.

# References

- https://github.com/Randgalt/expressive-java