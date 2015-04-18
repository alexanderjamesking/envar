(ns envar.core-test
  (:require [clojure.test :refer :all]
            [envar.core :refer :all]))

(deftest testing-envar
  (testing "environment variable that exists returned as string"
    (let [v (envar "ENVAR_TEST_STRING")]
      (is (= "hello world" v))
      (is (= java.lang.String (type v)))))

  (testing "missing environment variable results in exception"
    (is (thrown-with-msg? 
          java.lang.Exception 
          #"Missing required environment variable ENVAR_THAT_DOES_NOT_EXIST"
          (envar "ENVAR_THAT_DOES_NOT_EXIST"))))

  (testing "with converter function"
    (let [converter #(new java.util.Date (java.lang.Long/valueOf %))
          v (envar "ENVAR_TEST_JAVA_DATE" converter)]
      (is (= java.util.Date (type v)))
      (is (= 1429300824042 (.getTime v)))))

  (testing "with converter function and default"
    (let [converter #(new java.util.Date (java.lang.Long/valueOf %))
          v (envar "ENVAR_THAT_DOES_NOT_EXIST" converter 1429300824042)]
      (is (= java.util.Date (type v)))
      (is (= 1429300824042 (.getTime v)))))

  (testing "with converter and default (int)"
    (let [converter #(double (java.lang.Double/parseDouble %))
          v (envar "ENVAR_TEST_INT" converter)]
      (is (= java.lang.Double (type v)))
      (is (= 42.0 v))))

   (testing "with converter and default value and default-parser"
    (let [v (envar "ENVAR_THAT_DOES_NOT_EXIST" #(double (java.lang.Double/parseDouble %)) 42 double)]
      (is (= java.lang.Double (type v)))
      (is (= 42.0 v)))))


(defn same-type-and-value? [x y]
  (is (= (type x) (type y)))
  (is (== x y)))

(deftest testing-envar-int
  (testing "envar-int"
    (same-type-and-value? (int 42) (envar-int "ENVAR_TEST_INT")))

  (testing "envar-int-default"
    (same-type-and-value? (int 42) (envar-int "ENVAR_THAT_DOES_NOT_EXIST" 42)))

  (testing "int val results in exception"
    (is (thrown? java.lang.ClassCastException (envar-int "ENVAR_THAT_DOES_NOT_EXIST" "42")))))

(deftest testing-envar-num
  (testing "string val results in exception"
    (is (thrown? java.lang.ClassCastException (envar-num "ENVAR_THAT_DOES_NOT_EXIST" "42"))))

  (testing "envar-num-default"
    (same-type-and-value? (num 5) (envar-num "ENVAR_THAT_DOES_NOT_EXIST" 5))))

(deftest testing-envar-short
  (testing "string val results in exception"
    (is (thrown? java.lang.ClassCastException (envar-short "ENVAR_THAT_DOES_NOT_EXIST" "42"))))

  (testing "envar-num-default"
    (same-type-and-value? (short 5) (envar-short "ENVAR_THAT_DOES_NOT_EXIST" 5))))

(deftest testing-envar-bigint
  (testing "envar-bigint"
    (same-type-and-value? (bigint 42.00) (envar-bigint "ENVAR_TEST_INT")))

  (testing "envar-bigint-default"
    (same-type-and-value? (bigint 42.00) (envar-bigint "ENVAR_THAT_DOES_NOT_EXIST" 42)))

  (testing "envar-bigint-default-str"
    (same-type-and-value? (bigint 42.00) (envar-bigint "ENVAR_THAT_DOES_NOT_EXIST" "42"))))

(deftest testing-envar-bigdec
  (testing "envar-bigdec"
    (same-type-and-value? (bigdec 42.00) (envar-bigdec "ENVAR_TEST_INT")))

  (testing "envar-bigdec-default"
    (same-type-and-value? (bigdec 42.00) (envar-bigdec "ENVAR_THAT_DOES_NOT_EXIST" 42)))

  (testing "envar-bigdec-default-str"
    (same-type-and-value? (bigdec 42.00) (envar-bigdec "ENVAR_THAT_DOES_NOT_EXIST" "42"))))

(deftest testing-envar-double
  (testing "envar-long-default"
    (same-type-and-value? (double 2.00) (envar-double "ENVAR_THAT_DOES_NOT_EXIST" 2.00))))

(deftest testing-envar-float
  (testing "envar-float-default"
    (same-type-and-value? (float 5.678) (envar-float "ENVAR_THAT_DOES_NOT_EXIST" 5.678))))

(deftest testing-envar-long
  (testing "envar-long-default"
    (same-type-and-value? (long 5.67) (envar-long "ENVAR_THAT_DOES_NOT_EXIST" 5.67))))

