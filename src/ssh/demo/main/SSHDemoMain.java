package ssh.demo.main;

import ssh.demo.bean.SSHDemoBean;
public class SSHDemoMain {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		SSHDemoBean demo = new SSHDemoBean();
		if (demo.openConnection("192.168.1.101", 22, "salma", "As01022178704", 120000)){
			System.out.println("hey! ");
			Thread.sleep(300);
			demo.sendCommand("ls \n");
			Thread.sleep(300);
			System.out.println(demo.rcvdata());
			System.out.println("after rcv");
			demo.closeConnection();
		} else {
			System.out.println("Error");
		}
	}

}
