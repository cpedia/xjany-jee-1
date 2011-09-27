package com.lti.type;

import java.util.ArrayList;

public class TripleList extends ArrayList<Triple> {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Triple get(int index) {
        while (index >= size()) {
            add(new Triple());
        }
        return super.get(index);
    }
}
