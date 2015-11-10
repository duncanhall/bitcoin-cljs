(ns coinviz.ui
  (:require
   [reagent.core :as reagent]
   [goog.dom :as dom]))

(defn controls [app-state]
  (let [connected (:connected @app-state)]
    [:div.controls
     [:input {
              :type "button"
              :value (if connected "Disconnect" "Connect")
              :on-click (if connected (:disconnect! @app-state) (:connect! @app-state))}]]))

(defn output [transactions]
  [:div (str @transactions)])

(defn app-container [app-state transactions]
  [:div
   [:h1 "Realtime Bitcoin Info"]
   [controls app-state]
   [:br]
   [output transactions]])

(defn render [app-state transactions]
  (reagent/render-component [app-container app-state transactions] (dom/getElement "app")))

