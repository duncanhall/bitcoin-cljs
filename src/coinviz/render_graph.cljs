(ns coinviz.render-graph
  (:require
   [thi.ng.geom.viz.core :as viz]))


(defn update-spec [values]
  {:x-axis (viz/linear-axis
            {:domain [0 1000]
             :range  [50 980]
             :major  200
             :minor  20
             :pos    250})
   :y-axis (viz/linear-axis
            {:domain      [0 50]
             :range       [250 20]
             :major       10
             :minor       2
             :pos         50})
   :grid   {:attribs {:stroke "#caa"}
            :minor-y true}
   :data   [{:values  values
             :attribs {:fill "#0af" :stroke "#0af"}
             :layout  viz/svg-scatter-plot}]}
)
