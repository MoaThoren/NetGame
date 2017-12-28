package view;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class MessageEncoder implements Encoder.Text<Message> {
    @Override
    public void init(EndpointConfig ec) { }
    @Override
    public void destroy() { }
    @Override
    public String encode(Message msg) throws EncodeException {
        return "{ name:" + msg.name + ", message:" + msg.message + " }";
    }
}
