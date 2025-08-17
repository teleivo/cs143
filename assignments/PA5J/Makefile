
ASSN = 5J
CLASS= cs143
CLASSDIR= ../..

SRC= CgenClassTable.java CgenNode.java CgenSupport.java TreeConstants.java cool-tree.java IntSymbol.java StringSymbol.java BoolConst.java example.cl README
CSRC = \
	ASTConstants.java \
	ASTLexer.java \
	ASTParser.java \
	AbstractSymbol.java \
	AbstractTable.java \
	Flags.java \
	IdSymbol.java \
	IdTable.java \
	IntTable.java \
	ListNode.java \
	Cgen.java \
	StringTable.java \
	SymbolTable.java \
	TokenConstants.java \
	TreeNode.java \
	Utilities.java

TSRC= mycoolc cool-tree.aps
CGEN=
LIBS= lexer parser semant
CFIL= ${CSRC} ${CGEN} CgenClassTable.java CgenNode.java CgenSupport.java TreeConstants.java cool-tree.java IntSymbol.java StringSymbol.java BoolConst.java
HFIL=
LSRC= Makefile
CLS= ${CFIL:.java=.class}
OUTPUT= example.output

JAVAC := javac

# rt.jar yet again
CLASSPATH := ${CLASSDIR}/lib:.:/usr/java/lib/rt.jar

source: ${SRC} ${TSRC} ${LSRC} ${LIBS} ${CSRC}

${OUTPUT}: cgen
	@rm -f ${OUTPUT}
	./mycoolc  example.cl >example.output 2>&1

cgen: Makefile ${CLS}
	@rm -f cgen
	echo '#!/bin/sh' >> cgen
	echo 'SCRIPT_DIR=`dirname $$0`' >> cgen
	echo 'java -classpath ${CLASSPATH}:$$SCRIPT_DIR Cgen $$*' >> cgen
	chmod 755 cgen


dotest:	cgen example.cl
	@echo "\nRunning code generator on example.cl\n"
	-./mycoolc example.cl

## cool-tree.class is not a real class file, but we need to have it
## for dependency tracking
${CLS}: ${CFIL}
	${JAVAC} -g -nowarn -classpath ${CLASSPATH} ${CFIL}
	touch cool-tree.class

${LIBS}:
	${CLASSDIR}/etc/link-object ${ASSN} $@

${TSRC} ${CSRC}:
	-ln -s ${CLASSDIR}/src/PA${ASSN}/$@ $@

${HSRC}:
	-ln -s ${CLASSDIR}/include/PA${ASSN}/$@ $@

clean :
	-rm -f ${OUTPUT} *.s core ${CLS} ${CGEN} parser cgen semant lexer *~




