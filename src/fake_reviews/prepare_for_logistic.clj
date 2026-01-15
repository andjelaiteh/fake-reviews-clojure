(ns fake-reviews.prepare-for-logistic)

(defn to-double [x]
  (try
    (Double/parseDouble (str x))
    (catch Exception _
      0.0)))

;; Min–Max normalizacija
(defn normalize [x min-x max-x]
  (if (= min-x max-x)
    0.0
    (/ (- x min-x)
       (- max-x min-x))))

(defn prepare-for-logistic [reviews min-text-len max-text-len]
  (mapv
    (fn [review]
      {:x [1.0
           (normalize
             (to-double (:text-len review))
             min-text-len
             max-text-len)
           (to-double (:rating review))]
       :y (to-double (:label review))})
    reviews))

(defn split-60-40 [dataset]
  (let [shuffled (vec (shuffle dataset))
        idx (int (* 0.6 (count shuffled)))]
    {:train (subvec shuffled 0 idx)
     :test  (subvec shuffled idx)}))
