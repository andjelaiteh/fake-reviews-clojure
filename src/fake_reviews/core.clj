(ns fake-reviews.core
  (:require [fake-reviews.data-source :as ds]
            [fake-reviews.preprocess :as prep]
            [fake-reviews.analytics :as an]
            [fake-reviews.prepare-for-logistic :as prelog]
            [fake-reviews.logistic :as log]))

;;ucitavanje i sredjivanje csv-a

(def reviews
  (doall
    (map prep/prepare-reviews
         (prep/transform-rows-to-maps
           (ds/read-csv "fake_reviews_dataset.csv")))))

(def min-text-len (an/min-text-length reviews))
(def max-text-len (an/max-text-length reviews))

(def dataset-for-logistic (prelog/prepare-for-logistic reviews min-text-len max-text-len))
(def splits (prelog/split-60-40 dataset-for-logistic))

(def train-data (:train splits))
(def test-data  (:test splits))


(defn -main
  []
  ;(println "Total reviews:" (count reviews))
  ;(println "Total real:" (count (an/get-real-reviews reviews)))
  ;(println "Total fake:" (count (an/get-fake-reviews reviews)))
  ;(println "Average rating:" (an/average-rating reviews))
  ;(println "Average rating of real:" (an/average-rating-real reviews))
  ;(println "Average rating of fake:" (an/average-rating-fake reviews))
  ;(println "Average text length:" (an/average-text-length reviews))
  ;(println "Average text length of real:" (an/average-text-length-real reviews))
  ;(println "Average text length of fake:" (an/average-text-length-fake reviews))
  ;(println " Reviews by categories " (an/reviews-count-by-category reviews))
  ;(println " Real reviews by categories" (an/real-count-by-category reviews))
  ;(println " Fake reviews by categories" (an/fake-count-by-category reviews))
  ;(println "Top 3 categories with most real reviews" (an/top-real-categories reviews))
  ;(println "Top 3 categories with most fake reviews" (an/top-fake-categories reviews))
  ;(println "Average rating by category" (an/average-rating-by-category reviews))
  ;(println "Average rating of real reviews by category" (an/average-rating-real-by-category reviews))
  ;(println "Average rating of fake reviews by category" (an/average-rating-fake-by-category reviews))
  ;(println "Average text length by category " (an/average-text-len-by-category reviews))
  ;(println "Average text length of real reviews by category " (an/average-text-len-real-by-category reviews))
  ;(println "Average text length of fake reviews by category " (an/average-text-len-fake-by-category reviews))
  ;(println "Primer reda:" (select-keys (first reviews) [ :rating :text-len :label]));
  ;(println "Primer reda:" (select-keys (first reviews) [ :rating :text-len :label]))
  ;(println "Prvi red features dataseta "  (select-keys (first dataset-for-logistic) [:x :y]))
  ;(println "train:" (count train-data) "test:" (count test-data))
  ;(println "dotpr:"(log/sigmoid 0))
  (let [dim (count (:x (first train-data)))
        w0  (vec (repeat dim 0.0))
        w1  (log/train-epoch w0 train-data 0.001)]
    (println "w0 (pre treninga):" w0)
    (println "w1 (posle 1 epohe):" w1))

  (let [dim (count (:x (first train-data)))
        w0  (vec (repeat dim 0.0))
        w1  (log/train-epoch w0 train-data 0.001)]
    (println "x first:" (:x (first train-data)))
    (println "y first:" (:y (first train-data)))
    (println "score first:" (log/score w1 (:x (first train-data)))))

  (let [dim (count (:x (first train-data)))
        w0  (vec (repeat dim 0.0))
        w1  (log/train-epoch w0 train-data 0.001)
        w2  (log/train-epoch w1 train-data 0.001)]
    (println "w1:" w1)
    (println "w2:" w2))






  )
