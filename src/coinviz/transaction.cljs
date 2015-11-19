(ns coinviz.transaction)

(def hash-precision 10)

(defn get-hash [tx]
  (-> tx
      (get-in ["x" "hash"])
      (subs 0 hash-precision)))

(defn total-io [tx]
  "Converts a transaction into HashMap of values {:h :i (total input) :o (total output)}"
  (let [dx (-> tx
               (get "x")
               (select-keys ["inputs" "out"]))
        ix (->> (get dx "inputs")
                (map #(get-in % ["prev_out" "value"]))
                (reduce +))
        ox (->> (get dx "out")
                (map #(get % "value"))
                (reduce +) )]
    {:h (get-hash tx) :i ix :o ox}))

(defn count-io [tx]
  (let [dx (-> tx
               (get "x")
               (select-keys ["inputs" "out"]))
        ix (->> (get dx "inputs")
                (map #(get-in % ["prev_out" "value"]))
                (count))
        ox (->> (get dx "out")
                (map #(get % "value"))
                (count) )]
    {:h (get-hash tx) :i ix :o ox}))

(defn get-size [tx]
  (let [s (get-in tx ["x" "size"])
        s (/ s 20)]
    (.ceil js/Math s)))

(defn get-size-graph [data]
  (let [g5 (map #(.ceil js/Math (/ % 10)) data)]
    (reduce #(assoc %1 %2 (inc (%1 %2 0))) {} g5)))

(defn update-size-graph [app-state ts]
  (swap! app-state update-in [:graph ts] inc))
