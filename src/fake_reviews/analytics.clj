(ns fake-reviews.analytics)

;;racunanje srednje vrednosti
(defn avg [data]
  (let [data (remove nil? data)]
    (when (seq data)
      (/ (reduce + data) (double (count data))))))

;;vraca sve fake
(defn get-fake-reviews [data]
  (filter #(= (:label-name %) "fake") data))

;;vraca sve real recenzije
(defn get-real-reviews [data]
  (filter #(= (:label-name %) "real") data))

;;prosecna ocena
(defn average-rating [data]
  (avg (map :rating-num data)))

;;prosecna ocena real recenzija
(defn average-rating-real [data]
  (average-rating (get-real-reviews data)))

;;prosecna ocena fake recenzija
(defn average-rating-fake [data]
  (average-rating (get-fake-reviews data)))

;;prosecna duzina recenzija
(defn average-text-length [data]
  (avg (map :text-len data)))

;(apply min (map :text-len all-reviews-processed))
;(apply max (map :text-len all-reviews-processed))

;prosecna duzina teksta fake recenzija
(defn average-text-length-fake [data]
  (average-text-length (get-fake-reviews data)))

;prosecna duzina teksta real recenzija
(defn average-text-length-real [data]
  (average-text-length (get-real-reviews data)))

;broj recenzija po kategoriji
(defn reviews-count-by-category [data]
  (frequencies (map :category data)))

;broj fake recenzija po kategoriji
(defn fake-count-by-category [data]
  (reviews-count-by-category (get-fake-reviews data)))

;broj real recenzija po kategoriji
(defn real-count-by-category [data]
  (reviews-count-by-category (get-real-reviews data)))

;;top 3 fake kategorije
(defn top-fake-categories [data]
  (take 3 (sort-by val > (fake-count-by-category data))))

;;top 3 real kategorije
(defn top-real-categories [data]
  (take 3 (sort-by val > (real-count-by-category data))))

;;(defn average-rating-by-category [data]
;;(into {} (for [[cat reviews] (group-by :category data)] [cat (avg (map :rating-num reviews))])))

;;prosecna ocena po kategorijama
(defn average-rating-by-category [data]
  (let [sums
        (reduce
          (fn [acc review]
            (let [cat (:category review)
                  rating (:rating-num review)
                  {:keys [sum count]} (get acc cat {:sum 0 :count 0})]
              (assoc acc cat
                         {:sum (+ sum rating)
                          :count (inc count)})))
          {}
          data)]
    (into {}
          (map (fn [[cat {:keys [sum count]}]]
                 [cat (/ sum count)])
               sums))))

;;prosecna ocena fake recenzija po kategorijama
(defn average-rating-fake-by-category [data]
  (average-rating-by-category (get-fake-reviews data)))

;;prosecna ocena real recenzija po kategorijama
(defn average-rating-real-by-category [data]
  (average-rating-by-category (get-real-reviews data)))

;;prosecna duzina teksta recenzije po kategorijama
(defn average-text-len-by-category [data]
  (let [sums
        (reduce
          (fn [acc review]
            (let [cat (:category review)
                  text-len (:text-len review)
                  {:keys [sum count]} (get acc cat {:sum 0 :count 0})]
              (assoc acc cat
                         {:sum (+ sum text-len)
                          :count (inc count)})))
          {}
          data)]
    (into {}
          (map (fn [[cat {:keys [sum count]}]]
                 [cat (/ (double sum) count)])
               sums))))

;;prosecna duzina teksta real recenzija po kategorijama
(defn average-text-len-real-by-category [data]
  (average-text-len-by-category (get-real-reviews data)))

;;prosecna duzina teksta fake recenzija po kategorijama
(defn average-text-len-fake-by-category [data]
  (average-text-len-by-category (get-fake-reviews data)))
