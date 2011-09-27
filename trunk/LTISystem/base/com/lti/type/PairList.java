package com.lti.type;

import java.util.ArrayList;

public class PairList extends ArrayList<Pair> {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Pair get(int index) {
        while (index >= size()) {
            add(new Pair());
        }
        return super.get(index);
    }
}
