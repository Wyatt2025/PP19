echo off


echo Setting Up Derby Classpath.

set CLASSPATH=lib\derby.jar;lib\derbytools.jar;. 

echo Creating Database(BooksDB) and Tables prepopulated with data.
java -Dij.database=jdbc:derby:BooksDB;create=true org.apache.derby.tools.ij sql\booksTables.sql > week14_jdbc.log

echo Database created...