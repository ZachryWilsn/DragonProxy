package org.dragonet.proxy.utilities;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;

/**
 * A pre-defined chunk as a temporaray placeholder chunk for long-distance
 * teleporting. All of those chunks are in CLOUMNS order.
 */
public final class PlaceholderChunk {

    public final static byte[] FULL_GLASS;

    static {
        byte[] fullarr = new byte[64 * 64 * 128];
        byte[] target = null;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            Arrays.fill(fullarr, 0, fullarr.length, (byte) 20);
            bos.write(fullarr);//ID

            Arrays.fill(fullarr, 0, 64 * 64 * 64, (byte) 0);
            bos.write(fullarr, 0, 64 * 64 * 64);//Meta

            Arrays.fill(fullarr, 0, fullarr.length, (byte) 0xFF);
            bos.write(fullarr);//Sky light and block light

            bos.write(fullarr, 0, 256);//256 times of 0xFF, height-map

            fullarr[0] = (byte) 0x01;
            fullarr[1] = (byte) 0x85;
            fullarr[2] = (byte) 0xB2;
            fullarr[3] = (byte) 0x4A;
            for (int i = 0; i < 256; i++) {
                bos.write(fullarr, 0, 4);
            }
            
            bos.write(new byte[4]);
            
            target = bos.toByteArray();
            //bos.reset();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("[PlaceholderChunk] Error whist generating pre-defined chunk! ");
        }
        FULL_GLASS = target;
    }
}
