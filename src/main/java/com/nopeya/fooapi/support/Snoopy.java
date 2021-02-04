package com.nopeya.fooapi.support;

import com.nopeya.fooapi.utils.Assert;
import com.nopeya.fooapi.utils.DateUtils;
import com.nopeya.fooapi.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Slf4j
@Component
public class Snoopy {

    public static Map<String, Handler> HOLDER = new ConcurrentHashMap<>();

    static {
        Snoopy.startUp();
    }

    public static boolean contains(String name) {
        if (StringUtils.isNotBlank(name)) {
            return HOLDER.containsKey(name);
        }
        return false;
    }

    public static Handler get(String name) {
        return HOLDER.get(name);
    }

    public static void push(Handler handler) {
        Assert.isNotBlank(handler.getName(), "handler must has name!");
        HOLDER.put(handler.getName(), handler);
    }

    private static void startUp() {
        new Thread(() -> {
            try {
                loadBanner();
                HandlerExporter.exporter();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private static void loadBanner() {
        Resource resource = new ClassPathResource("snoopy-banner.txt");
        if (resource.exists()) {
            InputStream is = null;
            InputStreamReader isr = null;
            BufferedReader br = null;
            try {
                is = resource.getInputStream();
                isr = new InputStreamReader(is);
                br = new BufferedReader(isr);
                String data = null;
                while((data = br.readLine()) != null) {
                    System.out.println(data);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    br.close();
                    isr.close();
                    is.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private static class HandlerExporter {
        static Executor executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        final static int PORT = 8088;

        public static void exporter() throws Exception {
            log.info("");
            ServerSocket server = new ServerSocket();
            server.bind(new InetSocketAddress(PORT));
            try {
                while (true) {
                    executor.execute(new ExporterTask(server.accept()));
                }
            } finally {
                server.close();
            }
        }
    }

    private static class ExporterTask implements Runnable {

        Socket client = null;
        public ExporterTask(Socket socket) {
            this.client = socket;
        }

        @Override
        public void run() {
            ObjectInputStream input = null;
            ObjectOutputStream output = null;

            try {
                Date startTime = new Date();
                input = new ObjectInputStream(client.getInputStream());
                output = new ObjectOutputStream(client.getOutputStream());

                String handlerName = input.readUTF();
                Object[] paramsData = (Object[]) input.readObject();
                Object[] params = null;
                if (ArrayUtils.isNotEmpty(paramsData)) {
                    params = (Object[]) paramsData[0];
                }

                Handler handler = Snoopy.get(handlerName);
                Object result = handler.call(params);
                output.writeObject(result);
                Date finishTime = new Date();

                log.info("invoke: '{}', start at: '{}', finishat: '{}', duration: {}.",
                        handlerName, DateUtils.formatDateTime(startTime), DateUtils.formatDateTime(finishTime),
                        finishTime.getTime() - startTime.getTime());

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (output != null) {
                    try {
                        output.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (input != null) {
                    try {
                        input.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (client != null) {
                    try {
                        client.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
