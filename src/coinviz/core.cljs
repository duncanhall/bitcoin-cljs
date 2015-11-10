(ns ^:figwheel-always coinviz.core
  (:require
   [coinviz.transaction :as tx]
   [coinviz.connection :as cx]
   [coinviz.ui :as ui]
   [reagent.core :as reagent :refer [atom]]
   [goog.dom :as dom]
   [chord.client :refer [ws-ch]]
   [cljs.core.async :refer [<! >! put! close! chan]])
  (:require-macros
   [cljs.core.async.macros :refer [go go-loop]]))

(enable-console-print!)

(defonce transactions (atom () ))
(defonce app-state (atom {:channel nil  :connected false :connect! #() :disconnect! #()}))

(defn add-transaction! [t]
  (when t
    (swap! transactions conj (tx/to-total-io t))))

(defn connect! []
  (cx/connect! app-state add-transaction!))

(defn disconnect! []
  (cx/disconnect! app-state))

(defn on-js-reload []
  (disconnect!))

(swap! app-state assoc :connect! connect!)
(swap! app-state assoc :disconnect! disconnect!)

(ui/render app-state transactions)



