package com.rpc.structure;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.*;

public class Client {
	private static final Logger LOG = Logger.getLogger(Client.class.getName());
	static private volatile int counter = 0;

	private ExecutorService executor = Executors.newSingleThreadExecutor();


	private Socket socket;
	private String host;
	private int port;
	private ObjectOutputStream out;
	private ObjectInputStream in;

	public Client(String host, int port) {
		this.host = host;
		this.port = port;
		try {
			this.socket = new Socket(host, port);
		} catch (IOException e) {
			LOG.error("Socket failed");
		}
		try {
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
			LOG.info("Socket initialize!");
		} catch (IOException e) {
			endClient();
		}
	}

	public static synchronized int getCounter() {
		return counter++;
	}

	private void endClient() {
		try {
			if (!socket.isClosed()) {
				socket.close();
				in.close();
				out.close();
			}
		} catch (IOException ex) {
		}
	}

	public Object remoteCall(String id, String serviceName, String methodName, Object[] params) {
		try {
			/*out.writeObject(new CallRpc()
					.id(id)
					.serviceName(serviceName)
					.procedureName(methodName)
					.args(Arrays.copyOf(params, params.length)));
			out.flush();
			Thread.sleep(1000);
			LOG.info("CLIENT_OUT_TEEEEEEEEESSSSSSSSSSSSSSSSSSSSSSSSSTTTTTTTTTTTTTTTTTTTT: " + Arrays.asList(params));


			Object rpcResponse = in.readObject();
			LOG.info("CLIENT_IN_TEEEEEEEEESSSSSSSSSSSSSSSSSSSSSSSSSTTTTTTTTTTTTTTTTTTTT: " + rpcResponse);*/
			Callable<Object> task = () -> {
				out.writeObject(new CallRpc()
						.id(id)
						.serviceName(serviceName)
						.procedureName(methodName)
						.args(Arrays.copyOf(params, params.length)));
				out.flush();
				Thread.sleep(1000);
				LOG.info("CLIENT_OUT_TEEEEEEEEESSSSSSSSSSSSSSSSSSSSSSSSSTTTTTTTTTTTTTTTTTTTT: " + Arrays.asList(params));


				Object rpcResponse = in.readObject();
				LOG.info("CLIENT_IN_TEEEEEEEEESSSSSSSSSSSSSSSSSSSSSSSSSTTTTTTTTTTTTTTTTTTTT: " + rpcResponse);
				return rpcResponse;
			};
			Future<Object> future = executor.submit(task);
			Object rpcResponse = future.get();

			return rpcResponse;

		} catch (
			//IOException
			//| ClassNotFoundException
				InterruptedException
						| ExecutionException
						//|TimeoutException
						ex) {
			return new RpcResult()
					.id(id).result("Call Rpc: " + methodName + " finished with error:\n" + ex.toString());
		}
	}


}
