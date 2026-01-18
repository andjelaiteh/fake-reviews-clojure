(ns fake-reviews.core
  (:require [fake-reviews.data-source :as ds]
            [fake-reviews.preprocess :as prep]
            [fake-reviews.analytics :as an]
            [fake-reviews.logistic-regression :as lr]))

(def reviews
  (doall
    (map prep/prepare-reviews
         (prep/transform-rows-to-maps
           (ds/read-csv "fake_reviews_dataset.csv")))))

(defn -main []
  (println "---------------------------------------------- ANALYTICS -------------------------------------------------------------")
  (println "Total reviews:" (count reviews))
  (println "Total real:" (count (an/get-real-reviews reviews)))
  (println "Total fake:" (count (an/get-fake-reviews reviews)))
  (println "Average rating:" (an/average-rating reviews))
  (println "Average rating of real:" (an/average-rating-real reviews))
  (println "Average rating of fake:" (an/average-rating-fake reviews))
  (println "Average text length:" (an/average-text-length reviews))
  (println "Average text length of real:" (an/average-text-length-real reviews))
  (println "Average text length of fake:" (an/average-text-length-fake reviews))
  ;(println " Reviews by categories " (an/reviews-count-by-category reviews))
  ;(println " Real reviews by categories" (an/real-count-by-category reviews))
  ;(println " Fake reviews by categories" (an/fake-count-by-category reviews))
  (println "Top 3 categories with most real reviews" (an/top-real-categories reviews))
  (println "Top 3 categories with most fake reviews" (an/top-fake-categories reviews))
  ;(println "Average rating by category" (an/average-rating-by-category reviews))
  ;(println "Average rating of real reviews by category" (an/average-rating-real-by-category reviews))
  ;(println "Average rating of fake reviews by category" (an/average-rating-fake-by-category reviews))
  ;(println "Average text length by category " (an/average-text-len-by-category reviews))
  ;(println "Average text length of real reviews by category " (an/average-text-len-real-by-category reviews))
  ;(println "Average text length of fake reviews by category " (an/average-text-len-fake-by-category reviews))

  (let [alpha     0.001
        epochs    10
        threshold 0.5
        result (lr/run-logistic-regression reviews alpha epochs threshold)
        cm   (:cm result)
        acc  (get-in result [:metrics :accuracy])
        prec (get-in result [:metrics :precision])
        rec  (get-in result [:metrics :recall])
        f1   (get-in result [:metrics :f1])]

    (println "------------------------------------------- LOGISTIC REGRESSION RESULTS ----------------------------------------------------------")
    (println "Total samples:" (:total result))
    (println "Training samples:" (:train-size result))
    (println "Test samples:" (:test-size result))
    (println "Learning rate (alpha):" alpha)
    (println "Epochs:" epochs)
    (println "Threshold:" threshold)
    (println)

    (println "Confusion matrix:")
    (println "TP =" (:tp cm) " FP =" (:fp cm))
    (println "TN =" (:tn cm) " FN =" (:fn cm))
    (println)

    (println "Accuracy: " (:accuracy result))
    (println "Precision:" (:precision result))
    (println "Recall:   " (:recall result))
    (println "F1 score: " (:f1 result))
    (println "-------------------------------------------------------------------------------------------------------------------------------------------")))







