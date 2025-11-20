package com.macewan.cmpt305.edibletreesmap;

import java.util.List;

public interface EdibleTreeDAO {
    EdibleTree getById(int id);
    List<EdibleTree> getAll();
    List<EdibleTree> getByName(String name);
    List<EdibleTree> getByLocation(double latitude, double longitude);
    List<EdibleTree> getByNeighbourhood(String neighbourhood);
    List<EdibleTree> getByGenus(String genus);

}
