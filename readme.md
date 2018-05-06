# Grepy

## Purpose

This project is a verison of the grep utility, written in java.

Grephy searches files for regular expression pattern matches, and produces dot graph file output for the automata used in the matching computation.

## Sample useage

To run the program, invoke it from the console:

`java -jar grepy.jar [-d dfa-file-name] [-n nfa-file-name] REGEX FILE`

An example call: `java -jar grepy.jar -d dfa_file.txt '(word)*isa(word)*' file4`

produces the output to the conole: `wordisaword`

And dfa_file.txt will contain: 
`w->o->r->d
 i->s->a
 w->o->r->d`



