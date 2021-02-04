package com.nopeya.fooapi.support.rpc;

import com.nopeya.fooapi.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Date;

@Slf4j
public class HandlerImporter {

    public final static InetSocketAddress address = new InetSocketAddress("localhost", 8088);

    public Exporter importer(String handlerName) {
        return (Exporter) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{Exporter.class},
                (proxy, method, args) -> {
                    Socket socket = null;
                    ObjectOutputStream output = null;
                    ObjectInputStream input = null;
                    try {
                        Date startTime = new Date();
                        socket = new Socket();
                        socket.connect(address);

                        output = new ObjectOutputStream(socket.getOutputStream());
                        input = new ObjectInputStream(socket.getInputStream());

                        output.writeUTF(handlerName);
                        output.writeObject(args);

                        Object result = input.readObject();

                        Date finishTime = new Date();
                        log.info("invoke: '{}', start at: '{}', finishat: '{}', duration: {}.",
                                handlerName, DateUtils.formatDateTime(startTime), DateUtils.formatDateTime(finishTime),
                                finishTime.getTime() - startTime.getTime());

                        return result;
                    } finally {
                        if (socket != null) {
                            socket.close();
                        }
                        if (output != null) {
                            output.close();
                        }
                        if (input != null) {
                            input.close();
                        }
                    }
                });
    }
}
