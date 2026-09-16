(ns build.generator.core
  (:require
   [build.generator.bcd :as generator.bcd]
   [build.generator.icon :as generator.icon]
   [build.generator.schema-graph :as generator.schema-graph]
   [build.generator.shell-dsl :as generator.shell-dsl]))

(defn -main []
  (generator.icon/generate!)
  (generator.bcd/generate!)
  (generator.shell-dsl/generate!)
  (generator.schema-graph/generate!))
