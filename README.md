#YAMLLipter
---
YAMLLipter is a simple YAMLite parser.
It can valide a YAMLite file or parse a well-formatted YAMLite file to json foramt.

#Usage
---
~~~
usage: yamlite [option [value]] file
option:
           -parse : parse YAMLite, print 'valid' if there's no syntax error, report error otherwise
            -json : parse YAMLite to json format into a file with same name except its extension name is .json,
                    report error otherwise
       -find path : find the element specified by path and print it in json format. Print null if there's no such element,
                    report error otherwise
~~~