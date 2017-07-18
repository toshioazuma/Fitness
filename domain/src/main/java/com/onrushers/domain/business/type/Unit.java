package com.onrushers.domain.business.type;

public enum Unit {

	Metric(0),
	Imperial(1);

	private Integer value;

	Unit(Integer val) {
		this.value = val;
	}

	public Integer getValue() {
		return this.value;
	}

	public static Unit from(Integer val) {
		if (Metric.getValue() == val) {
			return Metric;
		} else if (Imperial.getValue() == val) {
			return Imperial;
		}
		return Metric;
	}

	@Override
	public String toString() {
		if (this == Metric) {
			return "metric";
		} else if (this == Imperial) {
			return "imperial";
		}
		return super.toString();
	}
}
