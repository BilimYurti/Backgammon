#!/bin/bash

if [ -d ./bin ]
  then
    rm -r bin/
fi
mkdir bin/
javac -d ./bin/ -cp src/main/java/ src/main/java/gui/Main.java
cp -r ./src/main/java/gui/fxml ./bin/gui/
cp -r ./src/main/java/gui/images ./bin/gui/
if [ -f ./run.sh ]
  then
    rm -r run.sh
fi

echo "#!/bin/bash" > run.sh
echo "cd bin/" >> run.sh
echo "java gui.Main" >> run.sh
chmod a+x run.sh

if [ -f ./jarMaker.sh ]
  then 
    rm -r jarMaker.sh
fi

echo "#!/bin/bash" > jarMaker.sh
echo "cd bin" >> jarMaker.sh
echo "echo "Main-Class: gui.Main" > manifest.txt" >>jarMaker.sh
echo "jar cfm ../Backgammon.jar manifest.txt gui/ game/ states/" >>jarMaker.sh
echo "rm manifest.txt" >>jarMaker.sh
chmod a+x jarMaker.sh
