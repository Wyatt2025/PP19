echo off


echo Setting Up Derby Classpath.

set CLASSPATH=lib\derby.jar;lib\derbytools.jar;. 

echo Compiling Java Program...
javac java\LibrarySystem.java -d .

echo Running Java Program...
java LibrarySystem MyTestDB

echo done