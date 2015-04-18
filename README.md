# envar

[![Build Status](https://travis-ci.org/alexanderjamesking/envar.svg?branch=master)](https://travis-ci.org/alexanderjamesking/envar)

## Running the tests

```bash
source ./set-test-environment-variables.sh
lein test
```

## Usage

Set up a lein project and add the envar dependency.

### REPL

Set environment variables:
```bash
export ENVAR_TEST_STRING="hello world"
export ENVAR_TEST_INT=42
export ENVAR_TEST_JAVA_DATE=1429300824042
```

Start the REPL 
```bash
lein repl
```

In the REPL:
```clojure
(require '[envar.core :refer :all])

; to a String
(envar-str "ENVAR_TEST_STRING")
; "hello world"

; if the environment variable is not set an exception is thrown
(envar-str "ENVAR_THAT_DOES_NOT_EXIST")
; Exception Missing required environment variable ENVAR_THAT_DOES_NOT_EXIST  
; envar.core/throw-exception (core.clj:4)

; providing a default
(envar-str "ENVAR_THAT_DOES_NOT_EXIST" "default hello message")
; "default hello message"

; to get an integer back
(envar-int "ENVAR_TEST_INT")
; 42

; with a default
(envar-int "ENVAR_THAT_DOES_NOT_EXIST" 42)
; 42

; or a double 
(envar-double "ENVAR_TEST_INT")
; 42.0

```
### Providing your own parser

```clojure
; providing your own parser to convert to a Java Date as inline function
(envar "ENVAR_TEST_JAVA_DATE" #(new java.util.Date (java.lang.Long/valueOf %)))
; returns java.util.Date 
; #inst "2015-04-17T20:00:24.042-00:00"

(defn date-parser [x] (new java.util.Date (java.lang.Long/valueOf x)))

; with a standard function and a default value
(envar "ENVAR_THAT_DOES_NOT_EXIST" date-parser 1429300824042)

; the parser will be run against both inputs (environment variable or default value)
; in some cases you will need to provide a different parser for the default value

(defn env-var-parser [x] (java.lang.Double/parseDouble x))
(defn default-parser [x] (double x))

(envar "ENVAR_THAT_DOES_NOT_EXIST" env-var-parser 42 default-parser)
; 42.0

; if the default is not provided an exception will be thrown as the env-var-parser is used
(envar "ENVAR_THAT_DOES_NOT_EXIST" env-var-parser 42)
; ClassCastException java.lang.Long cannot be cast to java.lang.String  user/env-var-parser 
; (form-init2356759060892620352.clj:1)
```

| function      | type                 |
| ------------- | -------------------- |
| envar-str     | java.lang.String     |
| envar-num     | java.lang.Number     |
| envar-short   | java.lang.Short      |
| envar-bigint  | clojure.lang.BigInt  |
| envar-int     | java.lang.Integer    | 
| envar-long    | java.lang.Long       |
| envar-bigdec  | java.math.BigDecimal | 
| envar-double  | java.lang.Double     |  
| envar-float   | java.lang.Float      |

## License

Copyright © 2015 Alexander James King

Distributed under the Eclipse Public License, the same as Clojure.
