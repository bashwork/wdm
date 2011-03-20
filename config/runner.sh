#!/bin/bash
#------------------------------------------------------------ #
# script variables
#------------------------------------------------------------ #
INPUT="config/database.txt"
SUPPORT="config/supports.txt"

#------------------------------------------------------------ #
# script runner
#------------------------------------------------------------ #
java -cp jar/wdm.jar org.school.main.MsGeneralizedSequentialPatternMain -i ${INPUT} -s ${SUPPORT}
java -cp jar/wdm.jar org.school.main.MsPrefixSpanMain -i ${INPUT} -s ${SUPPORT}
