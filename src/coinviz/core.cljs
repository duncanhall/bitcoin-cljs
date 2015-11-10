(ns ^:figwheel-always coinviz.core
  (:require
   [coinviz.transaction :as tx]
   [coinviz.connection :as cx]
   [coinviz.ui :as ui]
   [reagent.core :refer [atom]]))

(enable-console-print!)

(defonce app-state (atom {
                          :channel nil
                          :connected false
                          :transactions '()
                          :connect! #()
                          :disconnect! #()}))

(defn add-transaction! [t]
  (when t
    (swap! app-state update-in [:transactions] conj (tx/to-total-io t))))

(defn connect! []
  (cx/connect! app-state add-transaction!))

(defn disconnect! []
  (cx/disconnect! app-state))

(defn on-js-reload []
  (disconnect!))

(swap! app-state assoc :connect! connect!)
(swap! app-state assoc :disconnect! disconnect!)

(ui/render app-state)

