all:
	mkdir temp
	javac -d temp -cp src src/MainHW6.java

run:
	java -cp temp: MainHW6