package com.macewan.cmpt305.edibletreesmap;

import java.io.IOException;
import java.util.List;

public interface EdibleTreeDAO {

    EdibleTree getById(int id);

    List<EdibleTree> getResults(String query) throws IOException, InterruptedException;

    List<EdibleTree> getAll() throws IOException, InterruptedException;
    List<EdibleTree> getByName(String name);
    List<EdibleTree> getByLocation(double latitude, double longitude);
    List<EdibleTree> getByNeighbourhood(String neighbourhood);
    List<EdibleTree> getByGenus(String genus);

}
