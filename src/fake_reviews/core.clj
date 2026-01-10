(ns fake-reviews.core
  (:require [clojure.java.io :as io]
            [clojure.data.csv :as csv]
            [clojure.string :as str]))
;ucitavanje csv fajla
(defn read-csv [csvname]
  (with-open [r (io/reader (io/resource csvname))]
    (doall (csv/read-csv r))))

;transformacija redova u mape
(defn transform-rows-to-maps [rows]
  (let [headers (map keyword (first rows))]
    (map #(zipmap headers %) (rest rows))))

;parsiranje rating-a iz string u double
(defn parse-rating [s]
  (try
    (Double/parseDouble (str/trim s))
    (catch Exception _ nil)))

;racunanje duzine teksta recenzije
(defn text-length [s]
  (if (string? s) (count s) 0))


;racunanje srednje vrednosti
(defn avg [xs]
  (let [xs (remove nil? xs)]
    (when (seq xs)
      (/ (reduce + xs) (double (count xs))))))

;dodavanje atributa rating-num, text-lenght i label-name(real ili fake
(defn add-attributes [row]
  (assoc row
    :rating-num (parse-rating (:rating row))
    :text-len (text-length (:text row))
    :label-name (if (= (:label row) "1")
                           "fake"
                           "real")))

;zavrsno sredjivanje dataseta
(def all-reviews-processed
  (->> (read-csv "fake_reviews_dataset.csv")
       transform-rows-to-maps
       (map add-attributes)
       doall))

;ukupan broj recenzija
(def total-reviews-count
  (count all-reviews-processed))

;vraca sve fake
(defn get-fake-reviews [data]
  (filter #(= (:label-name %) "fake") data))

;broj svih fake recenzija
(def fake-reviews
  (get-fake-reviews all-reviews-processed))

;vraca sve real recenzije
(defn get-real-reviews [data]
  (filter #(= (:label-name %) "real") data))

;broj svih real
(def real-reviews
  (get-real-reviews all-reviews-processed))

;prosecan rating
(defn average-rating [data]
  (avg (map :rating-num data)))

;prosecan rating real recenzija
(def average-rating-real
  (avg (map :rating-num real-reviews)))

;prosecan rating fake recenzija
(def average-rating-fake
  (avg (map :rating-num fake-reviews)))

;prosecna duzina recenzija
(defn average-text-length [data]
  (avg (map :text-len data)))

;(apply min (map :text-len all-reviews-processed))
;(apply max (map :text-len all-reviews-processed))

;prosecna duzina teksta fake recenzija
(def average-text-length-fake
  (avg (map :text-len fake-reviews)))

;prosecna duzina teksta real recenzija
(def average-text-length-real
  (avg (map :text-len real-reviews)))

;sve kategorije
(def all-categories
  (distinct (map :category all-reviews-processed)))

;broj recenzija po kategoriji
(def reviews-count-by-category
  (frequencies (map :category all-reviews-processed)))

;broj fake recenzija po kategoriji
(def fake-count-by-category
  (frequencies (map :category fake-reviews)))

;broj real recenzija po kategoriji
(def real-count-by-category
  (frequencies (map :category real-reviews)))

;5 kategorija sa najvecim brojem fake
(def top-fake-categories
  (take 5 (sort-by val > fake-count-by-category)))

;5 kategorija sa najvecim brojem real
(def top-real-categories
  (take 5 (sort-by val > real-count-by-category)))


(defn -main []
  (println "top 5 fake" top-fake-categories)
  (println "top 5 real" top-real-categories)

)
