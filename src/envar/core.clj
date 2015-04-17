(ns envar.core)

(defn- throw-exception [var-key]
  (throw (Exception. (str "Missing required environment variable " var-key))))

(defn envar 
  ([var-key] 
    (if-let [var-value (System/getenv var-key)] 
      var-value 
      (throw-exception var-key)))
  ([var-key converting-fn]
    (if-let [var-value (System/getenv var-key)]
      (converting-fn var-value) 
      (throw-exception var-key)))
  ([var-key converting-fn var-default]
    (if-let [var-value (System/getenv var-key)]
      (converting-fn var-value)
      (converting-fn var-default))))
