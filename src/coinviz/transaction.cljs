(ns coinviz.transaction)

(def hash-precision 11)

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
