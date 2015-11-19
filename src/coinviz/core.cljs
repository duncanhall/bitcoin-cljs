(ns ^:figwheel-always coinviz.core
  (:require
   [coinviz.transaction :as tx]
   [coinviz.connection :as cx]
   [coinviz.ui :as ui]
   [reagent.core :refer [atom]]))

(enable-console-print!)

(defonce formatters {"hash" tx/get-hash "size" tx/get-size "count" tx/count-io "total" tx/total-io})

(defonce app-state (atom {
                          :channel nil
                          :connected false
                          :transactions []
                          :output-fmt "hash"
                          :graph '{}
                          :connect! #()
                          :disconnect! #()}))

(defn add-transaction! [x]
  (swap! app-state
         (fn [s]
           (update s :transactions #(conj (if (>= (count %) 50) (subvec % 1) %) x)))))

(defn handle-input! [t]
  (when t
    (->> (tx/get-size t)
    (tx/update-size-graph app-state))))

(defn connect! []
  (cx/connect! app-state handle-input!))

(defn disconnect! []
  (cx/disconnect! app-state))

(defn on-js-reload []
  (disconnect!))

(swap! app-state assoc :connect! connect!)
(swap! app-state assoc :disconnect! disconnect!)

(ui/render app-state)

