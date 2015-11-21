(ns coinviz.ui
  (:require
   [reagent.core :as reagent]
   [thi.ng.geom.svg.core :as svg]
   [thi.ng.geom.svg.adapter :as svgadapt]
   [thi.ng.geom.viz.core :as viz]
   [coinviz.render-graph :as rg]
   [goog.dom :as dom]))


(defn output-graph
  [app-state]
  (->> (rg/update-spec (:graph @app-state))
       (viz/svg-plot2d-cartesian)
       (svgadapt/inject-element-attribs svgadapt/key-attrib-injector)
       (svg/svg {:width 1000 :height 600})))


(defn controls [app-state]
  (fn []
   (let [connected (:connected @app-state)]
    [:div.controls
     [:input {
              :type "button"
              :value (if connected "Disconnect" "Connect")
              :on-click (if connected (:disconnect! @app-state) (:connect! @app-state))}]])))


(defn app-container [app-state]
  [:div
   [:h1 "Realtime Bitcoin Info"]
   [controls app-state]
   [:br]
   [:div [output-graph app-state]]])


(defn render [app-state]
  (reagent/render-component [app-container app-state] (dom/getElement "app")))

