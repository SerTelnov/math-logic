all:
	mkdir temp
	javac -d temp -cp src src/MainHW1.java

run:
	java -cp temp: MainHW1