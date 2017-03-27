package com.anthem.nimbus.platform.core.process.api;

import java.io.IOException;
import java.net.UnknownHostException;

import javax.annotation.PreDestroy;

import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.stereotype.Component;
import org.springframework.util.SocketUtils;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;

/*
 * 
 */

/**
 * MongoIntegrationTestConfig class extends AbstractMongoConfiguration. 
 * In the scope of test classes, Embedded Mongo connection is created instead of 
 * a real instance of Mongo. Embedded Mongo is being used from an open source library - de.flapdoodle.embed.mongo.
 * 
 * @author Swetha Vemuri
 *
 */

@Component
@Profile({"integrationTest","build"})
public class MongoIntegrationTestConfig extends AbstractMongoConfiguration{

	private static MongodExecutable mongodExe;
	private static MongoClient mongo;
	private IMongodConfig mongodConfig;
	private String bindIp = "localhost";
	private int port = SocketUtils.findAvailableTcpPort();

	protected String getDatabaseName() {
		return "integrationtest";
	}

	@Override
	public Mongo mongo() throws Exception {
		if (mongo == null) {
			MongodStarter starter = MongodStarter.getDefaultInstance();
			try {
				mongodConfig = new MongodConfigBuilder().version(Version.Main.PRODUCTION)
						.net(new Net(bindIp, port, Network.localhostIsIPv6())).build();

				mongodExe = starter.prepare(mongodConfig);
				mongodExe.start();
				mongo = new MongoClient(bindIp, port);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return mongo;
	}

	@PreDestroy
	public void stopMongo() {
		if (mongodExe != null) {
			mongodExe.stop();
		}
	}
}