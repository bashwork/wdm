@echo off
REM ------------------------------------------------------------ 
REM script variables
REM ------------------------------------------------------------ 
set JAVA=java
set INPUT=%1
set SUPPORT=%2

REM ------------------------------------------------------------ 
REM script runner
REM ------------------------------------------------------------ 
%JAVA% -cp jar\wdm.jar org.wdm.association.main.GeneralizedSequentialPatternMain -i %INPUT% -s %SUPPORT%
%JAVA% -cp jar\wdm.jar org.wdm.association.main.MsPrefixSpanMain -i %INPUT% -s %SUPPORT%
