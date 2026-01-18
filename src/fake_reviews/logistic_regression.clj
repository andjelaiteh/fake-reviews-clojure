(ns fake-reviews.logistic-regression)

;;parsiranje u double
(defn to-double [x]
  (try
    (Double/parseDouble (str x))
    (catch Exception _ 0.0)))

;;normaliyacija
(defn normalize [x min-x max-x]
  (if (= min-x max-x)
    0.0
    (/ (- x min-x)
       (- max-x min-x))))

;;podela na trening i test deo
(defn split-60-40 [dataset]
  (let [shuffled (vec (shuffle dataset))
        idx (int (* 0.6 (count shuffled)))]
    {:train (subvec shuffled 0 idx)
     :test  (subvec shuffled idx)}))

;;izracunavanje min i max vrednosti za ocenu i duzinu teksta
(defn min-text-len [train]
  (apply min (map #(to-double (:text-len %)) train)))

(defn max-text-len [train]
  (apply max (map #(to-double (:text-len %)) train)))

(defn min-rating [train]
  (apply min (map #(to-double (:rating-num %)) train)))

(defn max-rating [train]
  (apply max (map #(to-double (:rating-num %)) train)))

;;normalizacija i kreiranje vektora i formiranje vektora
(defn prepare-for-logistic [reviews min-text-len max-text-len min-rating max-rating]
  (mapv
    (fn [review]
      {:x [1.0
           (normalize (to-double (:text-len review)) min-text-len max-text-len)
           (normalize (to-double (:rating-num review)) min-rating max-rating)]
       :y (to-double (:label review))})
    reviews))

;;kona;na priprema za logisticku regresiju
(defn prepare-split [dataset]
  (let [{:keys [train test]} (split-60-40 dataset)
        min-tl (min-text-len train)
        max-tl (max-text-len train)
        min-r (min-rating train)
        max-r (max-rating train)
        train-data (prepare-for-logistic train min-tl max-tl min-r max-r)
        test-data  (prepare-for-logistic test  min-tl max-tl min-r max-r)]
    {:train-data train-data
     :test-data  test-data}))

;;racunanje skalarnog proizvoda
(defn dot-product [a b]
  (reduce + (map * a b)))

;;sigmoid funkcija
(defn sigmoid [z]
  (/ 1.0 (+ 1.0 (Math/exp (- z)))))

;;score
(defn score [w x]
  (sigmoid (dot-product w x)))

;;a\uriranje tezina
(defn update-weights [w x y alpha]
  (let [p   (score w x)
        err (- p y)]
    (mapv (fn [wi xi] (- wi (* alpha err xi))) w x)))

;;jedna epoha treniranja
(defn train-epoch [w train alpha]
  (reduce (fn [w {:keys [x y]}] (update-weights w x y alpha))
          w
          train))

;;trening
(defn train [train-data alpha epochs]
  (let [dim (count (:x (first train-data)))]
    (loop [w (vec (repeat dim 0.0))
           e 0]
      (if (>= e epochs)
        w
        (recur (train-epoch w train-data alpha)
               (inc e))))))

;;predikcija modela
(defn predict [w x threshold]
  (if (>= (score w x) threshold)
    1.0
    0.0))

;;računanje matrice konfuzije i metrika
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
  (if (zero? (+ tp fp)) 0.0
                        (double (/ tp (+ tp fp)))))

(defn recall [{:keys [tp fn]}]
  (if (zero? (+ tp fn)) 0.0
                        (double (/ tp (+ tp fn)))))

(defn f1 [cm]
  (let [p (precision cm)
        r (recall cm)]
    (if (zero? (+ p r))
      0.0
      (double (/ (* 2 p r) (+ p r))))))

(defn run-logistic-regression
  [dataset alpha epochs threshold]
  (let [{:keys [train-data test-data]} (prepare-split dataset)
        w (train train-data alpha epochs)
        preds (mapv (fn [{:keys [x y]}]
                      {:y-true y
                       :y-pred (predict w x threshold)})
                    test-data)
        cm (confusion-matrix preds)]
    {:cm cm
     :accuracy  (accuracy cm)
     :precision (precision cm)
     :recall    (recall cm)
     :f1        (f1 cm)
     :train-size (count train-data)
     :test-size  (count test-data)
     :total (+ (count train-data) (count test-data))}))
