package com.rpc.structure;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

@Data
@Accessors(fluent = true)
public class CallRpc implements Serializable {
	private static final long serialVersionUID = 8721263933535062039L;

	Object[] args;
	private String id;
	private String serviceName;
	private String procedureName;

	private void writeObject(ObjectOutputStream aOutputStream) throws IOException {
		aOutputStream.writeObject(args);
		aOutputStream.writeUTF(id);
		aOutputStream.writeUTF(serviceName);
		aOutputStream.writeUTF(procedureName);
	}
}
