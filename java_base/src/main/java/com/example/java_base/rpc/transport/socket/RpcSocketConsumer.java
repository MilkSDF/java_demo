package com.example.java_base.rpc.transport.socket;


import com.example.java_base.rpc.serialize.Hessian2Serialization;
import com.example.java_base.rpc.serialize.Serialization;
import com.example.java_base.rpc.transport.RpcRequest;
import com.example.java_base.rpc.transport.RpcResponse;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class RpcSocketConsumer {
    public static void main(String[] args) throws Exception {

        // 序列化层实现参考之前的章节
        Serialization serialization = new Hessian2Serialization();

        Socket socket = new Socket("localhost", 8088);
        InputStream is = socket.getInputStream();
        OutputStream os = socket.getOutputStream();
        // 封装 rpc 请求
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setRequestId(12345L);
        // 序列化 rpcRequest => requestBody
        byte[] requestBody = serialization.serialize(rpcRequest);
        DataOutputStream dos = new DataOutputStream(os);
        dos.writeInt(requestBody.length);
        dos.write(requestBody);
        dos.flush();
        DataInputStream dis = new DataInputStream(is);
        int length = dis.readInt();
        byte[] responseBody = new byte[length];
        dis.read(responseBody);
        // 反序列化 responseBody => rpcResponse
        RpcResponse rpcResponse = serialization.deserialize(responseBody, RpcResponse.class);
        is.close();
        os.close();
        socket.close();

        System.out.println(rpcResponse.getRequestId());
    }
}
