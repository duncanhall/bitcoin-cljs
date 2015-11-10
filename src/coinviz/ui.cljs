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

(defn output [app-state]
  [:div (str (:transactions @app-state))])

(defn app-container [app-state]
  [:div
   [:h1 "Realtime Bitcoin Info"]
   [controls app-state]
   [:br]
   [output app-state]])

(defn render [app-state]
  (reagent/render-component [app-container app-state] (dom/getElement "app")))

