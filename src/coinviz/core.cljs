(ns ^:figwheel-always coinviz.core
  (:require
   [coinviz.transaction :as tx]
   [coinviz.connection :as cx]
   [coinviz.ui :as ui]
   [reagent.core :refer [atom]]))

(enable-console-print!)

(defonce formatters {"hash" tx/get-hash "count" tx/count-io "total" tx/total-io})

(defonce app-state (atom {
                          :channel nil
                          :connected false
                          :transactions '()
                          :output-fmt "hash"
                          :connect! #()
                          :disconnect! #()}))

(defn add-transaction! [t]
  (when t
    (swap! app-state update-in [:transactions] conj
           ((get formatters (:output-fmt @app-state)) t))))

(defn connect! []
  (cx/connect! app-state add-transaction!))

(defn disconnect! []
  (cx/disconnect! app-state))

(defn on-js-reload []
  (disconnect!))

(swap! app-state assoc :connect! connect!)
(swap! app-state assoc :disconnect! disconnect!)

(ui/render app-state)

