javaPath:= src/Codes
classPath:= src/Classes

javaFiles := $(wildcard $(javaPath)/*.java)
classFiles:= $(patsubst $(javaPath)/%.java, $(classPath)/%.class, $(javaFiles))

#$(info "$(classFiles)")
.PHONY: default run clean jar

default: $(classFiles)
	
$(classFiles): $(classPath)%.class: $(javaPath)%.java
	javac $< -d $(classPath) -sourcepath $(javaPath) --release 8

run: default
	java -cp $(classPath) Driver
jar: default
	jar -cvmf manifest.txt TicTacToe4.jar -C src/Classes .
clean:
	rm $(classPath)/*.class
