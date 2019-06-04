package com.sshd.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.sshd.common.Factory;
import org.apache.sshd.common.NamedFactory;
import org.apache.sshd.common.file.virtualfs.VirtualFileSystemFactory;
import org.apache.sshd.server.Command;
import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.auth.UserAuth;
import org.apache.sshd.server.auth.password.PasswordAuthenticator;
import org.apache.sshd.server.auth.password.PasswordChangeRequiredException;
import org.apache.sshd.server.auth.password.UserAuthPasswordFactory;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.server.scp.ScpCommandFactory;
import org.apache.sshd.server.session.ServerSession;
import org.apache.sshd.server.subsystem.sftp.SftpSubsystemFactory;

public class SSHDMain {
	public static void main(String[] args) throws IOException {
		List<NamedFactory<UserAuth>> userAuthFactories = new ArrayList<NamedFactory<UserAuth>>();
		userAuthFactories.add(new UserAuthPasswordFactory());

		List<NamedFactory<Command>> sftpCommandFactory = new ArrayList<NamedFactory<Command>>();
		sftpCommandFactory.add(new SftpSubsystemFactory());

		final SshServer sshd = SshServer.setUpDefaultServer();
		sshd.setPort(222);
		sshd.setKeyPairProvider(new SimpleGeneratorHostKeyProvider());
		sshd.setUserAuthFactories(userAuthFactories);
		sshd.setCommandFactory(new ScpCommandFactory());
		sshd.setSubsystemFactories(sftpCommandFactory);
		sshd.setPasswordAuthenticator(new PasswordAuthenticator() {
		  public boolean authenticate(String username, String password, ServerSession session) {
		    if ((username.equals("admin")) && (password.equals("admin"))) {
		      sshd.setFileSystemFactory(new VirtualFileSystemFactory(new File("path").toPath()));
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
