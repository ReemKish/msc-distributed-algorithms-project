PROJECT = centralized_computing
SINALGO_DIRECTORY = /opt/sinalgo
CLASSPATH = $(SINALGO_DIRECTORY)/binaries/bin:$(SINALGO_DIRECTORY)/binaries/jdom.jar
SOURCES = $(shell find $(PROJECT) -name '*.java')
TARGET_DIRECTORY = $(SINALGO_DIRECTORY)/binaries/bin

compile: $(SOURCES)
	@echo "Compiling..."
	@javac -cp $(CLASSPATH) -d $(TARGET_DIRECTORY) $(SOURCES)

run: compile
	sinalgo -project centralized_computing -gen 10 centralized_computing:GraphNode centralized_computing:RandomDisc -rounds 1061
	# sinalgo -project centralized_computing -gen 10 centralized_computing:graphnode random -rounds 21
