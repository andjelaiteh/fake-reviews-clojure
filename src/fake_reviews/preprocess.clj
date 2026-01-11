(ns fake-reviews.preprocess
  (:require [clojure.string :as str]))

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

(defn prepare-category [category]
  (str/trim
    (str/replace category "_" " ")))

;dodavanje atributa rating-num, text-lenght i label-name(real ili fake
(defn prepare-reviews [row]
  (assoc row
    :rating-num (parse-rating (:rating row))
    :text-len (text-length (:text row))
    :label-name (if (= (:label row) "1")
                  "fake"
                  "real")
    :category (prepare-category (:category row))))








