package com.macewan.cmpt305.edibletreesmap;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EdibleTrees {
    private final List<EdibleTree> trees;
    public EdibleTrees(List<EdibleTree> trees) {
        this.trees = trees;
    }
    @Override
    public String toString() {
        return trees.stream().map(EdibleTree::toString).collect(Collectors.joining("\n"));
    }
    public List<EdibleTree> getTrees() {
        return trees;
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


}
