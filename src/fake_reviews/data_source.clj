
(ns fake-reviews.data-source
  (:require [clojure.java.io :as io]
            [clojure.data.csv :as csv]))


;;ucitavanje csv fajla
(defn read-csv [csvname]
  (with-open [r (io/reader (io/resource csvname))]
    (doall (csv/read-csv r))))






