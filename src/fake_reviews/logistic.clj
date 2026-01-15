(ns fake-reviews.logistic)

(defn dot-product [a b]
  (reduce + (map * a b)))

(defn sigmoid [z]
  (/ 1.0 (+ 1.0 (Math/exp (- z)))))

(defn score [w x]
  (sigmoid (dot-product w x)))

(defn update-weights [w x y alpha]
  (let [p (score w x)
        err (- p y)]
    (mapv (fn [wi xi]
            (- wi (* alpha err xi)))
          w x)))

(defn train-epoch [w train alpha]
  (reduce
    (fn [w {:keys [x y]}]
      (update-weights w x y alpha))
    w
    train))

(defn train [train-data alpha epochs]
  (let [dim (count (:x (first train-data)))]
    (loop [w (vec (repeat dim 0.0))
           e 0]
      (if (>= e epochs)
        w
        (recur (train-epoch w train-data alpha)
               (inc e))))))

