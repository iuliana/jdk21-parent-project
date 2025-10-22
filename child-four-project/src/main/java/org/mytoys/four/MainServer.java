/*
Freeware License, some rights reserved

Copyright (c) 2024 Iuliana Cosmina

Permission is hereby granted, free of charge, to anyone obtaining a copy 
of this software and associated documentation files (the "Software"), 
to work with the Software within the limits of freeware distribution and fair use. 
This includes the rights to use, copy, and modify the Software for personal use. 
Users are also allowed and encouraged to submit corrections and modifications 
to the Software for the benefit of other users.

It is not allowed to reuse,  modify, or redistribute the Software for 
commercial use in any way, or for a user's educational materials such as books 
or blog articles without prior permission from the copyright holder. 

The above copyright notice and this permission notice need to be included 
in all copies or substantial portions of the software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS OR APRESS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

package org.mytoys.four;

import com.sun.net.httpserver.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;

public class MainServer {
    private static final InetSocketAddress SERVER_ADDR =
            new InetSocketAddress(InetAddress.getLoopbackAddress(), 8081);

    public static void main(String... args) throws IOException {
        /*var fs = FileSystems.getFileSystem(URI.create("jrt:/"));
        var root = fs.getPath("modules").toAbsolutePath();
        var server = SimpleFileServer.createFileServer(LOOPBACK_ADDR,
                root, SimpleFileServer.OutputLevel.VERBOSE);
        server.start();*/

        var server = HttpServer.create(SERVER_ADDR, 0);
        server.createContext("/", new HomeHandler());
        server.setExecutor(java.util.concurrent.Executors.newCachedThreadPool());
        server.start();
        System.out.println("Server is running at http://" + SERVER_ADDR.getHostName() + ":" + SERVER_ADDR.getPort());
    }
}

class HomeHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange t) throws IOException {
        try (var in = getClass().getResourceAsStream("/home.html");
             var reader = new BufferedReader(new InputStreamReader(in)); var os = t.getResponseBody()) {
             var response= "";
             String line;
             while ((line = reader.readLine()) != null) {
                 response = response.concat(line);
             }
            Headers header = t.getResponseHeaders();
            header.add("Content-Type", "text/html;charset=UTF-8");
            var arr =  response.getBytes();
            t.sendResponseHeaders(200, arr.length);
            os.write(arr);
        }
    }
}
