(ns fake-reviews.core
  (:require [fake-reviews.data-source :as ds]
            [fake-reviews.preprocess :as prep]
            [fake-reviews.analytics :as an]))

;;ucitavanje i sredjivanje csv-a
(def reviews
  (doall
    (map prep/prepare-reviews
         (prep/transform-rows-to-maps
           (ds/read-csv "fake_reviews_dataset.csv")))))


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
  (println "Average text length by category " (an/average-text-len-by-category reviews))
  (println "Average text length of real reviews by category " (an/average-text-len-real-by-category reviews))
  (println "Average text length of fake reviews by category " (an/average-text-len-fake-by-category reviews))

  )

