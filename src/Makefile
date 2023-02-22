PROJECT = centralized_computing
SINALGO_DIRECTORY = /opt/sinalgo
CLASSPATH = $(SINALGO_DIRECTORY)/binaries/bin:$(SINALGO_DIRECTORY)/binaries/jdom.jar
SOURCES = $(shell find $(PROJECT) -name '*.java')
TARGET_DIRECTORY = $(SINALGO_DIRECTORY)/binaries/bin

compile: $(SOURCES)
	@echo "Compiling..."
	@javac -cp $(CLASSPATH) -d $(TARGET_DIRECTORY) $(SOURCES)

run: compile
	sinalgo -project centralized_computing
