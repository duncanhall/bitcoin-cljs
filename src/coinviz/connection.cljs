(ns coinviz.connection
  (:require
   [chord.client :refer [ws-ch]]
   [cljs.core.async :refer [<! >! put! close! chan]])
  (:require-macros
   [cljs.core.async.macros :refer [go go-loop]]))

(defn subscribe [channel rx-transaction]
  (go-loop []
    (let [{:keys [message error] :as msg} (<! channel)]
      (rx-transaction  message)
      (when message (recur))))
  (go
    (>! channel {:op "unconfirmed_sub"})))

(defn connect! [app-state rx-transaction]
  (go
    (let [{:keys [ws-channel error]} (<! (ws-ch "wss://ws.blockchain.info/inv" {:format :json}))]
      (if error
        (println (pr-str error))
        ((swap! app-state assoc
                :channel ws-channel
                :connected true)
         (subscribe ws-channel rx-transaction))))))

(defn disconnect! [app-state]
  (when (:channel @app-state)
    (go
      (close! (:channel @app-state))
      (swap! app-state assoc :channel nil )
      (swap! app-state assoc :connected false))))
