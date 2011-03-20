#!/usr/bin/env sh
#------------------------------------------------------------ #
# A helper runner script for *nix systems
#------------------------------------------------------------ #
java -cp ../jar/wdm.jar org.school.main.GeneralizedSequentialPatternMain -i data.txt -s para.txt
java -cp ../jar/wdm.jar org.school.main.PrefixSpanMain -i data.txt -s para.txt
