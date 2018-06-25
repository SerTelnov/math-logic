all:
	mkdir temp
	javac -d temp -cp src src/MainHW5.java

run:
	java -cp temp: MainHW5