package com.monitor.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.monitor.bean.MonitorBean;

@Service
public class MonitorServiceImpl implements MonitorService {
	static int port = 22;

	@Autowired
	MonitorBean monitorBean;
	
	public MonitorBean MonitorCheck() {

		RamChecker();
		CPUChecker();
		SwapChecker();
		
		System.out.println(monitorBean.toString());
		return monitorBean;
	}

	public static double remoteMonitor(String host, String user, String password, String command) {
		host = "10.26.194.50";
		user = "raorv";
		password = "raorv";
		int port = 22;
		double cpuUsage = 0.0;
		try {

		
			JSch jsch = new JSch();
			Session session = jsch.getSession(user, host, port);
			session.setPassword(password);
			session.setConfig("StrictHostKeyChecking", "no");
			session.connect();

			Channel channel = session.openChannel("exec");
			// ((ChannelExec) channel).setCommand("top -b -n 1 | grep '%Cpu' | awk '{print
			// $2}' | cut -d '.' -f 1");
			((ChannelExec) channel).setCommand(command);
			channel.setInputStream(null);

			InputStream in = channel.getInputStream();

			channel.connect();

			BufferedReader reader = new BufferedReader(new InputStreamReader(in));

			String line;
			while ((line = reader.readLine()) != null) {
				 cpuUsage = Double.parseDouble(line);
				System.out.println("CPU Usage on " + host + ": " + cpuUsage + "%");
			}
			
			channel.disconnect();
			session.disconnect();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cpuUsage;
	}

	public void RamChecker() {
		
		String host = "10.26.194.50";
		String user = "raorv";
		String password = "raorv";
		
//		String host = "10.89.14.85";
//		String user = "cognity";
//		String password = "co.Bo$$21";
		String command = "free -m | grep Mem | awk '{print $3/$2 * 100}'";
		Double ram=remoteMonitor(host, user, password, command);
		monitorBean.setRam(ram);
		

	}

	public void CPUChecker() {
		String host = "10.26.194.50";
		String user = "raorv";
		String password = "raorv";
//		String host = "10.89.14.85";
//		String user = "cognity";
//		String password = "co.Bo$$21";
		
		String command = "top -b -n 1 | grep '%Cpu' | awk '{print $2}' | cut -d '.' -f 1";
		remoteMonitor(host, user, password, command);
		Double cpu=remoteMonitor(host, user, password, command);
		monitorBean.setCpu(cpu);

	}

	public void SwapChecker() {
		String host = "10.26.194.50";
		String user = "raorv";
		String password = "raorv";
	//	String host = "10.89.14.85";
		//String user = "cognity";
		//String password = "co.Bo$$21";
		String command = "free -m | grep Swap | awk '{print $3/$2 * 100}'";
		remoteMonitor(host, user, password, command);
		Double swap=remoteMonitor(host, user, password, command);
		monitorBean.setSwap(swap);

	}

}
