all:
	mkdir temp
	javac -d temp -cp src src/MainHW4.java

run:
	java -cp temp: MainHW4