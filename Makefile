BUILD=_build
CRAWLER=lib/crawler4j-4.1-jar-with-dependencies.jar
PGSQL=lib/postgresql-9.4.1207.jre6.jar
LOGGER=lib/slf4j-simple-1.7.13.jar
ASSERT=test/lib/hamcrest-core-1.3.jar
JUNIT=test/lib/junit-4.12.jar
JDBC=jdbc.drivers=org.postgresql.Driver
CLASSPATH=.:$(LOGGER):$(ASSERT):$(JUNIT):$(CRAWLER):$(PGSQL):$(BUILD)

SHORTSTACK=node test/support/shortstack.js

SRC= \
		 src/ir/assignments/three/db/*.java \
		 src/ir/assignments/three/*.java \
		 test/support/*.java \
		 test/src/*.java \

PKG= \
		 ir.test.CrawlerTest \
		 ir.test.PageRepoTest

default: test

migrate: compile
	@java -classpath $(CLASSPATH) -D$(JDBC) ir.assignments.three.db.Migrate

migrate-pristine: compile
	@java -classpath $(CLASSPATH) -D$(JDBC) ir.assignments.three.db.Migrate pristine

crawl: compile
	@java -classpath $(CLASSPATH) -D$(JDBC) \
		-Dorg.slf4j.simpleLogger.showDateTime=true \
		-Dorg.slf4j.simpleLogger.dateTimeFormat="yyyy-MM-dd'T'HH:mm:ss.SSSZ" \
		-Dpg_password=$(shell cat _private/prod_db_password.txt) \
	 	ir.assignments.three.Crawler

test: compile
	@java -classpath $(CLASSPATH) -D$(JDBC) \
		-Dpg_password=$(shell cat _private/test_db_password.txt) \
		org.junit.runner.JUnitCore $(PKG) | $(SHORTSTACK)

compile: clean
	@javac -g -d $(BUILD) -classpath $(CLASSPATH) $(SRC)

clean:
	@rm -rf $(BUILD)
	@mkdir -p $(BUILD)

tdd:
	watchy -w Makefile,src,test -- bash -c "clear; make test"

autotest: tdd

.PHONY: docs
