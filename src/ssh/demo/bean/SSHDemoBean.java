package ssh.demo.bean;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class SSHDemoBean {
	private JSch mSShJSch = null;
	private Session mSSHSession = null;
	private Channel mSHHChannel = null;
	private OutputStream outputStream = null;
	private InputStream inputStream= null;
	
	
	public boolean openConnection(String host, int port, String userName, String password, int timeout){
		System.out.println("Start the connection!");
		boolean res = false;
		// Init JSch obj.
		mSShJSch = new JSch();
		
		// Set JSch obj properties, check no key to login.
		Properties config = new Properties();
		config.put("StrictHostKeyChecking", "no");
		this.mSShJSch.setConfig(config);
		
		try {
			this.mSSHSession = this.mSShJSch.getSession(userName, host, port);
			// Set password and connect.
			this.mSSHSession.setPassword(password);
			this.mSSHSession.connect(timeout);
			
			// Open the channel.
			this.mSHHChannel = this.mSSHSession.openChannel("shell");

			this.mSHHChannel.setOutputStream(System.out);
			this.mSHHChannel.setInputStream(System.in);
			this.mSHHChannel.connect();
			
			res = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	
	public boolean sendCommand(String command){
		boolean res = false;
		try {
			this.outputStream = this.mSHHChannel.getOutputStream();
			if (this.outputStream != null) {
				// Send data.
				this.outputStream.write(command.getBytes());
				this.outputStream.flush();
				res = true;	
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	
	public String rcvdata(){
		StringBuilder data = new StringBuilder();
		try {
			this.inputStream = this.mSHHChannel.getInputStream();
			if (this.inputStream != null) {
				int available = this.inputStream.available();
				
				while (available > 0) {
					byte[] buffer = new byte[available];
					int bytesRead = this.inputStream.read(buffer);
					
					available -= bytesRead;
					// append to data, after removing special characters.		
					data.append(new String(buffer).replaceAll("\\[\\d+;\\d+m", "").replace("[0m", "").replace("", ""));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data.toString();
	}
	
	public void closeConnection() {
		if (this.mSSHSession != null) {
			this.mSSHSession.disconnect();
		}
		if (this.mSHHChannel != null) {
			this.mSHHChannel.disconnect();
		} 
		try {
			if (inputStream != null) {
				inputStream.close();
			}
			if (outputStream != null) {
				outputStream.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.mSShJSch = null;
	}
}