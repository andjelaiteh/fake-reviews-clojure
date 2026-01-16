(ns fake-reviews.metrics)

(defn confusion-matrix [preds]
  (reduce
    (fn [{:keys [tp fp tn fn] :as acc} {:keys [y-true y-pred]}]
      (cond
        (and (= y-true 1.0) (= y-pred 1.0)) (update acc :tp inc)
        (and (= y-true 0.0) (= y-pred 1.0)) (update acc :fp inc)
        (and (= y-true 0.0) (= y-pred 0.0)) (update acc :tn inc)
        (and (= y-true 1.0) (= y-pred 0.0)) (update acc :fn inc)
        :else acc))
    {:tp 0 :fp 0 :tn 0 :fn 0}
    preds))

(defn accuracy [{:keys [tp tn fp fn]}]
  (double (/ (+ tp tn) (+ tp tn fp fn))))

(defn precision [{:keys [tp fp]}]
  (if (zero? (+ tp fp))
    0.0
    (double (/ tp (+ tp fp)))))

(defn recall [{:keys [tp fn]}]
  (if (zero? (+ tp fn))
    0.0
    (double (/ tp (+ tp fn)))))

(defn f1 [{:keys [tp fp fn] :as cm}]
  (let [p (precision cm)
        r (recall cm)]
    (if (zero? (+ p r))
      0.0
      (/ (* 2 p r) (+ p r)))))
