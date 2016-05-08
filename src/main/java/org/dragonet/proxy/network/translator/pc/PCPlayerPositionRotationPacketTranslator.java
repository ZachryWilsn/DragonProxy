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
package org.dragonet.proxy.network.translator.pc;

import org.dragonet.net.packet.minecraft.FullChunkPacket;
import org.dragonet.net.packet.minecraft.FullChunkPacket.ChunkOrder;
import org.dragonet.net.packet.minecraft.LoginStatusPacket;
import org.dragonet.net.packet.minecraft.MovePlayerPacket;
import org.dragonet.net.packet.minecraft.PEPacket;
import org.dragonet.net.packet.minecraft.RespawnPacket;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.cache.CachedEntity;
import org.dragonet.proxy.network.translator.PCPacketTranslator;
import org.dragonet.proxy.utilities.PlaceholderChunk;
import org.spacehq.mc.protocol.packet.ingame.server.entity.player.ServerPlayerPositionRotationPacket;

public class PCPlayerPositionRotationPacketTranslator implements PCPacketTranslator<ServerPlayerPositionRotationPacket> {

    @Override
    public PEPacket[] translate(UpstreamSession session, ServerPlayerPositionRotationPacket packet) {
        MovePlayerPacket pk = new MovePlayerPacket(0, (float) packet.getX(), (float) packet.getY(), (float) packet.getZ(), packet.getYaw(), packet.getPitch(), packet.getYaw(), false);
        CachedEntity cliEntity = session.getEntityCache().getClientEntity();

        long dX = (long) (cliEntity.x - packet.getX()) / 16;
        long dY = (long) (cliEntity.y - packet.getY()) / 16;
        long dZ = (long) (cliEntity.z - packet.getZ()) / 16;
        long squaredChunkDistance = dX * dX + dY * dY + dZ * dZ;
        if (squaredChunkDistance > 244) {
            //Before that, let's send a placeholder
            /*
            FullChunkPacket phChunk = new FullChunkPacket();
            phChunk.chunkX = (int) (packet.getX() / 16);
            phChunk.chunkZ = (int) (packet.getZ() / 16);
            phChunk.order = ChunkOrder.COLUMNS;
            phChunk.chunkData = PlaceholderChunk.FULL_GLASS;
            */
            
            RespawnPacket respawn = new RespawnPacket();
            respawn.x = (float) packet.getX();
            respawn.y = (float) packet.getY();
            respawn.z = (float) packet.getZ();
            
            LoginStatusPacket status = new LoginStatusPacket();
            status.status = LoginStatusPacket.PLAYER_SPAWN;
            
            cliEntity.x = packet.getX();
            cliEntity.y = packet.getY();
            cliEntity.z = packet.getZ();
            System.out.println("USING SHIT TELEPORT");
            return new PEPacket[]{respawn, status};
        }

        cliEntity.x = packet.getX();
        cliEntity.y = packet.getY();
        cliEntity.z = packet.getZ();
        System.out.println("USING FUCK TELEPORT");
        return new PEPacket[]{pk};
    }

}
