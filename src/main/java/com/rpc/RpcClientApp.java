package com.rpc;

import com.rpc.structure.Client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.log4j.Logger;

import static com.rpc.structure.Client.getCounter;

public class RpcClientApp {
	public static void main(String[] args) throws IOException {
		Client client = new Client("localhost", 4109);
		for (int i = 0; i < 10; i++) {
			new Thread(new Caller(client)).start();
		}
	}

	private static class Caller implements Runnable {
		private Logger LOG = Logger.getLogger(Caller.class.getName());
		private Client c;


		public Caller(Client c) {

			this.c = c;
		}

		public void run() {
			try {
				while (true) {
					Object call1 = c.remoteCall(String.valueOf(getCounter()), "service1", "sleep", new Object[]{1000L});
					Object call2 = c.remoteCall(String.valueOf(getCounter()), "service1", "getCurrentDate", new Object[]{});
					LOG.info("Call: " + call1);
					System.out.println(call2);
					LOG.info("Current Date is:" + call2);
					Thread.sleep(5000);
				}
			} catch (InterruptedException ex) {

			}
		}
	}
}
