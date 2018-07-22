package data;

import java.io.Serializable;

public class OutOfRangeSampleSize extends Exception implements Serializable{

	private static final long serialVersionUID = 1L;

	public OutOfRangeSampleSize(String arg0) {
		super(arg0);
	}

}
