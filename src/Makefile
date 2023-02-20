PROJECT = centralized_computing
CLASSPATH = /opt/sinalgo/binaries/bin:/opt/sinalgo/binaries/jdom.jar
SINALGO_DIRECTORY = /opt/sinalgo
SOURCES = $(shell find $(PROJECT) -name '*.java')
TARGET_DIRECTORY = $(SINALGO_DIRECTORY)/binaries/bin

compile: $(SOURCES)
	@echo "Compiling..."
	@javac -cp $(CLASSPATH) -d $(TARGET_DIRECTORY) $(SOURCES)

run: compile
	sinalgo -project centralized_computing
