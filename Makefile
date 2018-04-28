all:
	mkdir temp
	javac -d temp -cp src src/MainHW3.java

run:
	java -cp temp: MainHW3