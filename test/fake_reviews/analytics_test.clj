(ns fake-reviews.analytics-test
  (:require [midje.sweet :refer :all]
            [fake-reviews.analytics :refer :all]))

;testovi za avg
(fact "avg calculates the average of numbers"
      (avg [3 5]) => 4.0
      (avg [5 5 5]) => 5.0)

(fact "avg ignores nil values"
      (avg [3 nil 5]) => 4.0)

(def test-reviews
  [;; Books: fake=2, real=1
   {:label-name "fake" :rating-num 5 :text-len 120 :category "Books"}
   {:label-name "fake" :rating-num 1 :text-len  80 :category "Books"}
   {:label-name "real" :rating-num 4 :text-len 200 :category "Books"}

   ; Electronics: fake=1, real=2
   {:label-name "fake" :rating-num 2 :text-len  60 :category "Electronics"}
   {:label-name "real" :rating-num 5 :text-len 150 :category "Electronics"}
   {:label-name "real" :rating-num 3 :text-len  90 :category "Electronics"}

   ;; Home: fake=3, real=0
   {:label-name "fake" :rating-num 4 :text-len 110 :category "Home"}
   {:label-name "fake" :rating-num 4 :text-len  70 :category "Home"}
   {:label-name "fake" :rating-num 2 :text-len  50 :category "Home"}

   ;; Toys: fake=0, real=2
   {:label-name "real" :rating-num 2 :text-len  40 :category "Toys"}
   {:label-name "real" :rating-num 4 :text-len 100 :category "Toys"}

   ;; Sports: fake=1, real=1
   {:label-name "fake" :rating-num 3 :text-len  55 :category "Sports"}
   {:label-name "real" :rating-num 1 :text-len  30 :category "Sports"}])

(fact "get-fake-reviews returns only fake reviews"
      (count (get-fake-reviews test-reviews)) => 7)

(fact "get-real-reviews returns only fake reviews"
      (count (get-real-reviews test-reviews)) => 6)

(fact "computes average rating across all reviews (avg=3.0769)"
      (average-rating test-reviews)
      => (roughly 3.0769 0.001))

(fact "computes average review text length (avg=88.846)"
      (average-text-length test-reviews)
      => (roughly 88.846 0.001))

(fact "number of reviews by category"
      (reviews-count-by-category test-reviews)
      => {"Books" 3
          "Electronics" 3
          "Home" 3
          "Toys" 2
          "Sports" 2})

(fact "returns top 3 categories by number of fake reviews"
      (count (top-fake-categories test-reviews)) => 3
      (some #{["Home" 3]} (top-fake-categories test-reviews)) => truthy
      (some #{["Books" 2]} (top-fake-categories test-reviews)) => truthy
      (some #{["Electronics" 1] ["Sports" 1]} (top-fake-categories test-reviews)) => truthy)

(fact "top-real-categories returns the three categories with the most real reviews"
      (count (top-real-categories test-reviews)) => 3
      (some #{["Electronics" 2] ["Toys" 2]} (top-real-categories test-reviews)) => truthy
      (some #{["Electronics" 2] ["Toys" 2]} (top-real-categories test-reviews)) => truthy
      (some #{["Books" 1] ["Sports" 1]} (top-real-categories test-reviews)) => truthy)

(fact "average-rating-by-category computes mean rating by category"
      (get (average-rating-by-category test-reviews) "Books") => (roughly 3.333 0.001)
      (get (average-rating-by-category test-reviews) "Electronics") => (roughly 3.333 0.001)
      (get (average-rating-by-category test-reviews) "Home") => (roughly 3.333 0.001)
      (get (average-rating-by-category test-reviews) "Toys") => (roughly 3.0 0.001)
      (get (average-rating-by-category test-reviews) "Sports") => (roughly 2.0 0.001))


(fact "average-text-len-by-category computes mean text length by category"
      (get (average-text-len-by-category test-reviews) "Books") => (roughly 133.333 0.001)
      (get (average-text-len-by-category test-reviews) "Electronics") => (roughly 100.0 0.001)
      (get (average-text-len-by-category test-reviews) "Home") => (roughly 76.667 0.001)
      (get (average-text-len-by-category test-reviews) "Toys") => (roughly 70.0 0.001)
      (get (average-text-len-by-category test-reviews) "Sports") => (roughly 42.5 0.001))

