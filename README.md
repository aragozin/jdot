jdot
=========

[Graphviz](http://www.graphviz.org/) is an excelent open source tool for visualizing graph.
Graphviz is writter in C and distribute via platfrom specific binarires.

Native binaries is an issue for integrating to Java native toolset.

There were [some attempts](http://plantuml.sourceforge.net/jdot/jdot.html)
to port [Graphviz](http://www.graphviz.org/) to Java without much success so far.

Though, there is crosscompilation of [Graphviz](http://www.graphviz.org/) to JavaScript -
[viz.js](http://viz-js.com/).


[viz.js](http://viz-js.com/) can be executed with Java 8 built-in JavaScript engine. 
Packaged version is restricted to dot layout algorith and SVG output format.

[1.8.0](https://github.com/mdaines/viz.js/releases/tag/v1.8.0) version of [viz.js](http://viz-js.com/) is used.

This project packages [viz.js](http://viz-js.com/) into jar file together with thin wrapper to call it.


Resulting jar could be also executed from command line

	java -jar jdot.jar graph.dot -o graph.svg


Executing JavaScript in JVM is rather slow compared to native binary (or [viz.js](http://viz-js.com/) in browser).