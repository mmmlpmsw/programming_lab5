#!/usr/bin/env bash
rm App.jar 2> /dev/null;
rm -r build 2> /dev/null;
mkdir build;
echo "Compiling...";
javac18 -cp lib/JSONlib.jar -sourcepath src -d build src/ifmo/programming/lab5/Main.java &&
(
  echo "Creating jar file...";
  jar cfm App.jar src/MANIFEST.MF -C build .
)