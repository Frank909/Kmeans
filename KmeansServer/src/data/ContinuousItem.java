package data;


class ContinuousItem extends Item{

	private static final long serialVersionUID = 1L;

	ContinuousItem(Attribute attribute, Object value) {
		super(attribute, value);
	}

	@Override
	double distance(Object a) {
		double currentValue, secValue;
		ContinuousItem c = (ContinuousItem)a;
		currentValue = ((ContinuousAttribute)this.getAttribute()).getScaledValue((Double)this.getValue());
		secValue =  ((ContinuousAttribute)c.getAttribute()).getScaledValue((Double)c.getValue());
		return Math.abs(currentValue - secValue);
	}

}
