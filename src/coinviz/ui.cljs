(ns coinviz.ui
  (:require
   [reagent.core :as reagent]
   [thi.ng.geom.svg.core :as svg]
   [thi.ng.geom.svg.adapter :as svgadapt]
   [thi.ng.geom.viz.core :as viz]
   [thi.ng.color.gradients :as grad]
   [thi.ng.math.core :as m :refer [PI]]
   [goog.dom :as dom]))



(defn test-equation
  [t]
  (let [x (m/mix (- PI) PI t)]
    [x (* (Math/cos (* 0.5 x)) (Math/sin (* x x x)))]))


(defn get-spec [values]
  {:x-axis (viz/linear-axis
            {:domain [0 400]
             :range  [50 580]
             :major  50
             :minor  10
             :pos    250})
   :y-axis (viz/linear-axis
            {:domain      [0 20]
             :range       [250 20]
             :major       10
             :minor       2
             :pos         50})
   :grid   {:attribs {:stroke "#caa"}
            :minor-y true}
   :data   [{:values  values
             :attribs {:fill "#0af" :stroke "#0af"}
             :layout  viz/svg-area-plot}]})

;; area graph (based on line graph spec)

#_(def area-spec
  (update-in line-spec [:data 0] merge
             {:attribs {:fill "#0af"}
              :layout viz/svg-area-plot}))



(defn visualization
  "Takes a geom.viz visualization spec map and generates SVG component.
  The call to inject-element-attribs ensures that all SVG elements have
  an unique :key attribute, required for React.js."
  [app-state]
  (let [spec (get-spec (:graph @app-state))]
    (->> spec
         (viz/svg-plot2d-cartesian)
         (svgadapt/inject-element-attribs svgadapt/key-attrib-injector)
         (svg/svg {:width 600 :height 600}))))



(defn output-option [app-state value label]
  [:span.output-option
   [:input {:id value :type "radio" :name "output-type" :value value :defaultChecked (= value (:output-fmt @app-state))
            :on-click #(swap! app-state assoc :output-fmt value) }]
   [:label {:for value} label]])

(defn output-select [app-state]
  (fn []
  [:div
   [output-option app-state "hash" "Hash"]
   [output-option app-state "size" "Size"]
   [output-option app-state "count" "Count"]
   [output-option app-state "total" "Total"]]))

(defn controls [app-state]
  (fn []
   (let [connected (:connected @app-state)]
    [:div.controls
     [output-select app-state]
     [:br]
     [:input {
              :type "button"
              :value (if connected "Disconnect" "Connect")
              :on-click (if connected (:disconnect! @app-state) (:connect! @app-state))}]])))

(defn output [app-state]
  (fn []
  (let [txs (:transactions @app-state)]
  [:div
   [:ul
    (for [t txs] ^{:key t}
      [:li (str t)])]])))

(defn output-graph [app-state]
  (fn []
  [:div (str (:graph @app-state))]))

(defn app-container [app-state]
  [:div
   [:h1 "Realtime Bitcoin Info"]
   [controls app-state]
   [:br]
   [output-graph app-state]
   [:div [visualization app-state]]])

(defn render [app-state]
  (reagent/render-component [app-container app-state] (dom/getElement "app")))

