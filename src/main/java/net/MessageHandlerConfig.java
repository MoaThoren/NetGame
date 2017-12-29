package net;

import javax.websocket.Decoder;
import javax.websocket.Encoder;
import javax.websocket.Extension;
import javax.websocket.server.ServerEndpointConfig;
import java.util.List;
import java.util.Map;

public class MessageHandlerConfig implements ServerEndpointConfig {

    // the uri path to use to get to this endpoint.
    // JavaScript to access from a WebSocket capable browser would be:  ws://<Host Name>:<port>/<Context-Root>/ExtendedEndpoint
    String uriPath = "/message";

    public MessageHandlerConfig() {
        // no-arg constructor
    }

    @Override
    public Class<?> getEndpointClass() {
        return MessageHandler.class;
    }

    @Override
    public String getPath() {
        return uriPath;
    }

    @Override
    public Configurator getConfigurator() {
        ServerEndpointConfig.Configurator x = new ServerEndpointConfig.Configurator();
        return x;
    }

    @Override
    public Map<String, Object> getUserProperties() {
        return null;
    }

    @Override
    public List<Extension> getExtensions() {
        return null;
    }

    @Override
    public List<String> getSubprotocols() {
        return null;
    }

    @Override
    public List<Class<? extends Decoder>> getDecoders() {
        return null;
    }

    @Override
    public List<Class<? extends Encoder>> getEncoders() {
        return null;
    }

}
