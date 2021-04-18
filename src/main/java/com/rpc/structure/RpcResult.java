package com.rpc.structure;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

@Data
@Accessors(fluent = true)
public class RpcResult implements Serializable {
	private static final long serialVersionUID = 6529685098267757690L;

	private String id;
	private Object result;


	private void readObject(ObjectInputStream aInputStream) throws ClassNotFoundException, IOException {
		id = aInputStream.readUTF();
		result = aInputStream.readObject();
	}
}
