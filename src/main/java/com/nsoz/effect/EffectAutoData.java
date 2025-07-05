package com.nsoz.effect;

import com.nsoz.model.Frame;
import com.nsoz.model.ImageInfo;
import lombok.Getter;
import lombok.Setter;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

@Getter
@Setter
public class EffectAutoData {

    private short id;
    private ImageInfo[] imgInfo;
    private Frame[] frameEffAuto;
    private short[] frameRunning;
    private byte[] data;

    public void setData() {
        try {
            ByteArrayOutputStream bas = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(bas);
            dos.writeByte(imgInfo.length);
            for (ImageInfo img : imgInfo) {
                dos.writeByte(img.id);
                dos.writeByte(img.x);
                dos.writeByte(img.y);
                dos.writeByte(img.w);
                dos.writeByte(img.h);
            }
            dos.writeShort(frameEffAuto.length);
            for (Frame frame : frameEffAuto) {
                dos.writeByte(frame.idImg.length);
                for (int j = 0; j < frame.idImg.length; j++) {
                    dos.writeShort(frame.dx[j]);
                    dos.writeShort(frame.dy[j]);
                    dos.writeByte(frame.idImg[j]);
                }
            }
            dos.writeShort(frameRunning.length);
            for (short index : frameRunning) {
                dos.writeShort(index);
            }
            dos.flush();
            data = bas.toByteArray();
            dos.close();
            bas.close();
        } catch (Exception ex) {
            Logger.getLogger(EffectAutoData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
