(ns fake-reviews.core
  (:require [clojure.java.io :as io]
            [clojure.data.csv :as csv]))

(defn read-csv [csvname]
  (with-open [r (io/reader (io/resource csvname))]
    (doall (csv/read-csv r))))

(defn transform-rows-to-maps [rows]
  (let [headers (map keyword (first rows))]
    (map #(zipmap headers %) (rest rows))))

(defn parse-rating [s]
  (try
    (Integer/parseInt (str/trim s))
    (catch Exception _ nil)))

(defn text-length [s]
  (if (string? s) (count s) 0))

(defn avg [xs]
  (let [xs (remove nil? xs)]
    (when (seq xs)
      (/ (reduce + xs) (double (count xs))))))


(defn -main
  [& args]
  (println "Citanje csv")
  (let [rows (read-csv "fake_reviews_dataset.csv")
        data (rows->maps rows)]
    (println "Ukupno redova:" (count data))
    (println "Kolone koje postoje:" (keys (first data)))
    (println "Label counts (fake/real):" (frequencies (map :label data)))
    (println "Primer reda:" (select-keys (first data) [:category :rating :label]))))

