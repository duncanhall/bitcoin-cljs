(ns coinviz.graph
  (:require
   [thi.ng.geom.core :as g]
   [thi.ng.geom.core.utils :as gu]
   [thi.ng.geom.svg.core :as svg]
   [thi.ng.geom.svg.adapter :as svgadapt]
   [thi.ng.geom.viz.core :as viz]
   [thi.ng.color.gradients :as grad]
   [thi.ng.math.core :as m :refer [PI]]
   [thi.ng.math.simplexnoise :as n]))


(defn visualization
  "Takes a geom.viz visualization spec map and generates SVG component.
  The call to inject-element-attribs ensures that all SVG elements have
  an unique :key attribute, required for React.js."
  [spec]
  (->> spec
       (viz/svg-plot2d-cartesian)
       (svgadapt/inject-element-attribs svgadapt/key-attrib-injector)
       (svg/svg {:width 900 :height 600})))

(defn test-equation
  [t]
  (let [x (m/mix (- PI) PI t)]
    [x (* (Math/cos (* 0.5 x)) (Math/sin (* x x x)))]))

(def line-spec
  {:x-axis (viz/linear-axis
            {:domain [(- PI) PI]
             :range  [50 580]
             :major  (/ PI 2)
             :minor  (/ PI 4)
             :pos    250})
   :y-axis (viz/linear-axis
            {:domain      [-1 1]
             :range       [250 20]
             :major       0.2
             :minor       0.1
             :pos         50
             :label-dist  15
             :label-style {:text-anchor "end"}})
   :grid   {:attribs {:stroke "#caa"}
            :minor-y true}
   :data   [{:values  (map test-equation (m/norm-range 200))
             :attribs {:fill "none" :stroke "#0af"}
             :layout  viz/svg-line-plot}]})
