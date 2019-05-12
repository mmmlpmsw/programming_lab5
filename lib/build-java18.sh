#!/usr/bin/env bash
rm JSONlib1.jar 2> /dev/null;
rm -r build 2> /dev/null;
mkdir build;
echo "Compiling...";
javac18 -sourcepath src/com/company/lib -d build src/com/company/lib/Main.java &&
(
  echo "Creating jar file...";
  jar cfm JSONlib1.jar src/MANIFEST.MF -C build .
)