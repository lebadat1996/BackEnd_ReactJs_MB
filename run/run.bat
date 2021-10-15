@echo off
echo Setting JAVA_HOME
set JAVA_HOME=C:\Program Files\java\jdk-11.0.2
echo setting PATH
set PATH=%JAVA_HOME%\bin;%PATH%
echo Display java version
java -version
java -jar e-name-card-0.0.1-SNAPSHOT.jar
pause