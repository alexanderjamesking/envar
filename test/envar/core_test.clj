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
      (is (= 1429300824042 (.getTime v))))))
