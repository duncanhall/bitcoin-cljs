(ns coinviz.ui
  (:require
   [reagent.ratom :refer [reaction]]
   [reagent.core :as reagent]
   [goog.dom :as dom]))

(defn output-option [app-state value label]
  [:span.output-option
   [:input {:id value :type "radio" :name "output-type" :value value :defaultChecked (= value (:output-fmt @app-state))
            :on-click #(swap! app-state assoc :output-fmt value) }]
   [:label {:for value} label]])

(defn output-select [app-state]
  [:div
   [output-option app-state "hash" "Hash"]
   [output-option app-state "size" "Size"]
   [output-option app-state "count" "Count"]
   [output-option app-state "total" "Total"]])

(defn controls [app-state]
  (let [connected (reaction (:connected @app-state))
        cx (reaction (:connect! @app-state))
        dx (reaction (:disconnect! @app-state))]
    [:div.controls
     [output-select app-state]
     [:br]
     [:input {
              :type "button"
              :value (if @connected "Disconnect" "Connect")
              :on-click (if @connected (@dx) (@cx))}]]))

(defn output [app-state]
  (let [txs (reaction (:transactions @app-state))]
  [:div
   [:ul
    (for [t @txs] ^{:key t}
      [:li (str t)])]]))

(defn app-container [app-state]
  [:div
   [:h1 "Realtime Bitcoin Info"]
   [controls app-state]
   [:br]
   [output app-state]])

(defn render [app-state]
  (reagent/render-component [app-container app-state] (dom/getElement "app")))

