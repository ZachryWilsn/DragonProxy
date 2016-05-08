/*
 * GNU LESSER GENERAL PUBLIC LICENSE
 *                       Version 3, 29 June 2007
 *
 * Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 * Everyone is permitted to copy and distribute verbatim copies
 * of this license document, but changing it is not allowed.
 *
 * You can view LICENCE file for details. 
 *
 * @author The Dragonet Team
 */
package org.dragonet.net.packet.minecraft;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;
import org.dragonet.net.inf.mcpe.NetworkChannel;
import org.dragonet.proxy.utilities.io.PEBinaryReader;
import org.dragonet.proxy.utilities.io.PEBinaryWriter;

public class LoginPacket extends PEPacket {

    public String username;
    public int protocol1;
    public int protocol2;
    public long clientID;
    public UUID clientUuid;
    public String serverAddress;
    public String clientSecret;

    public String skinName;
    public byte[] skin;

    public LoginPacket(byte[] data) {
        this.setData(data);
    }
    
    public LoginPacket(){
    }

    @Override
    public int pid() {
        return PEPacketIDs.LOGIN_PACKET;
    }

    @Override
    public void encode() {
        try {
            setChannel(NetworkChannel.CHANNEL_PRIORITY);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            PEBinaryWriter writer = new PEBinaryWriter(bos);
            writer.writeByte((byte) (this.pid() & 0xFF));
            writer.writeString(this.username);
            writer.writeInt(protocol1);
            writer.writeInt(protocol2);
            writer.writeLong(clientID);
            writer.writeUUID(clientUuid);
            writer.writeString(serverAddress);
            writer.writeString(clientSecret);

            if (skin == null || skinName == null) {
                writer.writeString(skinName);
                writer.writeShort((short) (skin.length & 0xFFFF));
                writer.write(skin);
            } else {
                writer.writeString("Steve");
                writer.writeShort((short) 0);
            }
            this.setData(bos.toByteArray());
        } catch (IOException e) {
        }
    }

    @Override
    public void decode() {
        try {
            PEBinaryReader reader = new PEBinaryReader(new ByteArrayInputStream(this.getData()));
            reader.readByte(); //PID
            this.username = reader.readString();
            this.protocol1 = reader.readInt();
            this.protocol2 = reader.readInt();
            this.clientID = reader.readLong();
            this.clientUuid = reader.readUUID();
            this.serverAddress = reader.readString();
            this.clientSecret = reader.readString();

            this.skinName = reader.readString();
            int len = reader.readShort();
            this.skin = reader.read(len);
            this.setLength(reader.totallyRead());
        } catch (IOException e) {
        }
    }

}
