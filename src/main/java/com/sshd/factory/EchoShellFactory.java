package com.sshd.factory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import org.apache.sshd.common.Factory;
import org.apache.sshd.server.Command;
import org.apache.sshd.server.Environment;
import org.apache.sshd.server.ExitCallback;

public class EchoShellFactory implements Factory<Command>{

	public Command create() {
		return new EchoShell();
	}
	
	@SuppressWarnings("unused")
	public static class EchoShell implements Command, Runnable{
		private InputStream in;
		private OutputStream out;
		private OutputStream err;
		private ExitCallback callback;
		private Environment environment;
		private Thread thread;
		
		public void start(Environment env) throws IOException {
			environment = env;
			thread = new Thread(this, "EchoShell");
			thread.start();
		}

		public void destroy() throws Exception {
			thread.interrupt();
			
		}

		public void run() {
			BufferedReader r = new BufferedReader(new InputStreamReader(in));
			try {
				for(;;) {
					String s = r.readLine();
					if(s == null) {
						return;
					}
					out.write(( "Hello: " + s + "\n").getBytes());
					out.flush();
					if("exit".equals(s)) {
						return;
					}
				}
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				callback.onExit(0);
			}
			
		}

		public void setInputStream(InputStream in) {
			this.in = in;
			
		}

		public void setOutputStream(OutputStream out) {
			this.out = out;
			
		}

		public void setErrorStream(OutputStream err) {
			this.err = err;
			
		}

		public void setExitCallback(ExitCallback callback) {
			this.callback = callback;
			
		}
		
	}
	
}
