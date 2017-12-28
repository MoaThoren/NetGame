package view;

import com.google.gson.Gson;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

public class MessageDecoder implements Decoder.Text<Message> {
    @Override
    public void init(EndpointConfig ec) { }

    @Override
    public void destroy() { }

    @Override
    public Message decode(String string) throws DecodeException {
        return new Gson().fromJson(string, Message.class);
    }

    @Override
    public boolean willDecode(String string) {
        boolean canDecode = false;
        if (string.contains("name:") && string.contains("message:"))
            canDecode = true;
        return canDecode;
    }
}
