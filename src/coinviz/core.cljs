(ns ^:figwheel-always coinviz.core
  (:require
   [reagent.core :as reagent :refer [atom]]
   [goog.dom :as dom]
   [chord.client :refer [ws-ch]]
   [cljs.core.async :refer [<! >! put! close! chan]])
  (:require-macros
   [cljs.core.async.macros :refer [go go-loop]]))

(enable-console-print!)

(defonce app-state (atom {
                          :channel nil
                          :connected false
                          :transactions []}))

(defn add-transaction! [tx]
  (when tx
    (let [dx (-> tx
                 (get "x")
                 (select-keys ["inputs" "out"]))
          ix (->> (get dx "inputs")
                  (map #(get-in % ["prev_out" "value"]))
                  (reduce +)) 
          ox (->> (get dx "out")
                  (map #(get % "value"))
                  (reduce +) )]
      (println (get-in tx ["x" "hash"])  [ ix ox]  ))))

(defn subscribe [channel]
  (go-loop []
    (let [{:keys [message error] :as msg} (<! channel)]
      (add-transaction!  message)
      (when message (recur))))
  (go
    (>! channel {:op "unconfirmed_sub"})))

(defn connect! []
  (go
    (let [{:keys [ws-channel error]} (<! (ws-ch "wss://ws.blockchain.info/inv" {:format :json}))]
      (if error
        (println (pr-str error))
        ((swap! app-state assoc
                :channel ws-channel
                :connected true)
         (subscribe ws-channel))))))

(defn disconnect! []
  (go
    (close! (:channel @app-state))
    (swap! app-state assoc :connected false)))

(defn controls []
  (let [connected (:connected @app-state)]
    [:div.controls
     [:input {
            :type "button"
            :value (if connected "Disconnect" "Connect")
            :on-click (if connected disconnect! connect!)}]]))

(defn app-container []
  [:div
   [:h1 "Realtime Blockchain"]
   [controls]])

(reagent/render-component [app-container]  (dom/getElement "app"))

(defn on-js-reload []
  (println "Page reloaded")
  (swap! app-state assoc :connected false))


