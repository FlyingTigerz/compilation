#!/bin/bash
echo "========== Running Tests =========="
SEMANTIC_OUTPUT=./FOLDER_5_OUTPUT/SemanticStatus.txt
MIPS_OUTPUT=./FOLDER_5_OUTPUT/MIPS_OUTPUT.txt
MIPS=./FOLDER_5_OUTPUT/MIPS.txt
passed=0
total=0
for INPUT in ./FOLDER_4_INPUT/*
do
  rm $SEMANTIC_OUTPUT; touch $SEMANTIC_OUTPUT
  rm $MIPS; touch $MIPS
  if [ "$INPUT" = "./FOLDER_4_INPUT/Input.txt" ]; then
    continue
  fi
  TESTNAME=${INPUT/"./FOLDER_4_INPUT/"/}
  TESTNAME=${TESTNAME/.txt/}
  EXPECTED_OUTPUT=./FOLDER_6_EXPECTED_OUTPUT/${TESTNAME}_EXPECTED_OUTPUT.txt
  java -jar COMPILER ${INPUT} ${SEMANTIC_OUTPUT} >/dev/null 2>/dev/null
  spim -f $MIPS > $MIPS_OUTPUT 2>/dev/null
  ((total=total+1))
  diff -Z $MIPS_OUTPUT $EXPECTED_OUTPUT > /dev/null
  if test $? -eq 0; then
    echo -n "PASS..."
    ((passed=$passed+1))
  else
    echo -n "FAIL(!)"
  fi
  echo "....$TESTNAME"
done
echo Passed $passed/$total tests
