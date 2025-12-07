package com.macewan.cmpt305.edibletreesmap.DataObjectsLayer;

import com.esri.arcgisruntime.geometry.GeometryEngine;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import com.esri.arcgisruntime.geometry.Point;


public class EdibleTrees {
    private final List<EdibleTree> trees;
    public EdibleTrees(List<EdibleTree> trees) {
        this.trees = trees;
    }
    @Override
    public String toString() {
        return trees.stream().map(EdibleTree::toString).collect(Collectors.joining("\n"));
    }
    public Integer getSize(){
        return trees.size();
    }
    public List<EdibleTree> getTrees() {
        return trees;
    }
    public EdibleTree getTreeByPoint(Point point) {
        double toleranceInMeters = 10.0;

        return trees.stream()
                .filter(tree -> calculateDistance(point, tree.getPlantLocation().getPoint()) <= toleranceInMeters)
                .min(Comparator.comparingDouble(tree -> calculateDistance(point, tree.getPlantLocation().getPoint())))
                .orElse(null);
    }
    private double calculateDistance(Point p1, Point p2) {
        p1 = (Point)GeometryEngine.project(p1, SpatialReferences.getWebMercator());
        p2 = (Point)GeometryEngine.project(p2, SpatialReferences.getWebMercator());
        double dx = p1.getX() - p2.getX();
        double dy = p1.getY() - p2.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    public List<EdibleTree> getTreeByRadius(Point centerPoint, double radius) {
        return trees.stream()
                .filter(tree -> calculateDistance(centerPoint, tree.getPlantLocation().getPoint()) <= radius)
                .collect(Collectors.toList());
    }

    public List<EdibleTree> getByNeighborhood(String neighborhood) {
        return trees.stream()
                .filter(tree -> neighborhood
                        .equals(tree
                                .getPlantLocation()
                                .getNeighborhoodName()))
                .toList();

    }
    public List<EdibleTree> getByGenus(String genus) {
        return trees.stream()
                .filter(tree -> genus.equals(tree.getPlantBiology().getGenus()))
                .toList();
    }
    public List<EdibleTree> getBySpecies(String species) {
        return trees.stream()
                .filter(tree -> species.equals(tree.getPlantBiology().getSpeciesCommon()))
                .toList();
    }
    public List<EdibleTree> getByYear(int year) {
        return trees.stream()
                .filter(tree -> tree.getPlantInfo().getYearPlanted() == year)
                .toList();
    }
    public List<EdibleTree> getByFruit(String fruit) {
        return trees.stream()
                .filter(tree -> fruit.equals(tree.getPlantBiology().getTypeFruit()))
                .toList();
    }
    public List<String> getNeighborhoodNames() {
        return trees.stream()
                .map(tree -> tree.getPlantLocation().getNeighborhoodName())
                .collect(Collectors.toSet())
                .stream()
                .toList();

    }



}
