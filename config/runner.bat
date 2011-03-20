@echo off
REM ------------------------------------------------------------ 
REM script variables
REM ------------------------------------------------------------ 
set JAVA=java

REM ------------------------------------------------------------ 
REM script runner
REM ------------------------------------------------------------ 
%JAVA% -cp wdm.jar org.school.main.GeneralizedSequentialPatternMain -i %1 -s %2
%JAVA% -cp wdm.jar org.school.main.MsPrefixSpanMain -i %1 -s %2
