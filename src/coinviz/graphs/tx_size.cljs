(ns coinviz.graphs.tx-size)

(defn get-size [tx]
  (let [s (get-in tx ["x" "size"])
        s (/ s 1)]
    (.ceil js/Math s)))

(defn update-graph [app-state tx]
  (let [s (get-size tx)]
    (swap! app-state update-in [:graph s] inc)))
