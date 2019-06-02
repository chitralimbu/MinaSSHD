package com.sshd.main;

import java.io.File;
import java.io.IOException;

import org.apache.sshd.common.file.virtualfs.VirtualFileSystemFactory;
import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.auth.password.PasswordAuthenticator;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.server.scp.ScpCommandFactory;
import org.apache.sshd.server.session.ServerSession;
import org.apache.sshd.server.shell.ProcessShellFactory;

public class SSHDsecond {
	
	public static void main(String[] args) throws IOException {
		final SshServer sshd = SshServer.setUpDefaultServer();
		sshd.setPort(2222);
		
		sshd.setKeyPairProvider(new SimpleGeneratorHostKeyProvider());
		sshd.setShellFactory(new ProcessShellFactory(new String[] { "/bin/sh", "-i", "-l" }));
		sshd.setCommandFactory(new ScpCommandFactory());
		sshd.setPasswordAuthenticator(new PasswordAuthenticator() {
			  public boolean authenticate(String username, String password, ServerSession session) {
			    if ((username.equals("admin")) && (password.equals("admin"))) {
			      sshd.setFileSystemFactory(new VirtualFileSystemFactory(new File("C:\\test").toPath()));
			      return true;
			    }
			    return false;
			  }
			});
		sshd.start();
		
		while(true) {
			
		}
	}
}