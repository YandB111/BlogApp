package com.monitor.bean;


import org.springframework.stereotype.Component;

@Component
public class MonitorBean {
	private Double cpu;
	private Double ram;
	private Double swap;

	public Double getCpu() {
		return cpu;
	}

	public void setCpu(Double cpu) {
		this.cpu = cpu;
	}

	public Double getRam() {
		return ram;
	}

	public void setRam(Double ram) {
		this.ram = ram;
	}

	public Double getSwap() {
		return swap;
	}

	public void setSwap(Double swap) {
		this.swap = swap;
	}

	@Override
	public String toString() {
		return "MonitorBean [cpu=" + cpu + ", ram=" + ram + ", swap=" + swap + "]";
	}

}
