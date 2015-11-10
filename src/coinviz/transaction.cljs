(ns coinviz.transaction)

(def hash-precision 11)

(defn trim-hash [raw]
  (subs raw 0 hash-precision))

(defn to-total-io [tx]
  "Converts a transaction into HashMap of values {:hash :i (total input) :o (total output)}"
  (let [dx (-> tx
               (get "x")
               (select-keys ["inputs" "out"]))
        ix (->> (get dx "inputs")
                (map #(get-in % ["prev_out" "value"]))
                (reduce +))
        ox (->> (get dx "out")
                (map #(get % "value"))
                (reduce +) )]
    {:hash (trim-hash (get-in tx ["x" "hash"])) :i ix :o ox}))

