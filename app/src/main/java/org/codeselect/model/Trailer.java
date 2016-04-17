package org.codeselect.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CodeMyMobile on 11-04-2016.
 */
public class Trailer {

    private Integer id;
    private List<Youtube> results = new ArrayList<Youtube>();

    /**
     *
     * @return
     * The id
     */
    public Integer getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(Integer id) {
        this.id = id;
    }


    /**
     *
     * @return
     * The results
     */
    public List<Youtube> getResults() {
        return results;
    }

    /**
     *
     * @param results
     * The results
     */
    public void setResults(List<Youtube> results) {
        this.results = results;
    }

}