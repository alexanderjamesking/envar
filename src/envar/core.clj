(ns envar.core)

(defn- throw-exception [var-key]
  (throw (Exception. (str "Missing required environment variable " var-key))))

(defn envar 
  ([var-key fn-envar-parser]
    (if-let [var-value (System/getenv var-key)]
      (fn-envar-parser var-value) 
      (throw-exception var-key)))
  ([var-key fn-envar-parser var-default]
    (if-let [var-value (System/getenv var-key)]
      (fn-envar-parser var-value)
      (fn-envar-parser var-default)))
  ([var-key fn-envar-parser var-default fn-default-parser]
    (if-let [var-value (System/getenv var-key)]
      (fn-envar-parser var-value)
      (fn-default-parser var-default))))

(defn envar-fn
  [fn-envar-parser fn-default-parser var-key & [var-default]]
    (if var-default
      (envar var-key fn-envar-parser var-default fn-default-parser)
      (envar var-key fn-envar-parser)))

(defn envar-str [& args]
  (apply envar-fn str str args))

(defn envar-num [& args]
  (apply envar-fn num num args))

(defn envar-short [& args]
  (apply envar-fn short short args))

(defn envar-bigint [& args]
  (apply envar-fn bigint bigint args))

(defn envar-int [& args]
  (apply envar-fn #(java.lang.Integer/valueOf %) int args))

(defn envar-long [& args]
  (apply envar-fn #(java.lang.Long/parseLong %) long args))

(defn envar-bigdec [& args]
  (apply envar-fn #(new java.math.BigDecimal %) bigdec args))

(defn envar-double [& args]
  (apply envar-fn #(java.lang.Double/parseDouble %) double args))

(defn envar-float [& args]
  (apply envar-fn #(java.lang.Float/parseFloat %) float args))
