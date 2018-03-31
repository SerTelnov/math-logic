all:
	mkdir temp
	javac -d temp -cp src src/MainHW2.java

run:
	java -cp temp: MainHW2