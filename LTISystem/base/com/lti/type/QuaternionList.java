package com.lti.type;

import java.util.ArrayList;

public class QuaternionList extends ArrayList<Quaternion> {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Quaternion get(int index) {
        while (index >= size()) {
            add(new Quaternion());
        }
        return super.get(index);
    }
}
