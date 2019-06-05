package com.sshd.main;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.EnumSet;

import org.apache.sshd.common.NamedFactory;
import org.apache.sshd.common.file.virtualfs.VirtualFileSystemFactory;
import org.apache.sshd.server.Command;
import org.apache.sshd.server.CommandFactory;
import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.auth.password.PasswordAuthenticator;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.server.scp.ScpCommandFactory;
import org.apache.sshd.server.session.ServerSession;
import org.apache.sshd.server.shell.ProcessShellFactory;
import org.apache.sshd.server.subsystem.sftp.SftpSubsystemFactory;
import org.apache.sshd.server.shell.ProcessShellFactory;
import com.sshd.factory.EchoShellFactory;

public class SSHDsecond {

	public static void main(String[] args) throws IOException {
		SshServer sshd = SshServer.setUpDefaultServer();
		sshd.setPort(2222);

		sshd.setKeyPairProvider(new SimpleGeneratorHostKeyProvider());

		String os = System.getProperty("os.name");

		String [] env = os.startsWith("windows") ? new String [] {"cmd.exe"} : new String[] { "/bin/sh", "-i", "-l" }; 
		sshd.setShellFactory(new ProcessShellFactory(env));

		sshd.setSubsystemFactories(Arrays.<NamedFactory<Command>>asList(new SftpSubsystemFactory()));
		sshd.setCommandFactory(new ScpCommandFactory());
		sshd.setPasswordAuthenticator(new PasswordAuthenticator() {
			public boolean authenticate(String username, String password, ServerSession session) {
				if ((username.equals("admin")) && (password.equals("admin"))) {
					return true;
				}
				return false;
			}
		});

		String root = os.startsWith("Windows") ? "c:\\" : "/home/dharanboi"; 
		System.out.println(root + ": is the root direcotry");
		sshd.setFileSystemFactory(new VirtualFileSystemFactory(new File(root).toPath()));
		sshd.start();
		while(true) {

		}
	}
}
