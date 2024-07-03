package com.jar36.jchat.server;

import com.jar36.jchat.SqlHelper;
import com.jar36.jchat.Util;
import com.jar36.jchat.packet.Command;
import com.jar36.jchat.packet.GetDataPacket;
import com.jar36.jchat.packet.Message;
import com.jar36.jchat.packet.Session;
import com.jar36.jchat.server.data.SessionData;
import com.jar36.jchat.server.data.SessionMessageManager;
import com.jar36.jchat.server.data.SqlManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;

public class ServerGetHandler extends SimpleChannelInboundHandler<GetDataPacket> {
    private void getSessionMessage(ChannelHandlerContext ctx, GetDataPacket msg) throws IOException, SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        User user = Util.verifySessionToken(msg.getSessionToken());
        if(user==null){
            msg.setSessionToken(0);
            msg.setReason("bad session token");
            ctx.channel().writeAndFlush(msg);
            return;
        }
        SessionData sessionData = SqlHelper.queryTableToObject(
                SqlManager.sessionDatabaseStatement, SessionData.class, "sid", msg.getId());
        if(sessionData==null){
            msg.setSessionToken(0);
            msg.setReason("not such session");
            ctx.channel().writeAndFlush(msg);
            return;
        }
        if(sessionData.getUid1()!=user.getUserData().getUid()&&sessionData.getUid2()!=user.getUserData().getUid()){
            msg.setSessionToken(0);
            msg.setReason("you don't have the access right to the session");
            ctx.channel().writeAndFlush(msg);
            return;
        }
        ArrayList<Message> messageArrayList;
        messageArrayList = SessionMessageManager.readSessionDatabase(msg.getId());
        msg.setSessionToken(1);
        msg.setMessages(messageArrayList.toArray(new Message[0]));
        ctx.channel().writeAndFlush(msg);
    }
    private void getSessionList(ChannelHandlerContext ctx, GetDataPacket msg) throws SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        User user = Util.verifySessionToken(msg.getSessionToken());
        if(user==null){
            msg.setSessionToken(0);
            msg.setReason("bad session token");
            ctx.channel().writeAndFlush(msg);
            return;
        }
        ArrayList<Session> sessionArrayList = new ArrayList<>();
        for(int sid = 1;sid < SqlManager.nextSid;sid++){
            SessionData sessionData = SqlHelper.queryTableToObject(
                    SqlManager.sessionDatabaseStatement, SessionData.class, "sid", sid);
            assert sessionData != null;
            if(sessionData.getUid1()==user.getUserData().getUid()||sessionData.getUid2()==user.getUserData().getUid()){
                Session session = new Session();
                session.setName(sessionData.getName());
                session.setUid1(sessionData.getUid1());
                session.setUid2(sessionData.getUid2());
                sessionArrayList.add(session);
            }
        }
        msg.setSessionToken(1);
        msg.setSessions(sessionArrayList.toArray(new Session[0]));
        ctx.channel().writeAndFlush(msg);
    }
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof GetDataPacket) {
            super.channelRead(ctx, msg);
        }else {
            ctx.fireChannelRead(msg);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GetDataPacket msg) throws Exception {
        switch (msg.getSubfunction()){
            case Command.DATA_GET_SESSION_LIST:
                getSessionList(ctx, msg);
                break;
            case Command.DATA_GET_SESSION_MESSAGE:
                getSessionMessage(ctx, msg);
                break;
            default:
                msg.setSessionToken(0);
                msg.setReason("bad subfunction");
                ctx.channel().writeAndFlush(msg);
        }
    }
}
